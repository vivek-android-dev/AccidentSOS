package com.example.accidentsos.DataModel;

public class NotificationModel {
    private String id;
    private String name;
    private String age;
    private String dob;
    private String gender;
    private String bloodgroup;
    private String mobilenumber;
    private String longitude;
    private String latitude;
    private String address;

    public NotificationModel(String id, String name, String age, String dob, String gender, String bloodgroup, String mobilenumber, String longitude, String latitude, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dob = dob;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.mobilenumber = mobilenumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
