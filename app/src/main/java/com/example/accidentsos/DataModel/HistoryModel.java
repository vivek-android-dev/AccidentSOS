package com.example.accidentsos.DataModel;

public class HistoryModel {

    public String hospital_name;
    public String patient_name;
    public String attender_name;
    public String date;

    public HistoryModel(String hospital_name, String patient_name, String attender_name, String date) {
        this.hospital_name = hospital_name;
        this.patient_name = patient_name;
        this.attender_name = attender_name;
        this.date = date;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getAttender_name() {
        return attender_name;
    }

    public void setAttender_name(String attender_name) {
        this.attender_name = attender_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
