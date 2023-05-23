/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import java.awt.Checkbox;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author John Dave Aquino
 */
public class ActivityMunicipalityTable {
      private SimpleStringProperty tblRowCity;
      private Checkbox select;
      private SimpleStringProperty tblCity;

    public ActivityMunicipalityTable(String tblRowCity,String tblCity) {
        this.tblRowCity = new SimpleStringProperty(tblRowCity);
        this.select = select;
        this.tblCity = new SimpleStringProperty(tblCity);
    }

    public String getTblRowCity() {
        return tblRowCity.get();
    }

    public void setTblRowCity(String tblRowCity) {
        this.tblRowCity.set(tblRowCity);
    }

    public Checkbox getSelect() {
        return select;
    }

    public void setSelect(Checkbox select) {
        this.select = select;
    }

    public SimpleStringProperty getTblCity() {
        return tblCity;
    }

    public void setTblCity(String tblCity) {
        this.tblCity.set(tblCity);
    }
      
      
}
