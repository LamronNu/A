package Interface;

import Library.Consts;
import WebService.Domain.Lot;
import WebService.General.AuctionWs;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

class MainWindow extends Window implements BasicWindow{
//    public static void main(String[] args) {
//        new MainWindow(null,14);
//    }

    private static final Logger log = Logger.getLogger(MainWindow.class);
    private final AuctionWs auction;
    private final String userName;
    private final Table lotsTable;
    private VerticalLayout lotDetailsPanel;
    private FormLayout bidsPanel;
    private HorizontalLayout lotsPanel;

    private final UI parentWindow;
    private int userId;
    private String resultType;

    public MainWindow(UI components, int userId) {
        super("Auction"); // Set window caption
        center();
        //init auction variables
        parentWindow = components;
        auction = Authentication.getAuctionWebService();
        this.userId = userId;
        userName = auction.getUserName(userId);
        // Create form
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // form layouts
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout mainFrame = new VerticalLayout();
        content.addComponent(mainFrame);
        //top info
        HorizontalLayout topFrame = new HorizontalLayout();
        topFrame.setHeight(50, Unit.POINTS);
        topFrame.setSizeUndefined();
        //components
        Label auctionLabel = new Label("<H1>Auction</H1>", ContentMode.HTML);
        topFrame.addComponent(auctionLabel);
        topFrame.setComponentAlignment(auctionLabel, Alignment.MIDDLE_LEFT);
        auctionLabel.setSizeUndefined();
        topFrame.setSpacing(true);
        HorizontalLayout userInfoPanel = new HorizontalLayout();
        topFrame.addComponent(userInfoPanel);
        topFrame.setComponentAlignment(userInfoPanel, Alignment.TOP_RIGHT);
        Label userNameLabel = new Label("User: " + userName);
        userInfoPanel.addComponent(userNameLabel);

        //userInfoPanel.setComponentAlignment(userNameLabel, Alignment.TOP_RIGHT);
        Button logoutButton = new Button("Logout",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onLogout();
            }
        });
        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);

        userInfoPanel.addComponent(logoutButton);
        userInfoPanel.setSpacing(true);

        //topFrame.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);


        //main space
        HorizontalLayout middleFrame = new HorizontalLayout();
        mainFrame.addComponent(topFrame);
        mainFrame.addComponent(middleFrame);
        lotsPanel = new HorizontalLayout();
        lotsPanel.setWidth(40, Unit.PERCENTAGE);
        lotsPanel.setCaption("Lots");
        //lotsPanel.setStyleName();
//lots
        lotsTable = new Table("The Brightest Stars");
        fillLotsTable();
        lotsPanel.addComponent(lotsTable);
        Button btnAddNewLot = new Button("New lot", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onAddNewLot();
            }
        });
        lotsPanel.addComponent(btnAddNewLot);
        lotsPanel.setComponentAlignment(btnAddNewLot,Alignment.BOTTOM_RIGHT);

        middleFrame.addComponent(lotsPanel);
        VerticalLayout rightFrame = new VerticalLayout();
        middleFrame.addComponent(rightFrame);
        HorizontalLayout lotInfoFrame = new HorizontalLayout();
        lotInfoFrame.setHeight(50, Unit.PERCENTAGE);
        lotInfoFrame.setCaption("Lot details");
        lotDetailsPanel = new VerticalLayout();
        lotInfoFrame.addComponent(lotDetailsPanel);
        bidsPanel = new FormLayout();
        bidsPanel.setCaption("Bids");
        rightFrame.addComponent(lotInfoFrame);
        rightFrame.addComponent(bidsPanel);


    }

    private void onAddNewLot() {
        NewLotWindow wnd = new NewLotWindow(this, userId);
        this.parentWindow.getCurrent().addWindow(wnd);
    }

    private void fillLotsTable() {
        lotsTable.removeAllItems();//clear before filling
        // column captions
        lotsTable.addContainerProperty("Code", Integer.class, null);
        lotsTable.addContainerProperty("Name", String.class, null);
        lotsTable.addContainerProperty("Finish date", Date.class, null);
        lotsTable.addContainerProperty("State", String.class, null);
        //get lots
        List<Lot> lots = auction.getAllLotsForUser(userId);
        //records
        int i = 0;
        for (Lot lot : lots) {
            lotsTable.addItem(
                    new Object[]{lot.getId(), lot.getName(), lot.getFinishDate(),lot.getState()}, i++);

        }
        lotsTable.setPageLength(lotsTable.size());
    }

    private void onLogout() {
        close();
    }

    private void onCancel() {
        this.close();
    }

    private void onOk() {
        //System.exit(0);
        //1/ validate

//        try {
////            loginField.validate();
////            passwordField.validate();
////            firstNameField.validate();
//        } catch (Validator.InvalidValueException e) {
//            //Notification.show(e.getMessage());
//            log.warn("Catch Exception: " + e.getMessage());
//            return;//???
//        }
//        //2.register
//        //field values
//        String lgn = loginField.getValue();
//        String pwd = passwordField.getValue(); //.toString();Arrays.toString(.getPassword());
//        String fName = firstNameField.getValue();
//        String lName = lastNameField.getValue();
//        String message = "";
//        try {
//            AuctionWs auction = Authentication.getAuctionWebService();
//            this.NewUserID = auction.CreateNewUser(lgn,pwd,fName,lName);
//            message = "user [" + lgn + "] is registered!";
//            this.resultType = "Success";
//
//        } catch (LoginIsAlreadyExistsException /*| MalformedURLException*/ e){
//            message = "user [" + lgn
//                    + "] is not registered (cause: " + e.getMessage() + ")";
//            this.resultType = "Failure";
//           // Dialog dialog = new InformationDialog(this, message, false);
//        } catch (Exception e){
//            message = e.getMessage();
//            log.error(message);
//        }
//        Window dialog = new InformationDialog(this, message);
//        this.parentWindow.getCurrent().addWindow(dialog);
//        log.info(message);
    }

    @Override
    public void onNotify(String message) {
        if (message == Consts.REFRESH_LOTS_MESSAGE) {
            fillLotsTable();
        }

    }
}
