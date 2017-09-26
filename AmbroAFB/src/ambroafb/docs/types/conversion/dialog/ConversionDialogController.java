/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion.dialog;

import ambro.ADatePicker;
import ambroafb.accounts.AccountComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.docs.types.conversion.Conversion;
import ambroafb.general.Names;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class ConversionDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private IsoComboBox sellCurrency;
    @FXML @ContentNotEmpty
    private AccountComboBox sellAccount;
    @FXML @ContentNotEmpty @ContentPattern(value = "0\\.\\d*[1-9]\\d*|[1-9]\\d*(\\.\\d+)?", explain = "Amount text is incorrect")
    private TextField sellAmount;
    
    @FXML
    private IsoComboBox buyingCurrency;
    @FXML @ContentNotEmpty
    private AccountComboBox buyingAccount;
    @FXML @ContentNotEmpty @ContentPattern(value = "0\\.\\d*[1-9]\\d*|[1-9]\\d*(\\.\\d+)?", explain = "Amount text is incorrect")
    private TextField buyingAmount;
    @FXML
    private Button currentRate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    private Conversion conversion, conversionBackup;
    
    private boolean rateTopToBottomDirection = true;
    private final String topToBottomArrow = "\u2193";
    private final String bottomToTopArrow = "\u2191";
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
        
        sellAccount.fillComboBox();
        buyingAccount.fillComboBox();
        
        Utils.validateAmountField(sellAmount);
        Utils.validateAmountField(buyingAmount);
        
        sellCurrency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            sellAccount.filterBy(newValue);
            buyingCurrency.filterBy(newValue);
        });
        buyingCurrency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            buyingAccount.filterBy(newValue);
        });
        
        sellAmount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeRateText(newValue, buyingAmount.getText());
        });
        
        buyingAmount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeRateText(sellAmount.getText(), newValue);
        });
    }
    
    @FXML
    private void changeRateDirection(ActionEvent event){
        rateTopToBottomDirection = !rateTopToBottomDirection;
        changeRateText(sellAmount.getText(), buyingAmount.getText());
    }
    
    private void changeRateText(String sellAmountValue, String buyingAmountValue){
        if (sellAmountValue != null && !sellAmountValue.isEmpty() && buyingAmountValue != null && !buyingAmountValue.isEmpty()){
            float sellAmountFloat = Float.parseFloat(sellAmountValue);
            float buyingAmountFloat = Float.parseFloat(buyingAmountValue);
            String rateResult = "";
            String arrowSymbol = (rateTopToBottomDirection) ? topToBottomArrow : bottomToTopArrow;
            if (sellAmountFloat > 0 && buyingAmountFloat > 0){
                float amountsRate = (rateTopToBottomDirection) ? buyingAmountFloat / sellAmountFloat : sellAmountFloat / buyingAmountFloat;
                rateResult = NumberConverter.makeFloatSpecificFraction(amountsRate, 4);
            }
            currentRate.setText(arrowSymbol + "  " + rateResult);
        }
    }

    public void bindObject(Conversion conversion) {
        this.conversion = conversion;
        if(conversion != null){
            docDate.valueProperty().bindBidirectional(conversion.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(conversion.docInDocDateProperty());
            sellCurrency.valueProperty().bindBidirectional(conversion.sellCurrencyProperty());
            buyingCurrency.valueProperty().bindBidirectional(conversion.buyingCurrencyProperty());
            sellAccount.valueProperty().bindBidirectional(conversion.sellAccountProperty());
            buyingAccount.valueProperty().bindBidirectional(conversion.buyingAccountProperty());
            sellAmount.textProperty().bindBidirectional(conversion.sellAmountProperty());
            buyingAmount.textProperty().bindBidirectional(conversion.buyingAmountProperty());
            currentRate.textProperty().bindBidirectional(conversion.descripProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void setBackupDoc(Conversion conversionBackup) {
        this.conversionBackup = conversionBackup;
    }
    
    public boolean anyComponentChanged(){
        return !conversion.compares(conversionBackup);
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
