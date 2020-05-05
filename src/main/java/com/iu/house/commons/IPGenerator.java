package com.iu.house.commons;

import java.util.Random;

public class IPGenerator {
    public static String getNewIP() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }
}
