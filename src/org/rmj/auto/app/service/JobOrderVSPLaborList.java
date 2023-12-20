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
public class JobOrderVSPLaborList {

    private SimpleStringProperty tblindexRow;
    private CheckBox select;
    private SimpleStringProperty tblindex03; // LaborCd
    private SimpleStringProperty tblindex07; // sLaborDsc
    private SimpleStringProperty tblindex05; // sChrgeTyp
    private SimpleStringProperty tblindex04; //nLaborAmt
    private SimpleStringProperty tblindex11; //sDSNoxxxx

    public JobOrderVSPLaborList(String tblindexRow,
            String tblindex03,
            String tblindex07,
            String tblindex05,
            String tblindex04,
            String tblindex11) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.select = new CheckBox();
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex11 = new SimpleStringProperty(tblindex11);
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

    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

    public String getTblindex11() {
        return tblindex11.get();
    }

    public void setTblindex11(String tblindex11) {
        this.tblindex11.set(tblindex11);
    }
}
