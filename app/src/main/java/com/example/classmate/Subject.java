package com.example.classmate;

public class Subject{
    private String name;
    private int totalPresent;
    private int totalAbsent;

    public Subject(String name,int totalPresent,int totalAbsent){
        this.name=name;
        this.totalPresent=totalPresent;
        this.totalAbsent=totalAbsent;
    }

    public String getName(){
        return name;
    }

    public int getTotalPresent(){
        return totalPresent;
    }

    public int getTotalAbsent(){
        return totalAbsent;
    }

    public void incrementPresent(){
        totalPresent++;
    }

    public void incrementAbsent(){
        totalAbsent++;
    }

    public double getAttendancePercentage(){
        int total = totalPresent + totalAbsent;
        return total > 0 ? (totalPresent * 100.0) / total : 0;
    }
}
