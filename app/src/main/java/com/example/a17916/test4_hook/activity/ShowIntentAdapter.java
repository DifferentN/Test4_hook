package com.example.a17916.test4_hook.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.view_data.MyViewNode;

import java.util.List;

public class ShowIntentAdapter extends RecyclerView.Adapter<ShowIntentAdapter.MyViewHolder> {
    private List<Intent> intents;
    private Context context;
    public ShowIntentAdapter(List<Intent> list,Context context){
        intents = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.result_item,viewGroup,false);
        MyViewHolder viewNode = new MyViewHolder(view,new ItemListener(intents,context));
        return viewNode;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ComponentName componentName = intents.get(i).getComponent();
        myViewHolder.setText(componentName.getClassName());
    }

    @Override
    public int getItemCount() {
        return intents.size();
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
        public void setText(String text){
            textView.setText(textView.getText()+text);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v,getAdapterPosition());
        }
    }

}
