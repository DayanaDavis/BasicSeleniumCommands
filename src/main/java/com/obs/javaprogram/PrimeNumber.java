package com.obs.javaprogram;

public class PrimeNumber {
    public static void main(String[] args) {
        for (int i = 2; i <= 25; i++) {
            UtilMethod utilMethod = new UtilMethod();
            if (utilMethod.verifyPrimeOrNot(i)) {
                System.out.println(i);
            }
        }
    }
}