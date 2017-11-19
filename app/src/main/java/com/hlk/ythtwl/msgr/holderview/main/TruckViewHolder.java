package com.hlk.ythtwl.msgr.holderview.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.hlklib.lib.view.CorneredView;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.etc.Utils;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.model.Truck;

/**
 * <b>功能描述：</b>车辆ViewHolder<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/19 17:55 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/19 17:55 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class TruckViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_truck_layout)
    private CorneredView rootView;
    @ViewId(R.id.ui_holder_view_truck_time)
    private TextView timeView;
    @ViewId(R.id.ui_holder_view_truck_license)
    private TextView licenseView;
    @ViewId(R.id.ui_holder_view_truck_description)
    private TextView descView;
    @ViewId(R.id.ui_holder_view_truck_unread)
    private TextView unreadView;
    @ViewId(R.id.ui_holder_view_truck_count)
    private TextView countView;

    public TruckViewHolder(View itemView, Context activity) {
        super(itemView, activity);
        ViewUtility.bind(this, itemView);
    }

    public void showContent(Truck truck) {
        timeView.setText(Utils.formatTimeAgo(truck.getLastTime()));
        licenseView.setText(truck.getLicense());
        unreadView.setVisibility(truck.getUnread() > 0 ? View.VISIBLE : View.GONE);
        unreadView.setText(StringHelper.getString(R.string.ui_view_holder_main_truck_unread, truck.getUnread()));
        countView.setText(StringHelper.getString(R.string.ui_view_holder_main_truck_count, (truck.getUnread() > 0 ? "/" : ""), truck.getCount()));
        descView.setText(R.string.ui_view_holder_main_truck_msg_merged);
    }

    @Click({R.id.ui_holder_view_truck_layout})
    private void elementClick(View view) {
        startViewClickEffect(rootView);
    }

    @Override
    protected void onRootViewClickEffectComplete(View view) {
        if (null != mOnViewHolderElementClickListener) {
            mOnViewHolderElementClickListener.onClick(view, getAdapterPosition());
        }
    }
}
