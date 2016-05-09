package wanghaisheng.com.xiaoya.Exception;

/**
 * Created by sheng on 2016/5/9.
 */
public class NoSDCardException extends Exception {
    public NoSDCardException() {
    }

    public NoSDCardException(String detailMessage) {
        super(detailMessage);

    }

    public NoSDCardException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoSDCardException(Throwable throwable) {
        super(throwable);
    }
}
