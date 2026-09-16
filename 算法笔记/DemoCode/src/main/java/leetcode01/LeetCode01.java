package leetcode01;

import java.util.HashMap;
import java.util.Map;

public class LeetCode01 {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i = 0; i < nums.length; i++) {
            if(map.containsKey(target - nums[i])) return new int[] {map.get(target - nums[i]), i};
            map.put(nums[i], i);
        }
        return new int[] {-1, -1};
    }

    public static void main(String[] args) {
        LeetCode01 leetCode01 = new LeetCode01();
        int[] nums = new int[]{2,7,11,15};
        int target = 9;
        int[] result = leetCode01.twoSum(nums, target);
        System.out.println(result[0] + " " + result[1]);
    }
}
