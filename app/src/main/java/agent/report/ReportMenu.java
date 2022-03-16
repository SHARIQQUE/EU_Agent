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
import android.widget.Button;

import java.util.Locale;

import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class ReportMenu extends AppCompatActivity implements View.OnClickListener {

    Button transactionDetaislReport,commissionReport,agentBankReport;
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


        setContentView(R.layout.report_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_reports);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.reports));
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

        transactionDetaislReport = (Button) findViewById(R.id.transactionDetaislReport);
        transactionDetaislReport.setOnClickListener(this);

        commissionReport = (Button) findViewById(R.id.commissionReport);
        commissionReport.setOnClickListener(this);

        agentBankReport = (Button) findViewById(R.id.agentBankReport);
        agentBankReport.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            ReportMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.transactionDetaislReport:

                Intent intent = new Intent(ReportMenu.this, TransactionDetailsReport.class);
                startActivity(intent);
                // CashOutMenu.this.finish();
                break;

            case R.id.commissionReport:
                Intent intent2 = new Intent(ReportMenu.this, CommissionReportMenu.class);
                startActivity(intent2);
                // CashOutMenu.this.finish();
                break;

            case R.id.agentBankReport:
                Intent intent4 = new Intent(ReportMenu.this, AgentBankReport.class);
                startActivity(intent4);
                // CashOutMenu.this.finish();
                break;


        }
    }
}
