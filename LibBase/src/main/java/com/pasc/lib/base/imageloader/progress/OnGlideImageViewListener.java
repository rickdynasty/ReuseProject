package com.pasc.lib.base.imageloader.progress;

import com.bumptech.glide.load.engine.GlideException;

public interface OnGlideImageViewListener {

    void onProgress(int percent, boolean isDone, GlideException exception);
}
