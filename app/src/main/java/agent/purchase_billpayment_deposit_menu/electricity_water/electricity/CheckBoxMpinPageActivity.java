package agent.purchase_billpayment_deposit_menu.electricity_water.electricity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import agent.activities.R;


public class CheckBoxMpinPageActivity extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener {

   ModalClassStringObject modalClassStringObject;
   ModalClassListObject modalClassListObject;


    ListView listview;
    String mpinString;

    TextView payerMobileNumber_textview_secondPage, billdueDate_textview, amount_textview, fees_textview, billDate_textview, destinationCountry_textview_secondpage, subscriberNumber_textview, subscriberName_textview, totalAmount_textview_secondPage, billnumber_textview, vat_textview;

    Button button_next,button_submit;

    LinearLayout firstPage_linearlayout, secondPage_linearlayout,mpinpage_second_page_linearlayout;

    private EditText mpinEditText_secondPage;




    ArrayList<ModalClassStringObject> listData;
    ArrayList<String> billNumber_list;
    ArrayList<String> billDate_list;
    ArrayList<String> billDueDate_list;
    ArrayList<String> amount_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkbox_mpinpage_activity);

        listview = (ListView) findViewById(R.id.listview);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);



        button_next = (Button) findViewById(R.id.button_next);
        button_next.setOnClickListener(this);



        mpinEditText_secondPage = (EditText) findViewById(R.id.mpinEditText_secondPage);
        mpinEditText_secondPage.setOnEditorActionListener(this);


        destinationCountry_textview_secondpage = (TextView) findViewById(R.id.destinationCountry_textview_secondpage);
        subscriberNumber_textview = (TextView) findViewById(R.id.subscriberNumber_textview);
        subscriberName_textview = (TextView) findViewById(R.id.subscriberName_textview);
        billnumber_textview = (TextView) findViewById(R.id.billnumber_textview);
        vat_textview = (TextView) findViewById(R.id.vat_textview);
        billDate_textview = (TextView) findViewById(R.id.billDate_textview);
        totalAmount_textview_secondPage = (TextView) findViewById(R.id.totalAmount_textview_secondPage);
        billdueDate_textview = (TextView) findViewById(R.id.billdueDate_textview);
        amount_textview = (TextView) findViewById(R.id.amount_textview);
        fees_textview = (TextView) findViewById(R.id.fees_textview);
        payerMobileNumber_textview_secondPage = (TextView) findViewById(R.id.payerMobileNumber_textview_secondPage);


        firstPage_linearlayout = (LinearLayout) findViewById(R.id.firstPage_linearlayout);
        secondPage_linearlayout = (LinearLayout) findViewById(R.id.secondPage_linearlayout);
        mpinpage_second_page_linearlayout = (LinearLayout) findViewById(R.id.mpinpage_second_page_linearlayout);


        billNumber_list = new ArrayList<String>();

        billNumber_list.add("111111111111");
        billNumber_list.add("2222222222");
        billNumber_list.add("3333333333333");
        billNumber_list.add("444444444444");
        billNumber_list.add("5555555555555");
        billNumber_list.add("66666666666");
        billNumber_list.add("7777777777777");
        billNumber_list.add("88888888888888");
        billNumber_list.add("999999999999");
        billNumber_list.add("10101010101010");
        billNumber_list.add("11121212121212");
        billNumber_list.add("1011111111111");
        billNumber_list.add("12121212121212");
        billNumber_list.add("13131313131313");
        billNumber_list.add("14141414141414");
        billNumber_list.add("15151515151515");


        billDate_list = new ArrayList<String>();

        billDate_list.add("21-11-2001");
        billDate_list.add("21-11-2002");
        billDate_list.add("21-11-2003");
        billDate_list.add("21-11-2004");
        billDate_list.add("21-11-2005");
        billDate_list.add("21-11-2006");
        billDate_list.add("21-11-2007");
        billDate_list.add("21-11-2008");
        billDate_list.add("21-11-2009");
        billDate_list.add("21-11-20010");
        billDate_list.add("21-11-20011");
        billDate_list.add("21-11-20012");
        billDate_list.add("21-11-20013");
        billDate_list.add("21-11-20014");
        billDate_list.add("21-11-20015");


        billDueDate_list = new ArrayList<String>();

        billDueDate_list.add("00-00-2001");
        billDueDate_list.add("00-00-2002");
        billDueDate_list.add("00-00-2003");
        billDueDate_list.add("00-00-2004");
        billDueDate_list.add("00-00-2005");
        billDueDate_list.add("00-00-2006");
        billDueDate_list.add("00-00-2007");
        billDueDate_list.add("00-00-2008");
        billDueDate_list.add("00-00-2009");
        billDueDate_list.add("00-00-20010");
        billDueDate_list.add("00-00-20011");
        billDueDate_list.add("00-00-20012");
        billDueDate_list.add("00-00-20013");
        billDueDate_list.add("00-00-20014");
        billDueDate_list.add("00-00-20015");


        amount_list = new ArrayList<String>();

        amount_list.add("100");
        amount_list.add("200");
        amount_list.add("300");
        amount_list.add("400");
        amount_list.add("500");
        amount_list.add("600");
        amount_list.add("700");
        amount_list.add("800");
        amount_list.add("900");
        amount_list.add("1000");
        amount_list.add("1100");
        amount_list.add("1200");
        amount_list.add("1300");
        amount_list.add("1400");
        amount_list.add("1500");


        countryListData();

        AdapterCheckBox adapter = new AdapterCheckBox(CheckBoxMpinPageActivity.this, listData);
        listview.setAdapter(adapter);
    }

    private boolean validation_mpinPage() {

        boolean ret = false;
        mpinString = mpinEditText_secondPage.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;

        } else {
            Toast.makeText(CheckBoxMpinPageActivity.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();

        }

        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);  //this shows three dots at right corner on click settings open
        return true;
    }


    private void countryListData() {


        listData = new ArrayList<ModalClassStringObject>();
        modalClassListObject = new ModalClassListObject();

        String response = "{\n" +
                "  \"agentcode\": \"237100001012\",\n" +
                "  \"appversionDRCBA\": \"ANDROID,true,1,1.1,1.0,0,0\",\n" +
                "  \"appversion\": \"ANDROID,true,3,1.2,1.0,0,0123\",\n" +
                "  \"appversionfr\": \"ANDROID,true,3,1.2,1.0,0,0123\",\n" +
                "  \"resultcode\": \"0\",\n" +
                "  \"countrylist\": [\n" +
                "    {\n" +
                "      \"countrycode\": \"IND\",\n" +
                "      \"isocode\": \"91\",\n" +
                "      \"countryname\": \"India\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"1000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"CAM\",\n" +
                "      \"isocode\": \"237\",\n" +
                "      \"countryname\": \"Cameroon\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"GBN\",\n" +
                "      \"isocode\": \"241\",\n" +
                "      \"countryname\": \"Gabon\",\n" +
                "      \"mobilelength\": \"8\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"CNG\",\n" +
                "      \"isocode\": \"242\",\n" +
                "      \"countryname\": \"Congo\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"TCH\",\n" +
                "      \"isocode\": \"235\",\n" +
                "      \"countryname\": \"Tchad\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"RCA\",\n" +
                "      \"isocode\": \"236\",\n" +
                "      \"countryname\": \"Central African Republic\",\n" +
                "      \"mobilelength\": \"8\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"DRC\",\n" +
                "      \"isocode\": \"243\",\n" +
                "      \"countryname\": \"Democratic Republic of Congo\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"DBI\",\n" +
                "      \"isocode\": \"971\",\n" +
                "      \"countryname\": \"Dubai - United Arab Emirates\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"IND2\",\n" +
                "      \"isocode\": \"911\",\n" +
                "      \"countryname\": \"India2\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"10000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"CAM2\",\n" +
                "      \"isocode\": \"2377\",\n" +
                "      \"countryname\": \"Cameroon2\",\n" +
                "      \"mobilelength\": \"98\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"9000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"GBN2\",\n" +
                "      \"isocode\": \"2414\",\n" +
                "      \"countryname\": \"Gabon2\",\n" +
                "      \"mobilelength\": \"8\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"9000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"CNG2\",\n" +
                "      \"isocode\": \"2420\",\n" +
                "      \"countryname\": \"Congo2\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"90000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"TCH2\",\n" +
                "      \"isocode\": \"2355\",\n" +
                "      \"countryname\": \"Tchad2\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"90000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"RCA2\",\n" +
                "      \"isocode\": \"2366\",\n" +
                "      \"countryname\": \"Central African Republic22\",\n" +
                "      \"mobilelength\": \"88\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"90000\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"DRC2\",\n" +
                "      \"isocode\": \"2433\",\n" +
                "      \"countryname\": \"Democratic Republic of Congo2\",\n" +
                "      \"mobilelength\": \"999\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"58952\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"DBI2\",\n" +
                "      \"isocode\": \"971\",\n" +
                "      \"countryname\": \"Dubai - United Arab Emirates2\",\n" +
                "      \"mobilelength\": \"999\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"23598\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"countrycode\": \"BDS\",\n" +
                "      \"isocode\": \"880\",\n" +
                "      \"countryname\": \"Bangladesh\",\n" +
                "      \"mobilelength\": \"9\",\n" +
                "      \"booleanValue\": \"false\",\n" +
                "      \"cashoutamount\": \"0\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"apversionmer\": \"ANDROID,true,1,1.1,1.0,0,0\",\n" +
                "  \"resultdescription\": \"Transaction Successful\",\n" +
                "  \"requestcts\": \"25/05/2016 18:01:51\",\n" +
                "  \"apversionbrc\": \"ANDROID,true,1,1.1,1.0,0,0\",\n" +
                "  \"responsects\": \"14/12/2018 11:16:29\",\n" +
                "  \"apversionba\": \"ANDROID,true,1,1.1,1.0,0,0\",\n" +
                "  \"transid\": \"8017978\"\n" +
                "}";
        try {

            JSONObject jsonObject = new JSONObject(response);

            String agentCode = jsonObject.getString("agentcode");
            String appVersion = jsonObject.getString("appversion");
            String resultCode = jsonObject.getString("resultcode");

            String countryData = jsonObject.getString("countrylist");
            JSONArray jsonArray = new JSONArray(countryData);

            listData = new ArrayList<ModalClassStringObject>();
            modalClassListObject = new ModalClassListObject();

            for (int i = 0; i < jsonArray.length(); i++) {

                modalClassStringObject = new ModalClassStringObject();    // Object Create 1

                jsonObject = jsonArray.getJSONObject(i);

                modalClassStringObject.setBillNumber(jsonObject.getString("countrycode"));
                modalClassStringObject.setBillDate(jsonObject.getString("isocode"));
                modalClassStringObject.setBillDueDate(jsonObject.getString("countryname"));
                modalClassStringObject.setAmountBillpay(jsonObject.getString("cashoutamount"));

                listData.add(modalClassStringObject);
                modalClassListObject.setListData(listData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.button_next:
            {
                Toast.makeText(CheckBoxMpinPageActivity.this, "button_submit", Toast.LENGTH_LONG).show();

                firstPage_linearlayout.setVisibility(View.GONE);
                button_next.setVisibility(View.GONE);
                secondPage_linearlayout.setVisibility(View.VISIBLE);
                mpinpage_second_page_linearlayout.setVisibility(View.VISIBLE);

                Toast.makeText(CheckBoxMpinPageActivity.this, "button_submit", Toast.LENGTH_LONG).show();

                destinationCountry_textview_secondpage.setText("INDIA");
                subscriberNumber_textview.setText("9718196849");
                subscriberName_textview.setText("Sharique Anwar");
                billnumber_textview.setText("40000");
                billDate_textview.setText("21-11-2019");
                billdueDate_textview.setText("21-11-2002");
                payerMobileNumber_textview_secondPage.setText("9718196849199");
                amount_textview.setText("72500");
                fees_textview.setText("3 %");
                vat_textview.setText("36");
                totalAmount_textview_secondPage.setText("1000000");

                break;
            }

            case R.id.button_submit:
            {
                validation_mpinPage();

                break;
            }

        }


    }

     private void hideKeyboard() {
         InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                 InputMethodManager.HIDE_NOT_ALWAYS);
     }

     @Override
     public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
         try {//confCode_EditText_BankingActivation 2131558542 / 52

             if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                 hideKeyboard();

                 //  validation Details


             }

         } catch (Exception e) {
             e.printStackTrace();
         }
         return false;
     }

 }