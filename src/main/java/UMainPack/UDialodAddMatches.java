package UMainPack;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static UMainPack.UCommon.writeToFile;
import static UMainPack.UEmbeddedDB.getAllDataMatches;
import static UMainPack.USqlite.getBuildTableModel;

public class UDialodAddMatches extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableMatches;
    private JButton addButton;

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int sizeWidth = 800;
    private static int sizeHeight = 600;
    private static int locationX = (screenSize.width - sizeWidth) / 2;
    private static int locationY = (screenSize.height - sizeHeight) / 2;


    public static void main(String[] args) {
        UDialodAddMatches form = new UDialodAddMatches();
        form.setVisible(true);
    }

    public UDialodAddMatches() {
        setBounds(locationX, locationY, this.getWidth(), this.getHeight());
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addButton.addActionListener(e -> addNewRow());
        tableMatches.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == 127){ deleteSelectedRows(); }
            }
        });
        updateTableMatches();
    }

    private void addNewRow() {
        DefaultTableModel model = (DefaultTableModel) tableMatches.getModel();
        model.addRow(new Object[]{});
    }

    private void onOK() {
        UEmbeddedDB.cleareTableMatchesId();
        UEmbeddedDB.createTableMatchStrunaBDRV();
        UEmbeddedDB.insertDataToTableMatchesFromTable((TableModel) tableMatches.getModel());
        this.setVisible(false);
    }

    private void onCancel() {
        dispose();
    }

    private void deleteSelectedRows(){
        int[] selectedRows = tableMatches.getSelectedRows();
        DefaultTableModel model = (DefaultTableModel) tableMatches.getModel();
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeRow(selectedRows[i]);
            }
        }
    }

    public void updateTableMatches(){
        ResultSet tableMatchesResultSet = getAllDataMatches();
        DefaultTableModel tableMatchesModel = getBuildTableModel(tableMatchesResultSet);
        tableMatches.setModel(tableMatchesModel);
    };

}
