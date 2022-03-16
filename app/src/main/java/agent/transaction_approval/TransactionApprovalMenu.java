package agent.transaction_approval;

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
import agent.cashtocashimoney.CashToCashCancelFeesNew;
import agent.transaction_cancel.TransactionCancel;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */
public class TransactionApprovalMenu extends AppCompatActivity implements View.OnClickListener
{
    TextView orderTransferApproval_textview,transactionApproval_textview;
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


        setContentView(R.layout.transaction_approval_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_cashin_menu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.transactionApproval_small));
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



        transactionApproval_textview=(TextView) findViewById(R.id.transactionApproval_textview);
        transactionApproval_textview.setOnClickListener(this);

        orderTransferApproval_textview=(TextView) findViewById(R.id.orderTransferApproval_textview);
        orderTransferApproval_textview.setOnClickListener(this);




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            TransactionApprovalMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.transactionApproval_textview:

                Intent intent=new Intent(TransactionApprovalMenu.this, OrderTransfer.class);
                startActivity(intent);
                // CustomerService.this.finish();

                break;

            case R.id.orderTransferApproval_textview:


                Intent intent2=new Intent(TransactionApprovalMenu.this, OrderTransferApproval.class);
                startActivity(intent2);
                // CustomerService.this.finish();
                break;


        }
    }
}


