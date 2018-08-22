package com.hlk.ythtwl.msgr.orm;

/**
 * <b>功能描述：</b>本地缓存数据库数据表字段名<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 14:18 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 14:18 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public interface Fields {
    String Id = "id";
    String _Id = "__id";
    String License = "license";
    String Latitude = "latitude";
    String Longitude = "longitude";
    String Status = "status";
    String Begin = "beginAt";
    String Times = "times";
    String DriverName1 = "driverName1";
    String DriverPhone1 = "driverPhone1";
    String DriverName2 = "driverName2";
    String DriverPhone2 = "driverPhone2";
    String StopPoints = "stopPoints";
    String IsNew = "isNew";
}
