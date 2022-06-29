package com.gmastik.tanfi.configs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmastik.tanfi.R;
import com.gmastik.tanfi.models.CategoryTaskModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RvCategoryTaskConfig {

    public void setConfig(RecyclerView recyclerView,Context context,List<CategoryTaskModel> tags,List<String> key){
        TagsAdapter tagsAdapter = new TagsAdapter(tags, key);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(tagsAdapter);
    }

    static class TagsView extends RecyclerView.ViewHolder{
        private final TextView tag;
        private String key;
        private final LinearLayout linearLayout;

        public TagsView(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false));

            tag = itemView.findViewById(R.id.tags);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }

        public void bind(CategoryTaskModel tags,String key){
            tag.setText(tags.getTags());
            this.key = key;
        }
    }

    static class TagsAdapter extends RecyclerView.Adapter<TagsView>{
        private final List<CategoryTaskModel> tagList;
        private final List<String> keys;
        private int index;

        public TagsAdapter(List<CategoryTaskModel> categories, List<String> keys) {
            this.tagList = categories;
            this.keys = keys;
        }

        @NonNull
        @NotNull
        @Override
        public TagsView onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new TagsView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RvCategoryTaskConfig.TagsView holder, int position) {
            holder.bind(tagList.get(position),keys.get(position));

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
            return tagList.size();
        }
    }
}
