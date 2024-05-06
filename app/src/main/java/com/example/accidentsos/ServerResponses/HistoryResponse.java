package com.example.accidentsos.ServerResponses;

import java.util.ArrayList;

public class HistoryResponse {

    public class Datum {
        public String hospital_name;
        public String patient_name;
        public String attender_name;
        public String date;

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


    public String status;
    public String message;
    public ArrayList<Datum> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}
