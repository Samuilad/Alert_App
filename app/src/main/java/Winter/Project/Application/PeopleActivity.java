package Winter.Project.Application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class PeopleActivity extends AppCompatActivity implements View.OnClickListener {
	private HashMap<String, Integer> contacts = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people);
		LinearLayout person = (LinearLayout) findViewById(R.id.person_info);
		Button btn = findViewById(R.id.Contact);
		btn.setOnClickListener(this);
		btn = findViewById(R.id.Done);
		btn.setOnClickListener(this);

		person.setVisibility(View.GONE);

	}
	public void contact() {
		LinearLayout person = (LinearLayout) findViewById(R.id.person_info);

		if (person.getVisibility() == View.VISIBLE) {
			person.setVisibility(View.GONE);
		}
		if (person.getVisibility() == View.GONE) {
			person.setVisibility(View.VISIBLE);
		}
	}
	public void done() {
		EditText contactName = findViewById(R.id.editName);
		EditText contactPhone = findViewById(R.id.editPhone);
		LinearLayout person = (LinearLayout) findViewById(R.id.person_info);

		if (contactName.length() == 0 || contactPhone.length() == 0) {
			Toast.makeText(PeopleActivity.this, "Field(s) Empty", Toast.LENGTH_SHORT).show();
		} else {
			String name = contactName.getText().toString();
			Integer phoneNumber = parseInt(contactPhone.getText().toString());
			contacts.put(name, phoneNumber);
		}

		person.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.Contact:
				System.out.println("here1");
				contact();
		}
		//gets rid of contact entry windows and enters data
		switch (view.getId()) {
			case R.id.Done:
				done();
		}
	}
}
