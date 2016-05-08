package wanghaisheng.com.xiaoya.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.empty.EmptyLayout;
import wanghaisheng.com.xiaoya.utils.ShareHelper;

/**
 * Created by sheng on 2016/5/7.
 */
public abstract class BaseSimpleActivity extends BaseActivity implements BaseSimpleView{
    public static final int ERROR_TYPE_NETWORK = 1;
    public static final int ERROR_TYPE_NODATA = 2;
    public static final int ERROR_TYPE_NODATA_ENABLE_CLICK = 3;
    public static final int ERROR_TYPE_UNKNOWN = 4;

    @Bind(R.id.empty_layout)
    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息

    @Inject
    ShareHelper shareHelper;
    protected boolean isCollected;

    protected MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityComponent.inject(this);

        emptyLayout = (EmptyLayout) findViewById(R.id.empty_layout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadClick();
            }
        });
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            error(ERROR_TYPE_NETWORK,null);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void hideLoading() {
        emptyLayout.dismiss();
    }

    @Override
    public void showLoading() {
        emptyLayout.setNetworkLoading();
    }

    @Override
    public void error(int errorType,String errMsg) {
        switch (errorType) {
            case ERROR_TYPE_NETWORK:
                emptyLayout.setNetworkError();
                break;
            case ERROR_TYPE_NODATA:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
            case ERROR_TYPE_NODATA_ENABLE_CLICK:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodataEnableClick();
                break;
            default:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        mMenuItem = menu.findItem(R.id.menu_collect);
        updateCollectionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    protected void updateCollectionsMenu() {
        if(isCollected){
            mMenuItem.setIcon(R.mipmap.star_focus);
        }else {
            mMenuItem.setIcon(R.mipmap.start_black);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_share) {
            shareHelper.share(this,"分享到朋友圈");
        } else {
            //presenter.collectStory(story.getId());
            collect();
        }

        return true;
    }

    public abstract void collect();

    public void collectSuccess() {
        isCollected = true;
        updateCollectionsMenu();
    }

    public abstract void onReloadClick();

    public void uncollectSuccess() {
        isCollected = false;
        updateCollectionsMenu();
    }

    public void updateCollectionFlag(boolean ifCollected) {
        this.isCollected = ifCollected;
        updateCollectionsMenu();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


}
