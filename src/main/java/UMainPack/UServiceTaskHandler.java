package UMainPack;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.TimerTask;

import static UMainPack.ULogger.log;

class UServiceTaskHandler extends TimerTask {

    public void run() {
        Timestamp tmstmpFirstDattim = UEmbeddedDB.getLastDateTimeLoad();
        Timestamp tmstmpLastDattim = new Timestamp(System.currentTimeMillis());
        String typeObj = "0";

        UStruna uStruna = new UStruna();
        ResultSet rsStruna = uStruna.get_params_all(tmstmpFirstDattim, tmstmpLastDattim, typeObj);
        if(rsStruna == null){
            log.error("[STRUNA] Не удалось получить данные из базы Струна. Проверьте подключение и попробуйте повторить попытку.");
            return;
        }
        UBDRV uBdrv = new UBDRV();
        uBdrv.runLoadDataStrunaCmd(rsStruna, tmstmpLastDattim);
    }

}
