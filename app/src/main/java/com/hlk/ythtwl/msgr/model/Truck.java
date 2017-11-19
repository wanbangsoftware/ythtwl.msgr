package com.hlk.ythtwl.msgr.model;

import android.os.Parcel;

import com.hlk.hlklib.etc.Cryptography;

import java.math.BigInteger;

/**
 * <b>功能描述：</b>车辆信息<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/19 12:18 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/19 12:18 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Truck extends Model {

    public Truck() {
        super();
    }

    protected Truck(Parcel in) {
        super(in);
        license = in.readString();
        count = in.readInt();
        unread = in.readInt();
        lastTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(license);
        parcel.writeInt(count);
        parcel.writeInt(unread);
        parcel.writeLong(lastTime);
    }

    public static final Creator<Truck> CREATOR = new Creator<Truck>() {
        @Override
        public Truck createFromParcel(Parcel parcel) {
            return new Truck(parcel);
        }

        @Override
        public Truck[] newArray(int size) {
            return new Truck[size];
        }
    };

    public boolean equals(Truck truck) {
        return null != truck && truck.getLicense().equals(license);
    }

    @Override
    public boolean equals(Object object) {
        return null != object && (getClass() == object.getClass()) && (object instanceof Truck) && equals((Truck) object);
    }

    // 车牌
    private String license;
    // 信息个数
    private int count;
    // 未读消息个数
    private int unread;
    // 最后信息接收时间
    private long lastTime;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
        String md5 = Cryptography.md5(license);
        // 车辆信息的id为车牌号的md5值高位的long值
        setId(new BigInteger(md5.substring(0, 16), 16).longValue());
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
