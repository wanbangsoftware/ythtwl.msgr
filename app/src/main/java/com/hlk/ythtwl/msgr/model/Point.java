package com.hlk.ythtwl.msgr.model;

import java.io.Serializable;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>SYSTEM <br />
 * <b>创建时间：</b>2018/08/22 15:07 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2018/08/22 15:07 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Point implements Serializable {

    private double lng, lat;
    private long stop, restart, len;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getStop() {
        return stop;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public long getRestart() {
        return restart;
    }

    public void setRestart(long restart) {
        this.restart = restart;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }
}
