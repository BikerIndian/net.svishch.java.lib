package net.svishch.java.lib.xml;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 29.04.2019 
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 * @version 1.0.0
 */
public class XmlMap extends ConcurrentHashMap{
  private ConcurrentHashMap attribute = new ConcurrentHashMap();

    /**
     * получить коллекцию атрибутов
     * @return ConcurrentHashMap
     */
    public ConcurrentHashMap getAttributes() {
        return attribute;
    }

    public void setAttribute(ConcurrentHashMap attribute) {
        this.attribute = attribute;
    }
    
    public String getAttribute(String key) {
        
        if (attribute.containsKey(key)) {
  
      
          return  attribute.get(key).toString();  
        }
      return null;
    }

    public void putAttribute(String nodeName, ConcurrentHashMap attributes) {
        //System.out.println("!!!!!!!!!!!"+nodeName+" -- " + attributes);
        attribute.put(nodeName, attributes);
   
    }

    /**
     * Проверка на ниличие атрибутов
     *
     * @param key Ключ
     * @return boolean
     */

    public boolean isAttribute(String key){     
      return attribute.containsKey(key);   
    }
            
   public void put(String nodeName, Object value, ConcurrentHashMap attributes) {
        super.put(nodeName, value);
       if (null != attributes) {
         attribute.put(nodeName, attributes);  
       }
        
   
    }
   
}
