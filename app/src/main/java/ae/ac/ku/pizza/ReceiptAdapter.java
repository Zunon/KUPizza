package ae.ac.ku.pizza;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {
  ArrayList<String> mNames = new ArrayList<>();
  ArrayList<Integer>
    mQuantities = new ArrayList<>(),
    mPrices = new ArrayList<>();

  public ReceiptAdapter(HashMap<String, Integer[]> mCart) {
    for(String item : mCart.keySet()) {
      mNames.add(item);
      mQuantities.add(mCart.get(item)[0]);
      mPrices.add(mCart.get(item)[1]);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt, parent, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    viewHolder.itemText.setText(String.valueOf(mNames.get(i)));
    viewHolder.quantityText.setText(String.valueOf(mQuantities.get(i)));
    viewHolder.priceText.setText(String.valueOf(mPrices.get(i)));
  }

  @Override
  public int getItemCount() {
    return mNames.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    TextView
      itemText, quantityText, priceText, xText, aedText;
    LinearLayout item;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      itemText = itemView.findViewById(R.id.itemText);
      quantityText = itemView.findViewById(R.id.quantityText);
      xText = itemView.findViewById(R.id.xText);
      priceText = itemView.findViewById(R.id.priceText);
      aedText = itemView.findViewById(R.id.aedText);
      item = itemView.findViewById(R.id.receiptItem);
    }
  }
}
