package com.zscseh93;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zscseh93.data.Item;

import java.io.File;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item";

    private Item mItem;

    private MapView mMapView;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
                    .findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView tvQuantity = (TextView) rootView.findViewById(R.id.tvQuantity);
        String quantity = String.valueOf(mItem.getQuantity());
        tvQuantity.setText(quantity);

        TextView tvPrice = (TextView) rootView.findViewById(R.id.tvPrice);
        if (mItem.getPrice() == 0) {
            tvPrice.setText("unknown");
            tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.secondaryText));
        } else {
            String price = mItem.getPrice() + " Ft";
            tvPrice.setText(price);
            tvPrice.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryText));
        }

        LinearLayout others = (LinearLayout) rootView.findViewById(R.id.otherDetails);

        TextView tvPlaceName = (TextView) rootView.findViewById(R.id.tvPlaceName);
        if (mItem.getPlaceName() != null) {
            tvPlaceName.setText(mItem.getPlaceName());
            tvPlaceName.setTextColor(ContextCompat.getColor(getActivity(), R.color.primaryText));

            showMap(others, savedInstanceState);
        } else {
            tvPlaceName.setText("unknown");
            tvPlaceName.setTextColor(ContextCompat.getColor(getActivity(), R.color.secondaryText));
        }


//        if (mItem.getPhotoFileName() != null) {
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//            File file = null;
//            File storageDir = getActivity().getExternalFilesDir(null);
//            File[] asd = storageDir.listFiles();
//            for (File s :
//                    asd) {
//                if (s.getName().contains(mItem.getPhotoFileName())) {
//                    file = s;
//                }
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()/*mItem.getPhotoFileName() + "1088038940.jpg"*/, options);
//
//
//            ImageView imageView = (ImageView) rootView.findViewById(R.id.photo);
//
//            imageView.setImageBitmap(bitmap);
////            imageView.setVisibility(View.VISIBLE);
//            imageView.requestLayout();
//        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    private void showMap(LinearLayout layout, Bundle savedInstanceState) {
        mMapView = new MapView(getActivity());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        mMapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        mMapView.onCreate(savedInstanceState);

        GoogleMapReady googleMapReady = new GoogleMapReady();
        mMapView.getMapAsync(googleMapReady);

        layout.addView(mMapView);
    }

    private class GoogleMapReady implements OnMapReadyCallback {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mItem.getPlaceLatLng(), 16.0f));
            googleMap.addMarker(new MarkerOptions().position(mItem.getPlaceLatLng()).title(mItem.getPlaceName()));
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }
}
