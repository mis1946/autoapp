/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleStringProperty;

public class VSPTablePartList {

    private SimpleStringProperty tblPartsRow;
    private SimpleStringProperty tblindex01_Part;
    private SimpleStringProperty tblindex03_Part;
    private SimpleStringProperty tblindex14_Part;
    private SimpleStringProperty tblindex09_Part;
    private SimpleStringProperty tblindex08_Part;
    private SimpleStringProperty tblindex04_Part;
    private SimpleStringProperty tblindex06_Part;
    private SimpleStringProperty tblindex11_Part;
    private SimpleStringProperty tblindexTotAmnt;

    VSPTablePartList(String tblPartsRow,
            String tblindex01_Part,
            String tblindex03_Part,
            String tblindex14_Part,
            String tblindex09_Part,
            String tblindex08_Part,
            String tblindex06_Part,
            String tblindex04_Part,
            String tblindex11_Part,
            String tblindexTotAmnt
    ) {
        this.tblPartsRow = new SimpleStringProperty(tblPartsRow);
        this.tblindex01_Part = new SimpleStringProperty(tblindex01_Part);
        this.tblindex03_Part = new SimpleStringProperty(tblindex03_Part);
        this.tblindex14_Part = new SimpleStringProperty(tblindex14_Part);
        this.tblindex09_Part = new SimpleStringProperty(tblindex09_Part);
        this.tblindex08_Part = new SimpleStringProperty(tblindex08_Part);
        this.tblindex06_Part = new SimpleStringProperty(tblindex06_Part);
        this.tblindex04_Part = new SimpleStringProperty(tblindex04_Part);
        this.tblindex11_Part = new SimpleStringProperty(tblindex11_Part);
        this.tblindexTotAmnt = new SimpleStringProperty(tblindexTotAmnt);
    }

    public String getTblPartsRow() {
        return tblPartsRow.get();
    }

    public void setTblPartsRow(String tblPartsRow) {
        this.tblPartsRow.set(tblPartsRow);
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

    public String getTblindexTotAmnt() {
        return tblindexTotAmnt.get();
    }

    public void setTblindexTotAmnt(String tblindexTotAmnt) {
        this.tblindexTotAmnt.set(tblindexTotAmnt);
    }
}
