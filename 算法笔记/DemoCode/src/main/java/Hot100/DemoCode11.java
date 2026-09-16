package Hot100;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class DemoCode11 {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] ans = new int[nums.length - k + 1];
        if (nums.length == 0 || k == 0) return ans;

        Deque<Integer> deque = new LinkedList<>(); // 存索引，不存值！

        for (int i = 0; i < nums.length; i++) {

            // 第一步：清理过期的队头（已滑出窗口）
            if (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.removeFirst();
            }

            // 第二步：维护单调递减队列（从队尾清理比当前值小的索引）
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.removeLast();
            }

            // 第三步：当前索引入队
            deque.addLast(i);

            // 第四步：窗口形成后，队头索引对应的值就是最大值
            if (i >= k - 1) {
                ans[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        // nums = [1,3,-1,-3,5,3,6,7], k = 3
        int[] nums = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;

        DemoCode11 demoCode11 = new DemoCode11();
        int[] ints = demoCode11.maxSlidingWindow(nums, k);
        // [3,3,5,5,6,7]
        System.out.println(Arrays.toString(ints));
    }
}
