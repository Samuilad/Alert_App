package Winter.Project.Application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class PeopleActivity extends AppCompatActivity implements View.OnClickListener {
	private HashMap<String, String> contacts = new HashMap<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people);
		LinearLayout person = findViewById(R.id.person_info);
		for (TextView contact : getContactList()) {
			contact.setVisibility(View.GONE);
		}
		person.setVisibility(View.GONE);
		Button btn = findViewById(R.id.Contact);
		btn.setOnClickListener(this);
		btn = findViewById(R.id.Done);
		btn.setOnClickListener(this);
	}

	/**
	 * @return returns list of Contacts.
	 */
	public ArrayList<TextView> getContactList() {
		TextView contact1 = findViewById(R.id.Contact1);
		TextView contact2 = findViewById(R.id.Contact2);
		TextView contact3 = findViewById(R.id.Contact3);
		TextView contact4 = findViewById(R.id.Contact4);
		TextView contact5 = findViewById(R.id.Contact5);
		TextView contact6 = findViewById(R.id.Contact6);
		TextView contact7 = findViewById(R.id.Contact7);
		TextView contact8 = findViewById(R.id.Contact8);
		return (ArrayList<TextView>) new ArrayList() {{
			add(contact1);
			add(contact2);
			add(contact3);
			add(contact4);
			add(contact5);
			add(contact6);
			add(contact7);
			add(contact8);
		}};
	}

	/**
	 * Retrieves requested Contact.
	 * @param number identifies which contact.
	 * @return returns contact.
	 */
	public TextView getContact(int number) {
		int i = 1;
		for (TextView contact : getContactList()) {
			if (i == number) {
				return contact;
			} else {
				i++;
			}
		}
		throw new IllegalArgumentException();
	}
	/**
	 * opens up contact info window.
	 */
	public void contact() {
		LinearLayout person = findViewById(R.id.person_info);

		if (person.getVisibility() == View.VISIBLE) {
			person.setVisibility(View.GONE);
		}
		if (person.getVisibility() == View.GONE) {
			person.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * clears text in editText objects for name and phone number.
	 */
	public void clearEditText() {
		EditText contactName = findViewById(R.id.editName);
		EditText contactPhone = findViewById(R.id.editPhone);
		contactName.setText("");
		contactPhone.setText("");
	}
	/**
	 * Sets contact entry window to View.GONE.
	 */
	public void done() {
		EditText contactName = findViewById(R.id.editName);
		EditText contactPhone = findViewById(R.id.editPhone);
		LinearLayout person = findViewById(R.id.person_info);
		person.setVisibility(View.GONE);

		if (contactName.length() == 0 || contactPhone.length() != 10) {
			Toast.makeText(PeopleActivity.this, "Field(s) Empty or Invalid Phone #", Toast.LENGTH_SHORT).show();
		} else {
			String name = contactName.getText().toString();
			String phoneNumber = contactPhone.getText().toString();
			contacts.put(name, phoneNumber);
			for (TextView contact : getContactList()) {
				if (contact.getText().toString().length() == 0) {
					contact.setText(name);
					contact.setVisibility(View.VISIBLE);
					clearEditText();
					break;
				}
			}
		}
	}
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.Contact) {
			contact();
			clearEditText();
		}
		if (view.getId() == R.id.Done) {
			done();
		}
	}
}
