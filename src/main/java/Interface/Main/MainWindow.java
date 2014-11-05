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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Theme("auction")
public class MainWindow extends Window implements BasicWindow {

    private static final Logger log = Logger.getLogger(MainWindow.class);
    private final String SPACES_STRING = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private final AuctionWs auction;
    private final String userName;
    private final Table lotsTable = new Table();
    private final Button btnAddNewBid;
    private final Button btnCancelTrades;
    //private final float cancelTradesButtonSize;
    private HorizontalLayout lotDetailsLayout;
    private VerticalLayout bidsFrame;
    private VerticalLayout lotsFrame;

    public final UI parentWindow;
    private int userId;
    private String resultType;
    private List<Lot> lots;
    private Lot currentLot;
    private boolean IsSelectedLot;
    //private boolean IsTableLock = true;
    private Table bidsTable = new Table();
    private List<Bid> bids;
    private VerticalLayout lotDetailsValuesPanel;
    private SimpleDateFormat dateFormat;
    //private float remainingTimeLabelSize;

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
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        //content.setSizeFull();
        content.setSizeUndefined();
//        content.setWidth(100, Unit.PERCENTAGE);
        setContent(content);
        //content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            // form layouts
            HorizontalLayout mainFrame = new HorizontalLayout();
            //mainFrame.setSizeFull();
            //mainFrame.setHeight(100, Unit.PERCENTAGE);
            //mainFrame.setWidth(100, Unit.PERCENTAGE);//.setSizeUndefined();
                //--------LEFT FRAME---
                FormLayout leftFrame = new FormLayout();
                leftFrame.setHeight(100, Unit.PERCENTAGE);
                leftFrame.setWidth(40, Unit.PERCENTAGE);
                    //------TOP INFO----
                    HorizontalLayout auctionFrame = new HorizontalLayout();
                    auctionFrame.setHeight(50, Unit.PIXELS);
                    auctionFrame.setWidth(100, Unit.PERCENTAGE);

                        Label auctionLabel = new Label("<H1>Auction</H1>" + SPACES_STRING, ContentMode.HTML);
                        auctionLabel.setWidth(100, Unit.PERCENTAGE);//SizeFull();//Undefined();
                    auctionFrame.addComponent(auctionLabel);
                    auctionFrame.setComponentAlignment(auctionLabel, Alignment.MIDDLE_LEFT);
                    //leftFrame.setSpacing(true);
                leftFrame.addComponent(auctionFrame);


                //--LOTS
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

                leftFrame.addComponent(lotsPanel);
                leftFrame.setComponentAlignment(lotsPanel,Alignment.BOTTOM_RIGHT);

            mainFrame.addComponent(leftFrame);

                    ///// ----RIGHT FRAME-----
                FormLayout rightFrame = new FormLayout();
                rightFrame.setHeight(100, Unit.PERCENTAGE);
                //rightFrame.setWidthUndefined();//(60, Unit.PERCENTAGE);

                    //about user
                    HorizontalLayout userInfoLayout = new HorizontalLayout();
                    userInfoLayout.setHeight(50, Unit.PIXELS);
                    userInfoLayout.setWidthUndefined();//(100, Unit.PERCENTAGE);
                        Label userNameLabel = new Label("<b>User: </b>" + userName + SPACES_STRING, ContentMode.HTML);
                    userInfoLayout.addComponent(userNameLabel);
                    userInfoLayout.setComponentAlignment(userNameLabel, Alignment.TOP_LEFT);
                        Button logoutButton = new Button("Logout",new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                onLogout();
                            }
                        });
                        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
                    userInfoLayout.addComponent(logoutButton);
                    userInfoLayout.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);
                    userInfoLayout.setSpacing(true);

                rightFrame.addComponent(userInfoLayout);
                rightFrame.setComponentAlignment(userInfoLayout, Alignment.TOP_RIGHT);

                    //----LOT INFO (DETAILS)---
                    HorizontalLayout lotInfoFrame = new HorizontalLayout();
                    //lotInfoFrame.setHeight(50, Unit.PERCENTAGE);
                    //lotInfoFrame.setWidth(100, Unit.PERCENTAGE);
                    //lotInfoFrame.setCaption("Lot details");
                    lotInfoFrame.setMargin(true);
                    //lot details
                    lotDetailsLayout = new HorizontalLayout();
                    lotDetailsLayout.setHeight(100, Unit.PERCENTAGE);
                    //lotDetailsLayout.setWidth(150, Unit.POINTS);
                        initLotDetails();
                        fillLotDetails();
                    lotInfoFrame.addComponent(lotDetailsLayout);
                    lotInfoFrame.setComponentAlignment(lotDetailsLayout, Alignment.BOTTOM_LEFT);
                    //lot buttons
                    VerticalLayout lotButtonsFrame = new VerticalLayout();
                    lotButtonsFrame.setHeight(100, Unit.PERCENTAGE);
                    //lotButtonsFrame.setWidth(150, Unit.POINTS);

                        btnCancelTrades = new Button("Cancel trades",new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                onCancelTrades();
                            }
                        });
                        btnCancelTrades.setEnabled(false);
                        lotButtonsFrame.addComponent(btnCancelTrades);

                    lotInfoFrame.addComponent(lotButtonsFrame);
                    lotInfoFrame.setComponentAlignment(lotButtonsFrame, Alignment.BOTTOM_RIGHT);

                    //lot details panel
                    Panel lotDetailsPanel = new Panel();
                    lotDetailsPanel.setCaption("Lot details");
                    lotDetailsPanel.setContent(lotInfoFrame);
                    lotDetailsPanel.setWidth(100, Unit.PERCENTAGE);//Undefined();
                rightFrame.addComponent(lotDetailsPanel);

                    //--BIDS INFO-----
                    bidsFrame = new VerticalLayout();
                    //bidsFrame.setHeight(50, Unit.PERCENTAGE);
                    bidsFrame.setWidth(100, Unit.PERCENTAGE);
                    bidsFrame.setMargin(new MarginInfo(true, true, false, true));
                    //bidsFrame.setCaption("Bids");
                        initBidsTable();
                    bidsFrame.addComponent(bidsTable);
                    bidsFrame.setComponentAlignment(bidsTable, Alignment.TOP_CENTER);
                        //bid button
                        btnAddNewBid = new Button("New bid", new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent clickEvent) {
                                onAddNewBid();
                            }
                        });
//                    updateButtonEnabled();
                    btnAddNewBid.setEnabled(false);
                    bidsFrame.addComponent(btnAddNewBid);
                    bidsFrame.setComponentAlignment(btnAddNewBid, Alignment.BOTTOM_RIGHT);
                    //fillBidsTable();//on prepare lot is unselected
                    //bids panel
                    Panel bidsPanel = new Panel();
                    bidsPanel.setCaption("Bids");
                    bidsPanel.setContent(bidsFrame);
                    bidsPanel.setWidth(100, Unit.PERCENTAGE);//Undefined();
                rightFrame.addComponent(bidsPanel);
                //rightFrame.setMargin(new MarginInfo(false,false,false,true));
            //auctionFrame.addComponent(rightFrame);

            mainFrame.addComponent(rightFrame);

        content.addComponent(mainFrame);

    }

    private void initBidsTable() {
        log.info("init bids table");

        // column captions
        bidsTable.addContainerProperty("Bid", Double.class, null);
        bidsTable.addContainerProperty("Date", Date.class, null);
        bidsTable.addContainerProperty("Bidder", String.class, null);
        //temp
        //bidsTable.addContainerProperty("lotStartPrice", Double.class, null);
        //bidsTable.addContainerProperty("lotMaxBidValue", Double.class, null);
        // Allow the user to collapse and uncollapse columns
//        bidsTable.setColumnCollapsingAllowed(true);
//        bidsTable.setColumnCollapsed("lotStartPrice", true);
//        bidsTable.setColumnCollapsed("lotMaxBidValue", true);


        bidsTable.setPageLength(6);
        bidsTable.setWidth(100, Unit.PERCENTAGE);//Undefined();
    }

    private void fillBidsTable() {
        log.info("refresh bids for lot: " + (IsSelectedLot ?currentLot.getName() : Consts.PLEASE_SELECT_LOT_MESSAGE));
        //IsTableLock = true;
        bidsTable.removeAllItems();//clear before filling
        if (IsSelectedLot) {
            //get bids
            bids = new ArrayList<Bid>();//??
            bids = auction.getAllBidsForLot(currentLot.getId());
            //records
            int i = 0;
            for (Bid bid : bids) {
                bidsTable.addItem(
                        new Object[]{bid.getValue(), bid.getCreatedOnDate(), bid.getOwnerName()
                                //, bid.getLotStartPrice(), bid.getLotMaxBidValue()
                        }, i++);
                // lotsTable.setStyleName(lot.getOwnerId() == userId ? "another" : "normal");

            }
        }

        updateButtonEnabled();
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
                                + SPACES_STRING, ContentMode.HTML);
        lotDetailsValuesPanel.addComponent(valueLabel);
//start price
        valueLabel = new Label(IsSelectedLot ? (Double.toString(currentLot.getStartPrice()) + " $" ): Consts.EMPTY_STR);
        lotDetailsValuesPanel.addComponent(valueLabel);

        ///debug
        //log.debug(currentLot.toString());
    }

    private void initLotDetails() { //on prepare

        //lotDetailsLayout.removeAllComponents();//remove current info
        lotDetailsLayout.setMargin(new MarginInfo(false, true, false, false));
        //generate labels
        //
        VerticalLayout captionsPanel = new VerticalLayout();
        captionsPanel.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        captionsPanel.setWidth(25, Unit.PERCENTAGE);//SizeUndefined();//Full();
        captionsPanel.setHeight(100,Unit.PERCENTAGE);
        lotDetailsValuesPanel = new VerticalLayout();
        lotDetailsValuesPanel.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        lotDetailsValuesPanel.setWidth(75, Unit.PERCENTAGE);//.setSizeFull();
        lotDetailsValuesPanel.setHeight(100,Unit.PERCENTAGE);

//code
        Label captionLabel = new Label("Code:" + SPACES_STRING, ContentMode.HTML);
        Label valueLabel = new Label();//Integer.toString(currentLot.getId()));
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//name
        captionLabel = new Label("Name:" + SPACES_STRING, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getName());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//state
        captionLabel = new Label("State:" + SPACES_STRING, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getState());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//finish date
        captionLabel = new Label("Finish date:" + SPACES_STRING, ContentMode.HTML);
        //DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        valueLabel = new Label();//df.format(currentLot.getFinishDate()));
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//owner
        captionLabel = new Label("Owner:" + SPACES_STRING, ContentMode.HTML);
        valueLabel = new Label();//currentLot.getOwnerName());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//remaining time
        captionLabel = new Label("Remaining time:" + SPACES_STRING, ContentMode.HTML);
        //remainingTimeLabelSize = captionLabel.getWidth();
        valueLabel = new Label();//currentLot.getRemainingTime());
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//description
        captionLabel = new Label("Description:" + SPACES_STRING, ContentMode.HTML);
        valueLabel = new Label();//description+ SPACES_STRING, ContentMode.HTML);
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);
//start price
        captionLabel = new Label("Start price:" + SPACES_STRING, ContentMode.HTML);
        valueLabel = new Label();//Double.toString(currentLot.getStartPrice()) + " $");
        captionsPanel.addComponent(captionLabel);
        lotDetailsValuesPanel.addComponent(valueLabel);

        lotDetailsLayout.addComponent(captionsPanel);
        lotDetailsLayout.addComponent(lotDetailsValuesPanel);
    }

    private void fillLotsTable() {
        log.info("refresh lots table");
        //IsTableLock = true;
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
                //if (!IsTableLock){
                //    IsTableLock = true;
                    fillLotDetails();
                    fillBidsTable();
                //    IsTableLock = false;
                //}
            }
        });
       // IsTableLock = false;
    }

    private void updateButtonEnabled() {
        boolean CurrentUserIsLotOwner = currentLot.getOwnerId() == userId;
        boolean LotIsActive = Consts.ACTIVE_LOT_STATE.equals(currentLot.getState());
        btnAddNewBid.setEnabled(!CurrentUserIsLotOwner && LotIsActive);
        btnCancelTrades.setEnabled(CurrentUserIsLotOwner && LotIsActive);
    }

    private void onLogout() {
        LoginWindow wnd = new LoginWindow(this.parentWindow);
        getCurrent().addWindow(wnd);
        log.info("close Main window");
        close();
    }



    private void onCancelTrades() {
        if (!IsSelectedLot) {
            log.warn("onCancelTrades: please, select the lot!");
            showInformation("please, select the lot!");
            return;
        }
        log.info("cancel trades for lot: " + currentLot.getName());
        try {
            auction.cancelLotTrades(currentLot.getId(), userId);
        } catch (LotUpdateException e) {
            showInformation(e.getMessage());
            log.warn(e.getMessage());
            return;
        }
        showInformation("Lot: " + currentLot.getName() + " is successfully cancelled");
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
            IsSelectedLot = false;
            fillLotsTable();
            fillLotDetails();
            fillBidsTable();
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
