package com.joss.mapforit;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class MapSingle extends Activity {
	
	GoogleMap mMaps;
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	private static final int DEFAULTZOOM = 15;

	String Long,Lat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (serviceOk()) {
			setContentView(R.layout.activity_map_single);

			Bundle i=getIntent().getExtras();
			Long=i.getString("mylongitude");
			Lat=i.getString("mylatitude");
			
				Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
				//gotoLocation(Double.parseDouble(Lat),Double.parseDouble(Long),DEFAULTZOOM);
				//mMaps.setMyLocationEnabled(true);
		}else {
			setContentView(R.layout.activity_main);
		} 
	}
	private boolean serviceOk() {
		int isAvailable=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (isAvailable==ConnectionResult.SUCCESS) {
			return true;
		} else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog=GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}else {
			Toast.makeText(this,"Can't connect to google play", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private void gotoLocation(double lat, double lng,
			float zoom) {
		LatLng ll=new LatLng(lat, lng);
		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMaps.moveCamera(update);

	}
	}

