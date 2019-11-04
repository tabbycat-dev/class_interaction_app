package com.example.classinteraction.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassCode implements Parcelable {
    private String class_code;
    private String name;
    private boolean active;

    public ClassCode(){}//no-argument constructor for read firebase

    public ClassCode(String class_code, String name, boolean active) {
        this.class_code = class_code;
        this.name = name;
        this.active = active;
    }
    public void updateClassCode(String class_code, String name, boolean active) {
        this.class_code = class_code;
        this.name = name;
        this.active = active;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator <ClassCode> CREATOR = new Creator<ClassCode>(){
        @Override
        public ClassCode createFromParcel(Parcel parcel) {
            return new ClassCode(parcel);
        }

        @Override
        public ClassCode[] newArray(int size) {
            return new ClassCode[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //PAY ATTENTION TO ORDER
        parcel.writeString(class_code);
        parcel.writeString(name);
        parcel.writeInt(active ? 1 : 0);
    }
    //Construct the object from the Parcel
    //MUST BE IN THE SAME ORDER WITH writeToParcel
    private ClassCode(Parcel parcel) {
        this.class_code = parcel.readString();
        this.name = parcel.readString();
        this.active = parcel.readInt()==1; //1 is true, 0 is false
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
