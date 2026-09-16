package Hot100;

import java.util.HashMap;
import java.util.Map;

public class DemoCode01 {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> container = new HashMap<>();
        int length = nums.length;

        for (int i = 0; i < length; i++) {
            if (container.containsKey(target - nums[i])) {
                return new int[]{container.get(target - nums[i]), i};
            }
            container.put(nums[i], i);
        }

        return new int[]{0, 0};
    }

    public static void main(String[] args) {
        DemoCode01 demoCode01 = new DemoCode01();
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        System.out.println(demoCode01.twoSum(nums, target)[0] + " " + demoCode01.twoSum(nums, target)[1]);
    }

}
