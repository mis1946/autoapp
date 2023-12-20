/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.service;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class JobOrderPartsTableList {

    private SimpleStringProperty tblPartsRow;
    private SimpleStringProperty tblindex14_Part;
    private SimpleStringProperty tblindex04_Part;
    private SimpleStringProperty tblindex11_Part;
    private SimpleStringProperty tblindex06_Part;
    private SimpleStringProperty tblindex10_Part;

    JobOrderPartsTableList(String tblPartsRow,
            String tblindex14_Part,
            String tblindex04_Part,
            String tblindex11_Part,
            String tblindex06_Part,
            String tblindex10_Part) {

        this.tblPartsRow = new SimpleStringProperty(tblPartsRow);
        this.tblindex14_Part = new SimpleStringProperty(tblindex14_Part);
        this.tblindex04_Part = new SimpleStringProperty(tblindex04_Part);
        this.tblindex11_Part = new SimpleStringProperty(tblindex11_Part);
        this.tblindex06_Part = new SimpleStringProperty(tblindex06_Part);
        this.tblindex10_Part = new SimpleStringProperty(tblindex10_Part);
    }

    public String getTblPartsRow() {
        return tblPartsRow.get();
    }

    public void setTblPartsRow(String tblPartsRow) {
        this.tblPartsRow.set(tblPartsRow);
    }

    public String getTblindex14_Part() {
        return tblindex14_Part.get();
    }

    public void setTblindex14_Part(String tblindex14_Part) {
        this.tblindex14_Part.set(tblindex14_Part);
    }

    public String getTblindex04_Part() {
        return tblindex04_Part.get();
    }

    public void setTblindex04_Part(String tblindex04_Part) {
        this.tblindex04_Part.set(tblindex04_Part);
    }

    public String getTblindex11_Part() {
        return tblindex11_Part.get();
    }

    public void setTblindex11_Part(String tblindex11_Part) {
        this.tblindex11_Part.set(tblindex11_Part);
    }

    public String getTblindex06_Part() {
        return tblindex06_Part.get();
    }

    public void setTblindex06_Part(String tblindex06_Part) {
        this.tblindex06_Part.set(tblindex06_Part);
    }

    public String getTblindex10_Part() {
        return tblindex10_Part.get();
    }

    public void setTblindex10_Part(String tblindex10_Part) {
        this.tblindex10_Part.set(tblindex10_Part);
    }

}
