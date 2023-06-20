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
    private SimpleStringProperty tblindexMem24; //sDeptName
    private SimpleStringProperty tblindexMem14; //sDeptIDxx
    private SimpleStringProperty tblindexMem25; //sCompnyNm
    private SimpleStringProperty tblindexMem13; //sEmployID

    public ActivityMemberTable(String tblindexRow, String tblindexMem24, String tblindexMem14, String tblindexMem25, String tblindexMem13) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.select = new CheckBox();
        this.tblindexMem24 = new SimpleStringProperty(tblindexMem24);
        this.tblindexMem14 = new SimpleStringProperty(tblindexMem14);
        this.tblindexMem25 = new SimpleStringProperty(tblindexMem25);
        this.tblindexMem13 = new SimpleStringProperty(tblindexMem13);
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

    public String getTblindexMem24() {
        return tblindexMem24.get();
    }

    public void setTblindexMem24(String tblindexMem24) {
        this.tblindexMem24.set(tblindexMem24);
    }

    public String getTblindexMem14() {
        return tblindexMem14.get();
    }

    public void setTblindex14(String tblindexMem14) {
        this.tblindexMem14.set(tblindexMem14);
    }

    public String getTblindexMem25() {
        return tblindexMem25.get();
    }

    public void setTblindexMem25(String tblindex25) {
        this.tblindexMem25.set(tblindex25);
    }

    public String getTblindexMem13() {
        return tblindexMem13.get();
    }

    public void setTblindexMem13(String tblindexMem13) {
        this.tblindexMem13.set(tblindexMem13);
    }

}
