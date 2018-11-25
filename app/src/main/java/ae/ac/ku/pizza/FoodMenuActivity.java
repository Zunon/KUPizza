package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FoodMenuActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_menu);

    Intent intent = getIntent();
    User currentUser = intent.getParcelableExtra(MainActivity.USER);
    TextView hello = findViewById(R.id.menuUser);
    hello.setText("Hello, " + currentUser.getFirstName() + " " + currentUser.getLastName());
  }
}
