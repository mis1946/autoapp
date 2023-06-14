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
public class ActivityMemberTable {

    private SimpleStringProperty tblindexRow;
    private CheckBox select;
    private SimpleStringProperty tblindex24; //sDeptName
    private SimpleStringProperty tblindex14; //sDeptIDxx
    private SimpleStringProperty tblindex25; //sCompnyNm
    private SimpleStringProperty tblindex13; //sEmployID

    public ActivityMemberTable(String tblindexRow, String tblindex24, String tblindex14, String tblindex25, String tblindex13) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.select = new CheckBox();
        this.tblindex24 = new SimpleStringProperty(tblindex24);
        this.tblindex14 = new SimpleStringProperty(tblindex14);
        this.tblindex25 = new SimpleStringProperty(tblindex25);
        this.tblindex13 = new SimpleStringProperty(tblindex13);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindexRow() {
        return tblindexRow.get();
    }

    public void setTblindexRow(String tblindexRow) {
        this.tblindexRow.set(tblindexRow);
    }

    public String getTblindex24() {
        return tblindex24.get();
    }

    public void setTblindex24(String tblindex24) {
        this.tblindex24.set(tblindex24);
    }

    public String getTblindex14() {
        return tblindex14.get();
    }

    public void setTblindex14(String tblindex14) {
        this.tblindex14.set(tblindex14);
    }

    public String getTblindex25() {
        return tblindex25.get();
    }

    public void setTblindex25(String tblindex25) {
        this.tblindex25.set(tblindex25);
    }

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }

}
