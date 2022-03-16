package agent.tutionfees_fragment;

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

public class TutionFeesAcitivity extends AppCompatActivity {
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


        setContentView(R.layout.tution_fess_activity);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_remmetanceSendMoneymenu);
        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.tution_fees_capital));
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

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            String backButton_arrow = mComponentInfo.getArrowBackButtonTuitionFees();

            try {

                System.out.println(backButton_arrow);


          //  backButton_arrow="";   // comment tempoaray





                if (backButton_arrow.equalsIgnoreCase("frag_first")) {
                    TutionFeesAcitivity.this.finish();
                }

               else if (backButton_arrow.equalsIgnoreCase("frag_second")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();
                }

                else if (backButton_arrow.equalsIgnoreCase("frag_third")) {
                    getSupportFragmentManager().beginTransaction().addToBackStack("FragmentFirstTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentSecondTuitionFees()).commit();
                }

                else if (backButton_arrow.equalsIgnoreCase("frag_registrationNo_schollCode")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();
                }


                else if (backButton_arrow.equalsIgnoreCase("frag_fourth")) {
                    getSupportFragmentManager().beginTransaction().addToBackStack("FragmentSecondTuitionFees").replace(R.id.frameLayout_tutionfees, new FragmentThirdTutionFees()).commit();
                }

                else if (backButton_arrow.equalsIgnoreCase("frag_mpin")) {
                   getSupportFragmentManager().beginTransaction().addToBackStack("FragmentThirdTutionFees").replace(R.id.frameLayout_tutionfees, new FragmentFourthTuitionFees()).commit();
                }


                else {
                    TutionFeesAcitivity.this.finish();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return super.onOptionsItemSelected(menuItem);
    }
}




