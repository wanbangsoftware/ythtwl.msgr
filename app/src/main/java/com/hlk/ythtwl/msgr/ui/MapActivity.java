package com.hlk.ythtwl.msgr.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.helper.SnackbarHelper;
import com.hlk.ythtwl.msgr.helper.ToastHelper;
import com.hlk.ythtwl.msgr.notification.Msgr;
import com.hlk.ythtwl.msgr.permission.MPermission;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionDenied;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionGranted;
import com.hlk.ythtwl.msgr.permission.annotation.OnMPermissionNeverAskAgain;

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

    private static int CALL_TYPE = 1;
    private final int PHONE_PERMISSION_REQUEST_CODE = 100;

    public static void open(Context context, Msgr msgr) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(PARAM_MSGR, (Parcelable) msgr);
        context.startActivity(intent);
    }

    @ViewId(R.id.toolbar)
    private Toolbar toolbar;
    @ViewId(R.id.ui_map_map_view)
    private MapView mapView;
    @ViewId(R.id.ui_map_address)
    private TextView addressView;
    private AMap aMap;
    @ViewId(R.id.ui_map_center_pointer)
    private View pointView;

    @ViewId(R.id.ui_map_stop_points)
    private View stopPoints;
    @ViewId(R.id.ui_map_stop_points_title)
    private TextView stopPointTitle;
    @ViewId(R.id.ui_map_driver1)
    private View driver1;
    @ViewId(R.id.ui_map_driver1_name)
    private TextView driver1Name;
    @ViewId(R.id.ui_map_driver1_phone)
    private TextView driver1Phone;
    @ViewId(R.id.ui_map_driver2)
    private View driver2;
    @ViewId(R.id.ui_map_driver2_name)
    private TextView driver2Name;
    @ViewId(R.id.ui_map_driver2_phone)
    private TextView driver2Phone;

    private Msgr mMsgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ViewUtility.bind(this);
        setSupportActionBar(toolbar);
        mapView.onCreate(savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    @Click({R.id.ui_map_stop_points, R.id.ui_map_call_phone1, R.id.ui_map_call_phone2})
    private void elementClick(View view) {
        if (view.getId() == R.id.ui_map_stop_points) {
            if (mMsgr.getPoints().size() <= 0) {
                ToastHelper.make().showMsg(R.string.ui_map_stop_points_empty);
            } else {
                PointsActivity.open(this, mMsgr);
            }
        } else {
            CALL_TYPE = view.getId() == R.id.ui_map_call_phone1 ? 1 : 2;
            requestPhoneCallPermission();
        }
    }

    /**
     * 拨打电话权限管理
     */
    private final String[] PHONE_CALL_PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE
    };

    private void requestPhoneCallPermission() {
        MPermission.printMPermissionResult(true, this, PHONE_CALL_PERMISSIONS);
        MPermission.with(this)
                .setRequestCode(PHONE_PERMISSION_REQUEST_CODE)
                .permissions(PHONE_CALL_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(PHONE_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        MPermission.printMPermissionResult(false, this, PHONE_CALL_PERMISSIONS);
        Msgr msgr = getIntent().getParcelableExtra(PARAM_MSGR);
        if (null != msgr) {
            dialPhone(CALL_TYPE == 1 ? msgr.getPhone1() : msgr.getPhone2());
        }
    }

    @OnMPermissionDenied(PHONE_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(PHONE_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        SnackbarHelper.make(driver1).setText(R.string.activity_main_permission_not_grant_complete).show();
        MPermission.printMPermissionResult(false, this, PHONE_CALL_PERMISSIONS);
    }

    private void reducePosition() {
        Intent intent = getIntent();
        mMsgr = intent.getParcelableExtra(PARAM_MSGR);
        if (null != mMsgr) {
            stopPoints.setVisibility(mMsgr.getPoints().size() > 0 ? View.VISIBLE : View.GONE);
            stopPointTitle.setText(getString(R.string.ui_map_stop_points_title, mMsgr.getPoints().size()));
            toolbar.setTitle(getString(R.string.activity_title_map1, mMsgr.getLicense()));
            driver1.setVisibility(isEmpty(mMsgr.getName1()) ? View.GONE : View.VISIBLE);
            driver1Name.setText(mMsgr.getName1());
            driver1Phone.setText(mMsgr.getPhone1());
            driver2.setVisibility(isEmpty(mMsgr.getName2()) ? View.GONE : View.VISIBLE);
            driver2Name.setText(mMsgr.getName2());
            driver2Phone.setText(mMsgr.getPhone2());
            mapView.post(new Runnable() {
                @Override
                public void run() {
                    final LatLng pos = new LatLng(mMsgr.getLatitude(), mMsgr.getLongitude());
                    //CoordinateConverter converter = new CoordinateConverter();
                    // CoordType.GPS 待转换坐标类型
                    //converter.from(CoordinateConverter.CoordType.GPS);
                    // sourceLatLng待转换坐标点 LatLng类型
                    //converter.coord(pos);
                    // 执行转换操作
                    //final LatLng des = converter.convert();
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(pos, 15f);
                    aMap.animateCamera(update, 300, new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            //mAMap.animateCamera(CameraUpdateFactory.zoomTo(dftZoomLevel), duration(), null);
                            tryReverseGeoCode(new LatLonPoint(pos.latitude, pos.longitude));
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            });
        }
    }

    //***********************************反转地址服务
    // 地址反转服务
    protected GeocodeSearch mGeoCoder;

    /**
     * 反转编码地理位置
     */
    protected void tryReverseGeoCode(LatLonPoint location) {
        if (null == mGeoCoder) {
            // 创建GeoCoder实例对象
            mGeoCoder = new GeocodeSearch(this);
            // 设置查询结果监听者
            mGeoCoder.setOnGeocodeSearchListener(onGeocodeSearchListener);
        }
        // 发起反地理编码请求(经纬度->地址信息)
        RegeocodeQuery query = new RegeocodeQuery(location, 200, GeocodeSearch.AMAP);
        mGeoCoder.getFromLocationAsyn(query);
    }

    private GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            addressView.setText(getString(R.string.ui_map_address_view_text, regeocodeResult.getRegeocodeAddress().getFormatAddress()));
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

}
