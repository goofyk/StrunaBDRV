package UMainPack;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UCommon {

    static String pathRoot = System.getProperty("user.dir");
    static String javaHome = System.getProperty("java.home");
    static String sysdir = System.getenv("WINDIR");
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    start String test8;
    
    static String md5(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }

    static Date currentTime(){
        return new Date();
    }

    static void writeToFile(String text, String  nameFile) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathRoot + "\\" + nameFile + ".txt"), "UTF-8"));
            out.write(text);
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Timestamp convertStringToTimestamp(String dateInString) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
//        Timestamp timestamp = Timestamp.valueOf(convertStringToLocalDateTime(dateInString));
            timestamp = java.sql.Timestamp.valueOf(dateInString);
        }catch(Exception e){}

        return timestamp;
    }

    static LocalDateTime convertStringToLocalDateTime(String dateInString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateInString, formatter);
        return dateTime;
    }

    static boolean isDirExist(String pathToDir){
        return Files.exists(Paths.get(pathToDir));
    };

}

