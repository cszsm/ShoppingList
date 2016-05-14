package com.zscseh93;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.zscseh93.data.Item;

/**
 * Created by zscse on 2016. 05. 13..
 */
public class ItemCreateFragment extends DialogFragment {

    public static final String TAG = "ItemCreateFragment";

    private EditText mEditItemName;

    private ItemContainer mItemContainer;

    private View mRoot;

    private Place mLastPlace = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mItemContainer = (ItemContainer) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRoot = inflater.inflate(R.layout.item_create, container, false);

        getDialog().setTitle("Add new item");

        mEditItemName = (EditText) mRoot.findViewById(R.id.newItemName);

        Button btnAddItem = (Button) mRoot.findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item newItem = new Item(String.valueOf(mEditItemName.getText()));

                if (mLastPlace != null) {
                    newItem.setPlace(mLastPlace.getId(), String.valueOf(mLastPlace.getName()), String.valueOf(mLastPlace.getAddress()));
                    Log.d(TAG, String.valueOf(mLastPlace.getName()));
                }
//                mItemContainer.addItem(newItem);
                newItem.save();
                mItemContainer.update();
                dismiss();
            }
        });

        Button btnCancel = (Button) mRoot.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btnChoosePlace = (Button) mRoot.findViewById(R.id.btnChoosePlace);
        btnChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = intentBuilder.build(getActivity());
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                int PLACE_PICKER_REQUEST = 1;
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        return mRoot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Place place = PlacePicker.getPlace(getActivity(), data);
        if (place == null) {
            return;
        }

        mLastPlace = place;
        TextView tvChoosePlace = (TextView) mRoot.findViewById(R.id.tvChoosePlace);
        tvChoosePlace.setText(place.getName());
    }

    public interface ItemContainer {
        void update();
    }
}
