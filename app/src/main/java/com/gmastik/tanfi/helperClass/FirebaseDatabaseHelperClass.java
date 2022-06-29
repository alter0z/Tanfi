package com.gmastik.tanfi.helperClass;

import androidx.annotation.NonNull;

import com.gmastik.tanfi.models.CategoryTaskModel;
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
    private final DatabaseReference reference;
    private final List<CategoryTaskModel> tags = new ArrayList<>();

    public interface DataStatues{
        void DataIsLoaded(List<CategoryTaskModel> tags,List<String> key);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelperClass() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();
        reference = root.getReference("users").child(userID).child("task_tags");
    }

    public void readItem(final DataStatues dataStatues){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                tags.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    CategoryTaskModel categoryTask = keyNode.getValue(CategoryTaskModel.class);
                    tags.add(categoryTask);
                }

                dataStatues.DataIsLoaded(tags,keys);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
