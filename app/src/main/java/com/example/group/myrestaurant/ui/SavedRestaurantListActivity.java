package com.example.group.myrestaurant.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group.myrestaurant.Constants;

import com.example.group.myrestaurant.R;
import com.example.group.myrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.example.group.myrestaurant.adapters.FirebaseRestaurantViewHolder;
import com.example.group.myrestaurant.models.RestaurantModel;
import com.example.group.myrestaurant.util.ItemTouchHelperAdapter;
import com.example.group.myrestaurant.util.SimpleItemTouchHelperCallback;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity implements View.OnDragListener{
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private Query restaurantQuery;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(uid);
        restaurantQuery = mRestaurantReference.getRef();
        FirebaseRecyclerOptions restaurantOptions = new FirebaseRecyclerOptions.Builder<RestaurantModel>().setQuery(restaurantQuery,
                RestaurantModel.class).build();

        mFirebaseAdapter = new FirebaseRestaurantListAdapter(restaurantOptions,mRestaurantReference,this,this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.stopListening();
    }

    @Override protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

//    @Override
//    public void onDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
//    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }
}