

package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import agent.activities.R;
import callback.CallbackFromAdapter;
import agent.activities.OtherAccount;
import model.OtherAccountModel;

public class OtherAccountBaseAdapter extends BaseAdapter {
    Context context;

    ArrayList<OtherAccountModel> listData;
    String[] data;
    OtherAccount otherAccount = new OtherAccount();
    String StrData;
    CallbackFromAdapter callbackFromAdapter;
    private ProgressDialog mDialog;
    int pos ;

    public OtherAccountBaseAdapter(Context context, ArrayList<OtherAccountModel> list) {

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



    static class ViewHolderItem {
        TextView accountNumber, mobilebankingServiceTxtView, accountTypeTitle,titleTxtView;
        Button activerSelectedButton,activerNonSelectButton;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolderItem viewHolder;

        pos = position;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_other_account, null);

            viewHolder = new ViewHolderItem();

            viewHolder.accountNumber = (TextView) convertView.findViewById(R.id.accountNumber);
            viewHolder.mobilebankingServiceTxtView = (TextView) convertView.findViewById(R.id.mobilebankingServiceTxtView);
            viewHolder.titleTxtView = (TextView) convertView.findViewById(R.id.titleTxtView);
            viewHolder.accountTypeTitle = (TextView) convertView.findViewById(R.id.accountTypeTitle);

            viewHolder.activerSelectedButton = (Button) convertView.findViewById(R.id.activerSelectedButton);
            viewHolder.activerNonSelectButton = (Button) convertView.findViewById(R.id.activerNonSelectButton);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();

            viewHolder.accountNumber = (TextView) convertView.findViewById(R.id.accountNumber);
            viewHolder.mobilebankingServiceTxtView = (TextView) convertView.findViewById(R.id.mobilebankingServiceTxtView);
            viewHolder.titleTxtView = (TextView) convertView.findViewById(R.id.titleTxtView);
            viewHolder.accountTypeTitle = (TextView) convertView.findViewById(R.id.accountTypeTitle);

            viewHolder.activerSelectedButton = (Button) convertView.findViewById(R.id.activerSelectedButton);
            viewHolder.activerNonSelectButton = (Button) convertView.findViewById(R.id.activerNonSelectButton);

        }


        viewHolder.accountNumber.setText(listData.get(position).getAccountNumber());
        viewHolder.titleTxtView.setText(listData.get(position).getAcctypedescription());
        viewHolder.accountTypeTitle.setText(listData.get(position).getAccounttitle());
        viewHolder.mobilebankingServiceTxtView.setText(listData.get(position).getAccountstatusdescription());


        if (listData.get(position).getAccountstatusdescription().equalsIgnoreCase("Active")) {

            viewHolder.activerSelectedButton.setVisibility(View.GONE);
            viewHolder.activerNonSelectButton.setVisibility(View.VISIBLE);
        }
        if (listData.get(position).getAccountstatusdescription().equalsIgnoreCase("Pending Approval")) {

            viewHolder.activerSelectedButton.setVisibility(View.GONE);
            viewHolder.activerNonSelectButton.setVisibility(View.VISIBLE);
        }

        if (listData.get(position).getAccountstatusdescription().equalsIgnoreCase("InActive")) {

            viewHolder.activerNonSelectButton.setVisibility(View.GONE);
            viewHolder.activerSelectedButton.setVisibility(View.VISIBLE);
        }

        viewHolder.activerSelectedButton = (Button) convertView.findViewById(R.id.activerSelectedButton);
        viewHolder.activerSelectedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    if(listData.get(position).getAccountstatusdescription().equalsIgnoreCase("InActive"))
                    {

                        ((CallbackFromAdapter)context).activerOtherAccountRequestOnServer_CallbackFromAdapter(listData.get(position).getAccountNumber(), listData.get(position).getAccounttitle(), listData.get(position).getAccountstatusdescription(),listData.get(position).getAccountTypeCode());

                    }
                    else {

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                //  ((CallbackFromAdapter)context).activerOtherAccountRequestOnServer_CallbackFromAdapter(securityData[0], securityData[2], securityData[4],securityData[3]);
            }
        });



        return convertView;

    }

}


