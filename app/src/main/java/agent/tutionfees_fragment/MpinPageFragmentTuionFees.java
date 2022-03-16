package agent.tutionfees_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import commonutilities.InternetCheck;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;
import agent.thread.ServerTask;

import static android.content.Context.MODE_PRIVATE;


public class MpinPageFragmentTuionFees extends Fragment implements View.OnClickListener, ServerResponseParseCompletedNotifier {

    String countryCodeSelection, studentMobileNumberString, findRegistrationNumberString, studentFirstNameString, studentNameString, studentRegistrationNumberString, emailString, genderTypeString, agentCode, agentName, countrySelectionString, mobileNumberString;
    private ProgressDialog mDialog;
    EditText mpinEditText;

    Button submitButton;
    ComponentMd5SharedPre mComponentInfo;
    Double fees_tution_double, exam_tution_double, compitition_tution_double, transactionAmount_double, feesTariff_double, vatTariff_double, totalAmount_double;
    String[] feeNameArray_fromServer, feeAmountArray_fromServer, feeAmountEnteredArr_fromServer, feeNameEnteredArr_fromServer;
    String testingTagString_test_0,testingTagString_test1,testingTagString_test2,testingTagString_test3,testingTagString_test4;

    LinearLayout fees_master_linearLayout;
    String feesName_fromServer, feesAmount_fromServer;
    int countCharacter = 0;

    TextView transactionAmount_textview, fees_textview, vat_textview, totalAmount_textView, schoolname_textview, city_textview, sub_division_textview, division_textview, countryName_textview, schoolCode_textview, region_name_textview;
    String feesStringComment="",feeNameStr="",feeAmountStr="",feesString_na="",feesStringFinal="";
    String testingTagString="";    // temporary Tag add  04 Sep 2019
    String testingTagStringArray="";// its String temporary Tag add  04 Sep 2019
    View view;
    String mpinString;

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
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, MpinPageFragmentTuionFees.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.mpinpage_fragment_tutionfee, container, false); // Inflate the layout for this fragment


        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();

        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.ge223tmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);


        feeNameArray_fromServer = mComponentInfo.getFeeNameArray_fromServer();
        feeAmountArray_fromServer = mComponentInfo.getFeeAmountArray_fromServer();
        feeAmountEnteredArr_fromServer = mComponentInfo.getFeeAmountEnteredArr_fromServer();
        feeNameEnteredArr_fromServer = mComponentInfo.getFeeNameEnteredArr_fromServer();

        countryName_textview = (TextView) view.findViewById(R.id.countryName_textview);
        schoolCode_textview = (TextView) view.findViewById(R.id.schoolCode_textview);
        region_name_textview = (TextView) view.findViewById(R.id.region_name_textview);
        division_textview = (TextView) view.findViewById(R.id.division_textview);
        sub_division_textview = (TextView) view.findViewById(R.id.sub_division_textview);
        city_textview = (TextView) view.findViewById(R.id.city_textview);
        schoolname_textview = (TextView) view.findViewById(R.id.schoolname_textview);

        fees_master_linearLayout = (LinearLayout) view.findViewById(R.id.fees_master_linearLayout);

        testingTagString_test_0="Test 0:- "+feesStringComment;  // add 03 sep 2019 // display in log



        inflater = getLayoutInflater();

        for (int i = 0; i < feeNameArray_fromServer.length; i++) {


            if (feeAmountEnteredArr_fromServer[i].equalsIgnoreCase("0")) {  // feeNameEnteredArr_fromServer ="{AAAAA,0}
                System.out.println("test");
            } else {
                View view = inflater.inflate(R.layout.fees_item_review, null);
                TextView feeNameTitle = (TextView) view.findViewById(R.id.examFees_title_textview);
                TextView feeNameValue = (TextView) view.findViewById(R.id.examFees_textview);
                feeNameTitle.setText(feeNameEnteredArr_fromServer[i]);
                feeNameValue.setText(feeAmountEnteredArr_fromServer[i]);
                fees_master_linearLayout.addView(view);
            }

        }

//        for (int i = 0; i < feeAmountArray_fromServer.length; i++) {
//
//            if (feeAmountEnteredArr_fromServer[i].equalsIgnoreCase("0")) {
//                System.out.println("test");
//            }
//            else {
//
//                View view = inflater.inflate(R.layout.fees_item_review, null);
//                TextView feeNameValue = (TextView) view.findViewById(R.id.examFees_textview);
//                feeNameValue.setText(feeAmountEnteredArr_fromServer[i]);
//                fees_master_linearLayout.addView(view);
//            }
//        }
      /*  for (int i = 0; i < feeNameArray_fromServer.length; i++) {

            View view = inflater.inflate(R.layout.fees_item_review, null);

            TextView feeNameTitle = (TextView) view.findViewById(R.id.examFees_title_textview);
            feeNameTitle.setText(feeNameEnteredArr_fromServer[i]);


            TextView feeNameValue = (TextView) view.findViewById(R.id.examFees_textview);
            feeNameValue.setText(feeAmountEnteredArr_fromServer[i]);


            fees_master_linearLayout.addView(view);
        }*/


        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);


        countryName_textview.setText(mComponentInfo.getCountry_name());
        schoolCode_textview.setText(mComponentInfo.getSchool_code());
        region_name_textview.setText(mComponentInfo.getRegion_name());
        division_textview.setText(mComponentInfo.getDivision());
        sub_division_textview.setText(mComponentInfo.getSubdivision());
        city_textview.setText(mComponentInfo.getCity());
        schoolname_textview.setText(mComponentInfo.getSchool_name());


       /* mobileNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.mobileNumber_autoCompleteTextView);
        findRegistrationNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.findRegistrationNumber_autoCompleteTextView);
        studentRegistrationNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.studentRegistrationNumber_autoCompleteTextView);


        studentRegistrationNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.studentRegistrationNumber_autoCompleteTextView);
        studentMobileNumber_autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.studentMobileNumber_autoCompleteTextView);

        studentName_edittext = (EditText) view.findViewById(R.id.studentName_edittext);
        studentName_first_edittext = (EditText) view.findViewById(R.id.studentName_first_edittext);
        email_edittext = (EditText) view.findViewById(R.id.email_edittext);*/


        mpinEditText = (EditText) view.findViewById(R.id.mpinEditText);

        countryCodeSelection = mComponentInfo.getCountry_code();

        TextView email_student_textview, feesType_textview, paymentOption_textview, option_textview, level_textview, name_classDetails_textview, email_payers_textview, name_payers_textview, mobilenumber_payers_textview, registrationNumber_textview, studentName_textview, studentFirstName_textview, gender_textview, birtdate_textview, mobileNumber_student_textview;

        registrationNumber_textview = (TextView) view.findViewById(R.id.registrationNumber_textview);
        studentName_textview = (TextView) view.findViewById(R.id.studentName_textview);
        studentFirstName_textview = (TextView) view.findViewById(R.id.studentFirstName_textview);
        gender_textview = (TextView) view.findViewById(R.id.gender_textview);
        birtdate_textview = (TextView) view.findViewById(R.id.birtdate_textview);
        mobileNumber_student_textview = (TextView) view.findViewById(R.id.mobileNumber_student_textview);
        email_student_textview = (TextView) view.findViewById(R.id.email_textview);

        registrationNumber_textview.setText(mComponentInfo.getRegisterByNumberByName());
        studentName_textview.setText(mComponentInfo.getStudentNameString());
        studentFirstName_textview.setText(mComponentInfo.getStudentFirstNameString());
        gender_textview.setText(mComponentInfo.getGenderName());
        birtdate_textview.setText(mComponentInfo.getStudent_dateOfbirthdate());
        mobileNumber_student_textview.setText(mComponentInfo.getStudentMobileNumberString());
        email_student_textview.setText(mComponentInfo.getStudent_email());

        mobilenumber_payers_textview = (TextView) view.findViewById(R.id.mobilenumber_payers_textview);
        name_payers_textview = (TextView) view.findViewById(R.id.name_payers_textview);
        email_payers_textview = (TextView) view.findViewById(R.id.email_payers_textview);

        mobilenumber_payers_textview.setText(mComponentInfo.getPayerMobileNumber());
        name_payers_textview.setText(mComponentInfo.getPayerName());
        email_payers_textview.setText(mComponentInfo.getPayerEmail());


        level_textview = (TextView) view.findViewById(R.id.level_textview);
        option_textview = (TextView) view.findViewById(R.id.option_textview);
        name_classDetails_textview = (TextView) view.findViewById(R.id.name_classDetails_textview);

        level_textview.setText(mComponentInfo.getLevelTypeName());
        option_textview.setText(mComponentInfo.getOptionTypeName());
        name_classDetails_textview.setText(mComponentInfo.getClassName());


        feesType_textview = (TextView) view.findViewById(R.id.feesType_textview);
        paymentOption_textview = (TextView) view.findViewById(R.id.paymentOption_textview);

        feesType_textview.setText(mComponentInfo.getFeesTypeName());
        paymentOption_textview.setText(getString(R.string.full_payment));


        transactionAmount_textview = (TextView) view.findViewById(R.id.transactionAmount_textview);
        fees_textview = (TextView) view.findViewById(R.id.fees_textview);
        vat_textview = (TextView) view.findViewById(R.id.vat_textview);
        totalAmount_textView = (TextView) view.findViewById(R.id.totalAmount_textView);


        transactionAmount_textview.setText(mComponentInfo.getTransactionAmount() + " " + "XAF");
        try {


            fees_textview.setText(mComponentInfo.getFees_tariff() + " " + "XAF");
            vat_textview.setText(mComponentInfo.getVat_tariff() + " " + "XAF");


            feesTariff_double = Double.parseDouble(mComponentInfo.getFees_tariff());
            vatTariff_double = Double.parseDouble(mComponentInfo.getVat_tariff());
            transactionAmount_double = Double.parseDouble(mComponentInfo.getTransactionAmount());


            totalAmount_double = transactionAmount_double + feesTariff_double + vatTariff_double;


            mComponentInfo.setArrowBackButtonTuitionFees("frag_mpin");


            NumberFormat df = DecimalFormat.getInstance(Locale.ENGLISH);
            df.setMinimumFractionDigits(2);
            df.setGroupingUsed(false);
            df.setMaximumFractionDigits(2);
            df.setRoundingMode(RoundingMode.UP);

            mComponentInfo.setTotalAmount(df.format(totalAmount_double));

            //  Double totalAmount_double_temp=mComponentInfo.getTotalAmount();


            //    String totalAmountString = Double.toString(totalAmount_double);


            Double totalAmount = Double.parseDouble(df.format(totalAmount_double));
            String totalAmountString = Double.toString(totalAmount);

            totalAmount_textView.setText(totalAmountString + " " + "XAF");


            mComponentInfo.setTotalAmount(df.format(totalAmount_double));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    private void request_getFeePaymentTransaction() {

        if (new InternetCheck().isConnected(getActivity())) {
            showProgressDialog(getString(R.string.pleasewait));
            String requestData = generate_cashtoM_new();
            mComponentInfo.getmSharedPreferences().edit().putString("requestData", requestData).commit();

            new ServerTask(mComponentInfo, getActivity(), mHandler, requestData, "getFeePaymentTransaction", 237).start();

        } else {
            Toast.makeText(getActivity(), getString(R.string.pleaseCheckInternet), Toast.LENGTH_LONG).show();
        }
    }


    private String generate_cashtoM_new() {

        String jsonString = "";

        String pin = mComponentInfo.getMD5(agentCode + mpinString).toUpperCase();
        try {


            JSONObject countryObj = new JSONObject();
            countryObj.put("agentCode", agentCode);

            // countryObj.put("source", agentCode); // acording to bhawesh
            // countryObj.put("sourceName", mComponentInfo.getStudentNameString());
            // source code put paye'r mobile, and source name put payer's name // ############  acording to fonning 17 july 2019  ############

            countryObj.put("source", mComponentInfo.getPayerMobileNumber());
            countryObj.put("sourceName", mComponentInfo.getPayerName());

            //   countryObj.put("destination", mComponentInfo.getPayerMobileNumber());  // remove acording to bhawesh
            // countryObj.put("destinationName", mComponentInfo.getPayerName());   // remove acording to bhawesh

            countryObj.put("pin", pin);
            countryObj.put("pintype", "MPIN");
            countryObj.put("amount", mComponentInfo.getTransactionAmount());
            countryObj.put("billerName", "");
            countryObj.put("billerCode", "");
            countryObj.put("invoiceno", "0123445");// mandatory
            countryObj.put("clientType", "GPRS");

            String APP_VERSION_API = mComponentInfo.getmSharedPreferences().getString("APP_VERSION_API","");
            String APK_NAME_VERSION = mComponentInfo.getmSharedPreferences().getString("APK_NAME_VERSION","");
            countryObj.put(APK_NAME_VERSION, APP_VERSION_API);



            // #################### Comment   ####################

            commentFeesDetails();

            String payerMailString_temp = mComponentInfo.getPayerEmail();
            if (payerMailString_temp.equalsIgnoreCase("")) {
                payerMailString_temp = "NA";
            } else {
                payerMailString_temp = mComponentInfo.getPayerEmail();
            }


            String schoolCode_commnet = mComponentInfo.getSchool_code();
            if (schoolCode_commnet.equalsIgnoreCase("")) {
                schoolCode_commnet = "NA";
            } else {
                schoolCode_commnet = mComponentInfo.getSchool_code();
            }

            String schoolName_commnet = mComponentInfo.getSchool_name();
            if (schoolName_commnet.equalsIgnoreCase("")) {
                schoolName_commnet = "NA";
            } else {
                schoolName_commnet = mComponentInfo.getSchool_name();
            }

            String subdivision_commnet = mComponentInfo.getSubdivision();
            if (subdivision_commnet.equalsIgnoreCase("")) {
                subdivision_commnet = "NA";
            } else {
                subdivision_commnet = mComponentInfo.getSubdivision();
            }

            String city_comment = mComponentInfo.getCity();
            if (city_comment.equalsIgnoreCase("")) {
                city_comment = "NA";
            } else {
                city_comment = mComponentInfo.getCity();
            }

            String optionName_comment = mComponentInfo.getOptionTypeName();
            if (optionName_comment.equalsIgnoreCase("")) {
                optionName_comment = "NA";
            } else if (optionName_comment.equalsIgnoreCase("Option")) {
                optionName_comment = "NA";
            } else {
                optionName_comment = mComponentInfo.getOptionTypeName();
            }

            String studentName_comment = mComponentInfo.getStudentNameString();
            if (studentName_comment.equalsIgnoreCase("")) {
                studentName_comment = "NA";
            } else {
                studentName_comment = mComponentInfo.getStudentNameString();
            }

            String studentFirstName_comment = mComponentInfo.getStudentFirstNameString();
            if (studentFirstName_comment.equalsIgnoreCase("")) {
                studentFirstName_comment = "NA";
            } else {
                studentFirstName_comment = mComponentInfo.getStudentFirstNameString();
            }

            String studentMobileNumber_comment = mComponentInfo.getStudentMobileNumberString();
            if (studentMobileNumber_comment.equalsIgnoreCase("")) {
                studentMobileNumber_comment = "NA";
            } else {
                studentMobileNumber_comment = mComponentInfo.getStudentMobileNumberString();
            }


            String studentEmail_comment = mComponentInfo.getStudent_email();
            if (studentEmail_comment.equalsIgnoreCase("")) {
                studentEmail_comment = "NA";
            } else {
                studentEmail_comment = mComponentInfo.getStudent_email();
            }

            String registrationNumber_comment = mComponentInfo.getStudentRegistrationNumberString();
            if (registrationNumber_comment.equalsIgnoreCase("")) {
                registrationNumber_comment = "NA";
            } else {
                registrationNumber_comment = mComponentInfo.getStudentRegistrationNumberString();
            }

            String className_comment = mComponentInfo.getClassName();
            if (className_comment.equalsIgnoreCase("")) {
                className_comment = "NA";
            } else {
                className_comment = mComponentInfo.getClassName();
            }


            String studentDateOfBirthString = "";
            studentDateOfBirthString = mComponentInfo.getStudent_dateOfbirthdate();


            String monthString, dayString, yearString, finalDateOfBirth_student;


            if (studentDateOfBirthString.contains("/")) {


                String studentDateOfBirthString_date = studentDateOfBirthString.replace("/", "-");

                String[] tempDta = studentDateOfBirthString_date.split("-");

                dayString = tempDta[0];
                monthString = tempDta[1];
                yearString = tempDta[2];


                if (monthString.length() == 1) {
                    monthString = "0" + monthString;
                }

                if (dayString.length() == 1) {
                    dayString = "0" + dayString;
                }

                finalDateOfBirth_student = dayString + "-" + monthString + "-" + yearString;

            } else {


                String[] tempDta = studentDateOfBirthString.split("-");     // Server response date 2010-10-10

                yearString = tempDta[0];
                monthString = tempDta[1];
                dayString = tempDta[2];

                finalDateOfBirth_student = dayString + "-" + monthString + "-" + yearString;
            }

            System.out.println(finalDateOfBirth_student);


            String gender_comment = mComponentInfo.getGenderName();

            if (gender_comment.equalsIgnoreCase("m") || gender_comment.equalsIgnoreCase("male") || gender_comment.equalsIgnoreCase("Male") || gender_comment.equalsIgnoreCase("Homme")) {
                gender_comment = "male";
            } else {
                gender_comment = "female";
            }


            String commentString = "FEE::" + feesStringFinal + "||" +
                    "SCHOOL::" + schoolCode_commnet + "|" + schoolName_commnet + "||" +

                    "CLASS::" + mComponentInfo.getLevelTypeName() + "|" + optionName_comment + "|" + className_comment + "||" +

                    "STUDENT::" + studentFirstName_comment + "|" + studentName_comment + "|" +
                    finalDateOfBirth_student + "|" + gender_comment + "|"

                    + studentMobileNumber_comment + "|" + registrationNumber_comment + "|" + studentEmail_comment + "||" +


                    "PAYER::" + mComponentInfo.getPayerMobileNumber() + "|" + mComponentInfo.getPayerName() + "|" + payerMailString_temp;

            // #################### Comment   ####################


            countryObj.put("comments", commentString);

            testingTagString="****"+testingTagString_test_0+"@@@@ "+testingTagString_test1+" ##### "+testingTagString_test2+" $$$$ "+testingTagString_test3+" &&&&& "+testingTagString_test4;

            countryObj.put("testingTagString", testingTagString);  // temporary Tag add  04 Sep 2019
            countryObj.put("testingTagStringArray", testingTagStringArray);  // temporary Tag add  04 Sep 2019

            countryObj.put("vendorCode", "MICR");
            countryObj.put("udv1", "");

            jsonString = countryObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    // ################### Comment  Fees if remove 0  hard code Fee Remove Main Method remove ##############################


   public void commentFeesDetails() {

       feesStringComment="";      // add 03 sep 2019 // display in log



       testingTagStringArray = Arrays.toString(feeNameArray_fromServer);
       testingTagString_test1="Test 1:- "+feesStringComment;  // add 03 sep 2019 // display in log


       for (int i = 0; i < feeNameArray_fromServer.length; i++) {  //
            if (feeAmountEnteredArr_fromServer[i].equalsIgnoreCase("0")) {  // feeNameEnteredArr_fromServer ="{AAAAA,0}
                System.out.println("test");

                feesStringComment = feesStringComment.concat("NA" + "|");

                testingTagString_test2="Test 2:- "+feesStringComment;  // add 03 sep 2019 // display in log

            } else {
                feeNameStr = feeNameEnteredArr_fromServer[i];
                feeAmountStr = feeAmountEnteredArr_fromServer[i];

                feesStringComment = feesStringComment.concat(feeNameStr + ":" + feeAmountStr + "|");

                testingTagString_test3="Test 3:- "+feesStringComment;  // add 03 sep 2019 // display in log


            }
        }


        if (feesString_na.contains("NA")) {
            feesStringFinal =  feesString_na + " " + feesStringComment;
        } else {
            feesStringFinal = feesStringComment;

            testingTagString_test4="Test 4:- "+feesStringComment;  // add 03 sep 2019 // display in log

            removeLastCharacter(feesStringFinal);
        }
        System.out.println(feesStringFinal);   // display in log


   }

    String removeLastCharacter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '|') {
            str = str.substring(0, str.length() - 1);
        }

        feesStringFinal = str;
        characterCounter(feesStringFinal, '|');

        if(countCharacter==0)
        {
            feesStringFinal=feesStringFinal+"|NA|NA";
        }

        if(countCharacter==1)
        {
            feesStringFinal=feesStringFinal+"|NA";
        }


        return feesStringFinal;
    }
    public int characterCounter(String str, char c)
    {


        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            countCharacter++;
        }
        return countCharacter;
    }




    // #############################################################################



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submitButton:

                validationDetails();

                break;


        }
    }

    public void validationDetails() {

        if (validateDetails_PartI()) {

            request_getFeePaymentTransaction();
        }
    }


    private boolean validateDetails_PartI() {
        boolean ret = false;


        mpinString = mpinEditText.getText().toString();
        if (mpinString.trim().length() == 4) {


            ret = true;

        } else {
            Toast.makeText(getActivity(), getString(R.string.mpinAccountBalance), Toast.LENGTH_SHORT).show();
        }

        return ret;

    }


    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(getActivity());
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
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {

        if (generalResponseModel.getResponseCode() == 0) {
            hideProgressDialog();


            if (requestNo == 237) {
                try {


                    String responseData = generalResponseModel.getUserDefinedString();
                    String[] responseArray = responseData.split("\\#######");

                    mComponentInfo.setTransactionId_reprint(responseArray[0]);
                    mComponentInfo.setDateTime_reprintTuitionFees(responseArray[3]);
                    mComponentInfo.setAgentBranch_reprint(responseArray[9]);


                    Intent intent = new Intent(getActivity(), SucessReceiptTutionFees.class);
                    startActivity(intent);
                    getActivity().finish();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "" + getString(R.string.plzTryAgainLater), Toast.LENGTH_SHORT).show();

                    getActivity().finish();
                }
            }


        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        }

    }
}
