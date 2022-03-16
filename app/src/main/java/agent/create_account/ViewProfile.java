package agent.create_account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;

import adapter.CountryFlagAdapter;
import agent.activities.R;
import callback.DateSetNotifier;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.DatePickerFragment;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;
import agent.thread.ServerTaskFTP;


public class ViewProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, DateSetNotifier {

    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogAuth;
    private static final int SELECT_PICTURE_SIGN_PART_TWO = 103, SELECT_PICTURE_PROFILEPIC_PART_TWO = 104, PICTURE_SELECT_HOMELOCATION = 105;
    int backButtonOtp=0,chooseOneSelect = 0, selectNextButton = 0;
    int selectCamera = 0, selectGallery = 0, selectCameraElectricity = 0, selectGalleryElectricitybill = 0, selectCameraFront = 0, selectGalleryFront = 0, selectCameraBack = 0, selectGalleryBack = 0, selectGalleryDrawaHome = 0, selectCameraDrawaHome = 0;
    InputStream imageStream, imageStreamCamera;
    ArrayList accountsArrayDetailsList;
    Uri uriMpinPage, uriDrawHome, uidElectricity, uidString, selectedImageUriProfilPic, uriIddocumnetFront;
    boolean boolselectUploadSignaturePic = false;
    String agentName_reload, errorMsgToDisplay = "";
    boolean boolselectUploadPic = false;
    ListView listView;

    int imageHeight = 200; // New height in pixels
    int imagewidth = 200; // New width in pixels
    String[] responseArrayimage, resultStringArray, languageSelectDetails, languageSelectCode, bankSelectionArray, transferTagArray, idProofArray, idProofCodeArray, accountTypeArray, planAccountNameLabel, planCodeArray, agentTypeCode, tempState, professionArray, genderArray, genderCodeArray, attachBranchNameType, attachBranchNameCode, cityType, cityCodeArray, nationalityArray, nationalityCodeArray;

    Toolbar mToolbar;
    ImageView camera_mpin_imageview, upload_mpin_imageview, imageview_preview_mpinPage, camera_drawaHome_imageview, upload_DrawHome_imageview, imageview_preview_drawHome, imageview_bill_electricityBill, uploadpic_homelocation, camera_front_iddocument_imageview, sign_partTwo_imageView, billhomelocation_partone_imageView, sign_partOne_imageView, profilepicture_parttwo_imageView, profilepicture_partOne_imageView;
    Button idproof_calender_button, idproofExpiredDate_calender_button, dateOfBirth_calender_button;
    ComponentMd5SharedPre mComponentInfo;
    String otpString, cameraStringMpinPage, uploadStringMpinPage, attachBranchNameString, idproofNameString, cameraStringDrawaHome, uploadStringDrawaHome, cameraElectricityBillString, uploadStringElectricity, uploadStringIdDocumnetback, uploadString, cameraString, uploadIddocumentString, cameraIDdocumentString, agentName, customerIdString, spinnerCountryCodeString, loyalityCasrdNoString, spinnerIdProofTypeTemp, subscriberNameString, emailString, agentCode, idProofPlaceString, birthpalceString, agentTypeString, secondMobileNumberString, fixPhonenoString, professionString, genderString, idproofDueDate, nationalityString, planNameString, residenceAreaString, spinnerCountryString, transferBasisString, accountNumber, cityString, spinnerStateString, countryString, addressString, confirmationCodeString, spinnerIdProofTypeString, LanguageString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString,
            idProofCodeString, genderCodeString, professionCodeString, nationalityCodeString;
    TextView email_textview_secondPage,textview_waitingForSubscriberAuthenticate, agentProfile_textview, customerId_textview_detailform, customerName_edittext, genderDetails_textview, language_edittext, fixedNumber_textview, secondMobileNo_textview, profession_textview, dateOfBirth_textview, birthpalce_textview, address_textview_detailsform, city_textview, attachBranceName_textview, idproofIssueDate_textview, idProofissue_place_textview, idproofexpirayData_textview, nationality_textview_detailsForm, residenceArea_textview_detailsForm, idproofType_textview_detailsForm, idproof_name_textview_detailsForm;
    int counter;
    boolean isTimeUp = true;

    View viewForContainer;
    Button idproofIssueDate_calender_button, submitButton_partfirst;
    boolean isReview, isServerOperationInProcess, isOtherProfession;
    Dialog successDialog;
    int lengthToCheck, transferCase, accToAccLevel = 0, idProofSelectedPosition, genderSelectedPosition, professionSelectedPosition, languageSelectedPosition, citySelectedPosition, nationalitySelectedPosition, countrySelectedPosition;
    private Spinner spinnerState, spinnerCountry, spinnerAccountToDebit, transferBasisSpinner;
    private ScrollView scrollView_otp_page, scrollView_uploading_form_showreceipt, scrollView_electricity_bill, scrollView_createAgent_thirdPage_form, scrollView_button_idDocumnetpicure, scrollView_profilePiture_sign_parttwo, scrollView_waitingtoSubscriberAuthenticate, scrollView_button_profilepic_sign_partone, scrollView_button_mpin, scrollView_profilePiture_sign_partOne, scrollView_mpin_Page, scrollView_button_createAgent_firstPart, scrollView_createAgent_firstPart, scrollView_createAgent_confirmationPage;
    private AutoCompleteTextView otp_editText, number_textview_mpinpage, otherProfessionEditText, idproofEditText_datePicker_manually, loyalityCardNumberEditTex, idProofPlaceEditText, agentTypeEditText,
            emailEditText, stateEditText, countryEditText, subscriberNameEditText, mpinEditText, accountNumberEditText;
    private TextView customerId_textview, accountProfile_textview, idproofType_textview, idproofNumber_textview, idexpirayDate_textview, nationality_textview, address_reviewpage, idProofNumberTextView, accountNumberTextViewReview, recipientCountryTxtView_Review, agentCodeTextView, accountNameTextView_Review, cityTextView_Review, languageTextView_Review, idproofTypeTextView_Review, countryTextView_Review, addressTextView_Review, idproofTxtView_Review, transferBasisTxtView_Review, recipientNameNoTxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
    private ProgressDialog mDialog;


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
            } else {
                DataParserThread thread = new DataParserThread(ViewProfile.this, mComponentInfo, ViewProfile.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String[] selectDateNew;
    Timer timer;
    int timeCalculation=0;

    int iLevel = 99;
    boolean isMiniStmtMode = false;
    public AutoCompleteTextView idproofIssuDate_EditText_manually, dateOfBirth_EditText_manullay, idProofExpiredDate_EditText_manually;
    String dateOfBirthString, idissuePlaceString, idProofIssuDateString;
    String dateSetString, pic_fromServerString, sign_fromServerString, idf_fromServerString, idb_fromServerString, form_fromServerString, bill_fromServerString;
    String idProofIssuDate;
    boolean isAuthTimerStart = false;
    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                validateDetailsFirst();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }

       // languageToUse = "fr";
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.view_profile);

        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_createAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.viewProfile));
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
        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

            //  countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

            SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
            countrySelectionString = prefs.getString("countrySelectionString", null);

            // countrySelectionString="Gabon";

            genderArray = getResources().getStringArray(R.array.genderDetailsType);
            genderArray[0] = (getString(R.string.genderDetails));


            genderCodeArray = getResources().getStringArray(R.array.genderTypeCode);
            professionArray = getResources().getStringArray(R.array.professionArray);


            idProofArray = getResources().getStringArray(R.array.IDProofTypeArray);
            idProofCodeArray = getResources().getStringArray(R.array.IDProofTypeCodeArray);

        } catch (Exception e) {
            e.printStackTrace();
            //  CreateAccount.this.finish();
        }


        scrollView_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_createAgent_firstPart);
        scrollView_mpin_Page = (ScrollView) findViewById(R.id.scrollView_mpin_Page);

        scrollView_button_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_button_createAgent_firstPart);
        scrollView_electricity_bill = (ScrollView) findViewById(R.id.scrollView_electricity_bill);
        scrollView_uploading_form_showreceipt = (ScrollView) findViewById(R.id.scrollView_uploading_form_showreceipt);
        scrollView_button_mpin = (ScrollView) findViewById(R.id.scrollView_button_mpin);
        scrollView_profilePiture_sign_partOne = (ScrollView) findViewById(R.id.scrollView_profilePiture_sign_partOne);
        scrollView_waitingtoSubscriberAuthenticate = (ScrollView) findViewById(R.id.scrollView_waitingtoSubscriberAuthenticate);
        textview_waitingForSubscriberAuthenticate = (TextView) findViewById(R.id.textview_waitingForSubscriberAuthenticate);
        email_textview_secondPage = (TextView) findViewById(R.id.email_textview_secondPage);
        scrollView_button_idDocumnetpicure = (ScrollView) findViewById(R.id.scrollView_button_idDocumnetpicure);
        scrollView_button_profilepic_sign_partone = (ScrollView) findViewById(R.id.scrollView_button_profilepic_sign_partone);
        scrollView_profilePiture_sign_parttwo = (ScrollView) findViewById(R.id.scrollView_profilePiture_sign_parttwo);
        scrollView_otp_page = (ScrollView) findViewById(R.id.scrollView_otp_page);

        submitButton_partfirst = (Button) findViewById(R.id.submitButton_partfirst);
        submitButton_partfirst.setOnClickListener(this);

        scrollView_createAgent_confirmationPage = (ScrollView) findViewById(R.id.scrollView_createAgent_confirmationPage);

        scrollView_createAgent_thirdPage_form = (ScrollView) findViewById(R.id.scrollView_createAgent_thirdPage_form);

        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText("AgentCode:-  " + agentCode);

        accountNumberTextViewReview = (TextView) findViewById(R.id.accountNumberTextViewReview);
        idProofNumberTextView = (TextView) findViewById(R.id.idProofNumberTextView);
        address_reviewpage = (TextView) findViewById(R.id.address_reviewpage);

        customerId_textview = (TextView) findViewById(R.id.customerId_textview);
        accountProfile_textview = (TextView) findViewById(R.id.accountProfile_textview);
        idproofType_textview = (TextView) findViewById(R.id.idproofType_textview);
        idproofNumber_textview = (TextView) findViewById(R.id.idproofNumber_textview);
        idexpirayDate_textview = (TextView) findViewById(R.id.idexpirayDate_textview);
        nationality_textview = (TextView) findViewById(R.id.nationality_textview);


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        countryArray[0] = (getString(R.string.nationalityNew));

        System.out.print(countryArray);

        cityType = getResources().getStringArray(R.array.cityType);
        cityCodeArray = getResources().getStringArray(R.array.cityCodeArray);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(ViewProfile.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);


        languageSelectDetails = getResources().getStringArray(R.array.languageSelectDetails);
        languageSelectCode = getResources().getStringArray(R.array.languageSelectCode);


        attachBranchNameCode = getResources().getStringArray(R.array.attachBranchNameCode);
        attachBranchNameType = getResources().getStringArray(R.array.attachBranchNameType);


        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        //spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray));
        spinnerState.setOnItemSelectedListener(this);

        accountNumberEditText = (AutoCompleteTextView) findViewById(R.id.accountNumberEditText);
        accountNumberEditText.setOnEditorActionListener(this);

        loyalityCardNumberEditTex = (AutoCompleteTextView) findViewById(R.id.loyalityCardNumberEditTex);
        loyalityCardNumberEditTex.setOnEditorActionListener(this);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.emailEditText_AccToCash);
        emailEditText.setOnEditorActionListener(this);

        stateEditText = (AutoCompleteTextView) findViewById(R.id.stateEditText_AccToCash);
        stateEditText.setOnEditorActionListener(this);

        idproof_calender_button = (Button) findViewById(R.id.idproof_calender_button);
        idproof_calender_button.setInputType(InputType.TYPE_NULL);
        idproof_calender_button.setOnTouchListener(this);
        otherProfessionEditText = (AutoCompleteTextView) findViewById(R.id.otherProfessionEditText_CreateAcc);
        number_textview_mpinpage = (AutoCompleteTextView) findViewById(R.id.number_textview_mpinpage);
        otp_editText = (AutoCompleteTextView) findViewById(R.id.otp_editText);

        idproofExpiredDate_calender_button = (Button) findViewById(R.id.idproofExpiredDate_calender_button);
        idproofExpiredDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofExpiredDate_calender_button.setOnTouchListener(this);

        dateOfBirth_calender_button = (Button) findViewById(R.id.dateOfBirth_calender_button);
        dateOfBirth_calender_button.setInputType(InputType.TYPE_NULL);
        dateOfBirth_calender_button.setOnTouchListener(this);

        idproofIssueDate_calender_button = (Button) findViewById(R.id.idproofIssueDate_calender_button);
        idproofIssueDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofIssueDate_calender_button.setOnTouchListener(this);


        agentProfile_textview = (TextView) findViewById(R.id.agentProfile_textview);
        customerId_textview_detailform = (TextView) findViewById(R.id.customerId_textview_detailform);
        customerName_edittext = (TextView) findViewById(R.id.customerName_edittext);
        genderDetails_textview = (TextView) findViewById(R.id.genderDetails_textview);
        language_edittext = (TextView) findViewById(R.id.language_edittext);
        secondMobileNo_textview = (TextView) findViewById(R.id.secondMobileNo_textview);
        fixedNumber_textview = (TextView) findViewById(R.id.fixedNumber_textview);
        profession_textview = (TextView) findViewById(R.id.profession_textview);
        dateOfBirth_textview = (TextView) findViewById(R.id.dateOfBirth_textview);
        birthpalce_textview = (TextView) findViewById(R.id.birthpalce_textview);
        address_textview_detailsform = (TextView) findViewById(R.id.address_textview_detailsform);
        city_textview = (TextView) findViewById(R.id.city_textview);
        attachBranceName_textview = (TextView) findViewById(R.id.attachBranceName_textview);

        nationality_textview_detailsForm = (TextView) findViewById(R.id.nationality_textview_detailsForm);
        residenceArea_textview_detailsForm = (TextView) findViewById(R.id.residenceArea_textview_detailsForm);
        idproofType_textview_detailsForm = (TextView) findViewById(R.id.idproofType_textview_detailsForm);
        idproof_name_textview_detailsForm = (TextView) findViewById(R.id.idproof_name_textview_detailsForm);
        idproofIssueDate_textview = (TextView) findViewById(R.id.idproofIssueDate_textview);
        idProofissue_place_textview = (TextView) findViewById(R.id.idProofissue_place_textview);
        idproofexpirayData_textview = (TextView) findViewById(R.id.idproofexpirayData_textview);


        idproofEditText_datePicker_manually = (AutoCompleteTextView) findViewById(R.id.idproofEditText_datePicker_manually);
        idproofEditText_datePicker_manually.setOnEditorActionListener(this);
        subscriberNameEditText = (AutoCompleteTextView) findViewById(R.id.subscriberNameEditText);
        countryEditText = (AutoCompleteTextView) findViewById(R.id.countryEditText_AccToCash);
        countryEditText.setOnEditorActionListener(this);
        dateOfBirth_EditText_manullay = (AutoCompleteTextView) findViewById(R.id.dateOfBirth_EditText_manullay);
        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        idProofExpiredDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idProofExpiredDate_EditText_manually);
        idProofPlaceEditText = (AutoCompleteTextView) findViewById(R.id.idProofPlaceEditText_AccToCash);
        idProofPlaceEditText.setOnEditorActionListener(this);
        agentTypeEditText = (AutoCompleteTextView) findViewById(R.id.agentTypeEditText_AccToCash);
        agentTypeEditText.setOnEditorActionListener(this);
        String bankAccounts = mComponentInfo.getmSharedPreferences().getString("bankAccounts", "");
        if (bankAccounts.trim().length() > 0) {
            String[] data = bankAccounts.split("\\;");

            ArrayList<String> accountList = new ArrayList<String>();
            ArrayList<String> accountCodeList = new ArrayList<String>();
            accountList.add(getString(R.string.accounttodebit));
            accountCodeList.add(getString(R.string.accounttodebit));

            for (int i = 0; i < data.length; i++) {
                String[] tData = data[i].split("\\|");
                if (tData.length > 0) {
                    if (tData[4].equalsIgnoreCase("MA")) {
                        accountList.add(tData[0] + " - " + tData[1]);
                        accountCodeList.add(tData[4]);
                    }
                }
            }

            payerBankAccountsArray = accountList.toArray(new String[accountList.size()]);
            payerAccountCodeArray = accountCodeList.toArray(new String[accountCodeList.size()]);

        } else {
            payerBankAccountsArray = new String[1];
            payerBankAccountsArray[0] = getString(R.string.accounttodebit);
            payerAccountCodeArray = new String[1];
            payerAccountCodeArray[0] = getString(R.string.accounttodebit);
        }

     /*   spinnerAccountToDebit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, payerBankAccountsArray));
        spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempState));

*/
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        recipientCountryTxtView_Review = (TextView) findViewById(R.id.recipientCountry_TxtView_Review_AccToCash);
        transferBasisTxtView_Review = (TextView) findViewById(R.id.transferBasis_TxtView_Review_AccToCash);
        recipientNameNoTxtView_Review = (TextView) findViewById(R.id.recipientNameNo_TxtView_Review_AccToCash);
        addressTextView_Review = (TextView) findViewById(R.id.address_TxtView_Review_AccToCash);
        idproofTxtView_Review = (TextView) findViewById(R.id.idproof_TxtView_Review_AccToCash);

        languageTextView_Review = (TextView) findViewById(R.id.LanguageType_TxtView_Review_AccToCash);
        idproofTypeTextView_Review = (TextView) findViewById(R.id.idproofType_TxtView_Review_AccToCash);


        accountNameTextView_Review = (TextView) findViewById(R.id.account_TxtView_Review_AccToCash);
        cityTextView_Review = (TextView) findViewById(R.id.city_TxtView_Review_AccToCash);
        countryTextView_Review = (TextView) findViewById(R.id.country_TxtView_Review_AccToCash);

        payerAccountTypeTxtView_Review = (TextView) findViewById(R.id.payerAccountType_TxtView_Review_AccToCash);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText);
        mpinEditText.setOnEditorActionListener(this);


        mpinEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


        idproofIssuDate_EditText_manually.setOnEditorActionListener(this);

        setInputType(1);

        scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);

        imageview_preview_drawHome = (ImageView) findViewById(R.id.imageview_preview_drawHome);
        imageview_bill_electricityBill = (ImageView) findViewById(R.id.imageview_bill_electricityBill);
        imageview_preview_mpinPage = (ImageView) findViewById(R.id.imageview_preview_mpinPage);


        profilepicture_partOne_imageView = (ImageView) findViewById(R.id.profilepicture_partOne_imageView);
        sign_partOne_imageView = (ImageView) findViewById(R.id.sign_partOne_imageView);
        billhomelocation_partone_imageView = (ImageView) findViewById(R.id.billhomelocation_partone_imageView);
        profilepicture_parttwo_imageView = (ImageView) findViewById(R.id.profilepicture_parttwo_imageView);


        uploadpic_homelocation = (ImageView) findViewById(R.id.uploadpic_homelocation);
        sign_partTwo_imageView = (ImageView) findViewById(R.id.sign_partTwo_imageView);



        // --------------------------------------------------------------------------------------------------------
    }



    void customeAlertDialog_authorize_otp() {

        scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
        selectNextButton = 0;
        scrollView_otp_page.setVisibility(View.GONE);
        submitButton_partfirst.setText(getString(R.string.next));
        scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);



        Resources res = getResources();
        CharSequence[] items = res.getStringArray(R.array.otp_authorize);


        // Creating and Building the Dialog
         alertDialogAuth = new AlertDialog.Builder(this);
        alertDialogAuth.setTitle(getString(R.string.choice));
      //  alertDialogAuth.setCancelable(false);

        alertDialogAuth.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        otpGenerateRequest();


                      /*  Intent i = new Intent(ViewProfile.this, OTPVerificationActivity.class);
                        startActivityForResult(i, 299);*/



                        break;

                    case 1:

                        authorizeInitiateRequest();

                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogAuth.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


        }
    }


    void openImageChooser_homelocation() {

        chooseOneSelect = 1;
        selectGalleryElectricitybill = 1;
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECT_HOMELOCATION);
    }


    void openImageChooser_sign_partTwo() {

        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_SIGN_PART_TWO);
    }


    void openImageChooser_picureSign_partTwo() {

        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_PROFILEPIC_PART_TWO);
    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                accountNumberEditText.setText("");
                accountNumberEditText.setHint(getString(R.string.hintCreateAccountNumber));
                // accountNumberEditText.setFilters(null);
                accountNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }
                };
                digitsfilters[1] = new InputFilter.LengthFilter(Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]));
                accountNumberEditText.setHint(String.format(getString(R.string.hintCreateAccountNumber), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                accountNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                accountNumberEditText.setFilters(digitsfilters);
                accountNumberEditText.setText("");


            } else if (i == 2) {
                accountNumberEditText.setText("");
                InputFilter[] digitsfilters = new InputFilter[2];
                digitsfilters[0] = new InputFilter() {

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
                        if (end > start) {

                            char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
                                    'g', 'h', 'i', 'j', 'k', 'l',
                                    'm', 'n', 'o', 'p', 'q', 'r',
                                    's', 't', 'u', 'v', 'w', 'x',
                                    'y', 'z', 'A', 'B', 'C', 'D',
                                    'E', 'F', 'G', 'H', 'I', 'J',
                                    'K', 'L', 'M', 'N', 'O', 'P',
                                    'Q', 'R', 'S', 'T', 'U', 'V',
                                    'W', 'X', 'Y', 'Z', '.'};

                            for (int index = start; index < end; index++) {
                                if (new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                                    return source;
                                } else {
                                    return "";
                                }
                            }
                        }
                        return null;
                    }
                };

                digitsfilters[1] = new InputFilter.LengthFilter(16);
                accountNumberEditText.setHint(getString(R.string.pleaseentername));
                accountNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                accountNumberEditText.setFilters(digitsfilters);
                accountNumberEditText.setText("");
            }
        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:

                setInputType(i);


                break;


            case R.id.spinnerCountry:

                //  countrySelectedCode = countryCodeArray[i];


                //validateDetailsPartFirst();

                setInputType(transferBasisSpinner.getSelectedItemPosition());

                break;


        }
    }


    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        ViewProfile.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (iLevel) {

                    case 0:
                      /*  idproofIssuDate_EditText_manually.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofIssueDate(dateSetString)) {
                        }
                        idproofIssuDate_EditText_manually.setText(dateSetString);*/

                        break;

                    case 1:
                        dateOfBirth_EditText_manullay.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (dateOfBirthOrder(dateSetString)) {
                        }
                        break;

                    case 2:
                      /*  idProofExpiredDate_EditText_manually.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                       *//*  if(dateOfBirthDueDate(dateSetString)) {
                        }*/
                        break;

                    case 3:

                        idproof_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofDate(dateSetString)) {
                        }
                        idproofEditText_datePicker_manually.setText(dateSetString);

                        break;

                    case 4:
                        // idproofIssueDate_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idProofIssueDate(dateSetString)) {
                        }
                        break;

                    case 5:
                        //idproofExpiredDate_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (idproofExpiredDate(dateSetString)) {
                        }
                        break;

                    case 6:
                        // dateOfBirth_calender_button.setText("" + day + "-" + month + "-" + year);
                        dateSetString = ("" + day + "-" + month + "-" + year);
                        if (dateOfBirthOrder(dateSetString)) {
                        }
                        break;


                }
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

         /*   case R.id.dateOfBirth_EditText_manullay:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 1;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;*/

            /*  case R.id.idproofIssuDate_EditText:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 0;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;*/

            case R.id.idProofExpiredDate_EditText_manually:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 2;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.idproof_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 3;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.idproofIssueDate_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 4;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;


            case R.id.idproofExpiredDate_calender_button:

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 5;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

            case R.id.dateOfBirth_calender_button:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    iLevel = 6;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
                break;

        }

        return false;
    }


    public boolean dateOfBirthOrder(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(ViewProfile.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                } else {
                    dateOfBirth_EditText_manullay.setText(dateSetString);
                    // dateOfBirth_EditText_manullay.setTextColor(Color.RED);
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }


    public boolean idProofDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(ViewProfile.this, "Please select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                    dateOfBirth_EditText_manullay.setText("");
                    ret = false;//If start date is before end date
                }

            /* else if(simpleDateFormat.parse(currentDate).equals(simpleDateFormat.parse(selectDateNew)))
             {
                Toast.makeText(CreateAccount.this, "select date and current date are same", Toast.LENGTH_LONG).show();

                ret = true;//If two dates are equal
            }*/


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // return ret;


            // int resultDate = Integer.parseInt(dateOfBirthString);
      /*  System.out.println(resultDate);

        if (resultDate < currentDay && resultDate == currentYear && resultDate == currentMonth) {
            Toast.makeText(CreateAccount.this, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentMonth && resultDate == currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (resultDate < currentYear) {
            Toast.makeText(CreateAccount.this, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }
            */
            return ret;
        }
    }

    public boolean idProofIssueDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(ViewProfile.this, "Please select Valid ID Proof issue date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                } else {

                    idproofIssuDate_EditText_manually.setText(dateSetString);
                    // idproofIssuDate_EditText_manually.setTextColor(Color.RED);
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return ret;
        }
    }

    public boolean idproofExpiredDate(String selectDate) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = selectDate.split("\\-");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    //  Toast.makeText(CreateAccount.this, "Please select Valid Due Date", Toast.LENGTH_LONG).show();
                    idProofExpiredDate_EditText_manually.setText(dateSetString);

                    ret = false;//If start date is before end date
                } else {
                    idProofExpiredDate_EditText_manually.setText(dateSetString);
                    //idProofExpiredDate_EditText_manually.setTextColor(Color.RED);
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return ret;
        }
    }

    public boolean idProofDateOrder(String dateSetString) {
        boolean ret = false;
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        String currentDate = ("" + currentYear + "-" + currentMonth + "-" + currentDay);

        String tempYear, tempDay, tempMonth;
        selectDateNew = dateSetString.split("\\/");

        tempDay = selectDateNew[0];
        tempMonth = selectDateNew[1];
        tempYear = selectDateNew[2];

        String selectDateNew = ("" + tempYear + "-" + tempMonth + "-" + tempDay);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        {
            try {

                if (simpleDateFormat.parse(currentDate).before(simpleDateFormat.parse(selectDateNew))) {
                    Toast.makeText(ViewProfile.this, "Please select Valid Id Proof Issue Date", Toast.LENGTH_LONG).show();
                    idproofIssuDate_EditText_manually.setText("");
                    ret = false;//If start date is before end date
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return ret;
        }
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void hideKeyboard() {

        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){

        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.submitButton_partfirst:

                if (selectNextButton == 0) {
                    validateDetailsFirst();
                } else if (selectNextButton == 1) {
                    validation_profilepic_sign_partOne();
                } else if (selectNextButton == 2) {

                    Intent intent = new Intent(ViewProfile.this, AccountsMenu.class);
                    startActivity(intent);
                    ViewProfile.this.finish();

                }

                if (selectNextButton == 3) {
                    if (validationOTP()) {
                        otpVerifyRequest();
                    }
                }

                if (selectNextButton == 4) {
                    authorizeRequest();
                }
                break;


        }
    }


    public void validateDetailsFirst() {

        if (validateDetailsPartFirst()) {

            AgentIdentity();

        }
    }

    void validation_profilepic_sign_partOne() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // hide back button
        mToolbar.setTitle("        " + getString(R.string.viewProfile));
        mToolbar.setSubtitle("          " + agentName);


        scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
        selectNextButton = 2;
        submitButton_partfirst.setText(getString(R.string.close_button));

        scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
        scrollView_profilePiture_sign_parttwo.setVisibility(View.VISIBLE);
    }


    private boolean validateDetailsPartFirst() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileCashIn), lengthToCheck + 1 + "");

            } else {
                transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);
            }

            accountNumber = accountNumberEditText.getText().toString().trim();
            if (accountNumber.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (accountNumber.length() == ++lengthToCheck) {
                        accountNumber = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + accountNumber;
                    } else {
                        Toast.makeText(ViewProfile.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


                ret = true;

                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];


            } else {
                Toast.makeText(ViewProfile.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();

        }


        return ret;


    }


    private void AgentIdentity() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


           // new ServerTask(mComponentInfo, ViewProfile.this, mHandler, requestData, "getAgentIdentity", 195).start();

            vollyRequestApi_serverTask("getAgentIdentity",requestData, 195);


        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    void vollyRequestApi_serverTask(String apiName, final String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,ServerTask.baseUrl+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(ViewProfile.this,mComponentInfo,ViewProfile.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(ViewProfile.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }




    // {"agentCode":"237931349121","customerid":"ASDFSF65120320","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"CREATEAGENT"}


    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            String countryCodePrefixString = getCountryPrefixCode();

            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", accountNumber);            // verify check account number
            countryObj.put("transtype", "VIEWPROFILE");
            countryObj.put("isotp", "Y");

            String vpin = mComponentInfo.getMD5(agentCode.substring(3, 5) + "GETAGENTIDENTITY").toUpperCase();

            countryObj.put("vpin", vpin);
            countryObj.put("initiatorAgent", agentCode);   // agent Code Login
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

            System.out.println(e);
        }
        return jsonString;
    }


    private boolean validationMpin() {
        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.prompt_mPin), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 195) {

                resultStringArray = generalResponseModel.getUserDefinedString().split("\\|");

                if (resultStringArray[22].equalsIgnoreCase("Transaction Successful")) {
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);

                    selectNextButton = 1;
                    customerId_textview.setText(resultStringArray[14]);
                    idproofType_textview.setText(resultStringArray[12]);
                    idproofNumber_textview.setText(resultStringArray[9]);


                    profileView();

                } else {
                    Toast.makeText(ViewProfile.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
            } else if (requestNo == 196) {

                String[] responseArray = generalResponseModel.getUserDefinedString().split("\\|");

                accountProfile_textview.setText(responseArray[1]);
                agentProfile_textview.setText(responseArray[1]);
                customerId_textview_detailform.setText(resultStringArray[14]);

                customerName_edittext.setText(responseArray[2]);

                if(responseArray[3].equalsIgnoreCase("M"))
                {
                    responseArray[3]=getString(R.string.male);
                }
                if(responseArray[3].equalsIgnoreCase("F"))
                {
                    responseArray[3]=getString(R.string.female);
                }

                email_textview_secondPage.setText(responseArray[23]);   // from server

                genderDetails_textview.setText(responseArray[3]);

                language_edittext.setText(responseArray[4]);
                secondMobileNo_textview.setText(responseArray[5]);
                fixedNumber_textview.setText(responseArray[6]);

                dateOfBirth_textview.setText(responseArray[7]);
                birthpalce_textview.setText(resultStringArray[17]); // resultStringArray
                address_textview_detailsform.setText(responseArray[9]);
                city_textview.setText(responseArray[10]);
                idproof_name_textview_detailsForm.setText(responseArray[14]);
                idproofType_textview_detailsForm.setText(responseArray[15]);


                profession_textview.setText(resultStringArray[1]);
                residenceArea_textview_detailsForm.setText(responseArray[13]);
                idproofIssueDate_textview.setText(responseArray[16]);
                idProofissue_place_textview.setText(resultStringArray[17]);     // resultStringArray        // null
                nationality_textview.setText(responseArray[19]);
                nationality_textview_detailsForm.setText(responseArray[19]);


                idexpirayDate_textview.setText(responseArray[18]);
                attachBranceName_textview.setText(responseArray[24]);
                idproofexpirayData_textview.setText(responseArray[18]);


                backButtonOtp=1;


                 // kycsamebranch is "null" or ""     //// no pop up Direct Next page
                // kycsamebranch is  true //// only otp show
                // kycsamebranch "false" && agentTpe =="Sub"   // Then Open Popup Auth AP

               // resultStringArray[25]="T";

                if (resultStringArray[25].equalsIgnoreCase("")||resultStringArray[25].equalsIgnoreCase("null"))
                {
                    checkimage();
                }

                else if (resultStringArray[25].equalsIgnoreCase("true")||resultStringArray[25].equalsIgnoreCase("T"))
                {
                    otpGenerateRequest();
                }

                else if (resultStringArray[25].equalsIgnoreCase("false") || resultStringArray[25].equalsIgnoreCase("F")&& resultStringArray[24].equalsIgnoreCase("SUB")) {
                     //   scrollView_otp_page.setVisibility(View.GONE);
                        customeAlertDialog_authorize_otp();
                    }
                 else {

                    checkimage();
                }

            }

            else if(requestNo==205)
            {
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_otp_page.setVisibility(View.VISIBLE);
                selectNextButton = 3;

            }
            else if (requestNo == 203) {
                isTimeUp=false;
                authorizeRequest();

            } else if (requestNo == 204) {
                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);

                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                selectNextButton = 1;
                submitButton_partfirst.setText(getString(R.string.next));
                checkimage();
            }

            else if (requestNo == 103)  // if  OTP response is Success
            {
                checkimage();
            }


            else if (requestNo == 199) {


                responseArrayimage = generalResponseModel.getUserDefinedString().split("\\@");
                System.out.println(responseArrayimage);


                if (responseArrayimage[1].equalsIgnoreCase("null") || responseArrayimage[1].equalsIgnoreCase("")) {
                    System.out.println("image  null");
                } else {

                    if (responseArrayimage[1].contains("pic")) {
                        pic_fromServerString = "Y";

                    } else {
                        pic_fromServerString = "N";
                    }

                    if (responseArrayimage[1].contains("sign")) {
                        sign_fromServerString = "Y";
                    } else {
                        sign_fromServerString = "N";
                    }

                    if (responseArrayimage[1].contains("idf")) {
                        idf_fromServerString = "Y";
                    } else {
                        idf_fromServerString = "N";
                    }


                    if (responseArrayimage[1].contains("idb")) {
                        idb_fromServerString = "Y";
                    } else {
                        idb_fromServerString = "N";
                    }

                    if (responseArrayimage[1].contains("form")) {
                        form_fromServerString = "Y";
                    } else {
                        form_fromServerString = "N";
                    }


                    if (responseArrayimage[1].contains("bill")) {
                        bill_fromServerString = "Y";
                    } else {
                        bill_fromServerString = "N";
                    }


                    imageDownload();

                }


            } else if (requestNo == 200) {

                hideProgressDialog();


                scrollView_otp_page.setVisibility(View.GONE);
                selectNextButton = 1;
                scrollView_profilePiture_sign_partOne.setVisibility(View.VISIBLE);

                String[] responseImagearray = generalResponseModel.getUserDefinedString().split("\\|");


                if (responseImagearray[4].equalsIgnoreCase("") || responseImagearray[4].equalsIgnoreCase("null")) {
                    System.out.println(" No image From Backend / image is null");

                }
                else {


                    profilepicture_partOne_imageView.requestLayout();
                    profilepicture_partOne_imageView.getLayoutParams().height = imageHeight;
                    profilepicture_partOne_imageView.getLayoutParams().width = imagewidth;
                    profilepicture_partOne_imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    profilepicture_parttwo_imageView.requestLayout();
                    profilepicture_parttwo_imageView.getLayoutParams().height = imageHeight;
                    profilepicture_parttwo_imageView.getLayoutParams().width = imagewidth;
                    profilepicture_parttwo_imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    byte[] imageBytes1 = Base64.decode(responseImagearray[4], Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    profilepicture_partOne_imageView.setImageBitmap(decodedImage);  // set page First
                    profilepicture_parttwo_imageView.setImageBitmap(decodedImage);   // set page First
                }

                if (responseImagearray[5].equalsIgnoreCase("") || responseImagearray[5].equalsIgnoreCase("null"))
                {
                    System.out.println(" No image From Backend / image is null");
                }
                else {
                    byte[] imageBytes2 = Base64.decode(responseImagearray[5], Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);

                    sign_partOne_imageView.requestLayout();
                    sign_partOne_imageView.getLayoutParams().height = imageHeight;
                    sign_partOne_imageView.getLayoutParams().width = imagewidth;
                    sign_partOne_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    sign_partTwo_imageView.requestLayout();
                    sign_partTwo_imageView.getLayoutParams().height = imageHeight;
                    sign_partTwo_imageView.getLayoutParams().width = imagewidth;
                    sign_partTwo_imageView.setScaleType(ImageView.ScaleType.FIT_XY);       // for custome height image view set

                    sign_partOne_imageView.setImageBitmap(decodedImage2);   // set page Second
                    sign_partTwo_imageView.setImageBitmap(decodedImage2);   // set page Second

                }

                if (responseImagearray[2].equalsIgnoreCase("") || responseImagearray[2].equalsIgnoreCase("null")) {
                    System.out.println(" No image From Backend / image is null");

                } else {

                    billhomelocation_partone_imageView.requestLayout();
                    billhomelocation_partone_imageView.getLayoutParams().height = imageHeight;
                    billhomelocation_partone_imageView.getLayoutParams().width = imagewidth;
                    billhomelocation_partone_imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    uploadpic_homelocation.requestLayout();
                    uploadpic_homelocation.getLayoutParams().height = imageHeight;
                    uploadpic_homelocation.getLayoutParams().width = imagewidth;
                    uploadpic_homelocation.setScaleType(ImageView.ScaleType.FIT_XY);


                    byte[] imageBytes3 = Base64.decode(responseImagearray[2], Base64.DEFAULT);
                    Bitmap decodedImage3 = BitmapFactory.decodeByteArray(imageBytes3, 0, imageBytes3.length);
                    billhomelocation_partone_imageView.setImageBitmap(decodedImage3); // set page Third
                    uploadpic_homelocation.setImageBitmap(decodedImage3);     // set page Third
                }


            }



        }



        else      // else if not successfull

            {


            if (requestNo == 200) {

                selectNextButton = 1;
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_profilePiture_sign_partOne.setVisibility(View.VISIBLE);
                Toast.makeText(ViewProfile.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

            else if (requestNo == 199) {

                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                selectNextButton = 0;
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

                hideProgressDialog();

                Toast.makeText(ViewProfile.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }

            else if (requestNo == 203)   // initiate response
            {

                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {

                   hideKeyboard();
                  scrollView_createAgent_firstPart.setVisibility(View.GONE);
                  scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                    submitButton_partfirst.setText(getString(R.string.next));
                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText(getString(R.string.authorizelayout));
                    selectNextButton = 4;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(ViewProfile.this, R.anim.blink));
                  authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);

                    // Toast.makeText(ViewProfile.this, "Waiting time  1 to 10 Minute ", Toast.LENGTH_LONG).show();
                }

                else if (generalResponseModel.getResponseCode() == 404 || generalResponseModel.getResponseCode() == 405 || generalResponseModel.getResponseCode() == 231) { // from server

                    hideKeyboard();
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                    submitButton_partfirst.setText(getString(R.string.next));
                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText(getString(R.string.authorizelayout));
                    selectNextButton = 4;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(ViewProfile.this, R.anim.blink));
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);

                }

                else {

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                    selectNextButton = 0;
                    submitButton_partfirst.setText(getString(R.string.next));
                    scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

                    hideProgressDialog();
                    accountNumberEditText.setText("");
                    Toast.makeText(ViewProfile.this, generalResponseModel.getUserDefinedString(), Toast.LENGTH_LONG).show();
                }


            }
            else if (requestNo == 204)  //   Auth response
            {
                // scrollView_createAgent_firstPart.setVisibility(View.GONE);

                String[] authorizeResponse = generalResponseModel.getUserDefinedString().split("\\|");


                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {

                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                    checkimage();

                }

                else if (generalResponseModel.getResponseCode() == 19)
                {
                    Toast.makeText(ViewProfile.this, getString(R.string.request_cancel_by_customer), Toast.LENGTH_SHORT).show();
                    ViewProfile.this.finish();
                }

                else if (!isTimeUp)
                {
                    hideKeyboard();
                    hideProgressDialog();
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                    submitButton_partfirst.setText(getString(R.string.next));
                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText(getString(R.string.authorizelayout));
                    selectNextButton = 4;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(ViewProfile.this, R.anim.blink));

                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);
                }
                else {

//                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
//                    scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
//                    submitButton_partfirst.setText(getString(R.string.next));
//                    scrollView_otp_page.setVisibility(View.GONE);
//                    scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
//                    submitButton_partfirst.setText(getString(R.string.authorizelayout));
//                    selectNextButton = 4;
//                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
//                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(ViewProfile.this, R.anim.blink));

                    Toast.makeText(ViewProfile.this, getString(R.string.timeOutComplete), Toast.LENGTH_SHORT).show();
                    ViewProfile.this.finish();;

                }
            } else {
                hideProgressDialog();

                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);
                selectNextButton = 0;
                scrollView_otp_page.setVisibility(View.GONE);
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

                Toast.makeText(ViewProfile.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            }
        }

    }



    Handler authRequestDelayHandler = new Handler();


    private Runnable sendData_AuthRequestHandler_Runnable = new Runnable() {
        public void run() {
            try {
                //prepare and send the data here..

                timeCalculation=++timeCalculation;


                authRequestDelayHandler.removeCallbacks(sendData_AuthRequestHandler_Runnable);
                isTimeUp = false;


                if(timeCalculation>=30){   // 10 Minute
                    isTimeUp=true;
                }
                authorizeRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void checkimage() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateCheckImageDownloadingrequest();


          //  new ServerTaskFTP(mComponentInfo, ViewProfile.this, mHandler, requestData, "checkimage", 199).start();

            vollyRequestApi_FTP("checkimage",requestData, 199);


        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }



    void imageDownload() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateImageDownloadingRequest();

            new ServerTaskFTP(mComponentInfo, ViewProfile.this, mHandler, requestData, "downloadimage", 200).start();

          //  vollyRequestApi_FTP("downloadimage",requestData, 200);


        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void vollyRequestApi_FTP(String apiName, final String body, final int requestCode)
    {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,ServerTaskFTP.baseUrl_ftp+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(ViewProfile.this,mComponentInfo,ViewProfile.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(ViewProfile.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }


    private void profileView() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateProfileView();
           // new ServerTask(mComponentInfo, ViewProfile.this, mHandler, requestData, "getViewProfileInJSON", 196).start();

            vollyRequestApi_serverTask("getViewProfileInJSON",requestData, 196);

        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private void authorizeInitiateRequest() {
        backButtonOtp=3;


        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.pleasewait));

         //   new ServerTask(mComponentInfo, ViewProfile.this, mHandler, authorizeInitiateparsingData(), "authorize", 203).start();

            vollyRequestApi_serverTask("authorize",authorizeInitiateparsingData(), 203);

        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private String authorizeInitiateparsingData() {
        String jsonString = "";


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("destination", accountNumber);
            countryObj.put("action", "INITIATE");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    private void authorizeRequest() {


        if (new InternetCheck().isConnected(ViewProfile.this)) {

            showProgressDialog(getString(R.string.pleasewait));

          //  new ServerTask(mComponentInfo, ViewProfile.this, mHandler, generateAuthorizeData(), "authorize", 204).start();

            vollyRequestApi_serverTask("authorize",generateAuthorizeData(), 204);


        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private String generateAuthorizeData() {
        String jsonString = "";


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("destination", accountNumber);
            countryObj.put("action", "INITIATE");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    private void otpVerifyRequest() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.PleaseWaitVerifyingOTP));

           // new ServerTask(mComponentInfo, ViewProfile.this, mHandler, generateOtpVerify(), "getOTPInJSON", 103).start();

            vollyRequestApi_serverTask("getOTPInJSON",generateOtpVerify(), 103);

        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private String generateOtpVerify() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        String newpin = mComponentInfo.getMD5(accountNumber + pin).toUpperCase();

        System.out.println(newpin);


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("pin", newpin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "VIEWPROFILE");
            countryObj.put("accounttype", "MA");

            countryObj.put("otpCode", otpString);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    private String otpGenerate() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        String newpin = mComponentInfo.getMD5(accountNumber + pin).toUpperCase();

        System.out.println(newpin);


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("pin", newpin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "VIEWPROFILE");
            countryObj.put("accounttype", "MA");
            countryObj.put("otpCode", "");      // if generate OTP then otp Code Blank

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private void otpGenerateRequest() {

        if (new InternetCheck().isConnected(ViewProfile.this)) {
            showProgressDialog(getString(R.string.otp_generating));

          //  new ServerTask(mComponentInfo, ViewProfile.this, mHandler, otpGenerate(), "getOTPInJSON", 205).start();

            vollyRequestApi_serverTask("getOTPInJSON",otpGenerate(), 205);

        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validationOTP() {

        boolean ret = false;
        otpString = otp_editText.getText().toString().trim();

        if (otpString.length() >= 4) {
            ret = true;
        } else {
            Toast.makeText(ViewProfile.this, getString(R.string.otp_new), Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    private String generateCheckImageDownloadingrequest() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private String generateImageDownloadingRequest() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);

            countryObj.put("isagentimage", pic_fromServerString);
            countryObj.put("issignature", sign_fromServerString);
            countryObj.put("isidfrontimage", idf_fromServerString);
            countryObj.put("isidbackimage", idb_fromServerString);
            countryObj.put("isformimage", "N");               // no need
            countryObj.put("isbillimage", bill_fromServerString);


            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    private String generateProfileView() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("source", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] data;

        //  data = accountsArrayDetailsList.get(position).toString().split("\\|");

        Toast.makeText(ViewProfile.this, "Listview Click Lis", Toast.LENGTH_SHORT).show();


        // showPreConfirmationPopup(data);


    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            ViewProfile.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(ViewProfile.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            System.out.println(selectNextButton);
            System.out.println(backButtonOtp);

            if (selectNextButton ==3 && backButtonOtp==1) {

                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);

                selectNextButton = 0;
                scrollView_otp_page.setVisibility(View.GONE);
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

            }

           else if (selectNextButton == 0) {

                Intent intent = new Intent(ViewProfile.this, AccountsMenu.class);
                startActivity(intent);
                ViewProfile.this.finish();

            } else if (selectNextButton == 1) {

                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);

                selectNextButton = 0;
                scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 2) {



              /*  scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);

                selectNextButton = 1;
                submitButton_partfirst.setText("Show Details");
                scrollView_profilePiture_sign_parttwo.setVisibility(View.GONE);
                scrollView_profilePiture_sign_partOne.setVisibility(View.VISIBLE);*/
            }
            else if (selectNextButton == 4) {

                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                scrollView_button_createAgent_firstPart.setVisibility(View.VISIBLE);

                selectNextButton = 0;
                scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                submitButton_partfirst.setText(getString(R.string.next));
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

            }




        }
        return super.onOptionsItemSelected(menuItem);
    }

    private int getCountrySelection() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = i;
            }
        }
        return ret;
    }

    private int getMobileNoLength() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = Integer.parseInt(countryMobileNoLengthArray[i]);
            }
        }
        return ret;
    }

    private String getCountryPrefixCode() {
        String ret = "";
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {
                ret = countryPrefixArray[i];
            }
        }
        return ret;
    }

    private boolean validateEmail(String input) {
        boolean ret = false;

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (input.matches(emailPattern)) {
            // Toast.makeText(CreateAccount.this, "valid email address",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(ViewProfile.this, "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    private boolean validateAmount(String input) {
        boolean ret = false;
        try {
            int amt = Integer.parseInt(input);
            if (amt > 0) {
                ret = true;
                cityString = "" + amt;
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
