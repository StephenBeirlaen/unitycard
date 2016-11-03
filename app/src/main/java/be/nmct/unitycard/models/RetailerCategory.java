package be.nmct.unitycard.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class RetailerCategory {
    @SerializedName("Id")
    private int id;

    @SerializedName("Name")
    private String name;

    public RetailerCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
