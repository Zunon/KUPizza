package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class CartActivity extends Activity {
  HashMap<String, Integer> cart;
  User currentUser;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    cart = (HashMap<String, Integer>) getIntent().getSerializableExtra(MainActivity.CART);
    currentUser = getIntent().getParcelableExtra(MainActivity.USER);

    for (String item : cart.keySet()) {
      editNumber(item, cart.get(item));
      getViewFromIdentifier(item.toLowerCase() + "Card").setVisibility(View.VISIBLE);
    }
  }

  public void backToMenu(View view) {
    Intent backIntent = new Intent(this, FoodMenuActivity.class);
    backIntent.putExtra(MainActivity.CART, cart);
    startActivity(backIntent);
    finish();
  }

  public void addItem(View view) {
    String itemName = view.getTag().toString();
    if(cart.containsKey(itemName)) {
      final int
              value = cart.get(itemName) + 1;
      cart.put(itemName, value);
      editNumber(itemName, value);
    }
  }

  public void removeItem(View view) {
    String itemName = view.getTag().toString();
    if(cart.get(itemName) == 1) {
      cart.remove(itemName);
      CardView card = (CardView) getViewFromIdentifier(itemName.toLowerCase() + "Card");
      card.setVisibility(View.GONE);
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
