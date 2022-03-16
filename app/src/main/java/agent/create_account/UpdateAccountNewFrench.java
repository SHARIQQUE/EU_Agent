package agent.create_account;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapter.CountryFlagAdapter;
import agent.activities.R;
import callback.DateSetNotifier;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.DatePickerFragment;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import sucess_receipt.SucessReceiptCreateAgentNewUpdateNew;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;
import agent.thread.ServerTaskFTP;

import static android.provider.MediaStore.Images.Media.insertImage;


public class UpdateAccountNewFrench extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener, DateSetNotifier {

    int noSelectBillHomeSignature = 0, noSelectMpinSignature = 0, noSelectForm = 0, backButtonSelectTermEndCondition = 0, backButtonMenu, tempCamera = 0, formDisplay = 0, noUpdateAnyData = 0, selectCameraGallery_subscriberSign = 0, selectCameraGallery_profilePic = 0, selectCameraGallery_idFront = 0, selectCameraGallery_idback = 0, selectCameraGallery_billHome = 0;
    ImageView green_imageview_agentprofile, green_imageview_form, green_imageview_subscriberSign, green_imageview_idfront, green_imageview_idback, green_imageview_billlocation;
    LinearLayout linearLayout_green_imageview_agentprofile, linearLayout_green_imageview_form, linearLayout_green_imageview_idfront, linearLayout_green_imageview_idback, linearLayout_green_imageview_subscriberSign, linearLayout_green_imageview_billlocation;
    AlertDialog alertDialog;
    PhotoView photoView;
    CheckBox checkBox_termendCondition;
    int timeCalculation = 0;


    Button signaturePad_clearButton_mpinPage, signaturePad_saveButton_billElectricity, signaturePad_clearButton_billElectricity, signaturePad_saveButton_mpin;
    Bitmap signatureBitmapBillElectricity, signatureBitmapMpin, bitmapImageForm;
    private static final int CAMERA_READ_PERMISSION = 3;
    private static final int REQUEST_WIFI_PHONE = 1;
    private static final int REQUEST_SMS_PHONE = 2;
    private static final int REQUEST_READ_PHONE = 0;
    WebView webview_new;


    String profilePic_new, idFront_new, idBack_new, billHome_new, signature_new;
    String profilePic_temp, idFront_temp, idBack_temp, billHome_temp, signature_temp;
    AlertDialog.Builder alertDialogPopup;
    boolean isAuthTimerStart = false;
    private static final int CAMERA_REQUEST_SELFPORTRAIT = 102, SELECT_PICTURE_SELFPORTRAIT = 103, SELECT_PICTURE_ID_FRONT = 104, CAMERA_REQUEST_ID_FRONT = 105, SELECT_PICTURE_ID_BACK = 112, CAMERA_REQUEST_ID_BACK = 113, CAMERA_REQUEST_BILL_LOCATION = 114, SELECT_PICTURE_BILL_LOCATION = 115, CAMERA_REQUEST_MPIN_PAGE = 116, PICTURE_SELECT_MPIN_PAGE = 117;
    int chooseOneSelect = 0, humanSignature = 0, selectNextButton = 0;
    InputStream imageStream, imageStreamCamera;
    ArrayList accountsArrayDetailsList;
    boolean boolselectUploadSignaturePic = false;
    String agentName_reload, errorMsgToDisplay = "";
    String profilePicSelectString, idFrontPicSelectString, idBackPicSelectString, billHomePicSelectString, fromPicSelectString, signaturePicSelectString;

    boolean boolselectUploadPic = false;
    FileOutputStream fileoutputstream;
    View qrEmail_LL;
    Bitmap bitmapSignature;
    Bitmap screenShotImage;
    boolean isTimeUp = true;

    ByteArrayOutputStream bytearrayoutputstream;

    ListView listView;
    String[] responseArrayAgentIdentity, cityDetailsArray, languageSelectDetails, languageSelectCode, bankSelectionArray, transferTagArray, idProofArray, idProofCodeArray, accountTypeArray, planAccountNameLabel, planCodeArray, agentTypeCode, tempState, professionArray, genderArray, genderCodeArray, attachBranchNameType, attachBranchNameCode, cityType, cityCodeArray, nationalityArray, nationalityCodeArray;

    String profession_fromServerString, idProofIssuDate_fromServer, idProofIssuplace_fromServer, responseUpdateAccount, imageDownload_agentimage_fromServer, imageDownload_idfront_fromServer, imageDownload_idback_fromServer, imageDownload_billlocation_fromServer, idProofNumber_fromServer, idProofType_fromServer, imageDownload_signature_fromServer, imageDownload_form_fromServer, gender_fromServerString, nationality_fromServerString, city_fromServerString, language_fromServerString, secondMobileno_fromServerString, fixMobileno_fromServerString,final_dateOfBirthString,temp_date_of_birth_fromServerString, birthPlace_fromServerString, address_fromServerString, residenceArea_fromServerString;
    Toolbar mToolbar;
    ImageView imageview_subscriberForm, camera_mpin_imageview, upload_mpin_imageview, imageview_preview_mpinPage, camera_billlocation_imageview, upload_billlocation_imageview, imageview_selfportrait, imageview_preview_billlocation, imageview_preview_idback, upload_idback_imageview, imageview_preview_idfront, camera_idBack_imageview, camera_front_imageview, upload_idfront_imageview, upload_self_portrait_imageview, camera_selfportrait_imageview;
    Button idproof_calender_button, idproofExpiredDate_calender_button, dateOfBirth_calender_button;
    ComponentMd5SharedPre mComponentInfo;
    String cameraStringMpinPage, pictureChooseIDFrontString, pictureChooseIdBackString, cameraBillString, pictureChoosebillString, pictureChoosePortraitString, otpString, commentString, attachBranchNameString, idproofNameString, cameraStringDrawaHome, uploadStringDrawaHome, cameraElectricityBillString, uploadStringElectricity, agentName, spinnerCountryCodeString, loyalityCasrdNoString, spinnerIdProofTypeTemp, subscriberNameString, customerNameFromServer, emailStringFromServer, agentCode, agentTypeString, professionString, genderString, idproofDueDate, nationalityString, planNameString, spinnerCountryString, transferBasisString, accountNumber, cityString, spinnerStateString, countryString, confirmationCodeString, spinnerIdProofTypeString, LanguageString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString,
            idProofCodeString, genderCodeString, professionCodeString, nationalityCodeString;

    View viewForContainer;
    Button nextButton_idDetails, idproofIssueDate_calender_button, button_skip, button_next_selfportrait, submitButton_partfirst;
    boolean isReview, isServerOperationInProcess, isOtherProfession;
    Dialog successDialog;
    int lengthToCheck, transferCase, accToAccLevel = 0, idProofSelectedPosition, genderSelectedPosition, professionSelectedPosition, languageSelectedPosition, citySelectedPosition, nationalitySelectedPosition, countrySelectedPosition;
    private Spinner spinnerCountryNationality, spinnerAttachedBranchName, spinnerCity, spinnerProfession, spinnerGenderDetails, spinnerState, spinnerCountry, spinnerAccountToDebit, transferBasisSpinner, spinnerIdProofType, spinner_LanguageType;
    private ScrollView scrollView_signatureHuman_mpin_page, scrollView_signatureHuman_electricityBill, scrollView_mpin_Page, scrollView_createAgent_thirdPage_form, scrollView_self_portrait_back_front_page, scrollView_waitingtoSubscriberAuthenticate, scrollView_uploading_form, scrollView_otp_page, scrollView_button_idDetails, scrollView_button_selfportrait_idfront_idback, scrollView_button_firstPart, scrollView_createAgent_firstPart, scrollView_PersonalDetails_form, scrollView_createAgent_confirmationPage;
    private AutoCompleteTextView number_textview_mpinpage, mpinEditText, otp_editText, idproofName_edittext, idissuePlace_edittext, otherProfessionEditText, idproofEditText_datePicker_manually, number_textview_iddetails, number_textview_personalDetails, loyalityCardNumberEditTex, idProofPlaceEditText, birthpalceEditText, agentTypeEditText, secondMobileNumberEditText, fixednumber_edittext,
            residenceAreaEditText, emailEditText, stateEditText, countryEditText, subscriberNameEditText, addressEditText, accountNumberEditText;
    private TextView clear_selfpotrait_textview, clear_idfront_textview, clear_idback_textview, clear_bill_textview, address_reviewpage, idProofNumberTextView, accountNumberTextViewReview, recipientCountryTxtView_Review, agentCodeTextView, accountNameTextView_Review, cityTextView_Review, languageTextView_Review, idproofTypeTextView_Review, countryTextView_Review, addressTextView_Review, idproofTxtView_Review, transferBasisTxtView_Review, recipientNameNoTxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
    private ProgressDialog mDialog;

    private SignaturePad signaturePad_mpinPage, signaturePad_electricityBill;

    LinearLayout linearLayoutListview, scrollView_mpin_Page_linearLayout;
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
                DataParserThread thread = new DataParserThread(UpdateAccountNewFrench.this, mComponentInfo, UpdateAccountNewFrench.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String[] selectDateNew;
    TextView textview_waitingForSubscriberAuthenticate;
    int iLevel = 99;
    boolean isMiniStmtMode = false;
    public AutoCompleteTextView number_textview_uploding, idproofIssuDate_EditText_manually, dateOfBirth_EditText_manullay, idProofExpiredDate_EditText_manually;
    String idissuePlaceString, idProofIssuDateString;
    String dateSetString;
    int keyboardSelection = 0;
    String idProofIssuDate;
    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//otp_editText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                if (keyboardSelection == 0) {
                    validateDetailsFirst();
                } else if (keyboardSelection == 1) {
                    if (validationOTP()) {
                        otpVerifyRequest();
                    }
                } else if (keyboardSelection == 2) {

                    if (validationMpin()) {

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_button_idDetails.setVisibility(View.GONE);
                        scrollView_uploading_form.setVisibility(View.GONE);
                        scrollView_mpin_Page.setVisibility(View.VISIBLE);
                        selectNextButton = 5;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText(getString(R.string.next));


                        if (imageDownload_signature_fromServer == null || imageDownload_signature_fromServer.equalsIgnoreCase("")) {

                            if (selectCameraGallery_subscriberSign == 0 && selectCameraGallery_subscriberSign == 0) {

                                Toast.makeText(UpdateAccountNewFrench.this, getString(R.string.selectSubscriberSign), Toast.LENGTH_SHORT).show();
                            } else {
                                updateAccountRequest();
                            }
                        } else if (imageDownload_form_fromServer.equalsIgnoreCase(imageDownload_agentimage_fromServer)) {

                            updateAccountRequest();
                        } else {

                            updateAccountRequest();
                        }

                    }

                }
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

       /* String   languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }

        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/


        setContentView(R.layout.update_account_new_french);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_updateAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");

        webview_new = (WebView) findViewById(R.id.webview_new);
        webview_new.loadUrl("file:///android_asset/termcondition_fr.html");


        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle("METTRE A JOUR LE COMPTE");
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


            professionArray = getResources().getStringArray(R.array.professionArray);


            idProofArray = getResources().getStringArray(R.array.IDProofTypeArray);
            idProofCodeArray = getResources().getStringArray(R.array.IDProofTypeCodeArray);

        } catch (Exception e) {
            e.printStackTrace();
            //  CreateAccount.this.finish();
        }

        button_skip = (Button) findViewById(R.id.button_skip);
        button_skip.setOnClickListener(this);


        clear_selfpotrait_textview = (TextView) findViewById(R.id.clear_selfpotrait_textview);
        clear_selfpotrait_textview.setOnClickListener(this);

        clear_idfront_textview = (TextView) findViewById(R.id.clear_idfront_textview);
        clear_idfront_textview.setOnClickListener(this);

        clear_idback_textview = (TextView) findViewById(R.id.clear_idback_textview);
        clear_idback_textview.setOnClickListener(this);

        clear_bill_textview = (TextView) findViewById(R.id.clear_bill_textview);
        clear_bill_textview.setOnClickListener(this);

        button_next_selfportrait = (Button) findViewById(R.id.button_next_selfportrait);
        button_next_selfportrait.setOnClickListener(this);

        imageview_subscriberForm = (ImageView) findViewById(R.id.imageview_subscriberForm);
        imageview_subscriberForm.setOnClickListener(this);


        nextButton_idDetails = (Button) findViewById(R.id.nextButton_idDetails);
        nextButton_idDetails.setOnClickListener(this);

        scrollView_mpin_Page_linearLayout = (LinearLayout) findViewById(R.id.scrollView_mpin_Page_linearLayout);
        checkBox_termendCondition = (CheckBox) findViewById(R.id.checkBox_termendCondition);


        scrollView_signatureHuman_mpin_page = (ScrollView) findViewById(R.id.scrollView_signatureHuman_mpin_page);
        signaturePad_saveButton_mpin = (Button) findViewById(R.id.signaturePad_saveButton_mpin);
        signaturePad_clearButton_mpinPage = (Button) findViewById(R.id.signaturePad_clearButton_mpinPage);
        signaturePad_mpinPage = (SignaturePad) findViewById(R.id.signaturePad_mpinPage);
        signaturePad_mpinPage.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                noSelectMpinSignature = 1;
                // Toast.makeText(SignaturePadActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSigned() {
                signaturePad_saveButton_mpin.setEnabled(true);
                signaturePad_clearButton_mpinPage.setEnabled(true);
            }

            @Override
            public void onClear() {
                noSelectMpinSignature = 0;
                signaturePad_saveButton_mpin.setEnabled(true);
                signaturePad_clearButton_mpinPage.setEnabled(false);
            }
        });


        scrollView_signatureHuman_electricityBill = (ScrollView) findViewById(R.id.scrollView_signatureHuman_electricityBill);
        signaturePad_saveButton_billElectricity = (Button) findViewById(R.id.signaturePad_saveButton_billElectricity);
        signaturePad_clearButton_billElectricity = (Button) findViewById(R.id.signaturePad_clearButton_billElectricity);
        signaturePad_electricityBill = (SignaturePad) findViewById(R.id.signaturePad_electricityBill);
        signaturePad_electricityBill.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                noSelectBillHomeSignature = 1;
                // Toast.makeText(SignaturePadActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                signaturePad_saveButton_billElectricity.setEnabled(true);
                signaturePad_clearButton_billElectricity.setEnabled(true);
            }

            @Override
            public void onClear() {
                noSelectBillHomeSignature = 0;
                signaturePad_saveButton_billElectricity.setEnabled(true);
                signaturePad_clearButton_billElectricity.setEnabled(false);
            }
        });


        signaturePad_saveButton_billElectricity.setOnClickListener(this);
        signaturePad_clearButton_billElectricity.setOnClickListener(this);
        signaturePad_saveButton_mpin.setOnClickListener(this);
        signaturePad_clearButton_mpinPage.setOnClickListener(this);

        scrollView_PersonalDetails_form = (ScrollView) findViewById(R.id.scrollView_PersonalDetails_form);
        scrollView_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_createAgent_firstPart);
        scrollView_button_idDetails = (ScrollView) findViewById(R.id.scrollView_button_idDetails);

        scrollView_button_selfportrait_idfront_idback = (ScrollView) findViewById(R.id.scrollView_button_selfportrait_idfront_idback);
        scrollView_button_firstPart = (ScrollView) findViewById(R.id.scrollView_button_firstPart);

        scrollView_otp_page = (ScrollView) findViewById(R.id.scrollView_otp_page);
        scrollView_uploading_form = (ScrollView) findViewById(R.id.scrollView_uploading_form);
        linearLayoutListview = (LinearLayout) findViewById(R.id.linearLayoutListview);
        scrollView_mpin_Page = (ScrollView) findViewById(R.id.scrollView_mpin_Page);
        imageview_preview_mpinPage = (ImageView) findViewById(R.id.imageview_preview_mpinPage);
        upload_mpin_imageview = (ImageView) findViewById(R.id.upload_mpin_imageview);
        scrollView_waitingtoSubscriberAuthenticate = (ScrollView) findViewById(R.id.scrollView_waitingtoSubscriberAuthenticate);
        textview_waitingForSubscriberAuthenticate = (TextView) findViewById(R.id.textview_waitingForSubscriberAuthenticate);


        submitButton_partfirst = (Button) findViewById(R.id.submitButton_partfirst);
        submitButton_partfirst.setOnClickListener(this);

        scrollView_createAgent_confirmationPage = (ScrollView) findViewById(R.id.scrollView_createAgent_confirmationPage);

        scrollView_createAgent_thirdPage_form = (ScrollView) findViewById(R.id.scrollView_createAgent_thirdPage_form);
        scrollView_self_portrait_back_front_page = (ScrollView) findViewById(R.id.scrollView_self_portrait_back_front_page);

        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText("Code de l'Agent-  " + agentCode);

        accountNumberTextViewReview = (TextView) findViewById(R.id.accountNumberTextViewReview);
        idProofNumberTextView = (TextView) findViewById(R.id.idProofNumberTextView);
        address_reviewpage = (TextView) findViewById(R.id.address_reviewpage);

        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setEnabled(false);
        // recipientCountrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        countryArray[0] = "Nationalité";

        System.out.print(countryArray);
        spinnerCountryNationality = (Spinner) findViewById(R.id.spinnerCountryNationality);
        spinnerCountryNationality.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryArray));
        spinnerCountryNationality.setOnItemSelectedListener(UpdateAccountNewFrench.this);

        spinnerProfession = (Spinner) findViewById(R.id.spinnerProfession);
        spinnerProfession.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professionArray));

        spinnerProfession.setOnItemSelectedListener(UpdateAccountNewFrench.this);

        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);

        cityType = getResources().getStringArray(R.array.cityType);
        cityCodeArray = getResources().getStringArray(R.array.cityCodeArray);
        spinnerCity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityType));
        spinnerCity.setOnItemSelectedListener(this);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(UpdateAccountNewFrench.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);

        spinnerIdProofType = (Spinner) findViewById(R.id.spinner_idProofType);
        spinnerIdProofType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, idProofArray));
        spinnerIdProofType.setOnItemSelectedListener(this);


        spinner_LanguageType = (Spinner) findViewById(R.id.spinner_LanguageType);


        attachBranchNameCode = getResources().getStringArray(R.array.attachBranchNameCode);
        attachBranchNameType = getResources().getStringArray(R.array.attachBranchNameType);
        //  attachBranchNameCode[0] = "AGENT BRANCH NAME ";
        spinnerAttachedBranchName = (Spinner) findViewById(R.id.spinnerAttachedBranchName);
        spinnerAttachedBranchName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, attachBranchNameType));
        spinnerAttachedBranchName.setOnItemSelectedListener(this);


        spinnerGenderDetails = (Spinner) findViewById(R.id.spinnerGenderDetails);


        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        //spinnerState.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArray));
        spinnerState.setOnItemSelectedListener(this);

        accountNumberEditText = (AutoCompleteTextView) findViewById(R.id.accountNumberEditText);
        accountNumberEditText.setOnEditorActionListener(this);

        loyalityCardNumberEditTex = (AutoCompleteTextView) findViewById(R.id.loyalityCardNumberEditTex);
        loyalityCardNumberEditTex.setOnEditorActionListener(this);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.emailEditText);
        emailEditText.setOnEditorActionListener(this);

        stateEditText = (AutoCompleteTextView) findViewById(R.id.stateEditText_AccToCash);
        stateEditText.setOnEditorActionListener(this);

        idproof_calender_button = (Button) findViewById(R.id.idproof_calender_button);
        idproof_calender_button.setInputType(InputType.TYPE_NULL);
        idproof_calender_button.setOnTouchListener(this);
        otherProfessionEditText = (AutoCompleteTextView) findViewById(R.id.otherProfessionEditText_CreateAcc);
        idproofName_edittext = (AutoCompleteTextView) findViewById(R.id.idproofName_edittext);
        idissuePlace_edittext = (AutoCompleteTextView) findViewById(R.id.idissuePlace_edittext);
        otp_editText = (AutoCompleteTextView) findViewById(R.id.otp_editText);
        otp_editText.setOnEditorActionListener(this);
        mpinEditText = (AutoCompleteTextView) findViewById(R.id.mpinEditText);
        mpinEditText.setOnEditorActionListener(this);

        number_textview_mpinpage = (AutoCompleteTextView) findViewById(R.id.number_textview_mpinpage);


        idproofExpiredDate_calender_button = (Button) findViewById(R.id.idproofExpiredDate_calender_button);
        idproofExpiredDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofExpiredDate_calender_button.setOnTouchListener(this);

        dateOfBirth_calender_button = (Button) findViewById(R.id.dateOfBirth_calender_button);
        dateOfBirth_calender_button.setInputType(InputType.TYPE_NULL);
        dateOfBirth_calender_button.setOnTouchListener(this);

        idproofIssueDate_calender_button = (Button) findViewById(R.id.idproofIssueDate_calender_button);
        idproofIssueDate_calender_button.setInputType(InputType.TYPE_NULL);
        idproofIssueDate_calender_button.setOnTouchListener(this);


        idproofEditText_datePicker_manually = (AutoCompleteTextView) findViewById(R.id.idproofEditText_datePicker_manually);
        idproofEditText_datePicker_manually.setOnEditorActionListener(this);
        number_textview_personalDetails = (AutoCompleteTextView) findViewById(R.id.number_textview_personalDetails);
        number_textview_iddetails = (AutoCompleteTextView) findViewById(R.id.number_textview_iddetails);
        subscriberNameEditText = (AutoCompleteTextView) findViewById(R.id.subscriberNameEditText);
        addressEditText = (AutoCompleteTextView) findViewById(R.id.address_EditText_AccToCash);
        addressEditText.setOnEditorActionListener(this);
        countryEditText = (AutoCompleteTextView) findViewById(R.id.countryEditText_AccToCash);
        countryEditText.setOnEditorActionListener(this);
        dateOfBirth_EditText_manullay = (AutoCompleteTextView) findViewById(R.id.dateOfBirth_EditText_manullay);
        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        number_textview_uploding = (AutoCompleteTextView) findViewById(R.id.number_textview_uploding);
        idProofExpiredDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idProofExpiredDate_EditText_manually);
        idProofPlaceEditText = (AutoCompleteTextView) findViewById(R.id.idProofPlaceEditText_AccToCash);
        idProofPlaceEditText.setOnEditorActionListener(this);

        birthpalceEditText = (AutoCompleteTextView) findViewById(R.id.birthpalceEditText);
        birthpalceEditText.setOnEditorActionListener(this);

        agentTypeEditText = (AutoCompleteTextView) findViewById(R.id.agentTypeEditText_AccToCash);
        agentTypeEditText.setOnEditorActionListener(this);

        secondMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.secondMobileNumberEditText);
        secondMobileNumberEditText.setOnEditorActionListener(this);

        fixednumber_edittext = (AutoCompleteTextView) findViewById(R.id.fixednumber_edittext);
        fixednumber_edittext.setOnEditorActionListener(this);

        residenceAreaEditText = (AutoCompleteTextView) findViewById(R.id.residenceAreaEditText_AccToCash);
        residenceAreaEditText.setOnEditorActionListener(this);

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

        webview_new = (WebView) findViewById(R.id.webview_new);
        webview_new.loadUrl("file:///android_asset/termcondition_fr.html");


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


        idproofIssuDate_EditText_manually.setOnEditorActionListener(this);
        residenceAreaEditText.setOnEditorActionListener(this);

        setInputType(1);

        scrollView_button_firstPart.setVisibility(View.VISIBLE);

        imageview_selfportrait = (ImageView) findViewById(R.id.imageview_selfportrait);
        imageview_preview_idfront = (ImageView) findViewById(R.id.imageview_preview_idfront);
        imageview_preview_idback = (ImageView) findViewById(R.id.imageview_preview_idback);
        imageview_preview_billlocation = (ImageView) findViewById(R.id.imageview_preview_billlocation);


        imageview_selfportrait.setOnClickListener(this);
        imageview_preview_idfront.setOnClickListener(this);
        imageview_preview_idback.setOnClickListener(this);
        imageview_preview_billlocation.setOnClickListener(this);


        linearLayout_green_imageview_agentprofile = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_agentprofile);
        linearLayout_green_imageview_idfront = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_idfront);
        linearLayout_green_imageview_idback = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_idback);
        linearLayout_green_imageview_billlocation = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_billlocation);
        linearLayout_green_imageview_subscriberSign = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_subscriberSign);
        linearLayout_green_imageview_form = (LinearLayout) findViewById(R.id.linearLayout_green_imageview_form);


        green_imageview_agentprofile = (ImageView) findViewById(R.id.green_imageview_agentprofile);
        green_imageview_idfront = (ImageView) findViewById(R.id.green_imageview_idfront);
        green_imageview_idback = (ImageView) findViewById(R.id.green_imageview_idback);
        green_imageview_billlocation = (ImageView) findViewById(R.id.green_imageview_billlocation);
        green_imageview_subscriberSign = (ImageView) findViewById(R.id.green_imageview_subscriberSign);
        green_imageview_form = (ImageView) findViewById(R.id.green_imageview_form);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        upload_self_portrait_imageview = (ImageView) findViewById(R.id.upload_self_portrait_imageview);
        upload_self_portrait_imageview.setClickable(true);
        upload_self_portrait_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser_selfPortrait();
            }
        });

        camera_selfportrait_imageview = (ImageView) findViewById(R.id.camera_selfportrait_imageview);
        camera_selfportrait_imageview.setClickable(true);
        camera_selfportrait_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(UpdateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {

                    openCamera_selfportrait();
                }
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        upload_idfront_imageview = (ImageView) findViewById(R.id.upload_idfront_imageview);
        upload_idfront_imageview.setClickable(true);
        upload_idfront_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser_idfront();
            }
        });


        camera_front_imageview = (ImageView) findViewById(R.id.camera_front_imageview);
        camera_front_imageview.setClickable(true);
        camera_front_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {


                    openCamera_idFront();
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        camera_idBack_imageview = (ImageView) findViewById(R.id.camera_idBack_imageview);
        camera_idBack_imageview.setClickable(true);
        camera_idBack_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {

                    openCamera_idBack();
                }

            }
        });


        upload_idback_imageview = (ImageView) findViewById(R.id.upload_idback_imageview);
        upload_idback_imageview.setClickable(true);
        upload_idback_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser_idBack();


            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        upload_mpin_imageview = (ImageView) findViewById(R.id.upload_mpin_imageview);
        upload_mpin_imageview.setClickable(true);
        upload_mpin_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customeAlertDialogSignature_mpinpage();
            }
        });


        camera_mpin_imageview = (ImageView) findViewById(R.id.camera_mpin_imageview);
        camera_mpin_imageview.setClickable(true);
        camera_mpin_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(UpdateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {

                    openCamera_MpinPage();
                }


            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        upload_billlocation_imageview = (ImageView) findViewById(R.id.upload_billlocation_imageview);
        upload_billlocation_imageview.setClickable(true);
        upload_billlocation_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                customeAlertDialogSignature_billElectricity();
            }
        });

        camera_billlocation_imageview = (ImageView) findViewById(R.id.camera_billlocation_imageview);
        camera_billlocation_imageview.setClickable(true);
        camera_billlocation_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(UpdateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                } else {

                    openCamera_billLocation();
                }


            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    void openCamera_MpinPage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_MPIN_PAGE);
    }

    void openImageChooser_MpinPage() {     // SELECT_PICTURE_BACK_ID_DOCUMENT CAMERA_REQUEST_BACK_ID_DOCUMENT
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECT_MPIN_PAGE);
    }

    void cameraTake_MpinPage(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = insertImage(UpdateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            System.out.println(bitmap1);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            cameraStringMpinPage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            System.out.println(cameraStringMpinPage);
            imageview_preview_mpinPage.setImageBitmap(bitmap);

            selectCameraGallery_subscriberSign = 1;

            formDisplay = 1;
            customeLayout_subscriberFormImage();
            formDisplay = 2;
            noSelectForm = 1;

            signaturePicSelectString = "SIGNATURE";
        }
    }

    void customeAlertDialogSignature_billElectricity() {


        Resources res = getResources();
        CharSequence[] items = res.getStringArray(R.array.signaturehuman_gallery_french);


        // Creating and Building the Dialog
        alertDialogPopup = new AlertDialog.Builder(this);
        alertDialogPopup.setTitle("Choix");
        //  alertDialogAuth.setCancelable(false);

        alertDialogPopup.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                        scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                        scrollView_otp_page.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.VISIBLE);


                        break;

                    case 1:

                        openImageChooser_billlocation();

                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogPopup.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    void customeAlertDialogSignature_mpinpage() {

        Resources res = getResources();
        CharSequence[] items = res.getStringArray(R.array.signaturehuman_gallery_french);


        // Creating and Building the Dialog
        alertDialogPopup = new AlertDialog.Builder(this);
        alertDialogPopup.setTitle("Choix");
        //  alertDialogAuth.setCancelable(false);

        alertDialogPopup.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                        scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                        scrollView_otp_page.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_signatureHuman_mpin_page.setVisibility(View.VISIBLE);
                        humanSignature = 3;

                        break;

                    case 1:

                        openImageChooser_MpinPage();

                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogPopup.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    void setSubscriptionFormValues(View v, String firstName, String lastName, String datePlaceBirth,
                                   String nationality, String issueDate, String profession, String phoneNumber, String email,
                                   String maritalString, String address, String cityString, String residence, String planName,
                                   String attachBranchNameString, String accountNumber, String language, Bitmap userPicture, Bitmap userSignature) {

        TextView firstName_TxtView,
                lastName_TxtView,
                datePlaceBirth_TxtView,
                nationality_TxtView,
                issueDate_TxtView,
                profession_TxtView,
                phoneNumber_TxtView,
                email_TxtView,
                maritalStatus_TxtView,
                adress_TxtView,
                residence_TxtView,
                placeName_TxtView,
                branchName_TxtView,
                euphone_TxtView,
                city_TxtView, identification_textview,
                language_TxtView;

        ImageView profilePic, signaturePic;


        profilePic = (ImageView) v.findViewById(R.id.user_picture);
        signaturePic = (ImageView) v.findViewById(R.id.user_signature);

        firstName_TxtView = (TextView) v.findViewById(R.id.firstName_TxtView);
        lastName_TxtView = (TextView) v.findViewById(R.id.lastName_TxtView);
        datePlaceBirth_TxtView = (TextView) v.findViewById(R.id.datePlaceBirth_TxtView);
        nationality_TxtView = (TextView) v.findViewById(R.id.nationality_TxtView);

        issueDate_TxtView = (TextView) v.findViewById(R.id.issueDate_TxtView);
        profession_TxtView = (TextView) v.findViewById(R.id.profession_TxtView);
        phoneNumber_TxtView = (TextView) v.findViewById(R.id.phoneNumber_TxtView);
        email_TxtView = (TextView) v.findViewById(R.id.email_TxtView);
        maritalStatus_TxtView = (TextView) v.findViewById(R.id.maritalStatus_TxtView);
        adress_TxtView = (TextView) v.findViewById(R.id.adress_TxtView);
        residence_TxtView = (TextView) v.findViewById(R.id.residence_TxtView);
        placeName_TxtView = (TextView) v.findViewById(R.id.placeName_TxtView);
        branchName_TxtView = (TextView) v.findViewById(R.id.branchName_TxtView);
        euphone_TxtView = (TextView) v.findViewById(R.id.euphone_TxtView);
        language_TxtView = (TextView) v.findViewById(R.id.language_TxtView);
        city_TxtView = (TextView) v.findViewById(R.id.city_textview);
        identification_textview = (TextView) v.findViewById(R.id.identification_textview);


        firstName_TxtView.setText("Prénom (s).......................");
        lastName_TxtView.setText("Noms:.... " + customerNameFromServer + " ..................");
        datePlaceBirth_TxtView.setText("Date et lieu de naissance :... " + final_dateOfBirthString + "  " + birthPlace_fromServerString + " ..................");

        nationality_TxtView.setText("Nationalité...... " + nationality_fromServerString + " ..................");
        identification_textview.setText("........... " + idProofNumber_fromServer + " ....." + idProofType_fromServer + " ..................");


        issueDate_TxtView.setText("du.... " + idProofIssuDate_fromServer + " ....à....." + idProofIssuplace_fromServer + ".....par");


        profession_TxtView.setText("Profession..... " + profession_fromServerString + " ..................");

        String fixMobileno_subscriptionform = getCountryPrefixCode() + fixMobileno_fromServerString;

        phoneNumber_TxtView.setText("Numéro de téléphone :... " + fixMobileno_subscriptionform + " ..................");
        email_TxtView.setText("Email.... " + email + " ..................");
        maritalStatus_TxtView.setText("Situation matrimoniale :.... " + maritalString + " ..................");
        adress_TxtView.setText("Adresse :.. " + address + " ..................");
        residence_TxtView.setText("Ville/Quartier de résidence :..... " + city_fromServerString + " ..................");
        placeName_TxtView.setText("lieu dit :.. " + planName + " ..................");
        branchName_TxtView.setText("Account opening branch.... " + attachBranchNameString + " ..................");
        euphone_TxtView.setText(".......... " + accountNumber + " ..................");
        language_TxtView.setText("Language.... " + language + " ..................");

        profilePic.setImageBitmap(userPicture);
        signaturePic.setImageBitmap(userSignature);


    }

    Bitmap signatureBitmap;
    byte[] decodedString6;

    void customeLayout_subscriberFormImage() {

        try {


            LayoutUtilities layoutUtilities = new LayoutUtilities(this, 1500, 1000);
            layoutUtilities.initiateViewForImage(LayoutUtilities.VIEWTEMPLATE.THREE_BLOCK_HEADER_TWO);  // THREE_BLOCK_HEADER_TWO


            if (cameraStringMpinPage == null || cameraStringMpinPage.equalsIgnoreCase("")) {
                decodedString6 = Base64.decode(imageDownload_signature_fromServer, Base64.DEFAULT);
                signatureBitmap = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
            } else {
                decodedString6 = Base64.decode(cameraStringMpinPage, Base64.DEFAULT);
                signatureBitmap = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
            }


            byte[] decodedString2;
            Bitmap pictureSignBitmap;

            if (pictureChoosePortraitString == null || pictureChoosePortraitString.equalsIgnoreCase("")) {
                decodedString2 = Base64.decode(imageDownload_agentimage_fromServer, Base64.DEFAULT);
                pictureSignBitmap = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            } else {
                decodedString2 = Base64.decode(pictureChoosePortraitString, Base64.DEFAULT);
                pictureSignBitmap = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            }


            View v = getLayoutInflater().inflate(R.layout.subscription_form_layout, (ViewGroup) layoutUtilities.getParentLL());

            setSubscriptionFormValues(v,
                    "", "", birthPlace_fromServerString,
                    nationalityCodeString, idproofNameString, professionCodeString,
                    fixMobileno_fromServerString, emailStringFromServer, "",
                    address_fromServerString, cityString, "", "",
                    attachBranchNameString, accountNumber, LanguageString, pictureSignBitmap, signatureBitmap);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            layoutUtilities.finaliseViewForImage();

            layoutUtilities.restParentLL();


            /////////////   ////////////////
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            layoutUtilities.restParentLL();
            layoutUtilities.setMiscellinousView(v);
            layoutUtilities.initiateViewForImage(LayoutUtilities.VIEWTEMPLATE.MISCELLINOUS);
            layoutUtilities.finaliseViewForImage();
            layoutUtilities.getFinalImage().compress(Bitmap.CompressFormat.PNG, 100, baos);

            /////////////   ////////////////
            byte[] b = baos.toByteArray();
            if (imageDownload_form_fromServer == null || imageDownload_form_fromServer.equalsIgnoreCase("")) {
            } else {
                imageDownload_form_fromServer = Base64.encodeToString(b, Base64.DEFAULT);
            }


            if (formDisplay == 0) {
                imageview_subscriberForm.setImageBitmap(layoutUtilities.getFinalImage());
                photoView.setImageBitmap(layoutUtilities.getFinalImage());   // final zoom in zoom out pic set
            } else if (formDisplay == 1) {
                imageview_subscriberForm.setImageBitmap(layoutUtilities.getFinalImage());  // final zoom in zoom out pic set
            } else {
                photoView.setImageBitmap(layoutUtilities.getFinalImage());   // final zoom in zoom out pic set
            }

            try {         // save in Gallery
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut = null;
                Integer counter = 0;
                File file = new File(path, "pic" + counter + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                fOut = new FileOutputStream(file);


                layoutUtilities.getFinalImage().compress(Bitmap.CompressFormat.JPEG, 99, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream

                MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            } catch (Exception e) {


                e.printStackTrace();

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void customeLayout_imageFormFromServer()  // image from from server
    {

        try {

            imageview_subscriberForm.setImageBitmap(bitmapImageForm);
            photoView.setImageBitmap(bitmapImageForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void cameraTake_selfportrait(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = insertImage(UpdateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            System.out.println(bitmap1);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            pictureChoosePortraitString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_selfportrait.setImageBitmap(bitmap);
            selectCameraGallery_profilePic = 1;
            profilePicSelectString = "PROFILEPIC";
        }
    }


    void imageDownload() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateImageDownloadingRequest();
            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "downloadimage", 202).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private String generateImageDownloadingRequest() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);

            countryObj.put("isagentimage", "Y");
            countryObj.put("issignature", "Y");
            countryObj.put("isidfrontimage", "Y");
            countryObj.put("isidbackimage", "Y");
            countryObj.put("isformimage", "Y");   // error temporary remove  no response id Y
            countryObj.put("isbillimage", "Y");


            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }


    void pictureChoose_portrait(Intent data) {
        Uri uri = data.getData();

        if (null != uri) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            System.out.println(selectedImage);


            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            // imageview_selfportrait.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            pictureChoosePortraitString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_selfportrait.setImageBitmap(selectedImage);
            selectCameraGallery_profilePic = 1;
            profilePicSelectString = "PROFILEPIC";

        }
    }


    void cameraTake_idfront(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = insertImage(UpdateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            System.out.println(bitmap1);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            pictureChooseIDFrontString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_idfront.setImageBitmap(bitmap);
            selectCameraGallery_idFront = 1;
            idFrontPicSelectString = "IDFRONT";
        }
    }

    void pictureChoose_idFront(Intent data) {
        Uri uri = data.getData();

        if (null != uri) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            System.out.println(selectedImage);


            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            // imageview_preview_idfront.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            pictureChooseIDFrontString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_idfront.setImageBitmap(selectedImage);
            selectCameraGallery_idFront = 1;
            idFrontPicSelectString = "IDFRONT";


        }
    }


    void cameraTake_idBack(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = insertImage(UpdateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            System.out.println(bitmap1);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            pictureChooseIdBackString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_idback.setImageBitmap(bitmap);
            selectCameraGallery_idback = 1;
            idBackPicSelectString = "IDBACK";
        }
    }

    void pictureChoose_idBack(Intent data) {
        Uri uri = data.getData();

        if (null != uri) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            System.out.println(selectedImage);


            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //  imageview_preview_idback.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            pictureChooseIdBackString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_idback.setImageBitmap(selectedImage);
            selectCameraGallery_idback = 1;
            idBackPicSelectString = "IDBACK";

        }
    }


    void cameraTake_bill(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = insertImage(UpdateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            System.out.println(bitmap1);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            cameraBillString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_billlocation.setImageBitmap(bitmap);
            selectCameraGallery_billHome = 1;
            billHomePicSelectString = "BILLHOME";
        }
    }

    void pictureChoose_bill(Intent data) {
        Uri uri = data.getData();

        if (null != uri) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            System.out.println(selectedImage);


            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            // imageview_preview_billlocation.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            cameraBillString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_billlocation.setImageBitmap(selectedImage);
            selectCameraGallery_billHome = 1;
            billHomePicSelectString = "BILLHOME";

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE_SELFPORTRAIT) {
                pictureChoose_portrait(data);
            } else if (requestCode == CAMERA_REQUEST_SELFPORTRAIT) {
                cameraTake_selfportrait(data);
            } else if (requestCode == SELECT_PICTURE_ID_FRONT) {
                pictureChoose_idFront(data);
            } else if (requestCode == CAMERA_REQUEST_ID_FRONT) {
                cameraTake_idfront(data);
            } else if (requestCode == SELECT_PICTURE_ID_BACK) {
                pictureChoose_idBack(data);
            } else if (requestCode == CAMERA_REQUEST_ID_BACK) {
                cameraTake_idBack(data);
            } else if (requestCode == SELECT_PICTURE_BILL_LOCATION) {
                pictureChoose_bill(data);
            } else if (requestCode == CAMERA_REQUEST_BILL_LOCATION) {
                cameraTake_bill(data);

            } else if (requestCode == PICTURE_SELECT_MPIN_PAGE) {
                pictureChoose_MpinPage(data);
            } else if (requestCode == CAMERA_REQUEST_MPIN_PAGE) {
                cameraTake_MpinPage(data);
            }
        }

    }

    void pictureChoose_MpinPage(Intent data) {


        Uri uriMpinPage = data.getData();

        if (null != uriMpinPage) {

            try {
                imageStream = getContentResolver().openInputStream(uriMpinPage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            System.out.println(selectedImage);


            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            // imageview_preview_mpinPage.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            cameraStringMpinPage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_mpinPage.setImageBitmap(selectedImage);
            selectCameraGallery_subscriberSign = 1;

            formDisplay = 1;
            customeLayout_subscriberFormImage();
            formDisplay = 2;
            noSelectForm = 1;

            signaturePicSelectString = "SIGNATURE";

        }

    }


    void openCamera_selfportrait() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_SELFPORTRAIT);
    }

    void openCamera_idFront() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ID_FRONT);
    }

    void openCamera_idBack() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ID_BACK);
    }


    void openImageChooser_selfPortrait() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_SELFPORTRAIT);
    }


    void openCamera_billLocation() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_BILL_LOCATION);
    }

    void openImageChooser_idfront() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_ID_FRONT);
    }

    void openImageChooser_billlocation() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_BILL_LOCATION);
    }

    void openImageChooser_idBack() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_ID_BACK);
    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                accountNumberEditText.setText("");
                accountNumberEditText.setHint(getString(R.string.hintCreateAccountNumber_french));
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
                accountNumberEditText.setHint(String.format(getString(R.string.hintCreateAccountNumber_french), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);


                break;

            case R.id.spinnerAttachedBranchName:
                attachBranchNameString = spinnerAttachedBranchName.getSelectedItem().toString();

                if (i != 0) {
                    attachBranchNameString = attachBranchNameCode[i];
                } else {
                    attachBranchNameString = "";
                }

                System.out.println(attachBranchNameString);

                break;


            case R.id.spinner_LanguageType:

                LanguageString = spinner_LanguageType.getSelectedItem().toString();

                if (i != 0) {
                    LanguageString = languageSelectCode[i];
                } else {
                    LanguageString = "";
                }


                break;


            case R.id.spinner_idProofType:

                if (i != 0) {

                    spinnerIdProofTypeString = idProofArray[i];
                    idProofCodeString = idProofCodeArray[i];
                    idProofSelectedPosition = i;
                } else {

                    spinnerIdProofTypeString = "";
                    idProofCodeString = "";
                    idProofSelectedPosition = 0;
                }


                break;

            case R.id.spinnerGenderDetails:


                genderString = spinnerGenderDetails.getSelectedItem().toString();


                if (i != 0) {
                    genderString = genderArray[i];
                    genderCodeString = genderCodeArray[i];
                    genderSelectedPosition = i;

                } else {
                    genderString = "";
                    genderCodeString = "";
                    genderSelectedPosition = 0;

                }


                break;

            case R.id.spinnerCity:


                cityString = spinnerCity.getSelectedItem().toString();

                if (i != 0) {

                    cityString = cityCodeArray[i];
                } else {
                    cityString = "";
                }

                System.out.println(cityString);

            case R.id.spinnerCountry:

                //   validateDetailsPartFirst();

                setInputType(transferBasisSpinner.getSelectedItemPosition());


                break;


            case R.id.spinnerProfession:

                if (i != 0) {

                    professionString = professionArray[i];
                    professionCodeString = professionArray[i];
                    professionSelectedPosition = i;

                } else {
                    professionString = "";
                    professionCodeString = "";
                    professionSelectedPosition = 0;

                }


                break;

            case R.id.spinnerCountryNationality:
                if (i != 0) {
                    nationalityString = countryArray[i];
                    nationalityCodeString = countryCodeArray[i];
                    nationalitySelectedPosition = i;

                } else {

                    nationalityString = "";
                    nationalityCodeString = "";
                    nationalitySelectedPosition = 0;
                }


                break;


        }
    }

    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        UpdateAccountNewFrench.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez sélectionner une date de naissance valide", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez sélectionner une date de naissance valide", Toast.LENGTH_LONG).show();
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

            return ret;
        }
    }

    void customeAlertDialog_authorize_otp() {

        selectNextButton = 0;
        scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
        scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
        scrollView_otp_page.setVisibility(View.GONE);

        scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
        scrollView_button_firstPart.setVisibility(View.VISIBLE);

        Resources res = getResources();
        CharSequence[] items = res.getStringArray(R.array.otp_authorize_french);


        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choix");
        // builder.setCancelable(false);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                alertDialog.setCancelable(true);

                switch (item) {

                    case 0:


                        otpGenerateRequest();   // demo


                        break;

                    case 1:


                        authorizeInitiateRequest();

                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void otpVerifyRequest() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter la verification de l OTP");

            //  new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, generateOtpVerify(), "getOTPInJSON", 103).start();

            vollyRequestApi_serverTask("getOTPInJSON", generateOtpVerify(), 103);

        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateOtpVerify() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        String newpin = mComponentInfo.getMD5(accountNumber + pin).toUpperCase();


        try {
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("pin", newpin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("transtype", "UPDATEACCOUNT");
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
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez sélectionner la date de validité de la preuve d'identification valide", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez sélectionner une pièce d'identité valide Date de validation", Toast.LENGTH_LONG).show();
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
        } catch (Exception e) {

        }

    }


    private boolean validationIdDetailsForm() {


        boolean ret = false;


        spinnerIdProofTypeString = spinnerIdProofType.getSelectedItem().toString();
        if (spinnerIdProofType.getSelectedItemPosition() != 0) {

            idproofNameString = idproofName_edittext.getText().toString().trim();
            if (idproofNameString.length() >= 3) {


                idProofIssuDateString = idproofIssuDate_EditText_manually.getText().toString().trim();
                if (idProofIssuDateString.length() > 9 && idProofIssuDateString.matches("\\d{2}-\\d{2}-\\d{4}")) {

                    idissuePlaceString = idissuePlace_edittext.getText().toString().trim();
                    if (idissuePlaceString.length() >= 3) {


                        idproofDueDate = idProofExpiredDate_EditText_manually.getText().toString().trim();
                        if (idproofDueDate.length() >= 9) {


                            System.out.println(spinnerIdProofTypeString);
                            System.out.println(idproofNameString);
                            System.out.println(idProofIssuDateString);
                            System.out.println(idissuePlaceString);
                            System.out.println(idproofDueDate);


                            ret = true;

                        } else {
                            Toast.makeText(UpdateAccountNewFrench.this, "Date expiration pièce JJ-MM-AAA", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(UpdateAccountNewFrench.this, "Lieu d émission de la piéce", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UpdateAccountNewFrench.this, "Date emission pièce JJ-MM-AAAA", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(UpdateAccountNewFrench.this, "Nom de la piéce", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Type de piéce", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }


    private boolean validationDetailsGenderAddressEmailCityForm() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            if (spinnerGenderDetails.getSelectedItemPosition() != 0) {

                if (spinner_LanguageType.getSelectedItemPosition() != 0) {

                    lengthToCheck = getMobileNoLength();
                    secondMobileno_fromServerString = secondMobileNumberEditText.getText().toString().trim();

                    errorMsgToDisplay = String.format(getString(R.string.hintSecondMobileNumber_french), lengthToCheck + "");
                    //     if (secondMobileno_fromServerString.length() == lengthToCheck) {


                    errorMsgToDisplay = String.format(getString(R.string.hintSecondFixedMobileNumber_french), lengthToCheck + "");
                    fixMobileno_fromServerString = fixednumber_edittext.getText().toString().trim();

                    //   if (fixMobileno_fromServerString.length() == lengthToCheck) {

                    emailStringFromServer = emailEditText.getText().toString().trim();
                    //       if (emailStringFromServer.length() >= 4 && validateEmail(emailStringFromServer)) {


                    //   if (spinnerCity.getSelectedItemPosition() != 0) {

                    address_fromServerString = addressEditText.getText().toString().trim();
                    //        if (address_fromServerString.length() >= 3) {


                    final_dateOfBirthString = dateOfBirth_EditText_manullay.getText().toString().trim();
                    if (final_dateOfBirthString.length() > 9 && final_dateOfBirthString.matches("\\d{2}-\\d{2}-\\d{4}")) {

                        birthPlace_fromServerString = birthpalceEditText.getText().toString().trim();
                        //       if (birthPlace_fromServerString.length() >= 3) {


                        //   if (spinnerCountryNationality.getSelectedItemPosition() != 0) {

                        residenceArea_fromServerString = residenceAreaEditText.getText().toString().trim();
                        //         if (residenceArea_fromServerString.length() >= 3) {

                        noUpdateAnyData = 1;  // set 1


                        ret = true;

                                           /* } else {
                                                Toast.makeText(UpdateAccountNewFrench.this, "Lieu de Résidence", Toast.LENGTH_SHORT).show();
                                            }*/

                                            /*} else {
                                                Toast.makeText(UpdateAccount.this, getString(R.string.nationality), Toast.LENGTH_SHORT).show();
                                            }*/


                                       /* } else {
                                            Toast.makeText(UpdateAccountNewFrench.this, "Lieu de naissance", Toast.LENGTH_SHORT).show();
                                        }*/

                    } else {
                        Toast.makeText(UpdateAccountNewFrench.this, "Saisir la Date de naissance", Toast.LENGTH_LONG).show();
                    }


                               /* } else {
                                    Toast.makeText(UpdateAccountNewFrench.this, "Address", Toast.LENGTH_SHORT).show();
                                }
                           *//* } else {
                                Toast.makeText(UpdateAccount.this, getString(R.string.cityReceiptNew), Toast.LENGTH_SHORT).show();
                            }*//*

                            } else {
                                Toast.makeText(UpdateAccountNewFrench.this, "Email", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(UpdateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                            // ret = false;
                        }


                    } else {
                        Toast.makeText(UpdateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        // ret = false;
                    }*/
                } else {
                    Toast.makeText(UpdateAccountNewFrench.this, "Language choisie", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(UpdateAccountNewFrench.this, "Sex", Toast.LENGTH_LONG).show();
            }


        } else

        {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    void zoomInZoomOutPic_SubscriberForm_MpinPage() {

        android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(UpdateAccountNewFrench.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout_zoomin_zoomout, null);

        photoView = mView.findViewById(R.id.imageView);

        if (noSelectForm == 0) {
            customeLayout_subscriberFormImage();
        } else if (noSelectForm == 2) {
            // customeLayout_imageFormFromServer();  // form image from server
            customeLayout_subscriberFormImage();
        } else {
            customeLayout_subscriberFormImage();
        }


        mBuilder.setView(mView);
        android.support.v7.app.AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_skip:


                imageview_selfportrait.setImageDrawable(null);
                imageview_preview_idfront.setImageDrawable(null);
                imageview_preview_idback.setImageDrawable(null);
                imageview_preview_billlocation.setImageDrawable(null);

                selectCameraGallery_profilePic = 0;
                selectCameraGallery_idFront = 0;
                selectCameraGallery_idback = 0;
                selectCameraGallery_billHome = 0;

                break;


            case R.id.signaturePad_saveButton_billElectricity:
                signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();

                if (noSelectBillHomeSignature == 0) {
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez choisir dessiner signe", Toast.LENGTH_SHORT).show();

                    // Toast.makeText(CreateAccountNewTemp.this, "Veuillez choisir dessiner signe",Toast.LENGTH_SHORT).show();
                } else {


                    if (addJpgSignatureToGallery(signatureBitmapBillElectricity, "BillElectricity")) {

                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_otp_page.setVisibility(View.GONE);
                        scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);

                        scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
                        selectNextButton = 2;

                    }
                }

                break;

            case R.id.signaturePad_clearButton_billElectricity: {
                signaturePad_electricityBill.clear();
            }

            break;


            case R.id.signaturePad_saveButton_mpin:

                signatureBitmapMpin = signaturePad_mpinPage.getSignatureBitmap();

                if (noSelectMpinSignature == 0) {
                    Toast.makeText(UpdateAccountNewFrench.this, "Veuillez choisir dessiner signe", Toast.LENGTH_SHORT).show();

                    // Toast.makeText(CreateAccountNewTemp.this, "Veuillez choisir dessiner signe",Toast.LENGTH_SHORT).show();
                } else {

                    if (addJpgSignatureToGallery(signatureBitmapMpin, "MPINPAGE")) {

                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);

                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Suivant");

                        selectNextButton = 5;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);

                        formDisplay = 1;
                        customeLayout_subscriberFormImage();
                        formDisplay = 2;
                        noSelectForm = 1;
                        humanSignature = 3;

                    }

                }

                break;

            case R.id.signaturePad_clearButton_mpinPage: {
                signaturePad_mpinPage.clear();
            }

            break;

            case R.id.imageview_subscriberForm:

                if (noSelectForm == 0) {
                    System.out.println("first time not select form");
                } else {
                    zoomInZoomOutPic_SubscriberForm_MpinPage();
                }

                break;

            case R.id.clear_selfpotrait_textview:
                imageview_selfportrait.setImageDrawable(null);

                break;


            case R.id.clear_idfront_textview:
                imageview_preview_idfront.setImageDrawable(null);
                break;

            case R.id.clear_idback_textview:
                imageview_preview_idback.setImageDrawable(null);
                break;

            case R.id.clear_bill_textview:
                imageview_preview_billlocation.setImageDrawable(null);
                break;


            case R.id.button_next_selfportrait:  // click( second page
            {


                if (selectCameraGallery_profilePic == 1 || selectCameraGallery_idFront == 1 || selectCameraGallery_idback == 1 || selectCameraGallery_billHome == 1) {
                    // demo if any one selected request

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                    scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);

                    selectNextButton = 3;
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("Suivant");
                    scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

                } else {

                    if (imageDownload_agentimage_fromServer.equalsIgnoreCase("")
                            && imageDownload_idfront_fromServer.equalsIgnoreCase("")
                            && imageDownload_idback_fromServer.equalsIgnoreCase("")
                            && imageDownload_billlocation_fromServer.equalsIgnoreCase("")
                            ) {

                        Toast.makeText(UpdateAccountNewFrench.this, "S il vous plaît télécharger l'image", Toast.LENGTH_SHORT).show();

                    } else {

                        scrollView_otp_page.setVisibility(View.GONE);
                        scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                        scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);

                        selectNextButton = 3;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Suivant");
                        scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
                    }
                }
            }
            break;


            case R.id.submitButton_partfirst:

                if (selectNextButton == 0) {
                    validateDetailsFirst();
                } else if (selectNextButton == 1) {

                    if (validationOTP()) {


                        otpVerifyRequest();

                    }
                } else if (selectNextButton == 3) {

                    backButtonMenu = 31;

                    if (validationDetailsGenderAddressEmailCityForm()) {

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_button_idDetails.setVisibility(View.GONE);
                        scrollView_uploading_form.setVisibility(View.GONE);

                        selectNextButton = 5;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Exécuter");
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                        customeLayout_subscriberFormImage(); // 2652

                    }
                } else if (selectNextButton == 4) {


                    scrollView_PersonalDetails_form.setVisibility(View.GONE);
                    scrollView_button_idDetails.setVisibility(View.GONE);
                    scrollView_uploading_form.setVisibility(View.GONE);

                    selectNextButton = 5;
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("Exécuter");
                    scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                } else if (selectNextButton == 5)   // demo  mpin page  click listener
                {

                    if (validationMpin()) {

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_button_idDetails.setVisibility(View.GONE);
                        scrollView_uploading_form.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                        selectNextButton = 5;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Suivant");

                        // Toast.makeText(UpdateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();

                        if (imageDownload_signature_fromServer == null || imageDownload_signature_fromServer.equalsIgnoreCase("")) {

                            if (selectCameraGallery_subscriberSign == 0 && selectCameraGallery_subscriberSign == 0) {

                                Toast.makeText(UpdateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();
                            } else {
                                updateAccountRequest();
                            }
                        } else if (imageDownload_form_fromServer.equalsIgnoreCase(imageDownload_agentimage_fromServer)) {

                            updateAccountRequest();
                        } else {

                            updateAccountRequest();
                        }


                    }
                } else if (selectNextButton == 6) {  // demo  print page display

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // hide back button
                    mToolbar.setTitle("        " + "METTRE A JOUR LE COMPTE");
                    mToolbar.setSubtitle("          " + agentName);

                    // Toast.makeText(UpdateAccount.this, "Show Receipt Click ", Toast.LENGTH_SHORT).show();

                    try {
                        // showSuccessReceiptCreateAgentUpdateAccount(successReceiptString);


                        Bundle bundle = new Bundle();
                        bundle.putString("data", responseUpdateAccount);

                        String accountType = "UPDATE ACCOUNT";

                        mComponentInfo.getmSharedPreferences().edit().putString("data", responseUpdateAccount).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("ACCOUNTTYPE", accountType).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("CUSTOMERNAME", customerNameFromServer).commit();


                        Intent intent = new Intent(UpdateAccountNewFrench.this, SucessReceiptCreateAgentNewUpdateNew.class);
                        intent.putExtra("data", responseUpdateAccount);
                        intent.putExtra("ACCOUNTTYPE", accountType);
                        intent.putExtra("CUSTOMERNAME", customerNameFromServer);
                        startActivity(intent);
                        UpdateAccountNewFrench.this.finish();

                    } catch (Exception e) {
                        System.out.println("eroor----------------" + e);
                        e.printStackTrace();

                    }


                } else if (selectNextButton == 10) {

                    Intent intent = new Intent(UpdateAccountNewFrench.this, AccountsMenu.class);
                    startActivity(intent);
                    UpdateAccountNewFrench.this.finish();

                } else if (selectNextButton == 11) // click mpin page if both image and data update
                {
                    if (validationMpin()) {

                        if (selectCameraGallery_subscriberSign == 0 && selectCameraGallery_subscriberSign == 0) {
                            Toast.makeText(UpdateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();

                        } else {

                            updateAccountRequest(); // demo if image and data update

                            //  uploadImageRequest_signatuere();
                        }
                    }
                } else if (selectNextButton == 15) // mpin click page // only data Update
                {
                    if (validationMpin()) {

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_button_idDetails.setVisibility(View.GONE);
                        scrollView_uploading_form.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Exécuter");
                        noUpdateAnyData = 2;
                        selectNextButton = 11;


                        if (selectCameraGallery_subscriberSign == 0) {
                            Toast.makeText(UpdateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();

                        } else {
                            updateAccountRequest();
                        }

                    }
                }


                if (selectNextButton == 17) {
                    authorizeRequest();
                }
                if (selectNextButton == 18) {
                    if (validationOTP()) {
                        otpVerifyRequest();
                    }
                } else if (selectNextButton == 19) // click mpin page if both image and data update
                {
                    if (selectCameraGallery_profilePic == 0 && selectCameraGallery_idFront == 0 && selectCameraGallery_idback == 0 && selectCameraGallery_billHome == 0) {

                        Toast.makeText(UpdateAccountNewFrench.this, "S'il vous plaît télécharger la galerie photo ou prendre la caméra", Toast.LENGTH_SHORT).show();

                    } else {

                        // Toast.makeText(UpdateAccountNewFrench.this, "19", Toast.LENGTH_LONG).show();

                    }
                }


                break;
        }
    }

    private boolean checkPermission()

    {

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return true;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)


            {
                return true;
            }

           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                } else {
                    // if click check box do not show again click   click denny then else condition
                    Toast.makeText(UpdateAccountNewFrench.this, "S il vous plait aller à l application de configuration mobile et autoriser toutes les autorisations", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_READ_PERMISSION);

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(UpdateAccountNewFrench.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
        return false;
    }


    private void uploadImageRequest_signatuere() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_signatuere();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 187).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_signatuere() {
        String jsonString = "";
        String finalDataJsonString = "";
        try {

            JSONObject countryObj = new JSONObject();

            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", "");
            countryObj.put("signature", cameraStringMpinPage);
            countryObj.put("idfrontimage", "");
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    public void validateDetailsFirst() {
        if (validateDetailsPartFirst()) {

            AgentIdentity();

        }
    }

    private boolean validationMpin() {
        boolean ret = false;

        mpinString = mpinEditText.getText().toString().trim();
        if (checkBox_termendCondition.isChecked()) {

            if (mpinString.length() == 4) {
                ret = true;
            } else {
                Toast.makeText(UpdateAccountNewFrench.this, "Saisir le code secret à 4 chiffres", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "J accepte les termes et conditions", Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    private boolean validateDetailsThirdForm() {
        boolean ret = false;
        spinnerCountryCodeString = spinnerCountry.getSelectedItem().toString();

        countryString = countryEditText.getText().toString().trim();
        spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
        accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];
        return ret;
    }


    private boolean validateDetailsPartFirst() {
        boolean ret = false;

        accountNumber = accountNumberEditText.getText().toString().trim();


        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();

            //     if (transferBasisposition != 0) {
            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileCashIn_french), lengthToCheck + 1 + "");

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
                        Toast.makeText(UpdateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


                ret = true;

                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];


            } else {
                Toast.makeText(UpdateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();

        }


        return ret;


    }


    private void AgentIdentity() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateDataAgentIdentity();


            // new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, requestData, "getAgentIdentity", 197).start();

            vollyRequestApi_serverTask("getAgentIdentity", requestData, 197);

        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void updateAccountRequest() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = updateAccountParsingData();


            //  new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, requestData, "updateAccount", 201).start();

            vollyRequestApi_serverTask("updateAccount", requestData, 201);


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    // {"agentCode":"237931349121","customerid":"ASDFSF65120320","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"CREATEAGENT"}


    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            String countryCodePrefixString = getCountryPrefixCode();

            //  accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", accountNumber);            // verify check account number
            countryObj.put("transtype", "UPDATEACCOUNT");
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


    private String updateAccountParsingData() {
        String jsonString = "";

       /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);*/

        try {

            JSONObject countryObj = new JSONObject();

            // countryObj.put("country", countryCodeArray[getCountrySelection()]);

            countryObj.put("agentCode", agentCode);
            countryObj.put("gender", genderCodeString);
            countryObj.put("language", LanguageString);

            secondMobileno_fromServerString = getCountryPrefixCode() + secondMobileno_fromServerString;
            fixMobileno_fromServerString = getCountryPrefixCode() + fixMobileno_fromServerString;

            countryObj.put("secondmobphoneno", secondMobileno_fromServerString);

            countryObj.put("fixphoneno", fixMobileno_fromServerString);
            countryObj.put("city", "");
            countryObj.put("email", emailStringFromServer); // email not in Create Account UI
            countryObj.put("address", address_fromServerString);

            idProofIssuDateString = idProofIssuDateString + " 00:00:00";
            final_dateOfBirthString = final_dateOfBirthString + " 23:59:59";
            countryObj.put("dob", final_dateOfBirthString);

            countryObj.put("birthplace", birthPlace_fromServerString);
            //  countryObj.put("nationality", nationality_fromServerString);
            countryObj.put("nationality", "");      // null
            countryObj.put("residencearea", residenceArea_fromServerString);
            countryObj.put("comments", "");

            //   accountNumber = getCountryPrefixCode() + accountNumber;


            countryObj.put("source", accountNumber);

            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("requestcts", "");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);



            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private String getOrderTransferApprovalDetailsGenerate() {
        String jsonString = "";

        try {


           /* SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
            mpinString = prefs.getString("MPIN", null);
           */
            String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private boolean validationOTP() {
        boolean ret = false;
        otpString = otp_editText.getText().toString().trim();

        if (otpString.length() >= 4) {
            ret = true;
        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Code de Sécurité 06 chiffres", Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {


            if (requestNo == 197) {


                try {


                    number_textview_iddetails.setText(accountNumber); /////
                    number_textview_mpinpage.setText(accountNumber);
                    number_textview_uploding.setText(accountNumber);

                    responseArrayAgentIdentity = generalResponseModel.getUserDefinedString().split("\\|");


                    gender_fromServerString = responseArrayAgentIdentity[8];        //  // temporary  comment  selection
                    profession_fromServerString = responseArrayAgentIdentity[1];
                    idProofIssuplace_fromServer = responseArrayAgentIdentity[18];
                    idProofIssuDate_fromServer = responseArrayAgentIdentity[10];

                    idProofNumber_fromServer = responseArrayAgentIdentity[9];
                    idProofType_fromServer = responseArrayAgentIdentity[12];


                    genderArray = getResources().getStringArray(R.array.genderDetailsType_french);
                    genderArray[0] = "Sexe";
                    genderCodeArray = getResources().getStringArray(R.array.genderTypeCode);


                    if (gender_fromServerString.equalsIgnoreCase("M")) {
                        spinnerGenderDetails.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray));
                        spinnerGenderDetails.setOnItemSelectedListener(this);
                        spinnerGenderDetails.setSelection(1);
                    } else if (gender_fromServerString.equalsIgnoreCase("F")) {
                        spinnerGenderDetails.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray));
                        spinnerGenderDetails.setOnItemSelectedListener(this);
                        spinnerGenderDetails.setSelection(2);
                    }

                    language_fromServerString = responseArrayAgentIdentity[20];     // temporary  comment  selection

                    languageSelectDetails = getResources().getStringArray(R.array.languageSelectDetails);
                    languageSelectCode = getResources().getStringArray(R.array.languageSelectCode);

                    if (language_fromServerString.equalsIgnoreCase("EN")) {
                        spinner_LanguageType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languageSelectDetails));
                        spinner_LanguageType.setOnItemSelectedListener(this);
                        spinner_LanguageType.setSelection(2);
                    } else if (language_fromServerString.equalsIgnoreCase("FR")) {
                        spinner_LanguageType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languageSelectDetails));
                        spinner_LanguageType.setOnItemSelectedListener(this);
                        spinner_LanguageType.setSelection(1);
                    }

                    nationality_fromServerString = responseArrayAgentIdentity[13];  // selection


                    String temp_secondMobileno_fromServerString = responseArrayAgentIdentity[21];
                    String temp_fixMobileno_fromServerString = responseArrayAgentIdentity[11];


                    if (responseArrayAgentIdentity[21].contains("237") && responseArrayAgentIdentity[11].contains("237")) {

                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'

                    } else if (responseArrayAgentIdentity[21].contains("241") && responseArrayAgentIdentity[11].contains("241")) {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'

                    } else if (responseArrayAgentIdentity[21].contains("242") && responseArrayAgentIdentity[11].contains("242")) {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'


                    } else if (responseArrayAgentIdentity[21].contains("235") && responseArrayAgentIdentity[11].contains("235")) {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'

                    } else if (responseArrayAgentIdentity[21].contains("236") && responseArrayAgentIdentity[11].contains("236")) {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                    } else if (responseArrayAgentIdentity[21].contains("243") && responseArrayAgentIdentity[11].contains("243")) {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString.substring(3, temp_secondMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString.substring(3, temp_fixMobileno_fromServerString.length() - 0); //strNew is 'Hello World'
                    } else {
                        secondMobileno_fromServerString = temp_secondMobileno_fromServerString;
                        fixMobileno_fromServerString = temp_fixMobileno_fromServerString;
                    }


                    temp_date_of_birth_fromServerString = responseArrayAgentIdentity[6];    //   13/09/2018 16:32:03 coming from server
                    birthPlace_fromServerString = responseArrayAgentIdentity[17];

                    residenceArea_fromServerString = responseArrayAgentIdentity[19];

                    secondMobileNumberEditText.setText(secondMobileno_fromServerString);
                    fixednumber_edittext.setText(fixMobileno_fromServerString);


                  //  temp_date_of_birth_fromServerString="1989/11/21";

                    if (temp_date_of_birth_fromServerString.contains("/")) {
                        temp_date_of_birth_fromServerString = temp_date_of_birth_fromServerString.replace("/", "-"); // coming frm server

                        String[] dateOFBirth_array=temp_date_of_birth_fromServerString.split("\\-");   //  2013 - 11 21

                        String day="",month="",year="";

                        String[] temDay=dateOFBirth_array[2].split("\\ "); //split space
                        day=temDay[0];

                        month=dateOFBirth_array[1];
                        year=dateOFBirth_array[0];


                        final_dateOfBirthString=day+"-"+month+"-"+year;


                    }

                    else {
                        String[] dateOFBirth_array=temp_date_of_birth_fromServerString.split("\\-");   //  2013 - 11 21

                        String day="",month="",year="";

                        String[] temDay=dateOFBirth_array[2].split("\\ "); //split space
                        day=temDay[0];

                        month=dateOFBirth_array[1];
                        year=dateOFBirth_array[0];


                        final_dateOfBirthString=day+"-"+month+"-"+year;

                    }


                    dateOfBirth_EditText_manullay.setText(final_dateOfBirthString);
                    birthpalceEditText.setText(birthPlace_fromServerString);

                    residenceAreaEditText.setText(residenceArea_fromServerString);

                    //  cityStateList();

                    profileView();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            } else if (requestNo == 196) {

                keyboardSelection = 1;

                String[] responseArray = generalResponseModel.getUserDefinedString().split("\\|");
                customerNameFromServer = responseArray[2];

                emailStringFromServer = responseArray[23];
                emailEditText.setText(emailStringFromServer);  // coming from server emailStringFromServer
                address_fromServerString = responseArray[9];
                addressEditText.setText(address_fromServerString);
                city_fromServerString = responseArray[10];


                // responseArrayAgentIdentity[2] = "SMS";   // temporary data set
                //  responseArrayAgentIdentity[25] = "";   // temporary data set
                // now remove Comments check remobve
                //   kycsamebranch is "null" or ""     //// no pop up Direct Next page
                //   kycsamebranch is  true //  only show   Otp page
                //   kycsamebranch "false" && agentTpe =="Sub"   // Then Open Popup Auth


                //  responseArrayAgentIdentity[26]="";

                if (responseArrayAgentIdentity[26].equalsIgnoreCase("") || responseArrayAgentIdentity[26].equalsIgnoreCase("null")) {
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.GONE);

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
                    selectNextButton = 2;

                    imageDownload();

                } else if (responseArrayAgentIdentity[26].equalsIgnoreCase("false") || responseArrayAgentIdentity[26].equalsIgnoreCase("F") && responseArrayAgentIdentity[25].equalsIgnoreCase("SUB")) {
                    customeAlertDialog_authorize_otp();
                } else if (responseArrayAgentIdentity[26].equalsIgnoreCase("true") || responseArrayAgentIdentity[26].equalsIgnoreCase("T")) {
                    otpGenerateRequest();
                } else {

                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.GONE);

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
                    selectNextButton = 2;

                    imageDownload();

                    // imageDownload();
                }
            } else if (requestNo == 103)  // if  OTP response is Success
            {
                scrollView_otp_page.setVisibility(View.GONE);
                selectNextButton = 1;
                imageDownload();

            } else if (requestNo == 203) {
                isTimeUp = false;
                authorizeRequest();

            } else if (requestNo == 204) {
                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);

                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                selectNextButton = 1;
                submitButton_partfirst.setText("Suivant");

                imageDownload();
            } else if (requestNo == 205) {
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                // scrollView_profilePiture_sign_partOne.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_otp_page.setVisibility(View.VISIBLE);
                selectNextButton = 18;

            } else if (requestNo == 201) {


                responseUpdateAccount = generalResponseModel.getUserDefinedString();
                System.out.println(responseUpdateAccount);

                System.out.println(noUpdateAnyData);

                // if no any update

                if (selectCameraGallery_profilePic == 0 && selectCameraGallery_idFront == 0 && selectCameraGallery_idback == 0 && selectCameraGallery_billHome == 0 && noUpdateAnyData == 0) {
                    scrollView_PersonalDetails_form.setVisibility(View.GONE);
                    scrollView_button_idDetails.setVisibility(View.GONE);

                    selectNextButton = 4;

                   /* submitButton_partfirst.setText(getString(R.string.submit));

                    number_textview_uploding.setText(accountNumber);

                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    scrollView_uploading_form.setVisibility(View.GONE);
                    scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);*/

                    Toast.makeText(UpdateAccountNewFrench.this, "" + "Aucune mise à jour d un champ ou d une image", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateAccountNewFrench.this, AccountsMenu.class);
                    startActivity(intent);
                    UpdateAccountNewFrench.this.finish();
                }

                ////////////////////////////////////////////////    only data update   ///////////////////////////////////////////////////////////////

                else if (selectCameraGallery_profilePic == 0 && selectCameraGallery_idFront == 0 && selectCameraGallery_idback == 0 && selectCameraGallery_billHome == 0 && noUpdateAnyData == 1) {


                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // hide back button
                    mToolbar.setTitle("        " + "METTRE A JOUR LE COMPTE");
                    mToolbar.setSubtitle("          " + agentName);


                    scrollView_PersonalDetails_form.setVisibility(View.GONE);
                    scrollView_button_idDetails.setVisibility(View.GONE);
                    selectNextButton = 6;

                    number_textview_uploding.setText(accountNumber);
                    scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("Montrer le recu");
                    scrollView_uploading_form.setVisibility(View.VISIBLE);

                    uploadImageRequest_AllImage();

                }

                ////////////////////////////////////////////////    only data update Server Response     ///////////////////////////////////////////////////////////////

                else if (selectNextButton == 16 && noUpdateAnyData == 2) {

                    String[] responseArray2 = generalResponseModel.getUserDefinedString().split("\\|");
                    Toast.makeText(UpdateAccountNewFrench.this, "Mise à jour effectuée avec succès", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateAccountNewFrench.this, AccountsMenu.class);
                    startActivity(intent);
                    UpdateAccountNewFrench.this.finish();
                }


                ////////////////////////////   Both  Image and Data update    //////////////////////////////////////////////////////////////////////////////

                else if ((selectCameraGallery_profilePic == 1 || selectCameraGallery_idFront == 1 || selectCameraGallery_idback == 1 || selectCameraGallery_billHome == 1) && noUpdateAnyData == 1) {


                    String dataString = profilePicSelectString + "|" + idFrontPicSelectString + "|" + idBackPicSelectString + "|" + billHomePicSelectString + "|" + signaturePicSelectString;
                    System.out.println(dataString);
                    System.out.println(dataString);


                    String[] tempData = dataString.split("\\|");

                    profilePic_temp = tempData[0];
                    idFront_temp = tempData[1];
                    idBack_temp = tempData[2];
                    billHome_temp = tempData[3];
                    signature_temp = tempData[4];

                    if (profilePic_temp.equalsIgnoreCase("null") || idFront_temp.equalsIgnoreCase("null") || idBack_temp.equalsIgnoreCase("null")
                            || billHome_temp.equalsIgnoreCase("null") || signature_temp.equalsIgnoreCase("null")) {


                        profilePic_new = profilePic_temp.replace("null", "");
                        idFront_new = idFront_temp.replace("null", "");
                        idBack_new = idBack_temp.replace("null", "");
                        billHome_new = billHome_temp.replace("null", "");
                        signature_new = signature_temp.replace("null", "");

                        System.out.println(profilePic_new);
                        System.out.println(idFront_new);
                        System.out.println(idBack_new);
                        System.out.println(billHome_new);
                        System.out.println(signature_new);

                    } else {

                        // all image chnage Request
                        System.out.println(profilePic_temp);
                        System.out.println(idFront_temp);
                        System.out.println(idBack_temp);
                        System.out.println(billHome_temp);
                        System.out.println(imageDownload_form_fromServer);
                        System.out.println(signature_temp);

                    }

                    uploadImageRequest_AllImage();


                }

            } else if (requestNo == 212) {
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                selectNextButton = 6;

                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Montrer le recu");
                scrollView_uploading_form.setVisibility(View.VISIBLE);


                if (profilePic_temp == null) {
                    linearLayout_green_imageview_agentprofile.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_agentprofile.setVisibility(View.VISIBLE);
                    green_imageview_agentprofile.setVisibility(View.VISIBLE);
                }

                if (idFront_temp == null) {
                    linearLayout_green_imageview_idfront.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_idfront.setVisibility(View.VISIBLE);
                    green_imageview_idfront.setVisibility(View.VISIBLE);
                }

                if (idBack_temp == null) {
                    linearLayout_green_imageview_idback.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_idback.setVisibility(View.VISIBLE);
                    green_imageview_idback.setVisibility(View.VISIBLE);
                }

                if (billHome_temp == null) {
                    linearLayout_green_imageview_billlocation.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_billlocation.setVisibility(View.VISIBLE);
                    green_imageview_billlocation.setVisibility(View.VISIBLE);
                }


                if (signature_temp == null && signature_new == null) {
                    linearLayout_green_imageview_subscriberSign.setVisibility(View.GONE);
                } else if (imageDownload_signature_fromServer.equalsIgnoreCase("")) {
                    linearLayout_green_imageview_subscriberSign.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_subscriberSign.setVisibility(View.VISIBLE);
                    green_imageview_subscriberSign.setVisibility(View.VISIBLE);
                }

                // create green tic in xml form remaning

                if (imageDownload_form_fromServer == null || imageDownload_form_fromServer.equalsIgnoreCase("")) {

                    linearLayout_green_imageview_form.setVisibility(View.GONE);
                } else {
                    linearLayout_green_imageview_form.setVisibility(View.VISIBLE);
                    green_imageview_form.setVisibility(View.VISIBLE);
                }

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // hide back button
                mToolbar.setTitle("        " + "METTRE A JOUR LE COMPTE");
                mToolbar.setSubtitle("          " + agentName);


            } else if (requestNo == 202) {

                keyboardSelection = 2;

                try {


                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.GONE);
                    scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
                    scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);

                    String[] responseimageArray = generalResponseModel.getUserDefinedString().split("\\|");


                    imageDownload_agentimage_fromServer = responseimageArray[1];
                    imageDownload_idfront_fromServer = responseimageArray[2];
                    imageDownload_idback_fromServer = responseimageArray[3];
                    imageDownload_billlocation_fromServer = responseimageArray[4];
                    imageDownload_signature_fromServer = responseimageArray[5];
                    imageDownload_form_fromServer = responseimageArray[6];


                    ////////////////////////////////////////////////////////////

                    if (imageDownload_agentimage_fromServer.equalsIgnoreCase("") || imageDownload_agentimage_fromServer.equalsIgnoreCase("null")) {
                        imageview_selfportrait.setBackgroundResource(R.drawable.arrowred);
                    } else {
                        byte[] imageBytes1 = Base64.decode(imageDownload_agentimage_fromServer, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_selfportrait.setImageBitmap(decodedImage);
                    }


                    if (imageDownload_idfront_fromServer.equalsIgnoreCase("") || imageDownload_idfront_fromServer.equalsIgnoreCase("null")) {
                        imageview_preview_idfront.setBackgroundResource(R.drawable.arrowred);
                    } else {

                        byte[] imageBytes1 = Base64.decode(imageDownload_idfront_fromServer, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_preview_idfront.setImageBitmap(decodedImage);
                    }


                    if (imageDownload_idback_fromServer.equalsIgnoreCase("") || imageDownload_idback_fromServer.equalsIgnoreCase("null")) {
                        imageview_preview_idback.setBackgroundResource(R.drawable.arrowred);
                    } else {
                        byte[] imageBytes1 = Base64.decode(imageDownload_idback_fromServer, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_preview_idback.setImageBitmap(decodedImage);
                    }


                    if (imageDownload_billlocation_fromServer.equalsIgnoreCase("") || imageDownload_billlocation_fromServer.equalsIgnoreCase("null")) {
                        imageview_preview_billlocation.setBackgroundResource(R.drawable.arrowred);
                    } else {
                        byte[] imageBytes1 = Base64.decode(imageDownload_billlocation_fromServer, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_preview_billlocation.setImageBitmap(decodedImage);
                    }


                    if (imageDownload_signature_fromServer.equalsIgnoreCase("") || imageDownload_signature_fromServer.equalsIgnoreCase("null")) {
                        // imageview_preview_billlocation.setBackgroundResource(R.drawable.arrowred);

                    } else {
                        byte[] imageBytes1 = Base64.decode(imageDownload_signature_fromServer, Base64.DEFAULT);
                        bitmapSignature = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_preview_mpinPage.setImageBitmap(bitmapSignature);
                    }


                    if (imageDownload_form_fromServer.equalsIgnoreCase("") || imageDownload_form_fromServer.equalsIgnoreCase("null")) {
                        // imageview_preview_billlocation.setBackgroundResource(R.drawable.arrowred);

                    } else {

                        byte[] imageBytes1 = Base64.decode(imageDownload_form_fromServer, Base64.DEFAULT);
                        bitmapImageForm = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                        imageview_subscriberForm.setImageBitmap(bitmapImageForm);
                        noSelectForm = 2;

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                ////////////////////////////////////////////////////////////


            } else if (requestNo == 194) {


                String cityStateDetails = generalResponseModel.getUserDefinedCityCreateAgent();

                if (cityStateDetails.equalsIgnoreCase("$ CITY  $CITY Code "))   // if country List Is Null
                {
                    Toast.makeText(UpdateAccountNewFrench.this, "" + "Le code du pays est vide", Toast.LENGTH_SHORT).show();

                } else {

                    String[] cityListDetails = cityStateDetails.split("\\$");

                    String cityDetails = cityListDetails[1];
                    cityDetailsArray = cityDetails.split("\\|");

                    String cityCode = cityListDetails[2];
                    cityCodeArray = cityCode.split("\\|");


                    spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
                    spinnerCity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityDetailsArray));
                    spinnerCity.setOnItemSelectedListener(this);

                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    selectNextButton = 1;
                    submitButton_partfirst.setText("Suivant");
                    scrollView_otp_page.setVisibility(View.VISIBLE);

                }

            }


        } else {

            if (requestNo == 203)   // initiate response
            {

                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);

                    //  scrollView_button_createAgent_firstPart.setVisibility(View.GONE);
                    //  submitButton_partfirst.setText(getString(R.string.next));

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("AUTORISER");
                    selectNextButton = 17;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(UpdateAccountNewFrench.this, R.anim.blink));

                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);


                    // Toast.makeText(ViewProfile.this, "Waiting time  1 to 10 Minute ", Toast.LENGTH_LONG).show();
                }
                else if (generalResponseModel.getResponseCode() == 404 || generalResponseModel.getResponseCode() == 405 || generalResponseModel.getResponseCode() == 231) { // from server

                    scrollView_createAgent_firstPart.setVisibility(View.GONE);

                    //  scrollView_button_createAgent_firstPart.setVisibility(View.GONE);
                    //  submitButton_partfirst.setText(getString(R.string.next));

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("AUTORISER");
                    selectNextButton = 17;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(UpdateAccountNewFrench.this, R.anim.blink));

                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);


                }
                else {

                    selectNextButton = 0;
                    scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                    scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                    scrollView_otp_page.setVisibility(View.GONE);

                    scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("Suivant");

                    Toast.makeText(UpdateAccountNewFrench.this, generalResponseModel.getUserDefinedString(), Toast.LENGTH_LONG).show();

                    keyboardSelection = 0;
                }


            } else if (requestNo == 202) {

                imageDownload_agentimage_fromServer = "";
                imageDownload_idfront_fromServer = "";
                imageDownload_idback_fromServer = "";
                imageDownload_billlocation_fromServer = "";    // if  Download source folder not exit


                imageview_selfportrait.setBackgroundResource(R.drawable.arrowred);
                imageview_preview_idfront.setBackgroundResource(R.drawable.arrowred);
                imageview_preview_idback.setBackgroundResource(R.drawable.arrowred);
                imageview_preview_billlocation.setBackgroundResource(R.drawable.arrowred);

                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText(getString(R.string.next));
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);

                keyboardSelection = 2;

            } else if (requestNo == 204)  //   Auth response
            {
                // scrollView_createAgent_firstPart.setVisibility(View.GONE);

                String[] authorizeResponse = generalResponseModel.getUserDefinedString().split("\\|");

                if (generalResponseModel.getResponseCode() == 234 || generalResponseModel.getResponseCode() == 0) {
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);

                    imageDownload();
                }

                else if (generalResponseModel.getResponseCode() == 19)
                {
                    Toast.makeText(UpdateAccountNewFrench.this, getString(R.string.request_cancel_by_customer), Toast.LENGTH_SHORT).show();
                    UpdateAccountNewFrench.this.finish();
                }
                else if (!isTimeUp) {


                    scrollView_createAgent_firstPart.setVisibility(View.GONE);

                    //  scrollView_button_createAgent_firstPart.setVisibility(View.GONE);
                    //  submitButton_partfirst.setText(getString(R.string.next));

                    scrollView_otp_page.setVisibility(View.GONE);
                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("AUTORISER");
                    selectNextButton = 17;
                    scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.VISIBLE);
                    textview_waitingForSubscriberAuthenticate.setAnimation(AnimationUtils.loadAnimation(UpdateAccountNewFrench.this, R.anim.blink));
                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);

                    authRequestDelayHandler.postDelayed(sendData_AuthRequestHandler_Runnable, 20000);
                } else {
                    // UpdateAccountNewEnglish.this.finish();;

                    // Toast.makeText(UpdateAccountNewEnglish.this, "Please retry after some time", Toast.LENGTH_LONG).show();   //

                    Toast.makeText(UpdateAccountNewFrench.this, getString(R.string.timeOutComplete), Toast.LENGTH_SHORT).show();
                    UpdateAccountNewFrench.this.finish();

                }


            } else {

                hideProgressDialog();
                Toast.makeText(UpdateAccountNewFrench.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                // Subscriber/Agent Not Found
            }
        }
    }

    private void uploadImageRequest_agentimage() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_agentimage();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 186).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    Handler authRequestDelayHandler = new Handler();


    private Runnable sendData_AuthRequestHandler_Runnable = new Runnable() {
        public void run() {
            try {
                //prepare and send the data here..

                timeCalculation = ++timeCalculation;


                authRequestDelayHandler.removeCallbacks(sendData_AuthRequestHandler_Runnable);
                isTimeUp = false;


                if (timeCalculation >= 30) {   // 10 Minute
                    isTimeUp = true;
                }
                authorizeRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private String generateUploadImage_agentimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", pictureChoosePortraitString);
            countryObj.put("signature", "");     // signature
            countryObj.put("idfrontimage", "");
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", "");   // bill image
            countryObj.put("formimage", "");   // form image
            countryObj.put("requestcts", "");   // form image


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }


    private String generateUploadImage_allImage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");


            if (profilePic_temp == null || idFront_temp == null || idBack_temp == null
                    || billHome_temp == null || signature_temp == null) {


                profilePic_new = pictureChoosePortraitString;
                idFront_new = pictureChooseIDFrontString;
                idBack_new = pictureChooseIdBackString;
                billHome_new = cameraBillString;
                signature_new = cameraStringMpinPage;


                countryObj.put("agentimage", profilePic_new);
                countryObj.put("idfrontimage", idFront_new);
                countryObj.put("idbackimage", idBack_new);
                countryObj.put("billimage", billHome_new);   // bill image
                countryObj.put("signature", signature_new);     // signature
                countryObj.put("formimage", imageDownload_form_fromServer);   // form image
            } else {

                profilePic_temp = pictureChoosePortraitString;
                idFront_temp = pictureChooseIDFrontString;
                idBack_temp = pictureChooseIdBackString;
                billHome_temp = cameraBillString;
                signature_temp = cameraStringMpinPage;


                countryObj.put("agentimage", profilePic_temp);
                countryObj.put("idfrontimage", idFront_temp);
                countryObj.put("idbackimage", idBack_temp);
                countryObj.put("billimage", billHome_temp);   // bill image
                countryObj.put("signature", signature_temp);     // signature

                imageDownload_signature_fromServer = "";  // if select first page image and signature not select then display signatre

                countryObj.put("formimage", imageDownload_form_fromServer);   // form image

            }


            countryObj.put("requestcts", "");   // form image


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_AllImage() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_allImage();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 212).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImageRequest_idfrontimage() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idfrontimage();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 188).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_idfrontimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", "");
            countryObj.put("signature", "");
            countryObj.put("idfrontimage", pictureChooseIDFrontString);
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_idbackimage() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idbackimage();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 189).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_idbackimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", "");
            countryObj.put("signature", "");
            countryObj.put("idfrontimage", "");
            countryObj.put("idbackimage", pictureChooseIdBackString);
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_billimage() {


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_billimage();

            new ServerTaskFTP(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "uploadimage", 190).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_billimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", "");
            countryObj.put("signature", "");
            countryObj.put("idfrontimage", "");
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", cameraBillString);
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");

            System.out.println(finalDataJsonString);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }


    private String generateCityData() {

        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", mComponentInfo.getmSharedPreferences().getString("agentCode", ""));

            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("countrycode", countryCodeArray[getCountrySelection()]);

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return jsonString;
    }

    public Bitmap loadBitmapFromView(View v) {

        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());


        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);

        return bitmap;

    }


    private void cityStateList() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateCityData();

            new ServerTask(mComponentInfo, UpdateAccountNewFrench.this, mHandler, requestData, "getStateListInJSON", 194).start();


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] data;

        //  data = accountsArrayDetailsList.get(position).toString().split("\\|");

        Toast.makeText(UpdateAccountNewFrench.this, "Listview Click Lis", Toast.LENGTH_SHORT).show();


        // showPreConfirmationPopup(data);


    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            UpdateAccountNewFrench.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(UpdateAccountNewFrench.this);
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

            // temporary comment 03 oct 2018

            System.out.println(selectNextButton); //2 31 0 4
            System.out.println(backButtonMenu);
            System.out.println(backButtonSelectTermEndCondition);
            System.out.println(humanSignature);

            if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1 && humanSignature == 4) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1 && humanSignature == 3) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                humanSignature = 4;

            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 3) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                humanSignature = 4;
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1 && humanSignature == 3) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1 && humanSignature == 2) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1 && humanSignature == 1) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 1) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 0) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 0) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 2 && backButtonMenu == 0 && backButtonSelectTermEndCondition == 0 && humanSignature == 0) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                submitButton_partfirst.setText("Suivant");
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 3 && backButtonMenu == 0 && backButtonSelectTermEndCondition == 0 && humanSignature == 1) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 15 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 15 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 1) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 11 && backButtonSelectTermEndCondition == 1) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_button_idDetails.setVisibility(View.GONE);
                scrollView_uploading_form.setVisibility(View.GONE);

                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Exécuter");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 0) {

                Intent intent = new Intent(UpdateAccountNewFrench.this, AccountsMenu.class);
                startActivity(intent);
                UpdateAccountNewFrench.this.finish();

            } else if (selectNextButton == 1 && backButtonMenu == 30) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

            } else if (selectNextButton == 11 && backButtonMenu == 31) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 1 && backButtonMenu == 31) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

            } else if (selectNextButton == 2 && backButtonMenu == 0) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

            }


            // else if (selectNextButton == 2 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature==4)
            else if (selectNextButton == 2 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 4) {

                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);

            } else if (backButtonMenu == 30) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                submitButton_partfirst.setText("Suivant");
                submitButton_partfirst.setVisibility(View.VISIBLE);
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }

           /* else if (selectNextButton == 1) {


                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);

            }*/

            /*else if (selectNextButton == 2) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);

                selectNextButton = 1;
                submitButton_partfirst.setText("Suivant");
                submitButton_partfirst.setVisibility(View.VISIBLE);
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }*/
            else if (selectNextButton == 3) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 4) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_uploading_form.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);


            } /*else if (selectNextButton == 5) {

                selectNextButton = 3;
                webview.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_uploading_form.setVisibility(View.GONE);
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

            }*/ else if (selectNextButton == 6) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 5;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);


            } else if (selectNextButton == 15) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 21) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                submitButton_partfirst.setText("Suivant");
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 11) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");

                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 18) {
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 0;
                submitButton_partfirst.setText("Suivant");
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);

                keyboardSelection = 0;

            } else if (selectNextButton == 1 && backButtonMenu == 0 && backButtonSelectTermEndCondition == 0 && humanSignature == 0) {

                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.GONE);
                //  submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.VISIBLE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 5 && backButtonMenu == 31 && backButtonSelectTermEndCondition == 0 && humanSignature == 4) {
                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_uploading_form.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);


            } else {


                scrollView_waitingtoSubscriberAuthenticate.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);

                submitButton_partfirst.setText("Suivant");
                scrollView_self_portrait_back_front_page.setVisibility(View.GONE);
                scrollView_button_selfportrait_idfront_idback.setVisibility(View.GONE);
                scrollView_otp_page.setVisibility(View.GONE);
                selectNextButton = 0;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
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
            // Toast.makeText(QrCodeGenerate.this, "valid email address",Toast.LENGTH_SHORT).show();

            return true;
        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez entrer un identifiant email valide", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }


    private void authorizeInitiateRequest() {

        backButtonMenu = 31;


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");

            // new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, authorizeInitiateparsingData(), "authorize", 203).start();

            vollyRequestApi_serverTask("authorize", authorizeInitiateparsingData(), 203);

        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_SHORT).show();
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


        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {

            showProgressDialog("Veuillez patienter");

            // new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, generateAuthorizeData(), "authorize", 204).start();

            vollyRequestApi_serverTask("authorize", generateAuthorizeData(), 204);


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_SHORT).show();
        }
    }

    void vollyRequestApi_serverTask(String apiName, final String body, final int requestCode) {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTask.baseUrl + apiName, new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(UpdateAccountNewFrench.this, mComponentInfo, UpdateAccountNewFrench.this, requestCode, response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez réessayer plus tard", Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        } catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez réessayer plus tard", Toast.LENGTH_LONG).show();

        }

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


    private void profileView() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateProfileView();

            // new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, requestData, "getViewProfileInJSON", 196).start();

            vollyRequestApi_serverTask("getViewProfileInJSON", requestData, 196);


        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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

    private void otpGenerateRequest() {

        if (new InternetCheck().isConnected(UpdateAccountNewFrench.this)) {
            showProgressDialog("Génération d otp");

            //  new ServerTask(mComponentInfo, UpdateAccountNew.this, mHandler, otpGenerate(), "getOTPInJSON", 205).start();

            vollyRequestApi_serverTask("getOTPInJSON", otpGenerate(), 205);

        } else {
            Toast.makeText(UpdateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_SHORT).show();
        }
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
            countryObj.put("transtype", "UPDATEACCOUNT");
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

    //---------------------------------------------   Image Human Signature  --------------------------------------------------------------

    public boolean addJpgSignatureToGallery(Bitmap signature, String selectionType) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            signSignatureHuman(photo, selectionType);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    void signSignatureHuman(File photo, String selectionType) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photo);


        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        System.out.println(selectedImage);


        ColorMatrix matrix = new ColorMatrix();   // color to black in white
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        //  imageview_preview_mpinPage.setColorFilter(filter);

        Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
        selectedImage = resizeBmp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] imageBytes = baos.toByteArray();

        // ---------------------
        if (selectionType.equalsIgnoreCase("BillElectricity")) {
            cameraBillString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_billlocation.setImageBitmap(selectedImage);
        } else if (selectionType.equalsIgnoreCase("MPINPAGE")) {
            cameraStringMpinPage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_mpinPage.setImageBitmap(selectedImage);
        }


        // ---------------------

        mediaScanIntent.setData(uri);
        UpdateAccountNewFrench.this.sendBroadcast(mediaScanIntent);
    }


}
