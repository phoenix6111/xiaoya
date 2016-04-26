package wanghaisheng.com.xiaoya.widget.imagepick;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.widget.imagepick.utils.DispType;
import wanghaisheng.com.xiaoya.widget.imagepick.utils.ImageLoader;


/**
 * Created by sheng on 2016/1/2.
 */
public class ImageAdapter extends BaseAdapter {
    //图片的名称
    private List<String> mImgNames;
    //图片所在的路径
    private String mImgDir;
    private LayoutInflater mInflater;

    //选中状态的Images
    private Set<String> mSelectedImgs = new HashSet<>();

    /**
     * 获取选中的图片
     * @return
     */
    public Set<String> getSelectedImgs() {
        return mSelectedImgs;
    }

    public ImageAdapter(Context context,List<String > imgNames,String imgDir) {
        this.mImgNames = imgNames;
        this.mImgDir = imgDir;

        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.select_img_grid_item,parent,false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_img_grid_item);
            ImageView checkBtn = (ImageView) convertView.findViewById(R.id.id_check_btn);
            viewHolder.imageView = imageView;
            viewHolder.checkBtn = checkBtn;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //重置图片的状态
        viewHolder.imageView.setImageResource(R.drawable.pictures_no);
        viewHolder.checkBtn.setImageResource(R.drawable.picture_unselected);
        viewHolder.imageView.setColorFilter(null);

        //加载图片并在GridView中的ImageView中显示
        ImageLoader.getInstance(3, DispType.LIFO).loadImage(mImgDir + "/" + mImgNames.get(position), viewHolder.imageView);
        final String absImgPath = mImgDir+"/"+mImgNames.get(position);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是选中状态，则取消选择
                if(mSelectedImgs.contains(absImgPath)) {
                    mSelectedImgs.remove(absImgPath);
                    viewHolder.imageView.setColorFilter(null);
                    viewHolder.checkBtn.setImageResource(R.drawable.picture_unselected);
                } else {//如果是未选中状态
                    mSelectedImgs.add(absImgPath);
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.checkBtn.setImageResource(R.drawable.pictures_selected);
                }

                //notifyDataSetChanged();
            }
        });

        if(mSelectedImgs.contains(absImgPath)) {
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.checkBtn.setImageResource(R.drawable.pictures_selected);
        }


        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        ImageView checkBtn;
    }
}
