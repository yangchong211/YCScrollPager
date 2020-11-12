package com.ycbjie.ycscrollpager.tiktok;

import android.content.Context;

import com.yc.video.surface.InterSurfaceView;
import com.yc.video.surface.RenderTextureView;
import com.yc.video.surface.SurfaceFactory;


public class TikTokRenderViewFactory extends SurfaceFactory {

    public static TikTokRenderViewFactory create() {
        return new TikTokRenderViewFactory();
    }

    @Override
    public InterSurfaceView createRenderView(Context context) {
        return new TikTokRenderView(new RenderTextureView(context));
    }
}
