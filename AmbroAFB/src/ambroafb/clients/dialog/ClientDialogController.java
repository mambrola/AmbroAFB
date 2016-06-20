/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.countries.*;
import ambroafb.phones.PhoneComboBox;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ambroafb.general.interfaces.Annotations.*;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientDialogController implements Initializable {
    @FXML
    private VBox formPane, phonesContainer;
    @FXML
    private ADatePicker openDate;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private Label first_name, last_name;
    
    @FXML  
    @IsNotEpmty
    private TextField firstName, lastName, address, idNumber;
    @FXML
    private TextField fax, zipCode, city;
    
    @FXML 
    @IsNotEpmty 
    @ContentPattern(".+@.+\\..+")
    private TextField email;
    @FXML
    private CountryComboBox country;
    @FXML
    private DialogOkayCancelController okayCancelController;

    private ArrayList<Node> focusTraversableNodes;
    private final GeneralConfig conf = GeneralConfig.getInstance();
    private Client client;
    private Client clientBackup;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        juridical.setOnAction(this::switchJuridical);
    }

    public void bindClient(Client client) {
        this.client = client;
        if (client != null) {
            openDate.setValue(getClientCreatedDate());
            juridical.selectedProperty().bindBidirectional(client.isJurProperty());
            rezident. selectedProperty().bindBidirectional(client.isRezProperty());
            firstName.    textProperty().bindBidirectional(client.firstNameProperty());
            lastName.     textProperty().bindBidirectional(client.lastNameProperty());
            idNumber.     textProperty().bindBidirectional(client.IDNumberProperty());
            email.        textProperty().bindBidirectional(client.emailProperty());
            fax.          textProperty().bindBidirectional(client.faxProperty());
            address.      textProperty().bindBidirectional(client.addressProperty());
            zipCode.      textProperty().bindBidirectional(client.zipCodeProperty());
            city.         textProperty().bindBidirectional(client.cityProperty());
            country.     valueProperty().bindBidirectional(client.countryProperty());
        }
    }
    
    private LocalDate getClientCreatedDate(){
        LocalDate result = null;
        String date = client.createdDate;
        if (date != null){
            int beforeTime = date.indexOf(" ");
            String onlyDatePart = client.createdDate.substring(0, beforeTime);
            result = LocalDate.parse(onlyDatePart);
        }
        return result;
    }
    
    public void setBackupClient(Client backupClient){
        this.clientBackup = backupClient;
    }
    
    public boolean anyFieldChanged(){
        return !client.compares(clientBackup);
    }
    
    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        openDate.setDisable(true);
        boolean editable = true;
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            editable = false;
        }
        if (client != null){
            PhoneComboBox phonesCombobox = new PhoneComboBox(client.getPhoneList(), editable);
            phonesContainer.getChildren().add(phonesCombobox);
        }
        
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void switchJuridical(ActionEvent e) {
        double w = firstName.widthProperty().getValue() + lastName.widthProperty().getValue();
        if (((CheckBox) e.getSource()).isSelected()) {
            first_name.setText(conf.getTitleFor("firm_name"));
            last_name.setText(conf.getTitleFor("firm_form"));
            firstName.setPrefWidth(0.75 * w);
            lastName.setPrefWidth(0.25 * w);
        } else {
            first_name.setText(conf.getTitleFor("first_name"));
            last_name.setText(conf.getTitleFor("last_name"));
            firstName.setPrefWidth(0.50 * w);
            lastName.setPrefWidth(0.50 * w);
        }
    }
    
    
    /**
     * Disables all fields on Dialog stage except phones.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
