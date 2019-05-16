package core.utilities;

import java.util.ArrayList;
import java.util.Stack;

public class AlgorithmHandler {

    public static ArrayList<int[]> CombinationsNonrepetition(int k, int n)
    {
        ArrayList<int[]> resultArray = new ArrayList<>();
        int[] result = new int[k];
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(1);

        while (stack.size() > 0)
        {
            int index = stack.size() - 1;
            int value = stack.pop();

            while (value <= n)
            {
                result[index++] = value++;
                stack.push(value);
                if (index == k)
                {
                    resultArray.add(result.clone());
                    break;
                }
            }
        }
        return resultArray;
    }

    public static ArrayList<int[]> SumOfCombinationsNonrepetition(int n){
        ArrayList<int[]> arrIndex = new ArrayList<>();
        for(int k=1; k<= n; k++) {
            ArrayList<int[]> indexCombinationArray = CombinationsNonrepetition(k, n);
            arrIndex.addAll(indexCombinationArray);
        }
        return arrIndex;
    }
}
