package Winter.Project.Application;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.telephony.SmsManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button button;
public final int MY_PERMISSIONS_REQUESTS = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Checks permissions
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				||ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setMessage("These Permissions are required to send your contacts valuable information of potential situation")
						.setPositiveButton("Okay", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
								MY_PERMISSIONS_REQUESTS))
						.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
				builder.show();
			}
		}

		/*Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				sendSMS();
			}
		}, 5000); */

		setContentView(R.layout.activity_main);
		//Buttons
		button = findViewById(R.id.sendTo);
		button.setOnClickListener(this);
		button = findViewById(R.id.test2);
		button.setOnClickListener(this);
	}
	/*@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case MY_PERMISSIONS_REQUESTS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// this if loop will give grant status for Contacts
				} else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
					// grant status for Location}
				}
			}
		}
	}*/

	/**
	 * sends a
	 * @param phoneNumber message receiver.
	 * @param message message.
	 */
	public void sendSMS(String phoneNumber, String message) {
		try {
			SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
		} catch (Exception e) {
			AlertDialog.Builder alertDialogBuilder = new
					AlertDialog.Builder(this);
			AlertDialog dialog = alertDialogBuilder.create();


			dialog.setMessage(e.getMessage());


			dialog.show();


		}
	}
	/**
	 * sends and SMS message.
	 * @param view idk.
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.test2) {
			sendSMS("6304646531", "Sent from app");
		}
		//starts up person activity.
		if (view.getId() == R.id.sendTo) {
			Intent intent = new Intent(this, PeopleActivity.class);
			startActivity(intent);
		}

	}
}