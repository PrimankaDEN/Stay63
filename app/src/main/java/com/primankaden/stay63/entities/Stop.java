package com.primankaden.stay63.entities;

import com.primankaden.stay63.XmlUtils;

import org.w3c.dom.Element;

public class Stop extends AbsPoint {
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdjacentStreet() {
        return adjacentStreet;
    }

    public void setAdjacentStreet(String adjacentStreet) {
        this.adjacentStreet = adjacentStreet;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getAdjacentStreetEn() {
        return adjacentStreetEn;
    }

    public void setAdjacentStreetEn(String adjacentStreetEn) {
        this.adjacentStreetEn = adjacentStreetEn;
    }

    public String getDirectionEn() {
        return directionEn;
    }

    public void setDirectionEn(String directionEn) {
        this.directionEn = directionEn;
    }

    private String id;
    private String title;
    private String adjacentStreet;
    private String direction;
    private String titleEn;
    private String adjacentStreetEn;
    private String directionEn;

    public void init(Element e) {
        setId(XmlUtils.getValue(e, "KS_ID"));
        setTitle(XmlUtils.getValue(e, "title"));
        setAdjacentStreet(XmlUtils.getValue(e, "adjacentStreet"));
        setDirection(XmlUtils.getValue(e, "direction"));
        setTitleEn(XmlUtils.getValue(e, "titleEn"));
        setAdjacentStreetEn(XmlUtils.getValue(e, "adjacentStreetEn"));
        setDirectionEn(XmlUtils.getValue(e, "directionEn"));
    }
}
