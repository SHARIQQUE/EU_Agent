package agent.other_services;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import agent.activities.ChangeMpin;
import agent.activities.CustomerService;
import agent.activities.LoginActivity;
import agent.activities.PictureSign;
import agent.activities.R;
import agent.activities.TariffNew;
import agent.activities.TransactionApproval;
import agent.internationalremittance.view.InternationalRemittanceActivity;
import agent.report.ReportMenu;
import agent.report.ReportsPrintReprintMenu;
import agent.transaction_approval.OrderTransfer;
import agent.transaction_approval.OrderTransferApproval;
import agent.transaction_cancel.TransactionCancelMenu;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */
public class OtherServiceNew extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    Spinner spinner_Language_Customerservice;
    Toolbar mToolbar;
    String agentName, agentCode;
    boolean isArabicSelected = false, isNothingSelected = false;
    Button customerService_CustomerService;

    ComponentMd5SharedPre mComponentInfo;
    Button  changeMpin_linearlayout,orderTransferApproval_linearlayout, changeLanguage_linearlayout,order_transfer_linearlayout, logout_linearlayout, customerServices_linearlayout, pictureSign_linearlayout, tariff_linearlayout, transactionApproval_linearlayout, reports_print_linearlayout, transaction_cancel_linearlayout;
   LinearLayout main_linearlayout,linearLayout_changelanguageSetting_details;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.otherservice_new);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_otheraccount);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.otherAccount_capital));
        mToolbar.setSubtitle("" + agentName);
        //  mToolbar.setSubtitle(agentcode);
        // mToolbar.setSubtitle("Please provide details to proceed further");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        try {

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

            Log.e("", "" + e.toString());
        }
        spinner_Language_Customerservice = (Spinner) findViewById(R.id.spinner_Language_Customerservice);
        spinner_Language_Customerservice.setOnItemSelectedListener(this);


        customerService_CustomerService = (Button) findViewById(R.id.customerService_CustomerService);
        customerService_CustomerService.setOnClickListener(this);

        transactionApproval_linearlayout = (Button) findViewById(R.id.transactionApproval_linearlayout);
        transactionApproval_linearlayout.setOnClickListener(this);

        reports_print_linearlayout = (Button) findViewById(R.id.reports_print_linearlayout);
        reports_print_linearlayout.setOnClickListener(this);

        transaction_cancel_linearlayout = (Button) findViewById(R.id.transaction_cancel_linearlayout);
        transaction_cancel_linearlayout.setOnClickListener(this);

        customerServices_linearlayout = (Button) findViewById(R.id.customerServices_linearlayout);
        customerServices_linearlayout.setOnClickListener(this);

        pictureSign_linearlayout = (Button) findViewById(R.id.pictureSign_linearlayout);
        pictureSign_linearlayout.setOnClickListener(this);


        tariff_linearlayout = (Button) findViewById(R.id.tariff_linearlayout);
        tariff_linearlayout.setOnClickListener(this);

        order_transfer_linearlayout = (Button) findViewById(R.id.order_transfer_linearlayout);
        order_transfer_linearlayout.setOnClickListener(this);




        orderTransferApproval_linearlayout = (Button) findViewById(R.id.orderTransferApproval_linearlayout);
        orderTransferApproval_linearlayout.setOnClickListener(this);




        main_linearlayout = (LinearLayout) findViewById(R.id.main_linearlayout);


        changeMpin_linearlayout = (Button) findViewById(R.id.changeMpin_linearlayout);
        changeMpin_linearlayout.setOnClickListener(this);


        changeLanguage_linearlayout = (Button) findViewById(R.id.changeLanguage_linearlayout);
        changeLanguage_linearlayout.setOnClickListener(this);


        linearLayout_changelanguageSetting_details = (LinearLayout) findViewById(R.id.linearLayout_changelanguageSetting_details);

        logout_linearlayout = (Button) findViewById(R.id.logout_linearlayout);
        logout_linearlayout.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            OtherServiceNew.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.transactionApproval_linearlayout:

                Intent intent = new Intent(OtherServiceNew.this, TransactionApproval.class);
                startActivity(intent);

                break;

            case R.id.reports_print_linearlayout:

                Intent intent2 = new Intent(OtherServiceNew.this, ReportsPrintReprintMenu.class);
                startActivity(intent2);

                break;

            case R.id.transaction_cancel_linearlayout:

                Intent intent3 = new Intent(OtherServiceNew.this, TransactionCancelMenu.class);
                startActivity(intent3);

                break;

            case R.id.customerServices_linearlayout:

                Intent intent4 = new Intent(OtherServiceNew.this, CustomerService.class);
                startActivity(intent4);

                break;

            case R.id.pictureSign_linearlayout:

                Intent intent5 = new Intent(OtherServiceNew.this, PictureSign.class);
                startActivity(intent5);

                break;


            case R.id.customerService_CustomerService:

                if (!isNothingSelected) {
                    mComponentInfo.getmSharedPreferences().edit().putBoolean("isLoggedIn", false).commit();
                    Intent i = new Intent(OtherServiceNew.this, LoginActivity.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {

                    Toast.makeText(OtherServiceNew.this, getString(R.string.languageSelect), Toast.LENGTH_LONG).show();

                }

                break;


            case R.id.tariff_linearlayout:

                Intent intent6 = new Intent(OtherServiceNew.this, TariffNew.class);
                startActivity(intent6);

                break;


            case R.id.order_transfer_linearlayout:

                Intent intent8 = new Intent(OtherServiceNew.this, OrderTransfer.class);
                startActivity(intent8);

                break;

            case R.id.orderTransferApproval_linearlayout:

                Intent intent9 = new Intent(OtherServiceNew.this, OrderTransferApproval.class);
                startActivity(intent9);

                break;




            case R.id.changeMpin_linearlayout:

                Intent intent7 = new Intent(OtherServiceNew.this, ChangeMpin.class);
                startActivity(intent7);

                break;


            case R.id.changeLanguage_linearlayout:

                linearLayout_changelanguageSetting_details.setVisibility(View.VISIBLE);
                main_linearlayout.setVisibility(View.GONE);

                break;


            case R.id.logout_linearlayout:

                Intent i = new Intent(OtherServiceNew.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                OtherServiceNew.this.finish();

                break;


            case R.id.internationalRemittance:

                Intent intent10 = new Intent(OtherServiceNew.this, InternationalRemittanceActivity.class);
                startActivity(intent10);


                break;


                /*





            case R.id.reports:
                intent = new Intent(AccountManagement.this, ReportMenu.class);
                startActivity(intent);
                break;

            case R.id.RePrints:
                intent = new Intent(AccountManagement.this, RePrint.class);
                startActivity(intent);
                break;

            case R.id.prints:
                intent = new Intent(AccountManagement.this, PrintMenu.class);
                startActivity(intent);
                break;

            case R.id.customerService:
                intent = new Intent(AccountManagement.this, CustomerService.class);
                startActivity(intent);
                break;

          /*  case R.id.otherAccount:
                intent = new Intent(AccountManagement.this, OtherAccount.class);
                startActivity(intent);
                break;*//*





            case R.id.languageChange:

                menuScrollView.setVisibility(View.GONE);
                changeLanguageLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.customerService_CustomerService:

                if (!isNothingSelected) {
                    mComponentInfo.getmSharedPreferences().edit().putBoolean("isLoggedIn", false).commit();
                    i = new Intent(AccountManagement.this, LoginActivity.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {

                    Toast.makeText(AccountManagement.this,getString(R.string.languageSelect), Toast.LENGTH_LONG).show();

                }

                break;

            case R.id.internationalRemittance:

                Intent intent6 = new Intent(AccountManagement.this, InternationalRemittanceActivity.class);
                startActivity(intent6);


                break;



            case R.id.logout:

                i = new Intent(AccountManagement.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                AccountManagement.this.finish();
*/


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (spinner_Language_Customerservice.getSelectedItemPosition()) {
            case 0:
                //  Toast.makeText(this, "Please select language", Toast.LENGTH_SHORT).show();
                isArabicSelected = false;
                isNothingSelected = true;
                break;
            case 1:
                mComponentInfo.getmSharedPreferences().edit().putString("languageToUse", "en").commit();
                isArabicSelected = false;
                isNothingSelected = false;
                break;
            case 2:
                mComponentInfo.getmSharedPreferences().edit().putString("languageToUse", "fr").commit();
                isArabicSelected = false;
                isNothingSelected = false;
                ;
                break;
            case 3:
                isArabicSelected = true;
                isNothingSelected = false;
                break;


        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


