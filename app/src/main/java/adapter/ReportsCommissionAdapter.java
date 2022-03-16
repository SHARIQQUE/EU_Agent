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


public class ReportsCommissionAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> dList;
    String TxID = "", amt = "", txType = "", stat = "";
    boolean isOtherAccount;

    public ReportsCommissionAdapter(Context context, ArrayList<String> list, boolean isOtherAccount) {

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
            convertView = inflater.inflate(R.layout.report_commision_listview_item, null);
        }

        TextView textView_date,transatypeTxtViewTitle,textView_agentTransid,textview_source,textView_fees;

        textView_date = (TextView) convertView.findViewById(R.id.textView_date);
        textView_agentTransid = (TextView) convertView.findViewById(R.id.textView_agentTransid);
        textview_source = (TextView) convertView.findViewById(R.id.textview_source);
        textView_fees = (TextView) convertView.findViewById(R.id.textView_fees);




        String[] data = dList.get(arg0).split("\\|");

        textView_agentTransid.setText(data[0]);
        textView_fees.setText(data[1]);
        textview_source.setText(data[2]);
        textView_date.setText(data[3]);


        return convertView;
    }



}