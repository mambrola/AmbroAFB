/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class MainController implements Initializable {
    
    private GeneralConfig config;
    
    @FXML
    private Button back;
    
    @FXML
    private void light(ActionEvent event) {
        try{
            Stage stage = Utils.createStage(
                    "/ambroafb/light/Light.fxml", 
                    config.getTitleFor("light"), 
                    "/images/innerLogo.png",
                    AmbroAFB.mainStage
            );
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
    private void mainExit(ActionEvent event){
        Utils.exit();
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
        try{
            Stage stage = Utils.createStage(
                    "/ambroafb/clients/Clients.fxml", 
                    config.getTitleFor("clients"), 
                    Names.IN_OUT_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        }catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
//            alert.showAlert();
           
            Platform.runLater(() -> {
                AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
                alert.showAlert();
                System.out.println("errorr after");
            });
        }
    }
    
    @FXML
    private void accounts(ActionEvent event) {
        
    }
    
    @FXML
    private void programsInOut(ActionEvent event) {
        try{
            Stage stage = Utils.createStage(
                    Names.IN_OUT_FXML, 
                    config.getTitleFor(Names.IN_OUT_TITLE), 
                    Names.IN_OUT_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        }catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
//            alert.showAlert();
           
            Platform.runLater(() -> {
                AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
                alert.showAlert();
                System.out.println("errorr after");
            });
        }
    }
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = GeneralConfig.getInstance();
    }        
}
