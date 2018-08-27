package com.hlk.ythtwl.msgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.adapter.RecyclerViewAdapter;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.holderview.common.NothingMoreViewHolder;
import com.hlk.ythtwl.msgr.holderview.listener.OnViewHolderElementClickListener;
import com.hlk.ythtwl.msgr.holderview.stopping.StopPointViewHolder;
import com.hlk.ythtwl.msgr.model.Model;
import com.hlk.ythtwl.msgr.model.Point;
import com.hlk.ythtwl.msgr.notification.Msgr;

/**
 * <b>功能描述：</b>车辆的停车点<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2018/08/25 12:58 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/10/04 18:50 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class PointsActivity extends BaseActivity {

    public static void open(Context context, Msgr msgr) {
        Intent intent = new Intent(context, PointsActivity.class);
        intent.putExtra(PARAM_MSGR, (Parcelable) msgr);
        context.startActivity(intent);
    }

    @ViewId(R.id.toolbar)
    private Toolbar toolbar;
    @ViewId(R.id.recyclerView)
    private RecyclerView recyclerView;
    private PointAdapter mAdapter;
    private Model nothingMore = Model.getNothingMore();
    private Msgr mMsgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtility.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAdapter();
    }

    private void initializeAdapter() {
        if (null == mAdapter) {
            mAdapter = new PointAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mAdapter);
            Intent intent = getIntent();
            mMsgr = intent.getParcelableExtra(PARAM_MSGR);
            if (null != mMsgr) {
                toolbar.setTitle(getString(R.string.activity_title_map1, mMsgr.getLicense()));
                for (Point point : mMsgr.getPoints()) {
                    if (0L == point.getId()) {
                        point.setId(point.getStop());
                    }
                    mAdapter.add(point);
                }
            }
            mAdapter.update(nothingMore);
        }
    }

    private OnViewHolderElementClickListener elementClickListener = new OnViewHolderElementClickListener() {
        @Override
        public void onClick(View view, int index) {
            Point point = (Point) mAdapter.get(index);
            Msgr msgr = new Msgr();
            msgr.setLicense(mMsgr.getLicense());
            msgr.setLatitude(point.getLat());
            msgr.setLongitude(point.getLng());
            MapActivity.open(PointsActivity.this, msgr);
        }
    };

    private class PointAdapter extends RecyclerViewAdapter<BaseViewHolder, Model> {

        private static final int VT_POINT = 0, VT_LAST = 1;

        @Override
        public BaseViewHolder onCreateViewHolder(View itemView, int viewType) {
            if (viewType == VT_POINT) {
                StopPointViewHolder spvh = new StopPointViewHolder(itemView, PointsActivity.this);
                spvh.setOnViewHolderElementClickListener(elementClickListener);
                return spvh;
            }
            return new NothingMoreViewHolder(itemView, PointsActivity.this);
        }

        @Override
        public int itemLayout(int viewType) {
            return viewType == VT_POINT ? R.layout.holder_view_stop_point : R.layout.holder_view_nothing_more;
        }

        @Override
        public int getItemViewType(int position) {
            return get(position) instanceof Point ? VT_POINT : VT_LAST;
        }

        @Override
        public void onBindHolderOfView(BaseViewHolder holder, int position, @Nullable Model item) {
            if (holder instanceof StopPointViewHolder) {
                ((StopPointViewHolder) holder).showContent((Point) item);
            }
        }

        @Override
        protected int comparator(Model item1, Model item2) {
            return 0;
        }
    }
}
