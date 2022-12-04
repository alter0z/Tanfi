package com.gmastik.tanfi.configs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmastik.tanfi.R;
import com.gmastik.tanfi.models.UserTaskModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RvUserTaskConfig {

    public void setConfig(RecyclerView recyclerView,Context context,List<UserTaskModel> taskModels,List<String> key){
        TaskAdapter taskAdapter = new TaskAdapter(taskModels,key);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(taskAdapter);
    }

    static class TaskView extends RecyclerView.ViewHolder{
        private final TextView timeLine,title,desc,date;
        private String key;
        private final LinearLayout linearLayout;

        public TaskView(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false));

            timeLine = itemView.findViewById(R.id.time_line);
            title = itemView.findViewById(R.id.task_title);
            desc = itemView.findViewById(R.id.task_desc);
            date = itemView.findViewById(R.id.task_date);
            linearLayout = itemView.findViewById(R.id.task_linear_layout);
        }

        public void bind(UserTaskModel taskModel,String key){
            timeLine.setText(taskModel.getTime_line());
            title.setText(taskModel.getTitle());
            desc.setText(taskModel.getDesc());
            date.setText(taskModel.getDate());
            this.key = key;
        }
    }

    static class TaskAdapter extends RecyclerView.Adapter<TaskView>{
        private final List<UserTaskModel> taskList;
        private final List<String> keys;
//        private int index;

        public TaskAdapter(List<UserTaskModel> taskModelList, List<String> keys) {
            this.taskList = taskModelList;
            this.keys = keys;
        }

        @NonNull
        @NotNull
        @Override
        public TaskView onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new TaskView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RvUserTaskConfig.TaskView holder, int position) {
            holder.bind(taskList.get(position),keys.get(position));
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
