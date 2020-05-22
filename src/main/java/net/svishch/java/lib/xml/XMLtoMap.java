package net.svishch.java.lib.xml;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Возвращает коллекцию  из <b>XML</b> файла.
 * <br>Date: 28.09.2018
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 * @version 1.0.0
 *
 */

public class XMLtoMap {

    XmlMap xmlArr = new XmlMap();


    /**
     * Получить коллекцию из XML файла
     *
     * @param document На вход получает объект (Document)
     * @return Возвращает коллекцию (Map)
     * @throws Exception Возвращает ошибку
     */
    public Map get(Document document) throws Exception {

        Node root = document.getDocumentElement();

        XmlMap result = new XmlMap();
        putNodeArr(result,root);

        return result;
    }

    // Получить коллецию из нода
    // Отправляем нод на парсер
    private Map getArr(Node inNode) {
        NodeList books = inNode.getChildNodes();

        for (int i = 0; i < books.getLength(); i++) {
            Node rootNode = books.item(i);

            // Если нода не текст, то это книга - заходим внутрь
            if (rootNode.getNodeType() != Node.TEXT_NODE) {
                nodeControl(rootNode);
            }
        }

        return xmlArr;
    }

    /*   
      Добавление конечного элемента
     */
    private void addStr(Node rootNode) {

        String keyNode = rootNode.getNodeName();
        String strNode = rootNode.getChildNodes().item(0).getTextContent();

        if (xmlArr.containsKey(keyNode)) {

            String tipe = (String) xmlArr.get(keyNode).getClass().getTypeName();
            int sizeList = ((XmlMap) xmlArr.get(keyNode)).size();

            if (tipe.equals("net.svishch.lib.xml.XmlList") || sizeList == 1) {

                XmlList arr = (XmlList) xmlArr.get(keyNode);
                arr.add(strNode,getAttributes(rootNode));
               
                xmlArr.put(keyNode, arr);

            } else {
                System.err.println("Not known type: " + tipe + " key: " + keyNode + " Str:  " + strNode);

            }

        } else {
            // Конечный элемент (Новый)
            xmlArr.put(keyNode, addNewList(strNode,rootNode));
        }

    }

    private List addNewList(String strNode,Node node) {
        XmlList arr = new XmlList();
      
        arr.add(strNode,getAttributes(node));
        //arr.add(strNode);
        return arr;
    }

    private int getSize(Node node) {
        NodeList nodeList = node.getChildNodes();
        return nodeList.getLength();

    }

    /*
    Проверяем коллычество элементов в ноде
     */
    private void nodeControl(Node rootNode) {

        int size = getSize(rootNode);
        // Если 1 то строка
        if (1 == size) {
            addStr(rootNode);

        } else if (1 < size) {
            // Если если много то
            addArr(rootNode);
        } else {
            // Пустое значение
            xmlArr.put(rootNode.getNodeName(), addNewList("",rootNode));

        }
    }

    private void addArr(Node rootNode) {

        String keyNode = rootNode.getNodeName();
        // Если ключь уже есть 
        // это список это или коллекция
        if (xmlArr.containsKey(keyNode)) {

            String tipe = (String) xmlArr.get(keyNode).getClass().getTypeName();

              
            // Если это коллекция
            if (tipe.equals("net.svishch.lib.xml.XmlMap")) {
                // Достаем коллекцию по  ключу keyNode
                XmlMap arrMap = (XmlMap) xmlArr.get(keyNode);
                // Создаем новый список
                XmlList arr = new XmlList();
                // Тогда вставляем существующую коллекцию
                arr.add(arrMap); 
                 // Отправляем нод на парсер и вставляем то что получилось
                arr.add(new XMLtoMap().getArr(rootNode));
                xmlArr.put(keyNode, arr);

            // Если же это список
            } else if (tipe.equals("net.svishch.lib.xml.XmlList")) {
                // Достаем список по  ключу keyNode
                XmlList arr = (XmlList) xmlArr.get(keyNode);
                // Отправляем нод на парсер и вставляем то что получилось
                arr.add(new XMLtoMap().getArr(rootNode));
                xmlArr.put(keyNode, arr);

            } else {
                System.err.println("Not known type: " + tipe + " key: " + keyNode);
            }

        // Если такого ключа нет     
        } else {
            // Отправляем нод на парсер
            Map result = new XMLtoMap().getArr(rootNode);
            xmlArr.put(keyNode, result);
        }
    }

    
     // добавление атрибутов в XmlMap
    private void addpAttribute(Node node, XmlMap result) {


    }

        private void add(Node node,Object obj, XmlList result) {

        if (0 < node.getAttributes().getLength()) {

            ConcurrentHashMap attrMap = getAttributes(node);
            
            if (null != attrMap) {
                result.add(obj, attrMap);
            }

        }

    }
    
    
    // получение атрибутов из нода
    private ConcurrentHashMap getAttributes(Node node) {

        ConcurrentHashMap attrMap = new ConcurrentHashMap();

        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attribute = (Attr) attrs.item(i);
            attrMap.put(attribute.getName(), attribute.getValue());
            //System.out.println(i+" " + attribute.getName() + " = " + attribute.getValue());
        }
        return attrMap;

    }

    private void putNodeArr(XmlMap result, Node node ) {
        
        result.put(node.getNodeName(), getArr(node));
          
        if (0 < node.getAttributes().getLength()) {

            ConcurrentHashMap attrMap = getAttributes(node);
            
            if (null != attrMap && null != node) {
                result.putAttribute(node.getNodeName(), attrMap);
            }

        }   
        
        
        
    }

}
