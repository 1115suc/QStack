package Hot100;

import java.util.Arrays;

public class DemoCode15 {
    public void rotate(int[] nums, int k) {
        k %= nums.length;

        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;

            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        // 1,2,3,4,5,6,7
        int[] nums = new int[]{1, 2, 3, 4, 5, 6, 7};
        DemoCode15 demoCode15 = new DemoCode15();
        demoCode15.rotate(nums, 3);
        System.out.println(Arrays.toString(nums));
    }
}
