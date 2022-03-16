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
import agent.activities.PrintMenu;
import agent.activities.R;
import agent.activities.RePrint;
import agent.activities.TariffNew;
import agent.activities.TransactionApproval;
import agent.internationalremittance.view.InternationalRemittanceActivity;
import agent.report.ReportMenu;
import agent.transaction_cancel.TransactionCancelMenu;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by Shariq on 18-05-2017.
 */


public class AccountManagement extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar mToolbar;
    String agentName, agentCode;
    Button languageChange, changeMpin;
    ComponentMd5SharedPre mComponentInfo;
    Button internationalRemittance,transactionApproval, transactionCancel, prints, tariff, reports, RePrints, customerService, logout, otherAccount, pictureSign;
    Intent intent;
    Spinner spinner_Language_Customerservice;


    private ScrollView menuScrollView;
    private LinearLayout changeLanguageLayout;
    Button buttonChangeLanguage;

    boolean isArabicSelected = false, isNothingSelected = false;

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


        setContentView(R.layout.account_management);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_AccountManagement);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.accountmanagemnet_new));
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

        transactionApproval = (Button) findViewById(R.id.transactionApproval);
        transactionApproval.setOnClickListener(this);

        internationalRemittance = (Button) findViewById(R.id.internationalRemittance);
        internationalRemittance.setOnClickListener(this);




        buttonChangeLanguage = (Button) findViewById(R.id.customerService_CustomerService);
        buttonChangeLanguage.setOnClickListener(this);

        spinner_Language_Customerservice = (Spinner) findViewById(R.id.spinner_Language_Tariff);
        spinner_Language_Customerservice.setOnItemSelectedListener(this);

        menuScrollView = (ScrollView) findViewById(R.id.menuScrollView);
        changeLanguageLayout = (LinearLayout) findViewById(R.id.changeLanguageLayout_LL);
        transactionCancel = (Button) findViewById(R.id.transactionCancel);
        transactionCancel.setOnClickListener(this);

        tariff = (Button) findViewById(R.id.tariff);
         tariff.setOnClickListener(this);

        reports = (Button) findViewById(R.id.reports);
         reports.setOnClickListener(this);

        RePrints = (Button) findViewById(R.id.RePrints);
         RePrints.setOnClickListener(this);     // invisible Button

        customerService = (Button) findViewById(R.id.customerService);
        customerService.setOnClickListener(this);

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        otherAccount = (Button) findViewById(R.id.otherAccount);
        otherAccount.setOnClickListener(this);

        pictureSign = (Button) findViewById(R.id.pictureSign);
        pictureSign.setOnClickListener(this);

        prints = (Button) findViewById(R.id.prints);
          prints.setOnClickListener(this);      // invisible Button

        languageChange = (Button) findViewById(R.id.languageChange);
        languageChange.setOnClickListener(this);

        changeMpin = (Button) findViewById(R.id.changeMpin);
        changeMpin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.transactionApproval:
                intent = new Intent(AccountManagement.this, TransactionApproval.class);
                startActivity(intent);
                break;


            case R.id.transactionCancel:
                intent = new Intent(AccountManagement.this, TransactionCancelMenu.class);
                startActivity(intent);
                break;

            case R.id.tariff:
                intent = new Intent(AccountManagement.this, TariffNew.class);
                startActivity(intent);
                break;

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
                break;*/

            case R.id.pictureSign:
                intent = new Intent(AccountManagement.this, PictureSign.class);
                startActivity(intent);
                break;


            case R.id.changeMpin:
                Intent intent2 = new Intent(AccountManagement.this, ChangeMpin.class);
                startActivity(intent2);
                //CustomerService.this.finish();
                break;





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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            AccountManagement.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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