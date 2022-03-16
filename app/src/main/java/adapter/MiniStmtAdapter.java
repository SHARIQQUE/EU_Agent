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

public class MiniStmtAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> dList;
    String TxID = "", amt = "", txType = "", stat = "";
    boolean isOtherAccount;

    public MiniStmtAdapter(Context context, ArrayList<String> list, boolean isOtherAccount) {

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
            convertView = inflater.inflate(R.layout.transaction_history_listview_item_structure, null);
        }

        TextView txnTxtView,transatypeTxtViewTitle,amtTxtView, sourceTxtView, sourceTxtViewTitle, destinationTxtView, destinationTxtViewTitle, dateTxtView;
        txnTxtView = (TextView) convertView.findViewById(R.id.txnTxtView);
        amtTxtView = (TextView) convertView.findViewById(R.id.amountTxtView);
        sourceTxtView = (TextView) convertView.findViewById(R.id.sourceTxtView);
        destinationTxtView = (TextView) convertView.findViewById(R.id.destinationTxtView);
        dateTxtView = (TextView) convertView.findViewById(R.id.dateTxtView);
        sourceTxtViewTitle = (TextView) convertView.findViewById(R.id.sourceTxtViewTitle);
        destinationTxtViewTitle = (TextView) convertView.findViewById(R.id.destinationTxtViewTitle);
        transatypeTxtViewTitle = (TextView) convertView.findViewById(R.id.transatypeTxtViewTitle);


        if (isOtherAccount) {
            sourceTxtViewTitle.setText(context.getString(R.string.directionMiniStmt));
            destinationTxtViewTitle.setText(context.getString(R.string.currencyMiniStmt));
        }

        String[] data = dList.get(arg0).split("\\|");

        txnTxtView.setText(data[0]);
        amtTxtView.setText(data[1]);
        sourceTxtView.setText(data[2]);
        destinationTxtView.setText(data[3]);
        dateTxtView.setText(data[4]);
        transatypeTxtViewTitle.setText(data[5]);

        return convertView;
    }



}