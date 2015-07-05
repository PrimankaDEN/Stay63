package com.primankaden.stay63.entities;

public class Route {
    private String id;
    private String number;
    private String direction;
    private String directionEn;
    private String transportTypeID;
    private String transportType;
    private String affiliationID;
    private String affiliation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirectionEn() {
        return directionEn;
    }

    public void setDirectionEn(String directionEn) {
        this.directionEn = directionEn;
    }

    public String getTransportTypeID() {
        return transportTypeID;
    }

    public void setTransportTypeID(String transportTypeID) {
        this.transportTypeID = transportTypeID;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getAffiliationID() {
        return affiliationID;
    }

    public void setAffiliationID(String affiliationID) {
        this.affiliationID = affiliationID;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }
}
