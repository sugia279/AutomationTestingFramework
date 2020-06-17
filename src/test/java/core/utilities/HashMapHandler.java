package core.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;

public class HashMapHandler {
    public static LinkedHashMap<Integer, Object> CreateHashMapValueWithKeyIndex(Object[] obj, int startIndex)
    {
        LinkedHashMap<Integer, Object> indexHashMap = new LinkedHashMap<Integer, Object>();
        for(int i=startIndex; i <= obj.length; i++)
        {
            indexHashMap.put(i, obj[i-startIndex]);
        }
        return indexHashMap;
    }

    public static LinkedHashMap<String, Object> ConvertJsonObjectToHashMap(JSONObject jsonObj) {
        LinkedHashMap<String, Object> mapParams = new LinkedHashMap<String, Object>();
        if(jsonObj !=null) {
            for (Object keyParam : jsonObj.keySet()) {
                mapParams.put((String) keyParam, jsonObj.get(keyParam));
            }
        }
        return mapParams;
    }

    public static LinkedHashMap<String, Object> replaceValueByMapData(LinkedHashMap<String, Object> source, String beginPattern, String endPattern,LinkedHashMap<String, Object> map)
    {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>(source);
        for(String key: source.keySet())
        {
            if(source.get(key) instanceof String)
            {
                result.put(key, StringHandler.replaceValueByMapData((String) source.get(key), beginPattern, endPattern, map));
            }
            if(source.get(key) instanceof LinkedHashMap)
            {
                result.put(key, replaceValueByMapData((LinkedHashMap<String, Object>)source.get(key), beginPattern, endPattern,map));
            }
//            if(source.get(key) instanceof JsonNode)
//            {
//                result.put(key, JsonHandler.replaceValueByMapData((JsonNode)source.get(key), beginPattern, endPattern,map));
//            }
        }
        return result;
    }
}
