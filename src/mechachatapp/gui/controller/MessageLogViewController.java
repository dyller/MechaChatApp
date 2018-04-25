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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import mechachatapp.be.Message;
import mechachatapp.gui.commands.ExitCommand;
import mechachatapp.gui.commands.ICommand;
import mechachatapp.gui.commands.IUndoableCommand;
import mechachatapp.gui.commands.SendTextCommand;
import mechachatapp.gui.model.MechaChatLogModel;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        undoKeyComp = new KeyCodeCombination(KeyCode.Z, KeyCombination.META_DOWN);
        redoKeyComp = new KeyCodeCombination(KeyCode.Z, KeyCombination.META_DOWN, KeyCombination.SHIFT_DOWN);
        
        model = new MechaChatLogModel();
        editingLastMessage = false;
        lstMessages.setItems(model.getMessages());
        
        
       root.addEventHandler(KeyEvent.KEY_PRESSED, e -> handleShortcutKeys(e));
       
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
}