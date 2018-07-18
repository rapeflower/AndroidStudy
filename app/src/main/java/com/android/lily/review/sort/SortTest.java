package com.android.lily.review.sort;

/**
 * @author rape flower
 * @date 2018-07-16 18:26
 * @descripe java排序
 */
public class SortTest {

    public static void main(String[] args) {
        int[] numbers1 = {10, 20, 15, 0, 6, 7, 2, 1, -5, 55};
        bubbleSort(numbers1);
        System.out.print("冒泡排序：");
        printArray(numbers1);

        int[] numbers2 = {520, 3, 8, 22, 76, 21, -10, 66, -100, 99};
        quickSort(numbers2);
        System.out.print("快速排序：");
        printArray(numbers2);

        int[] numbers3 = {10, 20, 15, 0, 6, 7, 2, 1, -5, 55};
        selectSort(numbers3);
        System.out.print("选择排序：");
        printArray(numbers3);

        int[] numbers4 = {520, 3, 8, 22, 76, 21, -10, 66, -100, 99};
        selectSort(numbers4);
        System.out.print("插入排序：");
        printArray(numbers4);
    }

    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ",");
        }
        System.out.println();
    }

    /**
     * 冒泡排序
     *
     * @param numbers 待排序数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j+1]) {
                    temp = numbers[j];
                    numbers[j] = numbers[j+1];
                    numbers[j+1] = temp;
                }
            }
        }
    }

    /**
     * 快速排序
     *
     * @param numbers 待排序数组
     */
    public static void quickSort(int[] numbers) {
        if (numbers.length > 0) {
            quick(numbers, 0, numbers.length - 1);
        }
    }

    /**
     * 递归形式的分治排序算法
     *
     * @param numbers 待排序数组
     * @param low     开始位置
     * @param high    结束位置
     */
    private static void quick(int[] numbers, int low, int high) {
        if (low < high) {
            //将numbers数组一分为二
            int middle = getMiddle(numbers, low, high);
            //对低字段表进行递归排序
            quick(numbers, low, middle - 1);
            //对高字段表进行递归排序
            quick(numbers, middle + 1, high);
        }
    }

    /**
     * 查找出中轴（默认是最低位low）的在numbers数组排序后所在位置
     *
     * @param numbers 待排序数组
     * @param low     开始位置
     * @param high    结束位置
     */
    private static int getMiddle(int[] numbers, int low, int high) {
        int temp = numbers[low];
        while (low < high) {
            while (low < high && numbers[high] > temp) {
                high--;
            }
            //比中轴小的记录移到低段
            numbers[low] = numbers[high];
            while (low < high && numbers[low] < temp) {
                low++;
            }
            //比中轴大的记录移到高端
            numbers[high] = numbers[low];
        }
        //中轴记录到尾
        numbers[low] = temp;
        //返回中轴的位置
        return low;
    }


    /**
     * 选择排序
     * <p>在要排序的一组数中，选出最小的一个数与第一个位置的数交换；
     * 然后在剩下的数当中再找最小的与第二个位置的数交换，
     * 如此循环到倒数第二个数和最后一个数比较为止
     * </p>
     *
     * @param numbers 待排序数组
     */
    public static void selectSort(int[] numbers) {
        //数组长度
        int size = numbers.length;
        //中间变量
        int temp = 0;

        for (int i = 0; i < size; i++) {
            //待确定的位置
            int k = i;
            //选择出应该放在第i个位置的数
            for (int j = size - 1; j > i; j--) {
                if (numbers[j] < numbers[k]) {
                    k = j;
                }
            }
            //交换两个数据的位置
            temp = numbers[i];
            numbers[i] = numbers[k];
            numbers[k] = temp;
        }
    }

    /**
     * 插入排序
     *
     * @param numbers 待排序数组
     */
    public static void insertSort(int[] numbers) {
        int size = numbers.length;
        int temp = 0;
        int j = 0;

        for (int i = 0; i < size; i++) {
            temp = numbers[i];
            //假如temp比前面的值小，则将前面的值后移
            for (j = i; j > 0 && temp < numbers[j - 1]; j--) {
                numbers[j] = numbers[j - 1];
            }
            numbers[j] = temp;
        }
    }
}
