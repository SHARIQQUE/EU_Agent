package commonutilities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Properties;

import model.BillModel;


public class ComponentMd5SharedPre extends Application {

    Properties properties;
    AssetManager assetManager;
    private String resultDescription="";
    private String PREF_NAME = "eu_agentprefs";
    private SharedPreferences mSharedPreferences;
    public boolean activityRunning = true;
    public ArrayList<String> transHistoryData;
    public ArrayList<BillModel> paidBillsList;


    @Override
    public void onCreate() {
        super.onCreate();




      //  setupActivityListener();

    }

    public String get_OTP_STRING() {
        return _OTP_STRING;
    }

    public void set_OTP_STRING(String _OTP_STRING) {
        this._OTP_STRING = _OTP_STRING;
    }

    private String _OTP_STRING="";

    public String get_MPIN_OTP_STRING() {
        return _MPIN_OTP_STRING;
    }

    public void set_MPIN_OTP_STRING(String _MPIN_OTP_STRING) {
        this._MPIN_OTP_STRING = _MPIN_OTP_STRING;
    }

    public boolean is_IS_HIDE_MPIN_OTP() {
        return _IS_HIDE_MPIN_OTP;
    }

    public void set_IS_HIDE_MPIN_OTP(boolean _IS_HIDE_MPIN_OTP) {
        this._IS_HIDE_MPIN_OTP = _IS_HIDE_MPIN_OTP;
    }

    private String _MPIN_OTP_STRING="";

    private boolean _IS_HIDE_MPIN_OTP=false;

    public ArrayList<BillModel> getPaidBillsList() {
        if(paidBillsList==null){
            paidBillsList=new ArrayList<BillModel>();
        }
        return paidBillsList;
    }

    public void setPaidBillsList(ArrayList<BillModel> paidBillsList) {
        this.paidBillsList = paidBillsList;
    }

    public ArrayList<String> getTransHistoryData() {
        return transHistoryData;
    }

    public void setTransHistoryData(ArrayList<String> transHistoryData) {
        this.transHistoryData = transHistoryData;
    }

    public SharedPreferences getmSharedPreferences() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        }
        return mSharedPreferences;
    }


    public String getMD5(String input) {
        String output = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            output = number.toString(16);
            while (output.length() < 32)
                output = "0" + output;
        } catch (Exception e) {
            Log.e("MD5", e.getLocalizedMessage());
            output = "";
        }
        return output.toUpperCase();

    }

    public String getResultDescription(String key, String language) {



        if(properties==null){

            properties = new Properties();
        }
        if(assetManager==null){

            assetManager = getAssets();
        }

        try{

            InputStream inputStream = assetManager.open("resultcodedescription_"+language+".properties");
            properties.load(inputStream);
            resultDescription=new String( properties.getProperty(key).getBytes(),"UTF-8");
        }catch (Exception e){



        }
        if(resultDescription==null){

            resultDescription="";
        }


        return resultDescription;
    }


 // ############################################ TUITION FEES ############################################

    String country_name="";
    String country_code="";
    String school_name="";
    String school_code="";
    String region_name="";
    String region_code="";
    String division="";
    String genderName="";
    String genderCode="";
    String studentRegistrationNumberString="";
    String studentNameString="";
    String studentFirstNameString="";
    String studentMobileNumberString="";
    String emailString="";
    String levelTypeName="";
    String levelTypeCode="";
    String optionTypeName="";
    String tutionFeesTypeName="";
    String tutionfeesType_code="";
    String optionTypeCode="";
    String className="";
    String paymentTypeName="";
    String paymentTypeCode="";

   /* String feesTution="";
    String feesExam="";
    String feesCompetition="";*/

    String payerName="";
    String payerMobileNumber="";
    String payerEmail="";
    String feesTypeAmount="";
    String feesTypeName="";
    String dateTime_reprintTuitionFees="";
    String agentBranch_reprint="";
    String transactionId_reprint;
    String registerByNumberByName="", student_dateOfbirthdate = "", student_class_id = "", student_class_name = "", student_phone = "", student_email = "", student_subdivision="",student_city="", student_country_name = "", student_school_name = "", student_school_code = "", student_region_name = "", student_region_code = "", student_division = "";
    String subdivision="";
    String city="";
    String fees_tariff="",vat_tariff;
    String transactionAmount="",totalAmount;
    String firstChooseClick="";


    public String getFirstChooseClick() {
        return firstChooseClick;
    }

    public void setFirstChooseClick(String firstChooseClick) {
        this.firstChooseClick = firstChooseClick;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFees_tariff() {
        return fees_tariff;
    }

    public void setFees_tariff(String fees_tariff) {
        this.fees_tariff = fees_tariff;
    }

    public String getVat_tariff() {
        return vat_tariff;
    }

    public void setVat_tariff(String vat_tariff) {
        this.vat_tariff = vat_tariff;
    }

    String[] schoolCode_array;
    String[] regionName_array;
    String[] regionCode_array;
    String[] optionName_array;
    String[] optionCode_array;
    String[] division_array;
    String[] subdivision_array;
    String[] city_array;


    //////////  #############################################

    String[] feeNameArray_fromServer,feeAmountArray_fromServer,feeAmountEnteredArr_fromServer,feeNameEnteredArr_fromServer;

    public String[] getFeeNameArray_fromServer() {
        return feeNameArray_fromServer;
    }

    public String[] getFeeNameEnteredArr_fromServer() {
        return feeNameEnteredArr_fromServer;
    }

    public void setFeeNameEnteredArr_fromServer(String[] feeNameEnteredArr_fromServer) {
        this.feeNameEnteredArr_fromServer = feeNameEnteredArr_fromServer;
    }

    public void setFeeNameArray_fromServer(String[] feeNameArray_fromServer) {
        this.feeNameArray_fromServer = feeNameArray_fromServer;
    }

    public String[] getFeeAmountArray_fromServer() {
        return feeAmountArray_fromServer;
    }

    public void setFeeAmountArray_fromServer(String[] feeAmountArray_fromServer) {
        this.feeAmountArray_fromServer = feeAmountArray_fromServer;
    }

    public String[] getFeeAmountEnteredArr_fromServer() {
        return feeAmountEnteredArr_fromServer;
    }

    public void setFeeAmountEnteredArr_fromServer(String[] feeAmountEnteredArr_fromServer) {
        this.feeAmountEnteredArr_fromServer = feeAmountEnteredArr_fromServer;
    }


    //////////  #############################################


    public String getAgentBranch_reprint() {
        return agentBranch_reprint;
    }

    public void setAgentBranch_reprint(String agentBranch_reprint) {
        this.agentBranch_reprint = agentBranch_reprint;
    }

    public String getTransactionId_reprint() {
        return transactionId_reprint;
    }

    public void setTransactionId_reprint(String transactionId_reprint) {
        this.transactionId_reprint = transactionId_reprint;
    }

    String[] schoolname_array;

    public String getDateTime_reprintTuitionFees() {
        return dateTime_reprintTuitionFees;
    }

    public void setDateTime_reprintTuitionFees(String dateTime_reprintTuitionFees) {
        this.dateTime_reprintTuitionFees = dateTime_reprintTuitionFees;
    }


    public String getRegisterByNumberByName() {
        return registerByNumberByName;
    }

    public void setRegisterByNumberByName(String registerByNumberByName) {
        this.registerByNumberByName = registerByNumberByName;
    }

    public String getFeesTypeName() {
        return feesTypeName;
    }

    public void setFeesTypeName(String feesTypeName) {
        this.feesTypeName = feesTypeName;
    }

    public String getFeesTypeAmount() {
        return feesTypeAmount;
    }

    public void setFeesTypeAmount(String feesTypeAmount) {
        this.feesTypeAmount = feesTypeAmount;
    }




    public String[] getOptionName_array() {
        return optionName_array;
    }

    public void setOptionName_array(String[] optionName_array) {
        this.optionName_array = optionName_array;
    }

    public String[] getOptionCode_array() {
        return optionCode_array;
    }

    public void setOptionCode_array(String[] optionCode_array) {
        this.optionCode_array = optionCode_array;
    }

    public String getStudent_dateOfbirthdate() {
        return student_dateOfbirthdate;
    }

    public void setStudent_dateOfbirthdate(String student_dateOfbirthdate) {
        this.student_dateOfbirthdate = student_dateOfbirthdate;
    }



    public String getStudent_class_id() {
        return student_class_id;
    }

    public void setStudent_class_id(String student_class_id) {
        this.student_class_id = student_class_id;
    }

    public String getStudent_class_name() {
        return student_class_name;
    }

    public void setStudent_class_name(String student_class_name) {
        this.student_class_name = student_class_name;
    }

    public String getStudent_phone() {
        return student_phone;
    }

    public void setStudent_phone(String student_phone) {
        this.student_phone = student_phone;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_subdivision() {
        return student_subdivision;
    }

    public void setStudent_subdivision(String student_subdivision) {
        this.student_subdivision = student_subdivision;
    }

    public String getStudent_city() {
        return student_city;
    }

    public void setStudent_city(String student_city) {
        this.student_city = student_city;
    }


    public String getStudent_country_name() {
        return student_country_name;
    }

    public void setStudent_country_name(String student_country_name) {
        this.student_country_name = student_country_name;
    }

    public String getStudent_school_name() {
        return student_school_name;
    }

    public void setStudent_school_name(String student_school_name) {
        this.student_school_name = student_school_name;
    }

    public String getStudent_school_code() {
        return student_school_code;
    }

    public void setStudent_school_code(String student_school_code) {
        this.student_school_code = student_school_code;
    }

    public String getStudent_region_name() {
        return student_region_name;
    }

    public void setStudent_region_name(String student_region_name) {
        this.student_region_name = student_region_name;
    }

    public String getStudent_region_code() {
        return student_region_code;
    }

    public void setStudent_region_code(String student_region_code) {
        this.student_region_code = student_region_code;
    }

    public String getStudent_division() {
        return student_division;
    }

    public void setStudent_division(String student_division) {
        this.student_division = student_division;
    }



    public String[] getSchoolname_array() {
        return schoolname_array;
    }

    public void setSchoolname_array(String[] schoolname_array) {
        this.schoolname_array = schoolname_array;
    }

    public String[] getSchoolCode_array() {
        return schoolCode_array;
    }

    public void setSchoolCode_array(String[] schoolCode_array) {
        this.schoolCode_array = schoolCode_array;
    }

    public String[] getRegionName_array() {
        return regionName_array;
    }

    public void setRegionName_array(String[] regionName_array) {
        this.regionName_array = regionName_array;
    }

    public String[] getRegionCode_array() {
        return regionCode_array;
    }

    public void setRegionCode_array(String[] regionCode_array) {
        this.regionCode_array = regionCode_array;
    }

    public String[] getDivision_array() {
        return division_array;
    }

    public void setDivision_array(String[] division_array) {
        this.division_array = division_array;
    }

    public String[] getSubdivision_array() {
        return subdivision_array;
    }

    public void setSubdivision_array(String[] subdivision_array) {
        this.subdivision_array = subdivision_array;
    }

    public String[] getCity_array() {
        return city_array;
    }

    public void setCity_array(String[] city_array) {
        this.city_array = city_array;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerMobileNumber() {
        return payerMobileNumber;
    }

    public void setPayerMobileNumber(String payerMobileNumber) {
        this.payerMobileNumber = payerMobileNumber;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }



    /*public String getFeesTution() {
        return feesTution;
    }

    public void setFeesTution(String feesTution) {
        this.feesTution = feesTution;
    }

    public String getFeesExam() {
        return feesExam;
    }

    public void setFeesExam(String feesExam) {
        this.feesExam = feesExam;
    }

    public String getFeesCompetition() {
        return feesCompetition;
    }

    public void setFeesCompetition(String feesCompetition) {
        this.feesCompetition = feesCompetition;
    }
    */


    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTutionFeesTypeName() {
        return tutionFeesTypeName;
    }

    public void setTutionFeesTypeName(String tutionFeesTypeName) {
        this.tutionFeesTypeName = tutionFeesTypeName;
    }

    public String getTutionfeesType_code() {
        return tutionfeesType_code;
    }

    public void setTutionfeesType_code(String tutionfeesType_code) {
        this.tutionfeesType_code = tutionfeesType_code;
    }

    public String getLevelTypeName() {
        return levelTypeName;
    }

    public void setLevelTypeName(String levelTypeName) {
        this.levelTypeName = levelTypeName;
    }

    public String getLevelTypeCode() {
        return levelTypeCode;
    }

    public void setLevelTypeCode(String levelTypeCode) {
        this.levelTypeCode = levelTypeCode;
    }

    public String getOptionTypeName() {
        return optionTypeName;
    }

    public void setOptionTypeName(String optionTypeName) {
        this.optionTypeName = optionTypeName;
    }

    public String getOptionTypeCode() {
        return optionTypeCode;
    }

    public void setOptionTypeCode(String optionTypeCode) {
        this.optionTypeCode = optionTypeCode;
    }




    public String getStudentRegistrationNumberString() {
        return studentRegistrationNumberString;
    }

    public void setStudentRegistrationNumberString(String studentRegistrationNumberString) {
        this.studentRegistrationNumberString = studentRegistrationNumberString;
    }

    public String getStudentNameString() {
        return studentNameString;
    }

    public void setStudentNameString(String studentNameString) {
        this.studentNameString = studentNameString;
    }

    public String getStudentFirstNameString() {
        return studentFirstNameString;
    }

    public void setStudentFirstNameString(String studentFirstNameString) {
        this.studentFirstNameString = studentFirstNameString;
    }

    public String getStudentMobileNumberString() {
        return studentMobileNumberString;
    }

    public void setStudentMobileNumberString(String studentMobileNumberString) {
        this.studentMobileNumberString = studentMobileNumberString;
    }

    public String getEmailString() {
        return emailString;
    }

    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_code() {
        return school_code;
    }

    public void setSchool_code(String school_code) {
        this.school_code = school_code;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }



     int arrowBackButton_sendCash;
     int arrowBackButton_receiveCash;
     int finishActivity_receiveCash;

    public int getArrowBackButton_receiveCash() {
        return arrowBackButton_receiveCash;
    }

    public void setArrowBackButton_receiveCash(int arrowBackButton_receiveCash) {
        this.arrowBackButton_receiveCash = arrowBackButton_receiveCash;
    }

    public int getFinishActivity_receiveCash() {
        return finishActivity_receiveCash;
    }

    public void setFinishActivity_receiveCash(int finishActivity_receiveCash) {
        this.finishActivity_receiveCash = finishActivity_receiveCash;
    }

    public int getArrowBackButton_sendCash() {
        return arrowBackButton_sendCash;
    }

    public void setArrowBackButton_sendCash(int arrowBackButton_sendCash) {
        this.arrowBackButton_sendCash = arrowBackButton_sendCash;
    }



// ########################################## TUITION FEES #####################################

     String selectSearchBy;  //

    public String getSelectSearchBy() {
        return selectSearchBy;
    }

    public void setSelectSearchBy(String selectSearchBy) {
        this.selectSearchBy = selectSearchBy;
    }
    //  back Button Arrow manage

    String arrowBackButtonTuitionFees="";


    public String getArrowBackButtonTuitionFees() {
        return arrowBackButtonTuitionFees;
    }

    public void setArrowBackButtonTuitionFees(String arrowBackButtonTuitionFees) {
        this.arrowBackButtonTuitionFees = arrowBackButtonTuitionFees;
    }

    // ########################################## Reprint Send Cash  #####################################


   String dateTime_reprint_sendcash="",referenceNumber_reprint_sendcash="",senderFirstName_reprint_sendCash="",senderName_reprint_sendCash="",destinationCountryName_reprint_sendCash=""
    ,destinationName_reprint_sendCash="",destinationMobileNumber_reprint_sendCash="",senderCurrencyCode_reprint_sendCash="",amount_reprint_sendCash="",fees_reprint_sendCash=""
    ,attachedBranchName_reprint_sendCash="",senderCountryName_reprint_sendCash,comment_reprint_sendCash="",transactionId_reprint_sendCash="",senderMobileNumber_reprint_sendCash="",
    idDocumentCountryOffissue_reprint_sendCash="",IdDocumnetType_reprint_sendCash="",getIdDocumnetDateOfIssue_reprint_sendCash="",otherTax_reprint_sendCash="",tax_reprint_sendCash="",
    destinationCurrencyCode_reprint_sendCash="",amountToPay_reprint_sendCash="",questionName_reprint_sendCash="",answerName_reprint_sendCash="",destinationFirstName_reprint_sendCash="";


    public String getDestinationCurrencyCode_reprint_sendCash() {
        return destinationCurrencyCode_reprint_sendCash;
    }

    public void setDestinationCurrencyCode_reprint_sendCash(String destinationCurrencyCode_reprint_sendCash) {
        this.destinationCurrencyCode_reprint_sendCash = destinationCurrencyCode_reprint_sendCash;
    }

    public String getAmountToPay_reprint_sendCash() {
        return amountToPay_reprint_sendCash;
    }

    public void setAmountToPay_reprint_sendCash(String amountToPay_reprint_sendCash) {
        this.amountToPay_reprint_sendCash = amountToPay_reprint_sendCash;
    }

    public String getQuestionName_reprint_sendCash() {
        return questionName_reprint_sendCash;
    }

    public void setQuestionName_reprint_sendCash(String questionName_reprint_sendCash) {
        this.questionName_reprint_sendCash = questionName_reprint_sendCash;
    }

    public String getAnswerName_reprint_sendCash() {
        return answerName_reprint_sendCash;
    }

    public void setAnswerName_reprint_sendCash(String answerName_reprint_sendCash) {
        this.answerName_reprint_sendCash = answerName_reprint_sendCash;
    }

    public String getDestinationFirstName_reprint_sendCash() {
        return destinationFirstName_reprint_sendCash;
    }

    public void setDestinationFirstName_reprint_sendCash(String destinationFirstName_reprint_sendCash) {
        this.destinationFirstName_reprint_sendCash = destinationFirstName_reprint_sendCash;
    }

    public String getTax_reprint_sendCash() {
        return tax_reprint_sendCash;
    }

    public void setTax_reprint_sendCash(String tax_reprint_sendCash) {
        this.tax_reprint_sendCash = tax_reprint_sendCash;
    }

    public String getGetIdDocumnetDateOfIssue_reprint_sendCash() {
        return getIdDocumnetDateOfIssue_reprint_sendCash;
    }

    public void setGetIdDocumnetDateOfIssue_reprint_sendCash(String getIdDocumnetDateOfIssue_reprint_sendCash) {
        this.getIdDocumnetDateOfIssue_reprint_sendCash = getIdDocumnetDateOfIssue_reprint_sendCash;
    }

    public String getOtherTax_reprint_sendCash() {
        return otherTax_reprint_sendCash;
    }

    public void setOtherTax_reprint_sendCash(String otherTax_reprint_sendCash) {
        this.otherTax_reprint_sendCash = otherTax_reprint_sendCash;
    }

    public String getIdDocumnetType_reprint_sendCash() {
        return IdDocumnetType_reprint_sendCash;
    }

    public void setIdDocumnetType_reprint_sendCash(String idDocumnetType_reprint_sendCash) {
        IdDocumnetType_reprint_sendCash = idDocumnetType_reprint_sendCash;
    }

    public String getSenderMobileNumber_reprint_sendCash() {
        return senderMobileNumber_reprint_sendCash;
    }

    public void setSenderMobileNumber_reprint_sendCash(String senderMobileNumber_reprint_sendCash) {
        this.senderMobileNumber_reprint_sendCash = senderMobileNumber_reprint_sendCash;
    }

    public String getIdDocumentCountryOffissue_reprint_sendCash() {
        return idDocumentCountryOffissue_reprint_sendCash;
    }

    public void setIdDocumentCountryOffissue_reprint_sendCash(String idDocumentCountryOffissue_reprint_sendCash) {
        this.idDocumentCountryOffissue_reprint_sendCash = idDocumentCountryOffissue_reprint_sendCash;
    }



    public String getSenderFirstName_reprint_sendCash() {
        return senderFirstName_reprint_sendCash;
    }

    public void setSenderFirstName_reprint_sendCash(String senderFirstName_reprint_sendCash) {
        this.senderFirstName_reprint_sendCash = senderFirstName_reprint_sendCash;
    }

    public String getSenderName_reprint_sendCash() {
        return senderName_reprint_sendCash;
    }

    public void setSenderName_reprint_sendCash(String senderName_reprint_sendCash) {
        this.senderName_reprint_sendCash = senderName_reprint_sendCash;
    }

    public String getDestinationCountryName_reprint_sendCash() {
        return destinationCountryName_reprint_sendCash;
    }

    public void setDestinationCountryName_reprint_sendCash(String destinationCountryName_reprint_sendCash) {
        this.destinationCountryName_reprint_sendCash = destinationCountryName_reprint_sendCash;
    }

    public String getDestinationName_reprint_sendCash() {
        return destinationName_reprint_sendCash;
    }

    public void setDestinationName_reprint_sendCash(String destinationName_reprint_sendCash) {
        this.destinationName_reprint_sendCash = destinationName_reprint_sendCash;
    }

    public String getDestinationMobileNumber_reprint_sendCash() {
        return destinationMobileNumber_reprint_sendCash;
    }

    public void setDestinationMobileNumber_reprint_sendCash(String destinationMobileNumber_reprint_sendCash) {
        this.destinationMobileNumber_reprint_sendCash = destinationMobileNumber_reprint_sendCash;
    }

    public String getSenderCurrencyCode_reprint_sendCash() {
        return senderCurrencyCode_reprint_sendCash;
    }

    public void setSenderCurrencyCode_reprint_sendCash(String senderCurrencyCode_reprint_sendCash) {
        this.senderCurrencyCode_reprint_sendCash = senderCurrencyCode_reprint_sendCash;
    }

    public String getAmount_reprint_sendCash() {
        return amount_reprint_sendCash;
    }

    public void setAmount_reprint_sendCash(String amount_reprint_sendCash) {
        this.amount_reprint_sendCash = amount_reprint_sendCash;
    }

    public String getFees_reprint_sendCash() {
        return fees_reprint_sendCash;
    }

    public void setFees_reprint_sendCash(String fees_reprint_sendCash) {
        this.fees_reprint_sendCash = fees_reprint_sendCash;
    }

    public String getAttachedBranchName_reprint_sendCash() {
        return attachedBranchName_reprint_sendCash;
    }

    public void setAttachedBranchName_reprint_sendCash(String attachedBranchName_reprint_sendCash) {
        this.attachedBranchName_reprint_sendCash = attachedBranchName_reprint_sendCash;
    }

    public String getSenderCountryName_reprint_sendCash() {
        return senderCountryName_reprint_sendCash;
    }

    public void setSenderCountryName_reprint_sendCash(String senderCountryName_reprint_sendCash) {
        this.senderCountryName_reprint_sendCash = senderCountryName_reprint_sendCash;
    }

    public String getComment_reprint_sendCash() {
        return comment_reprint_sendCash;
    }

    public void setComment_reprint_sendCash(String comment_reprint_sendCash) {
        this.comment_reprint_sendCash = comment_reprint_sendCash;
    }

    public String getTransactionId_reprint_sendCash() {
        return transactionId_reprint_sendCash;
    }

    public void setTransactionId_reprint_sendCash(String transactionId_reprint_sendCash) {
        this.transactionId_reprint_sendCash = transactionId_reprint_sendCash;
    }

    public String getDateTime_reprint_sendcash() {
        return dateTime_reprint_sendcash;
    }

    public void setDateTime_reprint_sendcash(String dateTime_reprint_sendcash) {
        this.dateTime_reprint_sendcash = dateTime_reprint_sendcash;
    }

    public String getReferenceNumber_reprint_sendcash() {
        return referenceNumber_reprint_sendcash;
    }

    public void setReferenceNumber_reprint_sendcash(String referenceNumber_reprint_sendcash) {
        this.referenceNumber_reprint_sendcash = referenceNumber_reprint_sendcash;
    }

    // ########################################## Reprint Receive Cash  #####################################


    String dateTime_reprint_receiveCash="",referenceNumber_reprint_receiveCash="",senderFirstName_reprint_receiveCash="",senderName_reprint_receiveCash="",destinationCountryName_reprint_receiveCash=""
            ,destinationName_reprint_receiveCash="",destinationMobileNumber_reprint_receiveCash="",senderCurrencyCode_reprint_receiveCash="",amount_reprint_receiveCash="",fees_reprint_receiveCash=""
            ,attachedBranchName_reprint_receiveCash="",senderCountryName_reprint_receiveCash,comment_reprint_receiveCash="",transactionId_reprint_receiveCash="",senderMobileNumber_reprint_receiveCash="",
            idDocumentCountryOffissue_reprint_receiveCash="",IdDocumnetType_reprint_receiveCash="",getIdDocumnetDateOfIssue_reprint_receiveCash="",otherTax_reprint_receiveCash="",tax_reprint_receiveCash="",
            destinationCurrencyCode_reprint_receiveCash="",amountToPay_reprint_receiveCashh="",questionName_reprint_receiveCash="",answerName_reprint_receiveCash="",destinationFirstName_reprint_receiveCash="",
         destinationLastName_reprint_receiveCash="",agentBranch_reprint_receiveCash="",idProofType_reprint_receiveCash="",idProofIssueDate_reprint_receiveCash="",vat_reprint_receiveCash="",senderLatName_reprint_receiveCash="";

    public String getAgentBranch_reprint_receiveCash() {
        return agentBranch_reprint_receiveCash;
    }

    public void setAgentBranch_reprint_receiveCash(String agentBranch_reprint_receiveCash) {
        this.agentBranch_reprint_receiveCash = agentBranch_reprint_receiveCash;
    }

    public String getSenderLatName_reprint_receiveCash() {
        return senderLatName_reprint_receiveCash;
    }

    public void setSenderLatName_reprint_receiveCash(String senderLatName_reprint_receiveCash) {
        this.senderLatName_reprint_receiveCash = senderLatName_reprint_receiveCash;
    }

    public String getVat_reprint_receiveCash() {
        return vat_reprint_receiveCash;
    }

    public void setVat_reprint_receiveCash(String vat_reprint_receiveCash) {
        this.vat_reprint_receiveCash = vat_reprint_receiveCash;
    }

    public String getIdProofType_reprint_receiveCash() {
        return idProofType_reprint_receiveCash;
    }

    public String getIdProofIssueDate_reprint_receiveCash() {
        return idProofIssueDate_reprint_receiveCash;
    }

    public void setIdProofIssueDate_reprint_receiveCash(String idProofIssueDate_reprint_receiveCash) {
        this.idProofIssueDate_reprint_receiveCash = idProofIssueDate_reprint_receiveCash;
    }

    public void setIdProofType_reprint_receiveCash(String idProofType_reprint_receiveCash) {
        this.idProofType_reprint_receiveCash = idProofType_reprint_receiveCash;
    }

    public String getDestinationLastName_reprint_receiveCash() {
        return destinationLastName_reprint_receiveCash;
    }

    public void setDestinationLastName_reprint_receiveCash(String destinationLastName_reprint_receiveCash) {
        this.destinationLastName_reprint_receiveCash = destinationLastName_reprint_receiveCash;
    }

    public String getDateTime_reprint_receiveCash() {
        return dateTime_reprint_receiveCash;
    }

    public void setDateTime_reprint_receiveCash(String dateTime_reprint_receiveCash) {
        this.dateTime_reprint_receiveCash = dateTime_reprint_receiveCash;
    }

    public String getReferenceNumber_reprint_receiveCash() {
        return referenceNumber_reprint_receiveCash;
    }

    public void setReferenceNumber_reprint_receiveCash(String referenceNumber_reprint_receiveCash) {
        this.referenceNumber_reprint_receiveCash = referenceNumber_reprint_receiveCash;
    }

    public String getSenderFirstName_reprint_receiveCash() {
        return senderFirstName_reprint_receiveCash;
    }

    public void setSenderFirstName_reprint_receiveCash(String senderFirstName_reprint_receiveCash) {
        this.senderFirstName_reprint_receiveCash = senderFirstName_reprint_receiveCash;
    }

    public String getSenderName_reprint_receiveCash() {
        return senderName_reprint_receiveCash;
    }

    public void setSenderName_reprint_receiveCash(String senderName_reprint_receiveCash) {
        this.senderName_reprint_receiveCash = senderName_reprint_receiveCash;
    }

    public String getDestinationCountryName_reprint_receiveCash() {
        return destinationCountryName_reprint_receiveCash;
    }

    public void setDestinationCountryName_reprint_receiveCash(String destinationCountryName_reprint_receiveCash) {
        this.destinationCountryName_reprint_receiveCash = destinationCountryName_reprint_receiveCash;
    }

    public String getDestinationName_reprint_receiveCash() {
        return destinationName_reprint_receiveCash;
    }

    public void setDestinationName_reprint_receiveCash(String destinationName_reprint_receiveCash) {
        this.destinationName_reprint_receiveCash = destinationName_reprint_receiveCash;
    }

    public String getDestinationMobileNumber_reprint_receiveCash() {
        return destinationMobileNumber_reprint_receiveCash;
    }

    public void setDestinationMobileNumber_reprint_receiveCash(String destinationMobileNumber_reprint_receiveCash) {
        this.destinationMobileNumber_reprint_receiveCash = destinationMobileNumber_reprint_receiveCash;
    }

    public String getSenderCurrencyCode_reprint_receiveCash() {
        return senderCurrencyCode_reprint_receiveCash;
    }

    public void setSenderCurrencyCode_reprint_receiveCash(String senderCurrencyCode_reprint_receiveCash) {
        this.senderCurrencyCode_reprint_receiveCash = senderCurrencyCode_reprint_receiveCash;
    }

    public String getAmount_reprint_receiveCash() {
        return amount_reprint_receiveCash;
    }

    public void setAmount_reprint_receiveCash(String amount_reprint_receiveCash) {
        this.amount_reprint_receiveCash = amount_reprint_receiveCash;
    }

    public String getFees_reprint_receiveCash() {
        return fees_reprint_receiveCash;
    }

    public void setFees_reprint_receiveCash(String fees_reprint_receiveCash) {
        this.fees_reprint_receiveCash = fees_reprint_receiveCash;
    }

    public String getAttachedBranchName_reprint_receiveCash() {
        return attachedBranchName_reprint_receiveCash;
    }

    public void setAttachedBranchName_reprint_receiveCash(String attachedBranchName_reprint_receiveCash) {
        this.attachedBranchName_reprint_receiveCash = attachedBranchName_reprint_receiveCash;
    }

    public String getSenderCountryName_reprint_receiveCash() {
        return senderCountryName_reprint_receiveCash;
    }

    public void setSenderCountryName_reprint_receiveCash(String senderCountryName_reprint_receiveCash) {
        this.senderCountryName_reprint_receiveCash = senderCountryName_reprint_receiveCash;
    }

    public String getComment_reprint_receiveCash() {
        return comment_reprint_receiveCash;
    }

    public void setComment_reprint_receiveCash(String comment_reprint_receiveCash) {
        this.comment_reprint_receiveCash = comment_reprint_receiveCash;
    }

    public String getTransactionId_reprint_receiveCash() {
        return transactionId_reprint_receiveCash;
    }

    public void setTransactionId_reprint_receiveCash(String transactionId_reprint_receiveCash) {
        this.transactionId_reprint_receiveCash = transactionId_reprint_receiveCash;
    }

    public String getSenderMobileNumber_reprint_receiveCash() {
        return senderMobileNumber_reprint_receiveCash;
    }

    public void setSenderMobileNumber_reprint_receiveCash(String senderMobileNumber_reprint_receiveCash) {
        this.senderMobileNumber_reprint_receiveCash = senderMobileNumber_reprint_receiveCash;
    }

    public String getIdDocumentCountryOffissue_reprint_receiveCash() {
        return idDocumentCountryOffissue_reprint_receiveCash;
    }

    public void setIdDocumentCountryOffissue_reprint_receiveCash(String idDocumentCountryOffissue_reprint_receiveCash) {
        this.idDocumentCountryOffissue_reprint_receiveCash = idDocumentCountryOffissue_reprint_receiveCash;
    }

    public String getIdDocumnetType_reprint_receiveCash() {
        return IdDocumnetType_reprint_receiveCash;
    }

    public void setIdDocumnetType_reprint_receiveCash(String idDocumnetType_reprint_receiveCash) {
        IdDocumnetType_reprint_receiveCash = idDocumnetType_reprint_receiveCash;
    }

    public String getGetIdDocumnetDateOfIssue_reprint_receiveCash() {
        return getIdDocumnetDateOfIssue_reprint_receiveCash;
    }

    public void setGetIdDocumnetDateOfIssue_reprint_receiveCash(String getIdDocumnetDateOfIssue_reprint_receiveCash) {
        this.getIdDocumnetDateOfIssue_reprint_receiveCash = getIdDocumnetDateOfIssue_reprint_receiveCash;
    }

    public String getOtherTax_reprint_receiveCash() {
        return otherTax_reprint_receiveCash;
    }

    public void setOtherTax_reprint_receiveCash(String otherTax_reprint_receiveCash) {
        this.otherTax_reprint_receiveCash = otherTax_reprint_receiveCash;
    }

    public String getTax_reprint_receiveCash() {
        return tax_reprint_receiveCash;
    }

    public void setTax_reprint_receiveCash(String tax_reprint_receiveCash) {
        this.tax_reprint_receiveCash = tax_reprint_receiveCash;
    }

    public String getDestinationCurrencyCode_reprint_receiveCash() {
        return destinationCurrencyCode_reprint_receiveCash;
    }

    public void setDestinationCurrencyCode_reprint_receiveCash(String destinationCurrencyCode_reprint_receiveCash) {
        this.destinationCurrencyCode_reprint_receiveCash = destinationCurrencyCode_reprint_receiveCash;
    }

    public String getAmountToPay_reprint_receiveCashh() {
        return amountToPay_reprint_receiveCashh;
    }

    public void setAmountToPay_reprint_receiveCashh(String amountToPay_reprint_receiveCashh) {
        this.amountToPay_reprint_receiveCashh = amountToPay_reprint_receiveCashh;
    }

    public String getQuestionName_reprint_receiveCash() {
        return questionName_reprint_receiveCash;
    }

    public void setQuestionName_reprint_receiveCash(String questionName_reprint_receiveCash) {
        this.questionName_reprint_receiveCash = questionName_reprint_receiveCash;
    }

    public String getAnswerName_reprint_receiveCash() {
        return answerName_reprint_receiveCash;
    }

    public void setAnswerName_reprint_receiveCash(String answerName_reprint_receiveCash) {
        this.answerName_reprint_receiveCash = answerName_reprint_receiveCash;
    }

    public String getDestinationFirstName_reprint_receiveCash() {
        return destinationFirstName_reprint_receiveCash;
    }

    public void setDestinationFirstName_reprint_receiveCash(String destinationFirstName_reprint_receiveCash) {
        this.destinationFirstName_reprint_receiveCash = destinationFirstName_reprint_receiveCash;
    }

    // ############################## 17 october EUI / TUTION FEES #######################

    String senderMobileNumberRegisterCheck = "";
    String receiverMobileNumberRegisterCheck = "";
    String professionAgentIdentity = "";
    String nationalityAgentIdentity = "";
    String cityAgentIdentity = "";

    public String getReceiverMobileNumberRegisterCheck() {
        return receiverMobileNumberRegisterCheck;
    }

    public void setReceiverMobileNumberRegisterCheck(String receiverMobileNumberRegisterCheck) {
        this.receiverMobileNumberRegisterCheck = receiverMobileNumberRegisterCheck;
    }

    public String getCityAgentIdentity() {
        return cityAgentIdentity;
    }

    public void setCityAgentIdentity(String cityAgentIdentity) {
        this.cityAgentIdentity = cityAgentIdentity;
    }

    public String getNationalityAgentIdentity() {
        return nationalityAgentIdentity;
    }

    public void setNationalityAgentIdentity(String nationalityAgentIdentity) {
        this.nationalityAgentIdentity = nationalityAgentIdentity;
    }

    public String getProfessionAgentIdentity() {
        return professionAgentIdentity;
    }

    public void setProfessionAgentIdentity(String professionAgentIdentity) {
        this.professionAgentIdentity = professionAgentIdentity;
    }

    public String getSenderMobileNumberRegisterCheck() {
        return senderMobileNumberRegisterCheck;
    }

    public void setSenderMobileNumberRegisterCheck(String senderMobileNumberRegisterCheck) {
        this.senderMobileNumberRegisterCheck = senderMobileNumberRegisterCheck;
    }

    // ################################################################

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {

            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

}
