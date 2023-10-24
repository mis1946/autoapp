/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author User
 */
public class VSPTableLaborList {

    private SimpleStringProperty tblLaborRow;
    private SimpleStringProperty tblindex03_Labor;
    private SimpleStringProperty tblindex07_Labor;
    private SimpleStringProperty tblindex05_Labor;
    private SimpleStringProperty tblindex04_Labor;
    private SimpleStringProperty tblindex04_Labor_Rust;
    private SimpleStringProperty tblindex04_Labor_Perma;
    private SimpleStringProperty tblindex04_Labor_Under;
    private SimpleStringProperty tblindex04_Labor_Tint;
    private CheckBox addOrNot;
    private SimpleBooleanProperty tblindex08;

    VSPTableLaborList(String tblLaborRow,
            String tblindex03_Labor,
            String tblindex07_Labor,
            String tblindex05_Labor,
            String tblindex04_Labor,
            String tblindex04_Labor_Rust,
            String tblindex04_Labor_Perma,
            String tblindex04_Labor_Under,
            String tblindex04_Labor_Tint,
            boolean tblindex08) {
        this.tblLaborRow = new SimpleStringProperty(tblLaborRow);
        this.tblindex03_Labor = new SimpleStringProperty(tblindex03_Labor);
        this.tblindex07_Labor = new SimpleStringProperty(tblindex07_Labor);
        this.tblindex05_Labor = new SimpleStringProperty(tblindex05_Labor);
        this.tblindex04_Labor = new SimpleStringProperty(tblindex04_Labor);
        this.tblindex04_Labor_Rust = new SimpleStringProperty(tblindex04_Labor_Rust);
        this.tblindex04_Labor_Perma = new SimpleStringProperty(tblindex04_Labor_Perma);
        this.tblindex04_Labor_Under = new SimpleStringProperty(tblindex04_Labor_Under);
        this.tblindex04_Labor_Tint = new SimpleStringProperty(tblindex04_Labor_Tint);
        this.addOrNot = new CheckBox();
        this.tblindex08 = new SimpleBooleanProperty(tblindex08);
        this.addOrNot.setSelected(tblindex08);
        addOrNot.setDisable(true);
    }

    public String getTblLaborRow() {
        return tblLaborRow.get();
    }

    public void setTblLaborRow(String tblLaborRow) {
        this.tblLaborRow.set(tblLaborRow);
    }

    public String getTblindex03_Labor() {
        return tblindex03_Labor.get();
    }

    public void setTblindex03_Labor(String tblLaborRow) {
        this.tblindex03_Labor.set(tblLaborRow);
    }

    public String getTblindex07_Labor() {
        return tblindex07_Labor.get();
    }

    public void setTblindex07_Labor(String tblindex07_Labor) {
        this.tblindex07_Labor.set(tblindex07_Labor);
    }

    public String getTblindex05_Labor() {
        return tblindex05_Labor.get();
    }

    public void setTblindex05_Labor(String tblindex05_Labor) {
        this.tblindex05_Labor.set(tblindex05_Labor);
    }

    public String getTblindex04_Labor() {
        return tblindex04_Labor.get();
    }

    public void setTblindex04_Labor(String tblindex04_Labor) {
        this.tblindex04_Labor.set(tblindex04_Labor);
    }

    public String getTblindex04_Labor_Rust() {
        return tblindex04_Labor_Rust.get();
    }

    public void setTblindex04_Labor_Rust(String tblindex04_Labor_Rust) {
        this.tblindex04_Labor_Rust.set(tblindex04_Labor_Rust);
    }

    public String getTblindex04_Labor_Perma() {
        return tblindex04_Labor_Perma.get();
    }

    public void setTblindex04_Labor_Perma(String tblindex04_Labor_Perma) {
        this.tblindex04_Labor_Perma.set(tblindex04_Labor_Perma);
    }

    public String getTblindex04_Labor_Under() {
        return tblindex04_Labor_Under.get();
    }

    public void setTblindex04_Labor_Under(String tblindex04_Labor_Under) {
        this.tblindex04_Labor_Under.set(tblindex04_Labor_Under);
    }

    public String getTblindex04_Labor_Tint() {
        return tblindex04_Labor_Tint.get();
    }

    public void setTblindex04_Labor_Tint(String tblindex04_Labor_Tint) {
        this.tblindex04_Labor_Tint.set(tblindex04_Labor_Tint);

    }

    public CheckBox getAddOrNot() {
        return addOrNot;
    }

    public void setAddOrNot(CheckBox addOrNot) {
        this.addOrNot = addOrNot;
    }

    public boolean isTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(boolean tblindex08) {
        this.tblindex08.set(tblindex08);
    }

    public BooleanProperty selectedProperty() {
        return tblindex08;
    }

    public SimpleStringProperty laborProperty() {
        return tblindex07_Labor;
    }

}
