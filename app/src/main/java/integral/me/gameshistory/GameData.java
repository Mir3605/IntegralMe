package integral.me.gameshistory;

import java.sql.Date;
import java.util.ArrayList;

public class GameData {
    private final ArrayList<String> problems;
    private final int points;
    private final ArrayList<Long> times;
    private final long date;

    public ArrayList<String> getProblems() {
        return problems;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public long getDate() {
        return date;
    }
    public String getDateAsString(){
        Date date = new Date(getDate());
        return date.toString();
    }

    public GameData(ArrayList<String> problems, int points, ArrayList<Long> times, long date) {
        this.problems = problems;
        this.points = points;
        this.times = times;
        this.date = date;
    }
}
