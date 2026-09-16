package Hot100;

import java.util.HashMap;
import java.util.Map;

public class DemoCode12 {
    public String minWindow(String s, String t) {
        // need: t 中每个字符需要的数量
        // window: 当前窗口中每个字符的数量
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();

        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0; // 窗口中已满足条件的字符种数

        // 记录最小覆盖子串的起始索引和长度
        int start = 0, minLen = Integer.MAX_VALUE;

        while (right < s.length()) {
            // 1. 右移 right，扩大窗口
            char c = s.charAt(right);
            right++;

            // 2. 更新窗口数据
            if (need.containsKey(c)) {
                window.put(c, window.getOrDefault(c, 0) + 1);
                // 数量恰好满足时，valid +1
                if (window.get(c).equals(need.get(c))) {
                    valid++;
                }
            }

            // 3. 当所有字符都满足时，收缩左边界
            while (valid == need.size()) {
                // 更新最小覆盖子串
                if (right - left < minLen) {
                    start = left;
                    minLen = right - left;
                }

                // 左移 left，缩小窗口
                char d = s.charAt(left);
                left++;

                if (need.containsKey(d)) {
                    // 数量从满足变为不满足时，valid -1
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        // s = "ADOBECODEBANC", t = "ABC"
        String s = "ADOBECODEBANC";
        String t = "ABC";

        DemoCode12 demoCode12 = new DemoCode12();
        String s1 = demoCode12.minWindow(s, t);
        System.out.println(s1);
    }
}
