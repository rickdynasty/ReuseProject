package com.pasc.lib.base.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.pasc.lib.base.R;
import com.pasc.lib.base.imageloader.GlideImageLoader;

/**
 * Created by chendaixi947 on 18/6/6.
 */
public class ImageLoader {
    public static final int SCALE_FIT_CENTER = 0;
    public static final int SCALE_CENTER_INSIDE = 1;
    public static final int SCALE_CENTER_CROP = 2;

    private static final int DEFAULT_COLOR = R.color.violet_f0f5ff;

    /**
     * 加载网络图片
     *
     * @param imageUrl  图片url
     * @param imageView ImageView对象
     */
    public static void loadImageUrl(String imageUrl, ImageView imageView) {
        loadImageUrl(imageUrl, DEFAULT_COLOR, imageView);
    }

    /**
     * 加载网络图片
     *
     * @param imageUrl     图片url
     * @param imageView    ImageView对象
     * @param defaultImage 默认图片
     */
    public static void loadImageUrl(String imageUrl, @DrawableRes int defaultImage, ImageView imageView) {
        GlideImageLoader.create(imageView)
                .loadImage(imageUrl, defaultImage);
    }


    /**
     * 加载网络图片
     *
     * @param imageUrl  图片url
     * @param imageView ImageView对象
     * @param scaleType 裁剪类型
     */
    public static void loadImageUrl(String imageUrl, ImageView imageView, @ScaleType int scaleType) {
        loadImageUrl(imageUrl, DEFAULT_COLOR, imageView, scaleType);
    }

    /**
     * 加载网络图片
     *
     * @param imageUrl     图片url
     * @param imageView    ImageView对象
     * @param scaleType    裁剪类型
     * @param defaultImage 默认图片
     */
    public static void loadImageUrl(String imageUrl, @DrawableRes int defaultImage,
                                    ImageView imageView, @ScaleType int scaleType) {
        GlideImageLoader.create(imageView)
                .load(imageUrl, getRequestOptions(scaleType)
                        .error(defaultImage)
                        .placeholder(defaultImage));
    }

    /**
     * 加载网络图片
     *
     * @param imageUrl   图片url
     * @param imageView  ImageView对象
     * @param scaleType  裁剪类型
     * @param errorImage 图片
     */
    public static void loadLocalImageWithError(String imageUrl, @DrawableRes int errorImage,
                                               ImageView imageView, @ScaleType int scaleType) {
        GlideImageLoader.create(imageView)
                .load(imageUrl, getRequestOptions(scaleType)
                        .error(errorImage)
                        .placeholder(DEFAULT_COLOR));
    }


    /**
     * 加载sd卡图片
     *
     * @param imagePath 本地图片路径
     * @param imageView ImageView对象
     */
    public static void loadLocalImage(String imagePath, ImageView imageView) {
        loadLocalImage(imagePath, imageView, DEFAULT_COLOR);
    }

    /**
     * 加载sd卡图片
     *
     * @param imagePath    本地图片路径
     * @param imageView    ImageView对象
     * @param defaultImage 默认图片
     */
    public static void loadLocalImage(String imagePath, ImageView imageView, @DrawableRes int defaultImage) {
        GlideImageLoader.create(imageView)
                .loadLocalImage(imagePath, defaultImage);
    }

    /**
     * 加载sd卡图片
     *
     * @param imagePath    本地图片路径
     * @param imageView    ImageView对象
     * @param defaultImage 默认图片
     * @param scaleType    裁剪类型
     */
    public static void loadLocalImage(String imagePath, ImageView imageView, @DrawableRes int defaultImage, @ScaleType int scaleType) {
        GlideImageLoader.create(imageView)
                .load(imagePath, getRequestOptions(scaleType)
                        .error(defaultImage)
                        .placeholder(defaultImage));
    }

    /**
     * 加载资源图片
     *
     * @param resId     本地图片id
     * @param imageView ImageView对象
     */
    public static void loadImageRes(int resId, ImageView imageView) {
        loadImageRes(resId, DEFAULT_COLOR, imageView);
    }


    /**
     * 加载资源图片
     *
     * @param resId        本地图片id
     * @param defaultImage 默认图片
     * @param imageView    ImageView对象
     */
    public static void loadImageRes(int resId, @DrawableRes int defaultImage, ImageView imageView) {
        GlideImageLoader.create(imageView)
                .loadLocalImage(resId, defaultImage);
    }

    /**
     * 加载资源图片
     *
     * @param resId        本地图片id
     * @param defaultImage 默认图片
     * @param imageView    ImageView对象
     * @param scaleType    裁剪类型
     */
    public static void loadImageRes(int resId, @DrawableRes int defaultImage, ImageView imageView, @ScaleType int scaleType) {
        GlideImageLoader.create(imageView)
                .load(resId, getRequestOptions(scaleType)
                        .error(defaultImage)
                        .placeholder(defaultImage));
    }

    /**
     * 加载网络图片
     *
     * @param url     图片网址
     * @param options RequestOptions对象
     */
    public static void load(ImageView imageView, String url, RequestOptions options) {
        GlideImageLoader.create(imageView)
                .load(url, options);
    }

    @IntDef({SCALE_CENTER_CROP, SCALE_CENTER_INSIDE, SCALE_FIT_CENTER})
    @interface ScaleType {
    }


    @NonNull
    private static RequestOptions getRequestOptions(@ScaleType int scaleType) {
        RequestOptions requestOptions;
        if (scaleType == SCALE_CENTER_CROP) {
            requestOptions = new RequestOptions().centerCrop();
        } else if (scaleType == SCALE_CENTER_INSIDE) {
            requestOptions = new RequestOptions().centerInside();
        } else {
            requestOptions = new RequestOptions().fitCenter();
        }
        return requestOptions;
    }

}

