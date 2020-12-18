package Winter.Project.Application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Buttons
		button = findViewById(R.id.sendTo);
		button.setOnClickListener(this);
		button = findViewById(R.id.test2);
		button.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.test2:
				System.out.println("clicked");
		}
		//starts up person activity
		switch (view.getId()) {
			case R.id.sendTo:
				Intent intent = new Intent(this, PeopleActivity.class);
				startActivity(intent);
		}

	}
}