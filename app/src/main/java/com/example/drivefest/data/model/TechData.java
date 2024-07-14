package com.example.drivefest.data.model;

import java.util.Map;

public class TechData {
    private String insurance;
    private String tech;
    private String course;
    private String engineOil;
    private String transmissionOil;

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEngineOil() {
        return engineOil;
    }

    public void setEngineOil(String engineOil) {
        this.engineOil = engineOil;
    }

    public String getTransmissionOil() {
        return transmissionOil;
    }

    public void setTransmissionOil(String transmissionOil) {
        this.transmissionOil = transmissionOil;
    }

    public TechData(){
        this.insurance = null;
        this.tech = null;
        this.course = null;
        this.engineOil = null;
        this.transmissionOil = null;
    }

    public TechData(String insurance, String tech, String course, String engineOil, String transmissionOil){
        this.insurance = insurance;
        this.tech = tech;
        this.course = course;
        this.engineOil = engineOil;
        this.transmissionOil = transmissionOil;
    }

    public void setData(Map<String, Object> map){
        this.insurance = map.get("insurance").toString();
        this.tech =  map.get("tech").toString();
        this.course =  map.get("course").toString();
        this.engineOil =  map.get("engine").toString();
        this.transmissionOil =  map.get("transmission").toString();
    }
}
