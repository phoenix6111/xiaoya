package wanghaisheng.com.xiaoya.datasource;

import android.support.annotation.IntDef;

import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.R;

/**
 * Created by sheng on 2016/5/5.
 */
public class Data {
    protected static final int DATA_SOURCE_MEMORY = 1;
    protected static final int DATA_SOURCE_DISK = 2;
    protected static final int DATA_SOURCE_NETWORK = 3;
    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK}) @interface DataSource{}

    protected int dataSource;

    protected void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return AppContext.getInstance().getString(dataSourceTextRes);
    }
}
