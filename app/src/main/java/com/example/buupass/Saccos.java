package com.example.buupass;

public class Saccos {
    String SaccoName;
    String RegistrationNumber;
    String KRA_pin;
    String Location;
    String matatu_BusName;
    String uid;


    public Saccos(String saccoName, String registrationNumber, String KRA_pin, String location, String matatu_BusName, String uid) {
        this.SaccoName = saccoName;
        this.RegistrationNumber = registrationNumber;
        this.KRA_pin = KRA_pin;
        this.Location = location;
        this.matatu_BusName = matatu_BusName;
        this.uid=uid;
    }

    public String getSaccoName() {
        return SaccoName;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public String getKRA_pin() {
        return KRA_pin;
    }

    public String getLocation() {
        return Location;
    }

    public String getMatatu_BusName() {
        return matatu_BusName;
    }
    public String getUid(){
        return uid;
    }
}
