package Hot100;

public class DemoCode17 {
    public int firstMissingPositive(int[] nums) {
        int ans = 0;

        for (int i = 0; i < nums.length; i++) {
            while (nums[i] > 0 && nums[i] < nums.length + 1 && nums[i] != nums[nums[i] - 1]) {
                int pos = nums[i] - 1;
                int val = nums[i];
                nums[i] = nums[pos];
                nums[pos] = val;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                ans = i + 1;
                break;
            }
        }

        return ans + 1;
    }

    public static void main(String[] args) {
        int[] nums = {6, 7, 8, 9};
        DemoCode17 demo = new DemoCode17();
        System.out.println(demo.firstMissingPositive(nums));
    }
}
