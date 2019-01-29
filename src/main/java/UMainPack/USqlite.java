package UMainPack;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

import static UMainPack.UCommon.writeToFile;
import static UMainPack.ULogger.*;

public class USqlite {

    final static String fileName = "./DB/ULogs.db";
    final static String URL = "jdbc:sqlite:" + fileName;
    private static Connection conn = null;

    private static Connection connect() {
        try {
            Properties props = new Properties();
            props.setProperty("user", "");
            props.setProperty("password", "");
            Class clazz = Class.forName("org.sqlite.JDBC");
            Driver driver = (Driver) (clazz.newInstance());
            conn = driver.connect(URL, props);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void createTableLogs() {
        String sql = new String("CREATE TABLE IF NOT EXISTS LOGS\n" +
                "   (USER_ID VARCHAR(20)    NOT NULL,\n" +
                "    DATED   DATE           NOT NULL,\n" +
                "    LOGGER  VARCHAR(50)    NOT NULL,\n" +
                "    LEVEL   VARCHAR(10)    NOT NULL,\n" +
                "    MESSAGE VARCHAR(1000)  NOT NULL\n" +
                "   );");
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void cleareTable(String tableName) {
        String url = "jdbc:sqlite:" + fileName;
        //String sql = new String("DELETE FROM " + tableName + ";");
        String sql = new String("DROP TABLE " + tableName + ";");
        try {
             Statement stmt = conn.createStatement();
             stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertDataToDBFromTable(TableModel tableModel) {
        try{
            conn.setAutoCommit(false);
            String sql = "INSERT INTO MatchStrunaBDRV(STRUNA_ID_OBJ, STRUNA_P_NAME, BDRV_P_MSD_ID) VALUES (?,?,?)";
            int rowCount = tableModel.getRowCount();
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int row = 0; row < rowCount; row++){
                String STRUNA_ID_OBJ = (String)tableModel.getValueAt(row, 0);
                String STRUNA_P_NAME = (String)tableModel.getValueAt(row, 1);
                String BDRV_P_MSD_ID = (String)tableModel.getValueAt(row, 2);
                pst.setString(1, STRUNA_ID_OBJ);
                pst.setString(2, STRUNA_P_NAME);
                pst.setString(3, BDRV_P_MSD_ID);
                pst.addBatch();
            }
            pst.executeBatch();
            conn.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void createTableMatchStrunaBDRV() {
        String url = "jdbc:sqlite:" + fileName;
        String sql = new String("CREATE TABLE IF NOT EXISTS MatchStrunaBDRV\n" +
                "   (STRUNA_ID_OBJ VARCHAR(25)    NOT NULL,\n" +
                "    STRUNA_P_NAME  VARCHAR(25)    NOT NULL,\n" +
                "    BDRV_P_MSD_ID VARCHAR(25)\n" +
                "   );");
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ResultSet getAllDataLogs(int countTop) {
        if(countTop == 0) countTop = 200;
        String sql = new String("SELECT LOGS.DATED, LOGS.LEVEL, LOGS.MESSAGE  FROM LOGS AS LOGS ORDER BY LOGS.DATED DESC LIMIT " + countTop);
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return rs;
    }

    public static ResultSet getAllDataMatches() {
        String sql = "SELECT * FROM MatchStrunaBDRV";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public static DefaultTableModel getBuildTableModel(ResultSet rs) {
        if(rs == null) return new DefaultTableModel();
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        ResultSetMetaData metaData;
        try {
            metaData = rs.getMetaData();
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new DefaultTableModel(data, columnNames);
    }

    static {
        //createNewDatabase();
        connect();
        createTableLogs();
        createTableMatchStrunaBDRV();
    }

}