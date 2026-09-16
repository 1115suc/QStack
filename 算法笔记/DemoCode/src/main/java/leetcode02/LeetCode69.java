package leetcode02;

public class LeetCode69 {
    private static final int SQRT_INT_MAX = (int) Math.sqrt(Integer.MAX_VALUE);

    public int mySqrt(int x) {
        int l = 0, r = Math.min(x, SQRT_INT_MAX) + 1;
        while(l + 1 < r) {
            int mid = (l + r) >>> 1;
            if (mid * mid <= x) l = mid;
            else r = mid;
        }
        return l;
    }

    public static void main(String[] args) {
        LeetCode69 leetCode69 = new LeetCode69();
        System.out.println(leetCode69.mySqrt(8));
    }
}
