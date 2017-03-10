/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * @author dato
 */
public class ClientComboBox extends AnchorPane {
    
    private final Client clientALL = new Client();
    private final String separator = ", ";
    
    private final ComboBox<Client> clientsBox = new ComboBox<>();
    private final TextField comboBoxEditor = clientsBox.getEditor();
    private TextField searchField = new TextField();
    
    private int valueSelected = 0;
    private int movedInField = 0;
    private ObservableList<Client> items = FXCollections.observableArrayList();
    private FilteredList<Client> filteredList;
    
    public ClientComboBox(){
        addSceneComponentsToAnchorPane();
        setFeatures();

        // Field width must be equals to comboBox editor width:
        clientsBox.getEditor().widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            searchField.setPrefWidth(newValue.doubleValue());
        });
        
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            System.out.println("1   field.textProperty oldValue, newValue: " + oldValue + ", " + newValue);
            fieldTextChangeReaction(newValue);
        });
        
        clientsBox.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
//            System.out.println("2   box.valueProperty oldValue, newValue: " + oldValue + ", " + newValue);
            valueSelected = 1;
            movedInField = 0;
            if (newValue != null) {
                Platform.runLater(() -> {
                    clientsBox.requestFocus();
                    clientsBox.getEditor().end();
                    clientsBox.toFront();
                });
            }
        });
        
        clientsBox.getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            System.out.println("3   box.getEditor().textProperty oldValue, newValue: " + oldValue + ", " + newValue);
            if (valueSelected != 1 && movedInField != 1) {
//                System.out.println("ვწერ");

                Platform.runLater(() -> {
                    int currCaret = clientsBox.getEditor().getCaretPosition();
                    if (searchField.getText().equals(newValue)) {
                        fieldTextChangeReaction(newValue);
                    } else {
                        searchField.setText(newValue == null || newValue.equals("") ? "" : newValue);
                    }
                    searchField.requestFocus();
                    searchField.positionCaret(currCaret);
                    searchField.toFront();
                });
                movedInField = 1;
            }
            valueSelected = 0;
        });
        
        clientALL.setFirstName("ALL");
        clientALL.setRecId(0);
        items.add(clientALL);
        List<Client> clientsList = Client.getAllFromDB().stream().filter((Client c) -> c.getEmail() != null && !c.getEmail().isEmpty())
                                                    .collect(Collectors.toList());
        clientsList.sort((Client c1, Client c2) -> c1.getRecId() - c2.getRecId());
        items.addAll(clientsList);
//        setItems(items, (Client c) -> c.getShortDescrip(separator).get());
        setItems(items);
        clientsBox.setValue(clientALL);
    }
    
    private void addSceneComponentsToAnchorPane(){
        this.getChildren().add(clientsBox);
        this.getChildren().add(searchField);
    }
    
    private void fieldTextChangeReaction(String value) {
        filteredList.setPredicate((Client elem) -> {
            return elem.getShortDescrip(separator).get().toLowerCase().contains(value.toLowerCase());
        });
        Platform.runLater(() -> {
            clientsBox.hide();
            clientsBox.show();
        });
    }
    
    public void showCategoryALL(boolean show){
        if (!show && (!getItems().isEmpty() && getItems().get(0).getRecId() == 0)){
            getItems().remove(0);
        }
        
        if (show && (getItems().isEmpty() || getItems().get(0).getRecId() != 0)){
            getItems().add(0, clientALL);
        }
    }
    
    
    private void setFeatures(){
        clientsBox.setEditable(true);
        searchField.setPromptText("Search"); 
        
        comboBoxEditor.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            searchField.setMinWidth(newValue.doubleValue());
            searchField.setMaxWidth(newValue.doubleValue());
        });
        
        comboBoxEditor.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            searchField.setMinHeight(newValue.doubleValue());
            searchField.setMaxHeight(newValue.doubleValue());
        });
        
        clientsBox.setConverter(new CustomConverter());
        
        getStyleClass().add("blockAccessToChildrenFocus");
    }
    
    // Final keyword is needed for call in constructor. It must not be allowed to override.
    public final void setItems(ObservableList<Client> clientsList){ // Function<Client, String> clientFilterDataFn
        filteredList = new FilteredList(clientsList);
        items = clientsList;
        clientsBox.setItems(filteredList);
    }
    
    public final ObservableList<Client> getItems(){
        return items;
    }
    
    public ObjectProperty<Client> valueProperty(){
        return clientsBox.valueProperty();
    }
    
    public Client getValue(){
        return clientsBox.getValue();
    }
    
    public SingleSelectionModel<Client> getSelectionModel(){
        return clientsBox.getSelectionModel();
    }
    
    private class CustomConverter extends StringConverter<Client> {

        @Override
        public String toString(Client c) {
            return (c == null) ? "" : c.getShortDescrip(separator).get();
        }

        @Override
        public Client fromString(String input) {
            if (input.isEmpty()){
                return null;
            }
            int firstSeparatorIndex = input.indexOf(separator);
            String name = input.substring(0, firstSeparatorIndex);
            int secondSeparatorIndex = input.indexOf(separator, firstSeparatorIndex);
            String lastName = input.substring(firstSeparatorIndex + separator.length(), secondSeparatorIndex);
            int emailStartIndex = secondSeparatorIndex + separator.length();
            String email = input.substring(emailStartIndex);
            return getItems().stream().filter((Client c) -> c.getFirstName().equals(name) && c.getLastName().equals(lastName) && c.getEmail().equals(email)).collect(Collectors.toList()).get(0);
        }
        
    }
}
