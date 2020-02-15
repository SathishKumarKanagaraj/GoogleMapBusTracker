/*
 * @category Daimler
 * @copyright Copyright (C) 2018 Contus. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.bustrackingapp;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * This is the Marker animation class is used for making marker animation
 *
 *
 * @author Contus Team <developers@contus.in>
 * @version 1.
 * */
public class MarkerAnimation {


    /**
     * Default Constructor
     */
    private MarkerAnimation(){

    }

    /**
     * this method used for make animation
     * using this object move the bus object
     *
     * @param  marker object used to get latlng
     * @param finalPosition destination or new latlang
     * @param heading value passed
     * @param latLngInterpolator  interpolator interface class
     */
    public static void animateMarkerToGB(final Marker marker,
                                         final LatLng finalPosition, final float heading,
                                         final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;
            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
            //    marker.setRotation(heading);
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}