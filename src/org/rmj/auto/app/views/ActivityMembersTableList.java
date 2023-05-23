/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave
 */
public class ActivityMembersTableList {
    
    private SimpleStringProperty tblActvtyMembersRow;
    private CheckBox select;
    private SimpleStringProperty tblindex01; // sTransNox
    private SimpleStringProperty tblindex07; // sCompnyNm
    private SimpleStringProperty tblindex08; //sDeptName 

    public ActivityMembersTableList(String tblActvtyMembersRow,String tblindex01, String tblindex07, String tblindex08) {
        this.tblActvtyMembersRow = new SimpleStringProperty(tblActvtyMembersRow); 
        this.select = select;
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        
    }

    public String getTblActvtyMembersRow() {
        return tblActvtyMembersRow.get();
    }

    public void setTblActvtyMembersRow(String tblActvtyMembersRow) {
        this.tblActvtyMembersRow.set(tblActvtyMembersRow);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
    
    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
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
    

}
