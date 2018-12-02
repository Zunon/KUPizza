package ae.ac.ku.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import java.util.HashMap;

public class ReceiptActivity extends Activity {

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
  }

  private void initializeValues() {
    TextView
      nameText = findViewById(R.id.nameValue),
      phoneText = findViewById(R.id.phoneValue),
      totalText = findViewById(R.id.priceValue);

    nameText.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    phoneText.setText(currentUser.getPhoneNumber());
    totalText.setText(String.valueOf(totalPrice));
  }
}
