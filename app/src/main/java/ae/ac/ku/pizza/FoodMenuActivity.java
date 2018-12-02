package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class FoodMenuActivity extends Activity {

  HashMap<String, Integer> cart = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_menu);

    Intent intent = getIntent();
    User currentUser = intent.getParcelableExtra(MainActivity.USER);
    TextView hello = findViewById(R.id.menuUser);
    hello.setText(getString(R.string.login_message, currentUser.getFirstName(), currentUser.getLastName()));

    /*
      - TODO: add food items
        - TODO: use dropdown lists
     */
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if(intent.hasExtra(MainActivity.CART)) {
      cart = (HashMap<String, Integer>) intent.getSerializableExtra(MainActivity.CART);

      for(String item : new String[]{"Pizza", "Wings", "Bread", "Drinks"}) {
        Button
                increment = (Button) getViewFromIdentifier(item.toLowerCase() + "Increment"),
                add = (Button) getViewFromIdentifier(item.toLowerCase() + "Button"),
                decrement = (Button) getViewFromIdentifier(item.toLowerCase() + "Decrement");
        TextView number = (TextView) getViewFromIdentifier(item.toLowerCase() + "Number");
        add.setVisibility(View.VISIBLE);
        increment.setVisibility(View.GONE);
        decrement.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
      }

      for(String item : cart.keySet()) {
        Button
                increment = (Button) getViewFromIdentifier(item.toLowerCase() + "Increment"),
                add = (Button) getViewFromIdentifier(item.toLowerCase() + "Button"),
                decrement = (Button) getViewFromIdentifier(item.toLowerCase() + "Decrement");
        TextView number = (TextView) getViewFromIdentifier(item.toLowerCase() + "Number");
        editNumber(item, cart.get(item));
        add.setVisibility(View.GONE);
        increment.setVisibility(View.VISIBLE);
        number.setVisibility(View.VISIBLE);
        decrement.setVisibility(View.VISIBLE);
      }
    }
  }

  public void signOutAct(View view) {
    Intent signOutIntent = new Intent(this, MainActivity.class);
    signOutIntent.putExtra(MainActivity.LOGOUT, true);
    startActivity(signOutIntent);
    finish();
  }

  public void moveToCart(View view) {
    Intent cartIntent = new Intent(this, CartActivity.class);
    cartIntent.putExtra(MainActivity.CART, cart);
    startActivity(cartIntent);
  }

  public void addItem(View view) {
    String itemName = view.getTag().toString();
    if(cart.containsKey(itemName)) {
      final int
              value = cart.get(itemName) + 1;
      cart.put(itemName, value);
      editNumber(itemName, value);
    } else {
      cart.put(itemName, 1);
      view.setVisibility(View.GONE);
      Button
              incButton = (Button) getViewFromIdentifier(itemName.toLowerCase() + "Increment"),
              decButton = (Button) getViewFromIdentifier(itemName.toLowerCase() + "Decrement");
      TextView numText = (TextView) getViewFromIdentifier(itemName.toLowerCase() + "Number");
      numText.setText("1");
      incButton.setVisibility(View.VISIBLE);
      numText.setVisibility(View.VISIBLE);
      decButton.setVisibility(View.VISIBLE);
    }
  }

  public void removeItem(View view) {
    String itemName = view.getTag().toString();
    if(cart.get(itemName) == 1) {
      cart.remove(itemName);
      Button
        addButton = (Button) getViewFromIdentifier(itemName.toLowerCase() + "Button"),
        incrementButton = (Button) getViewFromIdentifier(itemName.toLowerCase() + "Increment");
      TextView numberText = (TextView) getViewFromIdentifier(itemName.toLowerCase() + "Number");

      view.setVisibility(View.GONE);
      incrementButton.setVisibility(View.GONE);
      numberText.setVisibility(View.GONE);
      addButton.setVisibility(View.VISIBLE);
    } else {
      final int value = cart.get(itemName) - 1;
      cart.put(itemName, value);
      editNumber(itemName, value);
    }
  }

  View getViewFromIdentifier(String identifier) {
    return findViewById(getResources().getIdentifier(identifier, "id", getPackageName()));
  }

  private void editNumber(String itemName, int newValue) {
    TextView numberText = (TextView) getViewFromIdentifier(itemName.toLowerCase() + "Number");
    numberText.setText(String.valueOf(newValue));
  }
}
