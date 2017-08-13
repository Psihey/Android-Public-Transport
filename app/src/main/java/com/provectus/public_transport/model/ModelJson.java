package com.provectus.public_transport.model;

/**
 * Created by Psihey on 11.08.2017.
 */

public class ModelJson {
    //TODO : Change or delete when server will be available
    private long id;
    private long number;
    private String type;

    public ModelJson(long number, String type) {
        this.number = number;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ModelJson{" +
                "id=" + id +
                ", number=" + number +
                ", type='" + type + '\'' +
                '}';
    }
}
