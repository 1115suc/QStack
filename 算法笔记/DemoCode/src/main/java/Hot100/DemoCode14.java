package Hot100;

import java.util.Arrays;

public class DemoCode14 {

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        int[][] ans = new int[intervals.length][2];
        int j = 0;

        for (int i = 0; i < intervals.length; i++) {
            if (j == 0 || intervals[i][0] > ans[j - 1][1]) {
                ans[j][0] = intervals[i][0];
                ans[j][1] = intervals[i][1];
                j++;
            } else {
                ans[j - 1][1] = Math.max(ans[j - 1][1], intervals[i][1]);
            }
        }

        return Arrays.copyOfRange(ans, 0, j);
    }

    public static void main(String[] args) {
        // [[1,6],[8,10],[15,18]]
        int[][] intervals = {{1, 6}, {8, 10}, {15, 18}};
        DemoCode14 demoCode14 = new DemoCode14();

        int[][] ans = demoCode14.merge(intervals);
        System.out.println(Arrays.deepToString(ans));
    }
}
