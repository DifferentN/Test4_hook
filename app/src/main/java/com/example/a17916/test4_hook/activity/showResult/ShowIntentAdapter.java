package com.example.a17916.test4_hook.activity.showResult;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a17916.test4_hook.R;

import java.util.List;

public class ShowIntentAdapter extends RecyclerView.Adapter<ShowIntentAdapter.MyViewHolder> {
    private List<ShowItem> showItems;
    private Context context;
    public ShowIntentAdapter(List<ShowItem> list,Context context){
        showItems = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.result_item,viewGroup,false);
        MyViewHolder viewNode = new MyViewHolder(view,new ItemListener(showItems,context));
        return viewNode;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setItem(showItems.get(i));
    }

    @Override
    public int getItemCount() {
        return showItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        private View contentView;
        private RecyclerViewItemListener listener;
        public MyViewHolder(@NonNull View itemView , RecyclerViewItemListener listener) {
            super(itemView);
            this.listener = listener;
            contentView = itemView;
            textView = contentView.findViewById(R.id.result_tarActivity_text);
            contentView.setOnClickListener(this);
        }
        public void setItem(ShowItem item){
            textView.setText("在"+item.getAppName()+"中搜索"+item.getResEntityName());
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getAdapterPosition());
        }
    }

}
