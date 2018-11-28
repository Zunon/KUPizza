package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
  }

  public void storeInfo(View view) {

    EditText
      fNameE = findViewById(R.id.signUpFirstName),
      lNameE = findViewById(R.id.signUpLastName),
      emailE = findViewById(R.id.signUpEmail),
      streetE = findViewById(R.id.signUpStreet),
      buildingE = findViewById(R.id.signUpBuilding),
      floorE = findViewById(R.id.signUpFloor),
      apartmentE = findViewById(R.id.signUpApartment);

    String
      fName = fNameE.getText().toString(),
      lName = lNameE.getText().toString(),
      email = emailE.getText().toString(),
      street = streetE.getText().toString(),
      building = buildingE.getText().toString(),
      floor = floorE.getText().toString(),
      apartment = apartmentE.getText().toString();

    SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);
    SharedPreferences.Editor editor = localPrefs.edit();

    editor.putString(getString(R.string.fname_key), fName);
    editor.putString(getString(R.string.lname_key), lName);
    editor.putString(getString(R.string.email_key), email);
    editor.putString(getString(R.string.street_key), street);
    editor.putString(getString(R.string.building_key), building);
    editor.putString(getString(R.string.floor_key), floor);
    editor.putString(getString(R.string.apartment_key), apartment);

    editor.apply();

    Intent done = new Intent(this, MainActivity.class);
    done.putExtra(MainActivity.SIGNUP, true);
    startActivity(done);
  }

  // TODO: Add Google Maps API support.
}
