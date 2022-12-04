package com.gmastik.tanfi.helperClass;

import androidx.annotation.NonNull;

import com.gmastik.tanfi.models.CategoryTaskModel;
import com.gmastik.tanfi.models.UserTaskModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseDatabaseHelperClass {
    private final DatabaseReference tagsRef,userTaskRef;
    private final List<CategoryTaskModel> tags = new ArrayList<>();
    private final List<UserTaskModel> userTasks = new ArrayList<>();

    public interface TagsDataStatues{
        void TagsDataIsLoaded(List<CategoryTaskModel> tags,List<String> key);
        void TagsDataIsInserted();
        void TagsDataIsUpdated();
        void TagsDataIsDeleted();
    }

    public interface TaskDataStatues{
        void TasksDataIsLoaded(List<UserTaskModel> tasks,List<String> key);
        void TasksDataIsInserted();
        void TasksDataIsUpdated();
        void TasksDataIsDeleted();
    }

    public FirebaseDatabaseHelperClass() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        tagsRef = root.getReference("users").child(userID).child("task_tags");
        userTaskRef = root.getReference("users").child(userID).child("tasks");
    }

    public void readUserTaskData(final TaskDataStatues taskDataStatues){
        userTaskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userTasks.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    UserTaskModel userTask = keyNode.getValue(UserTaskModel.class);
                    userTasks.add(userTask);
                }

                taskDataStatues.TasksDataIsLoaded(userTasks,keys);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void readItem(final TagsDataStatues tagsDataStatues){
        tagsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                tags.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    CategoryTaskModel categoryTask = keyNode.getValue(CategoryTaskModel.class);
                    tags.add(categoryTask);
                }

                tagsDataStatues.TagsDataIsLoaded(tags,keys);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
