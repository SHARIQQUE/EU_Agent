package agent.report;

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

import agent.activities.PrintMenu;
import agent.activities.R;
import agent.activities.RePrint;
import agent.transaction_approval.OrderTransfer;
import agent.transaction_approval.OrderTransferApproval;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */
public class ReportsPrintReprintMenu extends AppCompatActivity implements View.OnClickListener
{
    TextView reprint_textview,report_menu_textview,print_menu_textview;
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


        setContentView(R.layout.report_reprint_print_menu);

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



        report_menu_textview=(TextView) findViewById(R.id.report_menu_textview);
        report_menu_textview.setOnClickListener(this);

        reprint_textview=(TextView) findViewById(R.id.reprint_textview);
        reprint_textview.setOnClickListener(this);



        print_menu_textview=(TextView) findViewById(R.id.print_menu_textview);
        print_menu_textview.setOnClickListener(this);






    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            ReportsPrintReprintMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.report_menu_textview:

                Intent intent=new Intent(ReportsPrintReprintMenu.this, ReportMenu.class);
                startActivity(intent);
                // CustomerService.this.finish();

                break;

            case R.id.reprint_textview:


                Intent intent2=new Intent(ReportsPrintReprintMenu.this, RePrint.class);
                startActivity(intent2);
                // CustomerService.this.finish();
                break;

            case R.id.print_menu_textview:


                Intent intent3=new Intent(ReportsPrintReprintMenu.this, PrintMenu.class);
                startActivity(intent3);
                // CustomerService.this.finish();
                break;



        }
    }
}


