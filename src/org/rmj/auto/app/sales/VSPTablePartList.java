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
    private SimpleStringProperty tblindex15_Part;
    private SimpleStringProperty tblindex10_Part;
    private SimpleStringProperty tblindex08_Part;
    private SimpleStringProperty tblindex09_Part;
    private SimpleStringProperty tblindex05_Part;
    private SimpleStringProperty tblindex06_Part;
    private SimpleBooleanProperty tblindex11_Part;
    private CheckBox addOrNot;

    VSPTablePartList(String tblPartsRow,
            String tblindex15_Part,
            String tblindex10_Part,
            String tblindex08_Part,
            String tblindex06_Part,
            String tblindex05_Part,
            String tblindex09_Part,
            boolean tblindex11_Part) {
        this.tblPartsRow = new SimpleStringProperty(tblPartsRow);
        this.tblindex15_Part = new SimpleStringProperty(tblindex15_Part);
        this.tblindex10_Part = new SimpleStringProperty(tblindex10_Part);
        this.tblindex08_Part = new SimpleStringProperty(tblindex08_Part);
        this.tblindex09_Part = new SimpleStringProperty(tblindex09_Part);
        this.tblindex05_Part = new SimpleStringProperty(tblindex05_Part);
        this.tblindex06_Part = new SimpleStringProperty(tblindex06_Part);
        this.addOrNot = new CheckBox();
        this.tblindex11_Part = new SimpleBooleanProperty(tblindex11_Part);
        this.addOrNot.setSelected(tblindex11_Part);
        addOrNot.setDisable(true);
    }

    public String getTblPartsRow() {
        return tblPartsRow.get();
    }

    public void setTblPartsRow(String tblPartsRow) {
        this.tblPartsRow.set(tblPartsRow);
    }

    public String getTblindex15_Part() {
        return tblindex15_Part.get();
    }

    public void setTblindex15_Part(String tblindex15_Part) {
        this.tblindex15_Part.set(tblindex15_Part);
    }

    public String getTblindex10_Part() {
        return tblindex10_Part.get();
    }

    public void setTtblindex10_Part(String tblindex10_Part) {
        this.tblindex10_Part.set(tblindex10_Part);
    }

    public String getTblindex08_Part() {
        return tblindex08_Part.get();
    }

    public void setTtblindex08_Part(String tblindex08_Part) {
        this.tblindex08_Part.set(tblindex08_Part);
    }

    public String getTblindex09_Part() {
        return tblindex09_Part.get();
    }

    public void setTblindex09_Part(String tblindex09_Part) {
        this.tblindex09_Part.set(tblindex09_Part);
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

    public CheckBox getAddOrNot() {
        return addOrNot;
    }

    public void setAddOrNot(CheckBox addOrNot) {
        this.addOrNot = addOrNot;
    }

}
