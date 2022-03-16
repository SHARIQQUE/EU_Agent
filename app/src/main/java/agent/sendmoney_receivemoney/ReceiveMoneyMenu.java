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
import android.widget.TextView;

import java.util.Locale;

import agent.activities.R;
import agent.eui.receivemoney_cashtocash_fragment.ReceiveMoneyCashToCashDiffrentCountryActivity;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class ReceiveMoneyMenu extends AppCompatActivity implements View.OnClickListener
{
    TextView eraMobile_Button,remmitance_send_money_toname;
    TextView cashToCash_button;
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


        setContentView(R.layout.receive_money_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneymenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.receiveMoney));
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


        eraMobile_Button=(TextView)findViewById(R.id.eraMobile_Button);
        eraMobile_Button.setOnClickListener(this);

        remmitance_send_money_toname=(TextView)findViewById(R.id.remmitance_send_money_toname);
        remmitance_send_money_toname.setOnClickListener(this);

        cashToCash_button=(TextView)findViewById(R.id.cashToCash_button);
        cashToCash_button.setOnClickListener(this);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            ReceiveMoneyMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {


           case R.id.eraMobile_Button:

                Intent intent2= new Intent(ReceiveMoneyMenu.this,ReceiveMoneyCashToMobileSameCountry.class);
                startActivity(intent2);
                ReceiveMoneyMenu.this.finish();   //  finish not display one back menu

                break;

            case R.id.cashToCash_button:


                Intent intent3=new Intent(ReceiveMoneyMenu.this, ReceiveMoneyCashToCashDiffrentCountryActivity.class);
                startActivity(intent3);

                ReceiveMoneyMenu.this.finish();  //  finish not display one back menu


                break;

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    finish();


    }
}


