package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FoodMenuActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_menu);

    Intent intent = getIntent();
    User currentUser = intent.getParcelableExtra(MainActivity.USER);
    TextView hello = findViewById(R.id.menuUser);
    hello.setText(getString(R.string.login_message, currentUser.getFirstName(), currentUser.getLastName()));

    /*
      - TODO: add Cart button
      - TODO: add food items
        - TODO: use dropdown lists
        - TODO: add "add" buttons
        - TODO: show quantity of items in cart
        - TODO: use + and - buttons of items in cart
     */
  }

  public void signOutAct(View view) {
    Intent signOutIntent = new Intent(this, MainActivity.class);
    signOutIntent.putExtra(MainActivity.LOGOUT, true);
    startActivity(signOutIntent);
    finish();
  }
}
