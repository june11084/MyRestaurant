package com.example.group.myrestaurant.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group.myrestaurant.R;
import com.example.group.myrestaurant.models.RestaurantModel;
import com.example.group.myrestaurant.util.ItemTouchHelperAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class FirebaseRestaurantListAdapter extends FirebaseRecyclerAdapter<RestaurantModel, FirebaseRestaurantViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private View.OnDragListener mOnStartDragListener;
    private Context mContext;
    private Query restaurantQuery;

    public FirebaseRestaurantListAdapter(FirebaseRecyclerOptions<RestaurantModel> options,
                                         Query ref, View.OnDragListener onStartDragListener, Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    protected void onBindViewHolder(FirebaseRestaurantViewHolder holder, int position, RestaurantModel model) {
        holder.bindRestaurant(model);
    }

    @Override
    public FirebaseRestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_item_drag, parent, false);

        return new FirebaseRestaurantViewHolder(view);
    }
}
