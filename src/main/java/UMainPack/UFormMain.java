package UMainPack;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.ResultSet;

import static UMainPack.UCommon.pathRoot;
import static UMainPack.UCommon.writeToFile;
import static UMainPack.ULogger.log;

public class UFormMain extends JFrame{

    //public static JFrame mainForm = new UFormMain();

    private JTable UTableLog;
    private JButton btnInstallService;
    private JButton btnRemoveService;
    private JButton btnStartService;
    private JButton btnStopService;
    private JButton btnRestartService;
    private JButton updateButton;
    private JButton settingsButton;
    private JPanel JPanelMain;
    private JPanel panelTable;
    private JButton addMatchesButton;
    private JProgressBar progressBarLoad;
    private JPanel panelProgressBar;
    private JButton loadDataButton;
    private JPanel panelCommandJR;
    private JCheckBox checkBoxAutoUpdate;
    private JFormattedDateTextField firstdattim;
    private JFormattedDateTextField lastdattim;
    private JCheckBox chbChangeLast;

    private String nameOfService = "StrunaBDRV";
    private String pathToFileOfService = pathRoot + "\\service\\Service.exe";
    private Timer timer = null;

    private JButton[] arrBtnServices = {btnInstallService, btnRemoveService, btnStartService, btnStopService, btnRestartService};

    UFormMain() {

        super("Struna - BDRV");
        setSize(new Dimension(1020, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);
        Container container = getContentPane();

        //ULogger.log.error("This is debug : " + UCommon.currentTime());

        JTableHeader header = UTableLog.getTableHeader();
        panelTable.add(header, BorderLayout.NORTH);

        btnInstallService.addActionListener(e -> installService());
        btnRemoveService.addActionListener(e -> {removeService();});
        btnStartService.addActionListener(e -> startService());
        btnStopService.addActionListener(e -> stopService());
        btnRestartService.addActionListener(e -> restartService());
        settingsButton.addActionListener(e -> openDialogSetting());
        updateButton.addActionListener(e -> updateTableLogs(0));
        addMatchesButton.addActionListener(e -> openDialogAddMatches());
        checkBoxAutoUpdate.addActionListener(e -> startStopTimer(e));

        updateTableLogs(200);
        timer = new Timer(10000, e -> updateTableLogs(200));
        timer.start();

        container.add(JPanelMain);

        loadDataButton.addActionListener(e -> loadDataFromStrunaToBDRV());

        updateAvailabilityElements();

    }

   /**
     *  HANDLER LISTENERS
     */

    private void installService(){
        updateNameService();
        runCommandService("install");
    }

    private void removeService(){
        stopService();
        updateNameService();
        runCommandService("remove");
    };

    private void startService(){
        updateNameService();
        runCommandService("start");
    };

    private void stopService(){
        updateNameService();
        runCommandService("stop");
    };

    private void restartService(){
        updateNameService();
        runCommandService("restart");
    };

    private void updateNameService(){
        if(!UProperties.getProperty("NameService").isEmpty()) nameOfService = UProperties.getProperty("NameService");
    };

    private void runCommandService(String command){
        switch (command){
            case "install":
                execCommandService(command);
                break;

            case "remove":
                execCommandService(command);
                break;

            case "start":
                execCommandService(command);
                break;

            case "stop":
                execCommandService(command);
                break;

            case "restart":
                execCommandService(command);
                break;
        }
    };

    private Process execCommandService(String command){

        String fullCommand = pathRoot + "\\nssm\\win64\\nssm.exe " + command + " " + nameOfService;
        //if(command.equals("install")){ fullCommand = fullCommand + " " + nameOfService;}
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(fullCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proc;
    };

    private void updateTableLogs(int countTop){
        UTableLog.setModel(USqlite.getBuildTableModel(USqlite.getAllDataLogs(0)));
    };

    private void openDialogSetting(){
        UDialogSettings dialogSettings = new UDialogSettings();
        dialogSettings.pack();
        dialogSettings.setVisible(true);
    };

    private void openDialogAddMatches(){
        UDialodAddMatches dialogAddMatches = new UDialodAddMatches();
        dialogAddMatches.pack();
        dialogAddMatches.setVisible(true);
    };

    public void hideProgressBar(){
        panelProgressBar.setVisible(false);
    }

    private void startStopTimer(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if(selected){
            timer.start();
        }else{
            timer.stop();
        }
    }

    private void loadDataFromStrunaToBDRV() {
        java.sql.Timestamp tmstmpFirstDattim = UCommon.convertStringToTimestamp(firstdattim.getText());
        java.sql.Timestamp tmstmpLastDattim = UCommon.convertStringToTimestamp(lastdattim.getText());
        String typeObj = "0";

        UStruna uStruna = new UStruna();
        ResultSet rsStruna = uStruna.get_params_all(tmstmpFirstDattim, tmstmpLastDattim, typeObj);
        if(rsStruna == null){
            log.error("Не удалось получить данные из базы Струна. Проверьте подключение и попробуйте повторить попытку.");
            return;
        }

        UBDRV uBdrv = new UBDRV();
        uBdrv.sp_msr_value_send(rsStruna);
    }

    public void setProgressBarLoadValue(int value){
        progressBarLoad.setValue(value);
    };

    public void setEnableBtnloadDataButton(){
        if(!loadDataButton.isEnabled())loadDataButton.setEnabled(true);
    };

    public void setDisableBtnloadDataButton(){
        if(loadDataButton.isEnabled())loadDataButton.setEnabled(false);
    };

    public boolean isChangeLastDownloadDateTime(){
        return chbChangeLast.getModel().isSelected();
    };

    private void updateAvailabilityElements() {
        boolean serviceDirExist = UCommon.isDirExist(UCommon.pathRoot + "\\nssm\\win64");
        for (JButton btnEl: arrBtnServices) {
            btnEl.setEnabled(serviceDirExist);
        }
    }

}


