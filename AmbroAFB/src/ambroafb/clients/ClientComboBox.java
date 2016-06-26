/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public ClientComboBox(){
        setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                String result = client.getFirstName()
                                .concat("\t").concat(client.getLastName())
                                .concat("\t").concat(client.getEmail());
                return result;
            }

            @Override
            public Client fromString(String data) {
                return null;
            }
        });

        this.getItems().addAll(Client.getAllFromDB());
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
}