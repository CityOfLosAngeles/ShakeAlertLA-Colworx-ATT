package com.app.shakealertla;

import java.text.DecimalFormat;
public class JavaDemo {
    public static void main(String args[]) {
        Double num = 7.963;
        String str = " 7.68";
        System.out.println(new DecimalFormat("#.##").format(Double.valueOf(str)));
    }
}
