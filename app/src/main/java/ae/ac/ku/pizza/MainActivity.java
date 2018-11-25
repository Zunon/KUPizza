package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

  public static final String USER = "ae.ac.ku.pizza.USER_OBJECT";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    persist();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    persist();
  }

  public void signUpAct(View view) {
    Intent signUpIntent = new Intent(this, SignUpActivity.class);
    startActivity(signUpIntent);
  }

  public void persist() {
    TextView welcomeText = findViewById(R.id.welcomeText);
    SharedPreferences localPrefs = getSharedPreferences(getString(R.string.shared_preferences_filename), MODE_PRIVATE);
    String login = localPrefs.getString(getString(R.string.fname_key), "Login Failed!");

    if(!login.equals("Login Failed!")){
      String
              lastName = localPrefs.getString(getString(R.string.lname_key), ""),
              email = localPrefs.getString(getString(R.string.email_key), ""),
              street = localPrefs.getString(getString(R.string.street_key), ""),
              building = localPrefs.getString(getString(R.string.building_key), ""),
              floor = localPrefs.getString(getString(R.string.floor_key), ""),
              apartment = localPrefs.getString(getString(R.string.apartment_key), "");
      User currentUser = new User(login, lastName, email, street, building, floor, apartment);

      Intent toMenu = new Intent(this, FoodMenuActivity.class);
      toMenu.putExtra(USER, currentUser);
      startActivity(toMenu);
    }
  }
}
