/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.service;

import javafx.beans.property.SimpleStringProperty;

public class JobOrderLaborTableList {

    private SimpleStringProperty tblLaborRow;
    private SimpleStringProperty tblindex04_Labor;//sLaborCde
    private SimpleStringProperty tblindex10_Labor; // sLaborDsc
    private SimpleStringProperty tblindex03_Labor; // sPayChrge
    private SimpleStringProperty tblindex06_Labor; //nUnitPrce

    JobOrderLaborTableList(String tblLaborRow,
            String tblindex04_Labor,
            String tblindex10_Labor,
            String tblindex03_Labor,
            String tblindex06_Labor) {
        this.tblLaborRow = new SimpleStringProperty(tblLaborRow);
        this.tblindex04_Labor = new SimpleStringProperty(tblindex04_Labor);
        this.tblindex10_Labor = new SimpleStringProperty(tblindex10_Labor);
        this.tblindex03_Labor = new SimpleStringProperty(tblindex03_Labor);
        this.tblindex06_Labor = new SimpleStringProperty(tblindex06_Labor);
    }

    public String getTblLaborRow() {
        return tblLaborRow.get();
    }

    public void setTblLaborRow(String tblLaborRow) {
        this.tblLaborRow.set(tblLaborRow);
    }

    public String getTblindex04_Labor() {
        return tblindex04_Labor.get();
    }

    public void setTblindex04_Labor(String tblindex04_Labor) {
        this.tblindex04_Labor.set(tblindex04_Labor);
    }

    public String getTblindex10_Labor() {
        return tblindex10_Labor.get();
    }

    public void setTblindex10_Labor(String tblindex10_Labor) {
        this.tblindex10_Labor.set(tblindex10_Labor);
    }

    public String getTblindex03_Labor() {
        return tblindex03_Labor.get();
    }

    public void setTblindex03_Labor(String tblindex03_Labor) {
        this.tblindex03_Labor.set(tblindex03_Labor);
    }

    public String getTblindex06_Labor() {
        return tblindex06_Labor.get();
    }

    public void setTblindex06_Labor(String tblindex06_Labor) {
        this.tblindex06_Labor.set(tblindex06_Labor);
    }

}
