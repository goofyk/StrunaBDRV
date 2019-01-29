package UMainPack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UProperties {

    static Properties properties = new Properties();
    static File file = new File(System.getProperty("user.dir") + "\\config\\UStrunaBDRV.properties");

    UProperties(){
        getProperty("");
    }

    public static void main(String[] args) {
        loadProperties();
    }

    public static boolean loadProperties(){

        if(!createFileProperties()) return false;

        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean createFileProperties(){
        if(!file.exists() || file.isDirectory()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    static void initPropeties(){}

    public static String getProperty(String name){
        return properties.getProperty(name) == null ? "" : properties.getProperty(name);
    }

    static boolean setProperty(String name, String value){
        try {
            properties.setProperty(name, value);
            properties.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
