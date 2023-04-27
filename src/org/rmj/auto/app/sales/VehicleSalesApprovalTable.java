/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.sales;

import java.awt.Checkbox;
import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author User
 */
public class VehicleSalesApprovalTable {
    
    private SimpleStringProperty tblRow;
//    private SimpleBooleanProperty tblselected;
    private CheckBox select;
    private SimpleStringProperty tblslipNo;
    private SimpleStringProperty tbltype;
    private SimpleStringProperty tblslipDate;
    private SimpleStringProperty tblcustomerName;
    private SimpleStringProperty tblunitDescription;
    private SimpleStringProperty tblamount;
    private SimpleStringProperty tblseName;
    private SimpleStringProperty tblbranch;

    public VehicleSalesApprovalTable(String tblRow,String tblslipNo, String tbltype, String tblslipDate, String tblcustomerName, String tblunitDescription, String tblamount, String tblseName, String tblbranch) {
        this.tblRow = new SimpleStringProperty(tblRow);
//        this.tblselected = new SimpleBooleanProperty(tblselected);
        this.select = new CheckBox();
        this.tblslipNo = new SimpleStringProperty(tblslipNo);
        this.tbltype = new SimpleStringProperty(tbltype);
        this.tblslipDate = new SimpleStringProperty(tblslipDate);
        this.tblcustomerName = new SimpleStringProperty(tblcustomerName);
        this.tblunitDescription = new SimpleStringProperty(tblunitDescription);
        this.tblamount = new SimpleStringProperty(tblamount);
        this.tblseName = new SimpleStringProperty(tblseName);
        this.tblbranch = new SimpleStringProperty(tblbranch);
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
    public String getTblslipNo() {
        return tblslipNo.get();
    }

    public void setTblslipNo(String tblslipNo) {
        this.tblslipNo.set(tblslipNo);
    }

    public String getTbltype() {
        return tbltype.get();
    }

    public void setTbltype(String tbltype) {
        this.tbltype.set(tbltype);
    }
    

    public String getTblslipDate() {
        return tblslipDate.get();
    }

    public void setTblslipDate(String tblslipDate) {
        this.tblslipDate.set(tblslipDate);
    }

    public String getTblcustomerName() {
        return tblcustomerName.get();
    }

    public void setTblcustomerName(String tblcustomerName) {
        this.tblcustomerName.set(tblcustomerName);
    }

    public String getTblunitDescription() {
        return tblunitDescription.get();
    }

    public void setTblunitDescription(String tblunitDescription) {
        this.tblunitDescription.set(tblunitDescription);
    }

    public String getTblamount() {
        return tblamount.get();
    }

    public void setTblamount(String tblamount) {
        this.tblamount.set(tblamount);
    }

    public String getTblseName() {
        return tblseName.get();
    }

    public void setTblseName(String tblseName) {
        this.tblseName.set(tblseName);
    }

    public String getTblbranch() {
        return tblbranch.get();
    }

    public void setTblbranch(String tblbranch) {
        this.tblbranch.set(tblbranch);
    }

   
}
