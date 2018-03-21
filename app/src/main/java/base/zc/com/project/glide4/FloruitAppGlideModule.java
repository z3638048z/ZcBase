package base.zc.com.project.glide4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import base.zc.com.project.R;

@GlideModule
public class FloruitAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
        );
//        builder.setDefaultTransitionOptions(Drawable.class, DrawableTransitionOptions.withCrossFade());
        builder.setDefaultTransitionOptions(Drawable.class, GenericTransitionOptions.<Drawable>with(R.anim.glide_fade_anim));
//        String path = context.getCacheDir().getAbsolutePath();
//        String path2 = context.getExternalCacheDir().getAbsolutePath();
//        builder.setDiskCache(new DiskLruCacheFactory(path, 1024 * 1024 * 200));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
