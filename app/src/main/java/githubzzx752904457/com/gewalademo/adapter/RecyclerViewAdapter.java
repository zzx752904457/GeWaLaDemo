package githubzzx752904457.com.gewalademo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import githubzzx752904457.com.gewalademo.R;

/**
 * Created by admin on 2016/8/26.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private MyItemClickListener itemClickListener;
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(MyItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText("谍影重重5("+position+")");
    }

    @Override
    public int getItemCount() {
        return 50;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;
        private ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(iv, getLayoutPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(ImageView imageView, int position);
    }
}
