package Interface;

import WebService.General.AuctionWs;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.log4j.Logger;

class MainWindow extends Window implements BasicWindow{
//    public static void main(String[] args) {
//        new MainWindow(null,14);
//    }

    private static final Logger log = Logger.getLogger(MainWindow.class);
    private final AuctionWs auction;
    private final String userName;
    private VerticalLayout lotDetailsPanel;
    private FormLayout bidsPanel;
    private FormLayout lotsPanel;

    private final UI parentWindow;
    private int userId;
    private String resultType;

    public MainWindow(UI components, int userId) {
        super("Registration"); // Set window caption
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
        HorizontalLayout topFrame = new HorizontalLayout();
        topFrame.setHeight(50, Unit.POINTS);
        HorizontalLayout middleFrame = new HorizontalLayout();
        mainFrame.addComponent(topFrame);
        mainFrame.addComponent(middleFrame);
        lotsPanel = new FormLayout();
        lotsPanel.setWidth(40, Unit.PERCENTAGE);
        lotsPanel.setCaption("Lots");
        //lotsPanel.setStyleName();
        VerticalLayout rightFrame = new VerticalLayout();
        middleFrame.addComponent(lotsPanel);
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
        //components
        //top
        Label auctionLabel = new Label("Auction");
        topFrame.addComponent(auctionLabel);
        topFrame.setComponentAlignment(auctionLabel, Alignment.MIDDLE_LEFT);
        Label userNameLabel = new Label("User: " + userName);
        topFrame.addComponent(userNameLabel);
        topFrame.setComponentAlignment(userNameLabel, Alignment.MIDDLE_RIGHT);
        Button logoutButton = new Button("Logout",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onLogout();
            }
        });
        logoutButton.setStyleName(BaseTheme.BUTTON_LINK);
        topFrame.addComponent(logoutButton);
        topFrame.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        //lots























        /////fields////
        //login
//        Layout fieldsPanel = new FormLayout();
//        loginField = new TextField("Login");
//        loginField.addValidator(new StringLengthValidator(
//                "The name must be 3-10 letters (was {0})", 3, 10, true));//validation
//        loginField.setImmediate(true);
//        loginField.setWidth("100%");
//        fieldsPanel.addComponent(loginField);
//        //pwd
//        passwordField = new PasswordField("Password");
//        passwordField.addValidator(new StringLengthValidator(
//                "The password must be at least 4 letters (was {0})", 4, -1, true));//validation
//        passwordField.setImmediate(true);
//        passwordField.setWidth("100%");
//        fieldsPanel.addComponent(passwordField);
//        //1st name
//        firstNameField = new TextField("First name");
//        firstNameField.addValidator(new StringLengthValidator(
//                "The First name must be not null (was {0})", 1, -1, true));//validation
//        firstNameField.setImmediate(true);
//        firstNameField.setWidth("100%");
//        fieldsPanel.addComponent(firstNameField);
//        //2nd name
//        lastNameField = new TextField("Last name");
//        lastNameField.setWidth("100%");
//        fieldsPanel.addComponent(lastNameField);
//
//        ////buttons////
//        //ok
//        HorizontalLayout buttonsPanel = new HorizontalLayout();
//        buttonsPanel.setMargin(true);
//        buttonsPanel.setWidth("100%");
//        btnOk = new Button("Register");
//        Button.ClickListener okOnClick = new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                onOk();
//            }
//        };
//        btnOk.addClickListener(okOnClick);
//        buttonsPanel.addComponent(btnOk);
//        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
//        //cancel
//        btnCancel = new Button("Cancel",new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                onCancel();
//            }
//        });
//        buttonsPanel.addComponent(btnCancel);
//        buttonsPanel.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);
//        //total
//        content.addComponent(fieldsPanel);
//        content.addComponent(buttonsPanel);
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
    public void onNotify() {
        if (resultType == "Success") {
            //onMain();//todo main window
            //onRegister();
            this.close();//??
        }
        resultType = "";
    }
}
