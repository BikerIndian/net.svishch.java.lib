package net.svishch.java.lib.xml;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Date: 29.04.2019 
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 * @version 1.0.0
 */

public class XmlList extends CopyOnWriteArrayList{
    private ConcurrentHashMap attribute = new ConcurrentHashMap();
    private CopyOnWriteArrayList attributeLis = new CopyOnWriteArrayList();
    
    public ConcurrentHashMap getAttribute() {
        return attribute;
    }

    public void setAttribute(ConcurrentHashMap attribute) {
        this.attribute = attribute;
    }



    public boolean add(Object node,ConcurrentHashMap attributes) {


         // System.out.println("!!!!!!!!!!!"+node.toString()+" -- " + attributes); 
         // System.out.println("attributes"+ attributes);
        attributeLis.add(attribute);
       
       return super.add(node);
       
        
    }

    

    public Object getAttribute(int index) {   
        if (attributeLis.size()>index) { 
            return attributeLis.get(index); 
        } else {
            return null; 
        }
            
    }
    
  
    
    @Override
    public boolean remove(Object index) {
        attributeLis.remove(index);
        return super.remove(index);
    }
    
}
