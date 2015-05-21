package gui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.log4j.Logger;
import util.ex.UserDataNotValidException;
import ws.general.web.AuctionWebService;
import ws.model.User;

public class LoginWindow extends Window implements BasicWindow {
    private static final Logger log = Logger.getLogger(LoginWindow.class);

    private final UI parentWindow;
    private final TextField loginField;
    private final Button btnOk;
    private final Button btnRegister;
//    private final Button btnStartWs;

    private User user;
    private PasswordField passwordField;
    private String resultType = "";


    public LoginWindow(UI components) {
        super("Authentication"); // Set window caption
        log.info("start login window");
        center();
        this.parentWindow = components;
        // Create the content root layout for the UI
        FormLayout content = new FormLayout();
        content.setMargin(true);
        setContent(content);
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
        btnOk = new Button("Login", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        });
        btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER); //enter
        okPanel.addComponent(btnOk);
        okPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_CENTER);
        //register
        VerticalLayout registerPanel = new VerticalLayout();
        btnRegister = new Button("Register", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onRegister();
            }
        });
        btnRegister.setStyleName(BaseTheme.BUTTON_LINK);

        registerPanel.addComponent(btnRegister);
        registerPanel.setComponentAlignment(btnRegister, Alignment.BOTTOM_RIGHT);
//        //start ws (temp, todo better)
//        VerticalLayout startWsPanel = new VerticalLayout();
//        btnStartWs = new Button("Start WS", new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                new AuctionPublisher().publish();
//            }
//        });
//        btnStartWs.setStyleName(BaseTheme.BUTTON_LINK);
//        startWsPanel.addComponent(btnStartWs);
//        content.addComponent(startWsPanel);
        //total
        content.addComponent(fieldsPanel);
        content.addComponent(okPanel);
        content.addComponent(registerPanel);

        //other windows
//        VerticalLayout windowsPanel = new VerticalLayout();
//        Button btnNewLot = new Button("New Lot",new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                onNewLot();
//            }
//        });
//        btnNewLot.setStyleName(BaseTheme.BUTTON_LINK);
//        Button btnNewBid = new Button("New Bid",new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                onNewBid();
//            }
//        });
//        btnNewBid.setStyleName(BaseTheme.BUTTON_LINK);
//        Button btnMain = new Button("Main window",new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                onMain();
//            }
//        });
//        btnMain.setStyleName(BaseTheme.BUTTON_LINK);

//        windowsPanel.addComponent(btnNewLot);
//        windowsPanel.addComponent(btnNewBid);
//        windowsPanel.addComponent(btnMain);
//        content.addComponent(windowsPanel);

    }

    private void onMain() {
        MainWindow wnd = new MainWindow(this.parentWindow, user);
        getCurrent().addWindow(wnd);

    }

//    private void onNewBid() {
//        NewBidWindow wnd = new NewBidWindow();
//        getCurrent().addWindow(wnd);
//    }
//
//    private void onNewLot() {
//        NewLotWindow wnd = new NewLotWindow(this, userId);
//        getCurrent().addWindow(wnd);
//    }

    private void onRegister() {
        RegistrationWindow wnd = new RegistrationWindow(this.parentWindow);
        getCurrent().addWindow(wnd);
    }

    private void onOk() {
        log.info("Start authenticate user");//System.exit(0);
        String lgn = loginField.getValue();
        String pwd = passwordField.getValue();//Arrays.toString().toString();
        String message = "Enter user data!";
        if (lgn != "" && pwd != "") { //todo better?
            try {
                AuctionWebService auction = Authentication.getAuctionWebService();
                user = auction.authenticateUser(lgn, pwd);
                message = "Welcome, " + lgn + "!";
                this.resultType = "Success";
                log.info("user [" + lgn + "] is login");
            } catch (UserDataNotValidException e) {
                message = e.getMessage();
                this.resultType = "Failure";
                log.error("Catch Exception, user [" + lgn + "]: " + message);
            } catch (Exception e) {
                message = e.getMessage();
                this.resultType = "Failure";
                log.error("Catch Exception: ", e);
            }
        } else {
            log.info(message);
        }
        btnOk.removeClickShortcut();
        Window dialog = new InformationDialog(this, message);
        getCurrent().addWindow(dialog);
    }

    @Override
    public void onNotify(String message) {
        if (resultType == "Success") {

            log.info("close login window");
            this.close();//??
            onMain();
        }
        btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER); //enter
        resultType = "";
    }

    @Override
    public UI getCurrent() {
        return this.parentWindow.getCurrent();
    }
}
