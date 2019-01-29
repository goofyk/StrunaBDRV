package UMainPack;

public interface UQuery {

    String Q_GET_ALL_DATA_STRUNA = "SELECT\n" +
            "    StrunaData.DATTIM,\n" +
            "    StrunaData.ID_OBJ,\n" +
            "    StrunaData.P_NAME,\n" +
            "    StrunaData.P_VALUE,\n" +
            "    MatchesId.IdBdrv,\n" +
            "    cast(StrunaData.ID_OBJ as varchar(1)) || cast(right(CAST('00' as varchar(2)) || cast(MatchesId.IdBdrv as varchar(2)), 2) as varchar(2)) as P_MSD_ID\n" +
            "FROM\n" +
            "    GET_PARAMS_ALL(?, ?, ?) AS StrunaData\n" +
            "    left join (\n" +
            "        SELECT\n" +
            "            'M' as IdStruna,\n" +
            "            '1' as IdBdrv\n" +
            "        FROM rdb$database\n" +
            "        UNION\n" +
            "        SELECT\n" +
            "            'V',\n" +
            "            '2'\n" +
            "        FROM rdb$database\n" +
            "        UNION\n" +
            "        SELECT\n" +
            "            'T',\n" +
            "            '3'\n" +
            "        FROM rdb$database\n" +
            "        UNION\n" +
            "        SELECT\n" +
            "            'R',\n" +
            "            '4'\n" +
            "        FROM rdb$database\n" +
            "        UNION\n" +
            "        SELECT\n" +
            "            'H',\n" +
            "            '5'\n" +
            "        FROM rdb$database\n" +
            "        UNION\n" +
            "        SELECT\n" +
            "            'W',\n" +
            "            '6'\n" +
            "        FROM rdb$database\n" +
            "    ) as MatchesId\n" +
            "    on StrunaData.P_NAME = MatchesId.IdStruna\n" +
            "WHERE\n" +
            "    StrunaData.P_NAME IN ('M','V','T','R','H','W')";

}
