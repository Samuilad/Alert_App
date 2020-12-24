package Winter.Project.Application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private Button button;
	public String phoneNumbers = "";
	public final int MY_PERMISSIONS_REQUESTS = 3;
	private FusedLocationProviderClient fusedLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		getLocation();
		phoneNumbers = readFile();
		//Checks permissions
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setMessage("These Permissions are required to send your contacts valuable information of potential situation")
						.setPositiveButton("Okay", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
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

	public String longitude = "";
	public String latitude = "";
	public boolean locationStatus = true;

	/**
	 * @return updates longitude and latitude variables.
	 */
	public Task getLocation() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setMessage("These Permissions are required to send your contacts valuable information of potential situation")
					.setPositiveButton("Okay", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
							MY_PERMISSIONS_REQUESTS))
					.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
			builder.show();
		}
		return fusedLocationClient.getLastLocation()
				.addOnSuccessListener(this, new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if (location != null) {
							longitude = String.valueOf(location.getLongitude());
							latitude = String.valueOf(location.getLatitude());
						} else {
							Toast.makeText(getApplicationContext(), "Invalid Location", Toast.LENGTH_LONG).show();
							locationStatus = false;
						}
					}
				});
	}

	/**
	 * @return returns phonenumbers.
	 */
	public String readFile() {
		try {
			FileInputStream fileInputStream = openFileInput("PhoneNumbers.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();

			String lines;
			while ((lines = bufferedReader.readLine()) != null) {
				stringBuffer.append(lines + "\n");
			}

			return stringBuffer.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}
	/**
	 * sends a SMS message.
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

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.test2) {
			String numbers = readFile();
			String[] numberList = numbers.split("/");
			getLocation();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					for (String number: numberList) {
						sendSMS(number, "Testing app right now" + "\n" + "https://www.google.com/maps/place/" + latitude + "%20" + longitude);
					}
				}
			}, 5000);



		}
		//starts up person activity.
		if (view.getId() == R.id.sendTo) {
			Intent intent = new Intent(this, PeopleActivity.class);
			startActivity(intent);
		}

	}
}