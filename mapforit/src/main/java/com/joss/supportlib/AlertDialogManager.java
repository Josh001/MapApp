package com.joss.supportlib;

import com.joss.mapforit.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
	 public void showAlertDialog(Context context, String title, String message,
	            Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle(title);
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(message);
	 
	        if(status != null)
	            // Setting alert dialog icon
	            alertDialog.setIcon((status) ? R.drawable.common_signin_btn_icon_dark : R.drawable.ic_launcher);
	        //first sucess 
	        //second fails
	        //check back
	        
	        // Setting OK Button
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }
}
