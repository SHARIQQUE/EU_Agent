package agent.cashtocashimoney;

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
import billpay.BillPayActivity;
import commonutilities.ComponentMd5SharedPre;



public class CashToCashImoneyMenu extends AppCompatActivity implements View.OnClickListener {

    Button button_cashfortransferDeposit, button_cashfortransferWithdrawal, button_cashfortransferSendCashCasncel;
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


        setContentView(R.layout.cashtocashimoney_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_billpayMenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashtocashImoney_capital));
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

        button_cashfortransferDeposit = (Button) findViewById(R.id.button_cashfortransferDeposit);
        button_cashfortransferDeposit.setOnClickListener(this);

        button_cashfortransferWithdrawal = (Button) findViewById(R.id.button_cashfortransferWithdrawal);
        button_cashfortransferWithdrawal.setOnClickListener(this);

        button_cashfortransferSendCashCasncel = (Button) findViewById(R.id.button_cashfortransferSendCashCasncel);
        button_cashfortransferSendCashCasncel.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            CashToCashImoneyMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_cashfortransferDeposit:

                /*Intent intent = new Intent(CashToCashImoneyMenu.this, CashToCashTransferDeposit.class);
                startActivity(intent);*/


                break;

            case R.id.button_cashfortransferWithdrawal:

               /* Intent intent2 = new Intent(CashToCashImoneyMenu.this, CashToCashTransferWitthdrawal.class);
                startActivity(intent2);*/



                break;

            case R.id.button_cashfortransferSendCashCasncel:

                Intent gghg = new Intent(CashToCashImoneyMenu.this, CashToCashCancelFeesNew.class);
                startActivity(gghg);


                //Toast.makeText(CashToCashImoneyMenu.this, "Click ", Toast.LENGTH_SHORT).show();


                break;
        }
    }
}
