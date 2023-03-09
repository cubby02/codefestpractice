package com.example.codefestpractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsCustomAdapter extends RecyclerView.Adapter<DetailsCustomAdapter.ViewHolder> {

    List<Details> details;
    Context context;

    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public DetailsCustomAdapter(List<Details> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailsCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_details, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public int getItemCount() {
        return details.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, phone, dob;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            phone = itemView.findViewById(R.id.txtPhone);
            dob = itemView.findViewById(R.id.txtDOB);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsCustomAdapter.ViewHolder holder, int position) {
        Details item = details.get(position);

        int id = item.getId();
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());
        holder.dob.setText(item.getDob());
    }

    public void update(List<Details> list){
        if(list != null){
            details = list;
        }
        notifyDataSetChanged();
    }
}
