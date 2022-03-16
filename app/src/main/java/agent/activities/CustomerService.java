package agent.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import commonutilities.ComponentMd5SharedPre;

/**
 * Created by shariq on 06-Mar-17.
 */

public class CustomerService extends AppCompatActivity implements View.OnClickListener{
    Button unblockSubscriber,blockSubscriber,resendSmsConfcode,reSetMpin;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.customer_service);


        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_customerService);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.customerService));
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


        resendSmsConfcode=(Button)findViewById(R.id.resendSmsConfCode);
        resendSmsConfcode.setOnClickListener(this);  // invisible Button  20072017


        unblockSubscriber=(Button)findViewById(R.id.unblockSubscriber);
        unblockSubscriber.setOnClickListener(this);


        reSetMpin=(Button)findViewById(R.id.reSetMpin);
        reSetMpin.setOnClickListener(this);

        blockSubscriber=(Button)findViewById(R.id.blockSubscriber);
        blockSubscriber.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CustomerService.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
       switch(v.getId())
       {



           case R.id.resendSmsConfCode:
              Intent intentSms = new Intent(CustomerService.this, ResendSmsConfCode_new.class);
               startActivity(intentSms);
               break;

           case R.id.unblockSubscriber:
               Intent intentSubs = new Intent(CustomerService.this, UnblockSubscriber.class);
               startActivity(intentSubs);
               break;

           case R.id.blockSubscriber:
               Intent intentunblock= new Intent(CustomerService.this, BlockSubscriber.class);
               startActivity(intentunblock);
               break;


           case R.id.reSetMpin:
               Intent intent5= new Intent(CustomerService.this,ResetMpin.class);
               startActivity(intent5);
               //CustomerService.this.finish();

               break;
       }
    }
}


