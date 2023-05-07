package com.perry.jasus.Util;

public class SmsInfo {
    private String mAddress;
    private String mBody;
    private long mDateInMillis;

    public SmsInfo(String address, String body, long dateInMillis) {
        mAddress = address;
        mBody = body;
        mDateInMillis = dateInMillis;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getBody() {
        return mBody;
    }

    public long getDateInMillis() {
        return mDateInMillis;
    }

    @Override
    public String toString() {
        return "SmsInfo{" +
                "mAddress='" + mAddress + '\'' +
                ", mBody='" + mBody + '\'' +
                ", mDateInMillis=" + mDateInMillis +
                '}';
    }
}