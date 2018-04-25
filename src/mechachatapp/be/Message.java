/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author pgn
 */
public class Message
{
    
    private final StringProperty text = new SimpleStringProperty();
    private final IntegerProperty id = new SimpleIntegerProperty();
    
    public Message(int id, String text)
    {
        this.id.setValue(id);
        this.text.setValue(text);
    }
    
    public int getId()
    {
        return id.get();
    }
    
    public IntegerProperty idProperty()
    {
        return id;
    }
    
    public String getText()
    {
        return text.get();
    }
    
    public void setText(String value)
    {
        text.set(value);
    }
    
    public StringProperty textProperty()
    {
        return text;
    }
    
    @Override
    public String toString(){
        return getText();
    }
    
}
