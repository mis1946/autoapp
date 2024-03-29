/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class VSPTableLaborList {

    private SimpleStringProperty tblLaborRow;
    private CheckBox select;
    private SimpleStringProperty tblindex01_Labor;
    private SimpleStringProperty tblindex03_Labor;
    private SimpleStringProperty tblindex07_Labor;
    private SimpleStringProperty tblindex05_Labor;
    private SimpleStringProperty tblindex04_Labor;
    private SimpleStringProperty tblindex04_Labor_Rust;
    private SimpleStringProperty tblindex04_Labor_Perma;
    private SimpleStringProperty tblindex04_Labor_Under;
    private SimpleStringProperty tblindex04_Labor_Tint;
    private SimpleStringProperty tblindex11_Labor;
    private SimpleStringProperty tblindex14_Labor;
    private SimpleStringProperty tblindex12_Labor;
    private CheckBox addOrNot;
    private SimpleBooleanProperty tblindex08;
    private SimpleBooleanProperty tblindex05;
    private CheckBox FreeOrNot;

    VSPTableLaborList(String tblLaborRow,
            String tblindex01_Labor,
            String tblindex03_Labor,
            String tblindex07_Labor,
            String tblindex05_Labor,
            String tblindex04_Labor,
            String tblindex04_Labor_Rust,
            String tblindex04_Labor_Perma,
            String tblindex04_Labor_Under,
            String tblindex04_Labor_Tint,
            String tblindex11_Labor,
            String tblindex14_Labor,
            String tblindex12_Labor,
            boolean tblindex08,
            boolean tblindex05
    ) {
        this.tblLaborRow = new SimpleStringProperty(tblLaborRow);
        this.select = new CheckBox();
        this.tblindex01_Labor = new SimpleStringProperty(tblindex01_Labor);
        this.tblindex03_Labor = new SimpleStringProperty(tblindex03_Labor);
        this.tblindex07_Labor = new SimpleStringProperty(tblindex07_Labor);
        this.tblindex05_Labor = new SimpleStringProperty(tblindex05_Labor);
        this.tblindex04_Labor = new SimpleStringProperty(tblindex04_Labor);
        this.tblindex04_Labor_Rust = new SimpleStringProperty(tblindex04_Labor_Rust);
        this.tblindex04_Labor_Perma = new SimpleStringProperty(tblindex04_Labor_Perma);
        this.tblindex04_Labor_Under = new SimpleStringProperty(tblindex04_Labor_Under);
        this.tblindex04_Labor_Tint = new SimpleStringProperty(tblindex04_Labor_Tint);
        this.tblindex11_Labor = new SimpleStringProperty(tblindex11_Labor);
        this.tblindex14_Labor = new SimpleStringProperty(tblindex14_Labor);
        this.tblindex12_Labor = new SimpleStringProperty(tblindex12_Labor);
        this.addOrNot = new CheckBox();
        this.tblindex08 = new SimpleBooleanProperty(tblindex08);
        this.addOrNot.setSelected(tblindex08);
        addOrNot.setDisable(true);
        this.FreeOrNot = new CheckBox();
        this.tblindex05 = new SimpleBooleanProperty(tblindex05);
        this.FreeOrNot.setSelected(tblindex05);
        FreeOrNot.setDisable(true);
    }

    public String getTblLaborRow() {
        return tblLaborRow.get();
    }

    public void setTblLaborRow(String tblLaborRow) {
        this.tblLaborRow.set(tblLaborRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex01_Labor() {
        return tblindex01_Labor.get();
    }

    public void setTblindex01_Labor(String tblindex01_Labor) {
        this.tblindex01_Labor.set(tblindex01_Labor);
    }

    public String getTblindex03_Labor() {
        return tblindex03_Labor.get();
    }

    public void setTblindex03_Labor(String tblindex03_Labor) {
        this.tblindex03_Labor.set(tblindex03_Labor);
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

    public String getTblindex11_Labor() {
        return tblindex11_Labor.get();
    }

    public void setTblindex11_Labor(String tblindex11_Labor) {
        this.tblindex11_Labor.set(tblindex11_Labor);

    }

    public String getTblindex14_Labor() {
        return tblindex14_Labor.get();
    }

    public void setTblindex14_Labor(String tblindex14_Labor) {
        this.tblindex14_Labor.set(tblindex14_Labor);

    }

    public String getTblindex12_Labor() {
        return tblindex12_Labor.get();
    }

    public void setTblindex12_Labor(String tblindex12_Labor) {
        this.tblindex12_Labor.set(tblindex12_Labor);

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

    public CheckBox getFreeOrNot() {
        return FreeOrNot;
    }

    public void setFreeOrNot(CheckBox FreeOrNot) {
        this.FreeOrNot = FreeOrNot;
    }

    public boolean isTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(boolean tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    public BooleanProperty selectedProperty5() {
        return tblindex05;
    }

    public SimpleStringProperty laborProperty() {
        return tblindex07_Labor;
    }

}
