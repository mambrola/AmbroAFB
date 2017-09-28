/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.accounts.Account;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class Conversion extends EditorPanelable {

    private final ObjectProperty<LocalDate> docDate, docInDocDate;
    private final StringProperty sellCurrency, byingCurrency;
    private final ObjectProperty<Account> sellAccount, buyingAccount;
    private final StringProperty sellAmount, buyingAmount, descrip;
    
    private final float amountDefaultValue = -1;
    
    public Conversion(){
        docDate = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDate = new SimpleObjectProperty<>(LocalDate.now());
        
        sellCurrency = new SimpleStringProperty("");
        byingCurrency = new SimpleStringProperty("");
        
        sellAccount = new SimpleObjectProperty<>(new Account());
        buyingAccount = new SimpleObjectProperty<>(new Account());
        
        sellAmount = new SimpleStringProperty("" + amountDefaultValue);
        buyingAmount = new SimpleStringProperty("" + amountDefaultValue);
        
        descrip = new SimpleStringProperty("");
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDate;
    }
    
    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDate;
    }
    
    public StringProperty sellCurrencyProperty(){
        return sellCurrency;
    }
    
    public StringProperty buyingCurrencyProperty(){
        return byingCurrency;
    }
    
    public ObjectProperty<Account> sellAccountProperty(){
        return sellAccount;
    }

    public ObjectProperty<Account> buyingAccountProperty(){
        return buyingAccount;
    }
    
    public StringProperty sellAmountProperty(){
        return sellAmount;
    }
    
    public StringProperty buyingAmountProperty(){
        return buyingAmount;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    
    // Getters:
    public String getDocDate(){
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDate.get() == null) ? "" : docInDocDate.get().toString();
    }
    
    public String getSellCurrency(){
        return sellCurrency.get();
    }
    
    public String getBuyingCurrency(){
        return byingCurrency.get();
    }
    
    public Account getSellAccount(){
        return sellAccount.get();
    }
    
    public Account getBuyingAccount(){
        return buyingAccount.get();
    }
    
    public float getSellAmount(){
        return NumberConverter.stringToFloat(sellAmount.get(), 2);
    }
    
    public float getBuyingAmount(){
        return NumberConverter.stringToFloat(buyingAmount.get(), 2);
    }
    
    
    // Setters:
    public void setDocDate(String date) {
        this.docDate.set(DateConverter.getInstance().parseDate(date));
    }

    public void setDocInDocDate(String date) {
        this.docInDocDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setSellCurrency(String iso){
        this.sellCurrency.set(iso);
    }
    
    public void setBuyingCurrency(String iso){
        this.byingCurrency.set(iso);
    }
    
    public void setSellAccount(Account account){
        this.sellAccount.set(account);
    }
    
    public void setBuyingAccount(Account account){
        this.buyingAccount.set(account);
    }
    
    public void setSellAmount(float amount){
        this.sellAmount.set(NumberConverter.makeFloatStringBySpecificFraction(amount, 2));
    }
    
    public void setBuyingAmount(float amount){
        this.buyingAmount.set(NumberConverter.makeFloatStringBySpecificFraction(amount, 2));
    }
    
    
    @Override
    public Conversion cloneWithoutID() {
        Conversion clone = new Conversion();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Conversion cloneWithID() {
        Conversion clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Conversion otherConversion = (Conversion)other;
        setDocDate(otherConversion.getDocDate());
        setDocInDocDate(otherConversion.getDocInDocDate());
        setSellCurrency(otherConversion.getSellCurrency());
        setBuyingCurrency(otherConversion.getBuyingCurrency());
        setSellAccount(otherConversion.getSellAccount().cloneWithoutID());
        setBuyingAccount(otherConversion.getBuyingAccount().cloneWithoutID());
        setSellAmount(otherConversion.getSellAmount());
        setBuyingAmount(otherConversion.getBuyingAmount());
        // descrip property not change on scene, so need not copy.
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Conversion other = (Conversion)backup;
        return  docDate.get().equals(other.docDateProperty().get()) &&
                docInDocDate.get().equals(other.docInDocDateProperty().get()) &&
                getSellCurrency().equals(other.getSellCurrency()) &&
                Utils.avoidNullAndReturnString(getBuyingCurrency()).equals(other.getBuyingCurrency()) &&
                getSellAccount().compares(other.getSellAccount()) &&
                Utils.avoidNullAndReturnEmpty(getBuyingAccount()).compares(other.getBuyingAccount()) &&
                getSellAmount() == other.getSellAmount() &&
                getBuyingAmount() == other.getBuyingAmount();
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
