package com.example.buupass;

public class User {
    public String fullname, email, phonenumber, Role, Gender;
    public User(){

    }
    public User(String fullname, String email, String phonenumber, String radioMF,String spinnerroles){
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.Gender = radioMF;
        this.Role = spinnerroles;

    }
}
