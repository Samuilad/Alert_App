package Winter.Project.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.emergency.EmergencyNumber;
import android.view.View;
import android.widget.Button;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

//TO DO
//Add custom situation functionality
//Make the UI look better
//test if message is sent when phone is off
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private Button button;
	public String phoneNumbers = "";
	public final int MY_PERMISSIONS_REQUESTS = 1;
	private FusedLocationProviderClient fusedLocationClient;
	private Handler handler = new Handler();
	public String longitude = "";
	public String latitude = "";
	public boolean locationStatus = true;
	public String emergencyMessage = "I might be in a possible emergency situation. Please check up on me";
	public int emergencyTimer = 30;
	private String customMessage = "";
	private int customTimer = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		getLocation();
		if (readFile("emergencyTimer.txt").equals("")) {
			writeFile(String.valueOf(emergencyTimer),"emergencyTimer.txt");
		}
		phoneNumbers = readFile("PhoneNumbers.txt");
		//Checks permissions
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setMessage("These Permissions are required to send your contacts valuable information of potential situation. Please enable SMS permission for this App")
						.setPositiveButton("Okay", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS/*, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION*/},
								MY_PERMISSIONS_REQUESTS))
						.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
				builder.show();
			}
		}

		setContentView(R.layout.activity_main);
		//Buttons
		button = findViewById(R.id.changePrefs);
		button.setOnClickListener(this);
		button = findViewById(R.id.sendTo);
		button.setOnClickListener(this);
		button = findViewById(R.id.Emergency);
		button.setOnClickListener(this);
		button = findViewById(R.id.Custom);
		button.setOnClickListener(this);
		button = findViewById(R.id.immediately);
		button.setOnClickListener(this);
		button = findViewById(R.id.imOkay);
		button.setOnClickListener(this);
		button = findViewById(R.id.changeEmergency);
		button.setOnClickListener(this);
		button = findViewById(R.id.changeYes);
		button.setOnClickListener(this);
		button = findViewById(R.id.changeNo);
		button.setOnClickListener(this);
		button = findViewById(R.id.finish);
		button.setOnClickListener(this);
		button = findViewById(R.id.checkMessage);
		button.setOnClickListener(this);
		button = findViewById(R.id.changeCustom);
		button.setOnClickListener(this);
	}

	/*
	@Override
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
	 * turns main screen on or off.
	 * @param onOrOff
	 */
	public void mainScreenOkayOrNot(String onOrOff) {
		LinearLayout mainScreen2 = findViewById(R.id.mainScreen2);
		if (onOrOff.equals("On")) {
			mainScreen2.setVisibility(View.VISIBLE);
		} else if (onOrOff.equals("Off")) {
			mainScreen2.setVisibility(View.GONE);
		}
	}

	/**
	 * turns main screen on or off.
	 * @param onOrOff request for screen to be on or off.
	 */
	public void mainScreen(String onOrOff) {
		Button custom = findViewById(R.id.Custom);
		Button emergency = findViewById(R.id.Emergency);
		Button sendTo = findViewById(R.id.sendTo);
		Button changeEmeregencySettings = findViewById(R.id.changeEmergency);
		if (onOrOff.equals("Off")) {
			custom.setVisibility(View.GONE);
			emergency.setVisibility(View.GONE);
			sendTo.setVisibility(View.GONE);
			changeEmeregencySettings.setVisibility(View.GONE);
		} else if (onOrOff.equals("On")) {
			custom.setVisibility(View.VISIBLE);
			emergency.setVisibility(View.VISIBLE);
			sendTo.setVisibility(View.VISIBLE);
			changeEmeregencySettings.setVisibility(View.VISIBLE);
		}
	}
	/*
	/**
	 * changes the message for the selected type.
	 * @param type custom or emergency.
	 */
	/*
	public void changeMessageOrDelay(String type) {
		if (type.equals("Emergency")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setMessage("would you like to change the default emergency message or delay?")
					.setPositiveButton("Yes", ((dialog, which) -> {
						//opens up new horizontal layout with boxes
					}))
					.setNegativeButton("No", ((dialog, which) -> {
						dialog.dismiss();
						//make everything else visible
					}));
			builder.show();
		}
		if (type.equals("Custom")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setMessage("would you like to change the default emergency message or delay?")
					.setPositiveButton("Yes", ((dialog, which) -> {
						//opens up new horizontal layout with boxes
					}))
					.setNegativeButton("No", ((dialog, which) -> {
						dialog.dismiss();
						//make everything else visible
					}));
			builder.show();
		}
	}
	*/

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
	 * @return CountDownTimer object.
	 */
	public CountDownTimer getTimer(String type) {
		TextView countDown =  findViewById(R.id.countDown);
		long timer = Long.parseLong(readFile(type).trim()) * 1000 * 60;
		CountDownTimer counter = new CountDownTimer(timer, 1000) {

			public void onTick(long millisUntilFinished) {
				countDown.setText("Sending Message In: " + TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished) + ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
			}

			public void onFinish() {
				countDown.setText("Message Sent!");
			}

		};
		return counter;
	}


	/**
	 * @return returns phoneNumbers.
	 */
	public String readFile(String input) {
		try {
			FileInputStream fileInputStream = openFileInput(input);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();

			String lines;
			while ((lines = bufferedReader.readLine()) != null) {
				stringBuffer.append(lines + "\n");
			}
			return stringBuffer.toString();

		} catch (IOException e) {

		}
		return "";
	}

	/**
	 * Writes file.
	 * @param input
	 */
	public void writeFile(String input, String name) {
		try {
			FileOutputStream fileOutputStream = openFileOutput(name, MODE_PRIVATE);
			fileOutputStream.write(input.getBytes());
			fileOutputStream.close();
			//Toast.makeText(getApplicationContext(), "File saved", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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


	/**
	 * sends message after a certain amount of time.
	 * @param message message the user wants to send.
	 * @param delay amount of delay between button press and sent message.
	 */
	public void event(String message, int delay) {
		String numbers = readFile("PhoneNumbers.txt");
		String[] numberList = numbers.split("/");
		getLocation();
		if (delay == 0) {
			for (String number: numberList) {
				sendSMS(number, message + "\n" + "https://www.google.com/maps/place/" + latitude + "%20" + longitude);
			}
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					for (String number: numberList) {
						sendSMS(number, message + "\n" + "https://www.google.com/maps/place/" + latitude + "%20" + longitude);
					}
				}
			}, delay * 1000 * 60);
		}
	}

	/**
	 * Turns emergency layout on or off.
	 * @param onOrOff Determines if the layout will be on or off.
	 */
	public void changeEMLayout(String onOrOff) {
		EditText editMessage = findViewById(R.id.editMessage);
		EditText editCheckInTimer = findViewById(R.id.editCheckInTimer);
		Button finish = findViewById(R.id.finish);
		if (onOrOff.equals("On")) {
			editMessage.setVisibility(View.VISIBLE);
			editCheckInTimer.setVisibility(View.VISIBLE);
			finish.setVisibility(View.VISIBLE);
		} else if (onOrOff.equals("Off")) {
			editMessage.setVisibility(View.GONE);
			editCheckInTimer.setVisibility(View.GONE);
			finish.setVisibility(View.GONE);
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.Emergency) {
			mainScreenOkayOrNot("On");
			mainScreen("Off");
			event(emergencyMessage, emergencyTimer);
			getTimer("emergencyTimer.txt").start();
		}
		//starts up person activity.
		if (view.getId() == R.id.sendTo) {
			Intent intent = new Intent(this, PeopleActivity.class);
			startActivity(intent);
		}
		//sets Custom name, message, and timer
		if (view.getId() == R.id.changeEmergency) {
			LinearLayout changeEmeregencySettings = findViewById(R.id.changeEM);
			changeEmeregencySettings.setVisibility(View.VISIBLE);
			findViewById(R.id.changeCustom).setVisibility(View.GONE);
			mainScreen("Off");
		}
		if (view.getId() == R.id.imOkay) {
			mainScreenOkayOrNot("Off");
			mainScreen("On");
			handler.removeCallbacksAndMessages(null);
			Toast.makeText(getApplicationContext(), "Canceled Emergency Message", Toast.LENGTH_LONG).show();
			getTimer("emergencyTimer.txt").cancel();
		}
		if (view.getId() == R.id.immediately) {
			mainScreen("On");
			mainScreenOkayOrNot("Off");
			Toast.makeText(getApplicationContext(), "Messages Sent", Toast.LENGTH_LONG).show();
			event(emergencyMessage, 0);
			handler.removeCallbacksAndMessages(null);
			getTimer("emergencyTimer.txt").cancel();
		}
		if (view.getId() == R.id.changeYes) {
			changeEMLayout("On");
		}
		if (view.getId() == R.id.changeNo) {
			LinearLayout changeEmeregencySettings = findViewById(R.id.changeEM);
			changeEmeregencySettings.setVisibility(View.GONE);
			mainScreen("On");
		}
		if (view.getId() == R.id.Custom) {

		}
		if (view.getId() == R.id.finish) {
			EditText editMessage = findViewById(R.id.editMessage);
			EditText editCheckInTimer = findViewById(R.id.editCheckInTimer);
			emergencyMessage = editMessage.getText().toString();
			if (!editCheckInTimer.getText().toString().equals("")) {
				emergencyTimer = Integer.parseInt(editCheckInTimer.getText().toString());
				writeFile(String.valueOf(emergencyTimer), "emergencyTimer.txt");
			}
			if (!emergencyMessage.equals("")) {
				writeFile(emergencyMessage, "emergencyMessage.txt");
			}
			changeEMLayout("Off");
			LinearLayout changeEmeregencySettings = findViewById(R.id.changeEM);
			changeEmeregencySettings.setVisibility(View.GONE);
			mainScreen("On");
			findViewById(R.id.changeEmergency).setVisibility(View.GONE);
			findViewById(R.id.changeCustom).setVisibility(View.GONE);
			findViewById(R.id.changePrefs).setVisibility(View.VISIBLE);
		}
		if (view.getId() == R.id.checkMessage) {
			System.out.println(emergencyMessage);
			System.out.println(emergencyTimer);
		}
		if (view.getId() == R.id.changePrefs) {
			findViewById(R.id.changeEmergency).setVisibility(View.VISIBLE);
			findViewById(R.id.changeCustom).setVisibility(View.VISIBLE);
			findViewById(R.id.changePrefs).setVisibility(View.GONE);
		}
		if (view.getId() == R.id.changeCustom) {

		}
	}
}
