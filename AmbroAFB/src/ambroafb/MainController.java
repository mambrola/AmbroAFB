/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.clients.Clients;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.countries.Countries;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.products.Products;
import ambroafb.products.filter.ProductFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class MainController implements Initializable {
    
    private GeneralConfig config;
    
    @FXML
    private AnchorPane formPane;
    @FXML
    private Button back;
    
    private boolean allowToClose;
    
    @FXML
    private void light(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/light/Light.fxml", config.getTitleFor("light"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION);
            alert.showAlert();
        }
    }
    @FXML
    private void autoDealers(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/auto_dealers/AutoDealers.fxml", config.getTitleFor("auto_dealers"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION);
            alert.showAlert();
        }
    }
    
    @FXML //დროებით აღარ ვიყენებ, მენიუს პუნქტიდან ამოვიღეთ და ჩავსვამთ ანგარიშთა სიაში ერთ-ერთ პიქტოგრამად
    private void newAccount(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/new_account/NewAccount.fxml", config.getTitleFor("open_new_account"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION);
            alert.showAlert();
        }
    }
    
    @FXML
    private void mainConfig(ActionEvent event) {
        try{
            Stage stage = Utils.createStage(
                    Names.CONFIGURATION_FXML, 
                    config.getTitleFor(Names.CONFIGURATION_TITLE), 
                    Names.CONFIGURATION_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION);
            alert.showAlert();
        }
    }
    
    @FXML
    public void mainExit(ActionEvent event){
        Utils.saveSizeFor(AmbroAFB.mainStage);
        Utils.closeOnlyChildrenStages(AmbroAFB.mainStage);
        if (allowToClose){
            Utils.exit();
        }
    }
    
    @FXML
    private void programsCarFines(ActionEvent event) {
        try{
            Stage stage = Utils.createMultiSceneStage(
                    Names.CAR_FINES_FXML, 
                    config.getTitleFor(Names.CAR_FINES_TITLE), 
                    Names.CAR_FINES_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        }catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_CAR_FINES_SCENE_START);
            alert.showAlert();
        }
    }
    
    @FXML
    private void clients(ActionEvent event) {
        String mainStagePath = Utils.getPathForStage(AmbroAFB.mainStage);
        String clientsStagePath = mainStagePath + "/" + Clients.class.getSimpleName();
        
        Stage clientsStage = Utils.getStageForPath(clientsStagePath);
        if(clientsStage == null || !clientsStage.isShowing()){
            Clients clients = new Clients(AmbroAFB.mainStage);
            clients.show();
            
            ClientFilter filter = new ClientFilter(clients);
            JSONObject json = filter.getResult();
            clients.getClientsController().reAssignTable(json);

            if (json != null && json.length() == 0) 
                clients.close();
        }
        else {
            clientsStage.requestFocus();
        }
    }
    
    @FXML 
    private void invoices(ActionEvent event) {
        try{
            Stage stage = Utils.createStage(
                    "/ambroafb/invoices/Invoices.fxml", 
                    config.getTitleFor("invoices"), 
                    Names.IN_OUT_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        }catch(IOException ex){
            Platform.runLater(() -> {
                AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
                alert.showAlert();
            });
        }
    }
    
    @FXML 
    private void products(ActionEvent event) {
        String productsStagePath = Utils.getPathForStage(AmbroAFB.mainStage) + "/" + Products.class.getSimpleName();
        Stage productsStage = Utils.getStageForPath(productsStagePath);
        if (productsStage == null || !productsStage.isShowing()){
            Products products = new Products(AmbroAFB.mainStage);
            products.show();
            
            ProductFilter filter = new ProductFilter(products);
            products.getProductsController().reAssignTable(filter.getResult());
        }
        else{
            productsStage.requestFocus();
        }
    }
    
    @FXML 
    private void countries(ActionEvent event) {
        String mainStagePath = Utils.getPathForStage(AmbroAFB.mainStage);
        String countriesStagePath = mainStagePath + "/" + Countries.class.getSimpleName();
        
        Stage countriesStage = Utils.getStageForPath(countriesStagePath);
        if (countriesStage == null || !countriesStage.isShowing()){
            Countries countries = new Countries(AmbroAFB.mainStage);
            countries.show();
        }
        else {
            countriesStage.requestFocus();
        }
    }
    
    public void changePermissionForClose(boolean value){
        allowToClose = value;
        System.out.println("MainController. allowToClose: allowToClose: " + allowToClose);
        System.out.println("MainController. Alert Cancel click...");
    }
    
    
    @FXML private void accounts(ActionEvent event) {}
    @FXML private void balances(ActionEvent event) {}
    @FXML private void account_statments(ActionEvent event) {}
    @FXML private void other(ActionEvent event) {}
    
//    @FXML
//    private void programsInOut(ActionEvent event) {
//        try{
//            Stage stage = Utils.createStage(
//                    Names.IN_OUT_FXML, 
//                    config.getTitleFor(Names.IN_OUT_TITLE), 
//                    Names.IN_OUT_LOGO,
//                    AmbroAFB.mainStage
//            );
//            stage.show();
//        }catch(IOException ex){
////            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
////            alert.showAlert();
//           
//            Platform.runLater(() -> {
//                AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
//                alert.showAlert();
//                System.out.println("errorr after");
//            });
//        }
//    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = GeneralConfig.getInstance();
        allowToClose = true;
    }        
}
