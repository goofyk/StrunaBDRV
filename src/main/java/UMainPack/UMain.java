package UMainPack;

import static UMainPack.ULogger.log;
import static UMainPack.UProperties.*;
import static org.apache.log4j.PropertyConfigurator.*;

public class UMain {

    static UFormMain mainForm;

    public static void main(String[] args) {
        configure("config/ULogger.properties");
        loadProperties();
        mainForm = new UFormMain();
        mainForm.setVisible(true);
        //((UFormMain) UFormMain.mainForm).hideProgressBar();
    }

}
