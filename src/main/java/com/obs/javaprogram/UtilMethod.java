package com.obs.javaprogram;

public class UtilMethod {
    public boolean verifyheckEven(int num) {
        boolean flag = false;
        int r = num % 2;
        if (r == 0) {
            flag = true;
        }
        return flag;
    }
    public boolean verifyPrimeOrNot(int num){
        boolean flag=true;
        for(int i=2;i<=num/2;i++){
            if(num%i==0){
               flag=false;
            }
        }
        return flag;
    }
}
