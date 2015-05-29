package com.joss.ListPlaces;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joss.mapforit.GPSTracker;
import com.joss.mapforit.GooglePlaces;
import com.joss.mapforit.Place;
import com.joss.mapforit.PlacesList;
import com.joss.mapforit.R;
import com.joss.supportlib.AlertDialogManager;
import com.joss.supportlib.ConnectionDetector;

public class PlacesActivity extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// Places List
	PlacesList nearPlaces;	

	//adapter fot the list
	ArrayAdapter<String> adapter2;

	// GPS Location
	GPSTracker gps;



	// Progress dialog
	ProgressDialog pDialog;


	// Places Listview
	ListView lv;

	Spinner s1;
	String[] typesme={"atm","hospital","cafe","resturants","gas_station","police"};

	String alltypes="atm";

	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();


	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);

		cd=new ConnectionDetector(PlacesActivity.this);


		s1=(Spinner) findViewById(R.id.spinner1);


		ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(PlacesActivity.this, android.R.layout.simple_spinner_dropdown_item,typesme);
		s1.setAdapter(arrayAdapter);
		//cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(PlacesActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		//My GPSTRACKER CLASS
		gps=new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
		} else {
			// Can't get user's current location
			alert.showAlertDialog(PlacesActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}



		lv = (ListView) findViewById(R.id.list);

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview

		s1.setOnItemSelectedListener(new OnItemSelectedListener() {


			//worked here
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				alltypes=s1.getSelectedItem().toString();
				adapter2=new ArrayAdapter<String>(PlacesActivity.this, android.R.layout.simple_list_item_1, new String[]{});
				lv.setAdapter(adapter2);
				Log.d("DEBUG", alltypes);
				new LoadPlaces().execute();


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SinglePlaceActivity.class);	

				// Sending place refrence id to single place activity
				// place refrence id used to get "Place full details"
				in.putExtra(KEY_REFERENCE, reference);
				startActivity(in);
			}
		});
	}
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		private static final String DEBUG = "no Error";

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlacesActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				// 
				//alltypes = "cafe|restaurant"; // Listing places only cafes, restaurants
				//String types = null;
				// Radius in meters - increase this value if you don't find any places
				double radius = 2000; // 1000 meters 

				// get nearest places
				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), radius, alltypes);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

			try {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed Places into LISTVIEW
						 * */
						// Get json response status
						String status = nearPlaces.status;


						// Check for all possible status
						if(status.equals("OK")){
							// Successfully got places details
							if (nearPlaces.results != null) {
								// loop through each place
								for (Place p : nearPlaces.results) {
									HashMap<String, String> map = new HashMap<String, String>();

									// Place reference won't display in listview - it will be hidden
									// Place reference is used to get "place full details"
									map.put(KEY_REFERENCE, p.reference);

									// Place name
									map.put(KEY_NAME, p.name);


									// adding HashMap to ArrayList
									placesListItems.add(map);	
								}
								// list adapter
								ListAdapter adapter = new SimpleAdapter(PlacesActivity.this, placesListItems,
										R.layout.list_items,
										new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
										R.id.reference, R.id.name });

								// Adding data into listview
								lv.setAdapter(adapter);
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							// Zero results found
							alert.showAlertDialog(PlacesActivity.this, "Near Places",
									"Sorry no places found. Try to change the types of places",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(PlacesActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(PlacesActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(PlacesActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(PlacesActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}else if (status.isEmpty()) {
							alert.showAlertDialog(PlacesActivity.this, "Connection", "Ensure Stable internet", false);
						}
						else
						{
							alert.showAlertDialog(PlacesActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}
				});
			} catch (Exception e) {
				Toast.makeText(PlacesActivity.this, "This app requires constant internet connection", Toast.LENGTH_LONG).show();
			}	
		}
	}


}



