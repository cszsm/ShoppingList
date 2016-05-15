package com.zscseh93;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.zscseh93.data.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements ItemCreateFragment
        .ItemContainer {

    private static final String LOG_TAG = "ItemListActivity";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    //    private List<Item> mItems;
    private SimpleItemRecyclerViewAdapter mItems;

    private ItemTouchHelper mItemTouchHelper;

    private GoogleMap mMap;
    private List<Geofence> mGeofences;
    private GeofenceStore mGeofenceStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitle(getTitle());

        mItems = new SimpleItemRecyclerViewAdapter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemCreateFragment itemCreateFragment = new ItemCreateFragment();
                FragmentManager fragmentManager = getFragmentManager();
                itemCreateFragment.show(fragmentManager, ItemCreateFragment.TAG);
            }
        });

        final View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mItems.onItemDismiss(viewHolder.getAdapterPosition());
            }
        });
        mItemTouchHelper.attachToRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        update();

        mGeofences = new ArrayList<>();

        for (Item i :
                mItems.mValues) {

            mGeofences.add(new Geofence.Builder()
                    .setRequestId(i.getName())
                    .setCircularRegion(i.getPlaceLatLng().latitude, i.getPlaceLatLng().longitude,
                            1000)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }

        mGeofenceStore = new GeofenceStore(this, mGeofences);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mItems.update();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mItems);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void update() {
        mItems.update();
        updateSum();
    }

    public void updateSum() {
        int sum = 0;
        for (Item item : mItems.mValues) {
            sum += item.getQuantity() * item.getPrice();
        }
        TextView tvSum = (TextView) findViewById(R.id.tvSum);
        tvSum.setText(sum + " Ft");
    }

//    @Override
//    public void addItem(Item item) {
//        mItems.addItem(item);
//        mItems.notifyDataSetChanged();
//    }

    private interface ItemTouchHelperAdapter {
        void onItemDismiss(int position);
    }

    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements
            ItemTouchHelperAdapter {

        private List<Item> mValues;

        public SimpleItemRecyclerViewAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            assert holder.mItem != null;
            holder.mNameView.setText(holder.mItem.getName());
            holder.mQuantityView.setText(String.valueOf(holder.mItem.getQuantity()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(ItemDetailFragment.ARG_ITEM, holder.mItem);

                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void update() {
            mValues = Item.listAll(Item.class);
            notifyDataSetChanged();
        }

        @Override
        public void onItemDismiss(int position) {
            mValues.get(position).delete();
            mValues.remove(position);
            notifyItemRemoved(position);

            updateSum();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mQuantityView;
            public Item mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mQuantityView = (TextView) view.findViewById(R.id.quantity);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
