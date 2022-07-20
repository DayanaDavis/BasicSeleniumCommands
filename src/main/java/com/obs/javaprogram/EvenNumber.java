package com.obs.javaprogram;

import io.opentelemetry.exporter.logging.SystemOutLogExporter;

public class EvenNumber {
    public static void main(String[] args) {
    UtilMethod utilMethod=new UtilMethod();
    boolean status;
    for (int i = 0; i <= 10; i++) {
        status=utilMethod.verifyheckEven(i);
        if(status){
            System.out.println(i);
        }
        }
    }
}