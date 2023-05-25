/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import java.awt.Checkbox;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class ActivityMemberTable {
    
    private SimpleStringProperty tblindexRow;
    private CheckBox select; 
    private SimpleStringProperty tblindex01;
    private SimpleStringProperty tblindex24; //sDeptName
    private SimpleStringProperty tblindex25; //sCompnyNm

    public ActivityMemberTable(String tblindexRow, String tblindex01, String tblindex24,String tblindex25) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.select = new CheckBox();
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex24 = new SimpleStringProperty(tblindex24);
        this.tblindex25 = new SimpleStringProperty(tblindex25);
    }
 
    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
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
    
    
    
    
}
