package wanghaisheng.com.xiaoya.Exception;

/**
 * Created by sheng on 2016/5/10.
 */
public class NoDataException extends Exception {
    public NoDataException() {
    }

    public NoDataException(String detailMessage) {
        super(detailMessage);
    }

    public NoDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoDataException(Throwable throwable) {
        super(throwable);
    }
}
