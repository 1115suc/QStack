package Hot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoCode06 {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();

        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int left = i + 1;
            int right = nums.length - 1;
            int target = -nums[i];

            while (left < right) {
                if (nums[left] + nums[right] == target) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // 去重部分
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (nums[left] + nums[right] < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return ans;
    }

    public static void main(String[] args) {
//        int[] nums = new int[]{-1,0,1,2,-1,-4};
        int[] nums = new int[]{0, 0, 0, 0, 0, 1, 1, 2};

        DemoCode06 demoCode06 = new DemoCode06();
        List<List<Integer>> lists = demoCode06.threeSum(nums);
        System.out.println(lists);
    }
}
