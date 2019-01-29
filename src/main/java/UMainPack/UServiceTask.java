package UMainPack;

import java.util.*;

import static UMainPack.UCommon.*;
import static UMainPack.ULogger.log;
import static UMainPack.UProperties.*;
import static org.apache.log4j.PropertyConfigurator.*;

public class UServiceTask {

    private static Timer timer = new Timer();
    private static int intervalScheduler = 60000;

    public static void main(String[] args) {

        configure("config/ULogger.properties");
        loadProperties();
        //if(timer != null) timer.cancel();

        int intervalPropScheduler = intervalScheduler;
        if(!getProperty("IntervalScheduler").isEmpty()){
            intervalPropScheduler = Integer.valueOf(getProperty("IntervalScheduler")) * 1000;
        }

        //if(args.length > 0) intervalScheduler = Integer.valueOf(args[0]);

        if(intervalScheduler == 0) {
            log.error("Uncorrected interval sheduler (0)");
            return;
        }

        if(intervalPropScheduler != intervalScheduler){
            timer.cancel();
            intervalScheduler = intervalPropScheduler;
        }
        runScheduler();
    }

    public static void runScheduler() {
//        writeToFile("runScheduler", "runScheduler");
//        new UServiceTaskHandler(intervalScheduler).run();
//        log.debug("runScheduler");
        timer.schedule(new UServiceTaskHandler(), 0, intervalScheduler);
    }

}

