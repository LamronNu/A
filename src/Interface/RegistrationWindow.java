package Interface;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

class RegistrationWindow extends Window {
    private TextField loginField;
    private PasswordField passwordField;
    private TextField firstNameField;
    private TextField secondNameField;

    public RegistrationWindow() {
        super("Registration"); // Set window caption
        center();
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // form
        content.setCaption("Registration");
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
        secondNameField = new TextField("Second name");
        secondNameField.setWidth("100%");
        fieldsPanel.addComponent(secondNameField);

        ////buttons////
        //ok
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        buttonsPanel.setMargin(true);
        buttonsPanel.setWidth("100%");
        Button btnOk = new Button("Register");
        Button.ClickListener okOnClick = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        };
        btnOk.addClickListener(okOnClick);
        buttonsPanel.addComponent(btnOk);
        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
        //cancel
        Button btnCancel = new Button("Cancel",new Button.ClickListener() {
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
            Notification.show(e.getMessage());
        }
    }
}
