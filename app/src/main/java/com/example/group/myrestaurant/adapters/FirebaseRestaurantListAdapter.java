package com.example.group.myrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group.myrestaurant.R;
import com.example.group.myrestaurant.models.RestaurantModel;
import com.example.group.myrestaurant.ui.RestaurantDetailActivity;
import com.example.group.myrestaurant.util.ItemTouchHelperAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseRestaurantListAdapter extends FirebaseRecyclerAdapter<RestaurantModel, FirebaseRestaurantViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private View.OnDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<RestaurantModel> mRestaurants = new ArrayList<>();

    public FirebaseRestaurantListAdapter(FirebaseRecyclerOptions<RestaurantModel> options,
                                         Query ref, View.OnDragListener onStartDragListener, Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRestaurants.add(dataSnapshot.getValue(RestaurantModel.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRestaurants, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
//        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mRestaurants.remove(position);
        getRef(position).removeValue();
    }

    @Override
    protected void onBindViewHolder(final FirebaseRestaurantViewHolder holder, int position, RestaurantModel model) {
        holder.bindRestaurant(model);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public FirebaseRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_item_drag, parent, false);

        return new FirebaseRestaurantViewHolder(view);
    }

    public void setIndexInFirebase() {
        for (RestaurantModel restaurant : mRestaurants) {
            int index = mRestaurants.indexOf(restaurant);
            DatabaseReference ref = getRef(index);
            restaurant.setIndex(Integer.toString(index));
            ref.setValue(restaurant);
        }
    }
}
