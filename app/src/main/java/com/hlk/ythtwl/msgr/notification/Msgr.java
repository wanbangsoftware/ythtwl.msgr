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
     * 开始停留时间
     */
    @Column(Fields.Begin)
    private String begin;
    /**
     * 停留的时间长度
     */
    @Column(Fields.Times)
    private long times;
    @Column(Fields.DriverName1)
    private String name1;
    @Column(Fields.DriverPhone1)
    private String phone1;
    @Column(Fields.DriverName2)
    private String name2;
    @Column(Fields.DriverPhone2)
    private String phone2;
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
        begin = in.readString();
        times = in.readLong();
        name1 = in.readString();
        phone1 = in.readString();
        name2 = in.readString();
        phone2 = in.readString();
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
        parcel.writeString(begin);
        parcel.writeLong(times);
        parcel.writeString(name1);
        parcel.writeString(phone1);
        parcel.writeString(name2);
        parcel.writeString(phone2);
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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
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
