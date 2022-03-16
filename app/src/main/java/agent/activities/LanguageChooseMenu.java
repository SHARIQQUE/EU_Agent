package agent.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import commonutilities.ComponentMd5SharedPre;

public class LanguageChooseMenu extends AppCompatActivity {
     ComponentMd5SharedPre mComponentInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.language_choose_menu);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
    }


    @Override
    protected void onResume() {
        super.onResume();

        ((Button) findViewById(R.id.englishLanguageChooseButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mComponentInfo.getmSharedPreferences().edit().putString("languageToUse", "en").commit();
                mComponentInfo.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();

                Intent i = new Intent(LanguageChooseMenu.this, LoginActivity.class);
                startActivity(i);
                LanguageChooseMenu.this.finish();
            }
        });

        ((Button) findViewById(R.id.frenchLanguageChooseButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComponentInfo.getmSharedPreferences().edit().putString("languageToUse", "fr").commit();
                mComponentInfo.getmSharedPreferences().edit().putString("isFirstRun", "NO").commit();

                Intent i = new Intent(LanguageChooseMenu.this, LoginActivity.class);
                startActivity(i);
                LanguageChooseMenu.this.finish();
            }
        });
    }
}
