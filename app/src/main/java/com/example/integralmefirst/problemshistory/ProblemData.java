package com.example.integralmefirst.problemshistory;

public class ProblemData {
    private final String problem;
    private final int triesCounter;
    private final long averageTime;

    public ProblemData(String problem, int triesCounter, long averageTime) {
        this.problem = problem;
        this.triesCounter = triesCounter;
        this.averageTime = averageTime;
    }

    public String getProblem() {
        return problem;
    }

    public int getTriesCounter() {
        return triesCounter;
    }
    public String getAverageTimeFormatted(){
        String s = "";
        if (averageTime > 60*1000)
            s += averageTime/60000 + "min ";
        s += averageTime/1000%60 + "." + averageTime/100%10 + "s";
        return s;
    }
}
