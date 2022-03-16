package agent.create_account;

import android.Manifest;
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
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.app.AlertDialog;
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
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

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


public class CreateAccountNewFrench extends AppCompatActivity implements

        AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener,
        View.OnTouchListener,
        View.OnClickListener,
        ServerResponseParseCompletedNotifier,
        TextView.OnEditorActionListener,
        DateSetNotifier {


    enum CREATE_ACCOUNT_APIS {
        API_CREATE_ACCOUNT,
        API_IMAGE_UPLOAD_PERSONAL,
        API_IMAGE_UPLOAD_ID_FRONT,
        API_IMAGE_UPLOAD_ID_BACK,
        API_IMAGE_UPLOAD_FORM,
        API_IMAGE_UPLOAD_HOMELOCATION_BILLCOPY,
        API_NONE
    }

    ;

    private static final int CAMERA_READ_PERMISSION = 3;
    private static final int REQUEST_WIFI_PHONE = 1;
    private static final int REQUEST_SMS_PHONE = 2;
    private static final int REQUEST_READ_PHONE = 0;

    android.app.AlertDialog.Builder alertDialogPopup;
    android.app.AlertDialog alertDialog;
    Intent intent;
    String fullScreenInd;
    WebView webview_new;

    CheckBox checkBox_termendCondition;

    Bitmap signatureBitmapBillElectricity,signatureBitmapDrawHome,signatureBitmapMpin;
    Bitmap selectedImage;
    CREATE_ACCOUNT_APIS create_account_apis = CREATE_ACCOUNT_APIS.API_NONE;
    int noSelectForm = 0, tempGallery = 0, tempCamera = 0, formDisplay = 0;
    private Hashtable<CREATE_ACCOUNT_APIS, Integer> retryCount_List;
    Bitmap bitmapCameraCustomer;
    boolean isImageFitToScreen;
    LinearLayout linearlayout_imageview_fullScreen,scrollView_mpin_Page_linearLayout;
    int humanSignature=0,selectNextButton = 0,selectSignaurepic=0,noSelectElectricitySignature=0,noSelectBillHomeSignature=0,backButtonSignMpin=0,noSelectMpinSignature=0,backButtonBillHomeElectricity=0;
    Bitmap completeImage;
    private static int SELECT_PICTURE_CUSTOMER_UPLOAD = 100, CAMERA_REQUEST_CUSTOMERPICTURE = 101, CAMERA_REQUEST_FRONT_ID_DOCUMENT = 102, SELECT_PICTURE_FRONT_ID_DOCUMENT = 103, SELECT_PICTURE_BACK_ID_DOCUMENT = 104, CAMERA_REQUEST_BACK_ID_DOCUMENT = 105, PICTURE_SELECT_ELECTRICITY_BILL = 106, CAMERA_REQUEST_ELECTRICITY_BILL = 107, PICTURE_SELECT_DRAW_HOME = 108, CAMERA_REQUEST_DRAW_HOME = 109, PICTURE_SELECT_MPIN_PAGE = 110, CAMERA_REQUEST_MPIN_PAGE = 111;
    int backButtonSelectTermEndCondition = 0, chooseOneSelect = 0, selectCameraGalleryMpinSubscriberSign = 0;
    ;
    PhotoView photoView;

    int billElectricityBackButton=0,selectCamera = 0, selectCameraFullScreen_customer = 0, selectGalleryFullScreen_customer = 0, selectCameraFullScreen_customer_2 = 0, selectGalleryFullScreen_customer_2 = 0, selectCameraFullScree = 0, selectGallery = 0, selectCameraElectricity = 0, selectGalleryElectricitybill = 0, selectCameraFront = 0, selectGalleryFront = 0, selectCameraBack = 0, selectGalleryBack = 0, selectGalleryDrawaHome = 0, selectCameraDrawaHome = 0;
    InputStream imageStream, imageStreamCamera;
    ArrayList accountsArrayDetailsList;
    Uri uriMpinPage, uriDrawHome, uidElectricity, uidString, selectedImageUriProfilPic, uriIddocumnetFront;
    boolean boolselectUploadSignaturePic = false;
    String secondMobileTempString, fixMobiletempNumber, subscriberformString, cityString, countryCodePrefixString, agentName_reload, errorMsgToDisplay = "";
    boolean boolselectUploadPic = false;
    ListView listView;
    String[] cityCodeArray, cityDetailsArray, languageSelectDetails, cityArray, languageSelectCode, bankSelectionArray, transferTagArray, idProofArray, idProofCodeArray, accountTypeArray, planAccountNameLabel, planCodeArray, tempState, professionArray, genderArray, genderCodeArray, nationalityArray, nationalityCodeArray;
    String[] plancodeArray, agenttypeArray, profilenameArray, fixedBACodeArray, codeProfileArray;
    Toolbar mToolbar;
    ImageView imageview_subscriberForm, green_imageview_form, imageview_fullScreen, green_imageview_personalImage, green_imageview_idfront, green_imageview_idback, green_imageview_billlocation, green_imageview_signature, camera_mpin_imageview, upload_mpin_imageview, imageview_preview_mpinPage, camera_drawaHome_imageview, upload_DrawHome_imageview, imageview_priview_front_iddocument, imageview_preview_drawHome, imageview_bill_electricityBill, imageview_preview_back_iddocument, camera_back_iddocument_imageview, upload_back_iddocument_imageview, upload_pic_electricityBill_imageview, upload_front_iddocument_imageview, camera_billElectricity_imageview, camera_front_iddocument_imageview, upload_imageView, picturePreview_imageview, camera_imageview;
    Button signaturePad_saveButton_drawHome,signaturePad_clearButton_drawHome,signaturePad_clearButton_mpinPage,signaturePad_saveButton_billElectricity,signaturePad_clearButton_billElectricity,signaturePad_saveButton_mpin,idproof_calender_button, idproofExpiredDate_calender_button, dateOfBirth_calender_button;
    ComponentMd5SharedPre mComponentInfo;
    String branchCodeString, parentfixedBAString, successReceiptString, uploadCameraStringSignature, commentString, attachBranchNameString, idproofNameString, uploadGalleryStringElectricity, uploadCameraStringCustomerPicture, uploadGaleryIddocumentFrontString, cameraGalleryIDDocBackString, agentName, customerNameString, customerIdString, spinnerCountryCodeString, loyalityCasrdNoString, spinnerIdProofTypeTemp, subscriberNameString, emailString, agentCode, idProofPlaceString, birthpalceString, agentTypeString, secondMobileNumberString, fixPhonenoString, professionString, genderString, idproofDueDate, nationalityString, planNameString, residenceAreaString, spinnerCountryString, transferBasisString, accountNumber, spinnerStateString, countryString, addressString, confirmationCodeString, spinnerIdProofTypeString, LanguageString, spinnerAccountToDebitString, mpinString, countrySelectionString = "", accountCodeString,
            idProofCodeString, genderCodeString, professionCodeString, nationalityCodeString;

    View viewForContainer;
    Button idproofIssueDate_calender_button, submitButton_partfirst;
    boolean isReview, isServerOperationInProcess, isOtherProfession;
    Dialog successDialog;
    int lengthToCheck, transferCase, accToAccLevel = 0, idProofSelectedPosition, genderSelectedPosition, professionSelectedPosition, languageSelectedPosition, citySelectedPosition, nationalitySelectedPosition, countrySelectedPosition;
    private Spinner spinnerCountryNationality, spinnerAttachedBranchName, spinnerCity, spinnerProfession, spinnerPlanAccountProfile, spinnerGenderDetails, spinnerCountry, spinnerAccountToDebit, transferBasisSpinner, spinnerIdProofType, spinner_LanguageType;
    private ScrollView scrollView_signatureHuman_drawHome,scrollView_signatureHuman_electricityBill,scrollView_signatureHuman_mpin_page, scrollView_uploading_form_showreceipt, scrollView_electricity_bill, scrollView_createAgent_thirdPage_form, scrollView_addressDetails_form, scrollView_idDocumentPicture_Page, scrollView_button_idDocumnetpicure, scrollView_customerPicture, scrollView_mpin_Page, scrollView_idDetails_form, scrollView_button_firstPart, scrollView_createAgent_firstPart, scrollView_PersonalDetails_form, scrollView_createAgent_confirmationPage;
    private AutoCompleteTextView number_textview_uploding, number_textview_mpinpage, idproofName_edittext, idissuePlace_edittext, otherProfessionEditText, idproofEditText_datePicker_manually, number_textview_iddetails, number_textview_personalDetails, loyalityCardNumberEditTex, idProofPlaceEditText, birthpalceEditText, agentTypeEditText, secondMobileNumberEditText, fixednumber_edittext,
            residenceAreaEditText, emailEditText, stateEditText, countryEditText, customerName_edittext, customerIdNumber_edittext, subscriberNameEditText, addressEditText, mpinEditText, accountNumberEditText;
    private TextView address_reviewpage, idProofNumberTextView, accountNumberTextViewReview, recipientCountryTxtView_Review, agentCodeTextView, accountNameTextView_Review, cityTextView_Review, languageTextView_Review, idproofTypeTextView_Review, countryTextView_Review, addressTextView_Review, idproofTxtView_Review, transferBasisTxtView_Review, recipientNameNoTxtView_Review, titleTextView, payerAccountTypeTxtView_Review;
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
                DataParserThread thread = new DataParserThread(CreateAccountNewFrench.this, mComponentInfo, CreateAccountNewFrench.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;
    String[] attachBranchNameCode, selectDateNew;
    private SignaturePad signaturePad_mpinPage,signaturePad_electricityBill,signaturePad_drawHome;

    int iLevel = 99;
    boolean isMiniStmtMode = false;
    public AutoCompleteTextView number_textview_addressDetails, idproofIssuDate_EditText_manually, dateOfBirth_EditText_manullay, idProofExpiredDate_EditText_manually;
    String tla ,tlacode,dateOfBirthString, idissuePlaceString, idProofIssuDateString;
    String dateSetString;
    String idProofIssuDate;
    //---------------------------
    int keyboardSelection = 0;

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

                if (keyboardSelection == 0) {

                    if (validateDetailsPartFirst()) {
                        AgentIdentity();
                    }
                } else if (keyboardSelection == 1) {
                    if (validationMpin()) {

                        number_textview_mpinpage.setText(accountNumber);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_idDetails_form.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                        number_textview_uploding.setText(accountNumber);


                        if (selectCameraGalleryMpinSubscriberSign == 0 && humanSignature == 3) {
                            createAccountRequest();
                        } else if (selectCameraGalleryMpinSubscriberSign == 0) {
                            Toast.makeText(CreateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();
                        } else {
                            createAccountRequest();
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


          /* String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
            if (languageToUse.trim().length() == 0) {
                languageToUse = "fr";
            }

            Locale locale = new Locale(languageToUse);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());*/


        setContentView(R.layout.create_account_new_french);

        transferTagArray = getResources().getStringArray(R.array.TransferTag);
        bankSelectionArray = getResources().getStringArray(R.array.BankSelection);
        accountTypeArray = getResources().getStringArray(R.array.AccountType);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_createAccount);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");


        try {
             tla = mComponentInfo.getmSharedPreferences().getString("tla", "");
             tlacode = mComponentInfo.getmSharedPreferences().getString("tlacode", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle("CREATION DE COMPTE");
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

            genderArray = getResources().getStringArray(R.array.genderDetailsType_french);
            genderArray[0] = "Sexe";


            genderCodeArray = getResources().getStringArray(R.array.genderTypeCode);
            professionArray = getResources().getStringArray(R.array.professionArray_French);


            // idProofArray = getResources().getStringArray(R.array.IDProofTypeArray);
            // idProofCodeArray = getResources().getStringArray(R.array.IDProofTypeCodeArray);

        } catch (Exception e) {
            e.printStackTrace();
            //  CreateAccount.this.finish();
        }

        webview_new = (WebView) findViewById(R.id.webview_new);
        webview_new.loadUrl("file:///android_asset/termcondition_fr.html");


        scrollView_PersonalDetails_form = (ScrollView) findViewById(R.id.scrollView_PersonalDetails_form);
        scrollView_createAgent_firstPart = (ScrollView) findViewById(R.id.scrollView_createAgent_firstPart);
        scrollView_idDetails_form = (ScrollView) findViewById(R.id.scrollView_idDetails_form);
        scrollView_mpin_Page = (ScrollView) findViewById(R.id.scrollView_mpin_Page);

        scrollView_button_firstPart = (ScrollView) findViewById(R.id.scrollView_button_firstPart);
        scrollView_electricity_bill = (ScrollView) findViewById(R.id.scrollView_electricity_bill);
        scrollView_uploading_form_showreceipt = (ScrollView) findViewById(R.id.scrollView_uploading_form_showreceipt);
        scrollView_customerPicture = (ScrollView) findViewById(R.id.scrollView_customerPicture);
        scrollView_button_idDocumnetpicure = (ScrollView) findViewById(R.id.scrollView_button_idDocumnetpicure);

        submitButton_partfirst = (Button) findViewById(R.id.submitButton_partfirst);
        submitButton_partfirst.setOnClickListener(this);

        scrollView_createAgent_confirmationPage = (ScrollView) findViewById(R.id.scrollView_createAgent_confirmationPage);

        scrollView_createAgent_thirdPage_form = (ScrollView) findViewById(R.id.scrollView_createAgent_thirdPage_form);
        scrollView_idDocumentPicture_Page = (ScrollView) findViewById(R.id.scrollView_idDocumentPicture_Page);
        scrollView_addressDetails_form = (ScrollView) findViewById(R.id.scrollView_addressDetails_form);

        agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText("Code de l'Agent:-  " + agentCode);

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
        spinnerCountryNationality.setOnItemSelectedListener(CreateAccountNewFrench.this);

        spinnerPlanAccountProfile = (Spinner) findViewById(R.id.spinnerPlanAccountProfile);
        spinnerPlanAccountProfile.setOnItemSelectedListener(CreateAccountNewFrench.this);

        spinnerProfession = (Spinner) findViewById(R.id.spinnerProfession);
        spinnerProfession.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, professionArray));
        spinnerProfession.setOnItemSelectedListener(CreateAccountNewFrench.this);


        scrollView_mpin_Page_linearLayout = (LinearLayout) findViewById(R.id.scrollView_mpin_Page_linearLayout);

        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(CreateAccountNewFrench.this);

        spinnerAccountToDebit = (Spinner) findViewById(R.id.spinnerAccountToDebit);
        spinnerAccountToDebit.setOnItemSelectedListener(this);


        languageSelectDetails = getResources().getStringArray(R.array.languageSelectDetails_french);
        languageSelectCode = getResources().getStringArray(R.array.languageSelectCode);

        spinner_LanguageType = (Spinner) findViewById(R.id.spinner_LanguageType);
        spinner_LanguageType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languageSelectDetails));
        spinner_LanguageType.setOnItemSelectedListener(this);


        spinnerGenderDetails = (Spinner) findViewById(R.id.spinnerGenderDetails);
        spinnerGenderDetails.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray));
        spinnerGenderDetails.setOnItemSelectedListener(this);


        accountNumberEditText = (AutoCompleteTextView) findViewById(R.id.accountNumberEditText);
        accountNumberEditText.setOnEditorActionListener(this);

        loyalityCardNumberEditTex = (AutoCompleteTextView) findViewById(R.id.loyalityCardNumberEditTex);
        loyalityCardNumberEditTex.setOnEditorActionListener(this);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.emailEditText);
        emailEditText.setOnEditorActionListener(this);

        checkBox_termendCondition  = (CheckBox) findViewById(R.id.checkBox_termendCondition);


        stateEditText = (AutoCompleteTextView) findViewById(R.id.stateEditText_AccToCash);
        stateEditText.setOnEditorActionListener(this);

        idproof_calender_button = (Button) findViewById(R.id.idproof_calender_button);
        idproof_calender_button.setInputType(InputType.TYPE_NULL);
        idproof_calender_button.setOnTouchListener(this);
        otherProfessionEditText = (AutoCompleteTextView) findViewById(R.id.otherProfessionEditText_CreateAcc);
        idproofName_edittext = (AutoCompleteTextView) findViewById(R.id.idproofName_edittext);
        idissuePlace_edittext = (AutoCompleteTextView) findViewById(R.id.idissuePlace_edittext);
        number_textview_mpinpage = (AutoCompleteTextView) findViewById(R.id.number_textview_mpinpage);
        number_textview_uploding = (AutoCompleteTextView) findViewById(R.id.number_textview_uploding);

          //----------------------------------------   Humane Sign  ----------------------------------------------------------------


        scrollView_signatureHuman_drawHome = (ScrollView) findViewById(R.id.scrollView_signatureHuman_drawHome);
        signaturePad_saveButton_drawHome = (Button) findViewById(R.id.signaturePad_saveButton_drawHome);
        signaturePad_clearButton_drawHome = (Button) findViewById(R.id.signaturePad_clearButton_drawHome);
        signaturePad_drawHome = (SignaturePad) findViewById(R.id.signaturePad_drawHome);
        signaturePad_drawHome.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                noSelectBillHomeSignature=1;

                // Toast.makeText(SignaturePadActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
                selectSignaurepic=2;
            }

            @Override
            public void onSigned() {
                signaturePad_saveButton_drawHome.setEnabled(true);
                signaturePad_clearButton_drawHome.setEnabled(true);
            }

            @Override
            public void onClear() {
                noSelectBillHomeSignature=0;
                signaturePad_saveButton_drawHome.setEnabled(true);
                signaturePad_clearButton_drawHome.setEnabled(false);
            }
        });

        scrollView_signatureHuman_mpin_page = (ScrollView) findViewById(R.id.scrollView_signatureHuman_mpin_page);
        signaturePad_saveButton_mpin = (Button) findViewById(R.id.signaturePad_saveButton_mpin);
        signaturePad_clearButton_mpinPage = (Button) findViewById(R.id.signaturePad_clearButton_mpinPage);
        signaturePad_mpinPage = (SignaturePad) findViewById(R.id.signaturePad_mpinPage);
        signaturePad_mpinPage.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                noSelectMpinSignature=1;

                // Toast.makeText(SignaturePadActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSigned() {
                signaturePad_saveButton_mpin.setEnabled(true);
                signaturePad_clearButton_mpinPage.setEnabled(true);
            }

            @Override
            public void onClear() {
                noSelectMpinSignature=0;
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

                noSelectElectricitySignature=1;

                selectSignaurepic=2;
            }

            @Override
            public void onSigned() {
                signaturePad_saveButton_billElectricity.setEnabled(true);
                signaturePad_clearButton_billElectricity.setEnabled(true);
            }

            @Override
            public void onClear() {
                noSelectElectricitySignature=0;
                signaturePad_saveButton_billElectricity.setEnabled(true);  // change all page
                signaturePad_clearButton_billElectricity.setEnabled(false);
            }
        });





        signaturePad_saveButton_billElectricity.setOnClickListener(this);
        signaturePad_clearButton_billElectricity.setOnClickListener(this);
        signaturePad_saveButton_mpin.setOnClickListener(this);
        signaturePad_clearButton_mpinPage.setOnClickListener(this);
        signaturePad_clearButton_drawHome.setOnClickListener(this);
        signaturePad_saveButton_drawHome.setOnClickListener(this);



     //  signatureBitmapMpin = signaturePad_mpinPage.getSignatureBitmap();

        // -----------------------------------------------------------------------------------------------------



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
        customerIdNumber_edittext = (AutoCompleteTextView) findViewById(R.id.customerIdNumber_edittext);
        customerName_edittext = (AutoCompleteTextView) findViewById(R.id.customerName_edittext);
        addressEditText = (AutoCompleteTextView) findViewById(R.id.address_EditText_AccToCash);
        addressEditText.setOnEditorActionListener(this);
        countryEditText = (AutoCompleteTextView) findViewById(R.id.countryEditText_AccToCash);
        countryEditText.setOnEditorActionListener(this);
        dateOfBirth_EditText_manullay = (AutoCompleteTextView) findViewById(R.id.dateOfBirth_EditText_manullay);
        idproofIssuDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idproofIssuDate_EditText_manually);
        number_textview_addressDetails = (AutoCompleteTextView) findViewById(R.id.number_textview_addressDetails);
        idProofExpiredDate_EditText_manually = (AutoCompleteTextView) findViewById(R.id.idProofExpiredDate_EditText_manually);
        idProofPlaceEditText = (AutoCompleteTextView) findViewById(R.id.idProofPlaceEditText_AccToCash);
        idProofPlaceEditText.setOnEditorActionListener(this);
        birthpalceEditText = (AutoCompleteTextView) findViewById(R.id.birthpalceEditText_AccToCash);
        birthpalceEditText.setOnEditorActionListener(this);
        agentTypeEditText = (AutoCompleteTextView) findViewById(R.id.agentTypeEditText_AccToCash);
        agentTypeEditText.setOnEditorActionListener(this);
        secondMobileNumberEditText = (AutoCompleteTextView) findViewById(R.id.secondMobileNumberEditText);
        secondMobileNumberEditText.setOnEditorActionListener(this);
        fixednumber_edittext = (AutoCompleteTextView) findViewById(R.id.fixednumber_edittext);
        fixednumber_edittext.setOnEditorActionListener(this);
        residenceAreaEditText = (AutoCompleteTextView) findViewById(R.id.residenceAreaEditText_AccToCash);
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
        residenceAreaEditText.setOnEditorActionListener(this);

        setInputType(1);

        scrollView_button_firstPart.setVisibility(View.VISIBLE);

        picturePreview_imageview = (ImageView) findViewById(R.id.picturePreview_imageview);
        imageview_priview_front_iddocument = (ImageView) findViewById(R.id.imageview_priview_front_iddocument);
        imageview_preview_drawHome = (ImageView) findViewById(R.id.imageview_preview_drawHome);
        imageview_bill_electricityBill = (ImageView) findViewById(R.id.imageview_bill_electricityBill);
        imageview_preview_back_iddocument = (ImageView) findViewById(R.id.imageview_preview_back_iddocument);
        imageview_preview_mpinPage = (ImageView) findViewById(R.id.imageview_preview_mpinPage);

        imageview_subscriberForm = (ImageView) findViewById(R.id.imageview_subscriberForm);
        imageview_subscriberForm.setOnClickListener(this);


        green_imageview_form = (ImageView) findViewById(R.id.green_imageview_form);
        green_imageview_personalImage = (ImageView) findViewById(R.id.green_imageview_personalImage);
        green_imageview_idfront = (ImageView) findViewById(R.id.green_imageview_idfront);
        green_imageview_idback = (ImageView) findViewById(R.id.green_imageview_idback);
        green_imageview_billlocation = (ImageView) findViewById(R.id.green_imageview_billlocation);
        green_imageview_signature = (ImageView) findViewById(R.id.green_imageview_signature);

        linearlayout_imageview_fullScreen = (LinearLayout) findViewById(R.id.linearlayout_imageview_fullScreen);
        imageview_fullScreen = (ImageView) findViewById(R.id.imageview_fullScreen);
        scrollView_mpin_Page_linearLayout = (LinearLayout) findViewById(R.id.scrollView_mpin_Page_linearLayout);


        // -------------------------- Customer picture -----------------------------------------------------------

        camera_imageview = (ImageView) findViewById(R.id.camera_imageview);
        camera_imageview.setClickable(true);
        camera_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_customerpic();
                }
            }
        });

        upload_imageView = (ImageView) findViewById(R.id.upload_imageView);
        upload_imageView.setClickable(true);
        upload_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openImageChooser_customerpic();


            }
        });

        // ----------------------------------- id document  ----------------------------------------------------------


        camera_back_iddocument_imageview = (ImageView) findViewById(R.id.camera_back_iddocument_imageview);
        camera_back_iddocument_imageview.setClickable(true);
        camera_back_iddocument_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_back_Iddocument();
                }
            }
        });


        upload_back_iddocument_imageview = (ImageView) findViewById(R.id.upload_back_iddocument_imageview);
        upload_back_iddocument_imageview.setClickable(true);
        upload_back_iddocument_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser_Back_Iddocument();
            }
        });


        camera_front_iddocument_imageview = (ImageView) findViewById(R.id.camera_front_iddocument_imageview);
        camera_front_iddocument_imageview.setClickable(true);
        camera_front_iddocument_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_Front_Iddocument();
                }
            }
        });


        upload_front_iddocument_imageview = (ImageView) findViewById(R.id.upload_front_iddocument_imageview);
        upload_front_iddocument_imageview.setClickable(true);
        upload_front_iddocument_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser_Front_Iddocument();
            }
        });


        // -------------------camera gallery electricity --------------------------------------------------------


        camera_billElectricity_imageview = (ImageView) findViewById(R.id.camera_billElectricity_imageview);
        camera_billElectricity_imageview.setClickable(true);
        camera_billElectricity_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_billElectricity();
                }

            }
        });


        upload_pic_electricityBill_imageview = (ImageView) findViewById(R.id.upload_pic_electricityBill_imageview);
        upload_pic_electricityBill_imageview.setClickable(true);
        upload_pic_electricityBill_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customeAlertDialogSignature_billElectricity();

           }
        });


        // --------------------------Draw Home----------------------------------------------------

        camera_drawaHome_imageview = (ImageView) findViewById(R.id.camera_drawaHome_imageview);
        camera_drawaHome_imageview.setClickable(true);
        camera_drawaHome_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_DrawHome();
                }

            }
        });

        upload_DrawHome_imageview = (ImageView) findViewById(R.id.upload_DrawHome_imageview);
        upload_DrawHome_imageview.setClickable(true);
        upload_DrawHome_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                customeAlertDialogSignature_dwarHome();
            }
        });

        // --------------------------Mpin camera----------------------------------------------------

        camera_mpin_imageview = (ImageView) findViewById(R.id.camera_mpin_imageview);
        camera_mpin_imageview.setClickable(true);
        camera_mpin_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateAccountNewFrench.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    checkPermission();
                }

                else {

                    openCamera_MpinPage();
                }


            }
        });

        upload_mpin_imageview = (ImageView) findViewById(R.id.upload_mpin_imageview);
        upload_mpin_imageview.setClickable(true);
        upload_mpin_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customeAlertDialogSignature_mpinpage();
            }
        });


        picturePreview_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectGalleryFullScreen_customer == 0 && selectCameraFullScreen_customer == 0) {

                } else {
                    fullScreen();
                }


            }
        });
        // --------------------------------------------------------------------------------------------------------

        // customeLayout_subscriberFormImage();
    }
    private boolean checkPermission()

    {

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return true;
            }

            if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)


            {
                return true;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                } else {
                    // if click check box do not show again click   click denny then else condition
                    Toast.makeText(CreateAccountNewFrench.this, "S il vous plait aller à l application de configuration mobile et autoriser toutes les autorisations", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_READ_PERMISSION);

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CreateAccountNewFrench.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_SHORT).show();

        }
        return false;
    }


    void fullScreen()
    {
        scrollView_customerPicture.setVisibility(View.GONE);

        linearlayout_imageview_fullScreen.setVisibility(View.VISIBLE);
        selectNextButton = 13;
        submitButton_partfirst.setText("Derriere");
        submitButton_partfirst.setVisibility(View.VISIBLE);


        if (selectGalleryFullScreen_customer == 1) {
            imageview_fullScreen.setImageBitmap(selectedImage);
            tempGallery = 1;
        } else if (selectCameraFullScreen_customer == 1) {
            imageview_fullScreen.setImageBitmap(bitmapCameraCustomer);
            tempCamera = 1;
        }


        //   imageview_fullScreen.setImageResource(R.drawable.downloadimage);

        imageview_fullScreen.setScaleType(ImageView.ScaleType.FIT_XY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getSupportActionBar().hide();

    }

    // demo
    void customeAlertDialogSignature_billElectricity() {


        Resources res = getResources();

        CharSequence[] items = res.getStringArray(R.array.signaturehuman_gallery_french);

        // Creating and Building the Dialog
        alertDialogPopup = new android.app.AlertDialog.Builder(this);
        alertDialogPopup.setTitle("Choix");
        //  alertDialogAuth.setCancelable(false);

        alertDialogPopup.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.VISIBLE);

                        backButtonBillHomeElectricity=1;


                        break;

                    case 1:

                        openImageChooser_billElectricity();

                        break;
                }
                dialog.dismiss();
            }
        });

        alertDialog = alertDialogPopup.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    void customeAlertDialogSignature_dwarHome() {

        Resources res = getResources();
        CharSequence[] items = res.getStringArray(R.array.signaturehuman_gallery_french);

        // Creating and Building the Dialog
        alertDialogPopup = new android.app.AlertDialog.Builder(this);
        alertDialogPopup.setTitle("Choix");
        //  alertDialogAuth.setCancelable(false);

        alertDialogPopup.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_signatureHuman_drawHome.setVisibility(View.VISIBLE);

                        backButtonBillHomeElectricity=3;



                        break;

                    case 1:

                        openImageChooser_DrawHome();


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
        alertDialogPopup = new android.app.AlertDialog.Builder(this);
        alertDialogPopup.setTitle("Choix");
        //  alertDialogAuth.setCancelable(false);

        alertDialogPopup.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:

                        scrollView_createAgent_firstPart.setVisibility(View.GONE);
                        scrollView_button_firstPart.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                        scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                        scrollView_signatureHuman_mpin_page.setVisibility(View.VISIBLE);

                        backButtonSignMpin=1;

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


    private void instanstiate_RetryList() {


        retryCount_List = new Hashtable<>();

        retryCount_List.put(CREATE_ACCOUNT_APIS.API_CREATE_ACCOUNT, 0);
        retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_FORM, 0);
        retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_HOMELOCATION_BILLCOPY, 0);
        retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_BACK, 0);
        retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_FRONT, 0);
        retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_PERSONAL, 0);

    }


    private void process_ServerResponseFailure(int requestNo) {

        int retryCount = 0;

        switch (requestNo) {

            case 102:

                retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_CREATE_ACCOUNT);

                if (retryCount < 2) {
                    /// attempt one more time
                    retryCount_List.put(CREATE_ACCOUNT_APIS.API_CREATE_ACCOUNT, retryCount++);
                } else {
                    // display toast and end process.
                }

                break;


            case 103:

                switch (create_account_apis) {


                    case API_IMAGE_UPLOAD_PERSONAL:
                        retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_PERSONAL);

                        if (retryCount < 2) {
                            /// attempt one more time
                            retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_PERSONAL, retryCount++);
                        } else {
                            // display toast and continue to next image.
                        }

                        break;

                    case API_IMAGE_UPLOAD_ID_FRONT:

                        retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_FRONT);

                        if (retryCount < 2) {
                            /// attempt one more time
                            retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_FRONT, retryCount++);
                        } else {
                            // display toast and continue to next image.
                        }
                        break;

                    case API_IMAGE_UPLOAD_ID_BACK:
                        retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_BACK);

                        if (retryCount < 2) {
                            /// attempt one more time
                            retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_ID_BACK, retryCount++);
                        } else {
                            // display toast and continue to next image.
                        }
                        break;

                    case API_IMAGE_UPLOAD_HOMELOCATION_BILLCOPY:
                        retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_HOMELOCATION_BILLCOPY);

                        if (retryCount < 2) {
                            /// attempt one more time
                            retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_HOMELOCATION_BILLCOPY, retryCount++);
                        } else {
                            // display toast and continue to next image.
                        }
                        break;

                    case API_IMAGE_UPLOAD_FORM:
                        retryCount = retryCount_List.get(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_FORM);

                        if (retryCount < 2) {
                            /// attempt one more time
                            retryCount_List.put(CREATE_ACCOUNT_APIS.API_IMAGE_UPLOAD_FORM, retryCount++);
                        } else {
                            // display toast and end.
                        }
                        break;

                    default:
                        break;


                }


                break;


            default:
                // display toast and end process.
                break;
        }


    }


    void pictureChooseFirstPart(Intent data) {
        selectedImageUriProfilPic = data.getData();

        if (null != selectedImageUriProfilPic) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(selectedImageUriProfilPic);

                selectedImage = BitmapFactory.decodeStream(imageStream);


                ColorMatrix matrix = new ColorMatrix();   // color to black in white
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                //   upload_imageView.setColorFilter(filter);

                Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
                selectedImage = resizeBmp;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] imageBytes = baos.toByteArray();

                uploadCameraStringCustomerPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                //   upload_imageView.setImageBitmap(selectedImage);
                picturePreview_imageview.setImageBitmap(selectedImage);
                selectGalleryFullScreen_customer = 1;
                selectGallery = 1;


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void customeLayout_subscriberFormImage() {
        try {
            LayoutUtilities layoutUtilities = new LayoutUtilities(this, 1500, 1000);
            layoutUtilities.initiateViewForImage_Restrict();


            /////////////////////////////////////////////////////    DEMO   //////////////////////////////////////////////////////////////////

            byte[] decodedString = Base64.decode(uploadCameraStringSignature, Base64.DEFAULT);
            Bitmap signatureBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            byte[] decodedString2 = Base64.decode(uploadCameraStringCustomerPicture, Base64.DEFAULT); //
            Bitmap pictureSignBitmap = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);


            if(fixPhonenoString.equalsIgnoreCase(""))
            {


            }

            else {
                countryCodePrefixString = getCountryPrefixCode();
                fixMobiletempNumber = countryCodePrefixString + fixPhonenoString;
            }

            View v = getLayoutInflater().inflate(R.layout.subscription_form_layout, (ViewGroup) layoutUtilities.getParentLL());

            setSubscriptionFormValues(v,
                    customerNameString, "", birthpalceString,
                    nationalityCodeString, idproofNameString, professionCodeString,
                    fixMobiletempNumber, emailString, "",
                    addressString,cityString, "", "",
                    attachBranchNameString, accountNumber, LanguageString, pictureSignBitmap, signatureBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            layoutUtilities.restParentLL();
            layoutUtilities.initiateViewForImage_Restrict();
            layoutUtilities.setMiscellinousView(v);

            layoutUtilities.finaliseViewForImage();

            /////////////   ////////////////

            Bitmap mBitmapTem=layoutUtilities.getFinalImage();
            mBitmapTem .compress(Bitmap.CompressFormat.PNG, 100, baos);


            if(mBitmapTem!=null) {

                byte[] b = baos.toByteArray();

                subscriberformString = Base64.encodeToString(b, Base64.DEFAULT);

                imageview_subscriberForm.setImageBitmap(mBitmapTem);  // final zoom in zoom out pic set


            }else {
                Toast.makeText(CreateAccountNewFrench.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();
            }
//            if (formDisplay == 1) {
//           } else {
//                photoView.setImageBitmap(layoutUtilities.getFinalImage());   // final zoom in zoom out pic set
//            }

//            try {         // save in Gallery
//                String path = Environment.getExternalStorageDirectory().toString();
//                OutputStream fOut = null;
//                Integer counter = 0;
//
//
//                File file = new File(path, "pic" + counter + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
//
//
//                fOut = new FileOutputStream(file);
//
//
//                layoutUtilities.getFinalImage().compress(Bitmap.CompressFormat.JPEG, 99, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//                fOut.flush(); // Not really required
//                fOut.close(); // do not forget to close the stream
//
//                MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//
//
//            } catch (Exception e) {
//
//                StringWriter errors = new StringWriter();
//                e.printStackTrace(new PrintWriter(errors));
//
//                Toast.makeText(CreateAccountNewEnglish.this,   errors.toString(), Toast.LENGTH_LONG).show();   // check exception
//
//                e.printStackTrace();
//
//            }


        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Toast.makeText(CreateAccountNewFrench.this,   errors.toString()+"45645645456456456456", Toast.LENGTH_LONG).show();   // check exception

            e.printStackTrace();
        }
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
                city_TxtView,
                identification_textview,
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
        lastName_TxtView.setText("Noms:.... " + firstName + " ..................");
        datePlaceBirth_TxtView.setText("Date et lieu de naissance :... " + dateOfBirthString+"  "+birthpalceString + " ..................");
        nationality_TxtView.setText("Nationalité...... " + nationality + " ..................");
        identification_textview.setText("........... " +idproofNameString+" ....."+ spinnerIdProofTypeString + " ..................");



        issueDate_TxtView.setText("du.... " + idProofIssuDateString + " ....à....."+idissuePlaceString+".....par");



        profession_TxtView.setText("Profession..... " + profession + " ..................");
        phoneNumber_TxtView.setText("Numéro de téléphone :... " + phoneNumber + " ..................");
        email_TxtView.setText("Email.... " + email + " ..................");
        maritalStatus_TxtView.setText("Situation matrimoniale :.... " + maritalString + " ..................");
        adress_TxtView.setText("Adresse :.. " + address + " ..................");
        residence_TxtView.setText("Ville/Quartier de résidence :..... " + cityString + " ..................");
        placeName_TxtView.setText("lieu dit :.. " + planName + " ..................");
        branchName_TxtView.setText("Account opening branch.... " + attachBranchNameString + " ..................");
        euphone_TxtView.setText(".......... " + accountNumber + " ..................");
        language_TxtView.setText("Language.... " + language + " ..................");

        profilePic.setImageBitmap(userPicture);
        signaturePic.setImageBitmap(userSignature);


    }


    void cameraTakeFirstPart(Intent data) {
        bitmapCameraCustomer = (Bitmap) data.getExtras().get("data");

        try {
            String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmapCameraCustomer, null, null);
            Uri uri = Uri.parse(path);

            imageStreamCamera = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
        bitmapCameraCustomer = bitmap2;
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        bitmapCameraCustomer.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
        byte[] imageBytes = baos2.toByteArray();
        uploadCameraStringCustomerPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        picturePreview_imageview.setImageBitmap(bitmapCameraCustomer);


        selectCameraFullScreen_customer = 1;
        selectCamera = 1;


    }

    void cameraTakeSecondPartPart_Front(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            uploadGaleryIddocumentFrontString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_priview_front_iddocument.setImageBitmap(bitmap);
            selectCameraFront = 1;


        }
    }

    void cameraTakeSecondPartPart_Back(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            cameraGalleryIDDocBackString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_back_iddocument.setImageBitmap(bitmap);
            selectCameraBack = 1;

        }
    }


    void cameraTake_ElectricityBill(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_bill_electricityBill.setImageBitmap(bitmap);
            imageview_preview_drawHome.setImageDrawable(null);
            chooseOneSelect = 1;
            selectCameraElectricity = 1;

        }
    }


    void cameraTake_MpinPage(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            uploadCameraStringSignature = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_mpinPage.setImageBitmap(bitmap);
            selectCameraGalleryMpinSubscriberSign = 1;

            formDisplay = 1;
            customeLayout_subscriberFormImage();
            formDisplay = 2;
            noSelectForm = 1;


        }
    }


    void pictureChooseSecondPartFront(Intent data) {
        uriIddocumnetFront = data.getData();

        if (null != uriIddocumnetFront) {

            boolselectUploadPic = true;
            try {
                imageStream = getContentResolver().openInputStream(uriIddocumnetFront);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            // upload_front_iddocument_imageview.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            uploadGaleryIddocumentFrontString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_priview_front_iddocument.setImageBitmap(selectedImage);
            selectGalleryFront = 1;

        }

    }

    void pictureChooseSecondPart_Back(Intent data) {
        uidString = data.getData();

        if (null != uidString) {

            try {
                imageStream = getContentResolver().openInputStream(uidString);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //  upload_back_iddocument_imageview.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();
            cameraGalleryIDDocBackString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_back_iddocument.setImageBitmap(selectedImage);
            selectGalleryBack = 1;

        }

    }

    void pictureChooseSecondPart_ElectricityBill(Intent data) {
        uidElectricity = data.getData();

        if (null != uidElectricity) {

            try {
                imageStream = getContentResolver().openInputStream(uidElectricity);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //  imageview_bill_electricityBill.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_bill_electricityBill.setImageBitmap(selectedImage);
            imageview_preview_drawHome.setImageDrawable(null);
            chooseOneSelect = 1;
            selectGalleryElectricitybill = 1;

        }

    }

    void pictureChoose_billElectricity_signature() {

        byte[] imageBytes3 = Base64.decode(uploadGalleryStringElectricity, Base64.DEFAULT);
        Bitmap decodedImage3 = BitmapFactory.decodeByteArray(imageBytes3, 0, imageBytes3.length);
        imageview_bill_electricityBill.setImageBitmap(decodedImage3); // set page Third
        imageview_bill_electricityBill.setImageBitmap(decodedImage3);     // set page Third
        imageview_preview_drawHome.setImageDrawable(null);

        mComponentInfo.getmSharedPreferences().edit().putString("selectBillHomeHumanSign", "").commit(); //  put blank for next time not check

    }


    void pictureChoose_drawHome(Intent data) {


        uriDrawHome = data.getData();

        if (null != uriDrawHome) {

            try {
                imageStream = getContentResolver().openInputStream(uriDrawHome);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //  imageview_preview_drawHome.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_drawHome.setImageBitmap(selectedImage);
            imageview_bill_electricityBill.setImageDrawable(null);

            selectGalleryDrawaHome = 1;
            chooseOneSelect = 1;
        }

    }

    void cameraTake_DrawHome(Intent data) {
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            try {
                String path = MediaStore.Images.Media.insertImage(CreateAccountNewFrench.this.getContentResolver(), bitmap, null, null);
                Uri uri = Uri.parse(path);

                imageStreamCamera = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap1 = BitmapFactory.decodeStream(imageStreamCamera);
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, 200, 200, true);
            bitmap = bitmap2;
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2); //bm is the bitmap object
            byte[] imageBytes = baos2.toByteArray();
            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_drawHome.setImageBitmap(bitmap);
            imageview_bill_electricityBill.setImageDrawable(null);
            chooseOneSelect = 1;
            selectCameraDrawaHome = 1;
        }
    }


    void pictureChoose_MpinPage(Intent data) {


        uriMpinPage = data.getData();

        if (null != uriMpinPage) {

            try {
                imageStream = getContentResolver().openInputStream(uriMpinPage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ColorMatrix matrix = new ColorMatrix();   // color to black in white
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            //  imageview_preview_mpinPage.setColorFilter(filter);

            Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
            selectedImage = resizeBmp;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            uploadCameraStringSignature = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //   upload_imageView.setImageBitmap(selectedImage);
            imageview_preview_mpinPage.setImageBitmap(selectedImage);
            selectCameraGalleryMpinSubscriberSign = 1;

            formDisplay = 1;
            customeLayout_subscriberFormImage();
            formDisplay = 2;
            noSelectForm = 1;


        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            System.out.println("back");

            if (requestCode == SELECT_PICTURE_CUSTOMER_UPLOAD)  // customer picture Gallery  100
            {

                pictureChooseFirstPart(data);
            } else if (requestCode == CAMERA_REQUEST_CUSTOMERPICTURE) {   //  customer picture  Camera First Page 101
                cameraTakeFirstPart(data);
            } else if (requestCode == SELECT_PICTURE_FRONT_ID_DOCUMENT)   //  FRONT_ID_DOCUMENT   Gallery
            {
                pictureChooseSecondPartFront(data);
            } else if (requestCode == CAMERA_REQUEST_FRONT_ID_DOCUMENT)     // FRONT_ID_DOCUMENT CAMERA
            {
                cameraTakeSecondPartPart_Front(data);
            } else if (requestCode == SELECT_PICTURE_BACK_ID_DOCUMENT) {  //  FRONT_ID_DOCUMENT   Second page
                pictureChooseSecondPart_Back(data);
            } else if (requestCode == CAMERA_REQUEST_BACK_ID_DOCUMENT)     // FRONT_ID_DOCUMENT CAMERA
            {
                cameraTakeSecondPartPart_Back(data);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////

            else if (requestCode == PICTURE_SELECT_ELECTRICITY_BILL)  // Electricity  Gallery 106
            {
                pictureChooseSecondPart_ElectricityBill(data);
            } else if (requestCode == CAMERA_REQUEST_ELECTRICITY_BILL)  // Electricity Camera 107
            {
                cameraTake_ElectricityBill(data);
            } else if (requestCode == PICTURE_SELECT_DRAW_HOME)     // Draw Home Gallery 108
            {
                pictureChoose_drawHome(data);
            } else if (requestCode == CAMERA_REQUEST_DRAW_HOME)  // Draw Home  Camera 109
            {
                cameraTake_DrawHome(data);
            }

            /////////////////////////////////////// signature page camera gallery/////////////////////////////////////////////////////////

            else if (requestCode == PICTURE_SELECT_MPIN_PAGE)  //  MPIN PAGE gallery 110
            {
                pictureChoose_MpinPage(data);
            } else if (requestCode == CAMERA_REQUEST_MPIN_PAGE) // MPIN PAGE Camera  111
            {
                cameraTake_MpinPage(data);
            }


        }
    }


    void  openCamera_customerpic() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CUSTOMERPICTURE);

    }

    void openCamera_Front_Iddocument() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_FRONT_ID_DOCUMENT);
    }

    void openCamera_back_Iddocument() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_BACK_ID_DOCUMENT);
    }

    void openCamera_DrawHome() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_DRAW_HOME);
    }

    void openCamera_MpinPage() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_MPIN_PAGE);
    }

    void openImageChooser_MpinPage() {

        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECT_MPIN_PAGE);
    }


    void openImageChooser_Front_Iddocument() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_FRONT_ID_DOCUMENT);
    }

    void openCamera_billElectricity() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ELECTRICITY_BILL);
    }

    void openImageChooser_billElectricity() {


        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECT_ELECTRICITY_BILL);
    }

    void openImageChooser_Back_Iddocument() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_BACK_ID_DOCUMENT);
    }

    void openImageChooser_DrawHome() {

        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_SELECT_DRAW_HOME);
    }


    void openImageChooser_customerpic() {

        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_CUSTOMER_UPLOAD);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);


                break;

            case R.id.spinnerAttachedBranchName:


                attachBranchNameString = spinnerAttachedBranchName.getSelectedItem().toString();

                    if(tlacode.equalsIgnoreCase(""))
                        {

                            if (i != 0) {
                                attachBranchNameString = attachBranchNameCode[i];

                                branchCodeString = codeProfileArray[i];
                                parentfixedBAString = fixedBACodeArray[i];

                            } else {
                                attachBranchNameString = "";
                                branchCodeString = "";
                                parentfixedBAString = "";
                            }

                        }

                    else
                     {
                         tlacode=tlacode;
                     }

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

                } else {

                    spinnerIdProofTypeString = "";
                    idProofCodeString = "";

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

                break;

            case R.id.spinnerPlanAccountProfile:
                try {

                    planNameString = spinnerPlanAccountProfile.getSelectedItem().toString();


                    if (i != 0) {
                        planNameString = plancodeArray[i];
                        agentTypeString = agenttypeArray[i];


                    } else {
                        planNameString = "";
                        agentTypeString = "";

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }


                break;


            case R.id.spinnerCountry:

                //   validateDetailsPartFirst();

                setInputType(transferBasisSpinner.getSelectedItemPosition());


                break;

            case R.id.spinnerProfession:
                if (i != 0) {
                    professionString = professionArray[i];
                    professionCodeString = professionArray[i];
                    professionSelectedPosition = i;


                    //    isOtherProfession = professionCodeString.equalsIgnoreCase("others") ? true : false;
                    //   otherProfessionEditText.setVisibility(professionCodeString.equalsIgnoreCase("others") ? View.VISIBLE : View.GONE);

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
                accountNumberEditText.setHint(String.format(getString(R.string.hintCreateAccountNumberNew_french), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
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
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onDateSet(final DatePicker var1, final String year, final String month, final String day) {
        CreateAccountNewFrench.this.runOnUiThread(new Runnable() {
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
                    Toast.makeText(CreateAccountNewFrench.this, "Veuillez sélectionner une date de naissance valide", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CreateAccountNewFrench.this, "Veuillez sélectionner une date de naissance valide", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CreateAccountNewFrench.this, "Veuillez sélectionner une date démission de la piéce d identité valide", Toast.LENGTH_LONG).show();
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
                    idProofExpiredDate_EditText_manually.setText(dateSetString);

                    idProofExpiredDate_EditText_manually.setText(dateSetString);

                    ret = false;//If start date is before end date
                } else {
                    Toast.makeText(CreateAccountNewFrench.this, "Veuillez sélectionner la date d expiration valide", Toast.LENGTH_LONG).show();

                    idProofExpiredDate_EditText_manually.setText("");
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
                    Toast.makeText(CreateAccountNewFrench.this, "Veuillez sélectionner une pièce d'identité valide Date de validation", Toast.LENGTH_LONG).show();
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
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
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

                            number_textview_iddetails.setText(accountNumber); /////


                            ret = true;

                        } else {
                            Toast.makeText(CreateAccountNewFrench.this, "Date expiration pièce JJ-MM-AAA", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(CreateAccountNewFrench.this, "Lieu d émission de la piéce", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateAccountNewFrench.this, "Date emission pièce JJ-MM-AAAA", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(CreateAccountNewFrench.this,"Numéro de la pièce", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Type de piéce", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }


    private boolean validationPersonlDetailsForm() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            if (spinnerPlanAccountProfile.getSelectedItemPosition() != 0) {

                customerIdString = customerIdNumber_edittext.getText().toString().toString();
               // if (customerIdString.length() >= 4) {

                    customerNameString = customerName_edittext.getText().toString().toString();
                    if (customerNameString.length() >= 2) {


                        if (spinnerGenderDetails.getSelectedItemPosition() != 0) {

                            if (spinner_LanguageType.getSelectedItemPosition() != 0) {

                                lengthToCheck = getMobileNoLength();
                                secondMobileNumberString = secondMobileNumberEditText.getText().toString().trim();
                                errorMsgToDisplay = String.format(getString(R.string.hintSecondMobileNumber_french), lengthToCheck + "");

                              //  if (secondMobileNumberString.length() == lengthToCheck) {


                                    errorMsgToDisplay = String.format(getString(R.string.hintSecondFixedMobileNumber_french), lengthToCheck + "");
                                    fixPhonenoString = fixednumber_edittext.getText().toString().trim();

                                //    if (fixPhonenoString.length() == lengthToCheck) {

                                        emailString = emailEditText.getText().toString().trim();


                                     //   if (emailString.length() >= 4 && validateEmail(emailString)) {


                                            if (spinnerProfession.getSelectedItemPosition() != 0) {

                                                dateOfBirthString = dateOfBirth_EditText_manullay.getText().toString().trim();
                                                if (dateOfBirthString.length() > 9 && dateOfBirthString.matches("\\d{2}-\\d{2}-\\d{4}")) {


                                                    number_textview_iddetails.setText(accountNumber);


                                                    ret = true;


                                                } else {
                                                    Toast.makeText(CreateAccountNewFrench.this, "Saisir la Date de naissance", Toast.LENGTH_LONG).show();
                                                }


                                            } else {
                                                Toast.makeText(CreateAccountNewFrench.this, "Profession", Toast.LENGTH_LONG).show();
                                            }


                                    /*    } else {
                                            Toast.makeText(CreateAccountNewFrench.this, "Email", Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        Toast.makeText(CreateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                        // ret = false;
                                    }

                                } else {
                                    Toast.makeText(CreateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                                    // ret = false;
                                }*/
                            } else {
                                Toast.makeText(CreateAccountNewFrench.this, "Language choisie", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CreateAccountNewFrench.this, "Sexe", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(CreateAccountNewFrench.this, "Nom du Client", Toast.LENGTH_LONG).show();
                    }

               /* } else {
                    Toast.makeText(CreateAccountNewFrench.this,"Numéro du Client", Toast.LENGTH_LONG).show();
                }*/

            } else {
                Toast.makeText(CreateAccountNewFrench.this, "Profile du Compte", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();
        }
        return ret;
    }


    private boolean validationAddressDetailsForm() {
        boolean ret = false;


        birthpalceString = birthpalceEditText.getText().toString().trim();
       // if (birthpalceString.length() >= 3) {

            addressString = addressEditText.getText().toString().trim();
        //    if (addressString.length() >= 3) {

                if (spinnerCity.getSelectedItemPosition() != 0) {

                    if (spinnerAttachedBranchName.getSelectedItemPosition() != 0) {

                        if (spinnerCountryNationality.getSelectedItemPosition() != 0) {

                            residenceAreaString = residenceAreaEditText.getText().toString().trim();
                            if (residenceAreaString.length() >= 3) {


                                ret = true;


                            } else {
                                Toast.makeText(CreateAccountNewFrench.this, "Lieu de Résidence", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CreateAccountNewFrench.this, "Nationalité", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateAccountNewFrench.this, "Nom de l agence de domiciliation", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CreateAccountNewFrench.this, "Ville", Toast.LENGTH_SHORT).show();
                }

           /* } else {
                Toast.makeText(CreateAccountNewFrench.this, "Address", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Lieu de naissance", Toast.LENGTH_SHORT).show();
        }
*/

        return ret;

    }

    private boolean validationAddressDetailsForm_agentBranchAutoselected() {
        boolean ret = false;


        birthpalceString = birthpalceEditText.getText().toString().trim();
     //   if (birthpalceString.length() >= 3) {

            addressString = addressEditText.getText().toString().trim();
     //       if (addressString.length() >= 3) {

                if (spinnerCity.getSelectedItemPosition() != 0) {


                        if (spinnerCountryNationality.getSelectedItemPosition() != 0) {

                            residenceAreaString = residenceAreaEditText.getText().toString().trim();
                            if (residenceAreaString.length() >= 3) {

                                ret = true;

                            } else {
                                Toast.makeText(CreateAccountNewFrench.this, "Lieu de Résidence", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CreateAccountNewFrench.this, "Nationalité", Toast.LENGTH_SHORT).show();
                        }


                } else {
                    Toast.makeText(CreateAccountNewFrench.this, "Ville", Toast.LENGTH_SHORT).show();
                }

           /* } else {
                Toast.makeText(CreateAccountNewFrench.this, "Address", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Lieu de naissance", Toast.LENGTH_SHORT).show();
        }*/


        return ret;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.signaturePad_saveButton_billElectricity:
                signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();

                if(noSelectElectricitySignature==0)
                {
                     Toast.makeText(CreateAccountNewFrench.this, "Veuillez choisir dessiner signe",Toast.LENGTH_SHORT).show();
                }

                else {

                    if (addJpgSignatureToGallery(signatureBitmapBillElectricity, "BillElectricity")) {

                        scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_customerPicture.setVisibility(View.GONE);
                        scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.VISIBLE);
                        selectNextButton = 3;
                        submitButton_partfirst.setText("Suivant");
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        humanSignature = 1;

                    }
                }

                break;

            case R.id.signaturePad_clearButton_billElectricity:
            {
                signaturePad_electricityBill.clear();
            }

            break;

            case R.id.signaturePad_saveButton_drawHome:   // save Signature Bill demo

                signatureBitmapDrawHome = signaturePad_drawHome.getSignatureBitmap();

                if(noSelectBillHomeSignature==0)
                {
                  //  Toast.makeText(CreateAccountNewFrench.this, "Please Select Draw Sign",Toast.LENGTH_SHORT).show();

                     Toast.makeText(CreateAccountNewFrench.this, "Veuillez choisir dessiner signe",Toast.LENGTH_SHORT).show();
                }

         else {
                    if (addJpgSignatureToGallery(signatureBitmapDrawHome, "DRAWHOME")) {

                        scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_customerPicture.setVisibility(View.GONE);
                        scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.VISIBLE);
                        selectNextButton = 3;
                        submitButton_partfirst.setText("Suivant");
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        humanSignature = 2;
                    }
                }

                break;


            case R.id.signaturePad_clearButton_drawHome:
            {
                signaturePad_drawHome.clear();
            }
            break;


            case R.id.signaturePad_saveButton_mpin:   // save Signature Mpin demo



                signatureBitmapMpin = signaturePad_mpinPage.getSignatureBitmap();

                if(noSelectMpinSignature==0)
                {
                   // Toast.makeText(CreateAccountNewFrench.this, "Please Select Draw Sign",Toast.LENGTH_SHORT).show();

                     Toast.makeText(CreateAccountNewFrench.this, "Veuillez choisir dessiner signe",Toast.LENGTH_SHORT).show();
                }

            else {
                    if (addJpgSignatureToGallery(signatureBitmapMpin, "MPINPAGE")) {

                        scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                        scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                        scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);

                        scrollView_customerPicture.setVisibility(View.GONE);
                        scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                        scrollView_electricity_bill.setVisibility(View.GONE);

                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                        submitButton_partfirst.setText("Suivant");
                        selectNextButton = 7;
                        scrollView_button_firstPart.setVisibility(View.VISIBLE);
                        humanSignature = 3;

                        formDisplay = 1;
                        customeLayout_subscriberFormImage();
                        formDisplay = 2;
                        noSelectForm = 1;

                    }
                }


            break;

            case R.id.signaturePad_clearButton_mpinPage:
            {
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


            case R.id.submitButton_partfirst:

                if (selectNextButton == 0) {
                    validateDetailsFirst();
                } else if (selectNextButton == 1) {
                    if (selectCamera == 0 && selectGallery == 0) {
                        Toast.makeText(CreateAccountNewFrench.this, "Sélectionnez une photo prise depuis la camera", Toast.LENGTH_SHORT).show();
                    } else {
                        validationPageCustomerPicture();
                    }

                } else if (selectNextButton == 2) {

                    if (selectCameraFront == 0 && selectGalleryFront == 0) {
                        Toast.makeText(CreateAccountNewFrench.this, "Sélectionnez l avant d une photo prise depuis la camera", Toast.LENGTH_SHORT).show();
                    } else if (selectCameraBack == 0 && selectGalleryBack == 0) {
                        Toast.makeText(CreateAccountNewFrench.this, "Sélectionnez l arriere d une photo prise depuis la camera", Toast.LENGTH_SHORT).show();

                    } else {
                        validationPageidDocumentPicture();

                    }
                }

                else if (selectNextButton == 3) {


                      if (chooseOneSelect == 1 || humanSignature==1||humanSignature==2) {
                          validationElectricityForm();
                    }


                    else if (chooseOneSelect == 0) {
                        Toast.makeText(CreateAccountNewFrench.this, "Sélectionner la facture d electricite OU Dessiner le plan de localisation", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        validationElectricityForm();
                    }
                }




                else if (selectNextButton == 4) {
                    if (validationPersonlDetailsForm()) {

                        scrollView_electricity_bill.setVisibility(View.GONE);

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_addressDetails_form.setVisibility(View.VISIBLE);

                        selectNextButton = 5;
                        submitButton_partfirst.setText("Suivant");
                        number_textview_addressDetails.setText(accountNumber);

                    }
                }

                else if (selectNextButton == 5) {

                    if(tla.equalsIgnoreCase(""))
                    {

                        if (validationAddressDetailsForm())
                        {

                            scrollView_electricity_bill.setVisibility(View.GONE);
                            scrollView_PersonalDetails_form.setVisibility(View.GONE);
                            scrollView_addressDetails_form.setVisibility(View.GONE);

                            selectNextButton = 6;
                            submitButton_partfirst.setText("Suivant");
                            scrollView_idDetails_form.setVisibility(View.VISIBLE);
                        }

                    }

                    else {

                        if (validationAddressDetailsForm_agentBranchAutoselected())
                        {

                            scrollView_electricity_bill.setVisibility(View.GONE);
                            scrollView_PersonalDetails_form.setVisibility(View.GONE);
                            scrollView_addressDetails_form.setVisibility(View.GONE);

                            selectNextButton = 6;
                            submitButton_partfirst.setText("Suivant");
                            scrollView_idDetails_form.setVisibility(View.VISIBLE);
                        }

                    }





                } else if (selectNextButton == 6) {
                    if (validationIdDetailsForm()) {


                        number_textview_mpinpage.setText(accountNumber);

                        scrollView_electricity_bill.setVisibility(View.GONE);

                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_idDetails_form.setVisibility(View.GONE);
                        scrollView_addressDetails_form.setVisibility(View.GONE);

                        selectNextButton = 7;
                        submitButton_partfirst.setText("Exécuter");
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);


                    }
                }

                else if (selectNextButton == 7) {

                    if (validationMpin()) {

                        number_textview_mpinpage.setText(accountNumber);
                        scrollView_electricity_bill.setVisibility(View.GONE);
                        scrollView_PersonalDetails_form.setVisibility(View.GONE);
                        scrollView_idDetails_form.setVisibility(View.GONE);
                        scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                        number_textview_uploding.setText(accountNumber);


                        if (selectCameraGalleryMpinSubscriberSign == 0 && humanSignature==3) {
                            createAccountRequest();
                        }

                        else if (selectCameraGalleryMpinSubscriberSign == 0) {
                            Toast.makeText(CreateAccountNewFrench.this, "Sélectionnez le signature du client", Toast.LENGTH_SHORT).show();
                        }


                        else {
                            createAccountRequest();
                        }
                    }
                }

                else if (selectNextButton == 8) {

                    try {

                        Bundle bundle = new Bundle();
                        bundle.putString("data", successReceiptString);

                        String accountType = "CREATE ACCOUNT";

                        mComponentInfo.getmSharedPreferences().edit().putString("data", successReceiptString).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("ACCOUNTTYPE", accountType).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("customerNameString", customerNameString).commit();
                        mComponentInfo.getmSharedPreferences().edit().putString("attachBranchNameString", attachBranchNameString).commit();

                        Intent intent = new Intent(CreateAccountNewFrench.this, SucessReceiptCreateAgentNewUpdateNew.class);
                        intent.putExtra("data", successReceiptString);
                        intent.putExtra("ACCOUNTTYPE", accountType);
                        intent.putExtra("customerNameString", customerNameString);
                        intent.putExtra("attachBranchNameString", attachBranchNameString);
                        startActivity(intent);
                        CreateAccountNewFrench.this.finish();
                    } catch (Exception e) {
                        System.out.println("eroor----------------" + e);
                        e.printStackTrace();
                    }

                } else if (selectNextButton == 13) {  // demo

                    linearlayout_imageview_fullScreen.setVisibility(View.GONE);
                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    selectNextButton = 1;
                    submitButton_partfirst.setText("Suivant");
                    scrollView_customerPicture.setVisibility(View.VISIBLE);

                    if (selectGalleryFullScreen_customer == 1) {
                        imageview_fullScreen.setImageBitmap(selectedImage);
                        selectGalleryFullScreen_customer = 2;
                        tempGallery = 1;
                    } else if (selectCameraFullScreen_customer == 1) {
                        imageview_fullScreen.setImageBitmap(bitmapCameraCustomer);
                        selectCameraFullScreen_customer = 2;
                        tempCamera = 1;
                    }

                }

                break;
        }
    }
   /* private void showSuccessReceiptCreateAgentUpdateAccount(String data) {


        Bundle bundle = new Bundle();
        bundle.putString("data", data);

        String accountType="CREATE ACCOUNT";

        mComponentInfo.getmSharedPreferences().edit().putString("data", data).commit();
        mComponentInfo.getmSharedPreferences().edit().putString("ACCOUNTTYPE", accountType).commit();

        Intent intent = new Intent(CreateAccountNew.this, SucessReceiptCreateAgentNewUpdateNew.class);
        intent.putExtra("data", data);
        intent.putExtra("ACCOUNTTYPE", accountType);
        startActivity(intent);
        CreateAccountNew.this.finish();
    }
*/


    public void validateDetailsFirst() {
        if (validateDetailsPartFirst()) {

            AgentIdentity();

        }
    }

    void validationPageCustomerPicture() {
        scrollView_customerPicture.setVisibility(View.GONE);
        selectNextButton = 2;
        submitButton_partfirst.setText("Suivant");
        scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);

    }

    void zoomInZoomOutPic_SubscriberForm_MpinPage() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateAccountNewFrench.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout_zoomin_zoomout, null);

        photoView = mView.findViewById(R.id.imageView);
        photoView.setImageBitmap( ((BitmapDrawable) imageview_subscriberForm.getDrawable()).getBitmap());


       // customeLayout_subscriberFormImage();

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    void validationPageidDocumentPicture() {
        scrollView_customerPicture.setVisibility(View.GONE);
        // Toast.makeText(CreateAccountNew.this, "Go Next layout", Toast.LENGTH_SHORT).show();
        scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
        scrollView_electricity_bill.setVisibility(View.VISIBLE);

        selectNextButton = 3;
        submitButton_partfirst.setText("Suivant");
        billElectricityBackButton=1;


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
                        Toast.makeText(CreateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


                ret = true;

                spinnerAccountToDebitString = spinnerAccountToDebit.getSelectedItem().toString();
                accountCodeString = payerAccountCodeArray[spinnerAccountToDebit.getSelectedItemPosition()];


            } else {
                Toast.makeText(CreateAccountNewFrench.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();

            }


        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez selectionner le pays", Toast.LENGTH_LONG).show();

        }


        return ret;


    }

    void validationElectricityForm() {
        scrollView_electricity_bill.setVisibility(View.GONE);
        scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
        number_textview_personalDetails.setText(accountNumber); /////

        selectNextButton = 4;
        submitButton_partfirst.setText("Suivant");
       // selectSignaurepic=2;
    }


    private void createGetPlans() {
        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateDataGetPlans();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

           // new ServerTask(mComponentInfo, CreateAccountNew.this, mHandler, requestData, "getPlans", 156).start();

            vollyRequestApi_serverTask("getPlans",requestData, 156);

        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    /*void callApi(String apiName, final String body, final int requestCode){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.0.224:9090/RESTfulWebServiceEU/json/estel/";

            final String requestBody = body.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL+apiName, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    DataParserThread agent.thread = new DataParserThread(CreateAccountNew.this,mComponentInfo,CreateAccountNew.this,requestCode,response.toString());

                    agent.thread.execute();

                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : body.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/


    private String generateDataGetPlans() {
        String jsonString = "";

          /*  {"agentCode":"23333336988869","requestcts":"25/05/2016 18:01:51","vendorcode":
            "MICR","clienttype":"GPRS","syncstatus":"1","agenttype":"SUB"}
*/

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("requestcts", "");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");
            countryObj.put("syncstatus", "1");    //  according to Bheem hardCode Set // 18072017
            //   countryObj.put("agenttype", "SUB");   // according to Bheem hardCode Set // 18072017


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private void createAccountRequest() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateDataCreateAccount();

           // new ServerTask(mComponentInfo, CreateAccountNew.this, mHandler, requestData, "CreateAgentInJSON", 131).start();

            vollyRequestApi_serverTask("CreateAgentInJSON",requestData, 131);


        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadImageRequest_agentimage_fail() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_agentimage();


            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 206).start();




        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImageRequest_agentimage() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_agentimage();


            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 186).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_agentimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", accountNumber);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", uploadCameraStringCustomerPicture);
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


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_signature_fail() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateUploadImage_signatuere();

           new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 210).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageRequest_signatuere() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_signatuere();

            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 187).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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
            countryObj.put("signature", uploadCameraStringSignature);
            countryObj.put("idfrontimage", "");
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");



        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_idfrontimage_fails() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idfrontimage();




            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 207).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImageRequest_idfrontimage() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idfrontimage();



           new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 188).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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
            countryObj.put("idfrontimage", uploadGaleryIddocumentFrontString);
            countryObj.put("idbackimage", "");
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }
    private void uploadImageRequest_idbackimage_fail() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idbackimage();



            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 208).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }



    private void uploadImageRequest_idbackimage() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_idbackimage();

            //  callApi("uploadimage",requestData,189);


            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 189).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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
            countryObj.put("idbackimage", cameraGalleryIDDocBackString);
            countryObj.put("billimage", "");
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

    private void uploadImageRequest_billimage_fail() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_billimage();


            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 209).start();




        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImageRequest_billimage() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_billimage();


            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 190).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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
            countryObj.put("billimage", uploadGalleryStringElectricity);
            countryObj.put("formimage", "");
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }


    private void uploadImageRequest_formimage_fail() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_formimage();

            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 211).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageRequest_formimage() {


        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateUploadImage_formimage();

            new ServerTaskFTP(mComponentInfo, CreateAccountNewFrench.this, mHandler, requestData, "uploadimage", 191).start();



        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_formimage() {
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
            countryObj.put("billimage", "");
            countryObj.put("formimage", subscriberformString);   // temporary add form image
            countryObj.put("requestcts", "");


            jsonString = countryObj.toString();

            //  jsonString = jsonString.toString();


            String tempRequestData = jsonString.replaceAll("\\\\", "*");
            String tempRequestData2 = tempRequestData.replaceAll("\\*n", "");
            finalDataJsonString = tempRequestData2.replaceAll("\\*", "");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDataJsonString;
    }

/*
    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread agent.thread = new DataParserThread(AccountBalanceSpinner.this,mComponentInfo,AccountBalance.this,requestCode,response.toString());

                            agent.thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(AccountBalance.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(AccountBalance.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

    }
*/


    private void AgentIdentity() {

        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateDataAgentIdentity();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;


           // new ServerTask(mComponentInfo, CreateAccountNew.this, mHandler, requestData, "getAgentIdentity", 193).start();

            vollyRequestApi_serverTask("getAgentIdentity",requestData, 193);


        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void cityStateList() {

        if (new InternetCheck().isConnected(CreateAccountNewFrench.this)) {
            showProgressDialog("Veuillez patienter");
            String requestData = generateCityData();

          //  new ServerTask(mComponentInfo, CreateAccountNew.this, mHandler, requestData, "getStateListInJSON", 194).start();

            vollyRequestApi_serverTask("getStateListInJSON",requestData, 194);


        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez verifier votre connexion Internet", Toast.LENGTH_LONG).show();
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


                            DataParserThread thread = new DataParserThread(CreateAccountNewFrench.this,mComponentInfo,CreateAccountNewFrench.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(CreateAccountNewFrench.this, "Veuillez réessayer plus tard", Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(CreateAccountNewFrench.this,"Veuillez réessayer plus tard", Toast.LENGTH_LONG).show();

        }

    }





    // {"agentCode":"237931349121","customerid":"ASDFSF65120320","requestcts":"25/05/2016 18:01:51","vendorcode":"MICR","clienttype":"GPRS","transtype":"CREATEAGENT"}


    private String generateDataAgentIdentity() {
        String jsonString = "";

        try {

            JSONObject countryObj = new JSONObject();

            //  countryCodePrefixString = getCountryPrefixCode();
            //   accountNumber = getCountryPrefixCode() + accountNumber;

            countryObj.put("agentCode", accountNumber);            // verify check account number
            countryObj.put("transtype", "CREATEAGENT");
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

        }
        return jsonString;
    }


    private String generateDataCreateAccount() {
        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {


            JSONObject countryObj = new JSONObject();

            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("requestcts", "");
            countryObj.put("source", accountNumber);
            countryObj.put("language", LanguageString);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("nationality", nationalityCodeString);
            countryObj.put("address", addressString);
            countryObj.put("country", countryCodeArray[getCountrySelection()]);
            countryObj.put("email", emailString);                  // no ui in KYC page

            idProofIssuDateString = idProofIssuDateString + " 00:00:00";
            dateOfBirthString = dateOfBirthString + " 23:59:59";
            countryObj.put("idproofissuedate", idProofIssuDateString);

            countryObj.put("dob", dateOfBirthString);
            countryObj.put("idproofissueplace", idissuePlaceString);
            countryObj.put("clienttype", "GPRS");
            countryObj.put("comments", "");
            countryObj.put("birthplace", birthpalceString);


            // ----------------------- Second  Mobile numbrt ---------------------------------

            if(secondMobileNumberString.equalsIgnoreCase(""))
            {
                countryObj.put("secondmobphoneno", "");
            }

            else{
                countryCodePrefixString = getCountryPrefixCode();
                secondMobileTempString = countryCodePrefixString + secondMobileNumberString;
                countryObj.put("secondmobphoneno", secondMobileTempString);
            }

            // -----------------------fixed Mobile numbrt ---------------------------------
            if(fixPhonenoString.equalsIgnoreCase(""))
            {
                countryObj.put("fixphoneno", fixPhonenoString);
            }

            else {
                countryCodePrefixString = getCountryPrefixCode();
                fixMobiletempNumber = countryCodePrefixString + fixPhonenoString;
                countryObj.put("fixphoneno", fixMobiletempNumber);
            }


            countryObj.put("idproofduedate", idproofDueDate);
            countryObj.put("profession", professionCodeString);
            countryObj.put("gender", genderCodeString);
            countryObj.put("customerid", customerIdString);
            countryObj.put("residencearea", residenceAreaString);
            countryObj.put("idprooftype", idProofCodeString);
            countryObj.put("idproof", idproofNameString);
            countryObj.put("accountname", customerNameString);
            countryObj.put("city", cityString);
            countryObj.put("state", cityString);
            countryObj.put("plancode", planNameString);
            countryObj.put("agenttype", agentTypeString);


            if(tlacode.equalsIgnoreCase(""))
            {
                countryObj.put("branch", branchCodeString);
            }
            else {
                countryObj.put("branch", tlacode);
            }

            countryObj.put("parent", parentfixedBAString);


            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


    private boolean validationMpin() {
        boolean ret = false;
        mpinString = mpinEditText.getText().toString().trim();

        if (checkBox_termendCondition.isChecked()) {
        if (mpinString.length() == 4) {
            ret = true;
        } else {
            Toast.makeText(CreateAccountNewFrench.this, "Saisir le code secret à 4 chiffres", Toast.LENGTH_LONG).show();
        }
        } else {
            Toast.makeText(CreateAccountNewFrench.this, "J accepte les termes et conditions", Toast.LENGTH_LONG).show();
        }

        return ret;
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;


        if (generalResponseModel.getResponseCode() == 0) {


            if (requestNo == 131) {

                successReceiptString = generalResponseModel.getUserDefinedString();
                selectNextButton = 8;

                submitButton_partfirst.setText("Montrer le recu");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_uploading_form_showreceipt.setVisibility(View.VISIBLE);


                getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // hide back button
                mToolbar.setTitle("        " + "CREATION DE COMPTE");
                mToolbar.setSubtitle("          " + agentName);


                uploadImageRequest_agentimage();

            } else if (requestNo == 186) {                  // agent Image  // First Time Success
                green_imageview_personalImage.setVisibility(View.VISIBLE);
                green_imageview_personalImage.setImageDrawable(getResources().getDrawable(R.drawable.greentic));

                uploadImageRequest_signatuere();
            } else if (requestNo == 187) {              // signature / Subscription Form // First Time Success

                green_imageview_signature.setVisibility(View.VISIBLE);
                green_imageview_signature.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_idfrontimage();
            } else if (requestNo == 188) {              // id front image  // First Time Success
                green_imageview_idfront.setVisibility(View.VISIBLE);
                green_imageview_idfront.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_idbackimage();
            } else if (requestNo == 189) {              // id Back image  // First Time Success
                green_imageview_idback.setVisibility(View.VISIBLE);
                green_imageview_idback.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_billimage();
            } else if (requestNo == 190) {          // Bill Location   // First Time Success

                green_imageview_billlocation.setVisibility(View.VISIBLE);
                green_imageview_billlocation.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_formimage();
            } else if (requestNo == 191) {          // Form    // First Time Success

                green_imageview_form.setVisibility(View.VISIBLE);
                green_imageview_form.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
            }

            //--------------------- if failed First Time then Request Second Time  ---------------------------------------------------------------

            else if (requestNo == 206) {
                green_imageview_personalImage.setVisibility(View.VISIBLE);  // display agent image
                green_imageview_personalImage.setImageDrawable(getResources().getDrawable(R.drawable.greentic));

                uploadImageRequest_signatuere();
            }

            else if (requestNo == 210) {

                green_imageview_signature.setVisibility(View.VISIBLE);
                green_imageview_signature.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_idfrontimage();
            }


            else if (requestNo == 207) {
                green_imageview_idfront.setVisibility(View.VISIBLE);
                green_imageview_idfront.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_idbackimage();
            }
            else if (requestNo == 208) {
                green_imageview_idback.setVisibility(View.VISIBLE);
                green_imageview_idback.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_billimage();
            } else if (requestNo == 209) {

                green_imageview_billlocation.setVisibility(View.VISIBLE);
                green_imageview_billlocation.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
                uploadImageRequest_formimage();
            }

            else if (requestNo == 211) {

                green_imageview_personalImage.setVisibility(View.VISIBLE);
                green_imageview_personalImage.setImageDrawable(getResources().getDrawable(R.drawable.greentic));
            }





            else if (requestNo == 193) {

                try {


                    SharedPreferences prefs = getSharedPreferences("PROFILEDETAILS", MODE_PRIVATE);

                    //  profile

                    String profile = prefs.getString("profiles", null);

                    String[] tempData_profile = profile.split("\\$");

                    String agenttype = tempData_profile[1];
                    agenttypeArray = agenttype.split("\\|");

                    String profilename = tempData_profile[2];
                    profilenameArray = profilename.split("\\|");

                    String plancode = tempData_profile[3];
                    plancodeArray = plancode.split("\\|");

                    //  Branch


                    String branches = prefs.getString("branches", null);

                    String[] tempData_branches = branches.split("\\$");

                    String code = tempData_branches[1];
                    codeProfileArray = code.split("\\|");

                    String fixedBACode = tempData_branches[2];
                    fixedBACodeArray = fixedBACode.split("\\|");

                    String name = tempData_branches[3];
                    attachBranchNameCode = name.split("\\|");

                   // tla="";        // demo
                   // tlacode="";   // demo

                    attachBranchNameCode[0]="Nom de la filiale"; // set English French

                    if (tla.equalsIgnoreCase(""))   //
                    {
                        spinnerAttachedBranchName = (Spinner) findViewById(R.id.spinnerAttachedBranchName);
                        spinnerAttachedBranchName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, attachBranchNameCode));
                        spinnerAttachedBranchName.setOnItemSelectedListener(this);
                    }
                    else {
                        attachBranchNameCode[0] = tla;   // Default Set From Server
                        spinnerAttachedBranchName = (Spinner) findViewById(R.id.spinnerAttachedBranchName);
                        spinnerAttachedBranchName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, attachBranchNameCode));
                        spinnerAttachedBranchName.setOnItemSelectedListener(this);
                    }


                    //  id proofs

                    String idproofs = prefs.getString("idproofs", null);
                    String[] tempData_idproofs = idproofs.split("\\$");

                    String codeIdproof = tempData_idproofs[1];
                    idProofCodeArray = codeIdproof.split("\\|");

                    String nameIDproof = tempData_idproofs[2];
                    idProofArray = nameIDproof.split("\\|");
                    spinnerIdProofType = (Spinner) findViewById(R.id.spinner_idProofType);

                    idProofArray[0]="Type de piéce";
                    spinnerIdProofType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, idProofArray));
                    spinnerIdProofType.setOnItemSelectedListener(this);

                    profilenameArray[0]="Profile du compte";

                    spinnerPlanAccountProfile.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, profilenameArray));

                    scrollView_createAgent_firstPart.setVisibility(View.GONE);
                    selectNextButton = 1;
                    submitButton_partfirst.setText("Suivant");
                    scrollView_customerPicture.setVisibility(View.VISIBLE);

                    cityStateList();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateAccountNewFrench.this,"Veuillez réessayer plus tard",Toast.LENGTH_LONG).show();
                }


                //setInputType(1);
            } else if (requestNo == 194) {

                try {


                    String cityStateDetails = generalResponseModel.getUserDefinedCityCreateAgent();

                    if (cityStateDetails.equalsIgnoreCase("$ CITY  $CITY Code "))   // if country List Is Null
                    {
                        Toast.makeText(CreateAccountNewFrench.this, "" + "Le code du pays est vide", Toast.LENGTH_SHORT).show();

                    } else {

                        String[] cityListDetails = cityStateDetails.split("\\$");

                        String cityDetails = cityListDetails[1];
                        cityDetailsArray = cityDetails.split("\\|");

                        String cityCode = cityListDetails[2];
                        cityCodeArray = cityCode.split("\\|");

                        cityDetailsArray[0]="Ville";
                        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
                        spinnerCity.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityDetailsArray));
                        spinnerCity.setOnItemSelectedListener(this);

                        keyboardSelection = 1;
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateAccountNewFrench.this,"Veuillez réessayer plus tard",Toast.LENGTH_LONG).show();

                }

            }

        } else {     // ##########################  Else Part  ####################################################


            if (requestNo == 186) {
                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_personalImage.setVisibility(View.VISIBLE);
                    green_imageview_personalImage.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_agentimage_fail();    // Agent Image (206)  // if First Time Transaction fail  Second Time (206)
                } else {
                    uploadImageRequest_signatuere();
                }
            }


            else if (requestNo == 187) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_signature.setVisibility(View.VISIBLE);
                    green_imageview_signature.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_signature_fail();   // signature / Subscription Form   // if First Time Transaction fail  Second Time (210)
                } else {
                    uploadImageRequest_idfrontimage();
                }
            } else if (requestNo == 188) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_idfront.setVisibility(View.VISIBLE);
                    green_imageview_idfront.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_idfrontimage_fails();        //  ID Front Image  (207)  // if First Time Transaction fail  Second Time (207)

                } else {
                    uploadImageRequest_idbackimage();
                }
            } else if (requestNo == 189) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_idback.setVisibility(View.VISIBLE);
                    green_imageview_idback.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_idbackimage_fail();        //  ID Back Image (208)  // if First Time Transaction fail  Second Time (208)

                } else {
                    uploadImageRequest_billimage();
                }
            } else if (requestNo == 190) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_billlocation.setVisibility(View.VISIBLE);
                    green_imageview_billlocation.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_billimage_fail();           //  Bill  Image (209)  // if First Time Transaction fail  Second Time (209)
                } else {
                    uploadImageRequest_formimage();
                }
            } else if (requestNo == 191) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_form.setVisibility(View.VISIBLE);
                    green_imageview_form.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_formimage_fail();       //  Form   Image (211)  // if First Time Transaction fail  Second Time (211)
                } else {
                    Toast.makeText(CreateAccountNewFrench.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                }
            }


            // ##########################  2nd Time Call  If Transaction Fail ####################################################


            else if (requestNo == 206) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_personalImage.setVisibility(View.VISIBLE);
                    green_imageview_personalImage.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_signatuere();
                } else {
                    uploadImageRequest_signatuere();
                }
            }

            else if (requestNo == 207) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_idfront.setVisibility(View.VISIBLE);
                    green_imageview_idfront.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_idbackimage();
                } else {
                    uploadImageRequest_idbackimage();
                }
            }

            else if (requestNo == 208) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_idback.setVisibility(View.VISIBLE);
                    green_imageview_idback.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_billimage();
                } else {
                    uploadImageRequest_billimage();
                }

            }

            else if (requestNo == 209) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_billlocation.setVisibility(View.VISIBLE);
                    green_imageview_billlocation.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_formimage();
                } else {
                    uploadImageRequest_formimage();
                }
            }

            else if (requestNo == 210) {

                if (!generalResponseModel.getUserDefinedString().contains("Transaction Successful")) {
                    green_imageview_signature.setVisibility(View.VISIBLE);
                    green_imageview_signature.setImageDrawable(getResources().getDrawable(R.drawable.arrowred));

                    uploadImageRequest_idfrontimage();
                } else {
                    uploadImageRequest_idfrontimage();
                }



            // ########################## End 2nd Time Call  If Transaction Fail ####################################################

        } else {
                hideProgressDialog();
                Toast.makeText(CreateAccountNewFrench.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
                // Subscriber/Agent Not Found
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] data;

        //  data = accountsArrayDetailsList.get(position).toString().split("\\|");

        Toast.makeText(CreateAccountNewFrench.this, "Listview Click Lis", Toast.LENGTH_SHORT).show();


        // showPreConfirmationPopup(data);


    }


    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            CreateAccountNewFrench.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(CreateAccountNewFrench.this);
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

        try {

            System.out.println(selectNextButton); //
            System.out.println(backButtonSelectTermEndCondition);
            System.out.println(selectSignaurepic);
            System.out.println(humanSignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectElectricitySignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectBillHomeSignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectMpinSignature);  // 7023   backButtonSignMpin    // term end condition click   7123

        } catch (Exception e)  //702110
        {
            e.printStackTrace();
        }


        if (menuItem.getItemId() == android.R.id.home) {

            if (selectNextButton == 7 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 3) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);
                selectNextButton = 7;

                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                backButtonSelectTermEndCondition = 2;
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 2) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                scrollView_customerPicture.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 0 && humanSignature == 0 && backButtonBillHomeElectricity == 1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);

                backButtonBillHomeElectricity = 2;
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 0 && backButtonBillHomeElectricity == 3) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);

                backButtonBillHomeElectricity = 4;
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && selectSignaurepic == 2) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 3;
                submitButton_partfirst.setText("Suivant");
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 1 && humanSignature == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 0 && humanSignature == 0 && billElectricityBackButton == 1) {

                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

                billElectricityBackButton = 2;  // set Display Electricity Bill Page
            } else if (selectNextButton == 3 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 0 && humanSignature == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && selectSignaurepic == 1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 2;
                submitButton_partfirst.setText("Suivant");
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 3 && selectSignaurepic == 1) {
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);

                selectNextButton = 3;
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
            } else if (selectNextButton == 2 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 1 && humanSignature == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && humanSignature == 0 && backButtonSelectTermEndCondition == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 3 && humanSignature == 0 && backButtonSelectTermEndCondition == 0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            } else if (selectNextButton == 0) {

                Intent intent = new Intent(CreateAccountNewFrench.this, AccountsMenu.class);
                startActivity(intent);
                CreateAccountNewFrench.this.finish();

            } else if (selectNextButton == 1) {

                selectNextButton = 0;
                scrollView_customerPicture.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");

                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

                keyboardSelection=0;

            } else if (selectNextButton == 2) {  // demo

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 1;

                submitButton_partfirst.setText("Suivant");
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 3) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                submitButton_partfirst.setText("Suivant");
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 4) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 3;
                submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 4;
                submitButton_partfirst.setText("Suivant");
                scrollView_addressDetails_form.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 6) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 5;
                submitButton_partfirst.setText("Suivant");
                scrollView_idDetails_form.setVisibility(View.GONE);
                scrollView_addressDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 7 && backButtonSelectTermEndCondition == 1) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 7;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                backButtonSelectTermEndCondition = 2;
            } else if (selectNextButton == 7 && backButtonSelectTermEndCondition == 2) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 6;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_idDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 7 && backButtonSelectTermEndCondition == 0 && selectSignaurepic == 2 && humanSignature == 1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);
                selectNextButton = 7;

                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 7) {


                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 6;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_idDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else {

                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);

                selectNextButton = 0;
                scrollView_customerPicture.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");

                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            }


            // CreateAccountNew.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        try {

            System.out.println(selectNextButton); //
            System.out.println(backButtonSelectTermEndCondition);
            System.out.println(selectSignaurepic);
            System.out.println(humanSignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectElectricitySignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectBillHomeSignature);  // 7023   backButtonSignMpin    // term end condition click   7123
            System.out.println(noSelectMpinSignature);  // 7023   backButtonSignMpin    // term end condition click   7123

        }
        catch (Exception e)  //702110
        {
            e.printStackTrace();
        }


        if (menuItem.getItemId() == android.R.id.home) {

            if(selectNextButton==7 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==3) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 7;

                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                backButtonSelectTermEndCondition = 2;
            }

              else  if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }

            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==2) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                 scrollView_customerPicture.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }


            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==0 && humanSignature==0 && backButtonBillHomeElectricity==1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);

                backButtonBillHomeElectricity=2;
            }

            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==0 && backButtonBillHomeElectricity==3 ) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);

                backButtonBillHomeElectricity=4;
            }



            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                 scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }



            else if(selectNextButton==3 && selectSignaurepic==2) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 3;
                submitButton_partfirst.setText("Suivant");
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
            }

            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==1 && humanSignature==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }

            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==0 && humanSignature==0 && billElectricityBackButton==1) {

                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

                billElectricityBackButton=2;  // set Display Electricity Bill Page
            }

            else if(selectNextButton==3 && backButtonSelectTermEndCondition==0 && selectSignaurepic==0 && humanSignature==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);

                selectNextButton = 3;
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }

            else if(selectNextButton==3 && selectSignaurepic==1) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 2;
                submitButton_partfirst.setText("Suivant");
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }

          else  if(selectNextButton==3 && selectSignaurepic==1) {
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.GONE);

                selectNextButton = 3;
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
            }


            else if(selectNextButton==2 && backButtonSelectTermEndCondition==0 && selectSignaurepic==1 && humanSignature==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }

            else if(selectNextButton==3 && humanSignature==0 && backButtonSelectTermEndCondition==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }


            else if(selectNextButton==3 && humanSignature==0 && backButtonSelectTermEndCondition==0) {

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                submitButton_partfirst.setText("Suivant");
                scrollView_createAgent_firstPart.setVisibility(View.GONE);
            }


            else if (selectNextButton == 0) {

                Intent intent = new Intent(CreateAccountNewFrench.this, AccountsMenu.class);
                startActivity(intent);
                CreateAccountNewFrench.this.finish();

            } else if (selectNextButton == 1) {

                selectNextButton = 0;
                scrollView_customerPicture.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");

                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

                keyboardSelection=0;

            } else if (selectNextButton == 2) {  // demo

                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);

                selectNextButton = 1;

                submitButton_partfirst.setText("Suivant");
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_customerPicture.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }

            else if (selectNextButton == 3) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 2;
                submitButton_partfirst.setText("Suivant");
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 4) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 3;
                submitButton_partfirst.setText("Suivant");
                scrollView_PersonalDetails_form.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 5) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 4;
                submitButton_partfirst.setText("Suivant");
                scrollView_addressDetails_form.setVisibility(View.GONE);
                scrollView_PersonalDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            } else if (selectNextButton == 6) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 5;
                submitButton_partfirst.setText("Suivant");
                scrollView_idDetails_form.setVisibility(View.GONE);
                scrollView_addressDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            } else if (selectNextButton == 7 && backButtonSelectTermEndCondition == 1) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 7;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
                backButtonSelectTermEndCondition = 2;
            } else if (selectNextButton == 7 && backButtonSelectTermEndCondition == 2) {
                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                selectNextButton = 6;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_idDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }

            else if(selectNextButton==7 && backButtonSelectTermEndCondition==0 && selectSignaurepic==2 && humanSignature==1) {

                    scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                    scrollView_customerPicture.setVisibility(View.GONE);
                    scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                    scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                    scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                    scrollView_electricity_bill.setVisibility(View.GONE);

                    selectNextButton = 7;

                    scrollView_button_firstPart.setVisibility(View.VISIBLE);
                    submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.VISIBLE);

                }


            else if (selectNextButton == 7) {


                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);

                selectNextButton = 6;
                submitButton_partfirst.setText("Suivant");
                scrollView_mpin_Page_linearLayout.setVisibility(View.GONE);
                scrollView_idDetails_form.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);
            }

            else
            {

                scrollView_signatureHuman_mpin_page.setVisibility(View.GONE);
                scrollView_signatureHuman_electricityBill.setVisibility(View.GONE);
                scrollView_idDocumentPicture_Page.setVisibility(View.GONE);
                scrollView_electricity_bill.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);
                scrollView_signatureHuman_drawHome.setVisibility(View.GONE);

                selectNextButton = 0;
                scrollView_customerPicture.setVisibility(View.GONE);
                submitButton_partfirst.setText("Suivant");

                scrollView_createAgent_firstPart.setVisibility(View.VISIBLE);
                scrollView_button_firstPart.setVisibility(View.VISIBLE);

            }


            // CreateAccountNew.this.finish();

        }
        return super.onOptionsItemSelected(menuItem);
    }*/

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
            Toast.makeText(CreateAccountNewFrench.this, "Veuillez entrer un identifiant email valide", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
     //---------------------------------------------   Image Human Signature  --------------------------------------------------------------

    public boolean addJpgSignatureToGallery(Bitmap signature,String selectionType) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            signSignatureHuman(photo,selectionType);
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

    void signSignatureHuman(File photo,String selectionType) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photo);


        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


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
        if(selectionType.equalsIgnoreCase("BillElectricity"))
        {
            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_bill_electricityBill.setImageBitmap(selectedImage);
            imageview_preview_drawHome.setImageDrawable(null);
        }

        else if(selectionType.equalsIgnoreCase("DRAWHOME"))
        {
            uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_drawHome.setImageBitmap(selectedImage);
            imageview_bill_electricityBill.setImageDrawable(null);
        }

        else if(selectionType.equalsIgnoreCase("MPINPAGE"))
        {
            uploadCameraStringSignature = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageview_preview_mpinPage.setImageBitmap(selectedImage);
        }



       // ---------------------

        mediaScanIntent.setData(uri);
        CreateAccountNewFrench.this.sendBroadcast(mediaScanIntent);
    }


// ---------------------------------------------------------------------------------------------------------------------------------------

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
