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

import java.util.List;

public class SignUpActivity extends Activity {

  String location = "Unknown";
  LocationManager mLocationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    getLocation();
  }

  public void storeInfo(View view) {

    EditText
            fNameE = findViewById(R.id.signUpFirstName),
            lNameE = findViewById(R.id.signUpLastName),
            phoneE = findViewById(R.id.signUpPhone),
            emailE = findViewById(R.id.signUpEmail),
            streetE = findViewById(R.id.signUpStreet),
            buildingE = findViewById(R.id.signUpBuilding),
            floorE = findViewById(R.id.signUpFloor),
            apartmentE = findViewById(R.id.signUpApartment);

    String
            fName = fNameE.getText().toString(),
            lName = lNameE.getText().toString(),
            phone = phoneE.getText().toString(),
            email = emailE.getText().toString(),
            street = streetE.getText().toString(),
            building = buildingE.getText().toString(),
            floor = floorE.getText().toString(),
            apartment = apartmentE.getText().toString();

    SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);
    SharedPreferences.Editor editor = localPrefs.edit();

    editor.putString(getString(R.string.fname_key), fName);
    editor.putString(getString(R.string.lname_key), lName);
    editor.putString(getString(R.string.phone_key), phone);
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

  private void getLocation() {
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 69);
    }

    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    LocationListener locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        Log.d("Location: ", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
        setLocation(String.valueOf(location.getLatitude()).substring(0, 8) + ", " + String.valueOf(location.getLongitude()).substring(0, 8));
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

    mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
    if(this.location.equals("Unknown")) {
      Location loc = getLastKnownLocation();
      this.location = String.valueOf(loc.getLatitude()).substring(0, 8) + ", " + String.valueOf(loc.getLongitude());
    }
  }

  public void setLocation(String location) {
    this.location = location;
  }


  private Location getLastKnownLocation() {
    mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    List<String> providers = mLocationManager.getProviders(true);
    Location bestLocation = null;
    for(String provider : providers) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 69);
        }
        Location l = mLocationManager.getLastKnownLocation(provider);
      if (l == null) {
        continue;
      }
      if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
        // Found best last known location: %s", l);
        bestLocation = l;
      }
    }
    return bestLocation;
  }
}