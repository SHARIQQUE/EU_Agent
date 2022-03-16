package agent.sendmoney_receivemoney;

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

import agent.activities.R;
import agent.eui.SendMoneyCashToMobileDifferentCountry;
import agent.eui.SendMoneyCashToMobileSameCountry;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class CashToMobileMenu extends AppCompatActivity implements View.OnClickListener
{
    Button eraMobileNational,eraMobileInternational;
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


        setContentView(R.layout.cash_to_mobile_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
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


        eraMobileNational=(Button)findViewById(R.id.eraMobileNational);
        eraMobileNational.setOnClickListener(this);

        eraMobileInternational=(Button)findViewById(R.id.eraMobileInternational);
        eraMobileInternational.setOnClickListener(this);




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CashToMobileMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.eraMobileNational:
                Intent intent2= new Intent(CashToMobileMenu.this, SendMoneyCashToMobileSameCountry.class);
                startActivity(intent2);
                //CustomerService.this.finish();

                break;

            case R.id.eraMobileInternational:

                Intent intent=new Intent(CashToMobileMenu.this, SendMoneyCashToMobileDifferentCountry.class);
                startActivity(intent);
                // CustomerService.this.finish();
                break;





        }
    }
}


