package wanghaisheng.com.xiaoya.ui.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.utils.TDevice;

public class EmptyLayout extends LinearLayout implements
        android.view.View.OnClickListener {// , ISkinUIObserver {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;

    protected TextView tvLoading;
    protected LinearLayout progressContainer;
    protected RelativeLayout rlProgress;
    protected RelativeLayout rlError;
    protected TextView tvError;
    protected ImageView errorImage;
    private String strNoDataContent = "";
    private boolean clickEnable = true;

    private android.view.View.OnClickListener listener;

    private int mErrorState;

    private Context context;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_empty_layout, null);
        tvLoading = (TextView) view.findViewById(R.id.tvLoading);
        progressContainer = (LinearLayout) view.findViewById(R.id.progress_container);
        rlProgress = (RelativeLayout) view.findViewById(R.id.rlProgress);
        rlError = (RelativeLayout) view.findViewById(R.id.rlError);
        tvError = (TextView) view.findViewById(R.id.tvError);
        errorImage = (ImageView) view.findViewById(R.id.iv_error_img);

        errorImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });

        setOnClickListener(this);
        addView(view);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    /**
     *
     * @param msg
     */
    public void setErrorMessage(String msg) {
        tvError.setText(msg);
    }

    /**
     * 新添设置背景
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    public void setErrorImag(int imgResource) {
        try {
            errorImage.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    /**
     * 设置状态
     * @param i
     */
    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case NETWORK_ERROR:
            mErrorState = NETWORK_ERROR;
            if (TDevice.hasInternet()) {
                tvError.setText(R.string.error_view_load_error_click_to_refresh);
                errorImage.setBackgroundResource(R.drawable.pagefailed_bg);
            } else {
                tvError.setText(R.string.error_view_network_error_click_to_refresh);
                errorImage.setBackgroundResource(R.drawable.page_icon_network);
            }
            rlError.setVisibility(VISIBLE);
            rlProgress.setVisibility(GONE);
            clickEnable = true;
            break;
        case NETWORK_LOADING:
            mErrorState = NETWORK_LOADING;
            rlProgress.setVisibility(VISIBLE);
            rlError.setVisibility(GONE);
            tvError.setText(R.string.error_view_loading);
            clickEnable = false;
            break;
        case NODATA:
            mErrorState = NODATA;
            errorImage.setBackgroundResource(R.drawable.page_icon_empty);
            rlProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            tvError.setVisibility(VISIBLE);
            clickEnable = true;
            break;
        case HIDE_LAYOUT:
            mErrorState = HIDE_LAYOUT;
            setVisibility(View.GONE);
            break;
        case NODATA_ENABLE_CLICK:
            mErrorState = NODATA_ENABLE_CLICK;
            errorImage.setBackgroundResource(R.drawable.page_icon_empty);
            rlError.setVisibility(View.VISIBLE);
            rlProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            clickEnable = true;
            break;
        default:
            break;
        }
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            tvError.setText(strNoDataContent);
        else
            tvError.setText(R.string.error_view_no_data);
    }

    /**
     * 设置无内容时显示的文本
     * @param noDataContent
     */
    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    /**
     * 注入屏幕点击事件
     * @param listener
     */
    public void setOnLayoutClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置是否可见
     * @param visibility
     */
    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }

    /***************************************
     * 设置状态，以方便调用
     ***************************************/

    /**
     * 网络错误
     */
    public void setNetworkError() {
        setErrorType(NETWORK_ERROR);
    }

    /**
     * 正在加载中
     */
    public void setNetworkLoading() {
        setErrorType(NETWORK_LOADING);
    }

    /**
     * 无数据，且不能点击
     */
    public void setNodata() {
        setErrorType(NODATA);
    }

    /**
     * 无数据，但可以点击
     */
    public void setNodataEnableClick() {
        setErrorType(NODATA_ENABLE_CLICK);
    }

    /**
     * 移除
     */
    public void dismiss() {
        setErrorType(HIDE_LAYOUT);
    }


}