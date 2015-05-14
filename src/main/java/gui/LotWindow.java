package gui;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import util.Consts;
import ws.general.AuctionWs;
import ws.model.Lot;

import java.util.Date;

public class LotWindow extends Window implements BasicWindow {
    private static final Logger log = Logger.getLogger(LotWindow.class);
    private final BasicWindow parentWindow;
    private final int ownerId;
    private final boolean IsNewLot;
    private final Lot lot;
    //private final AuctionWs auction;
    public TextField lotNameField;
    private DateField finishDateField;
    private TextField startPriceField;
    private TextArea descriptionField;
    private Button btnOk;
    private Button btnCancel;
    private String resultType;

    public LotWindow(BasicWindow components, int ownerId, Lot lot) {
        super("New lot"); // Set window caption
        center();
        //
        this.parentWindow = components;
        this.ownerId = ownerId;
        IsNewLot = lot == null;
        this.lot = IsNewLot ? new Lot() : lot;
        //
        setCaption(IsNewLot ? "New lot" : "Edit lot " + lot.getId());
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // form

        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        /////////
        //lot
        Layout fieldsPanel = new FormLayout();
        lotNameField = new TextField("Lot name");
        lotNameField.addValidator(new StringLengthValidator(
                "The name must be al list 3 letters (was {0})", 3, Integer.MAX_VALUE, true));
        lotNameField.setImmediate(true);
        lotNameField.setWidth("100%");
        lotNameField.setValue(IsNewLot ? "" : lot.getName());
        fieldsPanel.addComponent(lotNameField);
        //finish date
        finishDateField = new DateField("Finish date");
//        DateRangeValidator dateRangeValidator
        finishDateField.addValidator(new DateRangeValidator(
                "The finish date must be not earlier than today (was {0})", new Date(), null, Resolution.MINUTE));//validation
        finishDateField.setImmediate(true);
        finishDateField.setResolution(Resolution.MINUTE);
        finishDateField.setWidth("100%");
        finishDateField.setDateFormat(Consts.DATE_FORMAT);
        finishDateField.setValue(IsNewLot ? new Date() : lot.getFinishDate());
        fieldsPanel.addComponent(finishDateField);
        //start price
        startPriceField = new TextField("Start price", new ObjectProperty<Double>(IsNewLot ? 1.00 : lot.getStartPrice()));
        startPriceField.addValidator(new DoubleRangeValidator(
                "The start price must be at least 1$ (was {0}$)", 1., Double.MAX_VALUE));//validation
        startPriceField.setImmediate(true);
        startPriceField.setWidth("100%");
        fieldsPanel.addComponent(startPriceField);
        //description
        descriptionField = new TextArea("Description");
        descriptionField.setWidth("100%");
        descriptionField.setHeight("50px");
        descriptionField.setValue(IsNewLot ? "" : lot.getDescription());
        fieldsPanel.addComponent(descriptionField);

        //buttons
        //ok
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        buttonsPanel.setMargin(true);
        buttonsPanel.setWidth("100%");
        btnOk = new Button(IsNewLot ? "Create" : "Edit");
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
            lotNameField.validate();
            finishDateField.validate();
            startPriceField.validate();
        } catch (Validator.InvalidValueException e) {
            //Notification.show(e.getMessage());
            log.warn(e.getMessage());
            return;
        }
        //save

        //field values
        String lotName = lotNameField.getValue();
        Date finishDate = finishDateField.getValue(); //.toString();Arrays.toString(.getPassword());
        Double startPrice = Double.parseDouble(startPriceField.getConvertedValue().toString());// new Double(startPriceField.getValue());
        String description = descriptionField.getValue();
        lot.setName(lotName);
        lot.setFinishDate(finishDate);
        lot.setStartPrice(startPrice);
        lot.setDescription(description);
        lot.setOwnerId(ownerId);
        String message = "";
        try {
            AuctionWs auction = Authentication.getAuctionWebService();
            boolean IsSuccess = IsNewLot ? auction.createNewLot(lot)//lotName, finishDate, startPrice, description, ownerId)
                    : auction.updateLot(lot);
            //this.newUserId = auction.CreateNewUser(lgn,pwd,fName,lName);
            if (IsSuccess) {
                message = "lot [" + lotName + "] is successfully " + (IsNewLot ? "created" : "updated") + "!";
                log.info(message);
            }
            this.resultType = IsSuccess ? "Success" : "Failure";

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
        }
        Window dialog = new InformationDialog(this, message);
        getCurrent().addWindow(dialog);

        //log.info(message);

    }

    @Override
    public void onNotify(String message) {
        if (resultType == "Success") {
            this.parentWindow.onNotify(Consts.REFRESH_LOTS_MESSAGE);
            this.close();//??
        }
        resultType = "";
    }

    @Override
    public UI getCurrent() {
        return this.parentWindow.getCurrent();
    }
}
