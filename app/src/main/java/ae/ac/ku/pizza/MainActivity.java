package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";
  public static final String
    USER = "ae.ac.ku.pizza.USER_OBJECT",
    LOGOUT = "ae.ac.ku.pizza.LOGOUT_VALUE",
    SIGNUP = "ae.ac.ku.pizza.SIGNUP_VALUE",
    CART = "ae.ac.ku.pizza.CART_HASHMAP",
    PAYMENT = "ae.ac.ku.pizza.PAYMENT_INFORMATION",
    TOTAL = "ae.ac.ku.pizza.TOTAL_VALUE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    persist();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    boolean logout = intent.getBooleanExtra(LOGOUT, false);
    boolean login = intent.getBooleanExtra(SIGNUP, false);
    if(logout) {
      SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);
      SharedPreferences.Editor editor = localPrefs.edit();
      editor.clear();
      editor.apply();
    }
    persist();
  }

  public void signUpAct(View view) {
    Intent signUpIntent = new Intent(this, SignUpActivity.class);
    signUpIntent.putExtra(SIGNUP, true);
    startActivity(signUpIntent);
  }

  public void persist() {
    SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);

    String login = localPrefs.getString(getString(R.string.fname_key), "Login Failed!");
    if(login != null) {
      if(!login.equals("Login Failed!")) {
        String
                lastName = localPrefs.getString(getString(R.string.lname_key), ""),
                phoneNumber = localPrefs.getString(getString(R.string.phone_key), ""),
                email = localPrefs.getString(getString(R.string.email_key), ""),
                street = localPrefs.getString(getString(R.string.street_key), ""),
                building = localPrefs.getString(getString(R.string.building_key), ""),
                floor = localPrefs.getString(getString(R.string.floor_key), ""),
                apartment = localPrefs.getString(getString(R.string.apartment_key), ""),
                location = localPrefs.getString(getString(R.string.location_key), "");
        User currentUser = new User(login, lastName, phoneNumber, email, street, building, floor, apartment, location);

        Intent toMenu = new Intent(this, FoodMenuActivity.class);
        toMenu.putExtra(USER, currentUser);
        startActivity(toMenu);
      }
    }
  }
}