package com.yunhongmin.kafka;

public class VersionUtil {
    public static String getVersion() {
        try {
            return VersionUtil.class.getPackage().getImplementationVersion();
        } catch (Exception e) {
            return ("0.0.0.0");
        }
    }
}
