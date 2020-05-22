package net.svishch.java.lib.xlm;

import java.util.List;
import java.util.Map;

/**
 * Date: 31.01.2019
 *
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 */
public class XMLarray {

    /**
     * Возвращает [Map] из [XML]
     * @param fileName Путь к файлу
     * @return Возвращает [Map]
     * @throws Exception Возвращает ошибку
     */
    public Map get(String fileName) throws Exception {

        Map result = conversionMap(new XMLarrayLoad().get(fileName));

        return result;
        
    }

    private Map conversionMap(Map arr) {

        arr.forEach((key, value) -> {

             arr.put(key,toChoose(value));
        });

        return arr;
    }

    private Object conversionList(List value) {

        if (isManyArrayList((List) value)) {

            for (int i = 0; i < value.size(); i++) {
                value.set(i, toChoose(value.get(i)));
            }
          return value;

        } else {  
            return toChoose(value.get(0));

        }
   

    }
    
    private Object toChoose(Object obj) {
        if (isArrayList(obj)) {
            obj = conversionList((List) obj);
        } else if (isHashMap(obj)) {
            obj = conversionMap((Map) obj);
        } else { 

            obj = (String) obj;
        }
        return obj;
    }

    private boolean isManyArrayList(List arrList) {
        if (1 < arrList.size()) {
            return true;
        }
        return false;
    }

    private boolean isArrayList(Object obj) {
        return obj.getClass().getTypeName().equals("java.util.ArrayList");
    }

    private boolean isHashMap(Object obj) {
        return obj.getClass().getTypeName().equals("java.util.HashMap");
    }

   
}
