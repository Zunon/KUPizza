package ae.ac.ku.pizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends Activity {
  HashMap<String, Integer[]> cart;
  User currentUser;
  private static final String TAG = "CartActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    cart = (HashMap<String, Integer[]>) getIntent().getSerializableExtra(MainActivity.CART);
    currentUser = getIntent().getParcelableExtra(MainActivity.USER);
    initRecyclerViews();
  }

  public void backToMenu(View view) {
    Intent backIntent = new Intent(this, FoodMenuActivity.class);
    backIntent.putExtra(MainActivity.CART, cart);
    backIntent.putExtra(MainActivity.USER, currentUser);
    startActivity(backIntent);
    finish();
  }

  public void addItem(View view) {
    String itemName = view.getTag().toString(),
      tagName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    TextView label = parent.findViewWithTag(tagName + "txt");
    itemName = label.getText().toString();
    final int
      value = cart.get(itemName)[0] + 1,
      price = cart.get(itemName)[1];
    final Integer[] values = new Integer[] {value, price};
    cart.put(itemName, values);
    TextView number = parent.findViewWithTag(tagName + "num");
    number.setText(String.valueOf(values[0]));
    TextView totalPrice = findViewById(R.id.priceNumber);
    totalPrice.setText(String.valueOf(total(cart)));
  }

  public void removeItem(View view) {
    String itemName = view.getTag().toString(),
            tagName = itemName.substring(0, itemName.length() - 3);
    ConstraintLayout parent = (ConstraintLayout) view.getParent();
    TextView label = parent.findViewWithTag(tagName + "txt");
    itemName = label.getText().toString();
    if(cart.get(itemName)[0] == 1) {
      cart.remove(itemName);
      RecyclerView recyclerView = findViewById(R.id.cartView);
      CardView card = recyclerView.findViewWithTag(tagName + "crd");
      card.setVisibility(View.GONE);
      TextView totalPrice = findViewById(R.id.priceNumber);
      totalPrice.setText(String.valueOf(total(cart)));
    } else {
      final int value = cart.get(itemName)[0] - 1, price = cart.get(itemName)[1];
      final Integer[] values = new Integer[] {value, price};
      cart.put(itemName, values);
      TextView number = parent.findViewWithTag(tagName + "num");
      number.setText(String.valueOf(values[0]));
      TextView totalPrice = findViewById(R.id.priceNumber);
      totalPrice.setText(String.valueOf(total(cart)));
    }
  }

  View getViewFromIdentifier(String identifier) {
    return findViewById(getResources().getIdentifier(identifier, "id", getPackageName()));
  }

  public void initRecyclerViews() {
    //INITIALIZE DATA
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Integer> prices = new ArrayList<>();
    for(String key : cart.keySet()) {
      names.add(key);
      prices.add(cart.get(key)[1]);
    }
    //INITIALIZE VIEW
    RecyclerView recyclerView = findViewById(R.id.cartView);
    CartAdapter adapter = new CartAdapter(names, prices, this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.getViewTreeObserver()
      .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          for (String item : cart.keySet()) {
            String tagName = item.toLowerCase().replace(" ", "_");
            CardView card = recyclerView.findViewWithTag(tagName + "crd");
            TextView number = card.findViewWithTag(tagName + "num");
            number.setText(String.valueOf(cart.get(item)[0]));
            card.setVisibility(View.VISIBLE);
          }
          TextView totalPrice = findViewById(R.id.priceNumber);
          totalPrice.setText(String.valueOf(total(cart)));
          recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      });

  }

  public void enableCard(View view) {
    EditText cardInput = findViewById(R.id.cardInput);
    cardInput.setEnabled(true);
    cardInput.setFocusable(true);
  }

  public void disableCard(View view) {
    EditText cardInput = findViewById(R.id.cardInput);
    cardInput.setEnabled(false);
    cardInput.setFocusable(false);
  }

  public void checkOut(View view) {
    String paymentData;
    EditText cardInput = findViewById(R.id.cardInput);
    if(cardInput.isEnabled()) {
      paymentData = "Credit Card: " + cardInput.getText().toString();
    } else {
      paymentData = "Cash on Delivery";
    }
    TextView totalText = findViewById(R.id.priceNumber);
    int totalValue = Integer.parseInt(totalText.getText().toString());
    Intent receiptIntent = new Intent(this, ReceiptActivity.class);
    receiptIntent.putExtra(MainActivity.USER, currentUser);
    receiptIntent.putExtra(MainActivity.CART, cart);
    receiptIntent.putExtra(MainActivity.PAYMENT, paymentData);
    receiptIntent.putExtra(MainActivity.TOTAL, totalValue);
    startActivity(receiptIntent);
    finish();
  }

  private static int total(HashMap<String, Integer[]> cartMap) {
    ArrayList<Integer> prices = new ArrayList<>();
    for(String item : cartMap.keySet()) prices.add(cartMap.get(item)[1] * cartMap.get(item)[0]);
    return prices.stream().mapToInt(Integer::intValue).sum();
  }
}
