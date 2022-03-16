package model;

import java.util.ArrayList;
import java.util.HashMap;


public class GeneralResponseModel {

    String userDefinedString = "";
    String userDefinedString_EUI = "";
    String userDefinedString3 = "";
    String userDefinedString1 = "";
    String userDefinedString2="";
    String userDefinedString4="";
    String userDefinedString5="";
    String userDefinedString6="";
    String userDefinedCurrency="";
    String reasonTranfer="";

    public String getUserDefinedString_EUI() {
        return userDefinedString_EUI;
    }

    public void setUserDefinedString_EUI(String userDefinedString_EUI) {
        this.userDefinedString_EUI = userDefinedString_EUI;
    }

    public String getReasonTranfer() {
        return reasonTranfer;
    }

    public void setReasonTranfer(String reasonTranfer) {
        this.reasonTranfer = reasonTranfer;
    }

    public String getUserDefinedCurrency() {
        return userDefinedCurrency;
    }

    public void setUserDefinedCurrency(String userDefinedCurrency) {
        this.userDefinedCurrency = userDefinedCurrency;
    }



    String userDefinedCityCreateAgent="";

    public String getUserDefinedCityCreateAgent() {
        return userDefinedCityCreateAgent;
    }

    public void setUserDefinedCityCreateAgent(String userDefinedCityCreateAgent) {
        this.userDefinedCityCreateAgent = userDefinedCityCreateAgent;
    }

    public String getUserDefinedString4() {
        return userDefinedString4;
    }

    public void setUserDefinedString4(String userDefinedString4) {
        this.userDefinedString4 = userDefinedString4;
    }

    public String getUserDefinedString5() {
        return userDefinedString5;
    }

    public void setUserDefinedString5(String userDefinedString5) {
        this.userDefinedString5 = userDefinedString5;
    }

    public String getUserDefinedString6() {
        return userDefinedString6;
    }

    public void setUserDefinedString6(String userDefinedString6) {
        this.userDefinedString6 = userDefinedString6;
    }

    ArrayList<OtherAccountModel> otherAccountModels;

    public ArrayList<OtherAccountModel> getOtherAccountModels() {
        return otherAccountModels;
    }

    public void setOtherAccountModels(ArrayList<OtherAccountModel> otherAccountModels) {
        this.otherAccountModels = otherAccountModels;
    }

    public String getUserDefinedString1() {
        return userDefinedString1;
    }

    public void setUserDefinedString1(String userDefinedString1) {
        this.userDefinedString1 = userDefinedString1;
    }

    String planNamePlanString;
    int responseCode;

    public String getUserDefinedString3() {
        return userDefinedString3;
    }

    public void setUserDefinedString3(String userDefinedString3) {
        this.userDefinedString3 = userDefinedString3;
    }

    ArrayList<String> customResponseList;

    boolean isOTP;
    HashMap<String, String> responseList;
    ArrayList<String> OtherAccountResponseList;


    public String getPlanNamePlanString() {
        return planNamePlanString;
    }

    public void setPlanNamePlanString(String planNamePlanString) {
        this.planNamePlanString = planNamePlanString;
    }



    public String getUserDefinedString2() {
        return userDefinedString2;
    }

    public void setUserDefinedString2(String userDefinedString2) {
        this.userDefinedString2 = userDefinedString2;
    }

    public ArrayList<String> getOtherAccountResponseList() {
        return OtherAccountResponseList;
    }

    public void setOtherAccountResponseList(ArrayList<String> otherAccountResponseList) {
        OtherAccountResponseList = otherAccountResponseList;
    }

    public ArrayList<String> getCustomResponseList() {
        return customResponseList;
    }

    public boolean isOTP() {
        return isOTP;
    }

    public void setOTP(boolean OTP) {
        isOTP = OTP;
    }

    public void setCustomResponseList(ArrayList<String> customResponseList) {

        this.customResponseList = customResponseList;
    }

    public HashMap<String, String> getResponseList() {
        return responseList;
    }

    public void setResponseList(HashMap<String, String> responseList) {
        this.responseList = responseList;
    }

    public String getUserDefinedString() {
        return userDefinedString;
    }

    public void setUserDefinedString(String userDefinedString) {
        System.out.print(userDefinedString);
        this.userDefinedString = userDefinedString;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
