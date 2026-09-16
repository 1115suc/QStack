package Hot100;

public class DemoCode08 {
    public int lengthOfLongestSubstring(String s) {
        int ans = 0;
        char[] charSet = new char[257];

        int flag = 0;
        for (int i = 0; i < s.length(); i++) {
            charSet[s.charAt(i)]++;

            while (charSet[s.charAt(i)] > 1) {
                charSet[s.charAt(flag)]--;
                flag++;
            }

            ans = Math.max(ans, i - flag + 1);
        }

        return ans;
    }

    public static void main(String[] args) {
        String scr = "abcabcbb";

        DemoCode08 demo = new DemoCode08();
        System.out.println(demo.lengthOfLongestSubstring(scr));
    }
}
