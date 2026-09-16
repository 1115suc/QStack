package leetcode01;

public class LeetCode35 {
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while(l < r) {
            int mid = (l + r) >> 1;
            if (target <= nums[mid]) r = mid;
            else l = mid + 1;
        }

        if(nums[l] >= target) return l;
        else return l + 1;
    }

    public static void main(String[] args) {
        LeetCode35 leetCode35 = new LeetCode35();
        System.out.println(leetCode35.searchInsert(new int[]{1,3,5,6}, 3));
        System.out.println(leetCode35.searchInsert(new int[]{1,3,5,6}, 4));
    }
}
