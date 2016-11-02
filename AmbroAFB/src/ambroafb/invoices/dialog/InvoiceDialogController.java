/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBox;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatus;
import ambroafb.licenses.License;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author dato
 */
public class InvoiceDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker createdDate, beginDate, endDate;
    @FXML
    private CheckBox revoked;
    @FXML
    private TextField invoiceNumber, additionalDiscount, moneyToPay, moneyPaid, vat;
    @FXML
    private ClientComboBox clients;
    @FXML
    private CountComboBox<License> licenses;
    @FXML
    private ComboBox<String> monthCount;
    @FXML
    private ComboBox<InvoiceReissuing> invoiceReissuings;
    @FXML
    private ComboBox<InvoiceStatus> statuses;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Invoice invoice;
    private Invoice invoiceBackup;
    private boolean permissionToClose;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        permissionToClose = true;
    }

    public void bindInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null){
            createdDate.setValue(invoice.getLocalDateObj());
            invoiceNumber.textProperty().bindBidirectional(invoice.invoiceNumberProperty());
            clients.valueProperty().bindBidirectional(invoice.clientProperty());
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
            endDate.valueProperty().bindBidirectional(invoice.endDateProperty());
            monthCount.valueProperty().bindBidirectional(invoice.monthsProperty());
            revoked.selectedProperty().bindBidirectional(invoice.revokedProperty());
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            moneyToPay.textProperty().bindBidirectional(invoice.moneyToPayProperty());
            moneyPaid.textProperty().bindBidirectional(invoice.moneyPaidProperty());
            vat.textProperty().bindBidirectional(invoice.vatProperty());
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            statuses.valueProperty().bindBidirectional(invoice.statusProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        boolean editable = true;
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            editable = false;
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
    
    public void setBackupInvoice(Invoice invoiceBackup) {
        this.invoiceBackup = invoiceBackup;
    }

    public boolean anyComponentChanged(){
        return !invoice.compares(invoiceBackup);
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
