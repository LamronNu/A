package Interface;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

class NewBidWindow extends Window {
    public TextField bidValueField;


    public NewBidWindow() {
        super("New bid"); // Set window caption
        center();
        // Create the content root layout for the UI
        HorizontalLayout content = new HorizontalLayout();
        content.setMargin(true);
        setContent(content);
        // form
        //content.setCaption("New bid");
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        /////////
        //bid
        FormLayout fieldsPanel = new FormLayout();
        TextField bidValueField = new TextField("Bid", new ObjectProperty<Double>(1.1));
        bidValueField.setConverter(new StringToDoubleConverter());
        bidValueField.addValidator(new DoubleRangeValidator(
                "The min bid must be 1$ (was {0})", 1., Double.MAX_VALUE));//validation
        bidValueField.setImmediate(true);

        bidValueField.setWidth("100%");
        fieldsPanel.addComponent(bidValueField);
        //dollar label
        HorizontalLayout dollarPanel = new HorizontalLayout();
        dollarPanel.setMargin(new MarginInfo(false,true,false,false));
        Label dollarLabel = new Label("$   ");
        dollarLabel.setWidth("100%");
        //startPriceField.setWidth("100%");
        dollarPanel.addComponent(dollarLabel);
        dollarPanel.setComponentAlignment(dollarLabel, Alignment.MIDDLE_LEFT);
        //buttons
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        //ok

        Button btnOk = new Button("OK");
        Button.ClickListener okOnClick = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        };
        btnOk.addClickListener(okOnClick);
        btnOk.setWidth("80%");

        buttonsPanel.addComponent(btnOk);
        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_RIGHT);

//        //total
        content.addComponent(fieldsPanel);
        content.addComponent(dollarPanel);
        content.addComponent(buttonsPanel);




    }

   private void onCancel() {
        this.close();
    }

    private void onOk() {
        //System.exit(0);
        //1/ validate

        try {
            bidValueField.validate();
        } catch (Validator.InvalidValueException e) {
            Notification.show(e.getMessage());
        }
    }
}
