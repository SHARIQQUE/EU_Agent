package agent.purchase_billpayment_deposit_menu.electricity_water;

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
import agent.purchase_billpayment_deposit_menu.electricity_water.electricity.ElectricityPayBillMpinPage;
import agent.purchase_billpayment_deposit_menu.electricity_water.recharge_meter.RechargeElectricityMeter;
import agent.purchase_billpayment_deposit_menu.electricity_water.settle_outstanding.SettleOutstanding;
import commonutilities.ComponentMd5SharedPre;


public class ElectricityWaterMenu extends AppCompatActivity implements View.OnClickListener {
    Button rechargeAmount_button_water, setteleOutstanding_button_water, billPay_button, electricityWater_button_water, rechargeMeter_electricity_button, rechargeAmount_button, setteleOutstanding_button, billPay_button_watter;
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

        setContentView(R.layout.electricity_water_menu);

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


        billPay_button = (Button) findViewById(R.id.billPay_button);
        billPay_button.setOnClickListener(this);

        rechargeMeter_electricity_button = (Button) findViewById(R.id.rechargeMeter_electricity_button);
        rechargeMeter_electricity_button.setOnClickListener(this);

        rechargeAmount_button = (Button) findViewById(R.id.rechargeAmount_button);
        rechargeAmount_button.setOnClickListener(this);

        setteleOutstanding_button = (Button) findViewById(R.id.setteleOutstanding_button);
        setteleOutstanding_button.setOnClickListener(this);


        billPay_button_watter = (Button) findViewById(R.id.billPay_button_watter);
        billPay_button_watter.setOnClickListener(this);

        electricityWater_button_water = (Button) findViewById(R.id.electricityWater_button_water);
        electricityWater_button_water.setOnClickListener(this);


        rechargeAmount_button_water = (Button) findViewById(R.id.rechargeAmount_button_water);
        rechargeAmount_button_water.setOnClickListener(this);

        setteleOutstanding_button_water = (Button) findViewById(R.id.setteleOutstanding_button_water);
        setteleOutstanding_button_water.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            ElectricityWaterMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.billPay_button:

                Intent intent = new Intent(ElectricityWaterMenu.this, ElectricityPayBillMpinPage.class);
                startActivity(intent);

                break;


            case R.id.rechargeMeter_electricity_button:

                Intent inteint = new Intent(ElectricityWaterMenu.this, RechargeElectricityMeter.class);
                startActivity(inteint);

                break;

            case R.id.rechargeAmount_button:

                Toast.makeText(ElectricityWaterMenu.this, "Coming soon", Toast.LENGTH_LONG).show();

                break;

            case R.id.setteleOutstanding_button:

                Intent intitt = new Intent(ElectricityWaterMenu.this, SettleOutstanding.class);
                startActivity(intitt);

                break;


            case R.id.billPay_button_watter:

                Toast.makeText(ElectricityWaterMenu.this, "Bill Pay coming soon", Toast.LENGTH_LONG).show();

                break;

            case R.id.electricityWater_button_water:

                Toast.makeText(ElectricityWaterMenu.this, "coming soon", Toast.LENGTH_LONG).show();

                break;


            case R.id.rechargeAmount_button_water:

                Toast.makeText(ElectricityWaterMenu.this, "coming soon", Toast.LENGTH_LONG).show();

                break;


            case R.id.setteleOutstanding_button_water:

                Toast.makeText(ElectricityWaterMenu.this, "coming soon", Toast.LENGTH_LONG).show();

                break;
        }
    }
}


