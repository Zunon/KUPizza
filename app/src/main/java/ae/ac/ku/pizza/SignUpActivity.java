package ae.ac.ku.pizza;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends Activity {

  String location = "Unknown";

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

    getLocation();

    SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);
    SharedPreferences.Editor editor = localPrefs.edit();

    editor.putString(getString(R.string.fname_key), fName);
    editor.putString(getString(R.string.lname_key), lName);
    editor.putString(getString(R.string.email_key), email);
    editor.putString(getString(R.string.street_key), street);
    editor.putString(getString(R.string.building_key), building);
    editor.putString(getString(R.string.floor_key), floor);
    editor.putString(getString(R.string.apartment_key), apartment);
    editor.putString(getString(R.string.location_key), location);

    editor.apply();
    Intent done = new Intent(this, MainActivity.class);
    done.putExtra(MainActivity.SIGNUP, true);
    startActivity(done);
  }

  // TODO: Add Google Maps API support.

  private void getLocation() {
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 69);
    }

    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    LocationListener locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        setLocation(location.getLatitude() + ", " + location.getLongitude());
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Zunon", "onStatusChanged");
      }

      @Override
      public void onProviderEnabled(String provider) {
        Log.d("Zunon", "onProviderEnabled");
      }

      @Override
      public void onProviderDisabled(String provider) {
        Log.d("Zunon", "onProviderDisabled");
      }
    };

    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
