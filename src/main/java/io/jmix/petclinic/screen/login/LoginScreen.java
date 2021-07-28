package io.jmix.petclinic.screen.login;

import com.vaadin.ui.Dependency;
import io.jmix.core.MessageTools;
import io.jmix.core.Messages;
import io.jmix.securityui.authentication.AuthDetails;
import io.jmix.securityui.authentication.LoginScreenSupport;
import io.jmix.ui.JmixApp;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.CheckBox;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.Image;
import io.jmix.ui.component.MessageDialogFacet;
import io.jmix.ui.component.PasswordField;
import io.jmix.ui.component.RelativePathResource;
import io.jmix.ui.component.TextField;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.ScreenDependencyUtils;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import io.jmix.ui.security.UiLoginProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import java.util.Locale;

@UiController("petclinic_LoginScreen")
@UiDescriptor("login-screen.xml")
@Route(path = "login", root = true)
public class LoginScreen extends Screen {

    @Autowired
    private Image logoImage;

    @Autowired
    private TextField<String> usernameField;

    @Autowired
    private PasswordField passwordField;

    @Autowired
    private CheckBox rememberMeCheckBox;

    @Autowired
    private ComboBox<Locale> localesField;

    @Autowired
    private Notifications notifications;

    @Autowired
    private Messages messages;

    @Autowired
    private MessageTools messageTools;

    @Autowired
    private LoginScreenSupport loginScreenSupport;

    @Autowired
    private UiLoginProperties loginProperties;

    @Autowired
    private JmixApp app;

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MessageDialogFacet helpDialog;

    @Subscribe
    private void onInit(InitEvent event) {
        usernameField.focus();
        initLogoImage();
        initLocalesField();
        initDefaultCredentials();
        loadStyles();
    }

    private void initLocalesField() {
        localesField.setOptionsMap(messageTools.getAvailableLocalesMap());
        localesField.setValue(app.getLocale());
    }

    private void initDefaultCredentials() {
        String defaultUsername = loginProperties.getDefaultUsername();
        if (!StringUtils.isBlank(defaultUsername) && !"<disabled>".equals(defaultUsername)) {
            usernameField.setValue(defaultUsername);
        } else {
            usernameField.setValue("");
        }

        String defaultPassword = loginProperties.getDefaultPassword();
        if (!StringUtils.isBlank(defaultPassword) && !"<disabled>".equals(defaultPassword)) {
            passwordField.setValue(defaultPassword);
        } else {
            passwordField.setValue("");
        }
    }

    @Subscribe("submit")
    private void onSubmitActionPerformed(Action.ActionPerformedEvent event) {
        login();
    }

    private void login() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(messages.getMessage(getClass(), "emptyUsernameOrPassword"))
                    .show();
            return;
        }

        try {
            loginScreenSupport.authenticate(
                    AuthDetails.of(username, password)
                            .withLocale(localesField.getValue())
                            .withRememberMe(rememberMeCheckBox.isChecked()), this);
        } catch (BadCredentialsException | DisabledException e) {
            log.info("Login failed", e);
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption(messages.getMessage(getClass(), "loginFailed"))
                    .withDescription(messages.getMessage(getClass(), "badCredentials"))
                    .show();
        }
    }

    private void loadStyles() {
        ScreenDependencyUtils.addScreenDependency(this,
                "vaadin://brand-login-screen/login.css", Dependency.Type.STYLESHEET);
    }

    private void initLogoImage() {
        logoImage.setSource(RelativePathResource.class)
                .setPath("VAADIN/brand-login-screen/petclinic_logo.svg");
    }


    @Subscribe("helpBtn")
    protected void onHelpBtnClick(Button.ClickEvent event) {
        helpDialog.show();
    }
}