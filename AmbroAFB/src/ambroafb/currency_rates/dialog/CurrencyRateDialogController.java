/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambro.ADatePicker;
import ambroafb.currency_rates.CurrencyRate;
import ambroafb.currency_rates.CurrencyRatesComboBox;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ambroafb.general.Names.*;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Dialogable;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyRateDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private ADatePicker currRateDate;
    
    @FXML
    private CurrencyRatesComboBox currRatesComboBox;
    @FXML
    private ProgressIndicator indocator;
    
    @FXML @ContentNotEmpty 
    private TextField count;
    
    @FXML @ContentNotEmpty @ContentPattern(value = "[0-9]{1}(\\.[0-9]{4}){1}", explain = "Rate pattern is incorrect!" )
    private TextField rate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    
    private CurrencyRate currencyRate;
    private CurrencyRate currencyRateBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        indocator.visibleProperty().bind(currRatesComboBox.disableProperty());
        indocator.setPrefSize(currRatesComboBox.getWidth(), currRatesComboBox.getHeight());
        currRatesComboBox.setShowCategoryALL(false);
        permissionToClose = true;
    }

    public void bindCurrencyRate(CurrencyRate currRate) {
        this.currencyRate = currRate;
        if (currRate != null){
            currRateDate.valueProperty().bindBidirectional(currRate.dateProperty());
            currRatesComboBox.valueProperty().bindBidirectional(currRate.isoProperty());
            count.textProperty().bindBidirectional(currRate.countProperty());
            rate.textProperty().bindBidirectional(currRate.rateProperty());
        }
    }

    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    /**
     * Disables all fields on Dialog stage.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void setBackupCurrencyRate(CurrencyRate currRateBackup) {
        this.currencyRateBackup = currRateBackup;
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    public boolean anyComponentChanged(){
        return !currencyRate.compares(currencyRateBackup);
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
}
