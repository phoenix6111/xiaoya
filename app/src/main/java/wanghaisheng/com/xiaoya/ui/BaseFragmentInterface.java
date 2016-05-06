package wanghaisheng.com.xiaoya.ui;

import android.view.View;

public interface BaseFragmentInterface {

	public void beforeInitView(View view);

	public void initView(View view);
	
	public void initData();
}
