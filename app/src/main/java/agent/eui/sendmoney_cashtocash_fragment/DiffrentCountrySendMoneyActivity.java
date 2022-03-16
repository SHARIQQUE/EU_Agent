package agent.eui.sendmoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Locale;

import agent.activities.R;
import agent.eui.SendMoneyCashtoMobileCashtoCashMenu;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class DiffrentCountrySendMoneyActivity extends AppCompatActivity {
    private ProgressDialog mDialog;

    ModalFragmentManage modalFragmentManage = new ModalFragmentManage();

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

        setContentView(R.layout.cashtocash_diffrentcountry_activity);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneymenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.cashToCash_cap));
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

     //   mComponentInfo.setArrowBackButton_sendCash(0);

        loadFragment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {


            int backButton_arrow=mComponentInfo.getArrowBackButton_sendCash();

            System.out.println(backButton_arrow);

           try
           {

               if(backButton_arrow==0){
                   DiffrentCountrySendMoneyActivity.this.finish();
               }

               else  if(backButton_arrow==1){
                   getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_cashtocash, new SenderCountryFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(0);
               }

               else  if(backButton_arrow==2){
                  getSupportFragmentManager().beginTransaction().addToBackStack("frag_one").replace(R.id.frameLayout_cashtocash, new DestinationCountryFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(1);
               }

               else  if(backButton_arrow==3){
                   getSupportFragmentManager().beginTransaction().addToBackStack("frag_two").replace(R.id.frameLayout_cashtocash, new TaxExchangeRateVatPageFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(2);
               }

               else  if(backButton_arrow==4){
                   getSupportFragmentManager().beginTransaction().addToBackStack("frag_three").replace(R.id.frameLayout_cashtocash, new SenderDetailPartOneFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(3);
               }

               else  if(backButton_arrow==5){
                   getSupportFragmentManager().beginTransaction().addToBackStack("frag_four").replace(R.id.frameLayout_cashtocash, new SenderDetailPartSecondFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(4);

               }
               else  if(backButton_arrow==6){
                   getSupportFragmentManager().beginTransaction().addToBackStack("frag_five").replace(R.id.frameLayout_cashtocash, new QuestionAnswerFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(5);

               }

               else  if(backButton_arrow==7){
                   getSupportFragmentManager().beginTransaction().addToBackStack("frag_six").replace(R.id.frameLayout_cashtocash, new QuestionAnswerFragment()).commit();
                   mComponentInfo.setArrowBackButton_sendCash(6);

               }

               else  if(backButton_arrow==8){   // final Receipt page

                   mComponentInfo.setArrowBackButton_sendCash(7);

                   //  getSupportFragmentManager().beginTransaction().addToBackStack("frag_six").replace(R.id.frameLayout_cashtocash, new QuestionAnswerFragment()).commit();

                   FragmentManager fm = DiffrentCountrySendMoneyActivity.this.getSupportFragmentManager();
                   for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                       fm.popBackStack();
                   }


                   Intent intent5= new Intent(DiffrentCountrySendMoneyActivity.this, SendMoneyCashtoMobileCashtoCashMenu.class);
                   startActivity(intent5);
                   DiffrentCountrySendMoneyActivity.this.finish();
               }




               else {
                   DiffrentCountrySendMoneyActivity.this.finish();
               }



           }
           catch (Exception e)
           {
               e.printStackTrace();
           }


            //
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void hideProgressDialog() {
        try {

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_cashtocash, new SenderCountryFragment()).commit();
    }



    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

               FragmentManager fm = DiffrentCountrySendMoneyActivity.this.getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
          }*/


    }



