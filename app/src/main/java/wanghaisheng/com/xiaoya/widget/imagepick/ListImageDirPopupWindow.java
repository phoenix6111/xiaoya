package wanghaisheng.com.xiaoya.widget.imagepick;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.widget.imagepick.beans.FolderBean;
import wanghaisheng.com.xiaoya.widget.imagepick.utils.ImageLoader;

/**
 * Created by sheng on 2016/1/2.
 */
public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;

    private List<FolderBean> mDatas;

    private View mConvertView;
    private ListView mListView;

    private DirSelectedListener mListener;

    public void setOnDirSelectedListener(DirSelectedListener listener) {
        this.mListener = listener;
    }

    public interface DirSelectedListener {
        void dirSelected(FolderBean folderBean);
    }

    public ListImageDirPopupWindow(Context context,List<FolderBean> mDatas) {
        calcWidthAndHeight(context);

        this.mDatas = mDatas;
        this.mConvertView = LayoutInflater.from(context).inflate(R.layout.select_img_popup_main,null);

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //如果在PopupWindow外面点击，则让它消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }

                return false;
            }
        });


        initViews(context);

        initEvents();
    }

    private void initEvents() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != mListener) {
                    mListener.dirSelected(mDatas.get(position));
                }
            }
        });
    }

    /**
     * 初始化视图数据
     */
    private void initViews(Context context) {
        mListView = (ListView) this.mConvertView.findViewById(R.id.id_popup);
        mListView.setAdapter(new ListImageDirAdapter(context,mDatas));

    }

    private class ListImageDirAdapter extends ArrayAdapter<FolderBean> {
        private LayoutInflater mInflater;
        private List<FolderBean> mFolderBean;

        public ListImageDirAdapter(Context context, List<FolderBean> folderBeans) {
            super(context, 0, folderBeans);
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(null == convertView) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.select_img_popup_item,parent,false);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_popup_img_item);
                viewHolder.dirName = (TextView) convertView.findViewById(R.id.id_popup_item_dirname);
                viewHolder.dirImgCount = (TextView) convertView.findViewById(R.id.id_popup_item_dirimgcount);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FolderBean folderBean = getItem(position);
            //重置
            viewHolder.imageView.setImageResource(R.drawable.pictures_no);
            ImageLoader.getInstance().loadImage(folderBean.getFirstImgPath(),viewHolder.imageView);

            viewHolder.dirName.setText(folderBean.getName());
            viewHolder.dirImgCount.setText(folderBean.getImgCount()+"");


            return convertView;
        }

        private class ViewHolder{
            ImageView imageView;
            TextView dirName;
            TextView dirImgCount;
        }
    }



    /**
     * 计算PopupWindow的宽度和高度
     * @param context
     */
    private void calcWidthAndHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = (int) (metrics.heightPixels * 0.7);
    }
}
