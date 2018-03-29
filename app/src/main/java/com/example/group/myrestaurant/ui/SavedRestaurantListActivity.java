package com.example.group.myrestaurant.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group.myrestaurant.Constants;
import com.example.group.myrestaurant.R;
import com.example.group.myrestaurant.adapters.FirebaseRestaurantViewHolder;
import com.example.group.myrestaurant.models.RestaurantModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private Query restaurantQuery;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(uid);
        restaurantQuery = mRestaurantReference.getRef();
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        FirebaseRecyclerOptions restaurantOptions = new FirebaseRecyclerOptions.Builder<RestaurantModel>().setQuery(restaurantQuery,
                RestaurantModel.class).build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<RestaurantModel, FirebaseRestaurantViewHolder>
                (restaurantOptions) {

            @Override
            public FirebaseRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_list_item, parent, false);

                return new FirebaseRestaurantViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(FirebaseRestaurantViewHolder holder, int position, RestaurantModel model) {
                holder.bindRestaurant(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
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
}