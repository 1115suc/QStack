package Hot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoCode02 {
    public List<List<String>> groupAnagrams(String[] str) {

        Map<String, List<String>> map = new HashMap<>();

        for (String s : str) {
            if (map.containsKey(StringToArray(s))) {
                map.get(StringToArray(s)).add(s);
            }else{
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(StringToArray(s), list);
            }
        }

        return new ArrayList<>(map.values());
    }

    public static String StringToArray(String str) {
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        return String.valueOf(charArray);
    }

    public static void main(String[] args) {
        /*
        * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
        * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
        * */
        String[] str = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};

    }
}
