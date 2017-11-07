package com.hlk.ythtwl.msgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hlk.hlklib.layoutmanager.CustomLinearLayoutManager;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.adapter.RecyclerViewAdapter;
import com.hlk.ythtwl.msgr.application.App;
import com.hlk.ythtwl.msgr.helper.SnackbarHelper;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.holderview.common.NothingMoreViewHolder;
import com.hlk.ythtwl.msgr.holderview.listener.OnViewHolderElementClickListener;
import com.hlk.ythtwl.msgr.holderview.main.TruckViewHolder;
import com.hlk.ythtwl.msgr.listener.OnMsgrEventListener;
import com.hlk.ythtwl.msgr.model.Model;
import com.hlk.ythtwl.msgr.notification.Msgr;

import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_NOTIFICATION = "ythtwl.extra.notification";

    public static Intent getIntent(Context context, Intent extras) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (null != extras) {
            intent.putExtras(extras);
        }
        return intent;
    }

    private OnMsgrEventListener msgrEventListener = new OnMsgrEventListener() {
        @Override
        public void onEvent(Msgr msgr) {
            if (null != mAdapter) {
                mAdapter.add(msgr, mAdapter.getItemCount() - 1);
            }
        }
    };

    @ViewId(R.id.toolbar)
    private Toolbar toolbar;
    @ViewId(R.id.recyclerView)
    private RecyclerView recyclerView;
    private LicenseAdapter mAdapter;
    private Model nothingMore = Model.getNothingMore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtility.bind(this);
        setSupportActionBar(toolbar);
        App.addMsgrEventListener(msgrEventListener);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnackbarHelper.make(view).show("Replace with your own action");
                //.setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            App.app().pressAgainExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        App.removeMsgrEventListener(msgrEventListener);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.app().setAppStayInBackground(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeAdapter();
    }

    @Override
    protected void onStop() {
        App.app().setAppStayInBackground(true);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingActivity.open(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeAdapter() {
        if (null == mAdapter) {
            mAdapter = new LicenseAdapter();
            recyclerView.setAdapter(mAdapter);
            List<Msgr> msgrs = Msgr.query();
            if (null != msgrs) {
                for (Msgr msgr : msgrs) {
                    mAdapter.update(msgr);
                }
            }
            mAdapter.update(nothingMore);
        }
    }

    private OnViewHolderElementClickListener elementClickListener = new OnViewHolderElementClickListener() {
        @Override
        public void onClick(View view, int index) {

        }
    };

    private class LicenseAdapter extends RecyclerViewAdapter<BaseViewHolder, Model> {

        static final int VT_TRUCK = 0, VT_NOMORE = 1;

        @Override
        public BaseViewHolder onCreateViewHolder(View itemView, int viewType) {
            switch (viewType) {
                case VT_TRUCK:
                    TruckViewHolder tvh = new TruckViewHolder(itemView, MainActivity.this);
                    tvh.setOnViewHolderElementClickListener(elementClickListener);
                    return tvh;
            }
            return new NothingMoreViewHolder(itemView, MainActivity.this);
        }

        @Override
        public int itemLayout(int viewType) {
            switch (viewType) {
                case VT_NOMORE:
                    return R.layout.holder_view_nothing_more;
            }
            return R.layout.holder_view_track_item;
        }

        @Override
        public int getItemViewType(int position) {
            if (get(position) instanceof Msgr) {
                return VT_TRUCK;
            }
            return VT_NOMORE;
        }

        @Override
        public void onBindHolderOfView(BaseViewHolder holder, int position, @Nullable Model item) {
            if (holder instanceof TruckViewHolder) {
                ((TruckViewHolder) holder).showContent((Msgr) item);
            }
        }

        @Override
        protected int comparator(Model item1, Model item2) {
            return 0;
        }
    }
}
