/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.parts;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Arsiela Date Created: 06-27-2023
 */
public class ItemEntryTableList {

    private SimpleStringProperty tblindexRow; //row
    private SimpleStringProperty tblindex01; //stockID
    private SimpleStringProperty tblindex02; //partNumber
    private SimpleStringProperty tblindex09; //brandName
    private SimpleStringProperty tblindex03; //Description
    private SimpleStringProperty tblindex04; //Brief Description
    private SimpleStringProperty tblindex12; //Unit Price
    private SimpleStringProperty tblindex07; //InvType
    private SimpleStringProperty tblindex33; //Category
    private SimpleStringProperty tblindex11; //Measurement

    ItemEntryTableList(String tblindexRow,
            String tblindex01,
            String tblindex02,
            String tblindex09,
            String tblindex03,
            String tblindex04,
            String tblindex12,
            String tblindex07,
            String tblindex33,
            String tblindex11
    ) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex12 = new SimpleStringProperty(tblindex12);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex33 = new SimpleStringProperty(tblindex33);
        this.tblindex11 = new SimpleStringProperty(tblindex11);
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

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
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

    public String getTblindex12() {
        return tblindex12.get();
    }

    public void setTblindex12(String tblindex12) {
        this.tblindex12.set(tblindex12);
    }

    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

    public String getTblindex33() {
        return tblindex33.get();
    }

    public void setTblindex33(String tblindex33) {
        this.tblindex33.set(tblindex33);
    }

    //
    public String getTblindex11() {
        return tblindex11.get();
    }

    public void setTblindex11(String tblindex11) {
        this.tblindex11.set(tblindex11);
    }

}
