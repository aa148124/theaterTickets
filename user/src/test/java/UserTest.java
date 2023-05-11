import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class UserTest {

    @Test
    public void dateTest() {
        LocalDateTime time1 = LocalDateTime.now();
        String str = time1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("str = " + str);
    }

    @Test
    public void testLong() {
        String seat = "001110000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000,000000000000";
        String[] oldSeat = seat.split(",");
        //获取座位坐标
        String[] str = "4排7座 5排12座 5排8座 ".replace("座 ", ",").split(",");
        for (String s : str) {
            String[] s1 = s.split("排");
            for (int i = 0; i < s1.length; i += 2) {
                //s1[i]排s1[i+1]座
                int x = Integer.valueOf(s1[i]);//排
                int y = Integer.valueOf(s1[i + 1]);//座
                oldSeat[x - 1] = oldSeat[x - 1].substring(0, y - 1) + "1" + oldSeat[x - 1].substring(y, oldSeat[x - 1].length());
            }
        }
        String newSeat = "";
        for (int i = 0; i < oldSeat.length; i++) {
            newSeat += oldSeat[i];
            if (i != oldSeat.length - 1) {
                newSeat += ",";
            }
        }
        System.out.println(newSeat);
    }

    @Test
    public void test1() {
        String n = "acea";

    }


    @Test
    public String largestNumber() {
        int[] nums = {3,30,34,5,9};
        String[] arr = new String[nums.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(arr, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (o1+o2).compareTo(o2+o1);
            }
        });
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result = result + arr[i];
        }
        return result;
    }

    public int count(int n) {
        if (n < 2) {
            return n;
        } else {
            return n % 2 == 0 ? count(n / 2) + 2 : count(n - 2) + 1;
        }
    }

    @Test
    public void test2() {

        Scanner sc = new Scanner(System.in);

        int n = 100000;

        // 初始化所有人还在圈内
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = 1;
        }

        // 报数
        int left = n;
        int count = 0; // 每报数到3，count加1
        int i = 0; // i表示当前报数到哪个人
        while (left > 1) { // 当圈内还剩一个人时结束循环
            if (a[i] == 1) { // 如果这个人还在圈内
                count++;
                if (count == 3) { // 报数到3，这个人退出圈子
                    a[i] = 0;
                    left--;
                    count = 0; // 重置count，从1开始报数
                }
            }
            i++;
            if (i == n) { // 报数完一轮，重新从第一个人开始报数
                i = 0;
            }
        }

        // 找到最后留下的那个人的编号
        for (i = 0; i < n; i++) {
            if (a[i] == 1) {
                System.out.println("最后留下的是原来第" + (i + 1) + "号的那位");
                break;
            }
        }
    }
}
