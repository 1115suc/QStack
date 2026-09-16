package Hot100;

public class DemoCode97 {
    public int majorityElement(int[] nums) {
        int res = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != res) {
                if (count == 0) {
                    res = nums[i];
                    count++;
                } else {
                    count--;
                }
            } else {
                count++;
            }
        }
        return res;
    }

    public static void main(String[] args) {

    }
}
