package gui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import util.Consts;

public class InformationDialog extends Window {
    private static final Logger log = Logger.getLogger(InformationDialog.class);
    private final Label messageLabel;
    private final Button btnOk;
    private BasicWindow notifyWindow;
    private String notifyMessage = Consts.OK_MESSAGE;


    public InformationDialog(BasicWindow parentWindow, String message) {
        super("Information"); // Set window caption
        center();
        this.notifyWindow = parentWindow;
        //this.setModal(true);
        //log.info(message);
        // Create the content root layout for the UI
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);
        // form
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        /////////
       //message label
        HorizontalLayout msgPanel = new HorizontalLayout();
        //msgPanel.setMargin(new MarginInfo(false,true,false,false));

        messageLabel = new Label(message);
        messageLabel.setWidth("100%");
        msgPanel.addComponent(messageLabel);
        msgPanel.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
        //buttons
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        //ok
        btnOk = new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        });
        btnOk.setWidth("80%");
        //btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER); //enter
        buttonsPanel.addComponent(btnOk);
        btnOk.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_RIGHT);

//        //total
        content.addComponent(msgPanel);
        content.addComponent(buttonsPanel);
    }

   private void onCancel() {
        this.close();
    }

    private void onOk() {
        if (notifyWindow.getClass() == LotWindow.class){
            notifyMessage = Consts.REFRESH_LOTS_MESSAGE;
        } else if (notifyWindow.getClass() == NewBidWindow.class) {
            notifyMessage = Consts.REFRESH_BIDS_MESSAGE;
        }
        notifyWindow.onNotify(notifyMessage);
        close();
    }
}
