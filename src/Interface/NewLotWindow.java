package Interface;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.util.Date;

class NewLotWindow extends Window {
    public TextField lotNameField;
    private PasswordField passwordField;
    private TextField firstNameField;
    private TextField secondNameField;

    public NewLotWindow() {
        super("New lot"); // Set window caption
        center();
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // form
        content.setCaption("New lot");
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        /////////
        //lot
        Layout fieldsPanel = new FormLayout();
        TextField lotNameField = new TextField("Lot name");
        lotNameField.addValidator(new StringLengthValidator(
                "The name must be al list 3 letters (was {0})", 3, 10, true));
        lotNameField.setImmediate(true);
        lotNameField.setWidth("100%");
        fieldsPanel.addComponent(lotNameField);
        //finish date
        DateField finishDateField = new DateField("Finish date");
//        DateRangeValidator dateRangeValidator
        finishDateField.addValidator(new DateRangeValidator(
                "The finish date must be not earlier than today (was {0})", new Date(), null, Resolution.MINUTE));//validation
        finishDateField.setImmediate(true);
        finishDateField.setWidth("100%");
        finishDateField.setValue(new Date());
        finishDateField.setDateFormat("dd-MM-yyyy hh:mm:ss");
        fieldsPanel.addComponent(finishDateField);
        //start price
        TextField startPriceField = new TextField("Start price", new ObjectProperty<Double>(1.00));
        startPriceField.addValidator(new DoubleRangeValidator(
                "The start price must be at least 1$ (was {0})", 1., Double.MAX_VALUE));//validation
        startPriceField.setImmediate(true);
        startPriceField.setWidth("100%");
        fieldsPanel.addComponent(startPriceField);
        //description
        TextArea descriptionField = new TextArea("Description");
        descriptionField.setWidth("100%");
        descriptionField.setHeight("50px");
        fieldsPanel.addComponent(descriptionField);

        //buttons
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

//        try {
//            logNameField.validate();
//            passwordField.validate();
//            firstNameField.validate();
//        } catch (InvalidValueException e) {
//            Notification.show(e.getMessage());
//        }
    }
}
