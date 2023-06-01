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
    private SimpleStringProperty tblindex01; //sTransNox
    private CheckBox select;
    private SimpleStringProperty tblCity; //sTransNox

    public TownEntryTableList(String tblRow, String tblindex01, String tblCity) {
        this.tblRow = new SimpleStringProperty(tblRow);
        this.select = new CheckBox();
        this.tblindex01 = new SimpleStringProperty(tblindex01);

    }

    public String getTblRow() {
        return tblRow.get();
    }

    public void setTblRow(String tblRow) {
        this.tblRow.set(tblRow);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
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
