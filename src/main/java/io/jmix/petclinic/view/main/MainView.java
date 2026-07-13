package io.jmix.petclinic.view.main;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.facet.Timer;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.online.OnlineDemoDataCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private UiComponents uiComponents;

    @ViewComponent
    private MessageBundle messageBundle;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Messages messages;

    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;

    @Autowired(required = false)
    private OnlineDemoDataCreator onlineDemoDataCreator;

    @ViewComponent
    private JmixListMenu menu;

    @Subscribe
    public void onInit(final InitEvent event) {
        initMyVisitBadge();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (onlineDemoDataCreator != null) {
            onlineDemoDataCreator.createDemoData();
        }
    }

    /*@Subscribe("refreshMyVisitsBadge")
    public void onRefreshMyVisitsBadgeTimerAction(final Timer.TimerActionEvent event) {
        ListMenu.MenuItem menuItem = menu.getMenuItem("petclinic_MyVisits");

        if (menuItem != null && menuItem.getSuffixComponent() instanceof Span badge) {
            badge.setText(messageBundle.formatMessage("myVisitMenuItemBadge.text", calculateAmountOfVisits()));
        }
    }*/

    @Install(to = "userMenu", subject = "buttonRenderer")
    private Component userMenuButtonRenderer(final UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            return null;
        }

        String userName = generateUserName(user);

        Div content = uiComponents.create(Div.class);
        content.setClassName("user-menu-button-content");

        Avatar avatar = createAvatar(userName);

        Span name = uiComponents.create(Span.class);
        name.setText(userName);
        name.setClassName("user-menu-text");

        content.add(avatar, name);

        if (isSubstituted(user)) {
            Span subtext = uiComponents.create(Span.class);
            subtext.setText(messages.getMessage("userMenu.substituted"));
            subtext.setClassName("user-menu-subtext");

            content.add(subtext);
        }

        return content;
    }

    @Install(to = "userMenu", subject = "headerRenderer")
    private Component userMenuHeaderRenderer(final UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            return null;
        }

        Div content = uiComponents.create(Div.class);
        content.setClassName("user-menu-header-content");

        String name = generateUserName(user);

        Avatar avatar = createAvatar(name);
        avatar.addThemeVariants(AvatarVariant.LUMO_LARGE);

        Span text = uiComponents.create(Span.class);
        text.setText(name);
        text.setClassName("user-menu-text");

        content.add(avatar, text);

        if (name.equals(user.getUsername())) {
            text.addClassNames("user-menu-text-subtext");
        } else {
            Span subtext = uiComponents.create(Span.class);
            subtext.setText(user.getUsername());
            subtext.setClassName("user-menu-subtext");

            content.add(subtext);
        }

        return content;
    }

    private Avatar createAvatar(String fullName) {
        Avatar avatar = uiComponents.create(Avatar.class);
        avatar.setName(fullName);
        avatar.getElement().setAttribute("tabindex", "-1");
        avatar.setClassName("user-menu-avatar");

        return avatar;
    }

    private String generateUserName(User user) {
        String userName = String.format("%s %s",
                        Strings.nullToEmpty(user.getFirstName()),
                        Strings.nullToEmpty(user.getLastName()))
                .trim();

        return userName.isEmpty() ? user.getUsername() : userName;
    }

    private boolean isSubstituted(User user) {
        UserDetails authenticatedUser = currentUserSubstitution.getAuthenticatedUser();
        return user != null && !authenticatedUser.getUsername().equals(user.getUsername());
    }

    private void initMyVisitBadge() {
        Span badge = uiComponents.create(Span.class);
        badge.setText(messageBundle.formatMessage("myVisitMenuItemBadge.text", calculateAmountOfVisits()));
        badge.getElement().getThemeList().add("badge warning");

        ListMenu.MenuItem menuItem = menu.getMenuItem("petclinic_MyVisits");
        if (menuItem != null) {
            menuItem.setSuffixComponent(badge);
        }
    }

    private long calculateAmountOfVisits() {
        return dataManager.loadValue("select count(e) from petclinic_Visit e " +
                                "where e.assignedNurse = :currentUser " +
                                "and e.treatmentStatus <> @enum(io.jmix.petclinic.entity.visit.VisitTreatmentStatus.DONE)",
                        Long.class)
                .parameter("currentUser", currentUserSubstitution.getEffectiveUser())
                .one();
    }
}
