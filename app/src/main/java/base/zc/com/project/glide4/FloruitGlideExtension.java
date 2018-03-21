package base.zc.com.project.glide4;

import android.annotation.SuppressLint;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

@GlideExtension
public class FloruitGlideExtension {

    private FloruitGlideExtension() {

    }

    @SuppressLint("CheckResult")
    @GlideOption
    public static void roundBound(RequestOptions options, final int radius, final int boundWidth, final int color) {
        options.transforms(new CenterCrop(), new RoundedCorners(radius - boundWidth), new RoundBound(radius, boundWidth, color));
    }

}
