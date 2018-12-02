package ae.ac.ku.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class ReceiptActivity extends Activity {

  private static final String TAG = "ReceiptActivity";
  

  HashMap<String, Integer[]> cart;
  User currentUser;
  String paymentInfo;
  int totalPrice;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receipt);

    Intent incomingData = getIntent();
    cart = (HashMap<String, Integer[]>) incomingData.getSerializableExtra(MainActivity.CART);
    currentUser = incomingData.getParcelableExtra(MainActivity.USER);
    paymentInfo = incomingData.getStringExtra(MainActivity.PAYMENT);
    totalPrice = incomingData.getIntExtra(MainActivity.TOTAL, -1);

    initializeValues();
    initRecycleView();
  }

  private void initializeValues() {
    TextView
      nameText = findViewById(R.id.nameValue),
      phoneText = findViewById(R.id.phoneValue),
      totalText = findViewById(R.id.priceValue),
      locationText = findViewById(R.id.locationValue);

    nameText.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    phoneText.setText(currentUser.getPhoneNumber());
    totalText.setText(String.valueOf(totalPrice));
    Log.d(TAG, "initializeValues: " + currentUser.getLocation());
    locationText.setText(currentUser.getLocation());
  }

  private void initRecycleView() {
    RecyclerView recyclerView = findViewById(R.id.orderValues);
    ReceiptAdapter adapter = new ReceiptAdapter(cart);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }
}
