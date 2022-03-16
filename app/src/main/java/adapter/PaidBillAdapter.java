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
 * Created by Shariq on 02-08-2017.
 */

public class PaidBillAdapter  extends BaseAdapter {

    private ArrayList<BillModel> billList;
    private Context ctx;

    public PaidBillAdapter(Context context,       ArrayList<BillModel> billList) {

        this.billList = new ArrayList<BillModel>();
        this. billList.addAll(billList);
        this.ctx=context;

    }

    private class ViewHolder {
        TextView billNumber;
        TextView dueDate;
        TextView amount;
        TextView feeAmount;
        TextView totalAmount;
        CheckBox isCheck;
        TextView name;
        TextView customerName;
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
            LayoutInflater vi = (LayoutInflater)ctx.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.paidbill_listview_itemstructure, null);

            holder = new ViewHolder();
            holder.billNumber = (TextView) convertView.findViewById(R.id.billNoTextView_BillsPaid_Item_TxtView);
            holder.dueDate = (TextView) convertView.findViewById(R.id.dueDateTextView_BillsPaid_Item_TxtView);
            holder.amount = (TextView) convertView.findViewById(R.id.amountTextView_BillsPaid_Item_TxtView);
            holder.totalAmount = (TextView) convertView.findViewById(R.id.totalAmountTextView_BillsPaid_Item_TxtView);
            holder.feeAmount = (TextView) convertView.findViewById(R.id.feeAmountTextView_BillsPaid_Item_TxtView);
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView_BillsPaid_Item_TxtView);
            holder.customerName = (TextView) convertView.findViewById(R.id.customerNameTextView_BillsPaid_Item_TxtView);
            holder.isCheck = (CheckBox) convertView.findViewById(R.id.billNoTextView_BillsPaid_Item_CheckBox);
            convertView.setTag(holder);


//            holder.isCheck.setOnClickListener( new View.OnClickListener() {
//                public void onClick(View v) {
//                    CheckBox cb = (CheckBox) v ;
//                    BillModel BillModel = (BillModel) cb.getTag();
//                    Toast.makeText(ctx,
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
//                    BillModel.setSelected(cb.isChecked());
//                }
//            });
//
//
//            holder.isCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    CheckBox cb = (CheckBox) compoundButton ;
//                    BillModel BillModel = (BillModel) cb.getTag();
//                    Toast.makeText(ctx,
//                            "Clicked on Checkbox: " + cb.getText() +
//                                    " is " + cb.isChecked(),
//                            Toast.LENGTH_LONG).show();
//                    BillModel.setSelected(cb.isChecked());
//                }
//            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        BillModel BillModel = billList.get(position);

        holder.billNumber.setText(BillModel.getBillName());
        holder.dueDate.setText(BillModel.getDueDate());
        holder.amount.setText("XAF "+BillModel.getTotalAmount());
        holder.name.setText(BillModel.getName());
        holder.customerName.setText(BillModel.getCustomerName());
        holder.feeAmount.setText("XAF "+BillModel.getFeeAmount());
        holder.totalAmount.setText("XAF "+BillModel.getAmount());

        holder.billNumber.setTag(BillModel);


        return convertView;

    }

}
