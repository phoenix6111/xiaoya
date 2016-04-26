package wanghaisheng.com.xiaoya.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by sheng on 2016/4/14.
 */
public class ImageConfig {
    private Context context;

    public ImageConfig(Context context) {
        this.context = context;
    }

    /**
     * 获取图片临时目录(网络图片缓存)
     *
     * @return
     */
    public File getImageTmpDir() {
        File dir = new File(getTmpDir(), "image_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public File getTmpDir() {
        return getTmpDir(false);
    }

    /**
     * 获取临时目录
     *
     * @param isSdcard 是否只取sd卡上的目录
     * @return
     */
    public File getTmpDir(boolean isSdcard) {
        File tmpDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdcard && !sdCardExist) {
            if (!sdCardExist) {
                return null;
            }
        }

        if (sdCardExist || isSdcard) {
            tmpDir = new File(Environment.getExternalStorageDirectory(),
                    getTmpDirName());
        } else {
            tmpDir = new File(context.getCacheDir(), getTmpDirName());
        }

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpDir;
    }

    /**
     * 获取缓存目录名
     *
     * @return
     */
    public String getTmpDirName() {
        return "ImageLoaderFactory";
    }
}
