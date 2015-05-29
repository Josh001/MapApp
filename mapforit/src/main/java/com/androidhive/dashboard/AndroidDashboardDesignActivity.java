package com.androidhive.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.joss.ListPlaces.PlacesActivity;
import com.joss.mapforit.MainActivity;
import com.joss.mapforit.R;

public class AndroidDashboardDesignActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);

		Button bMyproject=(Button) findViewById(R.id.btn_myproject);
		Button blistplaces=(Button) findViewById(R.id.btn_listplaces);
		Button babout=(Button) findViewById(R.id.btn_about);
		Button bgoogle=(Button) findViewById(R.id.btn_googleabout);
		Button bexButton=(Button) findViewById(R.id.btn_exit);
		Button bplaces=(Button) findViewById(R.id.btn_places);
		
		
		bMyproject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent fint=new Intent(getApplicationContext(), MainActivity.class);
				startActivity(fint);
			}
		});
		blistplaces.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AndroidDashboardDesignActivity.this, PlacesActivity.class);
				startActivity(intent);
				Log.d("JOSH", "done");				
			}
		});
		bexButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					close();

			}
		});

	}

	@Override
	public void onBackPressed() {
		  close();
	}

	private void close() {
		AlertDialog.Builder builder=new AlertDialog.Builder(AndroidDashboardDesignActivity.this);
	        builder.setMessage("Are you sure you want to exit?");
	        builder.setCancelable(false);
	        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                AndroidDashboardDesignActivity.this.finish();
	            }
	        });
	        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.cancel();
	            }
	        });
	        AlertDialog alert=builder.create();
	        alert.show();
	}
	
}
