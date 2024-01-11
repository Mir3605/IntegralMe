package com.example.integralmefirst.settings;

import com.example.integralmefirst.database.DBHelper;

public enum Settings {
    STAGES_PER_LEVEL,
    RETURN_ON_CLICK,
    CHRONOLOGICAL_ORDER_GAMES_HISTORY;
    private static int stagesPerLevel = 3;
    private static boolean returnOnClick = false;
    private static boolean chronologicalOrderGamesHistory = true;

    public static boolean isReturnOnClick() {
        return returnOnClick;
    }

    static void setReturnOnClick(boolean returnOnClick) {
        Settings.returnOnClick = returnOnClick;
        DBHelper.getCurrentDBHelper().updateSetting(RETURN_ON_CLICK);
    }

    public static boolean isChronologicalOrderGamesHistory() {
        return chronologicalOrderGamesHistory;
    }

    static void setChronologicalOrderGamesHistory(boolean chronologicalOrderGamesHistory) {
        Settings.chronologicalOrderGamesHistory = chronologicalOrderGamesHistory;
        DBHelper.getCurrentDBHelper().updateSetting(CHRONOLOGICAL_ORDER_GAMES_HISTORY);
    }

    public static int getStagesPerLevel() {
        return stagesPerLevel;
    }

    static void setStagesPerLevel(int stagesPerLevel) {
        if (stagesPerLevel < 1 || stagesPerLevel > 6 || stagesPerLevel == Settings.stagesPerLevel)
            return;
        Settings.stagesPerLevel = stagesPerLevel;
        DBHelper.getCurrentDBHelper().updateSetting(STAGES_PER_LEVEL);
    }
    public static void readSettingsFromDB() {
        DBHelper helper = DBHelper.getCurrentDBHelper();
        stagesPerLevel = helper.getSettingIntValue(STAGES_PER_LEVEL);
        returnOnClick = helper.getSettingIntValue(RETURN_ON_CLICK) == 1;
        chronologicalOrderGamesHistory = helper.getSettingIntValue(CHRONOLOGICAL_ORDER_GAMES_HISTORY) == 1;
    }

    public int getIntValue() {
        switch (this) {
            case STAGES_PER_LEVEL: return stagesPerLevel;
            case RETURN_ON_CLICK: {
                if (returnOnClick)
                    return 1;
                return 0;
            }
            case CHRONOLOGICAL_ORDER_GAMES_HISTORY: {
                if (chronologicalOrderGamesHistory)
                    return 1;
                return 0;
            }
        }
        throw new RuntimeException("Invalid setting");
    }
}
