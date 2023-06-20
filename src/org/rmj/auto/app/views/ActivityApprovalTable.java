/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class ActivityApprovalTable {

    private SimpleStringProperty tblRow;
    private CheckBox select;
    private SimpleStringProperty tblindex17; //cTranStat
    private SimpleStringProperty tblindex01; //sActvtyID
    private SimpleStringProperty tblindex02; //sActTitle
    private SimpleStringProperty tblindex03; //sActDescx
    private SimpleStringProperty tblindex29; //sActTypID
//    private SimpleStringProperty tblindex05; //sActSrcex
    private SimpleStringProperty tblindex06; //dDateFrom
    private SimpleStringProperty tblindex07; //dDateThru
    private SimpleStringProperty tblindex08; //sLocation
    private SimpleStringProperty tblindex09; //sCompnynx
    private SimpleStringProperty tblindex10; //nPropBdgt
    private SimpleStringProperty tblindex24; //sDeptName
    private SimpleStringProperty tblindex25; //sCompnyNm
    private SimpleStringProperty tblindex26; //sBranchNm
    private SimpleStringProperty tblindex28; //sProvName

    public ActivityApprovalTable(String tblRow,
            String tblindex17,
            String tblindex01,
            String tblindex02,
            String tblindex03,
            String tblindex29,
            //            String tblindex05,
            String tblindex06,
            String tblindex07,
            String tblindex08,
            String tblindex09,
            String tblindex10,
            String tblindex24,
            String tblindex25,
            String tblindex26,
            String tblindex28
    ) {
        this.tblRow = new SimpleStringProperty(tblRow);
        this.select = new CheckBox();
        this.tblindex17 = new SimpleStringProperty(tblindex17);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex29 = new SimpleStringProperty(tblindex29);
//        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex06 = new SimpleStringProperty(tblindex06);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex10 = new SimpleStringProperty(tblindex10);
        this.tblindex24 = new SimpleStringProperty(tblindex24);
        this.tblindex25 = new SimpleStringProperty(tblindex25);
        this.tblindex26 = new SimpleStringProperty(tblindex26);
        this.tblindex28 = new SimpleStringProperty(tblindex28);

    }

    public String getTblRow() {
        return tblRow.get();
    }

    public void setTblRow(String tblRow) {
        this.tblRow.set(tblRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblindex17() {
        return tblindex17.get();
    }

    public void setTblindex17(String tblindex17) {
        this.tblindex17.set(tblindex17);
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

    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }

    public String getTblindex29() {
        return tblindex29.get();
    }

    public void setTblindex04(String tblindex29) {
        this.tblindex29.set(tblindex29);
    }

//    public String getTblindex05() {
//        return tblindex05.get();
//    }
//
//    public void setTblindex05(String tblindex05) {
//        this.tblindex05.set(tblindex05);
//    }
    public String getTblindex06() {
        return tblindex06.get();
    }

    public void setTblindex06(String tblindex06) {
        this.tblindex06.set(tblindex06);
    }

    public String getTblindex07() {
        return tblindex07.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }

    public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }

    public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }

    public String getTblindex10() {
        return tblindex10.get();
    }

    public void setTblindex10(String tblindex10) {
        this.tblindex10.set(tblindex10);
    }

    public String getTblindex24() {
        return tblindex24.get();
    }

    public void setTblindex24(String tblindex24) {
        this.tblindex24.set(tblindex24);
    }

    public String getTblindex25() {
        return tblindex25.get();
    }

    public void setTblindex25(String tblindex25) {
        this.tblindex25.set(tblindex25);
    }

    public String getTblindex26() {
        return tblindex26.get();
    }

    public void setTblindex26(String tblindex26) {
        this.tblindex26.set(tblindex26);
    }

    public String getTblindex28() {
        return tblindex28.get();
    }

    public void setTblindex28(String tblindex28) {
        this.tblindex28.set(tblindex28);
    }

}
