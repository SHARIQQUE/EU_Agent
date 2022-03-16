package agent.purchase_billpayment_deposit_menu.electricity_water.electricity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import agent.activities.R;

public class AdapterCheckBox extends BaseAdapter {

    ArrayList<ModalClassStringObject> list;
    //    ModalClassStringObject modalClassStringObject;
    Activity context;
    PackageManager packageManager;
    boolean[] itemChecked;

    public AdapterCheckBox(Activity context, ArrayList<ModalClassStringObject> list) {
        super();
        this.context = context;
        this.list = list;
        itemChecked = new boolean[list.size()];
    }

    private class ViewHolder {
        TextView billnumber_listview_item;
        TextView billDate_listview_item;
        TextView billDueDate_listview_item;
        TextView amount_listview_item;
        CheckBox checkbox_electricityBill;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_electricity_checkbox, null);
            holder = new ViewHolder();

            holder.billnumber_listview_item = (TextView) convertView
                    .findViewById(R.id.billnumber_listview_item);

            holder.billnumber_listview_item = (TextView) convertView.findViewById(R.id.billnumber_listview_item);
            holder.billDate_listview_item = (TextView) convertView.findViewById(R.id.billDate_listview_item);
            holder.billDueDate_listview_item = (TextView) convertView.findViewById(R.id.billDueDate_listview_item);
            holder.amount_listview_item = (TextView) convertView.findViewById(R.id.amount_listview_item);


            holder.checkbox_electricityBill = (CheckBox) convertView
                    .findViewById(R.id.checkbox_electricityBill);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ModalClassStringObject modalClassStringObject = list.get(position);

        holder.billnumber_listview_item.setText(modalClassStringObject.getBillNumber());
        holder.billDate_listview_item.setText(modalClassStringObject.getBillDate());
        holder.billDueDate_listview_item.setText(modalClassStringObject.getBillDueDate());
        holder.amount_listview_item.setText(modalClassStringObject.getAmountBillpay());

        holder.checkbox_electricityBill.setChecked(false);

        if (itemChecked[position]) {
            holder.checkbox_electricityBill.setChecked(true);

        } else {
            holder.checkbox_electricityBill.setChecked(false);
        }

        holder.checkbox_electricityBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkbox_electricityBill.isChecked()) {

                    itemChecked[position] = true;

                    Toast.makeText(context,"Check Box Select ",Toast.LENGTH_LONG).show();

                }


                else {

                    itemChecked[position] = false;

                  //  Toast.makeText(context,"Check Box Un Select ",Toast.LENGTH_LONG).show();

                }
            }
        });

        return convertView;

    }


}
