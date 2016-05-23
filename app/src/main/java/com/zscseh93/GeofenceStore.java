package com.zscseh93;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zscse on 2016. 05. 15..
 */
public class GeofenceStore implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, ResultCallback<Status>, LocationListener {

    private static final String LOG_TAG = GeofenceStore.class.getCanonicalName();

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private List<Geofence> mGeofences;
    private GeofencingRequest mGeofencingRequest;
    private LocationRequest mLocationRequest;

    public GeofenceStore(Context context, List<Geofence> geofences) {
        mContext = context;
        mGeofences = new ArrayList<>(geofences);
        mPendingIntent = null;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isNotificationEnabled = sharedPreferences.getBoolean("pref_notifications", true);

        if (isNotificationEnabled) {
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mGeofences.isEmpty()) {
            return;
        }
        mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(mGeofences).build();

        mPendingIntent = createRequestPendingIntent();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission
                .ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences
                (mGoogleApiClient, mGeofencingRequest, mPendingIntent);

        pendingResult.setResultCallback(this);
    }

    private PendingIntent createRequestPendingIntent() {
        if (mPendingIntent == null) {
            Log.v(LOG_TAG, "Creating PendingIntent");
            Intent intent = new Intent(mContext, GeofenceTransitionsIntentService.class);
            mPendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent
                    .FLAG_UPDATE_CURRENT);
        }

        return mPendingIntent;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(LOG_TAG, "Connection suspended.");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(LOG_TAG, "Location Information\n"
                + "==========\n"
                + "Provider:\t" + location.getProvider() + "\n"
                + "Lat & Long:\t" + location.getLatitude() + ", "
                + location.getLongitude() + "\n"
                + "Altitude:\t" + location.getAltitude() + "\n"
                + "Bearing:\t" + location.getBearing() + "\n"
                + "Speed:\t\t" + location.getSpeed() + "\n"
                + "Accuracy:\t" + location.getAccuracy() + "\n");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(LOG_TAG, "Connection failed.");
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.v(LOG_TAG, "Success!");
        } else if (status.hasResolution()) {
            Log.v(LOG_TAG, "Resolution");
        } else if (status.isCanceled()) {
            Log.v(LOG_TAG, "Canceled");
        } else if (status.isInterrupted()) {
            Log.v(LOG_TAG, "Interrupted");
        }
    }

    public void enable() {
        mGoogleApiClient.disconnect();
    }

    public void disable() {
        mGoogleApiClient.connect();
    }
}
