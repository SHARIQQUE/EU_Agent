package billpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import adapter.BillSelectionAdapter;
import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;
import model.BillModel;


/**
 * Created by AdityaBugalia on 13/10/16.
 */

public class BillSelectionCustomerId extends AppCompatActivity implements View.OnClickListener, AutoCompleteTextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    private ListView listView;


    private Button selectAllBtn, proceedBtn;
    private ComponentMd5SharedPre mComponentInfo;

    Spinner payerAccountsSpinner, selectedInvoiceSpinner_Review;

    String[] payerBankAccountsArray, selectedInvoiceArray, payerBankAccountCodeArray;

    ArrayList<BillModel> billList;
    ArrayList<String> selectedBillList;
    String[] billSelectionArray;
    private ProgressDialog mDialog;

    private LinearLayout input_LL;
    private ScrollView review_LL;

    private AutoCompleteTextView mpinEditText;
    String billerSelectiondata;

    private String mpinString = "", payerAccountString = "", payerAccountCodeString = "", selectedBillString = "";
    int layoutLevel = 0;
    TextView billerCountry_TxtView_Review, billerName_TxtView_Review,
            billerPreference_TxtView_Review, customerId_TxtView_Review,

    payerAccount_TxtView_Review;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "en";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.billselection_customerid);


        listView = (ListView) findViewById(R.id.listView_BillSelection);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(this);


        selectAllBtn = (Button) findViewById(R.id.selectAll_BillSelection);
        proceedBtn = (Button) findViewById(R.id.nextButton_BillSelection);
        input_LL = (LinearLayout) findViewById(R.id.input_LL_BillSelection);
        review_LL = (ScrollView) findViewById(R.id.review_SV_BillSelection);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpin_EditText__Review_BillSelection);
        mpinEditText.setOnEditorActionListener(this);

        mpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });



        selectAllBtn.setOnClickListener(this);
        proceedBtn.setOnClickListener(this);

        billList = new ArrayList<BillModel>();
        selectedBillList = new ArrayList<String>();

        ViewGeneration vg = new ViewGeneration(0);

        vg.execute();

        payerAccountsSpinner = (Spinner) findViewById(R.id.spinner_PayerAccount_BillSelection);

        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        billerSelectiondata = mComponentInfo.getmSharedPreferences().getString("billSelectionData", "");
        if (bankAccounts.trim().length() > 5) {
            // Looper.prepare();
            String[] data = bankAccounts.split("\\;");


            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.accounttodebit));
            accountCodeList.add(getString(R.string.accounttodebit));


            payerBankAccountsArray = new String[data.length + 1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);

            payerBankAccountCodeArray = new String[data.length + 1];
            payerBankAccountCodeArray[0] = getString(R.string.payeraccount);

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                //  if(tData[4].equalsIgnoreCase("MA")) {
                accountList.add(tData[0] + " - " + tData[1]);

                accountCodeList.add(tData[4]);
                //    }


            }


            payerBankAccountsArray = accountList.toArray(new String[accountList.size()]);
            payerBankAccountCodeArray = accountCodeList.toArray(new String[accountCodeList.size()]);
        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.payeraccount);

            payerBankAccountCodeArray = new String[1];
            payerBankAccountCodeArray[0] = getString(R.string.payeraccount);
        }

       // payerAccountsSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));


        billerCountry_TxtView_Review = (TextView) findViewById(R.id.billerCountry_TxtView_Review_BillSelection);
        billerName_TxtView_Review = (TextView) findViewById(R.id.billerName_TxtView_Review_BillSelection);

        billerPreference_TxtView_Review = (TextView) findViewById(R.id.billPreference_TxtView_Review_BillSelection);
        customerId_TxtView_Review = (TextView) findViewById(R.id.customerId_TxtView_Review_BillSelection);


        payerAccount_TxtView_Review = (TextView) findViewById(R.id.payerAccount_TxtView_Review_BillSelection);
        selectedInvoiceSpinner_Review = (Spinner) findViewById(R.id.spinner_SelectedInvoice_BillSelection);


    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (validateDetails()) {

                proceedFinalBillLevel();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.selectAll_BillSelection:
                ViewGeneration vg = new ViewGeneration(1);
                vg.execute();

                break;

            case R.id.nextButton_BillSelection:


                if (validateDetails()) {

                    proceedFinalBillLevel();
                }

                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {


            case R.id.listView_BillSelection:
                CheckBox cb = (CheckBox) ((ViewGroup) (((ViewGroup) view).getChildAt(1))).getChildAt(0);
                cb.setChecked(!cb.isChecked());

                if (cb.isChecked()) {
                    cb.setVisibility(View.VISIBLE);
                    //cb.setButtonDrawable(R.drawable.checkboxselector);
                    cb.setBackgroundResource(R.drawable.checkboxselector);
                } else {
                    cb.setVisibility(View.INVISIBLE);

                }
                billList.get(position).setSelected(cb.isChecked());
                break;
        }

    }


    class ViewGeneration extends AsyncTask<Void, Void, Void> {
        int viewGenerationCase;

        ViewGeneration(int viewGenerationCase) {
            this.viewGenerationCase = viewGenerationCase;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideProgressDialog();
            showProgressDialog(getString(R.string.pleasewait));


        }

        @Override
        protected Void doInBackground(Void... voids) {

            switch (viewGenerationCase) {
                case 0:

                    String billData = mComponentInfo.getmSharedPreferences().getString("unPaidBills", "'");
                    String[] tempBills = billData.split("\\|");
                    BillModel billModel;
                    for (int i = 0; i < tempBills.length; i++) {
                        billModel = new BillModel();
                        String billDetailString = tempBills[i];
                        String[] tempBillDetailData = billDetailString.split("\\;");


                        billModel.setBillName(tempBillDetailData[0]);
                        billModel.setDueDate(tempBillDetailData[1]);
                        billModel.setAmount(tempBillDetailData[2]);
                        billModel.setFeeAmount(tempBillDetailData[3]);
                        billModel.setSelected(false);
                        billList.add(billModel);

                        billModel = new BillModel();


                    }

                    break;

                case 1:
                    for (int i = 0; i < billList.size(); i++) {

                        billList.get(i).setSelected(true);

                    }

                    break;
                case 2:
                    selectedBillList = new ArrayList<String>();
                    for (int i = 0; i < billList.size(); i++) {
                        if (billList.get(i).isSelected()) {
                            selectedBillList.add(billList.get(i).getBillName() + ";" + billList.get(i).getDueDate() + ";" + billList.get(i).getAmount()+";" + billList.get(i).getFeeAmount());

                        }

                    }
                    break;
                case 3:
                    selectedInvoiceArray = new String[selectedBillList.size()];
                    for (int i = 0; i < selectedBillList.size(); i++) {
                        selectedInvoiceArray[i] = selectedBillList.get(i);
                        if (selectedBillString.trim().length() == 0) {
                            selectedBillString = selectedInvoiceArray[i];

                        } else {
                            selectedBillString = selectedBillString + "," + selectedInvoiceArray[i];
                        }

                    }


                    break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
            BillSelectionAdapter adapter;
            switch (viewGenerationCase) {
                case 0:


                    BillSelectionCustomerId.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BillSelectionAdapter adapter = new BillSelectionAdapter(BillSelectionCustomerId.this, billList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(BillSelectionCustomerId.this);
                        }
                    });


                    break;

                case 1:
                    BillSelectionCustomerId.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BillSelectionAdapter adapter = new BillSelectionAdapter(BillSelectionCustomerId.this, billList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(BillSelectionCustomerId.this);
                        }
                    });

                    break;
                case 2:


                    if (selectedBillList.size() > 0) {
                     //   if (payerAccountsSpinner.getSelectedItemPosition() > 0) {
                            mpinString = mpinEditText.getText().toString().trim();
                           // payerAccountString = payerAccountsSpinner.getSelectedItem().toString().trim().split("\\-")[0];
                           // payerAccountCodeString = payerBankAccountCodeArray[payerAccountsSpinner.getSelectedItemPosition()];
                            mComponentInfo.getmSharedPreferences().edit().putString("payerAccountCodeString", payerAccountCodeString).commit();

                            setLayout(layoutLevel = 1);

                      /*  } else {

                            Toast.makeText(BillSelectionCustomerId.this, getString(R.string.pleaseselectaccount), Toast.LENGTH_SHORT).show();
                        }*/

                    } else {

                        Toast.makeText(BillSelectionCustomerId.this, getString(R.string.pleaseselectonebill), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    selectedInvoiceSpinner_Review.setAdapter(new ArrayAdapter<String>(BillSelectionCustomerId.this, R.layout.selected_bills_spinner_item_structure, selectedInvoiceArray));
                    billerCountry_TxtView_Review.setText(billSelectionArray[0]);
                    billerPreference_TxtView_Review.setText(billSelectionArray[1]);
                    billerName_TxtView_Review.setText(billSelectionArray[2]);
                    customerId_TxtView_Review.setText(billSelectionArray[3]);
                   // payerAccount_TxtView_Review.setText(payerAccountString);
                    break;


            }
        }

    }


    private void setLayout(int layoutLevel) {

        switch (layoutLevel) {

            case 0:
                payerAccountsSpinner.setSelection(0);
                input_LL.setVisibility(View.VISIBLE);
                review_LL.setVisibility(View.GONE);
                mpinEditText.setText("");

                selectAllBtn.setVisibility(View.VISIBLE);

                break;


            case 1:
                input_LL.setVisibility(View.GONE);
                review_LL.setVisibility(View.VISIBLE);
                selectAllBtn.setVisibility(View.GONE);
                proceedBtn.setText(getString(R.string.nextNew));
                billSelectionArray = billerSelectiondata.split("\\|");
                ViewGeneration vg = new ViewGeneration(3);
                vg.execute();

                break;
        }
    }

    void proceedFinalBillLevel() {

        billerSelectiondata = billerSelectiondata + "|" + selectedBillString + "|" + payerAccountString + "|" + mpinString;

        mComponentInfo.getmSharedPreferences().edit().putString("billSelectionData", billerSelectiondata).commit();
        Intent i = new Intent(BillSelectionCustomerId.this, BillSelectionCustomerFinal.class);
        startActivity(i);
        BillSelectionCustomerId.this.finish();
    }


    private boolean validateDetails() {
        boolean ret = false;


        switch (layoutLevel) {

            case 0:
                ViewGeneration vg = new ViewGeneration(2);
                vg.execute();

                break;


            case 1:
                mpinString = mpinEditText.getText().toString().trim();
                if (mpinString.length() == 4) {


                    ret = true;
                } else {

                    Toast.makeText(BillSelectionCustomerId.this, getString(R.string.prompt_mPin), Toast.LENGTH_SHORT).show();
                }
                break;
        }


        return ret;

    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(BillSelectionCustomerId.this);
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

}
