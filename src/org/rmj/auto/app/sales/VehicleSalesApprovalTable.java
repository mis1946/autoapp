/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.awt.Checkbox;
import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class VehicleSalesApprovalTable {
    
    private SimpleStringProperty tblRow;
//    private SimpleBooleanProperty tblselected;
    private SimpleStringProperty tblindex13; //sTransNox
    private CheckBox select;
    private SimpleStringProperty tblindex01; //sTransNox
    private SimpleStringProperty tblindex03; //sReferNox
    private SimpleStringProperty tblindex12; //cResrvTyp
    private SimpleStringProperty tblindex02; // dTransact
    private SimpleStringProperty tblindex20; // sCompnyNm
    private SimpleStringProperty tblindex23; // sDescript
    private SimpleStringProperty tblindex05; // nAmountxx
    private SimpleStringProperty tblindex24; // sSeNamexx 
    private SimpleStringProperty tblbranch; // Branch/

    public VehicleSalesApprovalTable(String tblRow, String tblindex13, String tblindex01, String tblindex03, String tblindex12, String tblindex02, String tblindex20, String tblindex23, String tblindex05, String tblindex24, String tblbranch) {
        this.tblRow = new SimpleStringProperty(tblRow);
//        this.tblselected = new SimpleBooleanProperty(tblselected);
        this.select = new CheckBox();
        this.tblindex01 = new SimpleStringProperty(tblindex13);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex12 = new SimpleStringProperty(tblindex12);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex20= new SimpleStringProperty(tblindex20);
        this.tblindex23 = new SimpleStringProperty(tblindex23);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex24 = new SimpleStringProperty(tblindex24);
        this.tblbranch = new SimpleStringProperty(tblbranch);
    }

    public String getTblindex13() {
        return tblindex13.get();
    }

    public void setTblindex13(String tblindex13) {
        this.tblindex13.set(tblindex13);
    }
    
    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
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

//    public boolean isTbselected() {
//         return tblselected.get();
//     }
//    public void setTblselected(boolean tblselected)
//     { 
//         this.tblselected.set(tblselected);
//     }
//    public BooleanProperty selectedProperty() 
//    {
//        return tblselected;
//    }
    public void setSelect(CheckBox select) {
        this.select = select;
    }
//    public void isSelect(CheckBox select) {
//        this.select = select;
//    }

//    }
    public String getTblindex03() {
        return tblindex03.get();
    }

    public void setTblindex03(String  tblindex03) {
        this. tblindex03.set(tblindex03);
    }

    public String getTblindex12() {
        return  tblindex12.get();
    }

    public void setTblindex12(String tblindex12) {
        this. tblindex12.set(tblindex12);
    }

    public String getTblindex02() {
        return  tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    public String getTblindex20() {
        return tblindex20.get();
    }

    public void setTblindex20(String tblindex20) {
        this.tblindex20.set(tblindex20);
    }

    public String getTblindex23() {
        return tblindex23.get();
    }

    public void setTblindex23(String tblindex23) {
        this.tblindex23.set(tblindex23);
    }

    public String getTblindex05() {
        return tblindex05.get();
    }

    public void setTbindex5(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }

    public String getTblindex24() {
        return tblindex24.get();
    }

    public void setTblseName(String tblindex24) {
        this.tblindex24.set(tblindex24);
    }

    public String getTblbranch() {
        return tblbranch.get();
    }

    public void setTblbranch(String tblbranch) {
        this.tblbranch.set(tblbranch);
    }

   
}
