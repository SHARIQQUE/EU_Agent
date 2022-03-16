package agent.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import agent.thread.SsltAllCertificates;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

public class SplashActivity extends AppCompatActivity implements ServerResponseParseCompletedNotifier {
    private ComponentMd5SharedPre mComponentInfo;
    private Snackbar mSnackBar;
    private ProgressDialog mDialog;
    Dialog successDialog;
    private String appVersion = "";

    private boolean canExitApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {


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
            setContentView(R.layout.splash_activity);
            mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();


            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersionApp = pInfo.versionCode;
            String currentVersionAppString=String.valueOf(currentVersionApp);

            mComponentInfo.getmSharedPreferences().edit().putString("APP_VERSION_API","BA_"+currentVersionAppString).commit();   // Globally Set  Request Tag   16/12/2021
            mComponentInfo.getmSharedPreferences().edit().putString("APK_NAME_VERSION","apkNameVersion").commit();               // Globally Set  Request Tag    16/12/2021


            SsltAllCertificates.trustAllCertificates();


            startServerInteraction_EUI();



        }
        catch (Exception e)
        {
              Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    private void startServerInteraction() {
        if (new InternetCheck().isConnected(SplashActivity.this)) {
            new ServerTask(mComponentInfo, SplashActivity.this, mHandler, generateCountryRequestData(), "getCountryListInJSON", 101).start();
        } else {
            showErrorSnackBar(getString(R.string.pleaseCheckInternet));
            canExitApp = true;
        }
    }

    private void startServerInteraction_EUI() {
        if (new InternetCheck().isConnected(SplashActivity.this)) {
            new ServerTask(mComponentInfo, SplashActivity.this, mHandler, generateCountryRequestData(), "getCurrency", 228).start();
        } else {
            showErrorSnackBar(getString(R.string.pleaseCheckInternet));
            canExitApp = true;
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(SplashActivity.this);
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void showErrorSnackBar(String message) {
        mSnackBar = Snackbar.make((View) findViewById(R.id.container), message, Snackbar.LENGTH_INDEFINITE);
        mSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startServerInteraction();
                mSnackBar.dismiss();
                canExitApp = false;
                mSnackBar.setCallback(null);
            }
        });
        mSnackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (canExitApp) {
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
        mSnackBar.setActionTextColor(Color.YELLOW);
        mSnackBar.show();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            SplashActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }

    }

    /*  1.1.2 REST API Request
         {"agentcode":"237100001012","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","appversiion":"ANDROID"}
     */
    private String generateCountryRequestData() {

        String jsonString = "";
        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", "237100001012");
            countryObj.put("requestcts", "25/05/2016 18:01:51");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("appversiion", "ANDROID");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                showErrorSnackBar(getString(R.string.pleaseCheckInternet));
                canExitApp = true;

                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
                showErrorSnackBar(getString(R.string.pleaseTryAgainLater));
                canExitApp = true;
            } else {
                DataParserThread thread = new DataParserThread(SplashActivity.this, mComponentInfo, SplashActivity.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }
    };

    private boolean isAppAllowedToRun(String[] appVersionData) {
        boolean ret = false;

        try {
            ret = Boolean.parseBoolean(appVersionData[1]);
        } catch (Exception e) {

        }

        return ret;

    }

    private void showUpdateAvailableDialog(final String firstRun) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

        builder.setCancelable(false);
        builder.setTitle(R.string.app_name);


        builder.setMessage(getString(R.string.splash_update_information_display));


        builder.setPositiveButton(getString(R.string.splash_update_installnow), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.expressunion.agent")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.expressunion.agent")));
                }
                SplashActivity.this.finish();
            }
        });
        builder.setNegativeButton(getString(R.string.splash_update_installLater), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                successDialog.cancel();
                if (firstRun.trim().length() == 0) {
                    intent = new Intent(SplashActivity.this, LanguageChooseMenu.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        });
        successDialog = builder.create();
        successDialog.show();


    }

    private boolean isNewVersionAvailable(String[] appVersionData) {
        boolean ret = false;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String currentVersionString = pInfo.versionName;
            String availableVersionString = appVersionData[3];

            float currentVersion = Float.parseFloat(currentVersionString);
            float availableVersion = Float.parseFloat(availableVersionString);

            if (availableVersion > currentVersion) {

                ret = true;
            }

        } catch (Exception e) {


        }

        return ret;

    }

    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {




            if (requestNo == 228) {

                SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                String[] data = generalResponseModel.getUserDefinedString_EUI().split("\\;");
                editor.putString("countryList_EUI", data[0]);
                editor.putString("countryCodeList_EUI", data[1]);
                editor.putString("countryPrefixCodeList_EUI", data[2]);
                editor.putString("currencyList_EUI", data[6]);
                editor.putString("countryMobileNoLength_EUI", data[3]);
                editor.putString("thresholderamount_EUI", data[7]);
                editor.commit();

                startServerInteraction();
            }

           else if (requestNo == 101) {

                SharedPreferences.Editor editor = mComponentInfo.getmSharedPreferences().edit();
                String[] data = generalResponseModel.getUserDefinedString().split("\\;");
                editor.putString("countryList", data[0]);
                editor.putString("countryCodeList", data[1]);
                editor.putString("countryPrefixCodeList", data[2]);
                editor.putString("amountcashOutSecurity", data[6]);
                editor.putString("countryMobileNoLength", data[3]);
                editor.commit();

                appVersion = data[4];

                String firstRunApp = mComponentInfo.getmSharedPreferences().getString("isFirstRun", "");
                Intent i;

                if (appVersion.equalsIgnoreCase("")) {
                    //  Toast.makeText(this, "App Version  is null", Toast.LENGTH_LONG).show();

                    if (firstRunApp.trim().length() == 0) {
                        i = new Intent(SplashActivity.this, LanguageChooseMenu.class);
                        startActivity(i);
                        SplashActivity.this.finish();
                    } else {
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        SplashActivity.this.finish();
                    }
                } else {

                    String[] verData = appVersion.split("\\,");

                    if (isAppAllowedToRun(verData)) {
                        if (isNewVersionAvailable(verData)) {
                            showUpdateAvailableDialog(firstRunApp);
                        } else {

                            if (firstRunApp.trim().length() == 0) {
                                i = new Intent(SplashActivity.this, LanguageChooseMenu.class);
                                startActivity(i);
                                SplashActivity.this.finish();
                            } else {
                                i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                                SplashActivity.this.finish();
                            }

                        }
                    } else {
                        Toast.makeText(this, getString(R.string.splash_update_stop_information_display), Toast.LENGTH_LONG).show();
                    }

                }
            }
        } else {
            showErrorSnackBar(generalResponseModel.getUserDefinedString());
        }

    }
}
