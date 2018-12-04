package ae.ac.ku.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
    setTime();
    try {
      writeToFile();
    } catch(IOException e) {
    }
  }

  private void initializeValues() {
    TextView
      nameText = findViewById(R.id.nameValue),
      phoneText = findViewById(R.id.phoneValue),
      totalText = findViewById(R.id.priceValue),
      locationText = findViewById(R.id.locationValue),
      paymentText = findViewById(R.id.paymentValue);
    paymentText.setText(paymentInfo);
    nameText.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
    phoneText.setText(currentUser.getPhoneNumber());
    totalText.setText(String.valueOf(totalPrice));
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

  private int calculateTime() {
    final double
      distance = calculateDistance(),
      kilometers = distance / 1000,
      exactApprox = kilometers * 7,
      exacterApprox = Math.sqrt(exactApprox * 17);
    return (int) (5*(Math.round(exacterApprox/5))) + 15;
  }

  private void setTime() {
    TextView timeValue = findViewById(R.id.timeValue);
    timeValue.setText(String.valueOf(calculateTime()) + " Minutes");
  }

  private void writeToFile() throws IOException {
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      File file = new File(getExternalFilesDir(null), "receipt.txt");
      PrintWriter printWriter = new PrintWriter(file);
      StringBuilder builder = new StringBuilder();
      builder.append(getString(R.string.receipt_start) + "\n");
      builder.append(getString(R.string.info_laber) + "\n");
      builder.append(getString(R.string.name_label) + "\t" + currentUser.getFirstName() + " " + currentUser.getLastName() + "\n");
      builder.append(getString(R.string.phone_label) + "\t" + currentUser.getPhoneNumber() + "\n");
      builder.append(getString(R.string.order_label) + "\n");
      for(String item : cart.keySet()) {
        builder.append(item + "\t" + cart.get(item)[0] + getString(R.string.x) + cart.get(item)[1] + getString(R.string.aed) + "\n");
      }
      builder.append(getString(R.string.price_label) + "\t" + totalPrice + "\n");
      builder.append(getString(R.string.payment_select) + "\t" + paymentInfo + "\n");
      builder.append(getString(R.string.location_label) + "\t" + currentUser.getLocation() + "\n");
      builder.append(getString(R.string.time_label) + "\t" + calculateTime() + " Minutes" + "\n");
      builder.append(getString(R.string.receipt_end));
      String output = builder.toString();
      printWriter.print(output);
      printWriter.close();
    } else {
    }
  }
}
