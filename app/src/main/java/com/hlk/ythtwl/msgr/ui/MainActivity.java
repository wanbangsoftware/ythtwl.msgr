package com.hlk.ythtwl.msgr.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import com.hlk.ythtwl.msgr.helper.ToastHelper;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.holderview.common.NothingMoreViewHolder;
import com.hlk.ythtwl.msgr.holderview.listener.OnViewHolderElementClickListener;
import com.hlk.ythtwl.msgr.holderview.main.AlarmViewHolder;
import com.hlk.ythtwl.msgr.holderview.main.TruckViewHolder;
import com.hlk.ythtwl.msgr.listener.OnMsgrEventListener;
import com.hlk.ythtwl.msgr.listener.OnRecyclerItemClickListener;
import com.hlk.ythtwl.msgr.model.Model;
import com.hlk.ythtwl.msgr.model.Truck;
import com.hlk.ythtwl.msgr.notification.Msgr;
import com.hlk.ythtwl.msgr.permission.MPermission;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionDenied;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionGranted;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionNeverAskAgain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String EXTRA_NOTIFICATION = "ythtwl.extra.notification";
    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;

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
                int index = mAdapter.indexOf(msgr);
                if (index >= 0) {
                    mAdapter.update(msgr);
                } else {
                    mAdapter.add(msgr, mAdapter.getItemCount() - 1);
                }
                smoothScrollToBottom(recyclerView, mAdapter.getItemCount() - 1);
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
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                showPopupDialog(viewHolder.getAdapterPosition());
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Snackbar展示，右边可以点击哦", Snackbar.LENGTH_LONG)
                        .setAction("点击", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastHelper.make().showMsg("我被点了");
                            }
                        }).show();
            }
        });
        requestBasicPermission();
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        SnackbarHelper.make(recyclerView).show(R.string.activity_main_permission_not_grant_complete);
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
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

    private void initializeAdapter() {
        if (null == mAdapter) {
            mAdapter = new LicenseAdapter();
            recyclerView.setAdapter(mAdapter);
            List<Msgr> msgrs = Msgr.query();
            if (null != msgrs) {
                for (Msgr msgr : msgrs) {
                    Truck truck = new Truck();
                    truck.setLicense(msgr.getLicense());
                    int index = mAdapter.indexOf(truck);
                    if (index >= 0) {
                        truck = (Truck) mAdapter.get(index);
                        truck.setCount(truck.getCount() + 1);
                        truck.setUnread(truck.getUnread() + (msgr.isUnread() ? 1 : 0));
                        truck.setLastTime(msgr.getId());
                        mAdapter.notifyItemChanged(index);
                    } else {
                        index = mAdapter.indexOfLicense(msgr.getLicense());
                        if (index >= 0) {
                            truck.setCount(2);
                            truck.setUnread(truck.getUnread() + (msgr.isUnread() ? 1 : 0));
                            truck.setLastTime(msgr.getId());
                            mAdapter.replace(index, truck);
                        } else {
                            mAdapter.update(msgr);
                        }
                    }
                }
            }
            mAdapter.update(nothingMore);
        }
    }

    private OnViewHolderElementClickListener elementClickListener = new OnViewHolderElementClickListener() {
        @Override
        public void onClick(View view, int index) {
            switch (view.getId()) {
                case R.id.ui_holder_view_truck_layout:
                    TruckActivity.open(MainActivity.this, (Truck) mAdapter.get(index));
                    break;
                case R.id.ui_holder_view_alarm_layout:
                    MapActivity.open(MainActivity.this, (Msgr) mAdapter.get(index));
                    break;
            }
        }
    };

    private class LicenseAdapter extends RecyclerViewAdapter<BaseViewHolder, Model> {

        static final int VT_TRUCK = 0, VT_ALARM = 1, VT_NOMORE = 2;

        @Override
        public BaseViewHolder onCreateViewHolder(View itemView, int viewType) {
            switch (viewType) {
                case VT_TRUCK:
                    TruckViewHolder tvh = new TruckViewHolder(itemView, MainActivity.this);
                    tvh.setOnViewHolderElementClickListener(elementClickListener);
                    return tvh;
                case VT_ALARM:
                    AlarmViewHolder avh = new AlarmViewHolder(itemView, MainActivity.this);
                    avh.setOnViewHolderElementClickListener(elementClickListener);
                    avh.setShowCautionIcon(false);
                    return avh;
            }
            return new NothingMoreViewHolder(itemView, MainActivity.this);
        }

        @Override
        public int itemLayout(int viewType) {
            switch (viewType) {
                case VT_NOMORE:
                    return R.layout.holder_view_nothing_more;
                case VT_TRUCK:
                    return R.layout.holder_view_truck_item;
            }
            return R.layout.holder_view_alarm_item;
        }

        @Override
        public int getItemViewType(int position) {
            Model model = get(position);
            if (model instanceof Msgr) {
                return VT_ALARM;
            } else if (model instanceof Truck) {
                return VT_TRUCK;
            }
            return VT_NOMORE;
        }

        @Override
        public void onBindHolderOfView(BaseViewHolder holder, int position, @Nullable Model item) {
            if (holder instanceof AlarmViewHolder) {
                ((AlarmViewHolder) holder).showContent((Msgr) item);
            } else if (holder instanceof TruckViewHolder) {
                ((TruckViewHolder) holder).showContent((Truck) item);
            }
        }

        int indexOfLicense(String license) {
            Iterator<Model> iterable = iterator();
            int index = 0;
            while (iterable.hasNext()) {
                Model model = iterable.next();
                if (model instanceof Msgr) {
                    Msgr msgr = (Msgr) model;
                    if (msgr.getLicense().equals(license)) {
                        return index;
                    }
                }
                index++;
            }
            return -1;
        }

        @Override
        protected int comparator(Model item1, Model item2) {
            return 0;
        }
    }
}
