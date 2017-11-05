package com.hlk.ythtwl.msgr.application;

import android.Manifest;

import com.hlk.hlklib.etc.Cryptography;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 14:21 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 14:21 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Orm extends Base {

    public static LiteOrm Orm;

    protected void closeOrm() {
        if (null != Orm) {
            Orm.close();
            Orm = null;
        }
    }

    /**
     * 按照指定的文件名初始化数据库
     */
    public void initializeLiteOrm(final String dbName) {
        if (isEmpty(dbName)) {
            throw new IllegalArgumentException("could not initialize database with null parameter.");
        }

        String db = getCachePath(DB_DIR) + Cryptography.md5(dbName) + ".db";
        if (null == Orm) {
            if (initialize(db)) {
                //log("database initialized at: " + db);
            }
        } else {
            if (!Orm.getDataBaseConfig().dbName.equals(db)) {
                Orm.close();
                if (initialize(db)) {
                    //log("database re-initialized at: " + db);
                }
            }
        }
    }
    private boolean initialize(String db) {
        try {
            Orm = LiteOrm.newSingleInstance(getConfig(db));
            Orm.openOrCreateDatabase();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Orm = null;
        return false;
    }

    private DataBaseConfig getConfig(String dbName) {
        DataBaseConfig config = new DataBaseConfig(Orm.this, dbName);
        //config.debugged = BuildConfig.DEBUG;
        config.dbVersion = 1;
        return config;
    }
}
