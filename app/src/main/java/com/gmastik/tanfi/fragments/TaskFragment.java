package com.gmastik.tanfi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gmastik.tanfi.R;
import com.gmastik.tanfi.configs.RvCategoryTaskConfig;
import com.gmastik.tanfi.configs.RvUserTaskConfig;
import com.gmastik.tanfi.helperClass.FirebaseDatabaseHelperClass;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class TaskFragment extends Fragment {

    // variables
    TaskFragmentListener listener;
    TextView date,userTask;
    FirebaseAuth auth;
    FirebaseDatabase root;
    DatabaseReference userRef;
    String userID;
    ImageView showNav,closeNav;
    RecyclerView categoryRecycle,taskRecycle;
    long taskAmount = 0;

    public interface TaskFragmentListener {
        void oButtonClick(String status);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task,container,false);

        // date init
        Calendar calendar = Calendar.getInstance();
        String nowDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // find id
        date = view.findViewById(R.id.date);
        categoryRecycle = view.findViewById(R.id.category_task);
        taskRecycle = view.findViewById(R.id.user_task);
        userTask = view.findViewById(R.id.task);
        showNav = view.findViewById(R.id.show_nav);
        closeNav = view.findViewById(R.id.close_nav);

        // set date
        date.setText(nowDate);

        // get DB reference
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        userID = user.getUid();
        root = FirebaseDatabase.getInstance();
        userRef = root.getReference("users").child(userID).child("tasks");

        // read how many task
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    taskAmount  = (snapshot.getChildrenCount());
                // read user task amount
                if(!snapshot.exists()){
                    userTask.setText("Anda tidak memiliki tugas untuk sekaran");
                } else {
                    userTask.setText("Anda memiliki "+(String.valueOf(taskAmount))+" tugas hari ini");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // read tags data
        new FirebaseDatabaseHelperClass().readItem(new FirebaseDatabaseHelperClass.TagsDataStatues() {

            @Override
            public void TagsDataIsLoaded(List<CategoryTaskModel> tags, List<String> key) {
                new RvCategoryTaskConfig().setConfig(categoryRecycle, inflater.getContext(), tags,key);
            }

            @Override
            public void TagsDataIsInserted() {

            }

            @Override
            public void TagsDataIsUpdated() {

            }

            @Override
            public void TagsDataIsDeleted() {

            }
        });

        // read task data
        new FirebaseDatabaseHelperClass().readUserTaskData(new FirebaseDatabaseHelperClass.TaskDataStatues() {
            @Override
            public void TasksDataIsLoaded(List<UserTaskModel> tasks, List<String> key) {
                new RvUserTaskConfig().setConfig(taskRecycle,inflater.getContext(),tasks,key);
            }

            @Override
            public void TasksDataIsInserted() {

            }

            @Override
            public void TasksDataIsUpdated() {

            }

            @Override
            public void TasksDataIsDeleted() {

            }
        });

        // show nav
        showNav.setOnClickListener(v -> {
            String status = "opened";
            listener.oButtonClick(status);
            showNav.setVisibility(View.INVISIBLE);
            closeNav.setVisibility(View.VISIBLE);
        });

        // close nav
        closeNav.setOnClickListener(v -> {
            String status = "closed";
            listener.oButtonClick(status);
            showNav.setVisibility(View.VISIBLE);
            closeNav.setVisibility(View.INVISIBLE);
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof TaskFragmentListener){
            listener = (TaskFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+"Task fragment not implemented");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
