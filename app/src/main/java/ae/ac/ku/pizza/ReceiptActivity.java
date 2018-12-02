package ae.ac.ku.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
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
    calculateTime();
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

  private double calculateDistance() {
    double userLatitude, userLongitude;
    String[] locationString = currentUser.getLocation().split(",", 2);
    userLatitude = Double.parseDouble(locationString[0]);
    userLongitude = Double.parseDouble(locationString[1]);
    final double
      radiusOfEarth = 6361e3,
      φ1 = Math.toRadians(MainActivity.STORE_LATITUDE),
      φ2 = Math.toRadians(userLatitude),
      Δφ = Math.toRadians(userLatitude - MainActivity.STORE_LATITUDE),
      Δλ = Math.toRadians(userLongitude - MainActivity.STORE_LONGITUDE),
      haversine = Math.pow(Math.sin(Δφ/2), 2) + Math.cos(φ1) * Math.cos(φ2) * Math.pow(Math.sin(Δλ/2), 2),
      c = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
    return radiusOfEarth * c;
  }

  private void calculateTime() {
    final double
      distance = calculateDistance(),
      kilometers = distance / 1000,
      exactApprox = kilometers * 7;
    final int minutes = (int) (5*(Math.round(exactApprox/5)));
    TextView timeValue = findViewById(R.id.timeValue);
    timeValue.setText(String.valueOf(minutes) + " Minutes");
  }
}
