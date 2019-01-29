package UMainPack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static UMainPack.UCommon.pathRoot;
import static UMainPack.UCommon.screenSize;
import static UMainPack.ULogger.log;

public class UDialogSettings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ServerNameBDRV;
    private JPanel panel1;
    private JPanel panel2;
    private JTextField NameDbBDRV;
    private JTextField UsernameBDRV;
    private JTextField ServerPortBDRV;
    private JPasswordField PasswordBDRV;
    private JTextField ServerNameStruna;
    private JTextField PathToDbStruna;
    private JTextField UsernameStruna;
    private JPasswordField PasswordStruna;
    private JTextField ServerPortStruna;
    private JPanel pnlBDRV;
    private JPanel pnlStruna;
    private JTextField NameService;
    private JTextField IntervalScheduler;
    private JButton createEXEButton;
    private JTextField PathToFileOfService;
    private JTextField EncodingStruna;
    private JButton btnCheckConnectBDRV;
    private JButton btnCheckConnectStruna;
    private JTextField IdTZK;
    private JButton btnLoadLastDateTimeLoad;
    private JFormattedDateTextField lastdattimload;
    private JButton getLastButton;
    private JButton saveButton;

    private static int sizeWidth = 800;
    private static int sizeHeight = 600;
    private static int locationX = (screenSize.width - sizeWidth) / 2;
    private static int locationY = (screenSize.height - sizeHeight) / 2;

    public UDialogSettings() {

        //setLocationRelativeTo(UMain.mainForm);
        setBounds(locationX, locationY, this.getWidth(), this.getHeight());
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        loadProperties();

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        buttonOK.addActionListener(e -> onOK());
        saveButton.addActionListener(e -> saveProperties());
        buttonCancel.addActionListener(e -> onCancel());
        createEXEButton.addActionListener(e -> createExeService());

        btnCheckConnectBDRV.addActionListener(e -> checkConnect(new UBDRV().getConnection()));
        btnCheckConnectStruna.addActionListener(e -> checkConnect(new UStruna().getConnection()));
        btnLoadLastDateTimeLoad.addActionListener(e -> UEmbeddedDB.insertDataToTableDateTimeLoad(UCommon.convertStringToTimestamp(lastdattimload.getText())));
        getLastButton.addActionListener(e -> showLastDateTimeLoad());

        updateAvailabilityElements();

    }

    private void onOK() {
        saveProperties();
        this.dispose();
    };

    private void onCancel() {
        this.dispose();
    };

    private void createExeService(){
        try {
            String command = pathRoot + "\\launch4j\\launch4j.exe " + pathRoot + "\\launch4j\\config\\config.xml";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("dfdfd");
        }
    };

    private void saveProperties() {

        UProperties.setProperty("IdTZK", IdTZK.getText());

        UProperties.setProperty("ServerNameBDRV", ServerNameBDRV.getText());
        UProperties.setProperty("ServerPortBDRV", ServerPortBDRV.getText());
        UProperties.setProperty("NameDbBDRV", NameDbBDRV.getText());
        UProperties.setProperty("UsernameBDRV", UsernameBDRV.getText());
        UProperties.setProperty("PasswordBDRV", new String(PasswordBDRV.getPassword()));

        UProperties.setProperty("ServerNameStruna", ServerNameStruna.getText());
        UProperties.setProperty("ServerPortStruna", ServerPortStruna.getText());
        UProperties.setProperty("PathToDbStruna", PathToDbStruna.getText());
        UProperties.setProperty("UsernameStruna", UsernameStruna.getText());
        UProperties.setProperty("EncodingStruna", EncodingStruna.getText());
        UProperties.setProperty("PasswordStruna", new String(PasswordStruna.getPassword()));

        UProperties.setProperty("NameService", NameService.getText());
        UProperties.setProperty("IntervalScheduler", IntervalScheduler.getText());
        UProperties.setProperty("PathToFileOfService", PathToFileOfService.getText());

    };

    private void loadProperties(){

        IdTZK.setText(UProperties.getProperty("IdTZK"));

        ServerNameBDRV.setText(UProperties.getProperty("ServerNameBDRV"));
        ServerPortBDRV.setText(UProperties.getProperty("ServerPortBDRV"));
        NameDbBDRV.setText(UProperties.getProperty("NameDbBDRV"));
        UsernameBDRV.setText(UProperties.getProperty("UsernameBDRV"));
        PasswordBDRV.setText(UProperties.getProperty("PasswordBDRV"));

        ServerNameStruna.setText(UProperties.getProperty("ServerNameStruna"));
        ServerPortStruna.setText(UProperties.getProperty("ServerPortStruna"));
        PathToDbStruna.setText(UProperties.getProperty("PathToDbStruna"));
        UsernameStruna.setText(UProperties.getProperty("UsernameStruna"));
        EncodingStruna.setText(UProperties.getProperty("EncodingStruna"));
        PasswordStruna.setText(UProperties.getProperty("PasswordStruna"));

        NameService.setText(UProperties.getProperty("NameService"));
        IntervalScheduler.setText(UProperties.getProperty("IntervalScheduler"));
        PathToFileOfService.setText(UProperties.getProperty("PathToFileOfService"));

    };

    private void checkConnect(Connection connection){
        String textConfirm = "Connection successfully established";
        int gradeMess = JOptionPane.INFORMATION_MESSAGE;
        if(connection == null){
            textConfirm = "Connection not established!";
            gradeMess = JOptionPane.ERROR_MESSAGE;
        }else {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, textConfirm, "Message", gradeMess);
    };

    private void showLastDateTimeLoad(){
        JOptionPane.showMessageDialog(this, String.valueOf(UEmbeddedDB.getLastDateTimeLoad()), "Message", 1);
    }

    private void updateAvailabilityElements() {
        boolean launch4jDirExist = UCommon.isDirExist(UCommon.pathRoot + "\\launch4j");
        createEXEButton.setEnabled(launch4jDirExist);
    }

}
