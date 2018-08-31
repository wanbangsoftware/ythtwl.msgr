package com.hlk.ythtwl.msgr.helper;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hlk.ythtwl.msgr.R;

import java.lang.ref.SoftReference;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2018/08/31 16:11 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2018/08/31 16:11  <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class GeocodeHelper {

    public static GeocodeHelper helper(Context context) {
        return new GeocodeHelper(context);
    }

    private SoftReference<Context> contextReference;

    private GeocodeHelper(Context context) {
        contextReference = new SoftReference<>(context);
    }

    //***********************************反转地址服务
    // 地址反转服务
    private GeocodeSearch mGeoCoder;

    public void tryReverseGeoCode(LatLonPoint location) {
        if (null == mGeoCoder) {
            // 创建GeoCoder实例对象
            mGeoCoder = new GeocodeSearch(contextReference.get());
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
            if (null != listener) {
                listener.onSearched(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    private OnGeoCodeSearchedListener listener;

    public GeocodeHelper setOnGeoCodeSearchedListener(OnGeoCodeSearchedListener l) {
        listener = l;
        return this;
    }

    public interface OnGeoCodeSearchedListener {
        void onSearched(String fullAddress);
    }
}
