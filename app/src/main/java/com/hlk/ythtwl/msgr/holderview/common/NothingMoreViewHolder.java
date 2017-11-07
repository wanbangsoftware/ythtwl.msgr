package com.hlk.ythtwl.msgr.holderview.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;
import com.hlk.ythtwl.msgr.model.Model;

/**
 * <b>功能描述：</b>后面没有了<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/10/19 11:19 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/10/19 11:19 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class NothingMoreViewHolder extends BaseViewHolder {

    @ViewId(R.id.ui_holder_view_nothing_more_text)
    private TextView textView;

    public NothingMoreViewHolder(View itemView, Context activity) {
        super(itemView, activity);
        ViewUtility.bind(this, itemView);
    }

    public void showContent(Model model) {
        textView.setText((int) model.getId());
    }
}
