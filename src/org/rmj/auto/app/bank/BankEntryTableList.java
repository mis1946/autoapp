
package org.rmj.auto.app.bank;
 
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author John Dave
 */
public class BankEntryTableList {
    
    private SimpleStringProperty tblindexRow; //Row 
    private SimpleStringProperty tblindex01; //sBankID      
    private SimpleStringProperty tblindex02;// sBankName
    private SimpleStringProperty tblindex03;// sBankCode
    private SimpleStringProperty tblindex17;// sBankBrch
    private SimpleStringProperty tblindex05;// sAddressx
    private SimpleStringProperty tblindex16;// sTownProvxx
    private SimpleStringProperty tblindex15;// sProvName
    private SimpleStringProperty tblindex07;// sZipCode
    private SimpleStringProperty tblindex04;// sContactP
    private SimpleStringProperty tblindex08;// sTelNoxxx
    private SimpleStringProperty tblindex09;// sFaxNoxx
    private SimpleStringProperty tblindex18;// sTownName
    
    
    BankEntryTableList(String tblindexRow,
                       String tblindex01,
                       String tblindex02,
                       String tblindex03,
                       String tblindex17,
                       String tblindex05,
                       String tblindex16,
                       String tblindex15,
                       String tblindex07,
                       String tblindex04,
                       String tblindex08,
                       String tblindex09,
                       String tblindex18){
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.tblindex03 = new SimpleStringProperty(tblindex03);
        this.tblindex17 = new SimpleStringProperty(tblindex17);
        this.tblindex05 = new SimpleStringProperty(tblindex05);
        this.tblindex16 = new SimpleStringProperty(tblindex16);
        this.tblindex15 = new SimpleStringProperty(tblindex15);
        this.tblindex07 = new SimpleStringProperty(tblindex07);
        this.tblindex04 = new SimpleStringProperty(tblindex04);
        this.tblindex08 = new SimpleStringProperty(tblindex08);
        this.tblindex09 = new SimpleStringProperty(tblindex09);
        this.tblindex18 = new SimpleStringProperty(tblindex18);
 
    }
    
    public String getTblindexRow(){
        return tblindexRow.get();
    }
    public void setTblindexRow(String tblindexRow){
        this.tblindexRow.set(tblindexRow);
    }
    public String getTblindex01(){
        return tblindex01.get();
    }
    public void setTblindex01(String tblindex01){
        this.tblindex01.set(tblindex01);
    }
    // sBankName
    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }
    // sBankCode
      public String getTblindex03() {
        return tblindex03.get();
    }
  
    public void setTblindex03(String tblindex03) {
        this.tblindex03.set(tblindex03);
    }
    // sBankBrch
      public String getTblindex17() {
        return tblindex17.get();
    }
    public void setTblindex17(String tblindex17) {
        this.tblindex17.set(tblindex17);
    }
     // sAddressx
      public String getTblindex05() {
        return tblindex05.get();
    }
    
    public void setTblindex05(String tblindex05) {
        this.tblindex05.set(tblindex05);
    }
    // sTownNamexx
      public String getTblindex16() {
        return tblindex16.get();
    }

    public void setTblindex16(String tblindex16) {
        this.tblindex16.set(tblindex16);
    }
    // sProvName
      public String getTblindex15() {
        return tblindex15.get();
    }

    public void setTblindex15(String tblindex15) {
        this.tblindex15.set(tblindex15);
    }
    // sZipCode
      public String getTblindex07() {
        return tblindex02.get();
    }

    public void setTblindex07(String tblindex07) {
        this.tblindex07.set(tblindex07);
    }
    // sContactP
      public String getTblindex04() {
        return tblindex04.get();
    }

    public void setTblindex04(String tblindex04) {
        this.tblindex04.set(tblindex04);
    }
    // sTelNoxxx
      public String getTblindex08() {
        return tblindex08.get();
    }

    public void setTblindex08(String tblindex08) {
        this.tblindex08.set(tblindex08);
    }
    // sFaxNoxx
      public String getTblindex09() {
        return tblindex09.get();
    }

    public void setTblindex09(String tblindex09) {
        this.tblindex09.set(tblindex09);
    }
    public String getTblindex18(){
        return tblindex18.get();
    }
    public void setTblindex18(String tblindex18){
        this.tblindex18.set(tblindex18);
    }
   
    
}
