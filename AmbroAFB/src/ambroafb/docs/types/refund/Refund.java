/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.refund;

import ambroafb.docs.types.DocDialogAbstraction;
import ambroafb.docs.types.SceneWithVBoxRoot;
import javafx.scene.Node;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Refund extends SceneWithVBoxRoot implements DocDialogAbstraction {

    public Refund() {
        super("/ambroafb/docs/types/refund/Refund.fxml");
    }

    @Override
    public Node getSceneNode() {
        return this;
    }

    @Override
    public JSONObject getResult() {
        return null;
    }

    @Override
    public void cancel() {
        System.out.println("Refund cancel method");
    }
    
    
}
