package com.joss.mapforit;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;

public class MapstateManager {
	private static final String LONGITUDE="long";
	private static final String LATITUDE="lat";
	private static final String ZOOM="zoom";
	private static final String BEARING="bearing";
	private static final String TILT="tilt";
	private static final String MAPTYPE="maptype";


	private static final String PREF_NAME="mapCameraState";
	
	private SharedPreferences sharedPreferences;

	public MapstateManager(Context context) {
		sharedPreferences=context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
	}
	
	public void savemapstate(GoogleMap map) {
		SharedPreferences.Editor editor=sharedPreferences.edit();
		CameraPosition position=map.getCameraPosition();
		
		editor.putFloat(LATITUDE, (float) position.target.latitude);
		editor.putFloat(LONGITUDE, (float) position.target.longitude);
		editor.putFloat(ZOOM, position.zoom);
		editor.putFloat(BEARING, position.bearing);
		editor.putFloat(TILT, position.tilt);
		editor.putInt(MAPTYPE, map.getMapType());
		
		editor.commit();
	}
	public CameraPosition getSavedPosition() {
		double Latitude=sharedPreferences.getFloat(LATITUDE, 0);
		if (Latitude==0) {
			return null;
		} else {
			double Longitude=sharedPreferences.getFloat(LONGITUDE, 0);
			LatLng target=new LatLng(Latitude, Longitude);
			
			float zoom=sharedPreferences.getFloat(ZOOM, 0);
			float bearing=sharedPreferences.getFloat(BEARING, 0);
			float tilt=sharedPreferences.getFloat(TILT, 0);
			
			CameraPosition cameraPosition=new CameraPosition(target, zoom, tilt, bearing);
			return cameraPosition;
		}
	}


	
}
