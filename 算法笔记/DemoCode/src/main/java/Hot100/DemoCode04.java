package Hot100;

import java.util.Arrays;

public class DemoCode04 {
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0 && nums[slow] == 0) {
                nums[slow] = nums[i];
                nums[i] = 0;
                slow++;
            } else if (nums[i] != 0 && nums[slow] != 0) {
                slow++;
            }
        }

        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        // [0,1,0,3,12]
        int[] nums = new int[]{0, 1, 0, 3, 12};

        DemoCode04 demoCode04 = new DemoCode04();
        demoCode04.moveZeroes(nums);
    }
}
