package com.hlk.ythtwl.msgr.holderview.stopping;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.hlklib.lib.view.CorneredView;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.etc.Utils;
import com.hlk.ythtwl.msgr.helper.GeocodeHelper;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.model.Point;


/**
 * <b>功能描述：</b>停车点的ViewHolder<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2018/08/26 09:19 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/10/04 18:50 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class StopPointViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_stop_point_date_divider)
    private View dateDivider;
    @ViewId(R.id.ui_holder_view_stop_point_date)
    private TextView dateView;
    @ViewId(R.id.ui_holder_view_stop_point_stop_length_layout)
    private CorneredView lengthLayout;
    @ViewId(R.id.ui_holder_view_stop_point_stop_length)
    private TextView lengthView;
    @ViewId(R.id.ui_holder_view_stop_point_stop_time)
    private TextView stopTimeView;
    @ViewId(R.id.ui_holder_view_stop_point_restart_time)
    private TextView startTimeView;
    @ViewId(R.id.ui_holder_view_stop_point_address)
    private TextView addressView;

    public StopPointViewHolder(View itemView, Context activity) {
        super(itemView, activity);
        ViewUtility.bind(this, itemView);
    }

    public void showContent(Point point) {
        dateView.setText(Utils.format(StringHelper.getString(R.string.ui_stop_point_date_fmt), point.getStop() * 1000));
        lengthView.setText(Html.fromHtml(formatLength(point.getLen())));
        lengthLayout.setBackground(ContextCompat.getColor(lengthLayout.getContext(), (point.getLen() > 120 ? R.color.colorAccent : R.color.colorLicenseBlue)));
        stopTimeView.setText(Utils.format(StringHelper.getString(R.string.ui_stop_point_time_fmt), point.getStop() * 1000));
        startTimeView.setText(Utils.format(StringHelper.getString(R.string.ui_stop_point_time_fmt), point.getRestart() * 1000));
        addressView.setSelected(true);
        GeocodeHelper.helper(context()).setOnGeoCodeSearchedListener(new GeocodeHelper.OnGeoCodeSearchedListener() {
            @Override
            public void onSearched(String fullAddress) {
                addressView.setText(fullAddress.replace("山东省", ""));
            }
        }).tryReverseGeoCode(new LatLonPoint(point.getLat(), point.getLng()));
    }

    private String formatLength(long length) {
        long hours = length / 3600;
        long minutes = (length % 3600) / 60;
        long seconds = length % 60;
        String ret = "";
        if (hours > 0) {
            ret = format("%d小时", hours);
            if (minutes > 0 || seconds > 0) {
                ret += "<br/>";
            }
        }
        if (minutes > 0) {
            ret = format("%s%d分", ret, minutes);
        }
        if (seconds > 0) {
            if (minutes > 0 || hours > 0) {
                ret = format("%s%02d秒", ret, seconds);
            } else {
                ret = format("%s%d秒", ret, seconds);
            }
        }
        return ret;
    }

    @Click({R.id.ui_holder_view_stop_point_map})
    private void viewClick(View view) {
        if (null != mOnViewHolderElementClickListener) {
            mOnViewHolderElementClickListener.onClick(view, getAdapterPosition());
        }
    }
}
