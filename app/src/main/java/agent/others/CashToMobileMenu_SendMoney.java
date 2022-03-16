/*
package shariq.eu_agent_new;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import commonutilities.ComponentMd5SharedPre;

*/
/**
 * Created by sharique on 13-Mar-17.
 *//*

public class CashToMobileMenu_SendMoney extends AppCompatActivity implements View.OnClickListener
{
    Button remmitance_receive_money_menu,remmitance_send_money_menu;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashtomobile_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmitanceMenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.moneyTransferMenu));
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

        remmitance_send_money_menu=(Button)findViewById(R.id.remmitance_send_money_menu);
        remmitance_send_money_menu.setOnClickListener(this);

        remmitance_receive_money_menu=(Button)findViewById(R.id.remmitance_receive_money_menu);
        remmitance_receive_money_menu.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            CashToMobileMenu_SendMoney.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.remmitance_send_money_menu:

                Intent intent=new Intent(CashToMobileMenu_SendMoney.this,CashToMobileMenu_SendMoney.class);
                startActivity(intent);
                // CustomerService.this.finish();
                break;

            case R.id.remmitance_receive_money_menu:
                Intent intent2= new Intent(CashToMobileMenu_SendMoney.this,RemmettanceSendMoneySameCountry.class);
                startActivity(intent2);
                //CustomerService.this.finish();

                break;
        }
    }
}


*/
