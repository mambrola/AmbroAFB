/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.mapeditor.MapEditor;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.products.Product;
import ambroafb.products.ProductsSpecificsComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ProductDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML @ContentNotEmpty
    private TextField abbreviation;
    @FXML @ContentNotEmpty @ContentPattern(value = "[0-9]{2}", explain = "The content length must be 2.")
    private TextField former;
    @FXML @ContentNotEmpty
    private TextField descrip;
    @FXML @ContentNotEmpty
    private ProductsSpecificsComboBox specifics;
    @FXML @ContentNotEmpty @ContentPattern(value = "(^0|[1-9][0-9]*)([.][0-9]{1,2})?", explain = "The price content is incorect. Exp: 1.25")
    private TextField price;
    @FXML @ContentNotEmpty
    private CurrencyComboBox currency;
    @FXML @ContentMapEditor(explainKey = "Left number must be days counter.", explainValue = "Right number must be discount percent. Exp: 4.25")
    private MapEditor discounts;
    @FXML
    private CheckBox isAlive;
    @FXML
    private TextField maxCount;
    
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Product product, productBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        Utils.validateTextFieldContentListener(former, "[0-9]{1,2}");
        Utils.validateTextFieldContentListener(price, "(^0|[1-9][0-9]*)?([.]|[.][0-9]{1,2})?");
        currency.setShowCategoryALL(false);
        permissionToClose = true;
    }

    public void bindProduct(Product product) {
        this.product = product;
        if (product != null){
            abbreviation.textProperty().bindBidirectional(product.abbreviationProperty());
            former.textProperty().bindBidirectional(product.formerProperty());
            descrip.textProperty().bindBidirectional(product.descriptionProperty());
            specifics.valueProperty().bindBidirectional(product.specificProperty());
            price.textProperty().bindBidirectional(product.priceProperty());
            currency.valueProperty().bindBidirectional(product.currencyProperty());
            discounts.setItems(product.getDiscountsForMapEditor());
            maxCount.textProperty().bindBidirectional(product.notJurMaxCountProperty());
            isAlive.selectedProperty().bindBidirectional(product.isAliveProperty());
        }
    }
    
    public boolean anyComponentChanged(){
        return !product.compares(productBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            discounts.changeState(true);
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    public void setBackupProduct(Product productBackup) {
        this.productBackup = productBackup;
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    private void setDisableComponents() {
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
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