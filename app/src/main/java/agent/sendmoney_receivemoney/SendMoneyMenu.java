/*
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
import commonutilities.ComponentMd5SharedPre;
import agent.cashtocash.CashToCashSendMoneyMenu;

*/
/**
 * Created by sharique on 13-Mar-17.
 *//*


public class SendMoneyMenu extends AppCompatActivity implements View.OnClickListener
{
    Button eraMobile_Button,remmitance_send_money_toname;
    Button cashToCash_button;
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

        setContentView(R.layout.sendmoney_menu);

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


        eraMobile_Button=(Button)findViewById(R.id.eraMobile_Button);
        eraMobile_Button.setOnClickListener(this);

        remmitance_send_money_toname=(Button)findViewById(R.id.remmitance_send_money_toname);
        remmitance_send_money_toname.setOnClickListener(this);

        cashToCash_button=(Button)findViewById(R.id.cashToCash_button);
        cashToCash_button.setOnClickListener(this);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            SendMoneyMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

         */
/*   case R.id.remmitance_send_money_toname:

                Intent intent=new Intent(SendMoneyMenu.this,CashToMobileMenu_SendMoney.class);
                startActivity(intent);
                // CustomerService.this.finish();
                break;*//*


            case R.id.eraMobile_Button:
                Intent intent2= new Intent(SendMoneyMenu.this,CashToMobileMenu.class);
                startActivity(intent2);
                //CustomerService.this.finish();

                break;

            case R.id.cashToCash_button:

                Intent intent3= new Intent(SendMoneyMenu.this,CashToCashSendMoneyMenu.class);
                startActivity(intent3);
                break;

        }
    }
}


*/
