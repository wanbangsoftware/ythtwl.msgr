package com.hlk.ythtwl.msgr.notification;

import com.hlk.ythtwl.msgr.orm.Fields;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <b>功能描述：</b>云信通知消息实体解析工具<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 19:21 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 19:21 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class MsgrParser implements MsgAttachmentParser {

    @Override
    public MsgAttachment parse(String attach) {
        try {
            Msgr msgr = new Msgr();
            JSONObject object = new JSONObject(attach);
            msgr.setId(object.optLong(Fields.Id, 0L));
            msgr.setLicense(object.optString(Fields.License, ""));
            msgr.setLatitude(object.optDouble(Fields.Latitude, 0F));
            msgr.setLongitude(object.optDouble(Fields.Longitude, 0F));
            msgr.setStatus(object.optInt(Fields.Status, 0));
            return msgr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String packageData(Msgr msgr) {
        JSONObject object = new JSONObject();
        try {
            object.put(Fields.Id, msgr.getId())
                    .put(Fields.License, msgr.getLicense())
                    .put(Fields.Latitude, msgr.getLatitude())
                    .put(Fields.Longitude, msgr.getLongitude())
                    .put(Fields.Status, msgr.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
