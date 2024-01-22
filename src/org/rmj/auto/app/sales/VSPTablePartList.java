/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class VSPTablePartList {

    private SimpleStringProperty tblPartsRow;
    private CheckBox select;
    private SimpleStringProperty tblindex01_Part;
    private SimpleStringProperty tblindex03_Part;
    private SimpleStringProperty tblindex14_Part;
    private SimpleStringProperty tblindex09_Part;
    private SimpleStringProperty tblindex08_Part;
    private SimpleStringProperty tblindex04_Part;
    private SimpleStringProperty tblindex06_Part;
    private SimpleStringProperty tblindex11_Part;
    private SimpleStringProperty tblindex20_Part;
    private SimpleStringProperty tblindexTotAmnt;
    private SimpleStringProperty tblindex17_Part;
    private SimpleStringProperty tblindex18_Part;
    private SimpleBooleanProperty tblindex08;
    private CheckBox FreeOrNot;

    VSPTablePartList(String tblPartsRow,
            String tblindex01_Part,
            String tblindex03_Part,
            String tblindex14_Part,
            String tblindex09_Part,
            String tblindex08_Part,
            String tblindex06_Part,
            String tblindex04_Part,
            String tblindex11_Part,
            String tblindex20_Part,
            String tblindexTotAmnt,
            String tblindex17_Part,
            String tblindex18_Part,
            boolean tblindex08
    ) {
        this.tblPartsRow = new SimpleStringProperty(tblPartsRow);
        this.select = new CheckBox();
        this.tblindex01_Part = new SimpleStringProperty(tblindex01_Part);
        this.tblindex03_Part = new SimpleStringProperty(tblindex03_Part);
        this.tblindex14_Part = new SimpleStringProperty(tblindex14_Part);
        this.tblindex09_Part = new SimpleStringProperty(tblindex09_Part);
        this.tblindex08_Part = new SimpleStringProperty(tblindex08_Part);
        this.tblindex06_Part = new SimpleStringProperty(tblindex06_Part);
        this.tblindex04_Part = new SimpleStringProperty(tblindex04_Part);
        this.tblindex11_Part = new SimpleStringProperty(tblindex11_Part);
        this.tblindex20_Part = new SimpleStringProperty(tblindex20_Part);
        this.tblindexTotAmnt = new SimpleStringProperty(tblindexTotAmnt);
        this.tblindex17_Part = new SimpleStringProperty(tblindex17_Part);
        this.tblindex18_Part = new SimpleStringProperty(tblindex18_Part);
        this.FreeOrNot = new CheckBox();
        this.tblindex08 = new SimpleBooleanProperty(tblindex08);
        this.FreeOrNot.setSelected(tblindex08);
        FreeOrNot.setDisable(true);
    }

    public String getTblPartsRow() {
        return tblPartsRow.get();
    }

    public void setTblPartsRow(String tblPartsRow) {
        this.tblPartsRow.set(tblPartsRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex01_Part() {
        return tblindex01_Part.get();
    }

    public void setTblindex01_Part(String tblindex01_Part) {
        this.tblindex01_Part.set(tblindex01_Part);
    }

    public String getTblindex03_Part() {
        return tblindex03_Part.get();
    }

    public void setTblindex03_Part(String tblindex03_Part) {
        this.tblindex03_Part.set(tblindex03_Part);
    }

    public String getTblindex14_Part() {
        return tblindex14_Part.get();
    }

    public void setTblindex14_Part(String tblindex14_Part) {
        this.tblindex14_Part.set(tblindex14_Part);
    }

    public String getTblindex09_Part() {
        return tblindex09_Part.get();
    }

    public void setTtblindex09_Part(String tblindex09_Part) {
        this.tblindex09_Part.set(tblindex09_Part);
    }

    public String getTblindex08_Part() {
        return tblindex08_Part.get();
    }

    public void setTtblindex08_Part(String tblindex08_Part) {
        this.tblindex08_Part.set(tblindex08_Part);
    }

    public String getTblindex06_Part() {
        return tblindex06_Part.get();
    }

    public void setTblindex06_Part(String tblindex06_Part) {
        this.tblindex06_Part.set(tblindex06_Part);
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

    public String getTblindex20_Part() {
        return tblindex20_Part.get();
    }

    public void setTblindex20_Part(String tblindex20_Part) {
        this.tblindex20_Part.set(tblindex20_Part);
    }

    public String getTblindexTotAmnt() {
        return tblindexTotAmnt.get();
    }

    public void setTblindexTotAmnt(String tblindexTotAmnt) {
        this.tblindexTotAmnt.set(tblindexTotAmnt);
    }

    public String getTblindex17_Part() {
        return tblindex17_Part.get();
    }

    public void setTblindex17_Part(String tblindex17_Part) {
        this.tblindex17_Part.set(tblindex17_Part);
    }

    public String getTblindex18_Part() {
        return tblindex18_Part.get();
    }

    public void setTblindex18_Part(String tblindex18_Part) {
        this.tblindex18_Part.set(tblindex18_Part);
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

}
