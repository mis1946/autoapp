/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author User
 */
public class VSPTableLaborList {

    private SimpleStringProperty tblLaborRow;
    private ComboBox<String> comboBoxPurchaseType;
    private SimpleStringProperty tblindex03_Labor;
    private SimpleStringProperty tblindex08_Labor;
    private SimpleStringProperty tblindex05_Labor;
    private ComboBox<String> comboBoxChargesTo;
    private SimpleStringProperty tblindex06_Labor;
    private SimpleStringProperty tblindex04_Labor;

    VSPTableLaborList(String tblLaborRow,
            String tblindex03_Labor,
            String tblindex08_Labor,
            String tblindex05_Labor,
            String tblindex06_Labor,
            String tblindex04_Labor) {
        this.tblLaborRow = new SimpleStringProperty(tblLaborRow);
        this.comboBoxPurchaseType = new ComboBox();
        this.comboBoxPurchaseType.getItems().add("FOC");
        this.comboBoxPurchaseType.getItems().add("CHARGE");
        this.comboBoxPurchaseType.setValue(tblindex05_Labor);
        this.tblindex03_Labor = new SimpleStringProperty(tblindex03_Labor);
        this.tblindex08_Labor = new SimpleStringProperty(tblindex08_Labor);

        this.comboBoxChargesTo = new ComboBox();
        this.comboBoxChargesTo.getItems().add("C/O CLIENT");
        this.comboBoxChargesTo.getItems().add("C/O BANK");
        this.comboBoxChargesTo.setValue(tblindex06_Labor);

        this.tblindex05_Labor = new SimpleStringProperty(tblindex05_Labor);
        this.tblindex06_Labor = new SimpleStringProperty(tblindex06_Labor);
        this.tblindex04_Labor = new SimpleStringProperty(tblindex04_Labor);
    }

    public ComboBox<String> getComboBoxPurchaseType() {
        return comboBoxPurchaseType;
    }

    public void setComboBoxPurchaseType(ComboBox<String> comboBoxPurchaseType) {
        this.comboBoxPurchaseType = comboBoxPurchaseType;
    }

    public ComboBox<String> getComboBoxChargesTo() {
        return comboBoxChargesTo;
    }

    public void setComboBoxChargesTo(ComboBox<String> comboBoxChargesTo) {
        this.comboBoxChargesTo = comboBoxChargesTo;
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

    public String getTblindex08_Labor() {
        return tblindex08_Labor.get();
    }

    public void setTblindex08_Labor(String tblindex08_Labor) {
        this.tblindex08_Labor.set(tblindex08_Labor);
    }

    public String getTblindex05_Labor() {
        return tblindex05_Labor.get();
    }

    public void setTblindex05_Labor(String tblindex05_Labor) {
        this.tblindex05_Labor.set(tblindex05_Labor);
    }

    public String getTblindex06_Labor() {
        return tblindex06_Labor.get();
    }

    public void setTblindex06_Labor(String tblindex06_Labor) {
        this.tblindex06_Labor.set(tblindex06_Labor);
    }

    public String getTblindex04_Labor() {
        return tblindex04_Labor.get();
    }

    public void setTblindex04_Labor(String tblindex04_Labor) {
        this.tblindex04_Labor.set(tblindex04_Labor);
    }
}
