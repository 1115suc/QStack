package Hot100;

public class DemoCode05 {
    public int maxArea(int[] height) {
        int ans = 0;
        int left = 0,  right = height.length - 1;

        while (left < right) {
            int h = Math.min(height[left], height[right]);
            ans = Math.max(ans, h * (right - left));

            if (height[left] < height[right]) left++;
            else right--;
        }

        return ans;
    }

    public static void main(String[] args) {
        // [1,8,6,2,5,4,8,3,7]
        int[] nums = new int[]{4,3,2,1,4};

        DemoCode05 demo = new DemoCode05();
        System.out.println(demo.maxArea(nums));
    }
}
