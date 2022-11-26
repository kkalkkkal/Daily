package com.kkalkkalparrot.daily;

public class Habit implements Cloneable{
    private String name;
    private int color;

    @Override
    protected Habit clone() throws CloneNotSupportedException{
        return (Habit) super.clone();
    }

    public void set_name(String name){
        this.name = name;
    }
    public String get_name(){
        return this.name;
    }

    public void set_Color(int color) { this.color = color; }
    public int get_Color() { return this.color; }

    Habit(){}

    public Habit(String name, int color){
        set_name(name);
        set_Color(color);
    }
}
