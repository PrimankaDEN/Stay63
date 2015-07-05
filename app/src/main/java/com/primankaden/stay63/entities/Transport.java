package com.primankaden.stay63.entities;

import com.primankaden.stay63.XmlUtils;

import org.w3c.dom.Element;

public class Transport {
    public enum TransportType {
        BUS(""),
        TRAIN(""),
        TROLLEYBUS("");
        public final String typeTag;

        TransportType(String tag) {
            this.typeTag = tag;
        }

        public TransportType getByTag(String tag) {
            for (TransportType type : TransportType.values()) {
                if (type.typeTag.equals(tag)) {
                    return type;
                }
            }
            return BUS;
        }
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    private TransportType type;
    private String number;
    private String id;
    private String time;
    private String modelTitle;

    public void init(Element e) {
        setNumber(XmlUtils.getValue(e, "number"));
        setTime(XmlUtils.getValue(e, "time"));
    }
}
