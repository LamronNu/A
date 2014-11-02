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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Theme("auctionStyle")
public class MainWindow extends Window implements BasicWindow {

    private static final Logger log = Logger.getLogger(MainWindow.class);
    private final String spacesString = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private final AuctionWs auction;
    private final String userName;
    private final Table lotsTable = new Table();
    private HorizontalLayout lotDetailsPanel;
    private FormLayout bidsPanel;
    private VerticalLayout lotsPanel;

    public final UI parentWindow;
    private int userId;
    private String resultType;
    private List<Lot> lots;
    private Lot currentLot;
    private boolean IsSelectedLot;
    private boolean IsTableLock = true;
    private Table bidsTable = new Table();
    private List<Bid> bids;

    public MainWindow(UI components, int userId) {
        super("Auction"); // Set window caption
        log.info("Start Main window");
        center();
        //init auction variables
        parentWindow = components;
        auction = Authentication.getAuctionWebService();
        this.userId = userId;
        userName = auction.getUserName(userId);
        // Create form
        FormLayout content = new FormLayout();
        content.setMargin(true);
        content.setWidth(100, Unit.PERCENTAGE);
        setContent(content);
        // form layouts
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout mainFrame = new VerticalLayout();

        //------TOP INFO----
        HorizontalLayout topFrame = new HorizontalLayout();
        topFrame.setHeight(50, Unit.POINTS);
        topFrame.setSizeFull();
        //top components
        Label auctionLabel = new Label("<H1>Auction</H1>", ContentMode.HTML);
        topFrame.addComponent(auctionLabel);
        topFrame.setComponentAlignment(auctionLabel, Alignment.MIDDLE_LEFT);
        auctionLabel.setSizeUndefined();
        //topFrame.setSpacing(true);
        HorizontalLayout userInfoPanel = new HorizontalLayout();
        topFrame.addComponent(userInfoPanel);
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
        userInfoPanel.setWidth(250, Unit.PIXELS);
        topFrame.setComponentAlignment(userInfoPanel, Alignment.TOP_RIGHT);
        mainFrame.addComponent(topFrame);

        //main space
        HorizontalLayout middleFrame = new HorizontalLayout();
        middleFrame.setSizeFull();
        middleFrame.setMargin(true);
        lotsPanel = new VerticalLayout();
        lotsPanel.setWidth(40, Unit.PERCENTAGE);
        lotsPanel.setCaption("Lots");
        //lotsPanel.setStyleName();
        lotsPanel.setVisible(true);
        //----LOTS INFO

        fillLotsTable();
        lotsPanel.addComponent(lotsTable);
        lotsPanel.setComponentAlignment(lotsTable,Alignment.TOP_LEFT);
        Button btnRefreshLots = new Button("refresh lots", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fillLotsTable();
            }
        });
        btnRefreshLots.setStyleName(BaseTheme.BUTTON_LINK);
        lotsPanel.addComponent(btnRefreshLots);
        lotsPanel.setComponentAlignment(btnRefreshLots,Alignment.BOTTOM_LEFT);
        Button btnAddNewLot = new Button("New lot", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onAddNewLot();
            }
        });
        lotsPanel.addComponent(btnAddNewLot);
        lotsPanel.setComponentAlignment(btnAddNewLot,Alignment.BOTTOM_RIGHT);

        middleFrame.addComponent(lotsPanel);
        /////
        VerticalLayout rightFrame = new VerticalLayout();
        rightFrame.setWidth(60, Unit.PERCENTAGE);
        HorizontalLayout lotInfoFrame = new HorizontalLayout();
        //lotInfoFrame.setHeight(50, Unit.PERCENTAGE);
        lotInfoFrame.setSizeFull();
        lotInfoFrame.setCaption("Lot details");
        lotDetailsPanel = new HorizontalLayout();
        fillLotDetails();
        lotInfoFrame.addComponent(lotDetailsPanel);
        //lot buttons
        VerticalLayout lotButtonsPanel = new VerticalLayout();
        lotButtonsPanel.setWidth(150, Unit.PIXELS);
        Button btnCancelTrades = new Button("Cancel trades", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onCancelTrades();
            }
        });
        lotButtonsPanel.addComponent(btnCancelTrades);
        Button btnFinishTrades = new Button("Finish trades", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onFinishTrades();
            }
        });
        lotButtonsPanel.addComponent(btnFinishTrades);

        lotInfoFrame.addComponent(lotButtonsPanel);
        lotInfoFrame.setComponentAlignment(lotButtonsPanel, Alignment.BOTTOM_RIGHT);
        rightFrame.addComponent(lotInfoFrame);
        //--BIDS INFO-----
        bidsPanel = new FormLayout();
        bidsPanel.setCaption("Bids");
        fillBidsTable();
        bidsPanel.addComponent(bidsTable);
        //bid button
        Button btnAddNewBid = new Button("New bid", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onAddNewBid();
            }
        });
        bidsPanel.addComponent(btnAddNewBid);
        bidsPanel.setComponentAlignment(btnAddNewBid,Alignment.BOTTOM_RIGHT);



        rightFrame.addComponent(bidsPanel);

        middleFrame.addComponent(rightFrame);

        mainFrame.addComponent(middleFrame);

        content.addComponent(mainFrame);

    }


    private void fillBidsTable() {
        log.info("refresh bids table");
        //IsTableLock = true;
        bidsTable.removeAllItems();//clear before filling

        // column captions
        bidsTable.addContainerProperty("Bid", Double.class, null);
        bidsTable.addContainerProperty("Date", Date.class, null);
        bidsTable.addContainerProperty("Bidder", String.class, null);
        //get lots
        bids = auction.getAllBidsForLot(currentLot.getId());
        //records
        int i = 0;
        for (Bid bid : bids) {
            bidsTable.addItem(
                    new Object[]{bid.getValue(), bid.getCreatedOnDate(), bid.getBidderName()}, i++);
            // lotsTable.setStyleName(lot.getOwnerId() == userId ? "another" : "normal");

        }
        bidsTable.setPageLength(7);
        //footer
//        bidsTable.setFooterVisible(true);
//        // Add some total sum and description to footer
//        bidsTable.setColumnFooter("Finish date", "Total count");
//        bidsTable.setColumnFooter("State", Integer.toString(i));

    }


    private void fillLotDetails() {
        //get lot
        Object selectedValue = lotsTable.getValue();
        IsSelectedLot = selectedValue != null;
        if (!IsSelectedLot) {
            selectedValue = lotsTable.firstItemId();
        }
        currentLot =  (selectedValue != null) ? lots.get((Integer) selectedValue) : new Lot();
        log.info("refresh lot details. Lot: " + currentLot.getName());
//remove current info
        lotDetailsPanel.removeAllComponents();
        lotDetailsPanel.setMargin(new MarginInfo(false,true,false,false));
        //generate labels
        //
        VerticalLayout captionsPanel = new VerticalLayout();
        captionsPanel.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        captionsPanel.setSizeFull();
        VerticalLayout valuesPanel = new VerticalLayout();
        valuesPanel.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        valuesPanel.setSizeFull();

//code
        Label captionLabel = new Label("Code:" + spacesString, ContentMode.HTML);
        Label valueLabel = new Label(Integer.toString(currentLot.getId()));
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//name
        captionLabel = new Label("Name:" + spacesString, ContentMode.HTML);
        valueLabel = new Label(currentLot.getName());
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//state
        captionLabel = new Label("State:" + spacesString, ContentMode.HTML);
        valueLabel = new Label(currentLot.getState());
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//finish date
        captionLabel = new Label("Finish date:" + spacesString, ContentMode.HTML);
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        valueLabel = new Label(df.format(currentLot.getFinishDate()));
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//owner
        captionLabel = new Label("Owner:" + spacesString, ContentMode.HTML);
        valueLabel = new Label(currentLot.getOwnerName());
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//remaining time
        captionLabel = new Label("Remaining time:" + spacesString, ContentMode.HTML);
        valueLabel = new Label(currentLot.getRemainingTime());
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//description
        captionLabel = new Label("Description:" + spacesString, ContentMode.HTML);
        String description = currentLot.getDescription();
        if (description == "" || description == null){
            description = "----";//???todo description
        }
        valueLabel = new Label(description+ spacesString, ContentMode.HTML);
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);
//start price
        captionLabel = new Label("Start price:" + spacesString, ContentMode.HTML);
        valueLabel = new Label(Double.toString(currentLot.getStartPrice()) + " $");
        captionsPanel.addComponent(captionLabel);
        valuesPanel.addComponent(valueLabel);

        lotDetailsPanel.addComponent(captionsPanel);
        lotDetailsPanel.addComponent(valuesPanel);
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
                }
            }
        });
        IsTableLock = false;
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
