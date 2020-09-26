package com.example.acerspin5.myapplication;

public class User {
    public String name,subname,phone,email ,Barcode,password,point;

public User()
{

}


    public User(String name, String subname, String phone , String email , String Barcode , String password , String point)
    {
        this.name = name;
        this.subname = subname;
        this.phone = phone;
        this.email = email;
        this.Barcode = Barcode;
        this.password = password;
        this.point = point;
    }


    public String getPassword() {
        return password;
    }


}

