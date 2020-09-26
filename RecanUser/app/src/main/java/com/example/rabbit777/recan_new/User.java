package com.example.rabbit777.recan_new;

public class User {
    public String name,subname,phone,email ,Barcode,password,point,URL;

public User()
{

}


    public User(String name, String subname,String phone ,String email ,String Barcode , String password ,String point,String URL)
    {
        this.name = name;
        this.subname = subname;
        this.phone = phone;
        this.email = email;
        this.Barcode = Barcode;
        this.password = password;
        this.point = point;
        this.URL = URL;
    }


    public String getPassword() {
        return password;
    }


}

