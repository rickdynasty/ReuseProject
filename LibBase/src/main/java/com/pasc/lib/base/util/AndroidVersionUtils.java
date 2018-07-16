/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pasc.lib.base.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class containing some static utility methods.
 */
public class AndroidVersionUtils {

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (AndroidVersionUtils.hasGingerbread()) {
//            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
//                    new StrictMode.ThreadPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//            StrictMode.VmPolicy.Builder vmPolicyBuilder =
//                    new StrictMode.VmPolicy.Builder()
//                            .detectAll()
//                            .penaltyLog();
//
//            if (Utils.hasHoneycomb()) {
//                threadPolicyBuilder.penaltyFlashScreen();
//                vmPolicyBuilder
//                        .setClassInstanceLimit(ImageGridActivity.class, 1)
//                        .setClassInstanceLimit(ImageDetailActivity.class, 1);
//            }
//            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
//            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshMallow() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.M;
    }

}
