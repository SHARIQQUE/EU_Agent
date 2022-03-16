package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import agent.activities.OtherAccount;
import agent.activities.R;
import callback.CallbackFromAdapter;
import callback.CallbackFromAdapterOrderApproval;

public class OrderTransferApprovalrBaseAdapter extends BaseAdapter {
    Context context;
    Button submitButton;
    ArrayList<String> listData;
    String[] data;
    OtherAccount otherAccount = new OtherAccount();
    String StrData;
    CallbackFromAdapter callbackFromAdapter;


    private ProgressDialog mDialog;

    static class ViewHolderItem {
        TextView orderForm_textview, transactionId_textview, amount_textview, ordertoName_textview, orderFromName_textview, actionTxtView, orderto_textview;

    }

    public OrderTransferApprovalrBaseAdapter(Context context, ArrayList<String> list) {

        this.context = context;
        this.listData = list;
        Log.e("", "" + listData.toString());
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_ordertrasfer_approval, null);

            viewHolder = new ViewHolderItem();
            viewHolder.orderForm_textview = (TextView) convertView.findViewById(R.id.orderForm_textview);
            viewHolder.ordertoName_textview = (TextView) convertView.findViewById(R.id.ordertoName_textview);
            viewHolder.orderto_textview = (TextView) convertView.findViewById(R.id.orderto_textview);
            viewHolder.orderFromName_textview = (TextView) convertView.findViewById(R.id.orderFromName_textview);
            viewHolder.amount_textview = (TextView) convertView.findViewById(R.id.amount_textview);
            viewHolder.transactionId_textview = (TextView) convertView.findViewById(R.id.transactionId_textview);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
            viewHolder.orderForm_textview = (TextView) convertView.findViewById(R.id.orderForm_textview);
            viewHolder.ordertoName_textview = (TextView) convertView.findViewById(R.id.ordertoName_textview);
            viewHolder.orderto_textview = (TextView) convertView.findViewById(R.id.orderto_textview);
            viewHolder.orderFromName_textview = (TextView) convertView.findViewById(R.id.orderFromName_textview);
            viewHolder.amount_textview = (TextView) convertView.findViewById(R.id.amount_textview);
            viewHolder.transactionId_textview = (TextView) convertView.findViewById(R.id.transactionId_textview);


        }


        data = listData.get(arg0).split("\\|");


        viewHolder.orderForm_textview.setText(data[0]);
        viewHolder.orderFromName_textview.setText(data[1]);
        viewHolder.orderto_textview.setText(data[2]);
        viewHolder.ordertoName_textview.setText(data[3]);
        viewHolder.amount_textview.setText(data[4]);
        viewHolder.transactionId_textview.setText(data[5]);


        return convertView;

    }

}


