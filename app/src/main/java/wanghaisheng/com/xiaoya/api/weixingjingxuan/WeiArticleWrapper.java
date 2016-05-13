package wanghaisheng.com.xiaoya.api.weixingjingxuan;

/**
 * Created by sheng on 2016/5/13.
 */
public class WeiArticleWrapper {

    private String reason;
    private WeiArticleResult result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public WeiArticleResult getResult() {
        return result;
    }

    public void setResult(WeiArticleResult result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

}
