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


public class CashOutWithdrawalMenu extends AppCompatActivity implements View.OnClickListener {

    Button sameBranch,diffrentBranch,cashWithdrawal;
    Toolbar mToolbar;
    String agentName, agentCode;
    ComponentMd5SharedPre mComponentInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casout_withdrawal_menu);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_MoneyTransfer);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashOut));
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

        sameBranch = (Button) findViewById(R.id.sameBranch);
        sameBranch.setOnClickListener(this);

        diffrentBranch = (Button) findViewById(R.id.diffrentBranch);
        diffrentBranch.setOnClickListener(this);

        cashWithdrawal = (Button) findViewById(R.id.cashWithdrawal);
        cashWithdrawal.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            CashOutWithdrawalMenu.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.sameBranch:

                Intent intent=new Intent(CashOutWithdrawalMenu.this,CashOutSameBranch.class);
                startActivity(intent);
               // CashOutMenu.this.finish();
                break;

            case R.id.diffrentBranch:
                Intent intent2= new Intent(CashOutWithdrawalMenu.this,CashOutWithdrawalCode.class);
                startActivity(intent2);
               // CashOutMenu.this.finish();

                break;

            case R.id.cashWithdrawal:
                Intent intent3= new Intent(CashOutWithdrawalMenu.this,CashOutWithdrawalinitiateOtp.class);
                startActivity(intent3);
                // CashOutMenu.this.finish();
                break;
        }
    }
}
*/
