package com.zscseh93;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zscseh93.data.Item;

/**
 * Created by zscse on 2016. 05. 13..
 */
public class ItemCreateFragment extends DialogFragment {

    public static final String TAG = "ItemCreateFragment";

    private EditText mEditItemName;

    private ItemContainer mItemContainer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mItemContainer = (ItemContainer) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View root = inflater.inflate(R.layout.item_create, container, false);

        getDialog().setTitle("Add new item (dialog title)");

        mEditItemName = (EditText) root.findViewById(R.id.newItemName);

        Button btnAddItem = (Button) root.findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item newItem = new Item(String.valueOf(mEditItemName.getText()));
                mItemContainer.addItem(newItem);
                dismiss();
            }
        });

        Button btnCancel = (Button) root.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }

    public interface ItemContainer {
        void addItem(Item item);
    }
}
