package com.example.rabbit777.recan_new.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.rabbit777.recan_new.History;
import com.example.rabbit777.recan_new.R;

import java.util.List;

public class history_adater extends RecyclerView.Adapter<history_adater.UserViewHolder>
{

private List<History> list;

    public history_adater(List<History> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder( ViewGroup paren, int i) {
        return new UserViewHolder(LayoutInflater.from(paren.getContext()).inflate(R.layout.history_item,paren,false));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
        public void onBindViewHolder(final UserViewHolder holder, int position)
    {

        History history = list.get(position);


        holder.content_xt.setText(history.detail);
        if(history.Negative_positive.equals("-"))
        {
            holder.point.setText("-" + history.Point);
            holder.point.setTextColor(Color.RED);
        }
        else
        {
            holder.point.setText("+" + history.Point);
            holder.point.setTextColor(Color.GREEN);
        }
        holder.dates.setText(history.Date);


    }

    class UserViewHolder extends RecyclerView.ViewHolder
    {
            TextView point,content_xt,dates;

        public UserViewHolder(View itemView) {
            super(itemView);

            content_xt =(TextView)itemView.findViewById(R.id.content_history_detail);
            point = (TextView)itemView.findViewById(R.id.point_history_dt);
            dates = (TextView)itemView.findViewById(R.id.date_history);


        }

    }


}
