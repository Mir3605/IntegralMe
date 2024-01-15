package com.example.integralmefirst.settings;

import com.example.integralmefirst.database.DBHelper;

public enum Settings {
    STAGES_PER_LEVEL,
    RETURN_ON_CLICK,
    FROM_NEWEST_GAMES_HISTORY,
    ANIMATIONS_DISPLAY;
    private static int stagesPerLevel = 3;
    private static boolean returnOnClick = false;
    private static boolean fromNewestGamesHistory = true;
    private static boolean animationsDisplay = true;

    public static boolean getAnimationsDisplay() {
        return animationsDisplay;
    }

    public static void setAnimationsDisplay(boolean animationsDisplay) {
        Settings.animationsDisplay = animationsDisplay;
        DBHelper.getCurrentDBHelper().updateSetting(ANIMATIONS_DISPLAY);
    }

    public static boolean getReturnOnClick() {
        return returnOnClick;
    }

    static void setReturnOnClick(boolean returnOnClick) {
        Settings.returnOnClick = returnOnClick;
        DBHelper.getCurrentDBHelper().updateSetting(RETURN_ON_CLICK);
    }

    public static boolean getFromNewestGamesHistory() {
        return fromNewestGamesHistory;
    }

    static void setFromNewestGamesHistory(boolean fromNewestGamesHistory) {
        Settings.fromNewestGamesHistory = fromNewestGamesHistory;
        DBHelper.getCurrentDBHelper().updateSetting(FROM_NEWEST_GAMES_HISTORY);
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
        fromNewestGamesHistory = helper.getSettingIntValue(FROM_NEWEST_GAMES_HISTORY) == 1;
        animationsDisplay = helper.getSettingIntValue(ANIMATIONS_DISPLAY) == 1;
    }

    public int getIntValue() {
        switch (this) {
            case STAGES_PER_LEVEL:
                return stagesPerLevel;
            case RETURN_ON_CLICK: {
                if (returnOnClick)
                    return 1;
                return 0;
            }
            case FROM_NEWEST_GAMES_HISTORY: {
                if (fromNewestGamesHistory)
                    return 1;
                return 0;
            }
            case ANIMATIONS_DISPLAY: {
                if (animationsDisplay)
                    return 1;
                return 0;
            }
        }
        throw new RuntimeException("Invalid setting");
    }
}
