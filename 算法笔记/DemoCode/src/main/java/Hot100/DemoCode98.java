package Hot100;

public class DemoCode98 {
    public void sortColors(int[] nums) {
        int n = nums.length;
        int left = 0,right = n-1;
        int i = 0;
        while(i <= right){
            if(nums[i] == 0){
                swap(nums,i,left);
                left++;
                i++;
            }
            else if(nums[i] == 1){
                i++;
            }
            else{
                swap(nums,i,right);
                right--;
                //不需要i++，因为换过来的数也需要判断
            }
        }
    }

    private void swap(int[] nums,int a,int b){
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    public static void main(String[] args) {

    }
}
