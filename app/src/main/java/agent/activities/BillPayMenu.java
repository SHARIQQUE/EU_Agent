package agent.activities;

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
import android.widget.Button;

import java.util.Locale;

import billpay.BillPayActivity;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class BillPayMenu extends AppCompatActivity implements View.OnClickListener {

    Button billPaybill, billpayDepostPTOP, prepaidElectricity;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;

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


        setContentView(R.layout.billpay_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_billpayMenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.billPaymentMenuNew));
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

        billPaybill = (Button) findViewById(R.id.billPaybill);
        billPaybill.setOnClickListener(this);

        billpayDepostPTOP = (Button) findViewById(R.id.billpayDepostPTOP);
         billpayDepostPTOP.setOnClickListener(this);

        prepaidElectricity = (Button) findViewById(R.id.prepaidElectricity);
        prepaidElectricity.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            BillPayMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.billPaybill:

                Intent intent = new Intent(BillPayMenu.this, BillPayActivity.class);
                startActivity(intent);
                // CashOutMenu.this.finish();
                break;

            case R.id.billpayDepostPTOP:
                Intent intent2 = new Intent(BillPayMenu.this, CashToMarchant.class);
                startActivity(intent2);
                // CashOutMenu.this.finish();
                break;

            case R.id.prepaidElectricity:
                Intent intent4 = new Intent(BillPayMenu.this, PrepaidBill.class);
                startActivity(intent4);
                // CashOutMenu.this.finish();
                break;
        }
    }
}
