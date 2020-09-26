package com.example.rabbit777.recan_new;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.rabbit777.recan_new.Adapter.Mutest_nomi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class Notification_page extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Notification_data> result;
    private Mutest_nomi adapter;
    private boolean asencing = true;
    private static ArrayList<String> list = new ArrayList<>();
    ImageButton sortnoti_pg;
    DatabaseReference notification;
    String uid,ste;
    ArrayList Userlist;
    Query query;
    SwipeController swipeController = null;
   private ImageView back_nt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        result = new ArrayList<>();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_notification);

        mAuth = FirebaseAuth.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        context = this;
        notification = database.getReference("Notification");
        recyclerView = (RecyclerView) findViewById(R.id.user_list);
        back_nt = (ImageView)findViewById(R.id.back_NT_back);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        lim.setReverseLayout(true);
        lim.setStackFromEnd(true);

        recyclerView.setLayoutManager(lim);


        query = notification.child(uid);
        adapter = new Mutest_nomi(result);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });



        SwipeController swipeController = new SwipeController();


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                notification.child(uid).child(result.get(position).uid).removeValue();
/*
                Toast.makeText(context, "ตำแหน่ง " + result.get(position).uid, Toast.LENGTH_SHORT).show();
*/
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        databasequery(query);
        back_nt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                finish();
            }
        });





    }

    private void databasequery(Query query)
    {

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                result.add(dataSnapshot.getValue(Notification_data.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Notification_data use = dataSnapshot.getValue(Notification_data.class);
                int index = getIndex(use);
                result.set(index,use);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Notification_data use = dataSnapshot.getValue(Notification_data.class);
                int index =getIndex(use);
                result.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 0:
                break;
            case 1:
                break;


        }


        return super.onContextItemSelected(item);
    }





    private int getIndex(Notification_data model)
    {
        int index = -1;
        for(int i = 0; i< result.size(); i++)
        {
            if(result.get(i).uid.equals(model.uid))
            {
                index = i;
                break;
            }

        }
        return index;
    }


}








