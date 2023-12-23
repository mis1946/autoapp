/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author User
 */
public class JobOrderVSPPartsList {

    private SimpleStringProperty tblindexRow;
    private CheckBox select;
    private SimpleStringProperty tblindex13; // sBarCodex
    private SimpleStringProperty tblindex09; // sDescript
    private SimpleStringProperty tblindex08; // sChrgeTyp
    private SimpleStringProperty tblindex06; // nQuantity
    private SimpleStringProperty tblindex04; //nUnitPrce
    private SimpleStringProperty tblindex03; //sStockIDx
    private SimpleStringProperty tblindex14; //sDSNoxxxx
    private SimpleStringProperty tblindex15; //sDSCodexx
    private SimpleStringProperty tblindexType;
    private SimpleStringProperty tblindex16;

    public JobOrderVSPPartsList(String tblindexRow,
            String tblindex13,
            String tblindex09,
            String tblindex08,
            String tblindex06,
            String tblindex04,
            String tblindex14,
            String tblindex03,
            String tblindex15,
            String tblindexType,
            String tblindex16) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.select = new CheckBox();

        this.tblindex13 = new SimpleStringProperty(tblindex13);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex14 = new SimpleStringProperty(tblindex14);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex15 = new SimpleStringProperty(tblindex15);
        this.tblindexType = new SimpleStringProperty(tblindexType);
        this.tblindex16 = new SimpleStringProperty(tblindex16);
    }

    public String getTblindexRow() {
        return tblindexRow.get();
    }

    public void setTblindexRow(String tblindexRow) {
        this.tblindexRow.set(tblindexRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex15() {
        return tblindex15.get();
    }

    public void setTblindex15(String tblindex15) {
        this.tblindex15.set(tblindex15);
    }

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }

    public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }

    public String getTblindex06() {
        return tblindex06.get();
    }

    public void setTblindex06(String tblindex06) {
        this.tblindex06.set(tblindex06);
    }

    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

    public String getTblindex14() {
        return tblindex14.get();
    }

    public void setTblindex14(String tblindex14) {
        this.tblindex14.set(tblindex14);
    }

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindexType() {
        return tblindexType.get();
    }

    public void setTblindexType(String tblindexType) {
        this.tblindexType.set(tblindexType);
    }

    public String getTblindex16() {
        return tblindex16.get();
    }

    public void setTblindex16(String tblindex16) {
        this.tblindex16.set(tblindex16);
    }

}
