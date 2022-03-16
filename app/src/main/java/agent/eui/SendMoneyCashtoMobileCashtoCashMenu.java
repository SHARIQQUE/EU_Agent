package agent.eui;

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
import agent.eui.sendmoney_cashtocash_fragment.DiffrentCountrySendMoneyActivity;
import agent.eui.sendmoney_cashtocash_fragment.ModalFragmentManage;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class SendMoneyCashtoMobileCashtoCashMenu extends AppCompatActivity implements View.OnClickListener
{
    Button cashtoMobile_sameCountry,cashtoMobile_diffrentCountry,cashtoCash_semeCountry,cashtoCash_diffrentCountry;
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

        setContentView(R.layout.sendmoney_cashtomobile_cashtocash_menu);

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


        cashtoMobile_sameCountry=(Button)findViewById(R.id.cashtoMobile_sameCountry);
        cashtoMobile_sameCountry.setOnClickListener(this);


        cashtoMobile_diffrentCountry=(Button)findViewById(R.id.cashtoMobile_diffrentCountry);
        cashtoMobile_diffrentCountry.setOnClickListener(this);


        cashtoCash_semeCountry=(Button)findViewById(R.id.cashtoCash_semeCountry);
        cashtoCash_semeCountry.setOnClickListener(this);


        cashtoCash_diffrentCountry=(Button)findViewById(R.id.cashtoCash_diffrentCountry);
        cashtoCash_diffrentCountry.setOnClickListener(this);

        ModalFragmentManage modalFragmentManage = new ModalFragmentManage();
        modalFragmentManage.setFragment_for_sender("zeroFragment");



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            SendMoneyCashtoMobileCashtoCashMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.cashtoMobile_sameCountry:

                Intent intent2= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this,SendMoneyCashToMobileSameCountry.class);
                startActivity(intent2);
                //CustomerService.this.finish();

                break;

            case R.id.cashtoMobile_diffrentCountry:

                Intent intent3= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this, SendMoneyCashToMobileDifferentCountry.class);
                startActivity(intent3);

                break;


            case R.id.cashtoCash_semeCountry:

                Intent intent4= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this, CashToCashSendMoneySameCountry.class);
                startActivity(intent4);

                break;


            case R.id.cashtoCash_diffrentCountry:


                Intent intent5= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this, DiffrentCountrySendMoneyActivity.class);
                startActivity(intent5);

             /* int  activityFinish=  mComponentInfo.getFinishActivity_sendCash();


                if(activityFinish==1)
                {
                    Intent intent5= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this, DiffrentCountrySendMoneyActivity.class);
                    startActivity(intent5);
                    finish();
                }
                else {
                    Intent intent5= new Intent(SendMoneyCashtoMobileCashtoCashMenu.this, DiffrentCountrySendMoneyActivity.class);
                    startActivity(intent5);

                }*/



                break;
        }
    }
}


