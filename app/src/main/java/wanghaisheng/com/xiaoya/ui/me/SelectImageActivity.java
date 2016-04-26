package wanghaisheng.com.xiaoya.ui.me;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.widget.imagepick.ImageAdapter;
import wanghaisheng.com.xiaoya.widget.imagepick.ListImageDirPopupWindow;
import wanghaisheng.com.xiaoya.widget.imagepick.beans.FolderBean;

/**
 * Created by sheng on 2016/4/22.
 */
public class SelectImageActivity extends AppCompatActivity {

    private GridView mGridView;
    private RelativeLayout mBottomLY;
    private TextView mDirName;
    private TextView mDirImgCount;

    private List<FolderBean> mFolderBeans = new ArrayList<>();

    //扫描进度条
    private ProgressDialog mProgressDialog;

    private int mMaxCount;
    //当前列表的dir
    private File mCurrentDir;
    //mCurrentDir中扫描到的images
    private List<String> mImgs = new ArrayList<>();

    private ImageAdapter mImageAdapter;

    private static final int SCAN_COMPLETED = 0x110;

    private ListImageDirPopupWindow mPopupWindow;

    private Button checkBtn;

    public static final int REQUEST_IMAGE = 0x110;

    //更新UI的handler
    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == SCAN_COMPLETED) {
                mProgressDialog.dismiss();

                //绑定数据到View
                data2View();

                //initEvents();
                initPopupWindow();
            }
        }
    };

    /**
     * 初始化popupwindow
     */
    private void initPopupWindow() {
        mPopupWindow = new ListImageDirPopupWindow(SelectImageActivity.this,mFolderBeans);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.DirSelectedListener() {
            @Override
            public void dirSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getPath());
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if(filename.endsWith(".jpg")||filename.endsWith("jpeg")||filename.endsWith("png")||filename.endsWith("gif")) {
                            return true;
                        }
                        return false;
                    }
                }));

                mImageAdapter = new ImageAdapter(SelectImageActivity.this,mImgs,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdapter);

                mDirName.setText(folderBean.getName());
                mDirImgCount.setText(""+mImgs.size());

                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 绑定数据到视图
     */
    private void data2View() {
        if(mCurrentDir == null) {
            Toast.makeText(SelectImageActivity.this,"未扫描到任何图片",Toast.LENGTH_LONG);
            return;
        }

        mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(filename.endsWith(".jpg")||filename.endsWith("jpeg")||filename.endsWith("png")||filename.endsWith("gif")) {
                    return true;
                }
                return false;
            }
        }));

        mImageAdapter = new ImageAdapter(SelectImageActivity.this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImageAdapter);

        mDirImgCount.setText(""+mMaxCount);
        mDirName.setText(mCurrentDir.getName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化布局View
        initView();

        initDatas();

        initEvents();


    }

    private void initEvents() {

        mBottomLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.setAnimationStyle(R.style.anim_dir_popwindow);
                mPopupWindow.showAsDropDown(mBottomLY, 0, 0);
                lightOff();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedImgs = convertSetToArrayList(mImageAdapter.getSelectedImgs());
                Intent intent = new Intent(SelectImageActivity.this,FeedbackActivity.class);
                intent.putExtra(FeedbackActivity.ARG_SELECTED_IMAGES,selectedImgs);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private ArrayList<String> convertSetToArrayList(Set<String> imgs) {
        ArrayList<String> selectedList = new ArrayList<>();
        for(String str : imgs) {
            selectedList.add(str);
        }

        return selectedList;
    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化数据
     * 利用ContentProvider扫描手机中的image
     */
    private void initDatas() {
        //判断手机存储卡是否可用
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(SelectImageActivity.this,"当前存储卡不可用",Toast.LENGTH_LONG);
            return;
        }

        mProgressDialog = ProgressDialog.show(this,null,"正在加载中。。。");

        new Thread(){
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver resolver = SelectImageActivity.this.getContentResolver();
                Cursor cursor = resolver.query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + " =? or "
                                + MediaStore.Images.Media.MIME_TYPE + " =?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                //扫描过的文件夹的绝对路径集合
                Set<String> scanedFolders = new HashSet<>();

                while (cursor.moveToNext()) {
                    //获取Image的path
                    String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //得到image文件所在的目录
                    File parentFile = new File(imgPath).getParentFile();
                    if(null == parentFile) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = new FolderBean();
                    folderBean.setPath(dirPath);
                    folderBean.setFirstImgPath(imgPath);

                    //如果已经扫描过该文件夹，则路过扫描
                    if(scanedFolders.contains(dirPath)) {
                        continue;
                    }

                    if(parentFile.listFiles() == null) {
                        continue;
                    }
                    //list parentFile中的image文件数
                    int imgCount = parentFile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")||filename.endsWith("jpeg")||filename.endsWith("png")||filename.endsWith("gif")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;

                    scanedFolders.add(dirPath);

                    folderBean.setImgCount(imgCount);
                    mFolderBeans.add(folderBean);

                    if(imgCount > mMaxCount) {
                        mMaxCount = imgCount;
                        mCurrentDir = parentFile;
                    }

                }

                cursor.close();

                //通知Handler扫描图片完成
                mUIHandler.sendEmptyMessage(SCAN_COMPLETED);
            }
        }.start();
    }

    /**
     * 初始化布局View
     */
    private void initView() {
        this.mGridView = (GridView) findViewById(R.id.id_grid);
        this.mBottomLY = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        this.mDirName = (TextView) findViewById(R.id.id_folder_name);
        this.mDirImgCount = (TextView) findViewById(R.id.id_image_count);
        this.checkBtn = (Button) findViewById(R.id.id_result_btn);
    }
}
