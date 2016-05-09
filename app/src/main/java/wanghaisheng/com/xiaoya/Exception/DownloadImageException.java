package wanghaisheng.com.xiaoya.Exception;

/**
 * Created by sheng on 2016/5/9.
 */
public class DownloadImageException extends Exception {
    public DownloadImageException() {
    }

    public DownloadImageException(String detailMessage) {
        super(detailMessage);
    }

    public DownloadImageException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DownloadImageException(Throwable throwable) {
        super(throwable);
    }
}
