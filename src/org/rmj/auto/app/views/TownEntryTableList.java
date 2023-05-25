/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class TownEntryTableList {
    
    
    private SimpleStringProperty tblRow;
    private CheckBox select;
    private SimpleStringProperty tblCity;

    public TownEntryTableList(String tblRow, String tblCity) {
        this.tblRow = new SimpleStringProperty(tblRow);
        this.select = new CheckBox();
        this.tblCity = new SimpleStringProperty(tblCity);
    }

    
    public String getTblRow() {
        return tblRow.get();
    }

    public void setTblRow(String tblRow) {
        this.tblRow.set(tblRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblCity() {
        return tblCity.get();
    }

    public void setTblCity(String tblCity) {
        this.tblCity.set(tblCity);
    }
    
    
}
