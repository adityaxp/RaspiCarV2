package com.aditya.raspicarv2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>{

    Context context;
    ArrayList<MenuItemData> menuItemDataArrayList;


    public static class MenuItemsViewHolder extends RecyclerView.ViewHolder{

        ImageView menuItemImageView;
        TextView menuItemTextView;
        CardView menuItemCardView;


        public MenuItemsViewHolder(@NonNull View itemView){
            super(itemView);

            menuItemCardView = (CardView) itemView.findViewById(R.id.menuItemCardView);
            menuItemImageView = (ImageView) itemView.findViewById(R.id.menuImage);
            menuItemTextView = (TextView) itemView.findViewById(R.id.menuItemTextView);

        }

    }

    public MenuItemsAdapter(Context context, ArrayList<MenuItemData> menuItemDataArrayList){
        this.context = context;
        this.menuItemDataArrayList = menuItemDataArrayList;
    }

    @NonNull
    @Override
    public MenuItemsAdapter.MenuItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_recycler_view_row_item, parent, false);
        MenuItemsViewHolder menuItemsViewHolder = new MenuItemsViewHolder(view);
        return menuItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemsAdapter.MenuItemsViewHolder holder, int position) {
        final MenuItemData menuItemData = menuItemDataArrayList.get(position);
        final int positionValue = position;


        holder.menuItemTextView.setText(menuItemData.getMenuItemData());
        holder.menuItemImageView.setImageResource(menuItemData.getMenuItemImage());
        holder.menuItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(positionValue == 0){
                    intent = new Intent(context, CameraStreamActivity.class);
                    context.startActivity(intent);
                }
                if(positionValue == 1){
                   intent = new Intent(context, ManualControl.class);
                   context.startActivity(intent);
                }

                if(positionValue == 2){
                    intent = new Intent(context, GuidedControlActivity.class);
                    context.startActivity(intent);
                }

                if(positionValue == 3){
                    intent = new Intent(context, LogsAndSensorDataActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemDataArrayList.size();
    }
}