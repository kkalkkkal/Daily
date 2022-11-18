package com.kkalkkalparrot.daily;

public class Habit implements Cloneable{
    private String name;
    private int color;
    private boolean did = false;

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

    public void set_did(boolean did){
        this.did = did;
    }
    public boolean get_did(){ return this.did; }

    Habit(){}

    public Habit(String name, int color){
        set_name(name);
        set_Color(color);
    }
}
