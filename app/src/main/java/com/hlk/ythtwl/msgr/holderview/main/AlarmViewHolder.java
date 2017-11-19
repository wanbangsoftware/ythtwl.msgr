package com.hlk.ythtwl.msgr.holderview.main;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hlk.hlklib.lib.inject.Click;
import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.hlklib.lib.view.CorneredView;
import com.hlk.hlklib.lib.view.CustomTextView;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.etc.Utils;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.notification.Msgr;

/**
 * <b>功能描述：</b>警报信息的ViewHolder<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/07 09:21 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/07 09:21 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class AlarmViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_alarm_layout)
    private CorneredView rootView;
    @ViewId(R.id.ui_holder_view_alarm_caution)
    private CustomTextView cautionIcon;
    @ViewId(R.id.ui_holder_view_alarm_license_layout)
    private CorneredView licenseLayout;
    @ViewId(R.id.ui_holder_view_alarm_license)
    private TextView licenseView;
    @ViewId(R.id.ui_holder_view_alarm_cause)
    private TextView causeView;
    @ViewId(R.id.ui_holder_view_alarm_time)
    private TextView timeView;
    @ViewId(R.id.ui_holder_view_alarm_description)
    private TextView descView;
    @ViewId(R.id.ui_holder_view_alarm_unread)
    private TextView unreadView;

    private boolean showCautionIcon = false;

    public AlarmViewHolder(View itemView, Context activity) {
        super(itemView, activity);
        ViewUtility.bind(this, itemView);
    }

    public void setShowCautionIcon(boolean shown) {
        showCautionIcon = shown;
    }

    public void showContent(Msgr msgr) {
        cautionIcon.setVisibility(showCautionIcon ? View.VISIBLE : View.GONE);
        licenseView.setText(msgr.getLicense());
        licenseLayout.setVisibility(showCautionIcon ? View.GONE : View.VISIBLE);
        timeView.setText(Utils.formatTimeAgo(msgr.getId()));
        unreadView.setVisibility(msgr.isUnread() ? View.VISIBLE : View.GONE);
        String time = msgr.getBegin();
        time = isEmpty(time) ? "--:--" : Utils.format(time, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        descView.setText(StringHelper.getString(R.string.ui_view_holder_main_truck_description, time, msgr.getTimes() / 60));
        if (msgr.isUnread()) {
            // 未读的话，直接保存为已读
            msgr.setIsNew(Msgr.Readable.READ);
            Msgr.save(msgr);
        }
    }

    @Click({R.id.ui_holder_view_alarm_layout})
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
