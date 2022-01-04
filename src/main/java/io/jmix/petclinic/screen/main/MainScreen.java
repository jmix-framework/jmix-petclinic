package io.jmix.petclinic.screen.main;

import com.vaadin.server.Page;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.screen.visit.MyVisits;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.ScreenTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.AppWorkArea;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Timer;
import io.jmix.ui.component.Window;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.component.mainwindow.SideMenu;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.OpenMode;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiControllerUtils;
import io.jmix.ui.screen.UiDescriptor;
import io.jmix.ui.theme.ThemeVariantsManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@UiController("petclinic_MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {

    @Autowired
    private ScreenTools screenTools;
    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private Drawer drawer;
    @Autowired
    private Button collapseDrawerButton;
    @Autowired
    protected SideMenu sideMenu;
    @Autowired
    protected DataManager dataManager;
    @Autowired
    protected CurrentAuthentication currentAuthentication;
    @Autowired
    protected ScreenBuilders screenBuilders;
    @Autowired
    protected ThemeVariantsManager heliumThemeVariantsManager;
    @Autowired
    protected Button switchThemeModeBtn;
    @Autowired
    protected MessageBundle messageBundle;

    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe("collapseDrawerButton")
    private void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();
    }

    @Subscribe
    protected void initMainMenu(AfterShowEvent event) {
        createMyVisitMenuItem();
        openPetclinicMenuItem();

        final PetclinicThemeSwitchBtnMode currentThemeMode = PetclinicThemeSwitchBtnMode
                .fromId(heliumThemeVariantsManager.getThemeModeUserSettingOrDefault());
        updateHeliumSwitchBtn(currentThemeMode);

        initMenuIcons();

    }

    private void initMenuIcons() {
        Stream.of(SideMenuIcon.values())
                .forEach(sideMenuIcon -> {

                    final SideMenu.MenuItem menuItem = sideMenu.getMenuItem(sideMenuIcon.getMenuId());

                    if (menuItem != null) {
                        menuItem.setIcon(sideMenuIcon.source());
                    }

                });
    }

    private void updateHeliumSwitchBtn(PetclinicThemeSwitchBtnMode mode) {
        switchThemeModeBtn.setIconFromSet(mode.getIcon());
        switchThemeModeBtn.setStyleName(mode.getStyleName());
    }


    private void openPetclinicMenuItem() {
        final SideMenu.MenuItem petclinicMenu = sideMenu.getMenuItem("application-petclinic");
        final SideMenu.MenuItem menuItem = petclinicMenu.getChildren().get(1);
        petclinicMenu.setExpanded(true);
        sideMenu.setSelectOnClick(true);
        sideMenu.setSelectedItem(menuItem);
    }

    private void createMyVisitMenuItem() {
        SideMenu.MenuItem myVisits = sideMenu.createMenuItem("myVisits");
        myVisits.setBadgeText(messageBundle.formatMessage("myVisitMenuItemBadge", amountOfVisits()));
        myVisits.setCaption(messageBundle.getMessage("myVisitsMenuItem"));
        myVisits.setIcon(JmixIcon.USER_CIRCLE.source());
        myVisits.setCommand(menuItem ->
                screenBuilders.screen(this)
                        .withScreenClass(MyVisits.class)
                        .withOpenMode(OpenMode.DIALOG)
                        .show()
        );
        sideMenu.addMenuItem(myVisits, 0);
    }

    private int amountOfVisits() {
        return dataManager.load(Visit.class)
                .query(
                        "e.assignedNurse = :currentUser and e.treatmentStatus <> @enum(io.jmix.petclinic.entity.visit.VisitTreatmentStatus.DONE)")
                .parameter("currentUser", currentAuthentication.getUser())
                .list().size();
    }

    @Subscribe("refreshMyVisits")
    protected void onRefreshMyVisitsTimerAction(Timer.TimerActionEvent event) {

        sideMenu.getMenuItem("myVisits")
                .setBadgeText(messageBundle.formatMessage("myVisitMenuItemBadge", amountOfVisits()));
    }

    @Subscribe("switchThemeMode")
    protected void onSwitchThemeMode(Action.ActionPerformedEvent event) {

        final PetclinicThemeSwitchBtnMode newTargetThemeMode = newTargetThemeMode();

        heliumThemeVariantsManager.setThemeMode(newTargetThemeMode.getName());
        Page.getCurrent().reload();
        updateHeliumSwitchBtn(newTargetThemeMode);
    }

    private PetclinicThemeSwitchBtnMode newTargetThemeMode() {

        return PetclinicThemeSwitchBtnMode.fromId(
                heliumThemeVariantsManager
                        .getThemeModeList()
                        .stream()
                        .filter(mode -> !mode.equals(
                                heliumThemeVariantsManager.getThemeModeUserSettingOrDefault())
                        )
                        .findFirst()
                        .orElse("light")
        );
    }

}
