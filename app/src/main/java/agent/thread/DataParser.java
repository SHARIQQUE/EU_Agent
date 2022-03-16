package agent.thread;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import commonutilities.CommonUtilities;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;
import model.OtherAccountModel;


public class DataParser {

    ArrayList<String> listdata;
    String transIdTransactionCode;
    String appVersion = "";
    String description = "";
    Context ctx;
    private ComponentMd5SharedPre mComponentInfo;
    ArrayList<OtherAccountModel> otherList;

    public GeneralResponseModel getCountryData(String jsonString) {


        mComponentInfo = (ComponentMd5SharedPre) ctx;

        String countryList = "", countryCodeList = "", countryPrefixCodeList = "", mobileNoLength = "", cashoutAmount = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("countrylist"));

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    countryList = countryList + "|" + jObject.getString("countryname");
                    countryCodeList = countryCodeList + "|" + jObject.getString("countrycode");
                    countryPrefixCodeList = countryPrefixCodeList + "|" + jObject.getString("isocode");
                    cashoutAmount = cashoutAmount + "|" + jObject.getString("cashoutamount");

                    if (jObject.has("mobilelength")) {
                        mobileNoLength = mobileNoLength + "|" + jObject.getString("mobilelength");
                    } else {
                        mobileNoLength = mobileNoLength + "|9";
                    }
                }
                appVersion = jsonObject.getString("apversionba");
                description = jsonObject.getString("resultdescription");

                countryList = "Please Select Country" + countryList;
                countryCodeList = "Please Select Country Code" + countryCodeList;
                countryPrefixCodeList = "Please Select Country Code" + countryPrefixCodeList;
                mobileNoLength = "Please Select Country Code" + mobileNoLength;
                cashoutAmount = "Cash out Amount " + cashoutAmount;


                responseModel.setUserDefinedString(countryList + ";" + countryCodeList + ";" + countryPrefixCodeList + ";" + mobileNoLength + ";" + appVersion + ";" + description + ";" + cashoutAmount);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setUserDefinedString(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }


    public GeneralResponseModel getCountryData_EUI(String jsonString) {


        mComponentInfo = (ComponentMd5SharedPre) ctx;

        String euithreshold = "", countryList = "", countryCodeList = "", countryPrefixCodeList = "", mobileNoLength = "", currencyList = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("countrylist"));

                JSONArray sortedJsonArray = new JSONArray();

                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonValues.add(jsonArray.getJSONObject(i));
                }
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    //You can change "Name" with "ID" if you want to sort by ID
                    private static final String KEY_NAME = "countryname";

                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();

                        try {
                            valA = (String) a.get(KEY_NAME);
                            valB = (String) b.get(KEY_NAME);
                        } catch (JSONException e) {
                            //do something
                        }

                        return valA.compareTo(valB);
                        //if you want to change the sort order, simply use the following:
                        //return -valA.compareTo(valB);
                    }
                });

                for (int i = 0; i < jsonArray.length(); i++) {
                    sortedJsonArray.put(jsonValues.get(i));
                }

                for (int i = 0; i < sortedJsonArray.length(); i++) {

                    JSONObject jObject = sortedJsonArray.getJSONObject(i);

                    countryList = countryList + "|" + jObject.getString("countryname");
                    countryCodeList = countryCodeList + "|" + jObject.getString("countrycode");
                    countryPrefixCodeList = countryPrefixCodeList + "|" + jObject.getString("isocode");
                    currencyList = currencyList + "|" + jObject.getString("currency");
                    euithreshold = euithreshold + "|" + jObject.getString("euithreshold");

                    if (jObject.has("mobilelength")) {
                        mobileNoLength = mobileNoLength + "|" + jObject.getString("mobilelength");
                    } else {
                        mobileNoLength = mobileNoLength + "|9";
                    }
                }
                //  appVersion = jsonObject.getString("apversionba");  // not comin
                description = jsonObject.getString("resultdescription");

                countryList = "Please Select Country" + countryList;
                countryCodeList = "Please Select Country Code" + countryCodeList;
                countryPrefixCodeList = "Please Select Country Code" + countryPrefixCodeList;
                mobileNoLength = "Please Select Country Code" + mobileNoLength;
                currencyList = "Currency  " + currencyList;
                euithreshold = "Thresholderamount  " + euithreshold;


                responseModel.setUserDefinedString_EUI(countryList + ";" + countryCodeList + ";" + countryPrefixCodeList + ";" + mobileNoLength + ";" + appVersion + ";" + description + ";" + currencyList + ";" + euithreshold);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setUserDefinedString_EUI(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString_EUI("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }

    public GeneralResponseModel getLoginResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);
                HashMap<String, String> dataHashMap = new HashMap<String, String>();
                try {

                    JSONObject loginJsonObj = new JSONObject(jsonString);

                    Iterator<String> keysItr = loginJsonObj.keys();
                    while (keysItr.hasNext()) {
                        String key = keysItr.next();
                        Object value = loginJsonObj.get(key);


                         /*if(value instanceof JSONObject) {
                            value = getLoginResponse((JSONObject) value);
                        }*/
                        dataHashMap.put(key, value.toString());
                    }
                    generalResponseModel.setResponseList(dataHashMap);
                    if (generalResponseModel.getResponseList().get("otp").trim().length() == 6) {

                        if (generalResponseModel.getResponseList().get("otp").trim().length() == 6) {
                            generalResponseModel.setUserDefinedString("OTP");
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }

                    }
                } catch (Exception e) {
                    generalResponseModel.setResponseCode(99);
                    generalResponseModel.setUserDefinedString("Please try again later");
                }

            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentInfoResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String bankDetails = "";
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {


                        String ss = obj.getString("accounts");

               /*for(int j=0;j<ss.length();j++){
                   Log.e(""+j,""+ss.charAt(j));

               }*/

                        JSONArray jsonArray = new JSONArray(ss);


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject objj = jsonArray.getJSONObject(i);

                            if (bankDetails.trim().length() == 0) {
                                bankDetails = objj.getString("accountno") + "|" + objj.getString("accounttype")
                                        + "|" + objj.getString("accountstatus") + "|" + objj.getString("mobileno") + "|" + objj.getString("accounttypecode");
                            } else {
                                bankDetails = bankDetails + ";" + objj.getString("accountno") + "|" + objj.getString("accounttype")
                                        + "|" + objj.getString("accountstatus") + "|" + objj.getString("mobileno") + "|" + objj.getString("accounttypecode");
                            }


                        }
                        generalResponseModel.setUserDefinedString(bankDetails);
                        generalResponseModel.setUserDefinedString1(obj.getString("state"));

                    } catch (Exception e) {

                        generalResponseModel.setResponseCode(99);
                        generalResponseModel.setUserDefinedString("Please try again later");
                    }

                    //   JSONArray jsonArray=new JSONArray(obj.getString("accounts").trim());


                /*ss = ss.replace("[", "");
                ss = ss.replace("]", "");
                ss = ss.replace("{", "");
                ss = ss.replace("}", "");
                String[] data = ss.split("\\,");*/
                    //  JSONArray jArray=new JSONArray(ss);
               /* ss = "Please Select Account";
                for (int i = 0; i < data.length; i++) {
                    ss = ss + "|" + data[i].replaceAll("=", ": ").toUpperCase();


                }*/


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getActivationResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("transid")
                            + "|" + obj.getString("resultdescription")
                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentListOrderDetails(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        JSONObject obj;
        try {
            obj = new JSONObject(jsonString);
            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    String ss = obj.getString("agentlist");

                    JSONArray jsonArray = new JSONArray(ss);
                    String agentCodeList = "", agentDetails = "", agentnameList = "", temData = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);

                        agentCodeList = agentCodeList + "|" + objj.getString("agentcode");
                    }

                    generalResponseModel.setUserDefinedString(agentCodeList);
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                    System.out.println();

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                }

            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        System.out.print(generalResponseModel);
        return generalResponseModel;
    }


    public GeneralResponseModel getBillerResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);
                String ss = obj.getString("billers");

                JSONArray jsonArray = new JSONArray(ss);
                String billerName = "", billerCode = "";

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objj = jsonArray.getJSONObject(i);

                    if (billerCode.length() == 0) {
                        billerCode = objj.getString("billercode");
                        billerName = objj.getString("billername");

                    } else {
                        billerCode = billerCode + "|" + objj.getString("billercode");
                        billerName = billerName + "|" + objj.getString("billername");
                    }
                }

                generalResponseModel.setUserDefinedString(billerName + ";" + billerCode);
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                System.out.println();

            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        System.out.print(generalResponseModel);

        return generalResponseModel;
    }

    public GeneralResponseModel getprepaidElectricityResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("billername")
                            + "|" + obj.getString("invoiceno")
                            + "|" + obj.getString("sessionid")
                            + "|" + obj.getString("fee")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("customername")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getprepaidElectricityBillpayResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid")
                                    + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    //+  obj.getString("fee") + "|"
                                    + obj.getString("amount")
                                    + "|" + obj.getString("source")
                                    // + "|" + obj.getString("sourcename")
                                    //+ "|" + obj.getString("invoiceno")
                                    + "|" + obj.getString("energyconst")
                                    + "|" + obj.getString("energy")
                                    + "|" + obj.getString("prdordno")
                                    + "|" + obj.getString("billername")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getActiverViwverResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCommissionForTransfer(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {


                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getOrderTransferApprovalResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("requestcts")
                            + "|" + obj.getString("destination")
                            + "|" + obj.getString("source")

                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getBillerCodeResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("billercode"));

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }
        return generalResponseModel;
    }

    public GeneralResponseModel getResendSmsConfCodeResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getResendOtpVerifyUpdateAccountResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getResendSmsConfCodeSecondPhaseResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getResetMpinConfcodeResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel updateAccount(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|"
                            + obj.getString("responsects") + "|"
                            + obj.getString("source") + "|"
                            + obj.getString("transid") + "|"
                            + obj.getString("state") + "|"
                            + obj.getString("country") + "|"
                            + obj.getString("parent") + "|"
                            + obj.getString("tla") + "|"     // tla is a agent branch name
                            + obj.getString("resultdescription") + "|"   // sourcename temprary sourcename is not coming
                            + obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getWithdrawalSecondPhaseResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("transid") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("destbranch")
                            + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("tax")
                            + "|" + obj.getString("resultdescription")

                    );

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getResetMpinResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAccountActiverResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }


    public GeneralResponseModel getPictureSignResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString("pictureSign" + "|"
                            + obj.getString("image") + "|"
                            + obj.getString("Sign") + "|"
                            + obj.getString("transid") + "|"
                            + obj.getString("idproof") + "|"
                            + obj.getString("agentname") +

                            "|" + obj.getString("idproof") +   // id proof number 15 nov 2018
                            "|" + obj.getString("idproofissueplace") + // id proof issue place 15 nov 2018
                            "|" + obj.getString("idproofissuedate") +   // id proof issue date number 15 nov 2018
                            "|" + obj.getString("firstname") +   // subscriber name
                            "|" + obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel serachImoneyDeposit(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                    generalResponseModel.setUserDefinedString(

                            obj.getString("resultdescription")
                                    + "|" + obj.getString("accountnumber")
                                    + "|" + obj.getString("cutomername")

                                    + "|" + obj.getString("idtype")
                                    + "|" + obj.getString("idnumber")
                                    + "|" + obj.getString("place")
                                    + "|" + obj.getString("expiry")   // date
                                    + "|" + obj.getString("resultdescription")

                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashToCashTransferDeposit(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                    generalResponseModel.setUserDefinedString(obj.getString("transid") + "|"     // locality
                            + obj.getString("resultdescription") + "|"
                            + obj.getString("resultdescription") + "|"
                            + obj.getString("requestcts") + "|"     // date time
                            + obj.getString("country") + "|"
                            + obj.getString("fees") + "|"
                            + obj.getString("agentbranch") + "|"
                            + obj.getString("resultdescription"));

                } else if (obj.getString("resultcode").equalsIgnoreCase("122")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("122");

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCashToCashTransferWithdrawal(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                    generalResponseModel.setUserDefinedString(obj.getString("transid") + "|"
                            + obj.getString("resultdescription") + "|"
                            + obj.getString("resultdescription") + "|"
                            + obj.getString("requestcts") + "|"
                            + obj.getString("country") + "|"
                            + obj.getString("fees") + "|"
                            + obj.getString("agentbranch") + "|"
                            + obj.getString("resultdescription")); // set on Page not change index if change aloso chnage on CashtoCash Trasfer 165

                } else if (obj.getString("resultcode").equalsIgnoreCase("122")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("122");

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReportAgentBank(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }
                    String ss = obj.getString("transactions");
                    ArrayList<String> list = new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(ss);
                    String transhistoryRecords = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);
                        if (objj.has("reason")) {
                            transhistoryRecords = objj.getString("transid") + "|" +
                                    objj.getString("amount") + "|"
                                    + objj.getString("direction")
                                    + "|" + objj.getString("currency")
                                    + "|" + objj.getString("date");
                        } else {
                            transhistoryRecords =


                                    objj.getString("closingbalance")
                                            + "|" + objj.getString("transdate")
                                            + "|" + objj.getString("transactiontype")
                                            + "|" + objj.getString("transactionamount")
                                            + "|" + objj.getString("debit")
                                            + "|" + objj.getString("credit")
                                            + "|" + objj.getString("id");

                            ;
                        }
                        list.add(transhistoryRecords);
                        transhistoryRecords = "";
                    }
                    generalResponseModel.setCustomResponseList(list);
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getOtpVerify(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                    generalResponseModel.setUserDefinedString(

                            obj.getString("resultdescription")
                                    + "|" + obj.getString("transid")
                                    + "|" + obj.getString("resultdescription")
                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getuploadImage_first(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_second(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_third(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_four(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_five(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_six(String jsonString) {


        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getuploadImage_profilePic(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_idFront(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_idBack(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_signature(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_billHome(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getuploadImage_form(String jsonString) {


        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel pictureSignatureServerRequest_cashoutAmount_authorize(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription") +
                        "|" + obj.getString("amount") +
                        "|" + obj.getString("agenttype") +
                        "|" + obj.getString("resultdescription"));
            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel taxAndCommint(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("customercharge") +         // fees
                                    "|" + obj.getString("taxcharged") +     // tax
                                    "|" + obj.getString("localamount") +   // amount from server
                                    "|" + obj.getString("payoutamount") +   // amount to pay  from server
                                    "|" + obj.getString("isfeeused") + // 4
                                    "|" + obj.getString("resultdescription") + // 04 set
                                    "|" + obj.getString("vat")  // vat
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel get_cashToCash(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("resultdescription") +
                                    "|" + obj.getString("transactiondatetime") +   // 1
                                    "|" + obj.getString("transid") +   // 2
                                    "|" + obj.getString("refrencenumber") +   // 3
                                    "|" + obj.getString("resultdescription") +   // 4
                                    "|" + obj.getString("resultdescription") +   // 5
                                    "|" + obj.getString("resultdescription")


                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel get_cashToReceive(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("resultDescription") +
                                    "|" + obj.getString("requestcts") +   // 1
                                    "|" + obj.getString("transid") +   // 2
                                    "|" + obj.getString("destcountry") +   // 3
                                    "|" + obj.getString("country") +   // 4
                                    "|" + obj.getString("agentbranch") +   // 5
                                    "|" + obj.getString("DestName") +
                                    "|" + obj.getString("refrencenumber") + // 7 refrencenumber number set
                                    "|" + obj.getString("resultDescription")


                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultDescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }


    public GeneralResponseModel get_searchInt(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("resultdescription") +
                                    "|" + obj.getString("changerate") +   // 1
                                    "|" + obj.getString("senderemail") +   // 2
                                    "|" + obj.getString("beneficiarylastname") +   // 3
                                    "|" + obj.getString("senderdatebirth") +   // 4
                                    "|" + obj.getString("senderidnumber") +   // 5
                                    "|" + obj.getString("senderdatebirth") +   // 6
                                    "|" + obj.getString("exchangerate") +   // 7
                                    "|" + obj.getString("sendertown") +   // 8
                                    "|" + obj.getString("destinationcountry") +   // 9
                                    "|" + obj.getString("beneficiaryfirstname") +   // 10
                                    "|" + obj.getString("beneficiarydatebirth") +   // 11
                                    "|" + obj.getString("senderidtype") +   // 12
                                    "|" + obj.getString("question") +   // 13
                                    "|" + obj.getString("beneficiaryphone") +   // 14
                                    "|" + obj.getString("beneficiaryfirstname") +   // 15
                                    "|" + obj.getString("senderiddeleveryplace") +   // 16
                                    "|" + obj.getString("amountsent") +   // 17
                                    "|" + obj.getString("amounttopay") +   // 18
                                    "|" + obj.getString("senderiddeleverydate") +   // 19
                                    "|" + obj.getString("beneficiaryaddress") +   // 20
                                    "|" + obj.getString("answer") +   // 21
                                    "|" + obj.getString("beneficiaryaddress") +   // 22
                                    "|" + obj.getString("sendercountry") +   // 23
                                    "|" + obj.getString("beneficiaryaddress") +   // 24
                                    "|" + obj.getString("senderaddress") +   // 25
                                    "|" + obj.getString("senderfirstname") +   // 26
                                    "|" + obj.getString("senderlastname") +   // 27
                                    "|" + obj.getString("beneficiaryemail") +   // 28
                                    "|" + obj.getString("beneficiarycountry") +   // 29
                                    "|" + obj.getString("senderphone") +  // 30
                                    "|" + obj.getString("feettc") +  // 31
                                    "|" + obj.getString("sendercurrency") +  // 32 set currency Sender ???? cehck server tag
                                    "|" + obj.getString("destinationcurrency") +  //  33 set currency Destination  ???? cehck server tag
                                    "|" + obj.getString("motif") +  //  34  reason off trasfer
//                                    "|" + obj.getString("vat") +  // 35
                                    "|" + obj.getString("resultdescription")   // 36


                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getCurrency(String jsonString) {

        String countrySender = "", currencySender = "", resultdescription = "", countrycode = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("currencyArray"));


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    currencySender = currencySender + "|" + jObject.getString("currency");
                }

                currencySender = "Sending currency" + currencySender;
                countrySender = jsonObject.getString("country");
                resultdescription = jsonObject.getString("resultdescription");
                countrycode = jsonObject.getString("countrycode");


                responseModel.setUserDefinedCurrency(currencySender + ";" + resultdescription + ";" + countrySender + ";" + countrycode);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setUserDefinedCurrency(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedCurrency("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }

    public GeneralResponseModel getCurrencyDestination(String jsonString) {

        String countrySender = "", currencySender = "", resultdescription = "", countrycode = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("currencyArray"));


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    currencySender = currencySender + "|" + jObject.getString("currency");
                }

                currencySender = "Destination currency" + currencySender;
                countrySender = jsonObject.getString("country");
                resultdescription = jsonObject.getString("resultdescription");
                countrycode = jsonObject.getString("countrycode");


                responseModel.setUserDefinedCurrency(currencySender + ";" + resultdescription + ";" + countrycode);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setUserDefinedCurrency(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedCurrency("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }


    public GeneralResponseModel getIdproofType(String jsonString) {

        String resultdescription = "", idProofTypeName = "", idProofType_code = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            //JSONObject jsonObject = new JSONObject(jsonString);


            JSONArray jsonArray = new JSONArray(jsonString);


            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject jObject = jsonArray.getJSONObject(i);


                idProofTypeName = idProofTypeName + "|" + jObject.getString("libelle");
                idProofType_code = idProofType_code + "|" + jObject.getString("code");

            }

            idProofTypeName = "id proof name " + idProofTypeName;
            idProofType_code = "id proof code " + idProofType_code;


            responseModel.setUserDefinedString(idProofTypeName + ";" + idProofType_code);
            responseModel.setResponseCode(0);


        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }


    public GeneralResponseModel getReasonList(String jsonString) {

        String reasonNameEnglish = "", reasonNameFrench = "", resultdescription = "", reasonCode = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("reasonArray"));


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    reasonCode = reasonCode + "|" + jObject.getString("CODE");
                    reasonNameEnglish = reasonNameEnglish + "|" + jObject.getString("REASONONE");
                    reasonNameFrench = reasonNameFrench + "|" + jObject.getString("REASONTWO");
                }

                reasonCode = "Reason Code" + reasonCode;
                reasonNameEnglish = "Reason of the transfer" + reasonNameEnglish;
                reasonNameFrench = "Motif du transfert" + reasonNameFrench;
                //countrySender = jsonObject.getString("country");
                resultdescription = jsonObject.getString("resultdescription");


                responseModel.setReasonTranfer(reasonCode + ";" + reasonNameEnglish + ";" + reasonNameFrench);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setReasonTranfer(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setReasonTranfer("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }

    public GeneralResponseModel getFeesDetails(String jsonString) {

        String fee_id = "", fee_name = "", fee_amount = "", partiality_amount = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success
            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    fee_id = fee_id + jObject.getString("fee_id") + "|";
                    fee_name = fee_name + jObject.getString("fee_name") + "|";
                    fee_amount = fee_amount + jObject.getString("fee_amount") + "|";
                    partiality_amount = partiality_amount + jObject.getString("partiality") + "|";
                }

                //  fee_id = fee_id;
                // fee_name = fee_name;
                // fee_amount =fee_amount;

                responseModel.setUserDefinedString(fee_id + ";" + fee_name + ";" + fee_amount + ";" + partiality_amount);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }


    public GeneralResponseModel getOptionDetails(String jsonString) {

        String optionName = "", optionId = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success
            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    optionName = optionName + "|" + jObject.getString("option_name");
                    optionId = optionId + "|" + jObject.getString("option_id");
                }

                optionName = "Option Name" + optionName;
                optionId = "Option Id" + optionId;

                responseModel.setUserDefinedString(optionName + ";" + optionId);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }

    public GeneralResponseModel getLevelDetails(String jsonString) {

        String LevelName = "", levelCode = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success
            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    LevelName = LevelName + "|" + jObject.getString("class_name");
                    levelCode = levelCode + "|" + jObject.getString("class_id");
                }

                LevelName = "Level Name" + LevelName;
                levelCode = "Class name" + levelCode;


                responseModel.setUserDefinedString(LevelName + ";" + levelCode);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }

    public GeneralResponseModel getFindSchool_byRegistrationNumber(String jsonString) {

        String schoolName = "", schoolCode = "", regionName = "", regionCode = "", division = "", subdivision = "", city = "";
        String student_registrationNo = "", student_student_name = "", student_student_birthdate = "", student_student_gender = "", student_student_phone = "", student_student_email = "";


        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success
            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    schoolName = schoolName + "|" + jObject.getString("school_name");
                    schoolCode = schoolCode + "|" + jObject.getString("school_code");
                    regionName = regionName + "|" + jObject.getString("region_name");
                    regionCode = regionCode + "|" + jObject.getString("region_code");
                    division = division + "|" + jObject.getString("division");
                    subdivision = subdivision + "|" + jObject.getString("subdivision");
                    city = city + "|" + jObject.getString("city");
                    student_registrationNo = student_registrationNo + "|" + jObject.getString("student_regnumber");
                    student_student_name = student_student_name + "|" + jObject.getString("student_name");
                    student_student_birthdate = student_student_birthdate + "|" + jObject.getString("student_birthdate");
                    student_student_gender = student_student_gender + "|" + jObject.getString("student_gender");
                    student_student_phone = student_student_phone + "|" + jObject.getString("student_phone");
                    student_student_email = student_student_email + "|" + jObject.getString("student_email");

                }

                schoolName = "School Name" + schoolName;
                schoolCode = "School Code" + schoolCode;
                regionName = "Region Name" + regionName;
                regionCode = "Region Code" + regionCode;
                division = "Division" + division;
                subdivision = "Subdivision" + subdivision;
                city = "City" + city;

                student_registrationNo = "Student registration No" + student_registrationNo + "|" + "Transaction Successfull";
                student_student_name = "Student name" + student_student_name;
                student_student_birthdate = "Student birth date" + student_student_birthdate + "|" + "Transaction Successfull";
                student_student_gender = "Student gender" + student_student_gender + "|" + "Transaction Successfull";
                student_student_phone = "Student Phone no" + student_student_phone + "|" + "Transaction Successfull";
                student_student_email = "Student email" + student_student_email + "|" + "Transaction Successfull";

                //  resultdescription  = jsonObject.getString("resultdescription");

                responseModel.setUserDefinedString(schoolName + ";" + schoolCode + ";" + regionName + ";" + regionCode + ";" + division + ";" + subdivision + ";" + city
                        + ";" + student_registrationNo + ";" + student_student_name + ";" + student_student_birthdate + ";" + student_student_gender + ";" + student_student_phone + ";" + student_student_email);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }


    public GeneralResponseModel getFindSchool(String jsonString) {

        String schoolName = "", schoolCode = "", regionName = "", regionCode = "", division = "", subdivision = "", city = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success
            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    schoolName = schoolName + "|" + jObject.getString("school_name");
                    schoolCode = schoolCode + "|" + jObject.getString("school_code");
                    regionName = regionName + "|" + jObject.getString("region_name");
                    regionCode = regionCode + "|" + jObject.getString("region_code");
                    division = division + "|" + jObject.getString("division");
                    subdivision = subdivision + "|" + jObject.getString("subdivision");
                    city = city + "|" + jObject.getString("city");

                }

                schoolName = "School Name" + schoolName;
                schoolCode = "School Code" + schoolCode;
                regionName = "Region Name" + regionName;
                regionCode = "Region Code" + regionCode;
                division = "Division" + division;
                subdivision = "Subdivision" + subdivision;
                city = "City" + city;


                //  resultdescription  = jsonObject.getString("resultdescription");

                responseModel.setUserDefinedString(schoolName + ";" + schoolCode + ";" + regionName + ";" + regionCode + ";" + division + ";" + subdivision + ";" + city);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }

    public GeneralResponseModel getSchoolDetails(String jsonString) {

        String schoolName = "", schoolCode = "", regionName = "", regionCode = "", division = "", subdivision = "", city = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success

            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    schoolName = jObject.getString("school_name");
                    schoolCode = jObject.getString("school_code");
                    regionName = jObject.getString("region_name");
                    regionCode = jObject.getString("region_code");
                    division = jObject.getString("division");
                    subdivision = jObject.getString("subdivision");
                    city = jObject.getString("city");
                }


                responseModel.setUserDefinedString(schoolName + "|" + schoolCode + "|" + regionName + "|" + regionCode + "|" + division + "|" + subdivision + "|" + city);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }

    public GeneralResponseModel getStudentDetails(String jsonString) {

        String student_regnumber = "", student_name = "", student_birthdate = "", student_gender = "", student_class_id = "", student_class_name = "", student_phone = "", student_email = "", student_subdivision = "", student_city = "", student_country_code = "", student_country_name = "", student_school_name = "", student_school_code = "", student_region_name = "", student_region_code = "", student_division = "";


        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success

            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    student_name = jObject.getString("student_name");
                    student_birthdate = jObject.getString("student_birthdate");
                    student_gender = jObject.getString("student_gender");
                    student_class_id = jObject.getString("student_class_id");
                    student_class_name = jObject.getString("student_class_name");
                    student_phone = jObject.getString("student_phone");
                    student_email = jObject.getString("student_email");
                    student_country_code = jObject.getString("country_code");
                    student_country_name = jObject.getString("country_name");
                    student_school_name = jObject.getString("school_name");
                    student_school_code = jObject.getString("school_code");
                    student_region_name = jObject.getString("region_name");
                    student_region_code = jObject.getString("region_code");
                    student_division = jObject.getString("division");
                    student_subdivision = jObject.getString("subdivision");
                    student_city = jObject.getString("city");
                    student_regnumber = jObject.getString("student_regnumber");


                }

                responseModel.setUserDefinedString(student_name + "|" + student_birthdate + "|" + student_gender + "|" + student_class_id + "|" + student_class_name + "|" + student_phone + "|" + student_email + "|" + student_country_code + "|" + student_country_name + "|" + student_school_name + "|" + student_school_code + "|" + student_region_name + "|" + student_region_code + "|" + student_division + "|" + student_subdivision + "|" + student_city + "|" + student_regnumber + "|" + "SuccessFull");
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }

    public GeneralResponseModel getStudentDetails_list(String jsonString) {

        String student_regnumber = "", student_name = "";


        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success

            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);


                    student_name = student_name + "|" + jObject.getString("student_name");
                    student_regnumber = student_regnumber + "|" + jObject.getString("student_regnumber");

                }
                student_name = "Search Result " + student_name;
                student_regnumber = "Search Result Code " + student_regnumber;


                responseModel.setUserDefinedString(student_name + ";" + student_regnumber);
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }


    public GeneralResponseModel getDetailSelectionStudentName(String jsonString) {

        String student_regnumber = "", student_name = "", student_birthdate = "", student_gender = "", student_class_id = "", student_class_name = "", student_phone = "", student_email = "", student_subdivision = "", student_city = "", student_country_code = "", student_country_name = "", student_school_name = "", student_school_code = "", student_region_name = "", student_region_code = "", student_division = "";


        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.getString("status").equalsIgnoreCase("200"))  // in api status 200 is success otherwise not success

            {

                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));   // array name


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    student_name = jObject.getString("student_name");
                    student_birthdate = jObject.getString("student_birthdate");
                    student_gender = jObject.getString("student_gender");
                    student_class_id = jObject.getString("student_class_id");
                    student_class_name = jObject.getString("student_class_name");
                    student_phone = jObject.getString("student_phone");
                    student_email = jObject.getString("student_email");
                    student_country_code = jObject.getString("country_code");
                    student_country_name = jObject.getString("country_name");
                    student_school_name = jObject.getString("school_name");
                    student_school_code = jObject.getString("school_code");
                    student_region_name = jObject.getString("region_name");
                    student_region_code = jObject.getString("region_code");
                    student_division = jObject.getString("division");
                    student_subdivision = jObject.getString("subdivision");
                    student_city = jObject.getString("city");
                    student_regnumber = jObject.getString("student_regnumber");


                }

                responseModel.setUserDefinedString(student_name + "|" + student_birthdate + "|" + student_gender + "|" + student_class_id + "|" + student_class_name + "|" + student_phone + "|" + student_email + "|" + student_country_code + "|" + student_country_name + "|" + student_school_name + "|" + student_school_code + "|" + student_region_name + "|" + student_region_code + "|" + student_division + "|" + student_subdivision + "|" + student_city + "|" + student_regnumber + "|" + "SuccessFull");
                responseModel.setResponseCode(0);


            } else {
                responseModel.setUserDefinedString(jsonObject.getString("message"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);   //
        }

        return responseModel;
    }


    public GeneralResponseModel getQuestionAnswer_recievCash(String jsonString) {

        String question = "", questionCode = "", resultdescription = "", answer = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.getString("resultcode").equalsIgnoreCase("0")) {


                JSONArray jsonArray = new JSONArray(jsonObject.getString("questionans"));


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);

                    question = question + "|" + jObject.getString("question");
                    questionCode = questionCode + "|" + jObject.getString("questioncode");
                    answer = answer + "|" + jObject.getString("answer");
                }

                question = "Question test " + question;
                questionCode = "Question Code" + questionCode;
                answer = "Answer" + answer;


                responseModel.setUserDefinedString(question + ";" + questionCode + ";" + answer);
                responseModel.setResponseCode(0);

            } else {
                responseModel.setUserDefinedString(jsonObject.getString("resultdescription"));
                responseModel.setResponseCode(Integer.parseInt(jsonObject.getString("resultcode")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setReasonTranfer("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }


    public GeneralResponseModel getSecurityQuestionResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                if (obj.has("otp")) {
                    if (obj.get("otp").toString().trim().length() == 6) {

                        generalResponseModel.setOTP(true);
                    } else {
                        generalResponseModel.setOTP(false);
                    }
                } else {
                    generalResponseModel.setOTP(false);
                }

                JSONArray questionsArr = obj.getJSONArray("questionans");

                String questions = "", answers = "", questionCodes = "";
                for (int i = 0; i < questionsArr.length(); i++) {

                    JSONObject jsonObject = questionsArr.getJSONObject(i);

                    if (questions.length() == 0) {

                        questions = questions + jsonObject.getString("question") + "|" + jsonObject.getString("answer") + "|" + jsonObject.getString("questioncode");

                    } else {

                        questions = questions + "#" + jsonObject.getString("question") + "|" + jsonObject.getString("answer") + "|" + jsonObject.getString("questioncode");

                    }
                }

                generalResponseModel.setUserDefinedString(questions);

            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

            }

        } catch (Exception e) {
            generalResponseModel.setUserDefinedString("App parsing Error Occured");
            generalResponseModel.setResponseCode(999);
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getuploadImage_all(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getMasterDataResponse(String jsonString) {
        String branch = "", code = "", code2 = "", fixedBACode = "", name = "", plancode = "", name2 = "", idproof2 = "", agenttype = "", profilename = "";
        GeneralResponseModel responseModel = new GeneralResponseModel();


        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    JSONArray jsonArray = new JSONArray(obj.getString("profiles"));
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonNew = jsonArray.getJSONObject(i);
                        JSONArray jsonarrayROW = jsonNew.getJSONArray("profile");

                        for (int j = 0; j < jsonarrayROW.length(); j++) {

                            JSONObject jObject = jsonarrayROW.getJSONObject(j);

                            agenttype = agenttype + "|" + jObject.getString("agenttype");
                            profilename = profilename + "|" + jObject.getString("profilename");
                            plancode = plancode + "|" + jObject.getString("plancode");
                        }
                    }


                    // AGENT TYPE |SUBPROFILE NAME |SUPERAGENTPLAN CODE |PLAN CODE
                    //responseModel.setUserDefinedString1("$"+ agenttype + "$" + profilename);

                    agenttype = "AGENT TYPE " + agenttype;
                    profilename = " PROFILE NAME " + profilename;
                    plancode = " PLAN CODE " + plancode;

                    responseModel.setUserDefinedString4("$" + agenttype + "$" + profilename + "$" + plancode);


                    JSONArray jsonArrayBranch = new JSONArray(obj.getString("branches"));
                    for (int k = 0; k < jsonArrayBranch.length(); k++) {

                        JSONObject jObject2 = jsonArrayBranch.getJSONObject(k);
                        JSONObject jInnerObject = jObject2.getJSONObject("branch");

                        code = code + "|" + jInnerObject.getString("code");
                        fixedBACode = fixedBACode + "|" + jInnerObject.getString("fixedBACode");
                        name = name + "|" + jInnerObject.getString("name");
                    }

                    code = "BRANCH CODE" + code;
                    fixedBACode = "BRANCH FIXED BA CODE" + fixedBACode;
                    name = "BRANCH NAME" + name;

                    //responseModel.setUserDefinedString("$" + branch + "$" + code + "$" + fixedBACode + "$" + name);

                    responseModel.setUserDefinedString5("$" + code + "$" + fixedBACode + "$" + name);

                    //    responseModel.setUserDefinedString5(code + fixedBACode + name);


                    JSONArray jsonArrayidproofs = new JSONArray(obj.getString("idproofs"));
                    for (int l = 0; l < jsonArrayidproofs.length(); l++) {

                        JSONObject jObject3 = jsonArrayidproofs.getJSONObject(l);
                        JSONObject JinnerObject = jObject3.getJSONObject("idproof");

                        code2 = code2 + "|" + JinnerObject.getString("code");
                        name2 = name2 + "|" + JinnerObject.getString("name");
                    }

                    code2 = "ID PROOF CODE" + code2;
                    name2 = "ID PROOF " + name2;

                    responseModel.setUserDefinedString6("$" + code2 + "$" + name2);

                } else {
                    responseModel.setUserDefinedString4(obj.getString("resultdescription"));
                    responseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                responseModel.setResponseCode(99);
                responseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }


    public GeneralResponseModel getUploadImagePictureSignResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getImageDownloadParsing(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") +
                            "|" + obj.getString("idfrontimage") +
                            "|" + obj.getString("billimage") +
                            "|" + obj.getString("idbackimage") +
                            "|" + obj.getString("agentimage") +  // 4
                            "|" + obj.getString("signature") +   // 5
                            "|" + obj.getString("resultdescription")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel parsingAuthrize_initiate(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    String counter = "5";

                    if (obj.has("authorizedcount")) {

                        counter = obj.getString("authorizedcount");

                        try {

                            int abc = Integer.parseInt(counter);
                            if (abc == 0) {
                                counter = "5";
                            } else {
                                counter = String.valueOf(++abc);
                            }


                        } catch (Exception e) {

                            counter = "5";
                        }

//                        if(counter==null||counter.trim().equalsIgnoreCase("")||counter.trim().equalsIgnoreCase("0")){
//                            counter="5";
//                        }
                    }

                    generalResponseModel.setUserDefinedString(obj.getString("transid") +

                            "|" + obj.getString("resultdescription") +   // 1  resultdescription mandatory not change
                            "|" + obj.getString("action") +
                            "|" + obj.getString("resultdescription") + "|" + counter

                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel parsingAuthrize_auth(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") +  //  0 index  not change resultdescription (Transaction Successful)

                            "|" + obj.getString("resultdescription") +
                            "|" + obj.getString("action") +
                            "|" + obj.getString("resultdescription")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }


    public GeneralResponseModel getImageDownloadParsing_updateAcoount(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") +

                            "|" + obj.getString("agentimage") +
                            "|" + obj.getString("idfrontimage") +
                            "|" + obj.getString("idbackimage") +
                            "|" + obj.getString("billimage") +
                            "|" + obj.getString("signature") +
                            "|" + obj.getString("formimage") +
                            "|" + obj.getString("resultdescription")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getPictureSignCahInReceiveMoneyResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString("pictureSign" + "|"
                            + obj.getString("image") +
                            "|" + obj.getString("Sign") +
                            "|" + obj.getString("resultdescription") +   // not change
                            "|" + obj.getString("state") +
                            "|" + obj.getString("firstname") +  // Subscriber Name(firstname)
                            "|" + obj.getString("idproof") +
                            "|" + obj.getString("issamebranch") +
                            "|" + obj.getString("statename") +

                            "|" + obj.getString("idproof") +   // id proof number 12 nov 2018 - CashOut
                            "|" + obj.getString("idproofissueplace") + // id proof issue place 12 nov 2018 - CashOut
                            "|" + obj.getString("idproofissuedate") +   // id proof issue date number 12 nov 2018 - CashOut
                            "|" + obj.getString("resultdescription")


                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getprofileViewResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("transid") +
                            "|" + obj.getString("profilename") +
                            "|" + obj.getString("firstname") +
                            "|" + obj.getString("gender") +
                            "|" + obj.getString("language") +
                            "|" + obj.getString("secondmobphoneno") +   // 5
                            "|" + obj.getString("fixphoneno") +   // 6
                            "|" + obj.getString("dob") +    // 7
                            "|" + obj.getString("birthplace") +   // 8
                            "|" + obj.getString("address") +     // 9
                            "|" + obj.getString("city") +    // 10
                            "|" + obj.getString("resultdescription") +
                            "|" + obj.getString("nationality") +     // 12
                            "|" + obj.getString("residencearea") +   // 13
                            "|" + obj.getString("idprooftype") +        // 14
                            "|" + obj.getString("idproof") +      // 15
                            "|" + obj.getString("idproofissuedate") + // 16
                            "|" + obj.getString("idproofissueplace") + // 17
                            "|" + obj.getString("idproofduedate") + // 18
                            "|" + obj.getString("nationality") + // 19
                            "|" + obj.getString("statename") + // 20  statename is atachched branch name
                            "|" + obj.getString("comments") +  // 21
                            "|" + obj.getString("agenttype") +  // 22
                            "|" + obj.getString("email") +  // 23
                            "|" + obj.getString("tla") +  // 24    // tla is baranch name / attached  branh name
                            "|" + obj.getString("resultdescription")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getCheckImageDownloadResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("transid") +
                            "@" + obj.getString("images") +
                            "@" + obj.getString("resultdescription") +
                            "@" + obj.getString("resultdescription")
                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getUnblockSubscriberConfCodeResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getBlockSubscriberResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("transid")
                    );

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getOrderTransferFinal(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("requestcts")
                            + "|" + obj.getString("destination")
                            + "|" + obj.getString("source")

                            // + "|" + obj.getString("agentbranch")
                            // + "|" + obj.getString("country")

                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCashToCashTransferCancelWithFees(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fees") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("responsects")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashToCashTransferCancelWithoutFees(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {


                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fees") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("responsects")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getUnBlockSubscriberResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") + "|" + obj.getString("transid"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getOrderTransferDetailsApprovedResponse(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String otherDetails = "";
        listdata = new ArrayList<String>();
        String subname = "", mobileno = "", title = "", idproofissueplace = "", resultdescription = "", transid = "", idproof = "", telephone = "",
                phoneno = "", idproofissuedate = "";


        ArrayList<String> completeDetails = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {
                        String ss = obj.getString("records");
                        JSONArray jsonArray = new JSONArray(ss);
                        JSONObject jsonLoopObject;

                        String strListData = "";
                        String str1 = "";

                   /* subname = obj.getString("subname");
                    mobileno = obj.getString("mobileno");
                    title = obj.getString("title");
                    idproofissueplace = obj.getString("idproofissueplace");
                    idproofissuedate = obj.getString("idproofissuedate");
                    transid = obj.getString("transid");
                    idproof = obj.getString("idproof");
                    telephone = obj.getString("telephone");
                    phoneno = obj.getString("phoneno");

                    str1 = subname + "|" + mobileno + "|" + title + "|" + idproofissueplace + "|" + idproofissuedate
                            + "|" + transid + "|" + idproof
                            + "|" + telephone + "|" + phoneno;*/

                        String transidString = "", amountString = "", agentcodeOrderFrom = "", destinationOrderTo = "", sourceName_orderTo = "", sourceName_orderFrom = "";


                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonLoopObject = jsonArray.getJSONObject(i);


                           /*   "confcode": "NMQF",
                                "amount": "250.0",
                                "agentcode": "237000271510",
                                "destinationname": "shipra Ag4",
                                "sourcename": "shipra sub1",
                                "transid": "9326191",
                                "destination": "237000271503"
                        */


                            transidString = jsonLoopObject.getString("transid");
                            amountString = jsonLoopObject.getString("amount");

                            agentcodeOrderFrom = jsonLoopObject.getString("agentcode");
                            destinationOrderTo = jsonLoopObject.getString("destination");
                            sourceName_orderFrom = jsonLoopObject.getString("sourcename");
                            sourceName_orderTo = jsonLoopObject.getString("destinationname");


                            strListData = agentcodeOrderFrom + "|" + sourceName_orderFrom + "|" + destinationOrderTo + "|" + sourceName_orderTo + "|" + amountString + "|" + transidString;

                            listdata.add(strListData);

                        }


                        generalResponseModel.setCustomResponseList(listdata);
                        generalResponseModel.setUserDefinedString(str1);

                        //  generalResponseModel.setOtherAccountResponseList(completeDetails);
                    } catch (JSONException e) {
                        Log.e("", "" + e.toString());
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }

            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getOtherDetailsResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String otherDetails = "";
        listdata = new ArrayList<String>();
        String subname = "", mobileno = "", title = "", idproofissueplace = "", resultdescription = "", transid = "", idproof = "", telephone = "",
                phoneno = "", idproofissuedate = "";


        ArrayList<String> completeDetails = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {
                        String ss = obj.getString("accounts");
                        JSONArray jsonArray = new JSONArray(ss);
                        JSONObject jsonProductObject;
                        String accountno = "", accounttitle = "", acctypedescription = "", accounttype = "";
                        String mobilebankingservice = "", accountstatusdescription = "";
                        String data = "";
                        String strListData = "";
                        String str1 = "";
                        subname = obj.getString("subname");
                        mobileno = obj.getString("mobileno");
                        title = obj.getString("title");
                        idproofissueplace = obj.getString("idproofissueplace");
                        idproofissuedate = obj.getString("idproofissuedate");
                        transid = obj.getString("transid");
                        idproof = obj.getString("idproof");
                        telephone = obj.getString("telephone");
                        phoneno = obj.getString("phoneno");

                        str1 = subname + "|" + mobileno + "|" + title + "|" + idproofissueplace + "|" + idproofissuedate
                                + "|" + transid + "|" + idproof
                                + "|" + telephone + "|" + phoneno;

                        otherList = new ArrayList<>();
                        OtherAccountModel otherAccountModel;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            otherAccountModel = new OtherAccountModel();
                            jsonProductObject = jsonArray.getJSONObject(i);

                            otherAccountModel.setAccountNumber(jsonProductObject.getString("accountno"));
                            otherAccountModel.setAccounttitle(jsonProductObject.getString("accounttitle"));
                            otherAccountModel.setAcctypedescription(jsonProductObject.getString("acctypedescription"));
                            otherAccountModel.setAccountTypeCode(jsonProductObject.getString("accounttype"));
                            otherAccountModel.setAccountstatusdescription(jsonProductObject.getString("accountstatusdescription"));
                            otherAccountModel.setMobilebankingservice(jsonProductObject.getString("mobilebankingservice"));

                            otherList.add(otherAccountModel);
                            generalResponseModel.setOtherAccountModels(otherList);


                        }

                        generalResponseModel.setCustomResponseList(listdata);
                        generalResponseModel.setUserDefinedString(str1);
                        //  generalResponseModel.setOtherAccountResponseList(completeDetails);
                    } catch (JSONException e) {
                        Log.e("", "" + e.toString());
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }


    public GeneralResponseModel withdrawalDetailsResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {


                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription")
                            + "|" + obj.getString("transid")
                            + "|" + obj.getString("confcode")
                            + "|" + obj.getString("resultdescription")


                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getBillerCodeBillPayResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("billercode"));

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getAccountToCashResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("confcode") + "|" + obj.getString("amount") + "|" +
                            obj.getString("destination") + "|" + obj.getString("transid") + "|" +
                            obj.getString("walletbalance"));
                    if (obj.has("otp")) {

                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }

                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAccountToAccountResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("amount") + "|" + obj.getString("destination") + "|" + obj.getString("transid") + "|" + obj.getString("walletbalance"));
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));


                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel tariffTutionFees(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("fee") + "|"

                            + obj.getString("transid") + "|"
                            + obj.getString("vat") + "|"
                            + obj.getString("resultdescription")
                    );


                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {
                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getTariffResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("fee") + "|"

                            + obj.getString("transid") + "|"
                            + obj.getString("feesupportedby") + "|"
                            + obj.getString("resultdescription")
                    );


                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {
                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getTariffResponseSendMoneyToMobile(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(

                            obj.getString("fee") +
                                    "|" + obj.getString("transid") +
                                    //"|" + obj.getString("currency") +
                                    "|" + obj.getString("amount"));

                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCityListCreateAgent(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String cityDetails = "";

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {
                        String cityStateCode = "", cityStatename = "";

                        JSONArray jsonArray = new JSONArray(obj.getString("statelist"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (cityStateCode.trim().length() == 0) {

                                cityStateCode = cityStateCode + "|" + jsonObject.getString("statecode");
                                cityStatename = cityStatename + "|" + jsonObject.getString("statename");
                            } else {
                                cityStateCode = cityStateCode + "|" + jsonObject.getString("statecode");
                                cityStatename = cityStatename + "|" + jsonObject.getString("statename");
                            }
                        }

                        cityStatename = " CITY  " + cityStatename;
                        cityStateCode = "CITY Code " + cityStateCode;


                        generalResponseModel.setUserDefinedCityCreateAgent("$" + cityStatename + "$" + cityStateCode);
                    } catch (Exception e) {

                        generalResponseModel.setResponseCode(99);
                        generalResponseModel.setUserDefinedString("Please try again later");
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCityResponse(String jsonString) {


        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String cityDetails = "";

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {


                        JSONArray jsonArray = new JSONArray(obj.getString("statelist"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject objj = jsonArray.getJSONObject(i);

                            if (cityDetails.trim().length() == 0) {
                                cityDetails = objj.getString("statecode") + ";" + objj.getString("statename");
                            } else {
                                cityDetails = cityDetails + "|" + objj.getString("statecode") + ";" + objj.getString("statename");
                            }
                        }
                        //invoiceDetails=invoiceDetails+";"+obj.getString("destination");

                        generalResponseModel.setUserDefinedString(cityDetails);

                    } catch (Exception e) {

                        generalResponseModel.setResponseCode(99);
                        generalResponseModel.setUserDefinedString("Please try again later");
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getBankingActivationResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCurrrentDayCommision(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }
                    String ss = obj.getString("transactions");
                    ArrayList<String> list = new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(ss);
                    String transhistoryRecords = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);
                        if (objj.has("reason")) {
                            transhistoryRecords = objj.getString("transid") + "|" +
                                    objj.getString("amount") + "|"
                                    + objj.getString("direction")
                                    + "|" + objj.getString("currency")
                                    + "|" + objj.getString("date");
                        } else {
                            transhistoryRecords =
                                    objj.getString("agenttransid") + "|"
                                            + objj.getString("fee")
                                            + "|" + objj.getString("source")
                                            + "|" + objj.getString("responsects");
                        }
                        list.add(transhistoryRecords);
                        transhistoryRecords = "";
                    }
                    generalResponseModel.setCustomResponseList(list);
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCommissionPeriod(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }
                    String ss = obj.getString("transactions");
                    ArrayList<String> list = new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(ss);
                    String transhistoryRecords = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);
                        if (objj.has("reason")) {
                            transhistoryRecords = objj.getString("transid") + "|" +
                                    objj.getString("amount") + "|"
                                    + objj.getString("direction")
                                    + "|" + objj.getString("currency")
                                    + "|" + objj.getString("date");
                        } else {
                            transhistoryRecords =
                                    objj.getString("agenttransid") + "|"
                                            + objj.getString("fee")
                                            + "|" + objj.getString("source")
                                            + "|" + objj.getString("responsects");
                        }
                        list.add(transhistoryRecords);
                        transhistoryRecords = "";
                    }
                    generalResponseModel.setCustomResponseList(list);
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getMiniStmtResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }
                    String ss = obj.getString("transactions");
                    ArrayList<String> list = new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(ss);
                    String transhistoryRecords = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);
                        if (objj.has("reason")) {
                            transhistoryRecords = objj.getString("transid") + "|" +
                                    objj.getString("amount") + "|"
                                    + objj.getString("direction")
                                    + "|" + objj.getString("currency")
                                    + "|" + objj.getString("date");
                        } else {
                            transhistoryRecords = objj.getString("transid") + "|"
                                    + objj.getString("amount") + "|" + objj.getString("source")
                                    + "|" + objj.getString("destination")
                                    + "|" + objj.getString("responsects")
                                    + "|" + objj.getString("transtype");
                        }
                        list.add(transhistoryRecords);
                        transhistoryRecords = "";
                    }
                    generalResponseModel.setCustomResponseList(list);
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getBalanceCheckResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("amount"));
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getPaymentRequestResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("destination") + "|" + obj.getString("transid") + "|" + obj.getString("amount") + "|" + obj.getString("confcode"));
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getPaymentAuthResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("accounttype") + "|" + obj.getString("source")
                            + "|" + obj.getString("amount") + "|" + obj.getString("destination") + "|" + obj.getString("walletbalance"));
                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getBillPaymentResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    HashMap<String, String> dataHashMap = new HashMap<String, String>();
                    Iterator<String> keysItr = obj.keys();
                    while (keysItr.hasNext()) {
                        String key = keysItr.next();
                        Object value = obj.get(key);


                         /*if(value instanceof JSONObject) {
                            value = getLoginResponse((JSONObject) value);
                        }*/
                        dataHashMap.put(key, value.toString());
                    }
                    generalResponseModel.setResponseList(dataHashMap);

                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"
                                    + obj.getString("resultdescription") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|"
                                    + obj.getString("fee") + "|"            // fees
                                    + obj.getString("amount") + "|"        // amount
                                    + obj.getString("state") + "|"
                                    + obj.getString("sourcename") + "|"    // Customer Name
                                    + obj.getString("source") + "|"        // customer ID
                                    + obj.getString("agentname") + "|"
                                    // + obj.getString("destinationname") + "|" // Destination name
                                    + obj.getString("billername") + "|"      // Marchant NAme
                                    + obj.getString("billercode") + "|"     // Label name
                                    + obj.getString("agentbranch") + "|"     // Attached Branch
                                    + obj.getString("custphoneno") + "|"     // Customer Phone no
                                    + obj.getString("invoiceno") + "|"       // Bill Number
                                    + obj.getString("feesupportedby"));


                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getReprintBillPaymentResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    HashMap<String, String> dataHashMap = new HashMap<String, String>();
                    Iterator<String> keysItr = obj.keys();
                    while (keysItr.hasNext()) {
                        String key = keysItr.next();
                        Object value = obj.get(key);


                         /*if(value instanceof JSONObject) {
                            value = getLoginResponse((JSONObject) value);
                        }*/
                        dataHashMap.put(key, value.toString());
                    }
                    generalResponseModel.setResponseList(dataHashMap);

                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"
                                    + obj.getString("resultdescription") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|"
                                    + obj.getString("fee") + "|"            // fees
                                    + obj.getString("amount") + "|"        // amount
                                    + obj.getString("state") + "|"
                                    + obj.getString("sourcename") + "|"    // Customer Name
                                    + obj.getString("source") + "|"        // customer ID
                                    + obj.getString("agentname") + "|"
                                    // + obj.getString("destinationname") + "|" // Destination name
                                    + obj.getString("destinationname") + "|"      // Marchant NAme
                                    + obj.getString("billercode") + "|"     // Label name
                                    + obj.getString("agentbranch") + "|"     // Attached Branch
                                    + obj.getString("resultdescription") + "|"     // Customer Phone no
                                    + obj.getString("invoiceno") + "|"       // Bill Number
                                    + obj.getString("transferno") + "|"       // old Trnaaction ID
                                    + obj.getString("tax") + "|"       // old Trnaaction ID
                                    + obj.getString("resultdescription"));


                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {

                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getChangeMpinResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription") +   // not change fix  resultdescription
                            "|" + obj.getString("otpflag") +
                            "|" + obj.getString("resultdescription")

                    );
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getOtpResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }


    public GeneralResponseModel otpGenerateResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel resendOtpNew_cashtoCash(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel resendOtp_verify(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getResendOtpResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    String temp = "";

    public GeneralResponseModel getAgentIdentityResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"
                                    + obj.getString("idproofissueplace") + "|"
                                    + obj.getString("residencearea") + "|"
                                    + obj.getString("language") + "|"
                                    + obj.getString("secondmobphoneno") + "|"
                                    + obj.getString("resultdescription"));

                } else if (obj.getString("resultcode").equalsIgnoreCase("122")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("122");

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentIdentityNewResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"
                                    + obj.getString("idproofissueplace") + "|"
                                    + obj.getString("residencearea") + "|"
                                    + obj.getString("language") + "|"
                                    + obj.getString("secondmobphoneno") + "|"
                                    + obj.getString("resultdescription"));

                } else if (obj.getString("resultcode").equalsIgnoreCase("122")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("122");

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getAgentIdentityProfileViewNewResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"
                                    + obj.getString("idproofissueplace") + "|"
                                    + obj.getString("residencearea") + "|"
                                    + obj.getString("language") + "|"
                                    + obj.getString("secondmobphoneno") + "|"
                                    + obj.getString("resultdescription") + "|"   // 22 not change this tags fixed  resultdescription
                                    + obj.getString("comments") + "|"     // 23
                                    + obj.getString("agenttype") + "|"     // 24
                                    + obj.getString("kycsamebranch") + "|"     // 25
                                    + obj.getString("resultdescription"));

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentIdentity_sender(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"       // 8
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"   // 13
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"      // 15
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"         // 17
                                    + obj.getString("idproofissueplace") + "|"  // 18
                                    + obj.getString("residencearea") + "|"      // 19
                                    + obj.getString("language") + "|"           // 20
                                    + obj.getString("secondmobphoneno") + "|"   // 21
                                    + obj.getString("resultdescription") + "|"   // 22 not change this tags fixed  resultdescription
                                    + obj.getString("city") + "|"                // 23
                                    + obj.getString("address1") + "|"            // 24
                                    + obj.getString("agenttype") + "|"            // 25
                                    + obj.getString("kycsamebranch") + "|"            // 26                                     + obj.getString("kycsamebranch") + "|"     // 25
                                    + obj.getString("resultdescription"));

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentIdentity_receiver_tutionfees(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(


                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"       // 8
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"   // 13
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"      // 15
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"         // 17
                                    + obj.getString("idproofissueplace") + "|"  // 18
                                    + obj.getString("residencearea") + "|"      // 19
                                    + obj.getString("language") + "|"           // 20
                                    + obj.getString("secondmobphoneno") + "|"   // 21
                                    + obj.getString("resultdescription") + "|"   // 22 not change this tags fixed  resultdescription
                                    + obj.getString("city") + "|"                // 23
                                    + obj.getString("address1") + "|"            // 24
                                    + obj.getString("agenttype") + "|"            // 25
                                    + obj.getString("kycsamebranch") + "|"            // 26                                     + obj.getString("kycsamebranch") + "|"     // 25
                                    + obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }


    public GeneralResponseModel getAgentIdentity_receiver(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(


                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"       // 8
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"   // 13
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"      // 15
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"         // 17
                                    + obj.getString("idproofissueplace") + "|"  // 18
                                    + obj.getString("residencearea") + "|"      // 19
                                    + obj.getString("language") + "|"           // 20
                                    + obj.getString("secondmobphoneno") + "|"   // 21
                                    + obj.getString("resultdescription") + "|"   // 22 not change this tags fixed  resultdescription
                                    + obj.getString("city") + "|"                // 23
                                    + obj.getString("address1") + "|"            // 24
                                    + obj.getString("agenttype") + "|"            // 25
                                    + obj.getString("kycsamebranch") + "|"            // 26                                     + obj.getString("kycsamebranch") + "|"     // 25
                                    + obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }


            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }
        return generalResponseModel;
    }

    public GeneralResponseModel getAgentIdentityUpdateAccountNewResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"       // 8
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"   // 13
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"      // 15
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"         // 17
                                    + obj.getString("idproofissueplace") + "|"  // 18
                                    + obj.getString("residencearea") + "|"      // 19
                                    + obj.getString("language") + "|"           // 20
                                    + obj.getString("secondmobphoneno") + "|"   // 21
                                    + obj.getString("resultdescription") + "|"   // 22 not change this tags fixed  resultdescription
                                    + obj.getString("city") + "|"                // 23
                                    + obj.getString("address1") + "|"            // 24
                                    + obj.getString("agenttype") + "|"            // 25
                                    + obj.getString("kycsamebranch") + "|"            // 26                                     + obj.getString("kycsamebranch") + "|"     // 25
                                    + obj.getString("resultdescription"));

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getBillerInfoResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("sessionid")

                            + "|" + obj.getString("fee")
                            + "|" + obj.getString("amount")
                            + "|" + obj.getString("fee")
                            + "|" + obj.getString("amount")
                    );

                    if (obj.has("otp")) {
                        if (obj.get("otp").toString().trim().length() == 6) {
                            generalResponseModel.setOTP(true);
                        } else {
                            generalResponseModel.setOTP(false);
                        }
                    } else {
                        generalResponseModel.setOTP(false);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }


        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getAgentIdentityResponseDestinationName(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("0");
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|"     // locality
                                    + obj.getString("profession") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("dateofbirth") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("gender") + "|"
                                    + obj.getString("idproof") + "|"
                                    + obj.getString("idproofissuedate") + "|"
                                    + obj.getString("fixphoneno") + "|"
                                    + obj.getString("idprooftype") + "|"
                                    + obj.getString("nationality") + "|"
                                    + obj.getString("customerid") + "|"
                                    + obj.getString("address") + "|"
                                    + obj.getString("clienttype") + "|"
                                    + obj.getString("birthplace") + "|"
                                    + obj.getString("idproofissueplace") + "|"
                                    + obj.getString("residencearea") + "|"
                                    + obj.getString("language") + "|"
                                    + obj.getString("secondmobphoneno") + "|"
                                    + obj.getString("resultdescription"));

                } else if (obj.getString("resultcode").equalsIgnoreCase("122")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString2("122");

                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getPlanCreateAgentResponse(String jsonString) {
        String planname = "", lavel = "", agenttype = "", plancode = "";

        GeneralResponseModel responseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    JSONArray jsonArray = new JSONArray(obj.getString("plans"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jObject = jsonArray.getJSONObject(i);

                        planname = planname + "|" + jObject.getString("planname");
                        lavel = lavel + "|" + jObject.getString("lavel");
                        agenttype = agenttype + "|" + jObject.getString("agenttype");
                        plancode = plancode + "|" + jObject.getString("plancode");

                  /* if (objj.has("mobilelength")) {
                        mobileNoLength = mobileNoLength + "|" + objj.getString("mobilelength");
                    } else {
                        mobileNoLength = mobileNoLength + "|9";
                    }*/
                    }

                    lavel = "Select Account Profile" + lavel;
                    plancode = "Select Account Profile" + plancode;
                    agenttype = "Select Agent Type" + agenttype;

                    responseModel.setUserDefinedString("$" + lavel + "$" + plancode + "$" + agenttype);

                    //  String planNamePlanCodeString= planname+ "$"+plancode;
                    //  responseModel.setPlanNamePlanString(planNamePlanCodeString);

                } else {
                    responseModel.setUserDefinedString(obj.getString("resultdescription"));
                    responseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

               /* responseModel.setResponseCode(99);
                responseModel.setUserDefinedString(obj.getString("Please try again later"));
*/
                responseModel.setResponseCode(99);
                responseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            responseModel.setUserDefinedString("Error occured");
            responseModel.setResponseCode(99);
        }

        return responseModel;
    }

    public GeneralResponseModel getReprintCashInResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("destbranch")
                            + "|" + obj.getString("state")
                            + "|" + obj.getString("transferno")
                            + "|" + obj.getString("agentname")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("tax")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashInResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("destbranch") + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashOutSameBranchResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("destbranch") + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname");

                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getPrintCashoutSameDiferenceWithdrawaResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"
                            + obj.getString("resultdescription")
                            + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects")
                            + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") +
                            "|" + obj.getString("fee") + "|"
                            + obj.getString("amount")
                            + "|" + obj.getString("destbranch")
                            + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("agentname")
                            + "|" + obj.getString("transferno")
                            + "|" + obj.getString("tax")
                            + "|" + obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel reprintNotValid(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription");

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashOutDifferenteBranchResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("transid") + "|"
                            + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("destbranch")
                            + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getRemmitanceSendMoneyToNameResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);


                    generalResponseModel.setUserDefinedString(
                            obj.getString("transid") + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                                    + obj.getString("amount") + "|" + obj.getString("state")
                                    + "|" + obj.getString("destinationname")
                                    + "|" + obj.getString("source")
                                    + "|" + obj.getString("sourcename")
                                    + "|" + obj.getString("agentbranch")
                                    + "|" + obj.getString("currency")
                                    + "|" + obj.getString("confcode")
                                    + "|" + obj.getString("source")
                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashOutWithdrawalResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("confcode") + "|" + obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getRemmitanceReceiveMoneyToNameResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(

                            obj.getString("transid") + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                                    + obj.getString("amount") + "|" + obj.getString("agentbranch")
                                    + "|" + obj.getString("destinationname")
                                    + "|" + obj.getString("source")
                                    + "|" + obj.getString("sourcename")
                                    + "|" + obj.getString("currency")
                                    + "|" + obj.getString("agentbranch"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getPrintResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("transid") +     // Transaid New
                            "|" + obj.getString("resultdescription") +
                            "|" + obj.getString("responsevalue") +  // responsevalue is a comments
                            "|" + obj.getString("responsects") +
                            "|" + obj.getString("fee") +      // eversal Fees :
                            "|" + obj.getString("amount") +
                            "|" + obj.getString("destinationname") +
                            "|" + obj.getString("resultdescription") +
                            "|" + obj.getString("sourcename") +     // Sender Name
                            "|" + obj.getString("agentbranch") +
                            "|" + obj.getString("invoiceno") +
                            "|" + obj.getString("transrefno") +     // Trans id Old
                            "|" + obj.getString("country") +
                            "|" + obj.getString("labelname") +
                            "|" + obj.getString("billername") +
                            "|" + obj.getString("resultdescription")
                    );



                /*generalResponseModel.setUserDefinedString(obj.getString("transid") +     // Transaid New
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("responsects") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("amount") +              // amount
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("source") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("transrefno") +     // Trans id Old
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription") +
                        "|" + obj.getString("resultdescription")
                );*/


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getRePrintResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    CommonUtilities.txnType = obj.getString("transtype");
                    generalResponseModel.setUserDefinedString(/*obj.getString("transid") + "|"
                        + obj.getString("resultdescription") + "|" + obj.getString("comments") + "|"
                        + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                        + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                        + obj.getString("amount") + "|" + obj.getString("destbranch") + "|" + obj.getString("state")
                        + "|" + obj.getString("destinationname") +
                        "|" + obj.getString("transferno") +
                        "|" + obj.getString("agentbranch")+ "|" +*/ obj.getString("transtype")
                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel getPrintBillPayResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                //  if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                if (obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                        getJSONObject(0).getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    try {
                        transIdTransactionCode = obj.getString("transactionno") +
                                "|" + obj.getString("transid")
                                + "|" + obj.getString("printcount");


                    } catch (Exception e) {
                        generalResponseModel.setResponseCode(99);
                        generalResponseModel.setUserDefinedString("Please try again later");
                    }

                    String tempAgentInfo = obj.getJSONArray("AgentInfo").getJSONObject(0).getString("agentname")
                            + "|" + obj.getJSONArray("AgentInfo").getJSONObject(0).getString("source")
                            + "|" + obj.getJSONArray("AgentInfo").getJSONObject(0).getString("sourcename")
                            + "|" + obj.getJSONArray("AgentInfo").getJSONObject(0).getString("agentbranch")
                            + "|" + obj.getJSONArray("AgentInfo").getJSONObject(0).getString("destbranch")
                            + "|" + obj.getJSONArray("AgentInfo").getJSONObject(0).getString("accounttype");


                    String tempBillingInfo = obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("BillerInfo").
                            getJSONObject(0).getString("billercode")
                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("BillerInfo").
                            getJSONObject(0).getString("billername")
                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("BillerInfo").
                            getJSONObject(0).getString("labelname")
                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("BillerInfo").
                            getJSONObject(0).getString("cin")

                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).get("invoiceno");


                    String tempTxnDetails = obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("amount")
                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("fee")
                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("tax")

                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("trnsdate")

                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("trnsdate")

                            + "|" + obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("resultdescription");


                    generalResponseModel.setUserDefinedString(tempAgentInfo + "$" + tempBillingInfo + "$" + tempTxnDetails + "$" + transIdTransactionCode);
                } else {
                    generalResponseModel.setUserDefinedString(obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getJSONArray("BillingInfo").getJSONObject(0).getJSONArray("TransactionDetails").
                            getJSONObject(0).getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReprintCashToCashReceiveMoneyToMobileResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    temp =
                            obj.getString("transid") + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                                    + obj.getString("amount") + "|" + obj.getString("agentbranch")
                                    + "|" + obj.getString("destinationname")  //  9
                                    + "|" + obj.getString("source")     // 10
                                    + "|" + obj.getString("sourcename")
                                    + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("agentbranch")    // 13
                                    + "|" + obj.getString("currency")    // 14
                                    + "|" + obj.getString("agentname")    // 15
                                    + "|" + obj.getString("transferno")    // 16
                                    + "|" + obj.getString("referenceNumber")    // 17
                                    + "|" + obj.getString("resultdescription"); //


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                        System.out.print(temp);
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }





    public GeneralResponseModel getReprint_tution_fees(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    temp =
                            obj.getString("trnsdate")                               // 0
                                    + "@@@@" + obj.getString("transid")
                                    + "@@@@" + obj.getString("comments")
                                    + "@@@@"+ obj.getString("amount")                // 3
                                    + "@@@@"+ obj.getString("fee")
                                    + "@@@@"+obj.getString("tax")                   // 5
                                    + "@@@@"+ obj.getString("agentbranch")
                                    + "@@@@"+ obj.getString("source")
                                    + "@@@@"+ obj.getString("resultdescription")
                                    + "@@@@"+ obj.getString("resultdescription")     //  9
                                    + "@@@@"+ obj.getString("resultdescription")     // 10
                                    + "@@@@"+ obj.getString("resultdescription")
                                    + "@@@@"+ obj.getString("resultdescription")
                                    + "@@@@"+ obj.getString("resultdescription")
                                    + "@@@@"+ obj.getString("resultdescription");    // 10


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp);
                        System.out.print(temp);
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getRemmitanceReceiveMoneyToMobileResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    temp =
                            obj.getString("transid") + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                                    + obj.getString("amount") + "|" + obj.getString("agentbranch")
                                    + "|" + obj.getString("destinationname")
                                    + "|" + obj.getString("source")
                                    + "|" + obj.getString("sourcename")
                                    + "|" + obj.getString("resultdescription")
                                    + "|" + obj.getString("agentbranch")
                                    + "|" + obj.getString("currency")
                                    + "|" + obj.getString("resultdescription");

                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getTransactionCancelResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("transid") +     // Transaid New
                                    "|" + obj.getString("resultdescription") +
                                    "|" + obj.getString("comments") +
                                    "|" + obj.getString("responsects") +
                                    "|" + obj.getString("fee") +      // eversal Fees :
                                    "|" + obj.getString("amount") +
                                    "|" + obj.getString("destinationname") +    // 6
                                    "|" + obj.getString("source") +
                                    "|" + obj.getString("sourcename") + // 8
                                    "|" + obj.getString("agentbranch") +    // 9
                                    "|" + obj.getString("invoiceno") +     // 10
                                    "|" + obj.getString("transferno") +     // Trans id Old
                                    "|" + obj.getString("country") +     // 12
                                    "|" + obj.getString("transtype") +   // 13
                                    "|" + obj.getString("confcode") +    // 14
                                    "|" + obj.getString("labelname") +   // 15
                                    "|" + obj.getString("billername") +    // 16
                                    "|" + obj.getString("resultdescription")

                    );


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getTransactionApprovedViewResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(
                            obj.getString("transid") + "|"
                                    + obj.getString("resultdescription") + "|"
                                    + obj.getString("comments") + "|"
                                    + obj.getString("responsects") + "|"
                                    + obj.getString("country") + "|"
                                    + obj.getString("destination") + "|"
                                    + obj.getString("fee") + "|"
                                    + obj.getString("amount") + "|"
                                    + obj.getString("destbranch") + "|"
                                    + obj.getString("state") + "|"
                                    + obj.getString("agentbranch") + "|"
                                    + obj.getString("sourcename") + "|"     // 11
                                    + obj.getString("source") + "|"
                                    + obj.getString("agentname") + "|"
                                    + obj.getString("confcode") + "|"     // 14
                                    + obj.getString("destinationname") + "|" // 15   Subscriber Name  //change response Tag 11062017
                                    + obj.getString("currency") + "|"    //   // 16
                                    + obj.getString("billername") + "|"    //   // 17
                                    + obj.getString("labelname") + "|"    //   // 18
                                    + obj.getString("showconfcode") + "|"    //   // 19
                                    + obj.getString("resultdescription")


                    );

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getTransactionApprovedResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    // generalResponseModel.setUserDefinedString(obj.getString("confcode") + "|" + obj.getString("resultdescription"));
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getFeePaymentTransaction(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {
                if (obj.getString("resultCode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "#######" + obj.getString("resultDescription")
                            + "#######" + obj.getString("comments") + "#######"
                            + obj.getString("responsects") + "#######"
                            + obj.getString("fee") + "#######"
                            + obj.getString("amount")
                            + "#######" + obj.getString("destinationName")
                            + "#######" + obj.getString("source")
                            + "#######" + obj.getString("sourceName")
                            + "#######" + obj.getString("agentBranch")
                            + "#######" + obj.getString("invoiceno")
                            + "#######" + obj.getString("transcode")
                            + "#######" + obj.getString("feesupportedby")
                            + "#######" + obj.getString("resultDescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "#######" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultDescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultCode")));
                }

            } catch (Exception e) {
                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }


    public GeneralResponseModel getBillpayDepositPtopResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {
                if (obj.getString("resultCode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultDescription")
                            + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|"
                            + obj.getString("fee") + "|"
                            + obj.getString("amount")
                            + "|" + obj.getString("destinationName")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("sourceName")
                            + "|" + obj.getString("agentBranch")
                            + "|" + obj.getString("invoiceno")
                            + "|" + obj.getString("transcode")
                            + "|" + obj.getString("feesupportedby")
                            + "|" + obj.getString("resultDescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultDescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }

            } catch (Exception e) {
                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel geReprintCashToMarchantResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"      // 0
                            + obj.getString("resultdescription")
                            + "|" + obj.getString("comments") + "|"    // 2
                            + obj.getString("responsects") + "|"    // 3
                            + obj.getString("fee") + "|"       // 4
                            + obj.getString("amount")          // 5
                            + "|" + obj.getString("destinationname")    // 6
                            + "|" + obj.getString("source")             // 7
                            + "|" + obj.getString("sourcename")
                            + "|" + obj.getString("agentbranch")     // 9
                            + "|" + obj.getString("invoiceno")
                            + "|" + obj.getString("confcode")        // 11
                            + "|" + obj.getString("transferno")      // 12
                            + "|" + obj.getString("agentname")      // 13
                            + "|" + obj.getString("labelname")      // 14
                            + "|" + obj.getString("billername")      // 15
                            + "|" + obj.getString("country")      // 15
                            + "|" + obj.getString("resultdescription")

                    ;


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultDescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReprintCreateAccountResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|" +
                            obj.getString("responsects") + "|"
                            + obj.getString("source") + "|"
                            + obj.getString("transid") +
                            "|" + obj.getString("state") + "|"
                            + obj.getString("country")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("sourcename")
                            + "|" + obj.getString("agentname")
                            + "|" + obj.getString("transferno")
                            + "|" + obj.getString("resultdescription")
                    ;

                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCreateAccountResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|" +
                            obj.getString("responsects") + "|" + obj.getString("source") + "|" + obj.getString("transid") +
                            "|" + obj.getString("state") + "|" + obj.getString("country")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("sourcename")
                            + "|" + obj.getString("resultdescription");

                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getBillPayBillResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReprintBillPayBillResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel getRemmitanceSendMoneyToMobileResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("sourcename")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("confcode")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("showconfcode")
                            + "|" + obj.getString("resultdescription")
                    ;


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (JSONException e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }


    public GeneralResponseModel getReprintSendMoneyToMobileResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("sourcename")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("confcode")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("agentname")
                            + "|" + obj.getString("transferno")
                            + "|" + obj.getString("currency")
                            + "|" + obj.getString("resultdescription")
                    ;


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (JSONException e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getCashToCashReceiveResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") +
                            "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("responsects")
                            + "|" + obj.getString("destcountry")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("destination")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("referencenumber")
                            + "|" + obj.getString("responsects") // hard code confirmation page

                            + "|" + obj.getString("resultdescription");

               /*
                {"agentcode":"237785785785","status":"RA","resultcode":"0","code":"",
                        "responsevalue":"Transaction Successful","agentname":"test","destination":
                    "237999666666","country":"Cameroon","vendorcode":"MICROEU",
                        "source":"237995555555",
                        "resultdescription":"Transaction Successful",
                        "destinationname":"ANWAR","transactionid":"1234",
                        "responsects":"","destcountry":"","referencenumber":
                    "PBX0711170003","transid":"9328163","agentbranch":"EU MEIDOUGOU"}*/


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (JSONException e) {
            Log.d("myTag", "This is my message");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReprintReceiveMoneyToMobile(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") +   // 0
                            "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("responsects")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("destinationname")  // 4
                            + "|" + obj.getString("destination")   // 5
                            + "|" + obj.getString("agentbranch")   // 6
                            + "|" + obj.getString("fee")          // 7
                            + "|" + obj.getString("country")     // 8
                            + "|" + obj.getString("amount")     // 9
                            + "|" + obj.getString("agentname")  // 10
                            + "|" + obj.getString("responsects")  // 11
                            + "|" + obj.getString("comments")  // 12
                            + "|" + obj.getString("transferno")  // 13
                            + "|" + obj.getString("currency")  // 14
                            + "|" + obj.getString("tax")   //
                            + "|" + obj.getString("resultdescription");

              /*  {
                    "labelname": "",
                        "billercode": "",
                        "showconfcode": "",
                        "userid": "",
                        "sourcename": "",
                        "state": "AbangMinkoo",
                        "resultcode": "0",
                        "responsevalue": "",
                        "prewalletbalance": "",
                        "amount": "948",
                        "accounttype": "",
                        "cin": "",
                        "transid": "9338709",
                        "destbranch": "",
                        "walletbalance": "",
                        "customername": "",
                        "transtype": "REMTRECV",
                        "trnsdate": "04/12/2017 11:08:55",
                        "agentcode": "237785785785",
                        "billername": "",
                        "udv3": "04/12/2017 11:08:55",
                        "udv4": "",
                        "agentname": "test",
                        "udv2": "",
                        "destination": "237000271015",
                        "country": "Cameroon",
                        "vendorcode": "MICROEU",
                        "fee": "0.0",
                        "confcode": "",
                        "invoiceno": "",
                        "source": "237000271503",
                        "tax": "",
                        "clienttype": "GPRS",
                        "resultdescription": "Transaction Successful",
                        "destinationname": "",
                        "requestcts": "",
                        "transrefno": "",
                        "aprusr": "",
                        "responsects": "04/12/2017 11:10:30",
                        "transferno": "9338707",
                        "language": "EN",
                        "agenttype": "",
                        "comments": "gtg",
                        "agentbranch": "EU BANGANGTE  II"
                }
*/


               /*
                {"agentcode":"237785785785","status":"RA","resultcode":"0","code":"",
                        "responsevalue":"Transaction Successful","agentname":"test","destination":
                    "237999666666","country":"Cameroon","vendorcode":"MICROEU",
                        "source":"237995555555",
                        "resultdescription":"Transaction Successful",
                        "destinationname":"ANWAR","transactionid":"1234",
                        "responsects":"","destcountry":"","referencenumber":
                    "PBX0711170003","transid":"9328163","agentbranch":"EU MEIDOUGOU"}*/


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (JSONException e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getSearchCashToCashReceiveResponseNew(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);

            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") +          // 0
                            "|" + obj.getString("source") +
                            "|" + obj.getString("destination") +
                            "|" + obj.getString("country") +
                            "|" + obj.getString("destinationname") +   // 4
                            "|" + obj.getString("resultdescription");


                    ArrayList<String> list = new ArrayList<String>();
                    JSONArray jsonArray = new JSONArray(obj.getString("records"));

                    String listDataString = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objj = jsonArray.getJSONObject(i);

                        listDataString = objj.getString("paysdest") +
                                "|" + objj.getString("referenceid") +
                                "|" + objj.getString("reason") +
                                "|" + objj.getString("ageexp") +
                                "|" + objj.getString("destinationmobilenumber") +   // 4 destination number
                                "|" + objj.getString("date") +        // 5 date
                                "|" + objj.getString("nomexp") +    // 6 sender name
                                "|" + objj.getString("amount") +    // 7 amount
                                "|" + objj.getString("paysexp") +   // 8
                                "|" + objj.getString("nomdest") +    // 9 Destination Name
                                "|" + objj.getString("currencycode") +// 10 currencycode
                                "|" + objj.getString("srcmobilenumber") +// 11  source mobile number

                                "|" + objj.getString("referenceid");


                        list.add(listDataString);
                    }


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        System.out.println(listDataString);
                        generalResponseModel.setCustomResponseList(list);
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }

            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (JSONException e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getSearchCashToCashReceiveResponseImoney(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") +
                            "|" + obj.getString("currency") +
                            "|" + obj.getString("nomexp") +
                            "|" + obj.getString("nombenef") +
                            "|" + obj.getString("source") +
                            "|" + obj.getString("destination") +
                            "|" + obj.getString("amount") +
                            "|" + obj.getString("feesenv") +
                            "|" + obj.getString("amountinWord") +
                            "|" + obj.getString("status") +
                            "|" + obj.getString("resultdescription");


/*
                {"blockstatus":"N","codemandat":"P160310170002","feesupdate":null,"resultcode":"0",
                "responsevalue":"Transaction Successful","currency":"XAF","nombenef":"GXHCHCHCHC "
                ,"transactionid":"856","transid":"6647965","senderaccount":"10F3147653701","nomexp":"FZXGGGXGF ",
                "agentcode":"237000271099","status":"EV","benefaccount":"","idreq":"0","code":"0","agentname":"shipra FR",
                "destination":"237688338353","country":"","lastupdatedate":"2017-10-03","vendorcode":"MICROEU","feesenv":"20.000","feesannul":"","source":"237653568585",
                "resultdescription":"Transaction Successful","destinationname":"","destcountry":"","cancelstatus":"N"}
*/


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }

            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (JSONException e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getSearchCashToCashReceiveResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") +
                            "|" + obj.getString("currency") +
                            "|" + obj.getString("nomexp") +
                            "|" + obj.getString("nombenef") +
                            "|" + obj.getString("source") +
                            "|" + obj.getString("destination") +
                            "|" + obj.getString("amount") +
                            "|" + obj.getString("feesenv") +
                            "|" + obj.getString("amountinWord") +
                            "|" + obj.getString("status") +
                            "|" + obj.getString("resultdescription");


/*
                {"blockstatus":"N","codemandat":"P160310170002","feesupdate":null,"resultcode":"0",
                "responsevalue":"Transaction Successful","currency":"XAF","nombenef":"GXHCHCHCHC "
                ,"transactionid":"856","transid":"6647965","senderaccount":"10F3147653701","nomexp":"FZXGGGXGF ",
                "agentcode":"237000271099","status":"EV","benefaccount":"","idreq":"0","code":"0","agentname":"shipra FR",
                "destination":"237688338353","country":"","lastupdatedate":"2017-10-03","vendorcode":"MICROEU","feesenv":"20.000","feesannul":"","source":"237653568585",
                "resultdescription":"Transaction Successful","destinationname":"","destcountry":"","cancelstatus":"N"}
*/


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (JSONException e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }


    public GeneralResponseModel getCastoCashSendMoneySameCountryResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("resultdescription") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fees") + "|"
                            + obj.getString("amount") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("source")
                            + "|" + obj.getString("accountded")
                            + "|" + obj.getString("referencenumber")
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("country")
                            + "|" + obj.getString("transcode")
                            + "|" + obj.getString("destcountry")
                            + "|" + obj.getString("resultdescription");



               /*   {"total":"610","sourcename":"","resultcode":"0","responsevalue":"Transaction Successful",
                        "agnentName":"shipra FR","amount":"600","transid":"6646837","agentcode":"237000271099",
                        "fees":"10.0","code":"0","agentname":"shipra FR","transcode":"RW6X","destination":"237999999999",
                        "country":"Cameroon","vendorcode":"MICROEU","source":"237888888888","accountded":
                    "10F3148053701","resultdescription":"Transaction Successful","destinationname":"","destcountry":
                    "Cameroon","responsects":"27/10/2017 15:31:00",
                        "balancebefore":"498512804.000","referencenumber":"P8M0310170133","comments":"",
                        "agentbranch":"EU BUEA I"}*/


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (JSONException e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }


    public GeneralResponseModel getuploadImageResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|"

                            + obj.getString("resultdescription")
                            + "|" + obj.getString("resultdescription");


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (JSONException e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getUploadImagePictureSignature(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                generalResponseModel.setResponseCode(0);

                temp = obj.getString("transid") + "|"
                        + obj.getString("resultdescription")
                ;
                if (obj.has("transferno")) {
                    generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                } else {
                    generalResponseModel.setUserDefinedString(temp);
                }

            } else {
                generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
            }
        } catch (JSONException e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getReprintCastoCashSendMoneySameCountryResponse(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("transid") + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("comments") + "|"
                            + obj.getString("responsects") + "|" + obj.getString("country") + "|"
                            + obj.getString("destination") + "|" + obj.getString("fee") + "|"
                            + obj.getString("amount") + "|" + obj.getString("state")
                            + "|" + obj.getString("destinationname")
                            + "|" + obj.getString("source")     // source 10
                            + "|" + obj.getString("sourcename")  // sender name 11
                            + "|" + obj.getString("agentbranch")
                            + "|" + obj.getString("resultdescription")
                            + "|" + obj.getString("confcode")
                            + "|" + obj.getString("referenceNumber")    // reference 15
                            + "|" + obj.getString("currency")          // currency  16
                            + "|" + obj.getString("agentname")         // agentname  17
                            + "|" + obj.getString("transferno")         // old transactionID  18
                            + "|" + obj.getString("tax")         // tax 19
                            + "|" + obj.getString("resultdescription")
                    ;


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (JSONException e) {

            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");

        }

        return generalResponseModel;
    }

    public GeneralResponseModel getUnpaidBillerInfoResponse(String jsonString) {


        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        String invoiceDetails = "";

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);
                    try {


                        JSONArray jsonArray = new JSONArray(obj.getString("unpaidbillers"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject objj = jsonArray.getJSONObject(i);

                            if (invoiceDetails.trim().length() == 0) {
                                invoiceDetails = objj.getString("invoiceno")
                                        + ";" + objj.getString("billdate") +
                                        ";" + objj.getString("amount") +
                                        ";" + objj.getString("feeamount");


                            } else {
                                invoiceDetails = invoiceDetails + "|" +
                                        objj.getString("invoiceno") + ";"
                                        + objj.getString("billdate")
                                        + ";" + objj.getString("amount") +
                                        ";" + objj.getString("feeamount");
                            }
                        }
                        //invoiceDetails=invoiceDetails+";"+obj.getString("destination");
                        generalResponseModel.setUserDefinedString(invoiceDetails);
                    } catch (Exception e) {
                        generalResponseModel.setResponseCode(99);
                        generalResponseModel.setUserDefinedString(obj.getString("Invoices Not Available."));
                    }
                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    //    ############################### Purchase Menu Airtime  6 Dec 2019 ###############################

    public GeneralResponseModel get_airtime_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_electricityCompany_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_searchBillnumber_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    public GeneralResponseModel get_checkBoxData_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_tvCompanyDistributor_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }
    public GeneralResponseModel get_subscriptionType_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }
    public GeneralResponseModel get_optionType_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }
    public GeneralResponseModel get_tvSubscriptionFinal_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_taxpayerType_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_rechargeDetails_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_categoryType_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_companyPartner_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_cashDepositType_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_electricityCompany_recharge_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    public GeneralResponseModel get_search_recharge_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }
    public GeneralResponseModel get_electricCompany_Response(String jsonString) {

        GeneralResponseModel generalResponseModel = new GeneralResponseModel();
        try {
            JSONObject obj = new JSONObject(jsonString);
            try {
                if (obj.getString("resultcode").equalsIgnoreCase("0")) {

                    generalResponseModel.setResponseCode(0);

                    temp = obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription") + "|"+
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription") + "|" +
                            obj.getString("resultdescription")+ "|" +
                            obj.getString("resultdescription");


                    if (obj.has("transferno")) {
                        generalResponseModel.setUserDefinedString(temp + "|" + obj.getString("transferno"));
                    } else {
                        generalResponseModel.setUserDefinedString(temp);
                    }

                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));
                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }
        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }


    // ##################################### Update Version Check ################################################################

    public GeneralResponseModel updtaVersionCheck(String jsonString) {
        GeneralResponseModel generalResponseModel = new GeneralResponseModel();

        try {
            JSONObject obj = new JSONObject(jsonString);
            try {

                if (obj.getString("resultcode").equalsIgnoreCase("0")) {
                    generalResponseModel.setResponseCode(0);

                    generalResponseModel.setUserDefinedString(obj.getString("apkversion")+"|"+obj.getString("resultdescription"));


                } else {
                    generalResponseModel.setUserDefinedString(obj.getString("resultdescription"));
                    generalResponseModel.setResponseCode(Integer.parseInt(obj.getString("resultcode")));

                }
            } catch (Exception e) {

                generalResponseModel.setResponseCode(99);
                generalResponseModel.setUserDefinedString("Please try again later");
            }

        } catch (Exception e) {
            generalResponseModel.setResponseCode(99);
            generalResponseModel.setUserDefinedString("Please try again later");
        }

        return generalResponseModel;
    }

    // #####################################################################################################

}
