/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.bll;

import java.util.ArrayList;
import java.util.List;
import mechachatapp.be.Message;
import mechachatapp.dal.IMechaChatDataFacade;
import mechachatapp.dal.XMLMechaChatDataFacade;

/**
 *
 * @author mjl
 */
public class MechaChatLogicFacade implements IMechaChatLogicFacade 
{
    //Singleton
    private static MechaChatLogicFacade instance = null;

    public static MechaChatLogicFacade getInstance() 
    {
        if(instance == null)
        {
            //Only generate an instance once!
            instance = new MechaChatLogicFacade();
        }
        
        return instance;
    }
    
    IMechaChatDataFacade dal;
    
    private MechaChatLogicFacade()
    {
        //Private constructor, that no one outside can use!
        
        dal = new XMLMechaChatDataFacade();
        
//        dal = new IMechaChatDataFacade()
//        {
//            @Override
//            public Message logMessage(String msg)
//            {
//                return new Message(0, msg);
//            }
//            
//            @Override
//            public boolean removeMessage(Message msg)
//            {
//                //Dummy function, doesn't do anything
//                return true;
//            }
//
//            @Override
//            public List<Message> getMessages()
//            {
//                List<Message> list = new ArrayList<>();
//                list.add(new Message(0, "This is my Message Log."));
//                list.add(new Message(1, "There are many others like it, but this one is mine."));
//                list.add(new Message(2, "My Message Log is my best friend."));
//                list.add(new Message(3, "It is my life."));
//                list.add(new Message(4, "I must master it as I must master my life."));
//                list.add(new Message(5, "Without me, my Message Log is useless."));
//                list.add(new Message(6, "Without my Message Log, I am useless."));
//
//                return list;
//            }   
//        };
    }
    

    @Override
    public Message logMessage(String msg) {
        return dal.logMessage(msg);
    }

    @Override
    public List<Message> getMessages() {
        return dal.getMessages();
    }

    @Override
    public boolean removeMessage(Message msg) {
        return dal.removeMessage(msg);
    }
    
}