package com.bustrackingapp;

public class BusModel {
    private Double latitude;

    private Double longtitude;

    private String stopingName;

    private String busNo;

    private String startTime;

    private String endTime;

    public BusModel(Double latitude,Double longtitude,
                    String stopingName,String busNo,String startTime,String endTime){
        this.latitude=latitude;
        this.longtitude=longtitude;
        this.stopingName=stopingName;
        this.busNo=busNo;
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public String getStopingName() {
        return stopingName;
    }

    public void setStopingName(String stopingName) {
        this.stopingName = stopingName;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
