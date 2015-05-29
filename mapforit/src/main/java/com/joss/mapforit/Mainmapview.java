package com.joss.mapforit;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Mainmapview extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMaps;
	MapView mview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (serviceOk()) {
			Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
			setContentView(R.layout.activity_mapview);

			mview=(MapView) findViewById(R.id.mapview);
			mview.onCreate(savedInstanceState);
			
		}else {
			setContentView(R.layout.activity_main);
		} 

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean serviceOk() {
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


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mview.onDestroy();
	}


	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mview.onLowMemory();
	}


	@Override
	protected void onPause() {
		super.onPause();
		mview.onLowMemory();
	}


	@Override
	protected void onResume() {
		super.onResume();
		mview.onResume();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mview.onSaveInstanceState(outState);
	}


	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
}
