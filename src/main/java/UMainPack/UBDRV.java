package UMainPack;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static UMainPack.UCommon.writeToFile;
import static UMainPack.ULogger.*;

public class UBDRV {

    private Connection conn = null;

    UBDRV(){
        connect();
    }

    public Connection getConnection() {
        return conn;
    };

    private boolean connect() {
        try {
            String ServerDB = UProperties.getProperty("ServerNameBDRV");
            String PortDB = UProperties.getProperty("ServerPortBDRV");
            String NameDB = UProperties.getProperty("NameDbBDRV");
            String UserDB = UProperties.getProperty("UsernameBDRV");
            String PswdDB = UProperties.getProperty("PasswordBDRV");
            Properties props = new Properties();
            props.setProperty("user", UserDB);
            props.setProperty("password", PswdDB);

//            String connectionUrl = "jdbc:sqlserver://" + ServerDB + ":" + PortDB + ";" +
//                                    "databaseName=" + NameDB + ";" +
//                                    "user=" + UserDB + ";" +
//                                    "password=" +  PswdDB;
//            conn = DriverManager.getConnection(connectionUrl);
            String connectionUrl = "jdbc:sqlserver://" + ServerDB + ":" + PortDB + ";" +
                    "databaseName=" + NameDB;
            Class clazz = Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Driver driver = (Driver) (clazz.newInstance());
            conn = driver.connect(connectionUrl, props);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return conn != null;
    }

    public void sp_msr_value_send(ResultSet DataStruna){
        Thread myThreadRun = new Thread(() -> runLoadDataStruna(DataStruna), "myThreadRun");
        myThreadRun.start();

//        runLoadDataStruna(DataStruna);

    }

    private int getSizeResultSet(ResultSet rs){
        int rowCount = 0;
        try {
            rs.beforeFirst();
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            rowCount = 0;
        }
        return rowCount;
    };

    private void runLoadDataStruna(ResultSet DataStruna){
        UMain.mainForm.setDisableBtnloadDataButton();
        UMain.mainForm.setProgressBarLoadValue(0);
        CallableStatement cs = null;
        Timestamp lastDateTimeLoaded = null;
        try {
            conn.setAutoCommit(false);
            int rowCount = getSizeResultSet(DataStruna);
            int curLoadedNumber = 0;
            int countLoaded = 0;
            String ffcId = UProperties.getProperty("IdTZK");
            while(DataStruna.next()) {
                int currentProgressValue = ++curLoadedNumber * 100 / rowCount;
                UMain.mainForm.setProgressBarLoadValue(currentProgressValue);
                //if(countLoaded >= 100) throw new SQLException("Check correct transaction!");

//                String msdId = DataStruna.getString("P_MSD_ID");
                String msdId = UEmbeddedDB.getMsdID(DataStruna.getString("ID_OBJ"), DataStruna.getString("P_NAME"));
                if(msdId.isEmpty()) continue;

                String msrValue = DataStruna.getString("P_VALUE");
                Timestamp msrTime = DataStruna.getTimestamp("DATTIM");
                lastDateTimeLoaded = msrTime;
                cs = conn.prepareCall("{call dbo.Sp_msr_value_send(?,?,?,?)}");
                cs.setInt(1, Integer.valueOf(ffcId));
                cs.setInt(2, Integer.valueOf(msdId));
                //cs.setInt(2, 457);
                cs.setFloat(3, Float.valueOf(msrValue));
                cs.setTimestamp(4, msrTime);
                cs.execute();
                countLoaded++;
                //try{Thread.sleep(5);}catch(Exception e){}
            }
            conn.commit();
            log.info("Загружено " + countLoaded + " записей из " + curLoadedNumber);
            if(UMain.mainForm.isChangeLastDownloadDateTime()){
                UEmbeddedDB.insertDataToTableDateTimeLoad(lastDateTimeLoaded);
            }
        }catch (SQLException se){
            log.error(se.getMessage().replace("'", "\""));
            UMain.mainForm.setProgressBarLoadValue(0);
        }
        try {
            DataStruna.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UMain.mainForm.setEnableBtnloadDataButton();
    };

    public void runLoadDataStrunaCmd(ResultSet DataStruna, Timestamp lastDateTimeLoad){
        CallableStatement cs = null;
        try {
            conn.setAutoCommit(false);
            int countLoaded = 0;
            String ffcId = UProperties.getProperty("IdTZK");
            int rowCount = getSizeResultSet(DataStruna);
            while(DataStruna.next()) {

                String msdId = UEmbeddedDB.getMsdID(DataStruna.getString("ID_OBJ"), DataStruna.getString("P_NAME"));
                if(msdId.isEmpty()) continue;

                String msrValue = DataStruna.getString("P_VALUE");
                Timestamp msrTime = DataStruna.getTimestamp("DATTIM");

                cs = conn.prepareCall("{call dbo.Sp_msr_value_send(?,?,?,?)}");
                cs.setInt(1, Integer.valueOf(ffcId));
                cs.setInt(2, Integer.valueOf(msdId));
                //cs.setInt(2, 457);
                cs.setFloat(3, Float.valueOf(msrValue));
                cs.setTimestamp(4, msrTime);

                cs.execute();
                countLoaded++;

            }
            conn.commit();
            log.info("В базу БДРВ из базы Струна загружено " + countLoaded + " записей!");
            UEmbeddedDB.insertDataToTableDateTimeLoad(lastDateTimeLoad);
        }catch (SQLException se){
            log.error(se.getMessage());
            se.printStackTrace();
        }

        try {
            DataStruna.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

}
