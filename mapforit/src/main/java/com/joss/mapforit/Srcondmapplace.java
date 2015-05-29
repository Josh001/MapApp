package com.joss.mapforit;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kaplandroid.shortestpathdirection.googlemaps.GMapV2Direction;
import com.kaplandroid.shortestpathdirection.googlemaps.GMapV2Direction.DirecitonReceivedListener;
import com.kaplandroid.shortestpathdirection.googlemaps.GetRotueListTask;

public class Srcondmapplace extends android.support.v4.app.FragmentActivity
implements OnClickListener,OnInfoWindowClickListener,DirecitonReceivedListener {
	
	private GoogleMap mMap;
	private Button btnDirection;

	LatLng startPosition;
	String startPositionTitle;
	String startPositionSnippet;

	LatLng destinationPosition;
	String destinationPositionTitle;
	String destinationPositionSnippet;
	
	ToggleButton tbMode;

	GPSTracker gpstrack;

	GoogleMap mymap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_srcondmapplace);
		
		gpstrack=new GPSTracker(Srcondmapplace.this);
		double latitudesource=gpstrack.getLatitude();
		double longitudesource=gpstrack.getLongitude();
		
		//destination address
		Bundle extras=getIntent().getExtras();
		Long latitudedest=extras.getLong("latitude");
		Long longitudedest=extras.getLong("longitude");
		
		Log.d("MAPERROR", latitudedest.toString());
		Log.d("MAPERROR2", longitudedest.toString());
		

		
		
		//source
		startPosition = new LatLng(latitudesource,longitudesource);
		startPositionTitle = "Taksim Square";
		startPositionSnippet = "Istanbul / Turkey";

		destinationPosition = new LatLng(latitudedest, longitudedest);
		destinationPositionTitle = "Sultanahmet Mosque, Istanbul";
		destinationPositionSnippet = "Istanbul / Turkey";
		
		btnDirection = (Button) findViewById(R.id.btnDirection);
		btnDirection.setOnClickListener(this);
		
		tbMode = (ToggleButton) findViewById(R.id.tbMode);

		tbMode.setChecked(true);

		setUpMapIfNeeded();
	}
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setMyLocationEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setAllGesturesEnabled(true);

		mMap.setOnInfoWindowClickListener(Srcondmapplace.this);

	}
	public void clearMap() {
		mMap.clear();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void OnDirectionListReceived(List<LatLng> mPointList) {
		if (mPointList != null) {
			PolylineOptions rectLine = new PolylineOptions().width(10).color(
					Color.RED);
			for (int i = 0; i < mPointList.size(); i++) {
				rectLine.add(mPointList.get(i));
			}
			mMap.addPolyline(rectLine);

			CameraPosition mCPFrom = new CameraPosition.Builder()
					.target(startPosition).zoom(15.5f).bearing(0).tilt(25)
					.build();
			final CameraPosition mCPTo = new CameraPosition.Builder()
					.target(destinationPosition).zoom(15.5f).bearing(0)
					.tilt(50).build();

			changeCamera(CameraUpdateFactory.newCameraPosition(mCPFrom),
					new CancelableCallback() {
						@Override
						public void onFinish() {
							changeCamera(CameraUpdateFactory
									.newCameraPosition(mCPTo),
									new CancelableCallback() {

										@Override
										public void onFinish() {

											LatLngBounds bounds = new LatLngBounds.Builder()
													.include(startPosition)
													.include(
															destinationPosition)
													.build();
											changeCamera(
													CameraUpdateFactory
															.newLatLngBounds(
																	bounds, 50),
													null, false);
										}

										@Override
										public void onCancel() {
										}
									}, false);
						}

						@Override
						public void onCancel() {
						}
					}, true);
		}
		
	}
	/**
	 * Change the camera position by moving or animating the camera depending on
	 * input parameter.
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback,
			boolean instant) {

		if (instant) {
			mMap.animateCamera(update, 1, callback);
		} else {
			mMap.animateCamera(update, 4000, callback);
		}
	}


	@Override
	public void onClick(View v) {
		if (v == btnDirection) {
			clearMap();

			MarkerOptions mDestination = new MarkerOptions()
					.position(destinationPosition)
					.title(destinationPositionTitle)
					.snippet(destinationPositionSnippet)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red));

			MarkerOptions mStart = new MarkerOptions()
					.position(startPosition)
					.title(startPositionTitle)
					.snippet(startPositionSnippet)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue));

			mMap.addMarker(mDestination);
			mMap.addMarker(mStart);

			if (tbMode.isChecked()) {
				new GetRotueListTask(Srcondmapplace.this, startPosition,
						destinationPosition, GMapV2Direction.MODE_DRIVING, this)
						.execute();
			} else {
				new GetRotueListTask(Srcondmapplace.this, startPosition,
						destinationPosition, GMapV2Direction.MODE_WALKING, this)
						.execute();
			}
		}

		
	}
	@Override
	public void onInfoWindowClick(Marker selectedMarker) {

		if (selectedMarker.getTitle().equals(startPositionTitle)) {
			Toast.makeText(this, "Marker Clicked: " + startPositionTitle,
					Toast.LENGTH_LONG).show();
		} else if (selectedMarker.getTitle().equals(destinationPositionTitle)) {
			Toast.makeText(this, "Marker Clicked: " + destinationPositionTitle,
					Toast.LENGTH_LONG).show();
		}
		selectedMarker.hideInfoWindow();

			
	}
}
