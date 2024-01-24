/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.insurance;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author User
 */
public class InsuranceTableList {

    private SimpleStringProperty tblindexRow; //Row
    private SimpleStringProperty tblindex01; //ID
    private SimpleStringProperty tblindex03; // INSURANCE NAME
    private SimpleStringProperty tblindex04; // BRANCH
    private SimpleStringProperty tblindex18; // TOWNPROVINC

    InsuranceTableList(String tblindexRow,
            String tblindex01,
            String tblindex03,
            String tblindex04,
            String tblindex18
    ) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex18 = new SimpleStringProperty(tblindex18);
    }

    public String getTblindexRow() {
        return tblindexRow.get();
    }

    public void setTblindexRow(String tblindexRow) {
        this.tblindexRow.set(tblindexRow);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }

    public String getTblindex18() {
        return tblindex18.get();
    }

    public void setTblindex18(String tblindex18) {
        this.tblindex18.set(tblindex18);
    }
}
