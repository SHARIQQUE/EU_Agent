package agent.purchase_billpayment_deposit_menu;

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
import android.widget.Toast;

import java.util.Locale;

import agent.activities.R;
import agent.purchase_billpayment_deposit_menu.airtime.Airtime;
import agent.purchase_billpayment_deposit_menu.electricity_water.ElectricityWaterMenu;
import agent.purchase_billpayment_deposit_menu.taxes.TaxesMenu;
import agent.purchase_billpayment_deposit_menu.tv_subscription.TvSubscription;
import commonutilities.ComponentMd5SharedPre;


public class PurchaseBillpaymentDepositMenu extends AppCompatActivity implements View.OnClickListener {
    Button   buyAirtime_button, electricityWater_button, tvSubscription_button,tuitionfees_button,cashToMarchant_button,taxes_button;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;

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

        setContentView(R.layout.purcahse_billpayment_deposit_menu);

        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneymenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.sendMoneyNewPage));
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


        buyAirtime_button = (Button) findViewById(R.id.buyAirtime_button);
        buyAirtime_button.setOnClickListener(this);

        electricityWater_button = (Button) findViewById(R.id.electricityWater_button);
        electricityWater_button.setOnClickListener(this);

        tuitionfees_button = (Button) findViewById(R.id.tuitionfees_button);
        tuitionfees_button.setOnClickListener(this);



        tvSubscription_button = (Button) findViewById(R.id.tvSubscription_button);
        tvSubscription_button.setOnClickListener(this);

        taxes_button = (Button) findViewById(R.id.taxes_button);
        taxes_button.setOnClickListener(this);


        cashToMarchant_button = (Button) findViewById(R.id.cashToMarchant_button);
        cashToMarchant_button.setOnClickListener(this);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            PurchaseBillpaymentDepositMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buyAirtime_button:

                Intent intent = new Intent(PurchaseBillpaymentDepositMenu.this, Airtime.class);
                startActivity(intent);

                break;


            case R.id.electricityWater_button:

                Intent intent2 = new Intent(PurchaseBillpaymentDepositMenu.this, ElectricityWaterMenu.class);
                startActivity(intent2);

                break;

            case R.id.tvSubscription_button:

                Intent intent5 = new Intent(PurchaseBillpaymentDepositMenu.this, TvSubscription.class);
                startActivity(intent5);
                break;

            case R.id.tuitionfees_button:

                Toast.makeText(PurchaseBillpaymentDepositMenu.this, "Tuition Fees", Toast.LENGTH_LONG).show();

                break;

            case R.id.taxes_button:

                Intent intent7 = new Intent(PurchaseBillpaymentDepositMenu.this, TaxesMenu.class);
                startActivity(intent7);

                break;

            case R.id.cashToMarchant_button:

                Toast.makeText(PurchaseBillpaymentDepositMenu.this, "Cash To Merchant  ", Toast.LENGTH_LONG).show();

                break;





        }
    }
}


