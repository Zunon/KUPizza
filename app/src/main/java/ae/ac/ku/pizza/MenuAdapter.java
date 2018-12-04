package ae.ac.ku.pizza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
  private static final String TAG = "MenuAdapter";

  private ArrayList<String> mNames = new ArrayList<>();
  private ArrayList<Integer> mPrices = new ArrayList<>();
  private Context mContext;

  public MenuAdapter(ArrayList<String> mNames, ArrayList<Integer> mPrices, Context mContext) {
    this.mNames = mNames;
    this.mPrices = mPrices;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    String labelTag =mNames.get(i).toLowerCase().replace(' ', '_');
    viewHolder.label.setText(mNames.get(i));
    viewHolder.price.setText(mPrices.get(i).toString());
    viewHolder.increment.setTag(labelTag + "inc");
    viewHolder.decrement.setTag(labelTag + "dec");
    viewHolder.add.setTag(labelTag + "add");
    viewHolder.number.setTag(labelTag + "num");
    viewHolder.price.setTag(labelTag + "csh");
    viewHolder.label.setTag(labelTag + "txt");
  }

  @Override
  public int getItemCount() {
    return mNames.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    Button
      add, increment, decrement;
    TextView
      number, label, price;
    CardView card;
    ConstraintLayout constraintLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      add = itemView.findViewById(R.id.button);
      increment = itemView.findViewById(R.id.increment);
      decrement = itemView.findViewById(R.id.decrement);
      number = itemView.findViewById(R.id.number);
      label = itemView.findViewById(R.id.text);
      card = itemView.findViewById(R.id.card);
      price = itemView.findViewById(R.id.price);
      constraintLayout = itemView.findViewById(R.id.constraintLayout);
    }
  }
}
