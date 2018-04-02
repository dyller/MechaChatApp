/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import mechachatapp.be.Message;
import mechachatapp.be.User;
import mechachatapp.gui.commands.ExitCommand;
import mechachatapp.gui.commands.ICommand;
import mechachatapp.gui.commands.IUndoableCommand;
import mechachatapp.gui.commands.ResetMessageLogCommand;
import mechachatapp.gui.commands.SendTextCommand;
import mechachatapp.gui.model.MechaChatLogModel;
import mechachatapp.util.OSUtil;

/**
 *
 * @author pgn & mjl
 */
public class MessageLogViewController implements Initializable
{
    
    @FXML
    private ListView<Message> lstMessages;
    @FXML
    private TextField txtMessage;
    
    private MechaChatLogModel model;
    private boolean editingLastMessage;
    
    private Stack<IUndoableCommand> history = new Stack<>();
    private Stack<IUndoableCommand> redoHistory = new Stack<>();
    
    @FXML
    private BorderPane root;
    
    private KeyCodeCombination undoKeyComp;
    private KeyCodeCombination redoKeyComp;
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem redoButton;
    @FXML
    private MenuItem exitButton;
    @FXML
    private ToggleGroup LogType;
    @FXML
    private Label usernameLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Modifier cmdOrCtrl = OSUtil.isWindows() ? KeyCombination.CONTROL_DOWN : KeyCombination.META_DOWN;
        undoKeyComp = new KeyCodeCombination(KeyCode.Z, cmdOrCtrl);
        redoKeyComp = new KeyCodeCombination(KeyCode.Z, cmdOrCtrl, KeyCombination.SHIFT_DOWN);
        
        KeyCodeCombination osxExitKeyComp = new KeyCodeCombination(KeyCode.Q, cmdOrCtrl);
        KeyCodeCombination windowsExitKeyComp = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        
        undoButton.acceleratorProperty().setValue(undoKeyComp);
        redoButton.acceleratorProperty().setValue(redoKeyComp);
        
        exitButton.acceleratorProperty().setValue(OSUtil.isWindows() ? windowsExitKeyComp : osxExitKeyComp);
        
        editingLastMessage = false;
        
        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> handleShortcutKeys(e));
        
        //Default model initializer
        setModel(new MechaChatLogModel());
    }
    
    public void setCurrentUser(User user)
    {
        model.setCurrentUser(user);
    }
    
    public User getCurrentUser()
    {
        return model.getCurrentUser();
    }
    
    public void setModel(MechaChatLogModel model)
    {
        this.model = model;
        
        lstMessages.setItems(this.model.getMessages());
        usernameLabel.setText(getCurrentUser().getUsername());
    }

    @FXML
    private void handleSendMessage(ActionEvent event)
    {
        if(editingLastMessage)
        {
            //Undo the last message
            if(!history.empty())
            {
                IUndoableCommand cmd = history.pop();
                cmd.undo();
            }
            else
            {
                //TODO: handle editing an old message
            }
            
            editingLastMessage = false;
            //Then create a new message as usual.
            
            //TODO: handle undo editing, redo editing, etc.
        }
        
        //We do not allow empty text, this also means if you use up arrow 
        //and then delete all the text, you've removed the text entirely
        if(txtMessage.getText().isEmpty()) return;
        
        //Get text from textfield
        String txt = txtMessage.getText();
        
        //Create the send text command
        IUndoableCommand cmd = new SendTextCommand(model, txt);
        
        //Execute the new send message command.
        cmd.execute();
        
        //Add to undo history
        history.push(cmd);
        
        //Clear text field, so the user doesn't have to clear it before typing the next message
        txtMessage.clear();
        
        //CLEAR REDO HISTORY?!?
        //If we clear here, any new messages will make it impossible to redo.
        //If we don't, the redo history could potentially be used for storing messages for later redo.
        redoHistory.clear();
    }

    @FXML
    private void handleUndo(ActionEvent event) {
        //If there are any items in the undo history, undo the latest item
        if(!history.empty())
        {
            IUndoableCommand cmd = history.pop();
            cmd.undo();
            redoHistory.push(cmd);
        }
    }

    @FXML
    private void handleRedo(ActionEvent event)
    {
        //If there are any items in the redo history, redo the latest item
        if(!redoHistory.empty())
        {
            IUndoableCommand cmd = redoHistory.pop();
            cmd.redo();
            history.push(cmd);
        }
    }

    @FXML
    private void handleExit(ActionEvent event) 
    {
        //This could also take a reference to the model,
        //allowing it to perform some cleanup before closing
        ICommand cmd = new ExitCommand();
        cmd.execute();
    }

    private void handleShortcutKeys(KeyEvent event)
    {
       if(undoKeyComp.match(event))
       {
           handleUndo(null);
           event.consume();
       }
       else if(redoKeyComp.match(event))
       {
           handleRedo(null);
           event.consume();
       }
    }
    
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        
        //TODO: handle undo / redo key presses
        
        //Allow user to edit last message using the Up key
        if(event.getCode() == KeyCode.UP)
        {
            if(!model.getMessages().isEmpty())
            {
                editingLastMessage = true;
                Message last = model.getMessages().get(model.getMessages().size()-1);
                txtMessage.setText(last.getText());
            }
        }
        else if(event.getCode() == KeyCode.ESCAPE)
        {
            //Break editing
            if(editingLastMessage)
            {
                editingLastMessage = true;
                txtMessage.clear();
            }
        }
    }

    @FXML
    private void handleReset(ActionEvent event) 
    {
        //Create the send text command
        IUndoableCommand cmd = new ResetMessageLogCommand(model);
        
        //Execute the new send message command.
        cmd.execute();
        
        //Add to undo history
        history.push(cmd);
        
        //Clear text field, so the user doesn't have to clear it before typing the next message
        txtMessage.clear();
        
        //CLEAR REDO HISTORY?!?
        //If we clear here, any new messages will make it impossible to redo.
        //If we don't, the redo history could potentially be used for storing messages for later redo.
        redoHistory.clear();
    }

    @FXML
    private void handleMultiLog(ActionEvent event) {
        model.gotoAllMessages();
    }

    @FXML
    private void handleSoloLog(ActionEvent event) {
        model.gotoSoloMessages();
    }

    @FXML
    private void handleSignOut(ActionEvent event) 
    {
        
    }
}