package com.kkalkkalparrot.daily;

public class HabitCheck implements Cloneable{
    private Habit habit;
    private boolean complete = false;

    public void set_complete(boolean complete){
        this.complete = complete;
    }
    public boolean get_complete(){ return this.complete; }

    @Override
    protected HabitCheck clone() throws CloneNotSupportedException{
        return (HabitCheck) super.clone();
    }

    public Habit getHabit(){
        return this.habit;
    }

    public HabitCheck(Habit habit){
        this.habit = habit;
    }
}
