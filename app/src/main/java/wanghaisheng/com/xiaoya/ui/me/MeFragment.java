package wanghaisheng.com.xiaoya.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.collection.BaseCollectionActivity;
import wanghaisheng.com.xiaoya.ui.other.BrowserActivity;
import wanghaisheng.com.xiaoya.ui.setting.SettingActivity;

/**
 * Created by sheng on 2016/4/19.
 */
public class MeFragment extends Fragment {
    private static final String TAG = "MeFragment";

    @Bind(R.id.collect_card)
    CardView collectCard;
    @Bind(R.id.feedback)
    CardView feedbackCard;
    @Bind(R.id.setup_card)
    CardView setupCard;
    @Bind(R.id.about_card)
    CardView aboutCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment_layout, container, false);


        ButterKnife.bind(this, view);
        return view;
    }

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.collect_card, R.id.feedback, R.id.setup_card, R.id.about_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collect_card:
                Intent collectionIntent = new Intent(getActivity(),BaseCollectionActivity.class);
                startActivity(collectionIntent);
                break;
            case R.id.feedback:
                Intent feedbackIntent = new Intent(getActivity(),FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.setup_card:
                LogUtils.v(TAG,"collection card clicked.............");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.about_card:
                BrowserActivity.startActivity(getActivity(),"http://justgogo.biz/xiaoya/about");
                break;
        }
    }
}
