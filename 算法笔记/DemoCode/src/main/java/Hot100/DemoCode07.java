package Hot100;

public class DemoCode07 {
    public int trap(int[] height) {
        int ans = 0;
        int left = 0,  right = height.length - 1;
        int leftMax = height[left], rightMax = height[right];
        while(left < right){
            if(height[left] < height[right]){
                ans += (leftMax - height[left]);
                left++;
                leftMax = Math.max(leftMax, height[left]);
            } else{
                ans += (rightMax - height[right]);
                right--;
                rightMax = Math.max(rightMax, height[right]);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        // [0,1,0,2,1,0,1,3,2,1,2,1]
        int[] height = new int[]{0,1,0,2,1,0,1,3,2,1,2,1};
        DemoCode07 demoCode07 = new DemoCode07();
        System.out.println(demoCode07.trap(height));
    }
}
