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
import android.widget.Toast;

import java.util.Locale;


import agent.tutionfees_fragment.TutionFeesMenu;
import billpay.BillPayActivity;
import commonutilities.ComponentMd5SharedPre;


public class BillpayMenuNew extends AppCompatActivity implements View.OnClickListener
{
    Button button_billpay,button_cashToMarchant,button_tutionfees,button_prepaid_electricity_recharge;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse=mComponentInfo.getmSharedPreferences().getString("languageToUse","");
        if(languageToUse.trim().length()==0){
            languageToUse="fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.billpay_menu_new);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneymenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.purchase_billpayment_deposit));
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


        button_billpay=(Button)findViewById(R.id.button_billpay);
        button_billpay.setOnClickListener(this);


        button_cashToMarchant=(Button)findViewById(R.id.button_cashToMarchant);
        button_cashToMarchant.setOnClickListener(this);


        button_tutionfees=(Button)findViewById(R.id.button_tutionfees);
        button_tutionfees.setOnClickListener(this);


        button_prepaid_electricity_recharge=(Button)findViewById(R.id.button_prepaid_electricity_recharge);
        button_prepaid_electricity_recharge.setOnClickListener(this);




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            BillpayMenuNew.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.button_billpay:

                Intent intent2= new Intent(BillpayMenuNew.this, BillPayActivity.class);
                startActivity(intent2);
                //CustomerService.this.finish();

                break;

            case R.id.button_cashToMarchant:

                Intent intent3= new Intent(BillpayMenuNew.this, CashToMarchant.class);
                startActivity(intent3);

                break;


            case R.id.button_prepaid_electricity_recharge:


                Intent intent5= new Intent(BillpayMenuNew.this, PrepaidBill.class);
                startActivity(intent5);

                break;

            case R.id.button_tutionfees:  // tutionFeees

                Intent intent6= new Intent(BillpayMenuNew.this, TutionFeesMenu.class);
                startActivity(intent6);


                break;

        }
    }
}


