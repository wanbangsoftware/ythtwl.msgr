package com.hlk.ythtwl.msgr.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hlk.ythtwl.msgr.holderview.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * <b>功能描述：</b>RecyclerView的适配器，提供添加删除功能<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2016/12/22 13:19 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2016/12/22 13:19 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public abstract class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder, T>
        extends RecyclerView.Adapter<VH> implements RecycleAdapter<T> {

    /**
     * 设置item所占的列数，只能用在GridLayoutManager中，其余的LayoutManager无效
     */
    @Override
    public int getItemSpanSize(int position) {
        return 1;
    }


    @Override
    public boolean isItemNeedFullLine(int position) {
        return false;
    }

    /**
     * 创建ViewHolder
     */
    public abstract VH onCreateViewHolder(View itemView, int viewType);

    /**
     * 返回单个item的layout布局
     */
    public abstract int itemLayout(int viewType);

    /**
     * 绑定数据
     */
    public abstract void onBindHolderOfView(VH holder, int position, @Nullable T item);

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).attachedFromWindow();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = ((GridLayoutManager) manager);
            final int spanCount = gridManager.getSpanCount();
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 第一个item占满整行
                    boolean isFullLineSupport = isItemNeedFullLine(position);
                    if (isFullLineSupport) {
                        return spanCount;
                    } else {
                        int size = getItemSpanSize(position);
                        // 小于1时默认占1列，大于等于spanCount时也即占满全行
                        return size < 1 ? 1 : (size >= spanCount ? spanCount : size);
                    }
                }
            });
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(itemLayout(viewType), parent, false);
        return onCreateViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindHolderOfView(holder, position, innerList.get(position));
        // 是否占满屏幕宽度的设定
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
            p.setFullSpan(isItemNeedFullLine(position));
            //int size = getItemSpanSize(position);
        }
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).detachedFromWindow();
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return innerList.size();
    }

    private List<T> innerList = new ArrayList<>();

    @Override
    public void clear() {
        int size = innerList.size();
        while (size > 0) {
            remove(size - 1);
            size = innerList.size();
        }
    }

    @Override
    public void remove(int position) {
        if (position >= 0) {
            innerList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void remove(T item) {
        remove(indexOf(item));
    }

    @Override
    public void replace(int index, T item) {
        if (index >= 0 && index < innerList.size() - 1) {
            innerList.set(index, item);
            notifyItemChanged(index);
        }
    }

    @Override
    public void replace(T from, T to) {
        int index = indexOf(from);
        if (index >= 0) {
            innerList.set(index, to);
            notifyItemChanged(index);
        }
    }

    @Override
    public void add(T item) {
        if (!exist(item)) {
            innerList.add(item);
            notifyItemInserted(innerList.size() - 1);
        }
    }

    @Override
    public void add(T item, boolean replace) {
        if (replace) {
            update(item);
        } else {
            add(item);
        }
    }

    @Override
    public void add(T item, int position) {
        if (!exist(item)) {
            innerList.add(position, item);
            notifyItemInserted(position);
        }
    }

    @Override
    public void add(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }

    @Override
    public void add(List<T> list, int position) {
        int index = position;
        for (T t : list) {
            if (!exist(t)) {
                add(t, index);
                index++;
            }
        }
    }

    @Override
    public T get(int position) {
        return innerList.get(position);
    }

    @Override
    public boolean exist(T item) {
        return indexOf(item) >= 0;
    }

    @Override
    public int indexOf(T item) {
        return innerList.indexOf(item);
    }

    /**
     * 更新一个指定id的item内容
     */
    @Override
    public void update(T item) {
        int index = indexOf(item);
        if (index >= 0) {
            innerList.set(index, item);
            notifyItemChanged(index);
        } else {
            add(item);
        }
    }

    @Override
    public void update(List<T> list) {
        update(list, true);
    }

    @Override
    public void update(List<T> list, boolean replaceable) {
        if (replaceable) {
            Iterator<T> iterator = innerList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                // 移除旧列表里不在list中的记录
                T t = iterator.next();
                if (list.indexOf(t) < 0) {
                    iterator.remove();
                    notifyItemRemoved(index);
                }
                index++;
            }
        }
        for (T t : list) {
            update(t);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return innerList.iterator();
    }

    @Override
    public void add(List<T> list, boolean fromStart) {
        int index = 0;
        for (T t : list) {
            if (!exist(t)) {
                if (fromStart) {
                    add(t, index);
                    index++;
                } else {
                    add(t);
                }
            }
        }
    }

    @Override
    public void sort() {
        Collections.sort(innerList, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return comparator(o1, o2);
            }
        });
        notifyItemRangeChanged(0, innerList.size());
    }

    /**
     * 重新排序
     */
    protected abstract int comparator(T item1, T item2);
}
