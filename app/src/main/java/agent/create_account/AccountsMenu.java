package agent.create_account;

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
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Locale;

import adapter.AdapterGridView;
import agent.activities.OtherAccount;
import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by Sahrique on 14/03/17.
 */

public class AccountsMenu extends AppCompatActivity {
    Toolbar mToolbar;
    GridView gridview;
    String languageToUse;
    Intent intent;
    String agentName, agentCode;
    boolean isServerOperationInProcess;
    public static int[] prgmImages =
            {
                    R.drawable.createaccount1,
                    R.drawable.createaccount1,
                    R.drawable.createaccount1,
                    R.drawable.createaccount1,
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComponentMd5SharedPre mComponentInfo;

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
         languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }

        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.accounts);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_accounts);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.accounts_capital));
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


        gridview = (GridView) findViewById(R.id.gridview);


        String createAccount = (getString(R.string.createAccount));
        String updateAccount = (getString(R.string.updateAccount_capital));
        String viewAccountProfile = (getString(R.string.viewAccountProfile_capital));
        String otherAccount = (getString(R.string.otherAccount_capital));

        String[] prgmNameList = {createAccount, updateAccount, viewAccountProfile, otherAccount};

        gridview.setAdapter(new AdapterGridView(this, prgmNameList, prgmImages));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                switch (position) {

                    case 0:

                        try {


                            if (languageToUse.equalsIgnoreCase("en")) {
                                intent = new Intent(AccountsMenu.this, CreateAccountNewEnglish.class);
                                startActivity(intent);
                                AccountsMenu.this.finish();
                            }

                            else if (languageToUse.equalsIgnoreCase("fr")) {
                                intent = new Intent(AccountsMenu.this, CreateAccountNewFrench.class);
                                startActivity(intent);
                                AccountsMenu.this.finish();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        break;

                    case 1:


                        try {


                            if (languageToUse.equalsIgnoreCase("en")) {
                                intent = new Intent(AccountsMenu.this, UpdateAccountNewEnglish.class);
                                startActivity(intent);
                                AccountsMenu.this.finish();
                            }

                            else if (languageToUse.equalsIgnoreCase("fr")) {
                                intent = new Intent(AccountsMenu.this, UpdateAccountNewFrench.class);
                                startActivity(intent);
                                AccountsMenu.this.finish();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        break;

                    case 2:
                        intent = new Intent(AccountsMenu.this, ViewProfile.class);
                        startActivity(intent);
                        AccountsMenu.this.finish();
                        break;

                    case 3:
                        intent = new Intent(AccountsMenu.this, OtherAccount.class);
                        startActivity(intent);
                        break;
                }
                return;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if (!isServerOperationInProcess) {
                AccountsMenu.this.finish();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}