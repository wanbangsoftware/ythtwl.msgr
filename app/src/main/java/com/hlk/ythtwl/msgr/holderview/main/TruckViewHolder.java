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
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.notification.Msgr;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/07 09:21 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/07 09:21 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class TruckViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_track_layout)
    private CorneredView rootView;
    @ViewId(R.id.ui_holder_view_track_license)
    private TextView licenseView;
    @ViewId(R.id.ui_holder_view_track_cause)
    private TextView causeView;
    @ViewId(R.id.ui_holder_view_track_time)
    private TextView timeView;
    @ViewId(R.id.ui_holder_view_track_description)
    private TextView descView;
    @ViewId(R.id.ui_holder_view_track_unread)
    private TextView unreadView;

    public TruckViewHolder(View itemView, Context activity) {
        super(itemView, activity);
        ViewUtility.bind(this, itemView);
    }

    public void showContent(Msgr msgr) {
        licenseView.setText(msgr.getLicense());
        timeView.setText(Utils.formatTimeAgo(msgr.getId()));
        unreadView.setVisibility(msgr.isUnread() ? View.VISIBLE : View.GONE);
        if (msgr.isUnread()) {
            // 未读的话，直接保存为已读
            msgr.setIsNew(Msgr.Readable.READ);
            Msgr.save(msgr);
        }
    }

    @Click({R.id.ui_holder_view_track_layout})
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
