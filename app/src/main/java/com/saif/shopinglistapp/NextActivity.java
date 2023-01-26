package com.saif.shopinglistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saif.shopinglistapp.databinding.ActivityNextBinding;

public class NextActivity extends AppCompatActivity {
    ActivityNextBinding binding;
    String title;
    String desc;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    myadapter adapter;
    ItemModel m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
binding.logOut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        // Redirect the user to the login activity
        Intent intent = new Intent(NextActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
});

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();
        String currentUserID = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("List");

        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(reference, ItemModel.class)
                        .build();

        adapter = new myadapter(options);
        binding.recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);
        final EditText editText1 = dialogView.findViewById(R.id.edit_text1);
        final EditText editText2 = dialogView.findViewById(R.id.edit_text2);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = editText1.getText().toString();
                desc = editText2.getText().toString();
                // Do something with the text
                auth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                ItemModel itemModel = new ItemModel(title, desc);
                databaseReference.child(auth.getCurrentUser().getUid()).child("List").child(title).setValue(itemModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(NextActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NextActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }
}

