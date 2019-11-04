package com.example.classinteraction.utils;

public class ClassCode {
    private String class_code;
    private String name;
    private boolean active;

    public ClassCode(){}//no-argument constructor for read firebase

    public ClassCode(String class_code, String name, boolean active) {
        this.class_code = class_code;
        this.name = name;
        this.active = active;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getClass_code() {
        return class_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
