/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mechachatapp.dal.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mjl
 */
@XmlRootElement
public class XMLDatabase {
    public List<XMLMessage> messages = new ArrayList<>();
}
