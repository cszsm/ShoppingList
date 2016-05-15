package com.zscseh93;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        TextView tvPrice = (TextView) rootView.findViewById(R.id.tvPrice);
        String price = mItem.getPrice() + " Ft";
        tvPrice.setText(price);
//        if (mItem != null) {
        if (mItem.getPlaceName() != null) {
            TextView tvPlaceName = (TextView) rootView.findViewById(R.id.tvPlaceName);
            tvPlaceName.setText(mItem.getPlaceName());
        }

        if (mItem.getPlaceAddress() != null) {
            TextView tvPlaceAddress = (TextView) rootView.findViewById(R.id.tvPlaceAddress);
            tvPlaceAddress.setText(mItem.getPlaceAddress());
        }

        if (mItem.getPlaceLatLng() != null) {
            mMapView = (MapView) rootView.findViewById(R.id.mapview);
            mMapView.onCreate(savedInstanceState);

            GoogleMapReady googleMapReady = new GoogleMapReady();
            mMapView.getMapAsync(googleMapReady);
        }

        if (mItem.getPhotoFileName() != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            File file = null;
            File storageDir = getActivity().getExternalFilesDir(null);
            File[] asd = storageDir.listFiles();
            for (File s :
                    asd) {
                if (s.getName().contains(mItem.getPhotoFileName())) {
                    file = s;
                }
            }

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()/*mItem.getPhotoFileName() + "1088038940.jpg"*/, options);





            ImageView imageView = (ImageView) rootView.findViewById(R.id.photo);
            imageView.setImageBitmap(bitmap);
//            imageView.setVisibility(View.VISIBLE);
            imageView.requestLayout();
            Log.d("Sdfsd", String.valueOf(imageView.isShown()));
        }

//        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
