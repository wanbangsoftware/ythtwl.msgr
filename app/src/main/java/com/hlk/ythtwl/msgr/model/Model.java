package com.hlk.ythtwl.msgr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.orm.Fields;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 14:42 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 14:42 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public abstract class Model implements Parcelable {

    public static Model getNothingMore() {
        return new Model() {
            @Override
            public void setId(long id1) {
                super.setId(R.string.ui_base_text_nothing_more);
            }
        };
    }

    public static boolean isEmpty(String text) {
        return StringHelper.isEmpty(text, false);
    }

    @PrimaryKey(AssignType.BY_MYSELF)
    @Column(Fields.Id)
    private long id;

    public Model() {
    }

    protected Model(Parcel in) {
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
    }

    @Override
    public boolean equals(Object object) {
        return null != object && (getClass() == object.getClass()) && (object instanceof Model) && equals((Model) object);
    }

    public boolean equals(Model msg) {
        return null != msg && msg.getId() == getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
