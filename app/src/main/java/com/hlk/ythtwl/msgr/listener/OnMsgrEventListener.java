package com.hlk.ythtwl.msgr.listener;

import com.hlk.ythtwl.msgr.notification.Msgr;

/**
 * <b>功能描述：</b>云信自定义通知事件<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 19:40 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 19:40 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public interface OnMsgrEventListener {
    void onEvent(Msgr msgr);
}
