package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import agent.activities.R;
import model.BillModel;

/**
 * Created by AdityaBugalia on 13/10/16.
 */

public class BillSelectionAdapter extends BaseAdapter {

    private ArrayList<BillModel> billList;
    private Context ctx;

    public BillSelectionAdapter(Context context, ArrayList<BillModel> billList) {

        this.billList = new ArrayList<BillModel>();
        this.billList.addAll(billList);
        this.ctx = context;

    }

    private class ViewHolder {
        TextView billNumber;
        TextView dueDate;
        TextView amount;
        TextView feeAmount;
        CheckBox isCheck;
    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int i) {
        return billList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.billselection_listview_item, null);

            holder = new ViewHolder();
            holder.billNumber = (TextView) convertView.findViewById(R.id.billNoTextView_BillSelection_Item_TxtView);
            holder.dueDate = (TextView) convertView.findViewById(R.id.dueDateTextView_BillSelection_Item_TxtView);
            holder.amount = (TextView) convertView.findViewById(R.id.amountTextView_BillSelection_Item_TxtView);
            holder.feeAmount = (TextView) convertView.findViewById(R.id.feeAmountTextView_BillSelection_Item_TxtView);
            holder.isCheck = (CheckBox) convertView.findViewById(R.id.billNoTextView_BillSelection_Item_CheckBox);
            convertView.setTag(holder);


//            holder.isCheck.setOnClickListener( new View.OnClickListener() {
//                public void onClick(View v) {
//                    CheckBox cb = (CheckBox) v ;
//                    BillModel billModel = (BillModel) cb.getTag();
//                    Toast.makeText(ctx,
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
//                    billModel.setSelected(cb.isChecked());
//                }
//            });
//
//
//            holder.isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    CheckBox cb = (CheckBox) compoundButton ;
//                    BillModel billModel = (BillModel) cb.getTag();
//                    Toast.makeText(ctx,
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
//                    billModel.setSelected(cb.isChecked());
//                }
//            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BillModel billModel = billList.get(position);

        holder.billNumber.setText(billModel.getBillName());
        holder.dueDate.setText(billModel.getDueDate());
        holder.amount.setText("XAF " + billModel.getAmount());
        holder.feeAmount.setText("XAF " + billModel.getFeeAmount());
        if (billModel.isSelected()) {
            holder.isCheck.setChecked(billModel.isSelected());
            holder.isCheck.setVisibility(View.VISIBLE);
        } else {
            holder.isCheck.setChecked(billModel.isSelected());
            holder.isCheck.setVisibility(View.INVISIBLE);

        }
        holder.isCheck.setChecked(billModel.isSelected());
        holder.billNumber.setTag(billModel);


        return convertView;

    }

}
