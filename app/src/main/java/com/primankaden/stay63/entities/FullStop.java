package com.primankaden.stay63.entities;

import com.primankaden.stay63.XmlUtils;

import org.w3c.dom.Element;

import java.util.List;

public class FullStop extends Stop {

    public List<String> getBusesMunicipal() {
        return busesMunicipal;
    }

    public void setBusesMunicipal(List<String> busesMunicipal) {
        this.busesMunicipal = busesMunicipal;
    }

    public List<String> getBusesCommercial() {
        return busesCommercial;
    }

    public void setBusesCommercial(List<String> busesCommercial) {
        this.busesCommercial = busesCommercial;
    }

    public List<String> getBusesPrigorod() {
        return busesPrigorod;
    }

    public void setBusesPrigorod(List<String> busesPrigorod) {
        this.busesPrigorod = busesPrigorod;
    }

    public List<String> getBusesSeason() {
        return busesSeason;
    }

    public void setBusesSeason(List<String> busesSeason) {
        this.busesSeason = busesSeason;
    }

    public List<String> getBusesSpecial() {
        return busesSpecial;
    }

    public void setBusesSpecial(List<String> busesSpecial) {
        this.busesSpecial = busesSpecial;
    }

    public List<String> getTrams() {
        return trams;
    }

    public void setTrams(List<String> trams) {
        this.trams = trams;
    }

    public List<String> getMetros() {
        return metros;
    }

    public void setMetros(List<String> metros) {
        this.metros = metros;
    }

    public List<String> getInfotabloExists() {
        return infotabloExists;
    }

    public void setInfotabloExists(List<String> infotabloExists) {
        this.infotabloExists = infotabloExists;
    }

    private List<String> busesMunicipal;
    private List<String> busesCommercial;
    private List<String> busesPrigorod;
    private List<String> busesSeason;
    private List<String> busesSpecial;
    private List<String> trams;
    private List<String> metros;
    private List<String> infotabloExists;

    @Override
    public void init(Element e) {
        super.init(e);
        setLatitude(XmlUtils.getDoubleValue(e, "latitude"));
        setLongitude(XmlUtils.getDoubleValue(e, "longitude"));
    }
}
