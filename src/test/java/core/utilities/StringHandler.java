package core.utilities;

import java.util.Base64;
import java.util.LinkedHashMap;

public class StringHandler {

    public static String getSubStringBetween(String str, String begin, String end){
        int beginIndex = str.indexOf(begin) + begin.length();
        int endIndex = str.substring(beginIndex, str.length()).indexOf(end);
        return str.substring(beginIndex,beginIndex + endIndex);
    }

    public static Object replaceValueByMapData(String source, String beginPattern, String endPattern, LinkedHashMap<String, Object> row)
    {
        if(source !=null) {
            String[] sections = source.split(beginPattern);
            Object resultValue = sections[0];
            String key = "";
            if (sections.length == 2 &&  source.indexOf(beginPattern) == 0 && sections[1].indexOf(endPattern) == sections[1].length()-1) {
                key = sections[1].substring(0, sections[1].indexOf(endPattern));
                if (row.containsKey(key)) {
                    resultValue = row.get(key);
                }
                else{
                    resultValue = source;
                }
            }
            else {
                int endIndex = -1;
                for (int i = 1; i < sections.length; i++) {
                    endIndex = sections[i].indexOf(endPattern);
                    if(endIndex > -1) {
                        key = sections[i].substring(0, endIndex);
                    }
                    resultValue = ((String) resultValue + beginPattern + sections[i]);
                    if (row.containsKey(key)) {
                        resultValue = ((String) resultValue).replace(beginPattern + key + endPattern, String.valueOf(row.get(key)));
                    }
                }
            }
            return resultValue;
        }
        return null;
    }

    public static String encodeToBase64String(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String stringMultiply(String s, int n){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++){
            sb.append(s);
        }
        return sb.toString();
    }
}
