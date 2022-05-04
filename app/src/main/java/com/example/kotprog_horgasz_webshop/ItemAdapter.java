package com.example.kotprog_horgasz_webshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.RecyViewHolder>{

    private ArrayList itemList;
    private Context ctx;
    private int pos = -1;

    public ItemAdapter(ArrayList<Item> itemList, Context ctx) {
        this.itemList = itemList;
        this.ctx = ctx;
    }

    class RecyViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName;
        private TextView itemPrice;
        private ImageView itemImage;

        public RecyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemPicture);
        }

        public void bind(Item currentItem) {
            itemName.setText(currentItem.getNev());
            itemPrice.setText(currentItem.getAr());
            Glide.with(ctx).load(currentItem.getImage()).into(itemImage);
            itemView.findViewById(R.id.buttonKosar).setOnClickListener(view -> ((WebshopActivity)ctx).addItemToCart(currentItem));
        }
    }





    @NonNull
    @Override
    public RecyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_of_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.RecyViewHolder holder, int position) {
        Item currentItem = (Item) itemList.get(position);

        holder.bind(currentItem);

        Random rand = new Random(System.currentTimeMillis());
        int chosenAnim = rand.nextInt(3);
        Animation animation;
        if(chosenAnim%2 == 0) {
            animation = AnimationUtils.loadAnimation(ctx, R.anim.scale_in);
        }else{
            animation = AnimationUtils.loadAnimation(ctx, R.anim.bounce_anim);
        }
        if(holder.getAdapterPosition() > pos) {
            holder.itemView.startAnimation(animation);
            pos = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


