package agent.eui.sendmoney_cashtocash_fragment;

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

/**
 * Created by AdityaBugalia on 01/07/16.
 */
public class IdprooftypeRequestUrlEnglish extends Thread implements MessageFromActivity {
    private boolean interupt = false;

    private ComponentMd5SharedPre componentInfo;
    // private ServerResponse serverResponse;

    private Context ctx;
    HttpURLConnection connection;


    public static String baseUrl = "https://wsdev.expressunion.net/transfinapiws/transactions/pay-out/",      //   Public 114


    parameter = "", webResponse = "", bodyData = "", apiName = "";

    int requestNo;
    Handler mHandler;

    public IdprooftypeRequestUrlEnglish(ComponentMd5SharedPre componentInfo, Context ctx, Handler mHandler, String body, String apiName, int requestNo) {
        this.componentInfo = componentInfo;
        this.apiName = apiName;
        this.ctx = ctx;
        this.mHandler = mHandler;
        this.bodyData = body;
        this.requestNo = requestNo;
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
                    Log.e("", "http start");
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
                    connection.setRequestMethod("GET");

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
              //  Log.e("http end-" + requestNo, "Resquest Json --> " + bodyData);
               // Log.e("http end-" + requestNo, "Response Json  - image -> " + webResponse);

                // Log.e("webResponse=========",webResponse);


                //  webResponse = "[{\"code\":\"CE\",\"libelle\":\"Carte Electeur\"},{\"code\":\"CI\",\"libelle\":\"Carte Nationale d'Identit¿\"},{\"code\":\"PP\",\"libelle\":\"Passeport\"}]";

                webResponse = "[{\"code\":\"NIC\",\"libelle\":\"National Identity Card\"},{\"code\":\"PASSPORT\",\"libelle\":\"Passport\"},{\"code\":\"DRVLIC\",\"libelle\":\"Driver license\"},{\"code\":\"VOTRID\",\"libelle\":\"Voter Card\"}]";

                connection.disconnect();
                connection = null;
            }

            Log.e("" + requestNo, webResponse);
            postMessage(webResponse, requestNo);
        }
    }
}
