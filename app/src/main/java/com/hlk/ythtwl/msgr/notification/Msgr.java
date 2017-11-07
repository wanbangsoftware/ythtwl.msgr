package com.hlk.ythtwl.msgr.notification;

import android.os.Parcel;

import com.hlk.ythtwl.msgr.model.Dao;
import com.hlk.ythtwl.msgr.model.Model;
import com.hlk.ythtwl.msgr.orm.Fields;
import com.hlk.ythtwl.msgr.orm.Tables;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import java.util.List;

/**
 * <b>功能描述：</b>云信通知<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 14:14 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 14:14 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
@Table(Tables.MESSAGE)
public class Msgr extends Model implements MsgAttachment {

    public static void save(Msgr msg) {
        new Dao<>(Msgr.class).save(msg);
    }

    public static void save(List<Msgr> msgs) {
        new Dao<>(Msgr.class).save(msgs);
    }

    public static void delete(long msgId) {
        Dao<Msgr> dao = new Dao<>(Msgr.class);
        Msgr msg = dao.querySingle(Fields.Id, msgId);
        dao.delete(msg);
    }

    public static Msgr query(long msgId) {
        return new Dao<>(Msgr.class).querySingle(Fields.Id, msgId);
    }

    public static List<Msgr> query() {
        QueryBuilder<Msgr> builder = new QueryBuilder<>(Msgr.class).orderBy(Fields.Id);
        return new Dao<>(Msgr.class).query(builder);
    }

    public static void clear() {
        new Dao<>(Msgr.class).clear();
    }

    /**
     * 消息是否读取的状态
     */
    public interface Readable {
        /**
         * 未读消息
         */
        int UNREAD = 0;
        /**
         * 已读消息
         */
        int READ = 1;
    }

    /**
     * 车牌号
     */
    @Column(Fields.License)
    private String license;
    /**
     * 所处位置纬度
     */
    @Column(Fields.Latitude)
    private double latitude;
    /**
     * 所处位置经度
     */
    @Column(Fields.Longitude)
    private double longitude;
    /**
     * 报警状态
     */
    @Column(Fields.Status)
    private int status;
    /**
     * 是否新消息
     */
    @Column(Fields.IsNew)
    private int isNew;

    public Msgr() {
        super();
    }

    protected Msgr(Parcel in) {
        super(in);
        license = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        status = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(license);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(status);
    }

    public static final Creator<Msgr> CREATOR = new Creator<Msgr>() {
        @Override
        public Msgr createFromParcel(Parcel in) {
            return new Msgr(in);
        }

        @Override
        public Msgr[] newArray(int size) {
            return new Msgr[size];
        }
    };

    @Override
    public String toJson(boolean send) {
        return MsgrParser.packageData(this);
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 本条消息是否未读消息
     */
    public boolean isUnread() {
        return isNew == Readable.UNREAD;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
