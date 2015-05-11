package Interface;

import Library.Exceptions.UserDataNotValidException;
import WebService.Domain.General.AuctionWs;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;

class LoginWindow extends Window implements BasicWindow {

    private final UI ParentWindow;
    private TextField loginField;
    private int UserID;
    private PasswordField passwordField;
    private String resultType;

    public LoginWindow(UI components) {
        super("Authentication"); // Set window caption
        center();
        this.ParentWindow = components;
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
        // example
//        content.addComponent(new Label("Hello World!"));
//        content.addComponent(new Button("Push Me!",
//                new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(Button.ClickEvent e) {
//                        Notification.show("Pushed!");
//                    }
//                }));

        //my form
        content.setCaption("Authentication");
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        /////////
        //login
        Layout fieldsPanel = new FormLayout();
        loginField = new TextField("Login");
        loginField.setWidth("100%");
        fieldsPanel.addComponent(loginField);
        //pwd
        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");
        fieldsPanel.addComponent(passwordField);
        //ok
        VerticalLayout okPanel = new VerticalLayout();
        Button btnOk = new Button("Login", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }});
        okPanel.addComponent(btnOk);
        okPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_CENTER);
        //register
        VerticalLayout registerPanel = new VerticalLayout();
        Button btnRegister = new Button("Register",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onRegister();
            }
        });
        btnRegister.setStyleName(BaseTheme.BUTTON_LINK);

        registerPanel.addComponent(btnRegister);
        registerPanel.setComponentAlignment(btnRegister, Alignment.BOTTOM_RIGHT);
        //total
        content.addComponent(fieldsPanel);
        content.addComponent(okPanel);
        content.addComponent(registerPanel);
        //other windows
        VerticalLayout windowsPanel = new VerticalLayout();
        Button btnNewLot = new Button("New Lot",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onNewLot();
            }
        });
        btnNewLot.setStyleName(BaseTheme.BUTTON_LINK);
        Button btnNewBid = new Button("New Bid",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onNewBid();
            }
        });
        btnNewBid.setStyleName(BaseTheme.BUTTON_LINK);
        Button btnMain = new Button("Main window",new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onMain();
            }
        });
        btnMain.setStyleName(BaseTheme.BUTTON_LINK);

        windowsPanel.addComponent(btnNewLot);
        windowsPanel.addComponent(btnNewBid);
        windowsPanel.addComponent(btnMain);
        content.addComponent(windowsPanel);
    }

    private void onMain() {

    }

    private void onNewBid() {
        NewBidWindow wnd = new NewBidWindow();
        this.ParentWindow.getCurrent().addWindow(wnd);
    }

    private void onNewLot() {
        NewLotWindow wnd = new NewLotWindow();
        this.ParentWindow.getCurrent().addWindow(wnd);
    }

    private void onRegister() {
        //System.exit(0);
        RegistrationWindow wnd = new RegistrationWindow();
        this.ParentWindow.getCurrent().addWindow(wnd);
    }

    private void onOk() {
        //System.exit(0);
        String lgn = loginField.getValue();
        String pwd = passwordField.getValue();//Arrays.toString().toString();
//        if (lgn == "" || pwd == ""null""){
//            return;
//        }
        //ValidateData();
        String message = "Enter user data";
        try {
            AuctionWs auction = Authentication.GetAuctionWebService();
            this.UserID = auction.AuthenticateUser(lgn, pwd);
            message = "Welcome, " + lgn + "!";
            this.resultType = "Success";


            //System.out.println("user " + lgn + " is registered!");
        } catch (UserDataNotValidException e) {
            message = e.getMessage();
            this.resultType = "Failure";
            //Window dialog = new InformationDialog(this, message);
        }
        finally {
            Window dialog = new InformationDialog(this, message);
            this.ParentWindow.getCurrent().addWindow(dialog);
        }
    }

    @Override
    public void onNotify(){
        if (resultType == "Success") {
            //onMain();
            onRegister();
        }
        resultType = "";
    }
}
