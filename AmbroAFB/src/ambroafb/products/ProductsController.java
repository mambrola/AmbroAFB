/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ATableView;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.MaskerPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ProductsController implements Initializable {
    
    @FXML
    private ATableView<EditorPanelable> table;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> products = FXCollections.observableArrayList();
    private boolean permissionToClose;
    
    /**
     * 
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setBundle(rb);
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(table);
        editorPanelController.setTableDataList(table, products);
        permissionToClose = true;
    }
    
    public void reAssignTable(JSONObject jsonFilter) {
        if (jsonFilter != null && jsonFilter.length() == 0){
            products.clear();
            Platform.runLater(() -> {
                masker.setVisible(true);
            });
            new Thread(() -> {
                products.setAll(Product.getAllFromDB());
                Platform.runLater(() -> {
                    masker.setVisible(false);
                });
            }).start();
        }
    }
    
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
}
