package Interface.Main;

import Interface.Authentication.Authentication;
import Interface.Authentication.LoginWindow;
import Interface.BasicWindow;
import Interface.InformationDialog;
import Library.Consts;
import Library.Exceptions.LotUpdateException;
import WebService.Domain.Bid;
import WebService.Domain.Lot;
import WebService.General.AuctionWs;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Theme("auction")
public class MainWindow extends Window implements BasicWindow {

    private static final Logger log = Logger.getLogger(MainWindow.class);
    private final String spacesString = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private final AuctionWs auction;
    private final String userName;
    private final Table lotsTable = new Table();
    private final Button btnAddNewBid;
    private final Button btnCancelTrades;
    private HorizontalLayout lotDetailsPanel;
    private VerticalLayout bidsFrame;
    private VerticalLayout lotsFrame;

    public final UI parentWindow;
    private int userId;
    private String resultType;
    private List<Lot> lots;
    private Lot currentLot;
    private boolean IsSelectedLot;
    private boolean IsTableLock = true;
    private Table bidsTable = new Table();
    private List<Bid> bids;
    private VerticalLayout lotDetailsValuesPanel;
    private SimpleDateFormat dateFormat;

    public MainWindow(UI components, int userId) {
        super("Auction"); // Set window caption
        log.info("Start Main window");
        center();
        //init auction variables
        parentWindow = components;
        auction = Authentication.getAuctionWebService();
        this.userId = userId;
        userName = auction.getUserName(userId);
        dateFormat = new SimpleDateFormat(Consts.DATE_FORMAT);
        // Create form
        FormLayout content = new FormLayout();
        content.setMargin(true);
        //content.setSizeFull();
        content.setSizeUndefined();
//        content.setWidth(100, Unit.PERCENTAGE);
        setContent(content);
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            // form layouts
            VerticalLayout mainFrame = new VerticalLayout();
            mainFrame.setSizeFull();
            mainFrame.setWidth(100, Unit.PERCENTAGE);//.setSizeUndefined();
                //------TOP INFO----
                HorizontalLayout topFrame = new HorizontalLayout();
                topFrame.setHeight(50, Unit.PIXELS);
                topFrame.setSizeFull();//Width(100, Unit.PERCENTAGE);
                    Label auctionLabel = new Label("<H1>Auction</H1>", ContentMode.HTML);
                    auctionLabel.setWidth(100, Unit.PERCENTAGE);//SizeFull();//Undefined();
                topFrame.addComponent(auctionLabel);
                topFrame.setComponentAlignment(auctionLabel, Alignment.MIDDLE_LEFT);
                //topFrame.setSpacing(true);
                    HorizontalLayout userInfoPanel = new HorizontalLayout();
                        Label userNameLabel = new Label("User: " + userName);
                    userInfoPanel.addComponent(userNameLabel);
                        Button logoutButton = new Button("Logout",new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                onLogout();
                            }
                        });
                        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
                    userInfoPanel.addComponent(logoutButton);
                    userInfoPanel.setSpacing(true);
                    userInfoPanel.setHeight(100, Unit.PERCENTAGE);
                    userInfoPanel.setWidth(200, Unit.POINTS);
                topFrame.addComponent(userInfoPanel);
                topFrame.setComponentAlignment(userInfoPanel, Alignment.TOP_RIGHT);
                mainFrame.addComponent(topFrame);
                //mainFrame.setComponentAlignment(topFrame, Alignment.TOP_CENTER);
                //---MAIN SPACE-----
                HorizontalLayout middleFrame = new HorizontalLayout();
                middleFrame.setHeight(100, Unit.PERCENTAGE);
                middleFrame.setWidth(100, Unit.PERCENTAGE);
        //        middleFrame.setSizeFull();
                //middleFrame.setMargin(true);
                    lotsFrame = new VerticalLayout();
                    lotsFrame.setHeight(100, Unit.PERCENTAGE);
                    lotsFrame.setWidth(40, Unit.PERCENTAGE);
                    lotsFrame.setMargin(true);//new MarginInfo(false, true, false, false));
                    lotsFrame.setCaption("Lots");
                    //lotsFrame.setStyleName();
            //        lotsFrame.setVisible(true);

                        //----LOTS INFO (TABLE)
                        fillLotsTable();
                    lotsFrame.addComponent(lotsTable);
                    lotsFrame.setComponentAlignment(lotsTable, Alignment.TOP_CENTER);
                        Button btnAddNewLot = new Button("New lot",new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                onAddNewLot();
                            }
                        });
                    lotsFrame.addComponent(btnAddNewLot);
                    lotsFrame.setComponentAlignment(btnAddNewLot, Alignment.BOTTOM_RIGHT);
                //left panel
                Panel lotsPanel = new Panel();
                lotsPanel.setCaption("Lots");
                lotsPanel.setContent(lotsFrame);
                middleFrame.addComponent(lotsPanel);

                    /////----LOT INFO (DETAILS)---
                    VerticalLayout rightFrame = new VerticalLayout();
                    rightFrame.setHeight(100, Unit.PERCENTAGE);
                    rightFrame.setWidth(60, Unit.PERCENTAGE);
                        HorizontalLayout lotInfoFrame = new HorizontalLayout();
                        //lotInfoFrame.setHeight(50, Unit.PERCENTAGE);
                        //lotInfoFrame.setSizeFull();
                        lotInfoFrame.setHeight(50, Unit.PERCENTAGE);
                        lotInfoFrame.setWidth(100, Unit.PERCENTAGE);
                        lotInfoFrame.setCaption("Lot details");
                        lotInfoFrame.setMargin(true);
                        //lot details
                        lotDetailsPanel = new HorizontalLayout();
                            initLotDetails();
                            fillLotDetails();
                        lotInfoFrame.addComponent(lotDetailsPanel);
                        lotInfoFrame.setComponentAlignment(lotDetailsPanel, Alignment.BOTTOM_LEFT);
                        //lot buttons
                        VerticalLayout lotButtonsFrame = new VerticalLayout();
                        lotButtonsFrame.setHeight(100, Unit.PERCENTAGE);
                        lotButtonsFrame.setWidth(150, Unit.POINTS);
                            ///temp
//                            String[] themes = { "valo", "reindeer", "runo", "chameleon" };
//                            ComboBox themePicker = new ComboBox("Theme", Arrays.asList(themes));
//                            themePicker.setValue(parentWindow.getTheme());
//themePicker.setWidthUndefined();
//                            themePicker.addValueChangeListener(new Property.ValueChangeListener() {
//                                @Override
//                                public void valueChange(Property.ValueChangeEvent event) {
//                                    String theme = (String) event.getProperty().getValue();
//                                    parentWindow.setTheme(theme);
//                                }
//                            });
//                            lotButtonsFrame.addComponent(themePicker);
                            ///
                            btnCancelTrades = new Button("Cancel trades",new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent clickEvent) {
                                    onCancelTrades();
                                }
                            });
                            lotButtonsFrame.addComponent(btnCancelTrades);
                            Button btnFinishTrades = new Button("Finish trades",new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent clickEvent) {
                                    onFinishTrades();
                                }
                            });
                        lotButtonsFrame.addComponent(btnFinishTrades);
                        lotInfoFrame.addComponent(lotButtonsFrame);
                        lotInfoFrame.setComponentAlignment(lotButtonsFrame, Alignment.BOTTOM_RIGHT);
                        //lot details panel
                        Panel lotDetailssPanel = new Panel();
                        lotDetailssPanel.setCaption("Lot details");
                        lotDetailssPanel.setContent(lotInfoFrame);
        lotDetailssPanel.setWidthUndefined();
                    rightFrame.addComponent(lotDetailssPanel);

                        //--BIDS INFO-----
                        bidsFrame = new VerticalLayout();
                        bidsFrame.setHeight(50, Unit.PERCENTAGE);
                        bidsFrame.setWidth(100, Unit.PERCENTAGE);
                        bidsFrame.setMargin(true);//new MarginInfo(true, false, false, false));
                        bidsFrame.setCaption("Bids");
                            initBidsTable();
                            fillBidsTable();
                        bidsFrame.addComponent(bidsTable);
                        bidsFrame.setComponentAlignment(bidsTable, Alignment.TOP_CENTER);
                            //bid button
                            btnAddNewBid = new Button("New bid", new Button.ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent clickEvent) {
                                    onAddNewBid();
                                }
                            });
        //btnAddNewBid.setStyleName("blue");
                        updateButtonEnabled();
                        bidsFrame.addComponent(btnAddNewBid);
                        bidsFrame.setComponentAlignment(btnAddNewBid, Alignment.BOTTOM_RIGHT);
                        //bids panel
                        Panel bidsPanel = new Panel();
                        bidsPanel.setCaption("Bids");
                        bidsPanel.setContent(bidsFrame);
        bidsPanel.setWidthUndefined();
                    rightFrame.addComponent(bidsPanel);
                    rightFrame.setMargin(new MarginInfo(false,false,false,true));
                middleFrame.addComponent(rightFrame);

            mainFrame.addComponent(middleFrame);

        content.addComponent(mainFrame);

    }

    private void initBidsTable() {
        log.info("init bids table");
        //IsTableLock = true;
        //bidsTable.removeAllItems();//clear before filling

        // column captions
        bidsTable.addContainerProperty("Bid", Double.class, null);
        bidsTable.addContainerProperty("Date", Date.class, null);
        bidsTable.addContainerProperty("Bidder", String.class, null);
        //temp
        bidsTable.addContainerProperty("lotStartPrice", Double.class, null);
        bidsTable.addContainerProperty("lotMaxBidValue", Double.class, null);
        // Allow the user to collapse and uncollapse columns
        bidsTable.setColumnCollapsingAllowed(true);
//        bidsTable.setColumnCollapsed("lotStartPrice", true);
//        bidsTable.setColumnCollapsed("lotMaxBidValue", true);


        bidsTable.setPageLength(7);
        //footer
//        bidsTable.setFooterVisible(true);
//        // Add some total sum and description to footer
//        bidsTable.setColumnFooter("Finish date", "Total count");
//        bidsTable.setColumnFooter("State", Integer.toString(i));
    }

    private void fillBidsTable() {
        log.info("refresh bids for lot: " + (IsSelectedLot ?currentLot.getName() : Consts.PLEASE_SELECT_LOT_MESSAGE));
        //IsTableLock = true;
        bidsTable.removeAllItems();//clear before filling
        if (IsSelectedLot) {
            //get bids
            bids = null;//??
            bids = auction.getAllBidsForLot(currentLot.getId());
            //records
            int i = 0;
            for (Bid bid : bids) {
                bidsTable.addItem(
                        new Object[]{bid.getValue(), bid.getCreatedOnDate(), bid.getOwnerName()
                                , bid.getLotStartPrice(), bid.getLotMaxBidValue()}, i++);
                // lotsTable.setStyleName(lot.getOwnerId() == userId ? "another" : "normal");

            }
        }


    }

    private void fillLotDetails() {
        //get lot
        Object selectedValue = lotsTable.getValue();
        IsSelectedLot = selectedValue != null;
        currentLot =  IsSelectedLot ? lots.get((Integer) selectedValue) : new Lot();
        log.info("refresh lot details. Lot: " + (IsSelectedLot ?currentLot.getName() : Consts.PLEASE_SELECT_LOT_MESSAGE));
        lotDetailsValuesPanel.removeAllComponents();
//code
        Label valueLabel = new Label(IsSelectedLot ? Integer.toString(currentLot.getId()) : Consts.PLEASE_SELECT_LOT_MESSAGE);
        lotDetailsValuesPanel.addComponent(valueLabel);
//name
        valueLabel = new Label(IsSelectedLot ? currentLot.getName() : Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
//state
        valueLabel = new Label(IsSelectedLot ? currentLot.getState() : Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
//finish date
        valueLabel = new Label(IsSelectedLot ? dateFormat.format(currentLot.getFinishDate()) : Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
//owner
        valueLabel = new Label(IsSelectedLot ? currentLot.getOwnerName() : Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
//remaining time
        valueLabel = new Label(IsSelectedLot ? currentLot.getRemainingTime(): Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
//description
        String description = currentLot.getDescription();
        if (description == "" || description == null){
            description = "----";//???todo description
        }
        valueLabel = new Label((IsSelectedLot ? description : Consts.EMPTY_STR)
                                + spacesString, ContentMode.HTML);
        lotDetailsValuesPanel.addComponent(valueLabel);
//start price
        valueLabel = new Label(IsSelectedLot ? (Double.toString(currentLot.getStartPrice()) + " $" ): Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);
    }

    private void initLotDetails() { //on prepare

        //lotDetailsPanel.removeAllComponents();//remove current info
        lotDetailsPanel.setMargin(new MarginInfo(false,true,false,false));
        //generate labels
        //
        VerticalLayout captionsPanel = new VerticalLayout();
        captionsPanel.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        captionsPanel.setSizeFull();
        lotDetailsValuesPanel = new VerticalLayout();
        lotDetailsValuesPanel.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        lotDetailsValuesPanel.setSizeFull();

//code
        Label captionLabel = new Label("Code:" + spacesString, ContentMode.HTML);
        Label valueLabel = new Label();//Integer.toString(currentLot.getId()));
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//name
        captionLabel = new Label("Name:" + spacesString, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getName());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//state
        captionLabel = new Label("State:" + spacesString, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getState());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//finish date
        captionLabel = new Label("Finish date:" + spacesString, ContentMode.HTML);
        //DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        valueLabel = new Label();//df.format(currentLot.getFinishDate()));
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//owner
        captionLabel = new Label("Owner:" + spacesString, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getOwnerName());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//remaining time
        captionLabel = new Label("Remaining time:" + spacesString, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getRemainingTime());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//description
        captionLabel = new Label("Description:" + spacesString, ContentMode.HTML);
//        String description = currentLot.getDescription();
//        if (description == "" || description == null){
//            description = "----";//???
//        }
        valueLabel = new Label();//description+ spacesString, ContentMode.HTML);
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//start price
        captionLabel = new Label("Start price:" + spacesString, ContentMode.HTML);
        valueLabel = new Label();//Double.toString(currentLot.getStartPrice()) + " $");
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);

        lotDetailsPanel.addComponent(captionsPanel);
        lotDetailsPanel.addComponent(lotDetailsValuesPanel);
    }

    private void fillLotsTable() {
        log.info("refresh lots table");
        IsTableLock = true;
        lotsTable.removeAllItems();//clear before filling

        // column captions
        lotsTable.addContainerProperty("Code", Integer.class, null);
        lotsTable.addContainerProperty("Name", String.class, null);
        lotsTable.addContainerProperty("Finish date", Date.class, null);
        lotsTable.addContainerProperty("State", String.class, null);
        //get lots
        lots = auction.getAllLotsForUser(-1);
        //records
        int i = 0;
        for (Lot lot : lots) {
            lotsTable.addItem(
                    new Object[]{lot.getId(), lot.getName(), lot.getFinishDate(),lot.getState()}, i++);
           // lotsTable.setStyleName(lot.getOwnerId() == userId ? "another" : "normal");

        }
        lotsTable.setPageLength(15);
        //footer
        lotsTable.setFooterVisible(true);
        // Add some total sum and description to footer
        lotsTable.setColumnFooter("Finish date", "Total count");
        lotsTable.setColumnFooter("State", Integer.toString(i));
        //selectable
        lotsTable.setSelectable(true);
        lotsTable.setColumnCollapsingAllowed(true);
        // Handle selection change.
        lotsTable.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (!IsTableLock){
                    fillLotDetails();
                    fillBidsTable();
                    updateButtonEnabled();
                }
            }
        });
        IsTableLock = false;
    }

    private void updateButtonEnabled() {
        boolean CurrentUserIsLotOwner = currentLot.getOwnerId() != userId;
        boolean LotIsActive = currentLot.getState() == Consts.ACTIVE_LOT_STATE;
        btnAddNewBid.setEnabled(!CurrentUserIsLotOwner && LotIsActive);
        btnCancelTrades.setEnabled(CurrentUserIsLotOwner && LotIsActive);
    }

    private void onLogout() {
        LoginWindow wnd = new LoginWindow(this.parentWindow);
        getCurrent().addWindow(wnd);
        log.info("close Main window");
        close();
    }

    private void onFinishTrades() {
        if (!IsSelectedLot) {
            log.warn("please, select the lot!");
            showInformation("please, select the lot!");
            return;
        }
        log.info("finish trades for lot: " + currentLot.getName());
        try {
            auction.changeLotState(currentLot.getId(), userId, Consts.SOLD_LOT_STATE);
        } catch (LotUpdateException e) {
            showInformation(e.getMessage());
            return;
        }
        showInformation("finish trades for lot: " + currentLot.getName());
        fillLotsTable();//todo ??refresh only 1 record
    }

    private void onCancelTrades() {
        if (!IsSelectedLot) {
            log.warn("please, select the lot!");
            showInformation("please, select the lot!");
            return;
        }
        log.info("cancel trades for lot: " + currentLot.getName());
        try {
            auction.cancelLotTrades(currentLot.getId(), userId);
        } catch (LotUpdateException e) {
            showInformation(e.getMessage());
            return;
        }
        showInformation("cancel trades for lot: " + currentLot.getName());
        fillLotsTable();//todo ??refresh only 1 record
    }
    private void showInformation(String msg) {
        InformationDialog wnd = new InformationDialog(this, msg);
        getCurrent().addWindow(wnd);
    }
    private void onAddNewLot() {
        NewLotWindow wnd = new NewLotWindow(this, userId);
        getCurrent().addWindow(wnd);
    }
    private void onAddNewBid() {
        NewBidWindow wnd = new NewBidWindow(this, userId, currentLot.getId());
        getCurrent().addWindow(wnd);
    }

        @Override
    public void onNotify(String message) {
        if (message == Consts.REFRESH_LOTS_MESSAGE) {
            fillLotsTable();
        } else
        if (message == Consts.REFRESH_BIDS_MESSAGE) {
            fillBidsTable();
        }

    }

    @Override
    public UI getCurrent() {
       return this.parentWindow.getCurrent();
    }
}
