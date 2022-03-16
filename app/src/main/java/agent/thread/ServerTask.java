package agent.thread;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import callback.MessageFromActivity;
import commonutilities.ComponentMd5SharedPre;


public class ServerTask extends Thread implements MessageFromActivity {
    private boolean interupt = false;

    private ComponentMd5SharedPre componentInfo;
    // private ServerResponse serverResponse;

    private Context ctx;
    HttpURLConnection connection;

     // #################### http (3668) ##########################################


    //   public static String baseUrl = "http://195.24.207.114:3668/RESTfulWebServiceEU/json/estel/",           //    Public 114 PORT 3667

       public static String baseUrl = "http://195.24.207.114:3668/RESTfulWebServiceEU_apkversion/json/estel/",           //    Temporary 23 Dec 2021



    // #################### https (3667) ##########################################

     //  public static String baseUrl = "https://195.24.207.114:3667/RESTfulWebServiceEU/json/estel/",           //    Public 114 PORT 3667

     //   public static String baseUrl = "https://192.168.0.224:9090/RESTfulWebServiceEU/json/estel/",        //    Local 224 estel

     //    public static String baseUrl = "http://expressunion.cm:5051/RESTfulWebServiceEU/json/estel/",    //   Production  08 08 2017

   // ####################################################################################

    parameter = "", webResponse = "", bodyData = "", apiName = "";

    int requestNo;
    Handler mHandler;

    public ServerTask(ComponentMd5SharedPre componentInfo, Context ctx, Handler mHandler, String body, String apiName, int requestNo) {
        this.componentInfo = componentInfo;
        this.apiName = apiName;
        this.ctx = ctx;
        this.mHandler = mHandler;
        this.bodyData = body;
        this.requestNo = requestNo;

        SsltAllCertificates.trustAllCertificates();

    }

    @Override
    public void onRecieveMessage(String message, int action) {
        switch (message) {
            case "interupt":
                interupt = false;
                break;
            default:
                break;
        }
    }

    private void postMessage(String webResponse, int requestNo) {
        if (componentInfo.activityRunning) {
            Message msg = new Message();
            msg.obj = webResponse;
            msg.arg1 = requestNo;
            mHandler.sendMessage(msg);
            // serverResponse.onServerResponse(message, requestNo);
        }
    }

    @Override
    public void run() {
        if (interupt == false) {

            String URL = "";

            if (!interupt) {

                try {

                    /*

                        Log.e("", "=========== HTTP REQUEST===========");
                        Log.e("", "Request Name  - " + apiName);
                        Log.e("", "Request Data  - " + bodyData);
                        Log.e("", "====================================");

                     */

                    Thread.sleep(2000);
                    System.setProperty("http.keepAlive", "false");
                    URL url = new URL(baseUrl + apiName);

                    try {
                        connection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    connection.setDoOutput(true);          //    setDoOutput(true) is used with POST to allow sending a body via the connection
                    connection.setDoInput(true);           //    doInput flag to true indicates that the application intends to read data from the URL  Coonection,,,setDoOutput(true) is used for POST and PUT requests. If it is false then it is for using GET requests
                    connection.setUseCaches(false);        //   the connection is allowed to use whatever caches it can. If false, caches are to be ignored
                    connection.setConnectTimeout(60000);
                    connection.setRequestMethod("POST");

                    //   if ( Build.VERSION.SDK_INT > 13) {
                    //   connection.setRequestProperty("Connection", "close"); }

                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    // connection.setRequestProperty("auth_token", Token);
                    // conn.setRequestProperty("Content-Type",
                    // "application/x-www-form-urlencoded;charset=UTF-8");
                    OutputStream out = null;
                    out = connection.getOutputStream();
                    String body = bodyData;
                    out.write(body.getBytes());
                    out.close();
                    connection.connect();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((str = reader.readLine()) != null) {
                        stringBuilder.append(str);
                        webResponse = stringBuilder.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Log.e("=======================", " Url Complete Api =======================  " + baseUrl+apiName);
                Log.e("=======================", " Request No       =======================  " + requestNo);
                Log.e("=======================", " Body Request     =======================  " + bodyData);
                Log.e("=======================", " Server Response  =======================  " + webResponse);


         /*
                // #################### For Demo #######################
                // ############ Cash In ############

                if(requestNo==148)
                {
                   webResponse="{\"idproofduedate\":\"\",\"profession\":\"\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"ABAN\",\"currency\":\"FCR-XAF\",\"amount\":\"250000\",\"idproofissueplace\":\"\",\"residencearea\":\"\",\"gender\":\"\",\"transid\":\"11187462\",\"idproof\":\"12365442432\",\"fixphoneno\":\"\",\"idproofissuedate\":\"11/01/2017 00:00:00\",\"issamebranch\":\"true\",\"agentcode\":\"237000271502\",\"profilename\":\"Plan Agent\",\"image\":\"\",\"firstname\":\"shipra Ag2\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"\",\"idprooftype\":\"DRVLIC\",\"source\":\"237000271501\",\"birthplace\":\"\",\"clienttype\":\"SELFCARE\",\"resultdescription\":\"Transaction Successful\",\"email\":\"\",\"address\":\"\",\"requestcts\":\"\",\"dob\":\"2018-03-01 00:00:00.0\",\"Sign\":\"\",\"responsects\":\"04/12/2020 06:16:11\",\"statename\":\"AbangMinkoo\",\"agenttype\":\"BANKAG\",\"language\":\"French\",\"tla\":\"\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";
                }
                else if(requestNo==114)
                 {
                     webResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"ABAN\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"237000271501\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"1500\",\"fromcity\":\"ABAN\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"\",\"responsects\":\"04/12/2020 06:17:37\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11187463\",\"comments\":\"\",\"walletbalance\":\"2850576.81\"}";
                  }
                else if(requestNo==119)
                {
                    webResponse="{\"traderegno\":\"\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"\",\"amount\":\"1500.0\",\"taxpayercard\":\"\",\"responsValue\":\"0\",\"faxno\":\"\",\"transid\":\"11187464\",\"destbranch\":\"EU BUEA I\",\"walletbalance\":\"2849076.81\",\"phoneno\":\"\",\"agentcode\":\"237000271502\",\"agentname\":\"shipra Ag3\",\"destination\":\"237000271501\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"0.0\",\"source\":\"237000271502\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"destinationname\":\"shipra Ag2\",\"responsects\":\"04/12/2020 06:18:54\",\"language\":\"EN\",\"comments\":\"\",\"agentbranch\":\"EU BUEA I\"}";
                 }

                // ############ Cash Out ############

                else if(requestNo==141)
                {
                    webResponse="{\"agentcode\":\"237000271502\",\"resultcode\":\"0\",\"agentname\":\"shipra Ag3\",\"fee\":\"\",\"vendorcode\":\"MICROEU\",\"amount\":\"500\",\"confcode\":\"GWYF\",\"tax\":\"0.0\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"\",\"responsects\":\"04/12/2020 06:27:55\",\"accounttype\":\"MA\",\"transid\":\"11187475\",\"comments\":\"commentSms\",\"walletbalance\":\"391922.3\"}";
                }

                else if(requestNo==215)
                {
                    webResponse="{\"idproofduedate\":\"\",\"profession\":\"\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"ABAN\",\"currency\":\"FCR-XAF\",\"amount\":\"250000\",\"idproofissueplace\":\"\",\"residencearea\":\"\",\"gender\":\"\",\"transid\":\"11187479\",\"idproof\":\"12365442432\",\"fixphoneno\":\"\",\"idproofissuedate\":\"11/01/2017 00:00:00\",\"issamebranch\":\"true\",\"agentcode\":\"237000271502\",\"profilename\":\"Plan Agent\",\"image\":\"\",\"firstname\":\"shipra Ag2\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"\",\"idprooftype\":\"DRVLIC\",\"source\":\"237000271501\",\"birthplace\":\"\",\"clienttype\":\"SELFCARE\",\"resultdescription\":\"Transaction Successful\",\"email\":\"\",\"address\":\"\",\"requestcts\":\"\",\"dob\":\"2018-03-01 00:00:00.0\",\"Sign\":\"\",\"responsects\":\"04/12/2020 06:31:02\",\"statename\":\"AbangMinkoo\",\"agenttype\":\"BANKAG\",\"language\":\"French\",\"tla\":\"\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";
                }

                else if (requestNo==148)
                {
                    webResponse="{\"idproofduedate\":\"\",\"profession\":\"\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"ABAN\",\"currency\":\"FCR-XAF\",\"amount\":\"250000\",\"idproofissueplace\":\"\",\"residencearea\":\"\",\"gender\":\"\",\"transid\":\"11187462\",\"idproof\":\"12365442432\",\"fixphoneno\":\"\",\"idproofissuedate\":\"11/01/2017 00:00:00\",\"issamebranch\":\"true\",\"agentcode\":\"237000271502\",\"profilename\":\"Plan Agent\",\"image\":\"\",\"firstname\":\"shipra Ag2\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"\",\"idprooftype\":\"DRVLIC\",\"source\":\"237000271501\",\"birthplace\":\"\",\"clienttype\":\"SELFCARE\",\"resultdescription\":\"Transaction Successful\",\"email\":\"\",\"address\":\"\",\"requestcts\":\"\",\"dob\":\"2018-03-01 00:00:00.0\",\"Sign\":\"\",\"responsects\":\"04/12/2020 06:16:11\",\"statename\":\"AbangMinkoo\",\"agenttype\":\"BANKAG\",\"language\":\"French\",\"tla\":\"\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";
                }

                else if (requestNo==142)
                 {
                    webResponse="{\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"\",\"prewalletbalance\":\"1.0001E7\",\"amount\":\"250.0\",\"responsValue\":\"0\",\"faxno\":\"\",\"accounttype\":\"MA\",\"idproof\":\"\",\"transid\":\"10492160\",\"destbranch\":\"EU BAFOUSSAM I\",\"walletbalance\":\"1.000125E7\",\"phoneno\":\"\",\"agentcode\":\"237000271501\",\"agentname\":\"shipra Ag2\",\"transcode\":\"2SPE\",\"destination\":\"237000271510\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"62.0\",\"idprooftype\":\"\",\"tax\":\"12.0\",\"source\":\"237000271501\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"shipra sub\",\"requestcts\":\"\",\"responsects\":\"13/11/2018 06:04:37\",\"language\":\"FR\",\"agenttype\":\"\",\"comments\":\"comments\",\"agentbranch\":\"EU BUEA I\"}";
                 }



                else
                {

                }
                */
                // #################### For Demo #######################

                connection.disconnect();
                connection = null;

            }

            postMessage(webResponse, requestNo);
        }
    }
}
