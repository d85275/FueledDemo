package com.e.fueleddemo.remote;

import android.webkit.JavascriptInterface;

public class JavaScriptUtils {

    private double lat;
    private double lng;

    public JavaScriptUtils(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @JavascriptInterface
    public double getLat() {
        return lat;
    }

    @JavascriptInterface
    public double getLng() {
        return lng;
    }
}
