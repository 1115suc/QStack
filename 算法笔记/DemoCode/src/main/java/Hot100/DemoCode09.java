package Hot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoCode09 {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();

        int[] temp = new int[26];
        for (char c : p.toCharArray()) {
            temp[c - 'a']++;
        }

        int left = 0;
        int[] compare = new int[26];
        for (int right = 0; right < s.length(); right++) {
            compare[s.charAt(right) - 'a']++;
            if (right - left + 1 != p.length()) {
                continue;
            } else{
                int compare1 = Arrays.compare(temp, compare);
                if (compare1 == 0) {
                    ans.add(left);
                }
                compare[s.charAt(left) - 'a']--;
                left++;
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        // "cbaebabacd"
        String s = "cbaebabacd";
        String p = "abc";
        DemoCode09 demoCode09 = new DemoCode09();
        List<Integer> ans = demoCode09.findAnagrams(s, p);
        System.out.println(ans);
    }
}
