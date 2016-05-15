package com.zscseh93;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.zscseh93.data.Item;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zscse on 2016. 05. 13..
 */
public class ItemCreateFragment extends DialogFragment {

    public static final String TAG = "ItemCreateFragment";
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private EditText mEditItemName;
    private EditText mEditItemPrice;
    private ItemContainer mItemContainer;
    private View mRoot;
    private Place mLastPlace = null;
    private String mLastImageFileName = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mItemContainer = (ItemContainer) activity;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mRoot = inflater.inflate(R.layout.item_create, container, false);

        getDialog().setTitle("Add new item");

        mEditItemName = (EditText) mRoot.findViewById(R.id.newItemName);
        mEditItemPrice = (EditText) mRoot.findViewById(R.id.newItemPrice);

        Button btnAddItem = (Button) mRoot.findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item newItem = new Item(String.valueOf(mEditItemName.getText()));
                newItem.setPrice(Integer.parseInt(String.valueOf(mEditItemPrice.getText())));

                if (mLastPlace != null) {
                    newItem.setPlace(String.valueOf(mLastPlace.getName()), String.valueOf
                            (mLastPlace.getAddress()), mLastPlace.getLatLng());
                    Log.d(TAG, String.valueOf(mLastPlace.getName()));

//                    Geofence geofence = new Geofence.Builder().setRequestId(mLastPlace.getId())
//                            .setCircularRegion(mLastPlace.getLatLng().latitude, mLastPlace
//                                    .getLatLng().longitude, 1000)
//                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).
//                    setExpirationDuration(Geofence.NEVER_EXPIRE).build();
//
//                    GeofencingRequest geofencingRequest = getGeofencingRequest(geofence);
//                    PendingIntent pendingIntent = getGeofencePendingIntent();
//
//                    GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this);
//
//                    LocationServices.GeofencingApi.addGeofences(, geofencingRequest, pendingIntent);
                }

                if (mLastImageFileName != null) {
                    newItem.setPhotoFileName(mLastImageFileName);
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
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        Button btnTakeAPhoto = (Button) mRoot.findViewById(R.id.btnTakeAPhoto);
        btnTakeAPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File imageFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (imageFile != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    }
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        return mRoot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            Place place = PlacePicker.getPlace(getActivity(), data);
            if (place == null) {
                return;
            }

            mLastPlace = place;
            TextView tvChoosePlace = (TextView) mRoot.findViewById(R.id.tvChoosePlace);
            tvChoosePlace.setText(place.getName());
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mLastImageFileName = "JPEG_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(null);

        return File.createTempFile(mLastImageFileName, ".jpg", storageDir);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public interface ItemContainer {
        void update();
    }
}
