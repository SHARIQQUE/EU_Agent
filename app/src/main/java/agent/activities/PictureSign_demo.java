package agent.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import adapter.CountryFlagAdapter;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static agent.activities.Base64.encode;

/**
 * Created by Sahrique on 14/03/17.
 */

public class PictureSign_demo extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {

    String[] bankSelectionArray, transferTagArray, operationCodeArray, accountTypeArray;
    byte[]   byteArrayUpload;
    Toolbar mToolbar;
    String uploadUserImageString,uploadSignatureImageString;
    Bitmap mBitmap;
    byte[] imageToByteProfile,imageToBytesignature;
    private static final int SELECT_PICTURE = 100;
    ComponentMd5SharedPre mComponentInfo;
    Uri selectedImageUriProfilPic;
    String agentName, spinnerTransactionTypeString, confCodeString, agentCode, spinnerCountryString, transferBasisString, subscriberNoString,
            amountString, spinnerAccountToDebitString, countrySelectionString = "", accountCodeString;
    View viewForContainer;
    int selectPic;
    boolean boolselectUploadPic=false;
    boolean boolselectUploadSignaturePic=false;
    ImageView    imageView_userimage_signature,imageView_userImage;
    Button button_firstPageSubmit, previousButton,button_secondPageSubmit_upload;
    boolean isReview, isServerOperationInProcess;
    Dialog successDialog;
    int transferCase, accToAccLevel = 0;
    private Spinner transferBasisSpinner,spinnerCountry, spinnerTransactionType;
    private ScrollView input_SV_AccToCash, review_SV_AccToCash;
    private AutoCompleteTextView subscriberNumberEditText, confcode_EditText_resendSms;
    private TextView titleTextView,languageReview,idproofReview,subscriberNameReview;
    ImageView imageViewPicture, imageViewSign;
    InputStream imageStream;

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
                DataParserThread thread = new DataParserThread(PictureSign_demo.this, mComponentInfo, PictureSign_demo.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };
    private String[] countryArray, countryCodeArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, recipientBankAccountsArray, payerAccountCodeArray;

    //---------------------------

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
                validateDetails();
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



        setContentView(R.layout.picture_sign);

        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_resendSms);
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
       // countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");

        SharedPreferences prefs = getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);

        // mToolbar.setNavigationIcon(R.drawable.franceflag);
        mToolbar.setTitle(getString(R.string.pictureSign));
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

        } catch (Exception e) {

            PictureSign_demo.this.finish();
        }

        button_firstPageSubmit = (Button) findViewById(R.id.button_firstPageSubmit);
        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);
        button_firstPageSubmit.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        button_firstPageSubmit.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);

        button_secondPageSubmit_upload = (Button) findViewById(R.id.button_secondPageSubmit_upload);
        button_secondPageSubmit_upload.setOnClickListener(this);




      /*  submitButton = (Button) findViewById(R.id.submit_ResendSms);
        submitButton.setOnClickListener(this);*/


        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        CountryFlagAdapter adapter = new CountryFlagAdapter(countryArray, getResources(), getLayoutInflater());
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setEnabled(false);
        spinnerCountry.setSelection(getCountrySelection());
        spinnerCountry.requestFocus();
        spinnerCountry.setOnItemSelectedListener(this);

        operationCodeArray = getResources().getStringArray(R.array.TxnTypeSmsConfcode);
        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);
        subscriberNumberEditText = (AutoCompleteTextView) findViewById(R.id.subscriberNumberEditText);
        subscriberNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));
        titleTextView = (TextView) findViewById(R.id.titleTextViewResendSmsDetails);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        imageViewSign = (ImageView) findViewById(R.id.imageViewSign);

        languageReview = (TextView) findViewById(R.id.languageReview);
        idproofReview = (TextView) findViewById(R.id.idproofReview);
        subscriberNameReview = (TextView) findViewById(R.id.subscriberNameReview);


        confcode_EditText_resendSms = (AutoCompleteTextView) findViewById(R.id.confcode_EditText_resendSms);
        review_SV_AccToCash = (ScrollView) findViewById(R.id.review_SV_AccToCash);
        input_SV_AccToCash = (ScrollView) findViewById(R.id.input_SV_AccToCash);

        subscriberNumberEditText.setOnEditorActionListener(this);

        imageView_userimage_signature=(ImageView)findViewById(R.id.imageView_userimage_signature) ;
        imageView_userImage=(ImageView)findViewById(R.id.imageView_userImage) ;

        imageView_userimage_signature.setClickable(true);
        imageView_userImage.setClickable(true);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(PictureSign_demo.this);




        imageView_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(),"image User",Toast.LENGTH_LONG).show();
                selectPic =0;

                openImageChooser();
            }
        });

        imageView_userimage_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPic =1;
                openImageChooser();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.spinnerCountry:
                //  setInputType(1);

                setInputType(transferBasisSpinner.getSelectedItemPosition());
                break;

            case R.id.spinnerTransactionType:
                setInputType(i);
                break;

            case R.id.spinnerSendMode_AccToCash:
                setInputType(i);


        }
    }


    private void setInputType(int i) {

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            if (i == 1) {
                subscriberNumberEditText.setText("");
                subscriberNumberEditText.setHint(getString(R.string.PleasEenterMobileNumber));
                // subscriberNumberEditText.setFilters(null);
                subscriberNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                subscriberNumberEditText.setHint(String.format(getString(R.string.hintMobileNoAcctoCashOut), countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()] + " "));
                subscriberNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                subscriberNumberEditText.setFilters(digitsfilters);
                subscriberNumberEditText.setText("");


            } else if (i == 2) {
                subscriberNumberEditText.setText("");
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
                subscriberNumberEditText.setHint(getString(R.string.pleaseentername));
                subscriberNumberEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                subscriberNumberEditText.setFilters(digitsfilters);
                subscriberNumberEditText.setText("");
            }
        } else {
            Toast.makeText(PictureSign_demo.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }




    int transferBasisposition = 0;

    private boolean validationSignPicture() {
        boolean ret = false;

        if (spinnerCountry.getSelectedItemPosition() != 0) {
            spinnerCountryString = spinnerCountry.getSelectedItem().toString();

            transferBasisposition = spinnerCountry.getSelectedItemPosition();
            int transferBasisposition = transferBasisSpinner.getSelectedItemPosition();


            int lengthToCheck = 3;
            String errorMsgToDisplay = "";

            if (transferBasisposition == 1) {
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");

            } else {
               /* transferBasisString = "Recipient Name";
                lengthToCheck = 2;
                errorMsgToDisplay = getString(R.string.receipentname);*/
                transferBasisString = "Mobile Number";
                lengthToCheck = Integer.parseInt(countryMobileNoLengthArray[spinnerCountry.getSelectedItemPosition()]) - 1;
                errorMsgToDisplay = String.format(getString(R.string.hintMobileNoAcctoCashOut), lengthToCheck + 1 + "");
            }
            subscriberNoString = subscriberNumberEditText.getText().toString().trim();
            if (subscriberNoString.length() > lengthToCheck) {
                if (transferBasisposition == 1) {
                    if (subscriberNoString.length() == ++lengthToCheck) {
                        subscriberNoString = countryPrefixArray[spinnerCountry.getSelectedItemPosition()] + subscriberNoString;
                    } else {
                        Toast.makeText(PictureSign_demo.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                //if (spinnerTransactionType.getSelectedItemPosition() != 0) {

                ret = true;


                   /* } else {
                        Toast.makeText(PictureSign.this, getString(R.string.PleaseSelectTransactionType), Toast.LENGTH_LONG).show();
                    }
                    */

            } else {
                Toast.makeText(PictureSign_demo.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(PictureSign_demo.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
        }
        return ret;
    }

    public void validateDetails() {

        if (validationSignPicture()) {
            ServerRequestPictureSign();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.button_firstPageSubmit:
                hideKeyboard();
                validateDetails();
                break;

            case R.id.button_secondPageSubmit_upload:

                //    hideKeyboard();

                //  serverRequestUploadImage();

                System.out.println(boolselectUploadPic);
                System.out.println(boolselectUploadSignaturePic);

                if(boolselectUploadPic==true || boolselectUploadSignaturePic==true)
                {
                    pictureImageUploadToServer();
                }

                else {
                    Toast.makeText(PictureSign_demo.this,"Plz Upload Picture ",Toast.LENGTH_LONG).show();
                }


                break;

         /*   case R.id.submit_ResendSms:
                validateDetailsSecondPhase();
                break;

            case R.id.imageViewPicture:
                  ClearAll();
                break;*/
        }
    }





    void openImageChooser() {
        Intent intent = new Intent();
        intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                String converted_txt = "";
                if (selectPic == 0)
                {
                    selectedImageUriProfilPic = data.getData();

                    if (null != selectedImageUriProfilPic) {

                        boolselectUploadPic=true;


                        try {
                            imageStream = getContentResolver().openInputStream(selectedImageUriProfilPic);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        System.out.println(selectedImage);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] imageBytes = baos.toByteArray();

                        uploadUserImageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                        System.out.println(uploadUserImageString);

                        imageView_userImage.setImageBitmap(selectedImage);



                        //  System.out.println(imageString);

                        //   uploadUserImageString = encodeImage(imageBytes);


                       /* File imgFile = new  File(selectedImageUriProfilPic.getPath());

                        if(imgFile.exists()){
                            try {
                                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUriProfilPic);

                           } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        imageView_userImage.setImageBitmap(mBitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byteArrayUpload = byteArrayOutputStream .toByteArray();

                        Bitmap resizeBmp = Bitmap.createScaledBitmap(mBitmap, 200, 100, true);
                        int size = resizeBmp.getRowBytes() * resizeBmp.getHeight();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                        resizeBmp.copyPixelsToBuffer(byteBuffer);
                        byte[] b1 =  byteBuffer.array();
                        uploadUserImageString = encodeImage(b1);*/


                        // uploadUserImageString = getStringByByte(uploadUserImageString.getBytes());
                        //  uploadUserImageString = getStringByByte(b1);
                        // Log.e("",""+uploadUserImageString);

                    }
                }
                else {

                    Uri selectedImageUriSignaturePic = data.getData();
                    if (null != selectedImageUriSignaturePic) {

                        selectPic=1;

                        boolselectUploadSignaturePic=true;

                        try {
                            imageStream = getContentResolver().openInputStream(selectedImageUriSignaturePic);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        final Bitmap selectedImageSignature = BitmapFactory.decodeStream(imageStream);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImageSignature.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] imageBytes = baos.toByteArray();

                        uploadSignatureImageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                        System.out.println(uploadSignatureImageString);
                        imageView_userimage_signature.setImageBitmap(selectedImageSignature);


                     /*   imageView_userimage_signature.setImageURI(selectedImageUriSignaturePic);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        mBitmap=((BitmapDrawable)imageView_userimage_signature.getDrawable()).getBitmap();
                        byteArrayUpload = byteArrayOutputStream .toByteArray();
                        Bitmap resizeBmp = Bitmap.createScaledBitmap(mBitmap, 200, 100, true);
                        int size = resizeBmp.getRowBytes() * resizeBmp.getHeight();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                        resizeBmp.copyPixelsToBuffer(byteBuffer);
                        byte[] b1 =  byteBuffer.array();
                        */


                        // uploadSignatureImageString = encodeImage(b1);
                        // uploadSignatureImageString = getStringByByte(uploadSignatureImageString.getBytes());
                        //  uploadUserImageString = getStringByByte(b1);



                    }
                }

            }
        }
    }

//    public String getStringByByte(byte[] bytes){
//        StringBuilder ret  = new StringBuilder();
//        if(bytes != null){
//            for (byte b : bytes) {
//                ret.append(Integer.toBinaryString(b & 255 | 256).substring(1));
//            }
//        }
//        return ret.toString();
//    }

    public String encodeImage(byte[] imageByteArray) {
        return encode(imageByteArray);
    }

    byte[] fromBinary( String s )
    {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for( int i = 0; i < sLen; i++ )
            if( (c = s.charAt(i)) == '1' )
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if ( c != '0' )
                throw new IllegalArgumentException();
        return toReturn;
    }
    public String getStringByByte(byte[] bytes){
        StringBuilder ret  = new StringBuilder();
        if(bytes != null){
            for (byte b : bytes) {
                ret.append(Integer.toBinaryString(b & 255 | 256).substring(1));
            }
        }
        return ret.toString();
    }
    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
                System.out.println(res);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        cursor.close();
        return res;
    }

    private void ServerRequestPictureSign() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(PictureSign_demo.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePictureSign();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;
            new ServerTask(mComponentInfo, PictureSign_demo.this, mHandler, requestData, "getViewProfileInJSON", 146).start();
        } else {
            Toast.makeText(PictureSign_demo.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private void pictureImageUploadToServer() {

        mComponentInfo.getmSharedPreferences().edit().putInt("moneyTransferCase", transferCase).commit();

        if (new InternetCheck().isConnected(PictureSign_demo.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generatePictureSigntoServer();

            String tempRequestData = requestData.replaceAll("\\\\", "");     // remove / back slash by default


            mComponentInfo.getmSharedPreferences().edit().putString("requestData", tempRequestData).commit();
            isServerOperationInProcess = true;
            new ServerTask(mComponentInfo, PictureSign_demo.this, mHandler, tempRequestData, "UploadImagesJson", 168).start();
        } else {
            Toast.makeText(PictureSign_demo.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generatePictureSigntoServer() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", agentCode);
            countryObj.put("source", subscriberNoString);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("pictureimage", uploadUserImageString);
            countryObj.put("signimage",uploadSignatureImageString );


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);


            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    private String generatePictureSign() {
        String jsonString = "";

        SharedPreferences prefs = getSharedPreferences("EU_MPIN", MODE_PRIVATE);
        String pin = prefs.getString("MPIN", null);

        try {

           /* {"agentCode":"237000271016","pin":"46A75E89BC40FDD2F70895AB710D5A22",
            "source":"237000271016","pintype":"IPIN","vendorcode":"MICR","clienttype":"GPRS"}
              */
            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);
            countryObj.put("pin", pin);
            countryObj.put("source", subscriberNoString);
            countryObj.put("pintype", "MPIN");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "GPRS");


            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);

            jsonString = countryObj.toString();

        } catch (Exception e) {
        }
        return jsonString;
    }


    InputStream inputImage, inputSign;
    String[] text = new String[2];
    public static byte[] resultByteArrayImage;
    public static byte[] resultByteArraySign;
    public static byte[][] bytesArray = new byte[2][2];

    String[] responseArray;


    public String[] getStreamFromResponse(String imageBinary, String signBinary) {
        try {
            inputImage = new ByteArrayInputStream(imageBinary.getBytes());
            inputSign = new ByteArrayInputStream(signBinary.getBytes());

            int sizeImageInput = inputImage.available();
            int sizeSignInput = inputSign.available();

            byte[] bufferImage = new byte[sizeImageInput];
            byte[] bufferSign = new byte[sizeSignInput];

            inputImage.read(bufferImage);
            inputImage.close();

            inputSign.read(bufferSign);
            inputSign.close();

            // byte buffer into a string
            text[0] = new String(bufferImage);
            text[1] = new String(bufferSign);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }

    public static byte[] getByteByStringSign(String binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;
        if (binaryStringArrayImageSign.length() % splitSize == 0) {
            // resultByteArraySign = new byte[binaryStringArrayImageSign[1].length() / splitSize];
            resultByteArraySign = new byte[binaryStringArrayImageSign.length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign);
            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArraySign[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArraySign;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign + "' % 8 != 0");
            return null;
        }

    }

    public static byte[] getByteByString(String[] binaryStringArrayImageSign) {
        int splitSize = 8;
        int index = 0;
        int position = 0;
        int i = 0;

        if (binaryStringArrayImageSign[0].length() % splitSize == 0) {
            // resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            resultByteArrayImage = new byte[binaryStringArrayImageSign[0].length() / splitSize];
            StringBuilder text = new StringBuilder(binaryStringArrayImageSign[0]);

            while (index < text.length()) {
                String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
                Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
                resultByteArrayImage[position] = byteAsInt.byteValue();
                index += splitSize;
                position++;
            }
            return resultByteArrayImage;
        } else {
            System.out.println("Cannot convert binary string to byte[], because of the input length. '" + binaryStringArrayImageSign[0] + "' % 8 != 0");
            return null;
        }


    }

    Bitmap bmpImage, bmpSign;

    public void setImage() {
        try {
            bmpImage = BitmapFactory.decodeByteArray(resultByteArrayImage, 0, resultByteArrayImage.length);
            imageViewPicture.setImageBitmap(bmpImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageSign() {
        try {
            bmpSign = BitmapFactory.decodeByteArray(resultByteArraySign, 0, resultByteArraySign.length);
            imageViewSign.setImageBitmap(bmpSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSuccess(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureSign_demo.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.pictureSign));
        responseArray = data.split("\\|");

        TextView agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText(getString(R.string.agentCodeNew) +" : "+ agentCode);

        button_firstPageSubmit.setVisibility(View.GONE);
        input_SV_AccToCash.setVisibility(View.GONE);
        review_SV_AccToCash.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.GONE);

        languageReview.setText(responseArray[0]);

        subscriberNameReview.setText(" -------Subscriber Name Add New------ ");


        //  subscriberName not tag add



        idproofReview.setText(responseArray[4]);
        button_firstPageSubmit.setVisibility(View.GONE);
        button_secondPageSubmit_upload.setVisibility(View.VISIBLE);


        try {
            if (getStreamFromResponse(responseArray[1], responseArray[2]) != null) {
                getByteByString(text);
                setImage();
            } else {
                // Toast.makeText(this, "Image Data is not in proper format", Toast.LENGTH_SHORT).show();
            }


            if (getByteByStringSign(text[1]) != null) {
                setImageSign();
            } else {
                // Toast.makeText(this, "Sign Data is not in proper format", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

            if (requestNo == 146) {

                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(PictureSign_demo.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }

            }

            if (requestNo == 168) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(PictureSign_demo.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                }
                else {

                    //  Toast.makeText(PictureSign.this, getString(R.string.uploadSucessfully), Toast.LENGTH_LONG).show();

                    showSuccessUploadImageSign(generalResponseModel.getUserDefinedString());
                }
            }

        } else {
            hideProgressDialog();
            Toast.makeText(PictureSign_demo.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    private void showSuccessUploadImageSign(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureSign_demo.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.pictureSign));


        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.PicImageUploadSucessReceipt) + "\n"+getString(R.string.transactionIdReceipt) + temp[0]);

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                PictureSign_demo.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            PictureSign_demo.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(PictureSign_demo.this);
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
            if (!isServerOperationInProcess) {
                PictureSign_demo.this.finish();
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


}
