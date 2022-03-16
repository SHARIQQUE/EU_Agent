package agent.eui.receivemoney_cashtocash_fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Locale;

import agent.activities.R;
import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class ReceiveMoneyCashToCashDiffrentCountryActivity extends AppCompatActivity
{
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

        setContentView(R.layout.receivemoney_cashtocash_diffrentcountry_activity);

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


        loadFragment();

    }
    
 
    private void loadFragment() {
         getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_cashtocash_receivemoney,new SearchReferenceNumberPageDemo()).commit();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {


            int backButton_arrow=mComponentInfo.getArrowBackButton_receiveCash();

            System.out.println(backButton_arrow);

            try
            {

                if(backButton_arrow==1){
                    ReceiveMoneyCashToCashDiffrentCountryActivity.this.finish();
                }

                else  if(backButton_arrow==2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_cashtocash_receivemoney,new SearchReferenceNumberPageDemo()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(1);
                }

                else  if(backButton_arrow==3){
                    getSupportFragmentManager().beginTransaction().addToBackStack("frag_question").replace(R.id.frameLayout_cashtocash_receivemoney, new QuestionAnswerReceiveMoneyFragment()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(2);
                }

                else  if(backButton_arrow==4){
                    getSupportFragmentManager().beginTransaction().addToBackStack("frag_sender_Receipt_transfer").replace(R.id.frameLayout_cashtocash_receivemoney, new ReceiveMoneySenderDetailsRecipientDetailTransferDetailsFragment()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(3);
                }

                else  if(backButton_arrow==5){
                     getSupportFragmentManager().beginTransaction().addToBackStack("frag_part_one").replace(R.id.frameLayout_cashtocash_receivemoney, new RecipientDetailPartOneFragment()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(4);
                }

                else  if(backButton_arrow==6){
                    getSupportFragmentManager().beginTransaction().addToBackStack("part_second").replace(R.id.frameLayout_cashtocash_receivemoney, new RecipientDetailpartSecondFragment()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(5);

                }


                else  if(backButton_arrow==7){
                    getSupportFragmentManager().beginTransaction().addToBackStack("frag_mpin").replace(R.id.frameLayout_cashtocash_receivemoney, new OtpPageReceiveMoney()).commit();
                    mComponentInfo.setArrowBackButton_receiveCash(6);

                }



                else {
                    ReceiveMoneyCashToCashDiffrentCountryActivity.this.finish();
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




}



