package com.joss.mapforit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnClickListener
{
	private static final String DEBUGER = "DEBUGER";
	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	//Button b1;
	GoogleMap mMaps;
	private static double Location1Lat=102.3344,
			Location1Lng=23.4556;
	private static float DEFAULTZOOM=15;	

	//Marker
	Marker marker;

	GPSTracker gpstrack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setUpMapIfNeeded();
//		if (serviceOk()) {
//			if (init()) {
//				Toast.makeText(this,"Ready to Map", Toast.LENGTH_SHORT).show();
//				gotoLocation(Location1Lat, Location1Lng,DEFAULTZOOM);
//				//mMaps.setMyLocationEnabled(true);
//
//
//			} else {
//				Toast.makeText(this,"Map not available", Toast.LENGTH_SHORT).show();
//			}
//		}else {
//			setContentView(R.layout.activity_main);
//		} 
		//b1=(Button) findViewById(R.id.button1);
		//b1.setOnClickListener(this);
	}
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMaps == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMaps = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMaps != null) {
				setUpMap();
			}
		}
	}
	private void setUpMap() {

		mMaps.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMaps.setMyLocationEnabled(true);
		mMaps.setIndoorEnabled(true);
		mMaps.getUiSettings().setZoomControlsEnabled(true);
		mMaps.getUiSettings().setMyLocationButtonEnabled(true);
		mMaps.getUiSettings().setCompassEnabled(true);
		mMaps.getUiSettings().setAllGesturesEnabled(true);
	}
	public void clearMap() {
		mMaps.clear();
	}

	private void gotoLocation(double lat, double lng,
			float zoom) {
		LatLng ll=new LatLng(lat, lng);
		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMaps.moveCamera(update);

	}




	private void hidescreenkey(View v) {
		InputMethodManager imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//To test for googleplay availability
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

	//To test for map  availability
//	private boolean init() {
//
//		if (mMaps==null) {
//			SupportMapFragment fragment=
//					(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//			mMaps=fragment.getMap();
//			if (mMaps!=null) {
//				mMaps.setInfoWindowAdapter(new InfoWindowAdapter() {
//
//					@Override
//					public View getInfoWindow(Marker arg0) {
//						return null;
//					}
//
//					@Override
//					public View getInfoContents(Marker marker) {
//
//						View view=getLayoutInflater().inflate(R.layout.info_window, null);
//						TextView tvlat=(TextView) findViewById(R.id.tv_lat);
//						TextView tvlngy=(TextView) findViewById(R.id.tv_lng);
//						TextView tvSnippet=(TextView) findViewById(R.id.tv_snippet);
//
//						LatLng ll=marker.getPosition();
//
//						tvlat.setText("Latitude"+ll.latitude);
//						tvlngy.setText("Latitude"+ll.longitude);
//						tvSnippet.setText(marker.getSnippet());
//
//
//						return view;
//					}
//				});
//				mMaps.setOnMapLongClickListener(new OnMapLongClickListener() {
//
//					@Override
//					public void onMapLongClick(LatLng ll) {
//						Geocoder geocoder=new Geocoder(MainActivity.this);
//						List<Address> list=null;
//						try {
//							list=geocoder.getFromLocation(ll.latitude, ll.longitude, 1);
//						} catch (IOException e) {
//							e.printStackTrace();
//							return;
//						}
//						Address add=list.get(0);
//						MainActivity.this.setMarker(ll.latitude, add.getCountryName(), ll.longitude);
//					}
//				});
//				mMaps.setOnMarkerClickListener(new OnMarkerClickListener() {
//
//					@Override
//					public boolean onMarkerClick(Marker marker) {
//						String msg=marker.getTitle()+" ("+marker.getPosition().latitude+
//								" , " + marker.getPosition().longitude + "}";
//						Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
//						return false;
//					}
//				});
//			}
//		} 
//		return(mMaps!=null);
//	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.mapTypeNormal:
			mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);		
			break;
		case R.id.mapTypeSatellite:
			mMaps.setMapType(GoogleMap.MAP_TYPE_SATELLITE);		
			break;	
		case R.id.terrain:
			mMaps.setMapType(GoogleMap.MAP_TYPE_TERRAIN);		
			break;
		case R.id.mapTypeHybrid:
			mMaps.setMapType(GoogleMap.MAP_TYPE_HYBRID);		
			break;	
		case R.id.Locate:
			gpstrack=new GPSTracker(MainActivity.this);

			if (gpstrack.canGetLocation()) {
				double latitude=gpstrack.getLatitude();
				double longitude=gpstrack.getLongitude();
				Toast.makeText(getApplicationContext(),
						"Your Location is - \nLat: " 
								+ latitude + "\nLong: "
								+ longitude,Toast.LENGTH_LONG).show(); 
				gotoLocation(latitude, longitude, DEFAULTZOOM);
				MarkerOptions markerOptions=new MarkerOptions().
						title("Current Positon")
						.position(new LatLng(latitude, longitude));
				mMaps.addMarker(markerOptions);

			} else {
				gpstrack.showSettingsAlert();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onStop() {
		super.onStop();
		MapstateManager manager=new MapstateManager(getApplicationContext());
		manager.savemapstate(mMaps);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MapstateManager manager=new MapstateManager(getApplicationContext());
		CameraPosition position=manager.getSavedPosition();
		if (position !=null) {
			CameraUpdate update=CameraUpdateFactory.newCameraPosition(position);
			mMaps.animateCamera(update);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.button1:
//			EditText et=(EditText) findViewById(R.id.edit1);
//			String Location=et.getText().toString();
//			new ValidateAddress(getApplicationContext(), this, true).validate(Location);
//			break;
		}		
	}


//	private void searchit(View v) throws IOException {
//		hidescreenkey(v);
//
//		EditText et=(EditText) findViewById(R.id.edit1);
//		String Location=et.getText().toString();
//
//		Geocoder geocoder=new Geocoder(MainActivity.this);
//		List<Address> list=geocoder.getFromLocationName(Location, 1);
//		Address address=list.get(0);
//		String Locality=address.getLocality();
//
//		String country=address.getCountryName();
//
//		Toast.makeText(getApplicationContext(),Locality, Toast.LENGTH_SHORT).show();
//		double lat1=address.getLatitude();
//		double lng1=address.getLongitude();
//
//		Log.d(DEBUGER,Locality);
//		gotoLocation(lat1, lng1, DEFAULTZOOM);
//
//		setMarker(lat1,country ,lng1);
//
//	}


	private void setMarker(double lat1,String country , double lng1) {
		if (marker!=null) {
			marker.remove();
		}
		MarkerOptions markerOptions=new MarkerOptions().
				title("Current Positon")
				//				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker))
				.position(new LatLng(lat1, lng1));
		if (country.length()>0) {
			markerOptions.snippet(country);
		}
		marker=mMaps.addMarker(markerOptions);
	}
}
