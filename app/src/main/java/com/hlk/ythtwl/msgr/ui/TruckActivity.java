package com.hlk.ythtwl.msgr.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hlk.hlklib.layoutmanager.CustomLinearLayoutManager;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.adapter.RecyclerViewAdapter;
import com.hlk.ythtwl.msgr.application.App;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.holderview.common.NothingMoreViewHolder;
import com.hlk.ythtwl.msgr.holderview.listener.OnViewHolderElementClickListener;
import com.hlk.ythtwl.msgr.holderview.main.AlarmViewHolder;
import com.hlk.ythtwl.msgr.listener.OnMsgrEventListener;
import com.hlk.ythtwl.msgr.listener.OnRecyclerItemClickListener;
import com.hlk.ythtwl.msgr.model.Model;
import com.hlk.ythtwl.msgr.model.Truck;
import com.hlk.ythtwl.msgr.notification.Msgr;

import java.util.List;

/**
 * <b>功能描述：</b>单台车辆的报警信息列表<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/19 18:52 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/19 18:52 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class TruckActivity extends BaseActivity {

    private static final String PARAM_TRUCK = "ta_param_truck";
    private static String license = "";

    public static void open(Context context, Truck truck) {
        Intent intent = new Intent(context, TruckActivity.class);
        intent.putExtra(PARAM_TRUCK, truck);
        context.startActivity(intent);
    }

    private OnMsgrEventListener msgrEventListener = new OnMsgrEventListener() {
        @Override
        public void onEvent(Msgr msgr) {
            if (null != msgr && !isEmpty(msgr.getLicense()) && msgr.getLicense().equals(license)) {
                if (null != mAdapter) {
                    int index = mAdapter.indexOf(msgr);
                    if (index >= 0) {
                        mAdapter.update(msgr);
                    } else {
                        mAdapter.add(msgr, mAdapter.getItemCount() - 1);
                    }
                    smoothScrollToBottom(recyclerView, mAdapter.getItemCount() - 1);
                }
            }
        }
    };

    @ViewId(R.id.toolbar)
    private Toolbar toolbar;
    @ViewId(R.id.recyclerView)
    private RecyclerView recyclerView;
    private AlarmAdapter mAdapter;
    private Model nothingMore = Model.getNothingMore();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtility.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        App.addMsgrEventListener(msgrEventListener);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                showPopupDialog(viewHolder.getAdapterPosition());
            }
        });
        parseIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAdapter();
    }

    @Override
    protected void onDestroy() {
        App.removeMsgrEventListener(msgrEventListener);
        super.onDestroy();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        Truck truck = intent.getParcelableExtra(PARAM_TRUCK);
        if (null != truck) {
            license = truck.getLicense();
            toolbar.setTitle(truck.getLicense());
        }
    }

    private void showPopupDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.ui_menu_recycler_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        // 删除单个
                        Msgr.delete(mAdapter.get(index).getId());
                        mAdapter.remove(index);
                        break;
                    case 1:
                        break;
                }
            }
        });
        builder.show();
    }

    private OnViewHolderElementClickListener elementClickListener = new OnViewHolderElementClickListener() {
        @Override
        public void onClick(View view, int index) {
            MapActivity.open(TruckActivity.this, (Msgr) mAdapter.get(index));
        }
    };

    private void initializeAdapter() {
        if (null == mAdapter) {
            mAdapter = new AlarmAdapter();
            recyclerView.setAdapter(mAdapter);
            List<Msgr> msgrs = Msgr.query(license);
            if (null != msgrs) {
                for (Msgr msgr : msgrs) {
                    mAdapter.update(msgr);
                }
            }
            mAdapter.update(nothingMore);
        }
    }

    private class AlarmAdapter extends RecyclerViewAdapter<BaseViewHolder, Model> {

        private static final int VT_ALARM = 0, VT_ENDER = 1;

        @Override
        public BaseViewHolder onCreateViewHolder(View itemView, int viewType) {
            switch (viewType) {
                case VT_ALARM:
                    AlarmViewHolder avh = new AlarmViewHolder(itemView, TruckActivity.this);
                    avh.setShowCautionIcon(true);
                    avh.setOnViewHolderElementClickListener(elementClickListener);
                    return avh;
            }
            return new NothingMoreViewHolder(itemView, TruckActivity.this);
        }

        @Override
        public int itemLayout(int viewType) {
            switch (viewType) {
                case VT_ENDER:
                    return R.layout.holder_view_nothing_more;
            }
            return R.layout.holder_view_alarm_item;
        }

        @Override
        public int getItemViewType(int position) {
            if (get(position) instanceof Msgr) {
                return VT_ALARM;
            }
            return VT_ENDER;
        }

        @Override
        public void onBindHolderOfView(BaseViewHolder holder, int position, @Nullable Model item) {
            if (holder instanceof AlarmViewHolder) {
                ((AlarmViewHolder) holder).showContent((Msgr) item);
            }
        }

        @Override
        protected int comparator(Model item1, Model item2) {
            return 0;
        }
    }
}
