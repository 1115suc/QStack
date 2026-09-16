package Hot100;

public class DemoCode96 {
    public int singleNumber(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum^=num;
        }
        return sum;
    }

    public static void main(String[] args) {

    }
}
