package core.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import java.io.*;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class JsonHandler {

    public static JsonNode readJsonFromFile(String fileName){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = null;
        try{

            // 1. JSON file to Java object
            json = objectMapper.readValue(new File(fileName), JsonNode.class);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return json;
    }
//
//    public static JSONObject getDataFile(String fileName){
//        JSONObject testObject = null;
//
//        try{
//            //FileReader reader = new FileReader(filePath + fileName);
//            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));;
//            JSONParser jsonParser = new JSONParser();
//            testObject = (JSONObject) jsonParser.parse(buff);
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return testObject;
//    }

    public static JsonNode getJsonNode(JsonNode parent, String objName) {
        JsonNode jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName);
        }
        return jObj;
    }

    public static String getTextValue(JsonNode parent, String objName) {
        String jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : parent.get(objName).asText();
        }
        return jObj;
    }

    public static Boolean getBooleanValue(JsonNode parent, String objName) {
        Boolean jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : parent.get(objName).asBoolean();
        }
        return jObj;
    }

    public static Double getDoubleValue(JsonNode parent, String objName) {
        Double jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : parent.get(objName).asDouble();
        }
        return jObj;
    }

    public static Integer getIntValue(JsonNode parent, String objName) {
        Integer jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : parent.get(objName).asInt();
        }
        return jObj;
    }

    public static Long getLongVal(JsonNode parent, String objName) {
        Long jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : parent.get(objName).asLong();
        }
        return jObj;
    }

    public static ArrayNode getArrayNode(JsonNode parent, String objName) {
        ArrayNode jObj = null;
        if(parent!=null) {
            jObj = parent.get(objName) == null? null : (ArrayNode)parent.get(objName);
        }
        return jObj;
    }

    public static LinkedHashMap<String, Object> convertJsonToLinkedHashMap(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, Object> result = mapper.convertValue(jsonNode, new TypeReference<LinkedHashMap<String, Object>>(){});
        return result;
    }

    public static JsonNode replaceValueByMapData(JsonNode jsonSource, String beginPattern, String endPattern,LinkedHashMap<String, Object> row){
        if(jsonSource.isObject()){
            Iterator<String> fieldNames = jsonSource.fieldNames();

            while(fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = jsonSource.get(fieldName);
                if (fieldValue.isValueNode()) {
                    updateValue(jsonSource, fieldName, StringHandler.replaceValueByMapData(fieldName, beginPattern, endPattern, row));
                }
                else {
                    replaceValueByMapData(fieldValue, beginPattern, endPattern, row);
                }
            }
        } else if(jsonSource.isArray()){
            ArrayNode arrayNode = (ArrayNode) jsonSource;
            for(int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                replaceValueByMapData(arrayElement, beginPattern, endPattern, row);
            }
        }
        return jsonSource;
    }

    public static JsonNode updateValue(JsonNode parent, String fieldName, Object value){
        if(value instanceof String){
            return ((ObjectNode)parent).put(fieldName, (String)value);
        }
        if(value instanceof Double){
            return ((ObjectNode)parent).put(fieldName, (Double)value);
        }
        if(value instanceof Float){
            return ((ObjectNode)parent).put(fieldName, (Float)value);
        }
        if(value instanceof Boolean){
            return ((ObjectNode)parent).put(fieldName, (Boolean)value);
        }
        if(value instanceof BigDecimal){
            return ((ObjectNode)parent).put(fieldName, (BigDecimal)value);
        }
        if(value instanceof Long){
            return ((ObjectNode)parent).put(fieldName, (Long)value);
        }
        if(value instanceof Integer){
            return ((ObjectNode)parent).put(fieldName, (Integer)value);
        }
        if(value instanceof Short){
            return ((ObjectNode)parent).put(fieldName, (Short)value);
        }
        return ((ObjectNode)parent).put(fieldName, (String)value);
    }
//
//    public static JsonNode traverse(JsonNode root){
//
//        if(root.isObject()){
//            Iterator<String> fieldNames = root.fieldNames();
//
//            while(fieldNames.hasNext()) {
//                String fieldName = fieldNames.next();
//                JsonNode fieldValue = root.get(fieldName);
//                traverse(fieldValue);
//            }
//        } else if(root.isArray()){
//            ArrayNode arrayNode = (ArrayNode) root;
//            for(int i = 0; i < arrayNode.size(); i++) {
//                JsonNode arrayElement = arrayNode.get(i);
//                traverse(arrayElement);
//            }
//        } else {
//
//
//        }
//    }

//    public static JSONObject GetJSONObject(JSONObject parent, String objName) {
//        JSONObject jObj = null;
//        if(parent!=null) {
//            Object obj = parent.get(objName);
//            jObj = (obj == null) ? null : (JSONObject) obj;
//        }
//        return jObj;
//    }
//
//    public static Object GetValueJSONObject(JSONObject parent, String objName) {
//        Object value = null;
//        if(parent!=null) {
//            Object obj = parent.get(objName);
//            value = (obj == null) ? null : obj;
//        }
//        return value;
//    }

//    public static JSONObject replaceValueByMapData(JSONObject source, String beginPattern, String endPattern,LinkedHashMap<String, Object> row)
//    {
//        JSONObject result = new JSONObject(source);
//        for (Object key : source.keySet()) {
//            if (source.get(key) instanceof String) {
//                result.put(key, StringHandler.replaceValueByMapData((String) source.get(key), beginPattern, endPattern, row));
//            }
//            if(source.get(key) instanceof JSONObject)
//            {
//                result.put(key,replaceValueByMapData((JSONObject)source.get(key), beginPattern, endPattern, row));
//            }
//        }
//        return result;
//    }
}
