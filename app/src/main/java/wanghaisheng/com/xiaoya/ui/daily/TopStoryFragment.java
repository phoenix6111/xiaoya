package wanghaisheng.com.xiaoya.ui.daily;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.di.component.DaggerFragmentComponent;
import wanghaisheng.com.xiaoya.di.component.FragmentComponent;
import wanghaisheng.com.xiaoya.di.module.FragmentModule;
import wanghaisheng.com.xiaoya.utils.ImageUtil;

/**
 * Created by sheng on 2016/4/12.
 */
public class TopStoryFragment extends Fragment {
    protected FragmentComponent mFragmentComponent;
    private static final String TAG = "TopStoryFragment";
    public static final String ARG_STORY = "arg_story";
    private Story story;
    private ImageView imageView;

    @Inject
    ImageUtil imageUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .appComponent(((AppContext)getActivity().getApplication()).getAppComponent())
                .build();
        mFragmentComponent.inject(this);
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.story = bundle.getParcelable(ARG_STORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.daily_top_layout,container,false);
        imageView = (ImageView) parentView.findViewById(R.id.iv_top_pic);

        //imageUtil.loadImage(getActivity(),story.getImage(),imageView);
        //Glide.with(getActivity()).load(story.getImage()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StoryDetailActivity.class);
                intent.putExtra("story",story);
                startActivity(intent);
            }
        });
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     *  传入需要的参数，设置给arguments
     *  给Fragment添加newInstance方法，将需要的参数传入，设置到bundle中，然后setArguments(bundle)，最后在onCreate中进行获取；
     *  这样就完成了Fragment和Activity间的解耦。当然了这里需要注意：
     *  setArguments方法必须在fragment创建以后，添加给Activity前完成。千万不要，首先调用了add，然后设置arguments。
     * @param story
     * @return
     */
    public static TopStoryFragment newInstance(Story story) {
        TopStoryFragment fragment = new TopStoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_STORY,story);
        fragment.setArguments(bundle);
        return fragment;
    }
}

