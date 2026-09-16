package Hot100;

import java.util.HashSet;
import java.util.Set;

public class DemoCode03 {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }

        int ans = 0;
        for (Integer x : set) {
            if (!set.contains(x - 1)) {
                int temp = x + 1;
                while(set.contains(temp)) temp++;
                ans = Math.max(ans, temp - x);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {0,0,1,2,3,4,5,9,1,5,4,8,70};
        DemoCode03 demo = new DemoCode03();
        System.out.println(demo.longestConsecutive(nums));
    }
}
