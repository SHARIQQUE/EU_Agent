package agent.internationalremittance.model;

import android.content.Context;

import java.util.Random;

import commonutilities.ComponentMd5SharedPre;


public class InternationalRemittanceModel {
    ComponentMd5SharedPre mComponentInfo;
String agentCode, agentName, sendMessage, reciveMessage, tokenNo;

    public String getSendMessage() {

        sendMessage="Dear "+agentName+", your transaction of sending remittance to %2$s from %1$s is successfull. Please use "+getTokenNo()+" token for recieving the remittance.";

        return sendMessage;
    }

    public String getReciveMessage() {
        reciveMessage="Dear "+agentName+", your transaction of receiving remittance to %1$s from %2$s is successfull. ";

        return reciveMessage;
    }

    public String getTokenNo() {

        tokenNo=    String.valueOf(6 < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, 6 - 1)) - 1)
                + (int) Math.pow(10, 6 - 1));
        return tokenNo;
    }

    String[] iroArr, countryNameArr,countryCodeArr, countryISOCodeArr, countryMobileLengthArr, currencyArr ;
int []fees;
    public InternationalRemittanceModel(ComponentMd5SharedPre mComponentInfo,Context context){

        this.mComponentInfo=mComponentInfo;

    }

    public String[] getCountryNameArr() {

        if(countryNameArr==null|| countryNameArr.length==0){
            generateData();
        }

        return countryNameArr;
    }

    public String[] getCountryCodeArr() {

        if(countryCodeArr==null|| countryCodeArr.length==0){
            generateData();
        }

        return countryCodeArr;
    }

    public String[] getCountryISOCodeArr() {

        if(countryISOCodeArr==null|| countryISOCodeArr.length==0){
            generateData();
        }

        return countryISOCodeArr;
    }

    public String[] getCountryMobileLengthArr() {

        if(countryMobileLengthArr==null|| countryMobileLengthArr.length==0){
            generateData();
        }

        return countryMobileLengthArr;
    }

    public String[] getCurrencyArr() {
        return currencyArr;
    }

    public String[] getIroArr() {

        if(iroArr==null|| iroArr.length==0){
            generateData();
        }
        return iroArr;
    }

    public String getAgentCode() {

        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        return agentCode;
    }

    public String getAgentName() {
        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");

        return agentName;
    }

    public int[] getFees() {
        return fees;
    }

    private void generateData(){

       iroArr=new String[5];
       iroArr[0]="Please Select Remittance Operator";
       iroArr[1]="Western Union Money Transfer";
       iroArr[2]="Money Gram";
       iroArr[3]="World Remit";
       iroArr[4]="RiA";

        currencyArr=new String[3];
        currencyArr[0]="Please Select Recipient Currency";
        currencyArr[1]="$ - USA";
        currencyArr[2]="INR - India";
       // currencyArr[3]="XAF - Cameroon";

        fees=new int[3];
        fees[0]=0;
        fees[1]=2;
        fees[2]=10;
      // fees[3]=0;


        countryNameArr = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
        countryCodeArr = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
        countryISOCodeArr = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
        countryMobileLengthArr = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

    }
}
