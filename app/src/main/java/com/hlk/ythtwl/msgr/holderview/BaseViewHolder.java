package com.hlk.ythtwl.msgr.holderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hlk.ythtwl.msgr.helper.LogHelper;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.holderview.listener.OnViewHolderElementClickListener;

import java.lang.ref.SoftReference;

/**
 * <b>功能：</b>RecyclerViewHolder基类<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2015/12/30 14:39 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SoftReference<Context> context;

    /**
     * 当前 holder 的 tag 对象
     */
    private Object mTag;

    public BaseViewHolder(View itemView, Context activity) {
        super(itemView);
        if (null != activity) {
            this.context = new SoftReference<>(activity);
        }
    }

    protected boolean isEmpty(String text) {
        return StringHelper.isEmpty(text);
    }

    protected boolean multiSelectable = false;

    /**
     * 设置是否可多选
     */
    public void setMultiSelectable(boolean selectable) {
        multiSelectable = selectable;
    }

    /**
     * attach
     */
    public void attachedFromWindow() {
    }

    /**
     * detach
     */
    public void detachedFromWindow() {
    }

    protected void log(String text) {
        LogHelper.log(this.getClass().getSimpleName(), text);
    }

    protected String format(String format, Object... args) {
        return StringHelper.format(format, args);
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    /**
     * 为CardView当作跟View的UI布局演示点击动画
     */
    protected void startRootViewClickEffect() {
        startViewClickEffect(itemView);
    }

    protected void startViewClickEffect(final View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 20, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onRootViewClickEffectComplete(view);
            }
        });
        animator.start();
    }

    /**
     * 根CardView的点击动画执行完毕
     */
    protected void onRootViewClickEffectComplete(View view) {
    }

    /**
     * 添加长按处理回调
     */
    public void addOnLongClickListener(View.OnLongClickListener l) {
    }

    /**
     * 保存暂存数据到bundle中
     */
    public void saveParamsToBundle(Bundle bundle) {
    }

    /**
     * 从bundle中恢复数据
     */
    public void getParamsFromBundle(Bundle bundle) {
    }

    protected OnViewHolderElementClickListener mOnViewHolderElementClickListener;

    /**
     * 添加元素点击事件处理回调
     */
    public void setOnViewHolderElementClickListener(OnViewHolderElementClickListener l) {
        mOnViewHolderElementClickListener = l;
    }
}
