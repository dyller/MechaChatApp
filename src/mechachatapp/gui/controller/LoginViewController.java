/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.gui.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import mechachatapp.gui.commands.ExitCommand;
import mechachatapp.gui.commands.ICommand;
import mechachatapp.gui.model.MechaChatLogModel;

/**
 * FXML Controller class
 *
 * @author mjl
 */
public class LoginViewController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private Label errorLabel;
    
    private MechaChatLogModel model;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MechaChatLogModel();
    }    
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        ICommand exit = new ExitCommand();
        exit.execute();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        //TODO: Do validation
        loadMainView();
    }

    @FXML
    private void handleCreateUser(ActionEvent event) {
        try {
            Stage dialog = new Stage();

            FXMLLoader createLoader = new FXMLLoader(getClass().getResource("/mechachatapp/gui/view/CreateUserView.fxml"));
            Parent createRoot = createLoader.load();
            CreateUserViewController createController = createLoader.getController();
            
            createController.setModel(model);
            
            dialog.setScene(new Scene(createRoot));
            
            dialog.initOwner(stage.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL); 
            dialog.showAndWait();
            
            //if cancelled go back to login view.
            if(createController.isCancelled())
            {
                return;
            }

            // process result of dialog operation. 
            //aka save the user into our user and open main view.
            loadMainView();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void loadMainView()
    {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/mechachatapp/gui/view/MessageLogView.fxml"));
            Parent mainRoot;
            
            mainRoot = mainLoader.load();
            MessageLogViewController controller = mainLoader.getController();
            Scene mainScene = new Scene(mainRoot);
            controller.setModel(model);
            
            stage.setScene(mainScene);
            
        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
