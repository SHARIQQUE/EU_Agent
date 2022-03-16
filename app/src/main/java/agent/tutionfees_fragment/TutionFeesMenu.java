package agent.tutionfees_fragment;

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

import commonutilities.ComponentMd5SharedPre;

/**
 * Created by sharique on 13-Mar-17.
 */

public class TutionFeesMenu extends AppCompatActivity implements View.OnClickListener
{
    TextView textview_school,textview_schoolcode,textview_registrationNumber;
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


        setContentView(R.layout.tution_fess_menu);

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


        textview_school=(TextView)findViewById(R.id.textview_school);
        textview_school.setOnClickListener(this);

        textview_registrationNumber=(TextView)findViewById(R.id.textview_registrationNumber);
        textview_registrationNumber.setOnClickListener(this);

        textview_schoolcode=(TextView)findViewById(R.id.textview_schoolcode);
        textview_schoolcode.setOnClickListener(this);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            TutionFeesMenu.this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {


            case R.id.textview_school:


                mComponentInfo.setFirstChooseClick("findSchool");

                Intent intent2= new Intent(TutionFeesMenu.this,TutionFeesAcitivity.class);
                startActivity(intent2);



              //  getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();


                break;

            case R.id.textview_schoolcode:

                mComponentInfo.setFirstChooseClick("findSchoolCode");

                Intent intent3=new Intent(TutionFeesMenu.this, TutionFeesAcitivity.class);
                startActivity(intent3);


              //  getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();



                break;

            case R.id.textview_registrationNumber:


                mComponentInfo.setFirstChooseClick("findMobileNumber");

                Intent intent4=new Intent(TutionFeesMenu.this, TutionFeesAcitivity.class);
                startActivity(intent4);

               // getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_tutionfees, new FragmentFirstTuitionFees()).commit();


                break;




        }
    }
}


