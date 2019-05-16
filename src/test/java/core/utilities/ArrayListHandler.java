package core.utilities;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListHandler {
    public static void RemoveAt(ArrayList<?> arr, int start, int end){
        for(int i=start ; i <= end; i++)
        {
            arr.remove(i);
        }
    }


    public static boolean containsAny(String str, String[] items){
        return Arrays.stream(items).parallel().anyMatch(str::contains);
    }

}
