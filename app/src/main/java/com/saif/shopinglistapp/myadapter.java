package com.saif.shopinglistapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class myadapter extends FirebaseRecyclerAdapter<ItemModel,myadapter.myviewholder> {

ItemModel model;
    private DatabaseReference databaseReference;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public myadapter(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull ItemModel model) {
        String textTitle = model.getTitle();
        String truncatedTextTitle = textTitle.substring(0, Math.min(textTitle.length(), 10));
        holder.title.setText(truncatedTextTitle + "...");

        String textDesc = model.getDesc();
        String truncatedTextDesc = textDesc.substring(0, Math.min(textDesc.length(), 30));
        holder.desc.setText(truncatedTextDesc + "...");
//        holder.title.setText(model.getTitle());
//        holder.desc.setText(model.getDesc());
        int imageId = R.drawable.delete;
        holder.deleteImg.setImageResource(imageId);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference itemRef = getRef(holder.getAdapterPosition());
                itemRef.removeValue();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                // Pass the data to the next activity
                intent.putExtra("title", model.getTitle());
                intent.putExtra("desc", model.getDesc());
                // Start the activity
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView title, desc;
        ImageView deleteImg;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            deleteImg = itemView.findViewById(R.id.deleteImg);

        }
    }
}

