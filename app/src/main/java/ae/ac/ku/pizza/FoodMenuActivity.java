package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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
  HashMap<String, Integer[]> cart = new HashMap<>();
  HashMap<String, ArrayList<String>> nameMap = new HashMap<>();
  HashMap<String, ArrayList<Integer>> priceMap = new HashMap<>();
  User currentUser;
  String[] categories = new String[]{"Deals", "Pizza", "Pasta", "Chicken Wings", "Drinks", "Desserts", "Starters"};
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
    super.onNewIntent(intent);
    if(intent.hasExtra(MainActivity.CART)) {
      cart = (HashMap<String, Integer[]>) intent.getSerializableExtra(MainActivity.CART);

      for(String type : categories) {
        try {
          RecyclerView recyclerView = (RecyclerView) getViewFromIdentifier(getTagFromText(type) + "View");
          recyclerView.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                for (String item : nameMap.get(type)) {
                  String tagName = item.toLowerCase().replace(" ", "_");
                  TextView number = recyclerView.findViewWithTag(tagName + "num");
                  Log.d(TAG, "onGlobalLayout: " + tagName);
                  Button
                    increment = recyclerView.findViewWithTag(tagName + "inc"),
                    decrement = recyclerView.findViewWithTag(tagName + "dec"),
                    add = recyclerView.findViewWithTag(tagName + "add");
                  if(number != null) {
                    number.setVisibility(View.GONE);
                    increment.setVisibility(View.GONE);
                    decrement.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                  }
                }

                ConstraintLayout ancestor = findViewById(R.id.ancestor);
                for(String item : cart.keySet()) {
                  String tagName = item.toLowerCase().replace(' ', '_');
                  TextView number = ancestor.findViewWithTag(tagName + "num");
                  Button
                          increment = ancestor.findViewWithTag(tagName + "inc"),
                          decrement = ancestor.findViewWithTag(tagName + "dec"),
                          add = ancestor.findViewWithTag(tagName + "add");
                  if(number != null) {
                    number.setVisibility(View.VISIBLE);
                    increment.setVisibility(View.VISIBLE);
                    decrement.setVisibility(View.VISIBLE);
                    add.setVisibility(View.GONE);
                    number.setText(String.valueOf(cart.get(item)[0]));
                  }
                }

                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
              }
            });
        } catch(Exception e) {
          Log.e(TAG, "onNewIntent: " + e.toString());
        }
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
    String
      itemName = view.getTag().toString(),
      tagName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    TextView label = parent.findViewWithTag(tagName + "txt");
    itemName = label.getText().toString();
    if(cart.containsKey(itemName)) {
      TextView price = parent.findViewWithTag(tagName + "csh");
      final int
        quantityValue = cart.get(itemName)[0] + 1,
        priceValue = Integer.parseInt(price.getText().toString());
      final Integer[] values = new Integer[]{quantityValue, priceValue};
      cart.put(itemName, values);
      TextView numberText = parent.findViewWithTag(tagName + "num");
      numberText.setText(String.valueOf(values[0]));
    } else {
      TextView price = parent.findViewWithTag(tagName + "csh");
      final int
              priceValue = Integer.parseInt(price.getText().toString());
      final Integer[] values = new Integer[]{1, priceValue};
      cart.put(itemName, values);
      view.setVisibility(View.GONE);
      Button
              incButton = parent.findViewWithTag(tagName + "inc"),
              decButton = parent.findViewWithTag(tagName + "dec");
      TextView numText = parent.findViewWithTag(tagName + "num");
      numText.setText("1");
      incButton.setVisibility(View.VISIBLE);
      numText.setVisibility(View.VISIBLE);
      decButton.setVisibility(View.VISIBLE);
    }
  }

  public void removeItem(View view) {
    String
      itemName = view.getTag().toString(),
      tagName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    TextView label = parent.findViewWithTag(tagName + "txt");
    itemName = label.getText().toString();
    if(cart.get(itemName)[0] == 1) {
      cart.remove(itemName);
      Button
        addButton = parent.findViewWithTag(tagName + "add"),
        incrementButton = parent.findViewWithTag(tagName + "inc");
      TextView numberText = parent.findViewWithTag(tagName + "num");

      view.setVisibility(View.GONE);
      incrementButton.setVisibility(View.GONE);
      numberText.setVisibility(View.GONE);
      addButton.setVisibility(View.VISIBLE);
    } else {
      TextView price = parent.findViewWithTag(tagName + "csh");
      final int
              quantityValue = cart.get(itemName)[0] - 1,
              priceValue = Integer.parseInt(price.getText().toString());
      final Integer[] values = new Integer[]{quantityValue, priceValue};
      cart.put(itemName, values);
      TextView numberText = parent.findViewWithTag(tagName + "num");
      numberText.setText(String.valueOf(values[0]));
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
    nameMap = new HashMap<>();
    priceMap = new HashMap<>();
    JSONObject data = new JSONObject(JSONText);
    for(String type : categories) {
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
      Log.d(TAG, "initRecyclerViews: Attaching adapter" );
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    Log.d(TAG, "initRecyclerViews: FINISHED");

  }
  public static String getTagFromText(String text) throws Exception {
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
