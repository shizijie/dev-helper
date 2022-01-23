package com.shizijie.dev.helper.web.demo;

/**
 * @author shizijie
 * @version 2022-01-19 下午11:38
 */
public class BitMapTest {
    /**
     * 1 byte = 8 bit    1 int = 4 byte
     */
    private int[] bitArray;

    /**
     * 左移：移动n位 乘2的n次方
     * 右移：移动n位 除2的n次方
     * @param size
     */
    public BitMapTest(long size) {
        bitArray = new int[(int) (size / 32 + 1)];
    }

    public void set1(int num) {
        //确定数组 index
        int arrayIndex = num / 32; // or num >> 5;
        //确定bit index
        int bitIndex = num % 32; // or num & 31;
        //设置1   1向左偏移多少位，多少位就是1，取"或"关系则永远是1
        bitArray[arrayIndex] |= 1 << bitIndex;
    }

    public void set0(int num) {
        //确定数组 index
        int arrayIndex = num / 32; // or num >> 5;
        //确定bit index
        int bitIndex = num % 32; // or num & 31;
        //设置0   1向左偏移多少位，多少位就是1，取"非"关系则是0，再取"与"关系则永远是0
        bitArray[arrayIndex] &= ~(1 << bitIndex);
    }


    public boolean isExists(int num) {
        //确定数组 index
        int arrayIndex = num / 32; // or num >> 5;
        //确定bit index
        int bitIndex = num % 32; // or num & 31;
        //判断是否存在，1向左偏移多少位，多少位是1，取"与"关系，只有原位是1才是1
        System.out.println((bitArray[arrayIndex] & (1 << bitIndex)));
        System.out.println(get32BitBinString(bitArray[arrayIndex]));
        System.out.println(get32BitBinString(1 << bitIndex));
        return (bitArray[arrayIndex] & (1 << bitIndex)) != 0;
    }

    private static String get32BitBinString(int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            stringBuilder.append(number & 1);
            number = number >>> 1;
        }
        return stringBuilder.reverse().toString();
    }

    private String getBitString(int num) {
        //确定数组 index
        int arrayIndex = num / 32; // or num >> 5;
        return get32BitBinString(bitArray[arrayIndex]);
    }

    public static void main(String[] args) {
        System.out.println("bit String : " + get32BitBinString(5));
        BitMapTest bitMapTest = new BitMapTest(100);
        System.out.println("number is existe: " + bitMapTest.isExists(33));
        System.out.println("String: " + bitMapTest.getBitString(33));
        bitMapTest.set0(33);
        System.out.println("set 0 String: " + bitMapTest.getBitString(33));
        bitMapTest.set1(36);
        bitMapTest.set1(35);
        bitMapTest.set1(37);
        System.out.println("set 1 String: " + bitMapTest.getBitString(33));
        System.out.println(bitMapTest.isExists(33));
        System.out.println(get32BitBinString(-1));
        System.out.println(get32BitBinString(Integer.MAX_VALUE));
        System.out.println(get32BitBinString(Integer.MIN_VALUE));
    }
}
