package Hot100;

import java.util.Arrays;

public class DemoCode16 {
    public int[] productExceptSelf(int[] nums) {
        int[] ans = new int[nums.length];

        int[] leftMulti = new int[nums.length];
        int[] rightMulti = new int[nums.length];

        leftMulti[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            leftMulti[i] = leftMulti[i - 1] * nums[i - 1];
        }

        rightMulti[nums.length - 1] = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            rightMulti[i] = rightMulti[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < nums.length; i++) {
            ans[i] = leftMulti[i] * rightMulti[i];
        }

        return ans;
    }

    public static void main(String[] args) {
        // 1,2,3,4
        int[] nums = {1,2,3,4};
        DemoCode16 demo = new DemoCode16();
        System.out.println(Arrays.toString(demo.productExceptSelf(nums)));
    }
}
