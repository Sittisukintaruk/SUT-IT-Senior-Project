package com.example.rabbit777.recan_new;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.rabbit777.recan_new.Adapter.history_adater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class History_page extends AppCompatActivity
{
    DatabaseReference user,History;
    FirebaseDatabase database;
    String uid;
    TextView name , point;
    private history_adater adapter;
    private List<History> result;
    Context context;
    private RecyclerView recyclerView;
    private ImageView back_nt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_page);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_history);

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users");
        History = database.getReference("History");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = (TextView)findViewById(R.id.Name_history);
        point = (TextView)findViewById(R.id.point_history);
        result = new ArrayList<>();
        back_nt = (ImageView)findViewById(R.id.back_HT_back);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        lim.setReverseLayout(true);
        lim.setStackFromEnd(true);
        recyclerView.setLayoutManager(lim);
        adapter = new history_adater (result);
        recyclerView.setAdapter(adapter);


        back_nt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        SetPointUser(uid); // เซตค่า แต้มUser บน TextView และ ชื่อ นามสกุล

        HistoryQuery(uid); // เรียกประวัติการใช้งาน User มาใน RecyclerView

    }

    private void HistoryQuery(String uid)
    {
        History.child(uid).limitToLast(20).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                result.add(dataSnapshot.getValue(History.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                History use = dataSnapshot.getValue(History.class);
                int index =getIndex(use);
                result.set(index,use);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {
                History use = dataSnapshot.getValue(History.class);
                int index =getIndex(use);
                result.remove(index);
                adapter.notifyItemRemoved(index);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SetPointUser(String uid)
    {
        user.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.name+" "+user.subname);
                double text = Double.parseDouble(user.point);
                String number_point = String.format("%.2f",text);
                point.setText(number_point);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private int getIndex(History model)
    {
        int index = -1;
        for(int i = 0; i< result.size(); i++)
        {
            if(result.get(i).key.equals(model.key))
            {
                index = i;
                break;
            }

        }
        return index;
    }

}
