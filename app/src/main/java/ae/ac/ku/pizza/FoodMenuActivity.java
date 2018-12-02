package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FoodMenuActivity extends Activity {

  private static final String TAG = "FoodMenuActivity";
  HashMap<String, Integer> cart = new HashMap<>();
  User currentUser;
  boolean expanded = false;
  private static final LinearLayout.LayoutParams
    ONE = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f),
    ZERO = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f),
    NINE = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 9f);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_menu);

    Intent intent = getIntent();
    currentUser = intent.getParcelableExtra(MainActivity.USER);
    TextView hello = findViewById(R.id.menuUser);
    hello.setText(getString(R.string.login_message, currentUser.getFirstName(), currentUser.getLastName()));
    try {
      initRecyclerViews();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    // TODO: NEW IMPLEMENTATION
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
    cartIntent.putExtra(MainActivity.USER, currentUser);
    startActivity(cartIntent);
  }

  public void addItem(View view) {
    String itemName = view.getTag().toString();
    itemName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    if(cart.containsKey(itemName)) {
      final int
              value = cart.get(itemName) + 1;
      cart.put(itemName, value);
      TextView numberText = parent.findViewWithTag(itemName + "num");
      numberText.setText(String.valueOf(value));
    } else {
      cart.put(itemName, 1);
      view.setVisibility(View.GONE);
      Button
              incButton = parent.findViewWithTag(itemName + "inc"),
              decButton = parent.findViewWithTag(itemName + "dec");
      TextView numText = parent.findViewWithTag(itemName + "num");
      numText.setText("1");
      incButton.setVisibility(View.VISIBLE);
      numText.setVisibility(View.VISIBLE);
      decButton.setVisibility(View.VISIBLE);
    }
  }

  public void removeItem(View view) {
    String itemName = view.getTag().toString();
    itemName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    if(cart.get(itemName) == 1) {
      cart.remove(itemName);
      Button
        addButton = parent.findViewWithTag(itemName.toLowerCase() + "add"),
        incrementButton = parent.findViewWithTag(itemName.toLowerCase() + "inc");
      TextView numberText = parent.findViewWithTag(itemName.toLowerCase() + "num");

      view.setVisibility(View.GONE);
      incrementButton.setVisibility(View.GONE);
      numberText.setVisibility(View.GONE);
      addButton.setVisibility(View.VISIBLE);
    } else {
      final int value = cart.get(itemName) - 1;
      cart.put(itemName, value);
      TextView numberText = parent.findViewWithTag(itemName + "num");
      numberText.setText(String.valueOf(value));
    }
  }

  public void expand(View view) {
    if(expanded) {
      collapse();
    } else {
      findViewById(R.id.dealsButton).setLayoutParams(ZERO);
      findViewById(R.id.pizzaButton).setLayoutParams(ZERO);
      findViewById(R.id.pastaButton).setLayoutParams(ZERO);
      findViewById(R.id.drinksButton).setLayoutParams(ZERO);
      findViewById(R.id.startersButton).setLayoutParams(ZERO);
      findViewById(R.id.dessertsButton).setLayoutParams(ZERO);
      findViewById(R.id.wingsButton).setLayoutParams(ZERO);

      view.setLayoutParams(ONE);
      String tag = view.getTag().toString();
      getViewFromIdentifier(tag + "View").setLayoutParams(NINE);
      Log.d(TAG, "expand: " + tag + "View");
      expanded = true;
    }
  }

  public void collapse() {
    findViewById(R.id.dealsView).setLayoutParams(ZERO);
    findViewById(R.id.pizzaView).setLayoutParams(ZERO);
    findViewById(R.id.pastaView).setLayoutParams(ZERO);
    findViewById(R.id.drinksView).setLayoutParams(ZERO);
    findViewById(R.id.startersView).setLayoutParams(ZERO);
    findViewById(R.id.dessertsView).setLayoutParams(ZERO);
    findViewById(R.id.wingsView).setLayoutParams(ZERO);
    findViewById(R.id.dealsButton).setLayoutParams(ONE);
    findViewById(R.id.pizzaButton).setLayoutParams(ONE);
    findViewById(R.id.pastaButton).setLayoutParams(ONE);
    findViewById(R.id.drinksButton).setLayoutParams(ONE);
    findViewById(R.id.startersButton).setLayoutParams(ONE);
    findViewById(R.id.dessertsButton).setLayoutParams(ONE);
    findViewById(R.id.wingsButton).setLayoutParams(ONE);
    expanded = false;
  }

  View getViewFromIdentifier(String identifier) {
    return findViewById(getResources().getIdentifier(identifier, "id", getPackageName()));
  }

  private void editNumber(String itemName, int newValue) {
    TextView numberText = (TextView) getViewFromIdentifier(itemName.toLowerCase() + "Number");
    numberText.setText(String.valueOf(newValue));
  }

  public String loadJSONFromAsset() {
    String json;
    try {
      InputStream is = getAssets().open("items.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;
  }

  private void initRecyclerViews() throws Exception {
    // INITIALIZE DATASET
    String JSONText = loadJSONFromAsset();
    HashMap<String, ArrayList<String>> nameMap = new HashMap<>();
    HashMap<String, ArrayList<Integer>> priceMap = new HashMap<>();
    JSONObject data = new JSONObject(JSONText);
    for(String type : new String[]{"Deals", "Pizza", "Pasta", "Chicken Wings", "Drinks", "Desserts", "Starters"}) {
      JSONObject prices = data.getJSONObject(type);
      Iterator names = prices.keys();
      nameMap.put(type, new ArrayList<>());
      priceMap.put(type, new ArrayList<>());
      while(names.hasNext()) {
        String name = names.next().toString();
        nameMap.get(type).add(name);
        priceMap.get(type).add(prices.getInt(name));
      }
      // INSTANTIATE RECYCLERVIEWS
      RecyclerView recyclerView = (RecyclerView) getViewFromIdentifier(getTagFromText(type) + "View");
      MenuAdapter adapter = new MenuAdapter(nameMap.get(type), priceMap.get(type), this);
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    Log.d(TAG, "initRecyclerViews: FINISHED");

  }
  private String getTagFromText(String text) throws Exception {
    switch(text) {
      case "Deals":
      case "Pizza":
      case "Drinks":
      case "Pasta":
      case "Desserts":
      case "Starters":
        return text.toLowerCase();
      case "Chicken Wings":
        return "wings";
      default:
        throw new Exception();
    }
  }
}
