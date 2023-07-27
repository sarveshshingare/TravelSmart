package com.example.travelsmart;

public class ReadWriteUserDetails {
    public String Dob,gender,mobile;

    //constructor
 public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textDob,String textGender,String textMobile)
    {

        this.Dob=textDob;
        this.gender=textGender;
        this.mobile=textMobile;
    }
}