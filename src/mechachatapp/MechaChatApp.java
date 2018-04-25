/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mechachatapp.gui.controller.MessageLogViewController;

/**
 *
 * @author pgn
 */
public class MechaChatApp extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mechachatapp/gui/view/MessageLogView.fxml"));
        Parent root = loader.load();
        MessageLogViewController controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(e -> Platform.exit());
        
        

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}


/*
scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
 public void handle(final KeyEvent keyEvent) {
   if (keyEvent.getCode() == KeyCode.F5) {
    System.out.println("F5 pressed");
    //Stop letting it do anything else
    keyEvent.consume();
   }
 }
});
final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.R,
                                    KeyCombination.CONTROL_DOWN);
scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
                @Override
                public void handle(KeyEvent event) {
                    if (keyComb1.match(event)) {
                        System.out.println("Ctrl+R pressed");
                    }
                }
            });
*/