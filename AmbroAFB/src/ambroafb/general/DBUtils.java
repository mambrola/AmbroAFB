/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.licenses.License;
import authclient.AuthServerException;
import authclient.db.DBClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class DBUtils {
    
    /**
     * The static function gets a ArrayList of specified class elements from DB.
     * @param <T>
     * @param listElementClass The class of elements which must be in list.
     * @param dbTableOrViewName The table or view name where entries are in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return 
     */
    public static <T> ArrayList<T> getObjectsListFromDB(Class<?> listElementClass, String dbTableOrViewName, JSONObject params){
        try {
            System.out.println(dbTableOrViewName + " params For DB: " + params);
            
            String data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params).toString();
            
            System.out.println(dbTableOrViewName + " data from DB: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(ArrayList.class, listElementClass));
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    
    /**
     * The static function gets one element from DB.
     * @param <T>
     * @param targetClass The element class
     * @param dbTableOrViewName The table or view name in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return 
     */
    public static <T> T getObjectFromDB(Class<?> targetClass, String dbTableOrViewName, JSONObject params){
        try {
            JSONArray selectResultAsArray = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
            JSONObject jsonResult = selectResultAsArray.optJSONObject(0);
            
            System.out.println("one " + targetClass + " data: " + jsonResult);
            
            return Utils.getClassFromJSON(targetClass, jsonResult);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * The static function saves one element to DB and gets appropriate entry from DB.
     * Note: If this element is new for DB, then it has not DB id. 
     * So after this function the element will has every old value and a DB id too.
     * @param <T>
     * @param source The element which must save to DB.
     * @param dbTableName The table name in DB.
     * @return 
     */
    public static <T> T saveObjectToDBSimple(Object source, String dbTableName){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(source);
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newSourceFromDB = dbClient.callProcedureAndGetAsJson("general_insert_update_simple", dbTableName, dbClient.getLang(), targetJson).getJSONObject(0);
            
            System.out.println("save " + source.getClass() + " data: " + newSourceFromDB.toString());
            
            return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * The static function saves element into DB.
     * Note: If this element is new for DB, then it has not DB id. 
     * So after this function the element will has every old value and a DB id too.
     * @param <T>
     * @param source The element which must save to DB.
     * @param dbTableNameSingularForm The DB table name in singular form (ex: client, product ...)
     * @return 
     */
    public static <T> T saveObjectToDB(Object source, String dbTableNameSingularForm){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(source);
            
            System.out.println("data for simple table to server: " + targetJson);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newSourceFromDB = dbClient.insertUpdate(dbTableNameSingularForm, targetJson);
            
            System.out.println("data for simple table from server: " + newSourceFromDB);
            
            return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
        } 
        catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAlert();
        }
        return null;
    }

    public static boolean deleteObjectFromDB(String deleteProcName, JSONObject params) {
        boolean result = false;
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            dbClient.callProcedure(deleteProcName, params);
            result = true;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static boolean deleteObjectFromDB(String deleteProcName, int id){
        boolean result = false;
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            dbClient.callProcedure(deleteProcName, id);
            result = true;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
