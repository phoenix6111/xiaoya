package wanghaisheng.com.xiaoya.ui.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.presenter.feedback.FeedbackPresenter;
import wanghaisheng.com.xiaoya.presenter.feedback.FeedbackView;
import wanghaisheng.com.xiaoya.ui.BaseSwipeBackActivity;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/4/22.
 */
public class FeedbackActivity extends BaseSwipeBackActivity implements FeedbackView{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.feedback_title)
    EditText feedbackTitle;
    @Bind(R.id.feedback_content)
    EditText feedbackContent;
    @Bind(R.id.scrollView)
    HorizontalScrollView scrollView;
    @Bind(R.id.feedback_image_container)
    LinearLayout feedbackImageContainer;

    @Inject
    ToastUtil toastUtil;

    private String title;
    private String content;

    public static final String ARG_SELECTED_IMAGES = "selected_images";
    private ArrayList<String> selectImages = new ArrayList<>();
    private MaterialDialog mDialog;

    @Inject
    FeedbackPresenter presenter;

    @Override
    public void getDatas(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.feedback_layout;
    }

    @Override
    public void showLoading() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.show();
        }
    }

    @Override
    public void postSuccess() {
        toastUtil.showToast("信息提交成功，谢谢您的反馈！！");
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                finish();
            }
        });
    }

    @Override
    public void postFailure(String msg) {
        toastUtil.showToast("反馈信息提交失败");
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            send();
        } else if (id == R.id.action_camera) {
            Intent intent = new Intent(FeedbackActivity.this,SelectImageActivity.class);
            intent.putExtra(ARG_SELECTED_IMAGES, selectImages);
            startActivityForResult(intent,SelectImageActivity.REQUEST_IMAGE);
        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SelectImageActivity.REQUEST_IMAGE&&resultCode==RESULT_OK) {
            //获取选取的照片地址
            ArrayList<String> pickedImgs = data.getStringArrayListExtra(ARG_SELECTED_IMAGES);
            selectImages.clear();
            selectImages.addAll(pickedImgs);
            //将照片显示在feedbackImageContainer中
            updatePicsUI();
        }
    }

    /**
     * 根据用户选择的图片更新视图，显示图片
     */
    private void updatePicsUI() {
        if (selectImages.isEmpty()) {
            scrollView.setVisibility(View.GONE);
            feedbackImageContainer.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            feedbackImageContainer.setVisibility(View.VISIBLE);
            feedbackImageContainer.removeAllViews();
            for (String path : selectImages) {
                View itemView = View.inflate(this, R.layout.feedback_img_item, null);
                SimpleDraweeView ivPic = (SimpleDraweeView) itemView.findViewById(R.id.ivPic);
                itemView.setTag(R.id.ivPic,path);
                itemView.setOnClickListener(onPictureClickListener);
                ivPic.setImageURI(Uri.fromFile(new File(path)));
                feedbackImageContainer.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    // 暂时只支持删除，不支持预览
    View.OnClickListener onPictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String path = v.getTag(R.id.ivPic).toString();

            new AlertDialogWrapper.Builder(FeedbackActivity.this)
                    .setMessage("确定删除图片")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectImages.remove(path);
                            for (int i = 0; i < feedbackImageContainer.getChildCount(); i++) {
                                View view = feedbackImageContainer.getChildAt(i);

                                if (view.getTag(R.id.ivPic).toString().equals(path)) {
                                    feedbackImageContainer.removeView(view);
                                    break;
                                }
                            }

                            if (selectImages.isEmpty()) {
                                scrollView.setVisibility(View.GONE);
                            }
                        }

                    })
                    .show();
        }

    };

    /**
     * 点击发送后，即调用presenter提交数据
     */
    private void send() {
        title = feedbackTitle.getText().toString();
        content = feedbackContent.getText().toString();
        LogUtils.d("send feedback datas...................");
        if(!TextUtils.isEmpty(content)) {
            presenter.postFeedbackData(title,content,selectImages);
        } else {
            toastUtil.showToast("请输入反馈内容。。");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initToolbar(toolbar);

        presenter.attachView(this);

        setTitle("反馈");
        feedbackTitle.setFocusable(false);
        feedbackTitle.setFocusableInTouchMode(false);
        feedbackTitle.setText(R.string.feedback_title);
        feedbackContent.setHint(R.string.feedback_content_hint);

        mDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在发送")
                .progress(true, 0).build();
    }

    @Override
    public void initData() {

    }

    @Override
    public void error(int errorType, String errMsg) {

    }
}
