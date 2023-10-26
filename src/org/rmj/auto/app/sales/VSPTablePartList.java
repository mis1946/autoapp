/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author User
 */
public class VSPTablePartList {

    private SimpleStringProperty tblPartsRow;
    private SimpleStringProperty tblindex01_Part;
    private SimpleStringProperty tblindex14_Part;
    private SimpleStringProperty tblindex09_Part;
    private SimpleStringProperty tblindex08_Part;
    private SimpleStringProperty tblindex05_Part;
    private SimpleStringProperty tblindex06_Part;
    private SimpleStringProperty tblindex11_Part;

    VSPTablePartList(String tblPartsRow,
            String tblindex01_Part,
            String tblindex14_Part,
            String tblindex09_Part,
            String tblindex08_Part,
            String tblindex06_Part,
            String tblindex05_Part,
            String tblindex11_Part) {
        this.tblPartsRow = new SimpleStringProperty(tblPartsRow);
        this.tblindex01_Part = new SimpleStringProperty(tblindex01_Part);
        this.tblindex14_Part = new SimpleStringProperty(tblindex14_Part);
        this.tblindex09_Part = new SimpleStringProperty(tblindex09_Part);
        this.tblindex08_Part = new SimpleStringProperty(tblindex08_Part);
        this.tblindex05_Part = new SimpleStringProperty(tblindex05_Part);
        this.tblindex06_Part = new SimpleStringProperty(tblindex06_Part);
        this.tblindex11_Part = new SimpleStringProperty(tblindex11_Part);
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

    public String getTblindex05_Part() {
        return tblindex05_Part.get();
    }

    public void setTblindex05_Part(String tblindex05_Part) {
        this.tblindex05_Part.set(tblindex05_Part);
    }

    public String getTblindex11_Part() {
        return tblindex11_Part.get();
    }

    public void setTblindex11_Part(String tblindex11_Part) {
        this.tblindex11_Part.set(tblindex11_Part);
    }

}
