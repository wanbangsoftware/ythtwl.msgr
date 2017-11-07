package com.hlk.ythtwl.msgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.notification.Msgr;

/**
 * <b>功能描述：</b>地图重现位置<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/07 10:42 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/07 10:42 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class MapActivity extends BaseActivity {

    private static final String PARAM_MSGR = "map_param_msgr";

    public static void open(Context context, Msgr msgr) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(PARAM_MSGR, (Parcelable) msgr);
        context.startActivity(intent);
    }

    @ViewId(R.id.ui_map_map_view)
    private MapView mapView;
    private AMap aMap;
    @ViewId(R.id.ui_map_center_pointer)
    private View pointView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ViewUtility.bind(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == aMap) {
            aMap = mapView.getMap();
            // 重现定位，不需要拖动地图
            aMap.getUiSettings().setScrollGesturesEnabled(false);
            reducePosition();
        }
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        aMap = null;
        mapView.onDestroy();
        super.onDestroy();
    }

    private void reducePosition() {
        Intent intent = getIntent();
        final Msgr msgr = intent.getParcelableExtra(PARAM_MSGR);
        if (null != msgr) {
            mapView.post(new Runnable() {
                @Override
                public void run() {
                    LatLng pos = new LatLng(msgr.getLatitude(), msgr.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 15f);
                    aMap.animateCamera(update, 300, new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            //mAMap.animateCamera(CameraUpdateFactory.zoomTo(dftZoomLevel), duration(), null);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            });
        }
    }
}
