package Interface.Authentication;

import Interface.BasicWindow;
import Interface.InformationDialog;
import Interface.Main.MainWindow;
import Library.Exceptions.LoginIsAlreadyExistsException;
import WebService.General.AuctionWs;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

class RegistrationWindow extends Window implements BasicWindow {
    private static final Logger log = Logger.getLogger(RegistrationWindow.class);
    private final TextField loginField;
    private final PasswordField passwordField;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final Button btnCancel;
    private final Button btnOk;
    private final UI parentWindow;
    private int newUserId;
    private String resultType;

    public RegistrationWindow(UI components) {
        super("Registration"); // Set window caption
        log.info("Start Registration window");
        center();
        this.parentWindow = components;
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // form
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        /////fields////
        //login
        Layout fieldsPanel = new FormLayout();
        loginField = new TextField("Login");
        loginField.addValidator(new StringLengthValidator(
                "The name must be 3-10 letters (was {0})", 3, 10, true));//validation
        loginField.setImmediate(true);
        loginField.setWidth("100%");
        fieldsPanel.addComponent(loginField);
        //pwd
        passwordField = new PasswordField("Password");
        passwordField.addValidator(new StringLengthValidator(
                "The password must be at least 4 letters (was {0})", 4, -1, true));//validation
        passwordField.setImmediate(true);
        passwordField.setWidth("100%");
        fieldsPanel.addComponent(passwordField);
        //1st name
        firstNameField = new TextField("First name");
        firstNameField.addValidator(new StringLengthValidator(
                "The First name must be not null (was {0})", 1, -1, true));//validation
        firstNameField.setImmediate(true);
        firstNameField.setWidth("100%");
        fieldsPanel.addComponent(firstNameField);
        //2nd name
        lastNameField = new TextField("Last name");
        lastNameField.setWidth("100%");
        fieldsPanel.addComponent(lastNameField);

        ////buttons////
        //ok
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        buttonsPanel.setMargin(true);
        buttonsPanel.setWidth("100%");
        btnOk = new Button("Register");
        Button.ClickListener okOnClick = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        };
        btnOk.addClickListener(okOnClick);
        btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER); //enter
        buttonsPanel.addComponent(btnOk);
        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
        //cancel
        btnCancel = new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onCancel();
            }
        });
        buttonsPanel.addComponent(btnCancel);
        buttonsPanel.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);
        //total
        content.addComponent(fieldsPanel);
        content.addComponent(buttonsPanel);
    }

    private void onCancel() {
        this.close();
    }

    private void onOk() {
        //System.exit(0);
        //1/ validate

        try {
            loginField.validate();
            passwordField.validate();
            firstNameField.validate();
        } catch (Validator.InvalidValueException e) {
            //Notification.show(e.getMessage());
            log.warn("Catch Exception: " + e.getMessage());
            return;//???
        }
        //2.register
        //field values
        String lgn = loginField.getValue();
        String pwd = passwordField.getValue(); //.toString();Arrays.toString(.getPassword());
        String fName = firstNameField.getValue();
        String lName = lastNameField.getValue();
        String message = "";
        try {
            AuctionWs auction = Authentication.getAuctionWebService();
            this.newUserId = auction.createNewUser(lgn, pwd, fName, lName);
            message = "user [" + lgn + "] is registered!";
            this.resultType = "Success";

        } catch (LoginIsAlreadyExistsException /*| MalformedURLException*/ e) {
            message = "user [" + lgn
                    + "] is not registered (cause: " + e.getMessage() + ")";
            this.resultType = "Failure";
            // Dialog dialog = new InformationDialog(this, message, false);
        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
        }
        log.info(message);
        Window dialog = new InformationDialog(this, message);
        getCurrent().addWindow(dialog);

    }

    @Override
    public void onNotify(String message) {
        if (resultType == "Success") {
            MainWindow wnd = new MainWindow(this.parentWindow, newUserId);
            getCurrent().addWindow(wnd);
            this.close();//??
        }
        resultType = "";
    }

    @Override
    public UI getCurrent() {
        return this.parentWindow.getCurrent();
    }
}
