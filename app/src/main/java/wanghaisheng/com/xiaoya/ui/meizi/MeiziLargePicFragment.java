package wanghaisheng.com.xiaoya.ui.meizi;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.component.fresco.photodraweeview.OnPhotoTapListener;
import wanghaisheng.com.xiaoya.component.fresco.photodraweeview.OnViewTapListener;
import wanghaisheng.com.xiaoya.component.fresco.photodraweeview.PhotoDraweeView;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziLargePicPresenter;
import wanghaisheng.com.xiaoya.presenter.meizi.MeiziLargePicView;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/5/8.
 */
public class MeiziLargePicFragment extends BaseFragment implements MeiziLargePicView {
    public static final String ARG_INDEX = "arg_index";
    public static final String ARG_URL = "arg_url";
    public static final String ARG_GROUPID = "arg_groupid";

    private int index;
    private String groupId;
    private String url;

//    TouchImageView imageView;
    PhotoDraweeView mPhotoDraweeView;
    MaterialDialog mDialog;

    @Inject
    MeiziLargePicPresenter presenter;
    @Inject
    ToastUtil toastUtil;

    public static MeiziLargePicFragment newInstance(int index,String url,String groupId) {
        MeiziLargePicFragment fragment = new MeiziLargePicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_INDEX,index);
        bundle.putString(ARG_URL,url);
        bundle.putString(ARG_GROUPID,groupId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_large_pic;
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        this.index = getArguments().getInt(ARG_INDEX);
        this.groupId = getArguments().getString(ARG_GROUPID);
        this.url = getArguments().getString(ARG_URL);
//        LogUtils.d("print urls..............");
//        LogUtils.d(url);
    }

    @Override
    public void beforeInitView(View view) {

    }

    @Override
    public void initView(View view) {
        this.mPhotoDraweeView = (PhotoDraweeView) view.findViewById(R.id.image);
    }

    @Override
    public void initData() {
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("url.........."+url);

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(Uri.parse(url));
        controller.setOldController(mPhotoDraweeView.getController());
        // You need setControllerListener
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null) {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mPhotoDraweeView.setController(controller.build());
        mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override public void onPhotoTap(View view, float x, float y) {
//                Toast.makeText(view.getContext(), "onPhotoTap :  x =  " + x + ";" + " y = " + y,
//                        Toast.LENGTH_SHORT).show();
                getActivity().supportFinishAfterTransition();
            }
        });
        mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override public void onViewTap(View view, float x, float y) {
//                Toast.makeText(view.getContext(), "onViewTap", Toast.LENGTH_SHORT).show();
            }
        });

        mPhotoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
//                Toast.makeText(v.getContext(), "onLongClick", Toast.LENGTH_SHORT).show();

                mDialog = new MaterialDialog.Builder(getActivity()).content("保存图片")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                presenter.saveLargePic(url,groupId+"_"+index);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();

                return true;
            }
        });

    }

    public View getSharedElement() {
        return mPhotoDraweeView;
    }

    @Override
    public void onImageSaved(boolean imgSaved,String imgPath) {
        if(imgSaved) {
            toastUtil.showToast(String.format(getString(R.string.msg_image_saved),imgPath));
        } else {
            toastUtil.showToast(imgPath);
        }
        mDialog.dismiss();
    }
}
