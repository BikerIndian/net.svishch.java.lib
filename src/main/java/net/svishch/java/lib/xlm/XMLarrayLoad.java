package net.svishch.java.lib.xlm;

/**
 * Date: 28.09.2018
 *
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLarrayLoad {

    Map xmlArr = new HashMap();

    /**
     * Получить коллекцию [Map] из [XML] файла
     *
     * @param fileName Путь к файлу
     * @return Возвращает [Map]
     * @throws Exception Возвращает ошибку
     */
    public Map get(String fileName) throws Exception {

        // Создается построитель документа
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse(fileName);

        Node root = document.getDocumentElement();

        Map result = new HashMap();

        getAttributes(root);

        result.put(root.getNodeName(), getArr(root));

        //getArrNew(root);
        //result = new XLMarrayConversion().get(result);
        return result;
    }

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
            int sizeList = ((List) xmlArr.get(keyNode)).size();

            if (tipe.equals("java.util.ArrayList") || sizeList == 1) {

                List arr = (List) xmlArr.get(keyNode);
                arr.add(strNode);
                xmlArr.put(keyNode, arr);

            } else {
                System.err.println("Не известный тип: " + tipe + " key: " + keyNode + " Str:  " + strNode);

            }

        } else {
            // Конечный элемент (Новый)
            xmlArr.put(keyNode, addNewList(strNode));
        }

    }

    private List addNewList(String strNode) {
        List arr = new ArrayList();
        arr.add(strNode);
        return arr;
    }

    private void addStr1(Node rootNode) {

        String keyNode = rootNode.getNodeName();
        String strNode = rootNode.getChildNodes().item(0).getTextContent();
        if (xmlArr.containsKey(keyNode)) {
            String tipe = (String) xmlArr.get(keyNode).getClass().getTypeName();

            // Это строка?
            if (tipe.equals("java.lang.String")) {
                // Тогда вставляем коллекцию
                String str = (String) xmlArr.get(keyNode);
                List arr = new ArrayList();
                arr.add(str);
                arr.add(strNode);

                xmlArr.put(keyNode, arr);

            } else {
                // Это список?
                if (tipe.equals("java.util.ArrayList")) {

                    List arr = (List) xmlArr.get(keyNode);
                    arr.add(strNode);
                    xmlArr.put(keyNode, arr);

                } else {
                    System.err.println("Не известный тип: " + tipe + " key: " + keyNode + " Str:  " + strNode);
                }

            }

        } else {
            // Конечный элемент
            xmlArr.put(keyNode, strNode);
        }

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
        if (1 == size) {
            addStr(rootNode);

        } else if (1 < size) {
            // Если если много то
            addArr(rootNode);
        } else {
            // Пустое значение
            xmlArr.put(rootNode.getNodeName(), addNewList(""));
        }
    }

    private void addArr(Node rootNode) {

        String keyNode = rootNode.getNodeName();
        // Если ключь уже есть то...
        if (xmlArr.containsKey(keyNode)) {

            String tipe = (String) xmlArr.get(keyNode).getClass().getTypeName();

            // Если это Map
            if (tipe.equals("java.util.HashMap")) {

                // Тогда вставляем коллекцию
                HashMap arrMap = (HashMap) xmlArr.get(keyNode);
                List arr = new ArrayList();
                arr.add(arrMap);
                arr.add(new XMLarrayLoad().getArr(rootNode));
                xmlArr.put(keyNode, arr);

            } else if (tipe.equals("java.util.ArrayList")) {

                List arr = (List) xmlArr.get(keyNode);
                arr.add(new XMLarrayLoad().getArr(rootNode));
                xmlArr.put(keyNode, arr);

            } else {
                System.err.println("Не известный тип: " + tipe + " key: " + keyNode);
            }

        } else {
            Map result = new XMLarrayLoad().getArr(rootNode);
            xmlArr.put(keyNode, result);
        }
    }

    private void getAttributes(Node node) {

        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attribute = (Attr) attrs.item(i);
            System.out.println(" " + attribute.getName() + " = " + attribute.getValue());
        }

    }

  
}
