package Hot100;

public class DemoCode13 {
    public int maxSubArray(int[] nums) {
        if (nums.length == 1) return nums[0];

        int ans = Integer.MIN_VALUE;
        int nowPrefixSum = 0;
        int minPrefixSum = 0;

        for (int num : nums) {
            nowPrefixSum += num;
            ans = Math.max(ans, nowPrefixSum - minPrefixSum);
            minPrefixSum = Math.min(minPrefixSum, nowPrefixSum);
        }
        return ans;
    }

    public static void main(String[] args) {
        // [-2,1,-3,4,-1,2,1,-5,4] 6
        int[] nums = {5, 4, -1, 7, 8};
        DemoCode13 demoCode13 = new DemoCode13();
        int ans = demoCode13.maxSubArray(nums);
        System.out.println(ans);
    }
}
