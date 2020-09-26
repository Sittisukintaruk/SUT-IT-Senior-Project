package com.example.rabbit777.recan_new.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.rabbit777.recan_new.Notification_data;
import com.example.rabbit777.recan_new.R;

import java.util.List;

public class Mutest_nomi extends RecyclerView.Adapter<Mutest_nomi.UserViewHolder>
{

private List<Notification_data> list;

    public Mutest_nomi(List<Notification_data> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder( ViewGroup paren, int i) {
        return new UserViewHolder(LayoutInflater.from(paren.getContext()).inflate(R.layout.notification_item,paren,false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
        public void onBindViewHolder(final UserViewHolder holder, int position)
    {

        Notification_data noti = list.get(position);

        holder.content_xt.setText(noti.Content);
        holder.date.setText(noti.date);


        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
            {
                menu.add(holder.getAdapterPosition(),0,0,"Delect");
                menu.add(holder.getAdapterPosition(),1,0,"Delect");

            }
        });
    }

    class UserViewHolder extends RecyclerView.ViewHolder
    {
            TextView date,content_xt;

        public UserViewHolder(View itemView) {
            super(itemView);

            content_xt =(TextView)itemView.findViewById(R.id.content_xt);
            date = (TextView)itemView.findViewById(R.id.date);

        }
    }

}
