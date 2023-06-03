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
public class ActivityVchlEntryTable {

    private SimpleStringProperty tblRow;
    private CheckBox select;
    private SimpleStringProperty tblSerial03;//sSerialID
    private SimpleStringProperty tblDescription04; //sDescript
    private SimpleStringProperty tblCs04; //sCSNoxxxx

    public ActivityVchlEntryTable(String tblRow, String tblSerial03, String tblDescription04, String tblCs04) {
        this.tblRow = new SimpleStringProperty(tblRow);
        this.select = new CheckBox();
        this.tblSerial03 = new SimpleStringProperty(tblSerial03);
        this.tblDescription04 = new SimpleStringProperty(tblDescription04);
        this.tblCs04 = new SimpleStringProperty(tblCs04);
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

    public String getTblSerial03() {
        return tblSerial03.get();
    }

    public void setTblSerial03(String tblSerial03) {
        this.tblSerial03.set(tblSerial03);
    }

    public String getTblDescription04() {
        return tblDescription04.get();
    }

    public void setTblDescription04(String tblDescription04) {
        this.tblDescription04.set(tblDescription04);
    }

    public String getTblCs04() {
        return tblCs04.get();
    }

    public void setTblCs04(String tblCs04) {
        this.tblCs04.set(tblCs04);
    }

}
