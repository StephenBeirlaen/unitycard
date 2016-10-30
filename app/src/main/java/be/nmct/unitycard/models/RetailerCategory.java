package be.nmct.unitycard.models;

import java.util.List;

/**
 * Created by lorenzvercoutere on 30/10/16.
 */

public class RetailerCategory {
    private int id;
    private String name;
    private List<Retailer> retailers;

    public RetailerCategory(){}

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

    public List<Retailer> getRetailers() {
        return retailers;
    }

    public void setRetailers(List<Retailer> retailers) {
        this.retailers = retailers;
    }

}
