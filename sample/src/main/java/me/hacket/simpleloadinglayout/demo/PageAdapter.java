package me.hacket.simpleloadinglayout.demo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.hacket.simpleloadinglayout.R;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.MyViewHolder> {

    private List<String> mDatas;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public PageAdapter(Context mContext, List<String> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建视图
        LayoutInflater inflater = LayoutInflater.from(mContext);
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.rv_test_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        View itemView = holder.itemView;
        boolean b = position % 2 == 0;
        itemView.setBackgroundColor(b ? Color.GRAY : Color.YELLOW);

        // 绑定数据给view
        holder.tv.setText(mDatas.get(position));
        // 绑定点击事件
        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView) {
            tv = (TextView) itemView.findViewById(R.id.num);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemClickLongListener(OnItemLongClickListener onItemClickLongListener) {
        this.mOnItemLongClickListener = onItemClickLongListener;
    }

}
