package Hot100;

import java.util.HashMap;
import java.util.Map;

public class DemoCode10 {
    public int subarraySum(int[] nums, int k) {
        int ans = 0, prefixSum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        for (int num : nums) {
            prefixSum += num;
            int target = prefixSum - k;
            if (map.containsKey(target)) ans += map.get(target);

            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
        }

        return ans;
    }

    static void main(String[] args) {
        // [1,1,1], k = 2
        int[] arr = {1, 2, 1, 2, 1};
        int k = 3;
        DemoCode10 demoCode10 = new DemoCode10();
        int i = demoCode10.subarraySum(arr, k);
        System.out.println(i);
    }
}
