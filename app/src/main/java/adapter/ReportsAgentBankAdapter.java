package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import agent.activities.R;


public class ReportsAgentBankAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> dList;
    String TxID = "", amt = "", txType = "", stat = "";
    boolean isOtherAccount;

    public ReportsAgentBankAdapter(Context context, ArrayList<String> list, boolean isOtherAccount) {

        this.context = context;
        this.dList = list;
        this.isOtherAccount = isOtherAccount;
        Log.e("", "" + dList.toString());
    }

    @Override
    public int getCount() {
        return dList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return dList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.report_agentbank_listview_item, null);
        }

        TextView textView_debit,textView_credit,textView_closing,textView_date,textView_transactionType,textView_transactionId,textView_transaction_amount;

        textView_date = (TextView) convertView.findViewById(R.id.textView_date);
        textView_transactionId = (TextView) convertView.findViewById(R.id.textView_transactionId);

        textView_transaction_amount = (TextView) convertView.findViewById(R.id.textView_transaction_amount);
        textView_transactionType = (TextView) convertView.findViewById(R.id.textView_transactionType);

        textView_debit = (TextView) convertView.findViewById(R.id.textView_debit);
        textView_credit = (TextView) convertView.findViewById(R.id.textView_credit);
        textView_closing = (TextView) convertView.findViewById(R.id.textView_closing);




        String[] data = dList.get(arg0).split("\\|");

        textView_transactionId.setText(data[6]);         //transaction ID
        textView_transaction_amount.setText(data[3]);    // Transaction amount
        textView_date.setText(data[1]);                  //Transaction Date
        textView_transactionType.setText(data[2]);            // Transaction Type
        textView_debit.setText(data[4]);                      // Debit
        textView_credit.setText(data[5]);                     // Credit
        textView_closing.setText(data[0]);                 // Closing


        return convertView;
    }



}