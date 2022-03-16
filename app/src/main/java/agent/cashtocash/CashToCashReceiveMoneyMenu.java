package agent.cashtocash;

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
import agent.eui.receivemoney_cashtocash_fragment.ReceiveMoneyCashToCashDiffrentCountryActivity;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class CashToCashReceiveMoneyMenu extends AppCompatActivity implements View.OnClickListener
{
    Button cashtocashReceivemoneySamecountry,cashtocashReceivemoneyDiffrentcountry;
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

        setContentView(R.layout.cashtocash_receive_money_menu);

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

        cashtocashReceivemoneySamecountry=(Button)findViewById(R.id.cashtocashReceivemoneySamecountry);
        cashtocashReceivemoneySamecountry.setOnClickListener(this);

        cashtocashReceivemoneyDiffrentcountry=(Button)findViewById(R.id.cashtocashReceivemoneyDiffrentcountry);
        cashtocashReceivemoneyDiffrentcountry.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            CashToCashReceiveMoneyMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.cashtocashReceivemoneySamecountry:

                Intent intent=new Intent(CashToCashReceiveMoneyMenu.this,CashToCashReceiveMoneySameCountryNew.class);
                startActivity(intent);

                break;

            case R.id.cashtocashReceivemoneyDiffrentcountry:



                Intent intent2=new Intent(CashToCashReceiveMoneyMenu.this, ReceiveMoneyCashToCashDiffrentCountryActivity.class);
                startActivity(intent2);

                break;
        }
    }
}


