package com.joss.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MapBgServices extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			return super.onStartCommand(intent, flags, startId);
			
		}
	
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}
}
