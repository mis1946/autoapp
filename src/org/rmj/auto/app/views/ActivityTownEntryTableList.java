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
public class ActivityTownEntryTableList {

    private SimpleStringProperty tblRow;
    private CheckBox select;
    private SimpleStringProperty tblindexId01; //sTownIDxx
    private SimpleStringProperty tblCity; //

    public ActivityTownEntryTableList(String tblRow, String tblindexId01, String tblCity) {
        this.tblRow = new SimpleStringProperty(tblRow);
        this.select = new CheckBox();
        this.tblindexId01 = new SimpleStringProperty(tblindexId01);
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

    public String getTblindexId01() {
        return tblindexId01.get();
    }

    public void setTblindexId01(String tblindex01) {
        this.tblindexId01.set(tblindex01);
    }

    public String getTblCity() {
        return tblCity.get();
    }

    public void setTblCity(String tblCity) {
        this.tblCity.set(tblCity);
    }

}
