package Interface;

import com.vaadin.ui.*;

class InformationDialog extends Window {
    public TextField bidValueField;
    private BasicWindow notifyWindow;


    public InformationDialog(BasicWindow parentWindow, String message) {
        super("Information"); // Set window caption
        center();
        this.notifyWindow = parentWindow;
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
        Label messageLabel = new Label(message);
        messageLabel.setWidth("100%");
        msgPanel.addComponent(messageLabel);
        msgPanel.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
        //buttons
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        //ok
        Button btnOk = new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                onOk();
            }
        });
        btnOk.setWidth("80%");
        buttonsPanel.addComponent(btnOk);
        buttonsPanel.setComponentAlignment(btnOk, Alignment.MIDDLE_RIGHT);

//        //total
        content.addComponent(msgPanel);
        content.addComponent(buttonsPanel);
    }

   private void onCancel() {
        this.close();
    }

    private void onOk() {
        this.notifyWindow.onNotify();
        this.close();
    }
}
