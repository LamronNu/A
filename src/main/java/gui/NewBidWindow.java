package gui;//package gui.main;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import util.Consts;
import ws.general.web.AuctionWebService;
import ws.model.Lot;
import ws.model.User;


public class NewBidWindow extends Window implements BasicWindow {
    private static final Logger log = Logger.getLogger(NewBidWindow.class);
    private final Label dollarLabel;
    private final Button btnOk;
    private final BasicWindow parentWindow;
    private final User owner;
    private final Lot lot;
    public TextField bidValueField;
    private String resultType = "";


    public NewBidWindow(BasicWindow components, User user, Lot lot) {
        super("New bid"); // Set window caption
        log.info("start Bid window for lot " + lot.getId());
        center();
        //fields
        parentWindow = components;
        owner = user;
        this.lot = lot;
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
        Double minBidValue = lot.getTotalLotPrice() + 1.0;
        bidValueField = new TextField("Bid", new ObjectProperty<Double>(minBidValue));
        bidValueField.setConverter(new StringToDoubleConverter());
        bidValueField.addValidator(new DoubleRangeValidator(
                "The min bid must be " + minBidValue + "$ (was {0}$)",
                minBidValue, Double.MAX_VALUE));//validation
        bidValueField.setImmediate(true);

        bidValueField.setWidth("100%");
        fieldsPanel.addComponent(bidValueField);
        //dollar label
        HorizontalLayout dollarPanel = new HorizontalLayout();
        dollarPanel.setMargin(new MarginInfo(false, true, false, false));
        dollarLabel = new Label("$   ");
        dollarLabel.setWidth("100%");
        //startPriceField.setWidth("100%");
        dollarPanel.addComponent(dollarLabel);
        dollarPanel.setComponentAlignment(dollarLabel, Alignment.MIDDLE_LEFT);
        //buttons
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        //ok
        btnOk = new Button("OK");
        Button.ClickListener okOnClick = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        };
        btnOk.addClickListener(okOnClick);
        btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER); //enter
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
        //1/ validate
        try {
            bidValueField.validate();
        } catch (Validator.InvalidValueException e) {
            log.warn(e.getMessage());
            showInformation(e.getMessage());
            return;
        }
        //save
        //field values
        Double bidValue = Double.parseDouble(bidValueField.getConvertedValue().toString());// new Double(startPriceField.getValue());
        log.info("try to create new bid " + bidValue);
        String message = "";
        try {
            AuctionWebService auction = Authentication.getAuctionWebService();
            boolean IsCreated = auction.createNewBid(bidValue, owner, lot);
            if (IsCreated) {
                message = "bid [" + bidValue + "] is created!";
                log.info(message);
            }
            resultType = IsCreated ? "Success" : "Failure";

        } catch (Exception e) {
            message = e.getMessage();
            log.error(message);
        }
        showInformation(message);
    }

    @Override
    public void onNotify(String message) {
        if (resultType == "Success") {
            log.info("close bid window");
            this.close();//??
            this.parentWindow.onNotify(Consts.REFRESH_BIDS_MESSAGE);
        }
        resultType = "";
    }

    private void showInformation(String msg) {
        InformationDialog wnd = new InformationDialog(this, msg);
        getCurrent().addWindow(wnd);
    }

    public UI getCurrent() {
        return parentWindow.getCurrent();
    }
}
