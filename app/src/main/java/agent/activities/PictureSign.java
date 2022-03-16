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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
import agent.thread.ServerTaskFTP;

import static agent.activities.Base64.*;

/**
 * Created by Sahrique on 14/03/17.
 */

public class PictureSign extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
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
    private ScrollView scrollview_firstPage, scrollview_secondpage_picture_sign;
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
                DataParserThread thread = new DataParserThread(PictureSign.this, mComponentInfo, PictureSign.this, message.arg1, message.obj.toString());
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

            PictureSign.this.finish();
        }


        previousButton = (Button) findViewById(R.id.previousButton_MoneyTransfer);

        button_firstPageSubmit = (Button) findViewById(R.id.button_firstPageSubmit);
        button_firstPageSubmit.setOnClickListener(this);


        previousButton.setOnClickListener(this);

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
        scrollview_secondpage_picture_sign = (ScrollView) findViewById(R.id.scrollview_secondpage_picture_sign);
        scrollview_firstPage = (ScrollView) findViewById(R.id.scrollview_firstPage);


        // feb 15 2018


         button_firstPageSubmit.setVisibility(View.VISIBLE);                  // comment removie
     //    scrollview_firstPage.setVisibility(View.GONE);                        // remove this line
      //    scrollview_secondpage_picture_sign.setVisibility(View.VISIBLE);       // remove this line
      //    button_secondPageSubmit_upload.setVisibility(View.VISIBLE);           // remove this line


        subscriberNumberEditText.setOnEditorActionListener(this);

        imageView_userimage_signature=(ImageView)findViewById(R.id.imageView_userimage_signature) ;
        imageView_userImage=(ImageView)findViewById(R.id.imageView_userImage) ;

        imageView_userimage_signature.setClickable(true);
        imageView_userImage.setClickable(true);


        transferBasisSpinner = (Spinner) findViewById(R.id.spinnerSendMode_AccToCash);
        String[] transferBasisArray = getResources().getStringArray(R.array.TransferBasis);
        transferBasisSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transferBasisArray));
        transferBasisSpinner.setSelection(1);
        transferBasisSpinner.setOnItemSelectedListener(PictureSign.this);




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
            Toast.makeText(PictureSign.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PictureSign.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
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
                Toast.makeText(PictureSign.this, errorMsgToDisplay, Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(PictureSign.this, getString(R.string.pleaseselectcountry), Toast.LENGTH_LONG).show();
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

                if (boolselectUploadPic == true || boolselectUploadSignaturePic == true) {
                    uploadImageRequest_agentimage();
                } else {
                    Toast.makeText(PictureSign.this, "Plz Upload Picture ", Toast.LENGTH_LONG).show();
                }


                break;

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
                         Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        System.out.println(selectedImage);




                        ///////////////////////  15 fev 2018  /////////////////////////////

                        ColorMatrix matrix = new ColorMatrix();   // color to black in white
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_userImage.setColorFilter(filter);


                        Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
//                        int size = resizeBmp.getRowBytes() * resizeBmp.getHeight();
//                        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//                        resizeBmp.copyPixelsToBuffer(byteBuffer);
//                        byte[] b1 =  byteBuffer.array();
//                        uploadUserImageString = encodeImage(b1);

                        selectedImage=resizeBmp;

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] imageBytes = baos.toByteArray();

                        uploadUserImageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                        imageView_userImage.setImageBitmap(selectedImage);


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
                         Bitmap selectedImageSignature = BitmapFactory.decodeStream(imageStream);

                        Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImageSignature, 200, 200, true);
//                        int size = resizeBmp.getRowBytes() * resizeBmp.getHeight();
//                        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//                        resizeBmp.copyPixelsToBuffer(byteBuffer);
//                        byte[] b1 =  byteBuffer.array();
//                        uploadUserImageString = encodeImage(b1);

                        selectedImageSignature=resizeBmp;


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImageSignature.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] imageBytes = baos.toByteArray();

                        uploadSignatureImageString = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                        imageView_userimage_signature.setImageBitmap(selectedImageSignature);


                        ///////////////////////  15 fev 2018  /////////////////////////////

                        // color to black in white

                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_userimage_signature.setColorFilter(filter);

                       /* Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImageSignature, 200, 200, true);
                        int size = resizeBmp.getRowBytes() * resizeBmp.getHeight();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                        resizeBmp.copyPixelsToBuffer(byteBuffer);
                        byte[] b1 =  byteBuffer.array();
                        uploadSignatureImageString = encodeImage(b1);*/

                    }
                }

            }
        }
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayscale;
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

        if (new InternetCheck().isConnected(PictureSign.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generatePictureSign();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();
            isServerOperationInProcess = true;

          //  callApi("getViewProfileInJSON",requestData,146);


           new ServerTask(mComponentInfo, PictureSign.this, mHandler, requestData, "getViewProfileInJSON", 146).start();


        } else {
            Toast.makeText(PictureSign.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }



    private void uploadImageRequest_agentimage() {

        if (new InternetCheck().isConnected(PictureSign.this)) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generateUploadImage_agentimage();

            new ServerTaskFTP(mComponentInfo, PictureSign.this, mHandler, requestData, "uploadimage", 186).start();


        } else {
            Toast.makeText(PictureSign.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }

    private String generateUploadImage_agentimage() {
        String jsonString = "";
        String finalDataJsonString = "";

        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", subscriberNoString);
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("agentimage", uploadUserImageString);
            countryObj.put("signature", uploadSignatureImageString);     // signature
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


    void callApi(String apiName, String body, final int requestCode){
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,getString(R.string.url)+apiName,new JSONObject(body),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            DataParserThread thread = new DataParserThread(PictureSign.this,mComponentInfo,PictureSign.this,requestCode,response.toString());

                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(PictureSign.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();

                        }
                    });
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(PictureSign.this,getString(R.string.pleaseTryAgainLater),Toast.LENGTH_LONG).show();
            System.out.println(e);
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureSign.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.pictureSign));
        responseArray = data.split("\\|");

        TextView agentCodeTextView = (TextView) findViewById(R.id.agentCodeTextView);
        agentCodeTextView.setText(getString(R.string.agentCodeNew) +" : "+ agentCode);

        button_firstPageSubmit.setVisibility(View.GONE);
        scrollview_firstPage.setVisibility(View.GONE);
        scrollview_secondpage_picture_sign.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.GONE);

        languageReview.setText(responseArray[0]);
        subscriberNameReview.setText(" -------Subscriber Name Add New------ ");

        //  subscriberName not tag add

        idproofReview.setText(responseArray[4]);
        button_firstPageSubmit.setVisibility(View.GONE);
        button_secondPageSubmit_upload.setVisibility(View.VISIBLE);

        imageDownload();
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
                    Intent i = new Intent(PictureSign.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                } else {
                    showSuccess(generalResponseModel.getUserDefinedString());
                }

            }

           else if (requestNo == 186) {
                hideProgressDialog();
                if (generalResponseModel.isOTP()) {
                    mComponentInfo.getmSharedPreferences().edit().putString("process", "REMTSEND").commit();
                    Intent i = new Intent(PictureSign.this, OTPVerificationActivity.class);
                    startActivityForResult(i, 299);
                }
                else {
                    //  Toast.makeText(PictureSign.this, getString(R.string.uploadSucessfully), Toast.LENGTH_LONG).show();
                    showSuccessUploadImageSign(generalResponseModel.getUserDefinedString());
                }
            }

            else if (requestNo == 200)
            {
                try {

                    String[] responseImagearray = generalResponseModel.getUserDefinedString().split("\\|");

                    byte[] imageBytes1 = android.util.Base64.decode(responseImagearray[5], android.util.Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    imageViewSign.setImageBitmap(decodedImage);

                    byte[] imageBytes2 = android.util.Base64.decode(responseImagearray[4], android.util.Base64.DEFAULT);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    imageViewPicture.setImageBitmap(decodedImage2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            hideProgressDialog();
            Toast.makeText(PictureSign.this, "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }

    private void showSuccessUploadImageSign(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureSign.this);

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.pictureSign));


        String[] temp = data.split("\\|");

        builder.setMessage(getString(R.string.PicImageUploadSucessReceipt) + "\n"+getString(R.string.transactionIdReceipt) + temp[0]);

        builder.setPositiveButton(getString(R.string.close_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                successDialog.cancel();
                PictureSign.this.finish();
            }
        });
        successDialog = builder.create();
        successDialog.show();
    }

    private void updateProgressDialogMessage(final String message) {
        if (mDialog != null) {
            PictureSign.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDialog.setMessage(message);
                }
            });
        }
    }


    void imageDownload() {

        if (new InternetCheck().isConnected(PictureSign.this)) {
            showProgressDialog(getString(R.string.pleasewait));

            String requestData = generateImageDownloadingRequest();

              new ServerTaskFTP(mComponentInfo, PictureSign.this, mHandler, requestData, "downloadimage", 200).start();

         //   vollyRequestApi_FTP("downloadimage",requestData, 200);

        } else {
            Toast.makeText(PictureSign.this, getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    void vollyRequestApi_FTP(String apiName, final String body, final int requestCode)
    {
        try {


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ServerTaskFTP.baseUrl_ftp+apiName,new JSONObject(body),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Volly request  No- " + requestCode, "Volly request  BODY --> " + body);
                            Log.e("Volly request Code \n" + requestCode, "Volly Response --> " + response);


                            DataParserThread thread = new DataParserThread(PictureSign.this,mComponentInfo,PictureSign.this,requestCode,response.toString());
                            thread.execute();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressDialog();
                            Toast.makeText(PictureSign.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

                        }
                    });
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);


        }catch (Exception e){
            hideProgressDialog();
            Toast.makeText(PictureSign.this, getString(R.string.pleaseTryAgainLater), Toast.LENGTH_LONG).show();

        }

    }
    private String generateImageDownloadingRequest() {
        String jsonString = "";


        try {

            JSONObject countryObj = new JSONObject();
            countryObj.put("agentcode", subscriberNoString);

            countryObj.put("isagentimage", "Y");
            countryObj.put("issignature", "Y");

            countryObj.put("isidfrontimage", "N");
            countryObj.put("isidbackimage", "N");
            countryObj.put("isformimage", "N");
            countryObj.put("isbillimage", "N");
            countryObj.put("vendorcode", "MICR");
            countryObj.put("clienttype", "FTP");
            countryObj.put("requestcts", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {

        }
        return jsonString;
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(PictureSign.this);
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
                PictureSign.this.finish();
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
