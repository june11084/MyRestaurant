package com.example.group.myrestaurant.ui;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group.myrestaurant.Constants;
import com.example.group.myrestaurant.R;
import com.example.group.myrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.example.group.myrestaurant.models.RestaurantModel;
import com.example.group.myrestaurant.util.SimpleItemTouchHelperCallback;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListFragment extends Fragment implements View.OnDragListener {
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private DatabaseReference mRestaurantReference;
    private FirebaseRestaurantListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Query restaurantQuery;

    public SavedRestaurantListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("mfirebaseAdapter", "OnCreateView working");
        View view = inflater.inflate(R.layout.fragment_saved_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        setUpFirebaseAdapter();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.setIndexInFirebase();
        mFirebaseAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }

    public void setUpFirebaseAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(uid);

        restaurantQuery = mRestaurantReference.getRef().orderByChild(Constants.FIREBASE_QUERY_INDEX);
        FirebaseRecyclerOptions restaurantOptions = new FirebaseRecyclerOptions.Builder<RestaurantModel>().setQuery(restaurantQuery,
                RestaurantModel.class).build();

        mFirebaseAdapter = new FirebaseRestaurantListAdapter(restaurantOptions,restaurantQuery, this,getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mFirebaseAdapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
