package com.gmastik.tanfi.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmastik.tanfi.R;
import com.gmastik.tanfi.models.CategoryTaskModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CategoryTaskAdapter extends RecyclerView.Adapter<CategoryTaskAdapter.CategoryTaskViewHolder> {

    private final ArrayList<CategoryTaskModel> items;
    int index = -1; // index item initial

    public CategoryTaskAdapter(ArrayList<CategoryTaskModel> items) {
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryTaskViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoryTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryTaskAdapter.CategoryTaskViewHolder holder, int position) {
        CategoryTaskModel currentItem = items.get(position);
        holder.tag.setText(currentItem.getTags());

        // selected item
        holder.linearLayout.setOnClickListener(v ->{
            index = position;
            notifyDataSetChanged();
        });

        // if item has selected or not
        if(index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.selected_category_bg);
            holder.tag.setTextColor(Color.parseColor("#F9F9F9"));
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.category_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoryTaskViewHolder extends RecyclerView.ViewHolder{

        TextView tag;
        LinearLayout linearLayout;

        public CategoryTaskViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tags);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
