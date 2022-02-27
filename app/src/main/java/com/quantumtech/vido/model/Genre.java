package com.quantumtech.vido.model;

import java.util.ArrayList;

public class Genre {

    private int id;
    private String name;
    private ArrayList<Movie> list;
    private boolean boxOffice;


    public Genre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Movie> getList() {
        return list;
    }

    public void setList(ArrayList<Movie> list) {
        this.list = list;
    }

    public void setBoxOffice(boolean boxOffice) {
        this.boxOffice = boxOffice;
    }

    public boolean getBoxOffice() {
        return boxOffice;
    }
}
