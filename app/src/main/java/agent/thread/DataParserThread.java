package agent.thread;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;


public class DataParserThread extends AsyncTask<Void, Void, Void> {

    private ServerResponseParseCompletedNotifier parseCompletedNotifier;
    private int requestNo;
    private ComponentMd5SharedPre mComponentInfo;
    private Context ctx;
    //private HashMap<String,String> generalResponseList;
    private ArrayList<Object> customResponseList;
    private String userDefinedString, serverResponse;
    private GeneralResponseModel responseModel;

    public DataParserThread(Context ctx, ComponentMd5SharedPre componentInfo, ServerResponseParseCompletedNotifier parseCompletedNotifier, int requestNo, String serverResponse) {
        this.ctx = ctx;
        this.mComponentInfo = componentInfo;
        this.parseCompletedNotifier = parseCompletedNotifier;
        this.requestNo = requestNo;
        this.serverResponse = serverResponse;

    }


    @Override
    protected Void doInBackground(Void... voids) {


        switch (requestNo) {


            case 2021:  // version

                // serverResponse="{\"resultdescription\":\"Transaction Successful\",\"resultcode\":\"0\",\"apkversion\":\"1\"}";

                responseModel = new DataParser().updtaVersionCheck(serverResponse);

                break;

            case 101://country data

             //   serverResponse="{\"agentcode\":\"237100001012\",\"appversionDRCBA\":\"ANDROID,true,1,1.1,1.0,0,0\",\"appversion\":\"ANDROID,true,3,1.2,1.0,0,0123\",\"appversionfr\":\"ANDROID,true,3,1.2,1.0,0,0123\",\"resultcode\":\"0\",\"countrylist\":[{\"countrycode\":\"IND\",\"isocode\":\"91\",\"countryname\":\"India\",\"cashoutamount\":\"1000\"},{\"countrycode\":\"CAM\",\"isocode\":\"237\",\"countryname\":\"Cameroon\",\"mobilelength\":\"9\",\"cashoutamount\":\"900\"},{\"countrycode\":\"GBN\",\"isocode\":\"241\",\"countryname\":\"Gabon\",\"mobilelength\":\"8\",\"cashoutamount\":\"900\"},{\"countrycode\":\"CNG\",\"isocode\":\"242\",\"countryname\":\"Congo\",\"cashoutamount\":\"900\"},{\"countrycode\":\"TCH\",\"isocode\":\"235\",\"countryname\":\"Tchad\",\"mobilelength\":\"9\",\"cashoutamount\":\"900\"},{\"countrycode\":\"RCA\",\"isocode\":\"236\",\"countryname\":\"Central African Republic\",\"mobilelength\":\"8\",\"cashoutamount\":\"900\"},{\"countrycode\":\"DRC\",\"isocode\":\"243\",\"countryname\":\"Democratic Republic of Congo\",\"mobilelength\":\"9\",\"cashoutamount\":\"900\"},{\"countrycode\":\"DBI\",\"isocode\":\"971\",\"countryname\":\"Dubai - United Arab Emirates\",\"mobilelength\":\"9\",\"cashoutamount\":\"0\"},{\"countrycode\":\"BDS\",\"isocode\":\"880\",\"countryname\":\"Bangladesh\",\"mobilelength\":\"9\",\"cashoutamount\":\"0\"}],\"apversionmer\":\"ANDROID,true,1,1.1,1.0,0,0\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"25/05/2016 18:01:51\",\"apversionbrc\":\"ANDROID,true,1,1.1,1.0,0,0\",\"responsects\":\"14/12/2018 11:16:29\",\"apversionba\":\"ANDROID,true,1,1.1,1.0,0,0\",\"transid\":\"8017978\"}";


                responseModel = new DataParser().getCountryData(serverResponse);
                break;

            case 102://login

                // serverResponse="{\"agentcode\":\"237000271502\",\"state\":\"ABAN\",\"parent\":\"237000016000\",\"resultcode\":\"0\",\"otpflag\":\"true\",\"tlacity\":\"BUE\",\"agentname\":\"shipra Ag3\",\"tlacode\":\"237000016000\",\"country\":\"Cameroon\",\"currency\":\"FCR-XAF\",\"profileactivation\":\"false\",\"mpinexpiry\":\"NA\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"18/10/2019 11:29:56\",\"firstlogin\":\"\",\"responsects\":\"18/10/2019 11:29:57\",\"accounttype\":\"2\",\"language\":\"EN\",\"tla\":\"EU BUEA I\",\"agenttype\":\"\",\"otp\":\"048430\",\"transid\":\"11078674\"}";

                responseModel = new DataParser().getLoginResponse(serverResponse);

                break;

            case 103:  //otp verification

               /* serverResponse="{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"requestcts\": \"13/09/2018 14:14:44\",\n" +
                        "  \"responsects\": \"13/09/2018 14:14:44\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"otpflag\": \"\",\n" +
                        "  \"transid\": \"7996946\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";
*/
                responseModel = new DataParser().getOtpResponse(serverResponse);

                break;

            case 104:// activation
                responseModel = new DataParser().getActivationResponse(serverResponse);
                break;

            case 105:// agentinfo
                responseModel = new DataParser().getAgentInfoResponse(serverResponse);
                break;

            case 106: //biller name fetch
                responseModel = new DataParser().getBillerResponse(serverResponse);
                break;

            case 107: // account To Cash
                responseModel = new DataParser().getAccountToCashResponse(serverResponse);
                break;
            case 108:// account to Account
                responseModel = new DataParser().getAccountToAccountResponse(serverResponse);
                break;
            case 109: // bill payment
                // serverResponse="{\"energyconst\":\"\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"sourcename\":\"NGOUPAYOU OUMAROU \",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"otpflag\":\"\",\"amount\":\"700.0\",\"responsValue\":\"Transaction Successful\",\"duedate\":\"12/31/2017\",\"accounttype\":\"MA\",\"otp\":\"\",\"transid\":\"9310493\",\"walletbalance\":\"99819884.80\",\"prdordno\":\"\",\"agentcode\":\"237785785785\",\"billername\":\"ENEO\",\"feesupportedby\":\"SUB\",\"destinatinBalance\":\"610172.0\",\"agentname\":\"test\",\"custphoneno\":\"\",\"destination\":\"237100004001\",\"country\":\"Cameroon\",\"fee\":\"200.0\",\"vendorcode\":\"MICROEU\",\"invoiceno\":\"305793803\",\"tax\":\"32.0\",\"source\":\"201341861\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"ENEO\",\"requestcts\":\"03/08/2017 14:23:21\",\"responsects\":\"03/08/2017 14:23:23\",\"language\":\"EN\",\"energy\":\"\",\"comments\":\"\",\"agentbranch\":\"EU MEIDOUGOU\"}";

                //  serverResponse="{\"energyconst\":\"\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"sourcename\":\"NGOUPAYOU OUMAROU \",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"otpflag\":\"\",\"amount\":\"550.0\",\"responsValue\":\"Transaction Successful\",\"duedate\":\"12/31/2017\",\"accounttype\":\"MA\",\"otp\":\"\",\"transid\":\"9310590\",\"walletbalance\":\"99818877.20\",\"prdordno\":\"\",\"agentcode\":\"237785785785\",\"billername\":\"ENEO\",\"feesupportedby\":\"SUB\",\"destinatinBalance\":\"621672.0\",\"agentname\":\"test\",\"custphoneno\":\"\",\"destination\":\"237100004001\",\"country\":\"Cameroon\",\"fee\":\"200.0\",\"vendorcode\":\"MICROEU\",\"invoiceno\":\"324410838\",\"tax\":\"32.0\",\"source\":\"201341861\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"ENEO\",\"requestcts\":\"03/08/2017 17:42:59\",\"responsects\":\"03/08/2017 17:43:01\",\"language\":\"EN\",\"energy\":\"\",\"comments\":\"\",\"agentbranch\":\"EU MEIDOUGOU\"}";
                responseModel = new DataParser().getBillPaymentResponse(serverResponse);
                break;
            case 110://Payment Request
                responseModel = new DataParser().getPaymentRequestResponse(serverResponse);

                break;
            case 111:// payement Auth
                responseModel = new DataParser().getPaymentAuthResponse(serverResponse);
                break;
            case 112:    // Account Balance
                responseModel = new DataParser().getBalanceCheckResponse(serverResponse);
                break;
            case 113:   // Report Mini Statements
                responseModel = new DataParser().getMiniStmtResponse(serverResponse);
                break;

            case 114: // Tariff

               // serverResponse="{\"transtype\":\"CASHOUT\",\"tariffs\":[],\"agentcode\":\"237000271501\",\"billercode\":\"\",\"tocity\":\"ABAN\",\"feesupportedby\":\"\",\"resultcode\":\"0\",\"destination\":\"237000271510\",\"fee\":\"74.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"1500\",\"fromcity\":\"ABAN\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"\",\"responsects\":\"13/11/2018 05:44:58\",\"accounttype\":\"\",\"language\":\"FR\",\"transid\":\"10492156\",\"comments\":\"\",\"walletbalance\":\"10001000\"}";


                responseModel = new DataParser().getTariffResponse(serverResponse);

                break;
            case 115:
                responseModel = new DataParser().getBankingActivationResponse(serverResponse);
                break;

            case 116:
                // serverResponse="{\"agentcode\":\"237785785785\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"03/08/2017 17:48:29\",\"unpaidbillers\":[{\"amount\":\"87461.0\",\"billdate\":\"2013-09-17 14:04:51.0\",\"invoiceno\":\"275570134\",\"feeamount\":\"500.0\",\"customerid\":\"201341897\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"duedate\":\"31/12/2017 23:59:59\",\"custPhoneNo\":null,\"custname\":\"MENDOUE TOBIE \",\"destination\":\"237100004001\"},{\"amount\":\"294876.0\",\"billdate\":\"2013-12-16 14:04:51.0\",\"invoiceno\":\"280412552\",\"feeamount\":\"750.0\",\"customerid\":\"201341897\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"duedate\":\"31/12/2017 23:59:59\",\"custPhoneNo\":null,\"custname\":\"MENDOUE TOBIE \",\"destination\":\"237100004001\"},{\"amount\":\"508620.0\",\"billdate\":\"2013-08-15 14:04:51.0\",\"invoiceno\":\"291252086\",\"feeamount\":\"1700.0\",\"customerid\":\"201341897\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"duedate\":\"31/12/2017 23:59:59\",\"custPhoneNo\":null,\"custname\":\"MENDOUE TOBIE \",\"destination\":\"237100004001\"},{\"amount\":\"3700.0\",\"billdate\":\"2015-04-16 14:04:51.0\",\"invoiceno\":\"305675180\",\"feeamount\":\"200.0\",\"customerid\":\"201341897\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"duedate\":\"31/12/2017 23:59:59\",\"custPhoneNo\":null,\"custname\":\"MENDOUE TOBIE \",\"destination\":\"237100004001\"},{\"amount\":\"4174.0\",\"billdate\":\"2014-11-25 14:04:51.0\",\"invoiceno\":\"297480147\",\"feeamount\":\"200.0\",\"customerid\":\"201341897\",\"billercode\":\"TR030\",\"duedate\":\"31/12/2017 23:59:59\",\"custPhoneNo\":null,\"custname\":\"MENDOUE TOBIE \",\"destination\":\"237100004001\"}],\"resultcode\":\"0\",\"transid\":\"9310592\"}";

                responseModel = new DataParser().getUnpaidBillerInfoResponse(serverResponse);
                break;

            case 117:  // Re send OTp
                responseModel = new DataParser().getResendOtpResponse(serverResponse);
                break;

            case 118:  // Change Mpin
                responseModel = new DataParser().getChangeMpinResponse(serverResponse);
                break;
            case 119:  // cash In
                responseModel = new DataParser().getCashInResponse(serverResponse);
                break;
            case 120:  // cash Out Same Brance
                responseModel = new DataParser().getCashOutSameBranchResponse(serverResponse);
                break;

            case 121:  // cash Out Diffrent Branch Branch

                /*  serverResponse="{\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"\",\"prewalletbalance\":\"1.000285E7\",\"amount\":\"100.0\",\"responsValue\":\"0\",\"faxno\":\"\",\"accounttype\":\"MA\",\"idproof\":\"\",\"transid\":\"10492214\",\"destbranch\":\"EU BUEA I\",\"walletbalance\":\"1.000295E7\",\"phoneno\":\"\",\"agentcode\":\"237000271501\",\"agentname\":\"shipra Ag2\",\"transcode\":\"JBWS\",\"destination\":\"237000271511\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"29.0\",\"idprooftype\":\"\",\"tax\":\"5.0\",\"source\":\"237000271501\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"wdjkhdkj\",\"requestcts\":\"\",\"responsects\":\"13/11/2018 07:39:14\",\"language\":\"FR\",\"agenttype\":\"\",\"comments\":\"tftfffftct\",\"agentbranch\":\"EU BUEA I\"}";*/
                responseModel = new DataParser().getCashOutDifferenteBranchResponse(serverResponse);
                break;


            case 122:  // Remmitance Send money  To Mobile
                responseModel = new DataParser().getRemmitanceSendMoneyToMobileResponse(serverResponse);
                break;
            case 123:  // Remmitance Send To Name
                responseModel = new DataParser().getRemmitanceSendMoneyToNameResponse(serverResponse);
                break;
            case 124:  // Remmitance Receive money To Mobile

               //  serverResponse="{\"sendermobile\":\"237222222222\",\"countrycode\":\"\",\"showconfcode\":\"\",\"sourcename\":\"\",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"responsevalue\":\"Transaction Successful\",\"otpflag\":\"\",\"currency\":\"CFA-XAF\",\"amount\":\"3962.655\",\"faxno\":\"\",\"accounttype\":\"\",\"transid\":\"11098165\",\"otp\":\"\",\"phoneno\":\"\",\"walletbalance\":\"2852995.81\",\"agentcode\":\"237000271502\",\"agentname\":\"shipra Ag3\",\"destination\":\"237333333333\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"0.0\",\"confcode\":\"\",\"tax\":\"0.0\",\"source\":\"237222222222\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"destinationname\":\"qqqqqqqqq\",\"requestcts\":\"\",\"responsects\":\"28/02/2020 07:41:10\",\"language\":\"EN\",\"agenttype\":\"\",\"comments\":\"\",\"agentbranch\":\"EU BUEA I\"}";

                responseModel = new DataParser().getRemmitanceReceiveMoneyToMobileResponse(serverResponse);
                break;
            case 125:  // Remmitance Receive money To Name
                responseModel = new DataParser().getRemmitanceReceiveMoneyToNameResponse(serverResponse);
                break;

            case 126:  // Re Prints


              /*  serverResponse="{\n" +
                        "  \"labelname\": \"\",\n" +
                        "  \"billercode\": \"\",\n" +
                        "  \"referenceNumber\": \"\",\n" +
                        "  \"userid\": \"\",\n" +
                        "  \"sourcename\": \"\",\n" +
                        "  \"state\": \"AbangMinkoo\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"responsevalue\": \"\",\n" +
                        "  \"currency\": \"XAF\",\n" +
                        "  \"prewalletbalance\": \"2828461.81\",\n" +
                        "  \"amount\": \"23000\",\n" +
                        "  \"accounttype\": \"\",\n" +
                        "  \"cin\": \"\",\n" +
                        "  \"transid\": \"11198521\",\n" +
                        "  \"destbranch\": \"EU SIEGE MA UNIV\",\n" +
                        "  \"walletbalance\": \"2805261.81\",\n" +
                        "  \"customername\": \"\",\n" +
                        "  \"transtype\": \"FEEPAYMENT\",\n" +
                        "  \"trnsdate\": \"15/07/2021 07:56:07\",\n" +
                        "  \"agentcode\": \"237000271502\",\n" +
                        "  \"billername\": \"\",\n" +
                        "  \"udv3\": \"15/07/2021 07:56:07\",\n" +
                        "  \"udv4\": \"\",\n" +
                        "  \"agentname\": \"shipra Ag3\",\n" +
                        "  \"udv2\": \"\",\n" +
                        "  \"destination\": \"237100001080\",\n" +
                        "  \"country\": \"Cameroon\",\n" +
                        "  \"vendorcode\": \"MICROEU\",\n" +
                        "  \"fee\": \"168.0\",\n" +
                        "  \"confcode\": \"\",\n" +
                        "  \"invoiceno\": \"0123445\",\n" +
                        "  \"source\": \"237555555555\",\n" +
                        "  \"tax\": \"32.0\",\n" +
                        "  \"clienttype\": \"GPRS\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"destinationname\": \"\",\n" +
                        "  \"requestcts\": \"\",\n" +
                        "  \"transrefno\": \"\",\n" +
                        "  \"aprusr\": \"\",\n" +
                        "  \"responsects\": \"15/07/2021 08:00:31\",\n" +
                        "  \"transferno\": \"11198510\",\n" +
                        "  \"language\": \"EN\",\n" +
                        "  \"agenttype\": \"\",\n" +
                        "  \"comments\": \"FEE::FRAIS EXAMEN BEPC:3500|FRAIS EXIGIBLES:10000|FRAIS PROBATOIRE:9500||SCHOOL::LT58097H01|LYCEE BILINGUE DE DEIDO||CLASS::1ere|NA|1ere||STUDENT::student sharique|student name Sharique anwar|15-07-2021|male|237333333333|11111111111111111|student@gmail.co\",\n" +
                        "  \"agentbranch\": \"EU BUEA I\"\n" +
                        "}";*/

                responseModel = new DataParser().getRePrintResponse(serverResponse);

                String txnType = responseModel.getUserDefinedString();

                switch (txnType) {
                    case "CASHIN":
                        responseModel = new DataParser().getReprintCashInResponse(serverResponse);
                        break;
                    case "CREATEAGENT":
                        responseModel = new DataParser().getReprintCreateAccountResponse(serverResponse);
                        break;

                    case "REMTSEND":
                        responseModel = new DataParser().getReprintSendMoneyToMobileResponse(serverResponse);
                        break;

                    case "REMTRECV":   //
                        responseModel = new DataParser().getReprintReceiveMoneyToMobile(serverResponse);
                        break;

                    case "CASHTOM":
                        responseModel = new DataParser().geReprintCashToMarchantResponse(serverResponse);
                        break;

                    case "CASHOUT":   // Reprint Cashout , Same , withdrawal
                        responseModel = new DataParser().getPrintCashoutSameDiferenceWithdrawaResponse(serverResponse);
                        break;

                    case "BILLPAY":   // REPRINT Not again  Transaction Not Allowed

                        //   serverResponse="{\"labelname\":\"FACTURE DE CONSOMMATION\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"userid\":\"201341720\",\"sourcename\":\"NSANU AMBE \",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"responsevalue\":\"\",\"prewalletbalance\":\"9.62887195E7\",\"amount\":\"1400\",\"accounttype\":\"\",\"cin\":\"\",\"transid\":\"9322918\",\"destbranch\":\"\",\"walletbalance\":\"9.62873195E7\",\"customername\":\"NSANU AMBE \",\"transtype\":\"BILLPAY\",\"trnsdate\":\"16/10/2017 13:25:38\",\"agentcode\":\"237785785785\",\"billername\":\"\",\"udv3\":\"16/10/2017 13:25:38\",\"udv4\":\"\",\"agentname\":\"test\",\"udv2\":\"\",\"destination\":\"237100004001\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"0.0\",\"confcode\":\"\",\"invoiceno\":\"305596595\",\"source\":\"201341720\",\"tax\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"ENEO\",\"requestcts\":\"\",\"transrefno\":\"\",\"aprusr\":\"\",\"responsects\":\"16/10/2017 13:42:27\",\"transferno\":\"9322912\",\"language\":\"EN\",\"agenttype\":\"\",\"comments\":\"\",\"agentbranch\":\"EU MEIDOUGOU\"}";
                        responseModel = new DataParser().getReprintBillPaymentResponse(serverResponse);
                        break;

                    case "SENDCASH":

                        //   serverResponse="{\"labelname\":\"FACTURE DE CONSOMMATION\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"userid\":\"201341720\",\"sourcename\":\"NSANU AMBE \",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"responsevalue\":\"\",\"prewalletbalance\":\"9.62887195E7\",\"amount\":\"1400\",\"accounttype\":\"\",\"cin\":\"\",\"transid\":\"9322918\",\"destbranch\":\"\",\"walletbalance\":\"9.62873195E7\",\"customername\":\"NSANU AMBE \",\"transtype\":\"BILLPAY\",\"trnsdate\":\"16/10/2017 13:25:38\",\"agentcode\":\"237785785785\",\"billername\":\"\",\"udv3\":\"16/10/2017 13:25:38\",\"udv4\":\"\",\"agentname\":\"test\",\"udv2\":\"\",\"destination\":\"237100004001\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"0.0\",\"confcode\":\"\",\"invoiceno\":\"305596595\",\"source\":\"201341720\",\"tax\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"ENEO\",\"requestcts\":\"\",\"transrefno\":\"\",\"aprusr\":\"\",\"responsects\":\"16/10/2017 13:42:27\",\"transferno\":\"9322912\",\"language\":\"EN\",\"agenttype\":\"\",\"comments\":\"\",\"agentbranch\":\"EU MEIDOUGOU\"}";
                        responseModel = new DataParser().getReprintCastoCashSendMoneySameCountryResponse(serverResponse);

                        break;


                    case "RECVCASH":   // REPRINT Not again  Transaction Not Allowed
                        responseModel = new DataParser().getReprintCashToCashReceiveMoneyToMobileResponse(serverResponse);
                        break;

                    case "REPRINT":   // REPRINT Not again  Transaction Not Allowed
                        responseModel = new DataParser().reprintNotValid(serverResponse);
                        break;

                    case "FEEPAYMENT":   // Tuition Fees  // 17 July 2021


                        // 17 July 2021 Server Response (Reprint Tution Fees)

                        // serverResponse="{\"agentCode\":\"237000271502\",\"source\":\"237999999999\",\"sourceName\":\"annu payer\",\"pin\":\"8C82976342B9DADE8CDBF6BE6BD3E6D7\",\"pintype\":\"MPIN\",\"amount\":\"23000.0\",\"billerName\":\"\",\"billerCode\":\"\",\"invoiceno\":\"0123445\",\"clientType\":\"GPRS\",\"comments\":\"FEE::FRAIS EXAMEN BEPC:3500|FRAIS EXIGIBLES:10000|FRAIS PROBATOIRE:9500||SCHOOL::LT58097H01|LYCEE BILINGUE DE DEIDO||CLASS::1ere|Arabe|1ere||STUDENT::bbbbbbbbb|aaaaaaaaa|19-07-2021|male|237999999999|11111111111|aaaaa@gmai.com||PAYER::237999999999|annu payer|payer@gmail.com\",\"testingTagString\":\"****Test 0:- @@@@ Test 1:-  ##### null $$$$ Test 3:- FRAIS EXAMEN BEPC:3500|FRAIS EXIGIBLES:10000|FRAIS PROBATOIRE:9500| &&&&& Test 4:- FRAIS EXAMEN BEPC:3500|FRAIS EXIGIBLES:10000|FRAIS PROBATOIRE:9500|\",\"testingTagStringArray\":\"[FRAIS EXAMEN BEPC, FRAIS EXIGIBLES, FRAIS PROBATOIRE]\",\"vendorCode\":\"MICR\",\"udv1\":\"\"}";

                        responseModel = new DataParser().getReprint_tution_fees(serverResponse);

                        break;

                }
                break;
            case 127:  // Transaction Cancel
                responseModel = new DataParser().getTransactionCancelResponse(serverResponse);
                break;


            case 128:  // Transaction Approved View


                //serverResponse="{\"labelname\":\"\",\"billercode\":\"\",\"sourcename\":\"sdfsdafs\",\"state\":\"AbangMinkoo\",\"resultcode\":\"0\",\"currency\":\"XAF\",\"amount\":\"101000\",\"destinationcountry\":\"\",\"accounttype\":\"\",\"transid\":\"10414554\",\"destbranch\":\"\",\"walletbalance\":\"999999500.00\",\"transtype\":\"\",\"agentcode\":\"237000271502\",\"billername\":\"\",\"agentname\":\"shipra Ag3\",\"destination\":\"237999999999\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"20.0\",\"confcode\":\"FCFL\",\"source\":\"237888888888\",\"tax\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"\",\"requestcts\":\"02/08/2018 09:36:17\",\"responsects\":\"02/08/2018 09:36:17\",\"transferno\":\"\",\"language\":\"EN\",\"agenttype\":\"\",\"showconfcode\":\"1\",\"comments\":\"Transaction Successful\",\"agentbranch\":\"EU BUEA I\"}";

                responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);

                /* txnType = responseModel.getUserDefinedString();

                switch (txnType) {
                    case "CASHIN":
                        responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);
                        break;

                    case "REMTSEND":
                        responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);
                        break;

                    case "REMTRECV":
                        responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);
                        break;

                    case "CASHTOM":
                        responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);
                        break;

                    case "CASHOUT":
                        responseModel = new DataParser().getTransactionApprovedViewResponse(serverResponse);
                        break;

                }*/

                break;
            case 129:  // Bill pay Bill
                responseModel = new DataParser().getBillPayBillResponse(serverResponse);
                break;
            case 130:  // Bill Pay Deposit Ptop
                responseModel = new DataParser().getBillpayDepositPtopResponse(serverResponse);
                break;
            case 131:  // Create Account

                //serverResponse="{\"commercialregisternumbervaliditydate\":\"\",\"dateoftakinglegalcapacityintoaccount\":\"\",\"accountname\":\"SHARIQUE \",\"numberofchildren\":\"\",\"socialidentitynumber\":\"\",\"profession\":\"\",\"sourcename\":\"SHARIQUE \",\"dateofbirth\":\"28-08-2018 23:59:59\",\"resultcode\":\"0\",\"city\":\"DLH\",\"maritalstatus\":\"\",\"residencearea\":\"\",\"birthdepartment\":\"\",\"maidenname\":\"\",\"gender\":\"\",\"transid\":\"7994008\",\"selfcarepwd\":\"GUGQUF4C\",\"idproofissuedate\":\"28-08-2018 00:00:00\",\"agentcode\":\"237000271501\",\"commercialregisternumber\":\"\",\"customerfirstname\":\"\",\"parent\":\"237000363000\",\"organisationissuingidproof\":\"\",\"legalform\":\"\",\"country\":\"Cameroon\",\"validitydatelicensenumber\":\"\",\"suboccupation\":\"\",\"resultdescription\":\"Transaction Successful\",\"birthplace\":\"yyffyyfyfhffhhf\",\"requestcts\":\"28/08/2018 18:55:20\",\"responsects\":\"28/08/2018 18:55:21\",\"branch\":\"237000246000\",\"language\":\"EN\",\"agenttype\":\"BANKAG\",\"patennumber\":\"\",\"clientfamilycode\":\"\",\"legalcapacity\":\"\",\"state\":\"AbangMinkoo\",\"familystatus\":\"\",\"idproofissueplace\":\"28-08-2018 00:00:00\",\"newpin\":\"1354\",\"title\":\"\",\"countryofbirth\":\"\",\"countryofresidence\":\"\",\"idproof\":\"123456\",\"walletbalance\":\"0.00\",\"fixphoneno\":\"237544558555\",\"acronym\":\"\",\"legalstatus\":\"\",\"chamberofcommerce\":\"\",\"agentname\":\"Test\",\"companyname\":\"\",\"nationalidentitynumber\":\"\",\"idprooftype\":\"PASSPORT\",\"nationality\":\"\",\"source\":\"237323556588\",\"clienttype\":\"GPRS\",\"address\":\"ydydgxfghffg\",\"dateoftakingintoaccountthelegalsituation\":\"\",\"taxidnumber\":\"\",\"secondmobphoneno\":\"\",\"companystartdate\":\"\",\"formationonspouse\":\"\",\"comments\":\"\",\"agentbranch\":\"EU BUEA I\",\"typeofcustomer\":\"\"}";

                responseModel = new DataParser().getCreateAccountResponse(serverResponse);
                break;

            case 132:  // Transaction Approved
                responseModel = new DataParser().getTransactionApprovedResponse(serverResponse);
                break;

            case 133:  // Biller Code 1
                responseModel = new DataParser().getBillerCodeResponse(serverResponse);
                break;

            case 134:  // Cash out withdrawal
                responseModel = new DataParser().getCashOutWithdrawalResponse(serverResponse);
                break;

            case 135:  // Biller Code
                responseModel = new DataParser().getBillerCodeBillPayResponse(serverResponse);
                break;

            case 136:  // Resend Code Conf Code

                /*   serverResponse="{\"transtype\":\"CASHOUT\",\"agentcode\":\"237000271503\",\"source\":\"237000271511\",\"otpcode\":\"545659\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"\",\"resultcode\":\"0\",\"otpflag\":\"\",\"transid\":\"10520615\"}";*/

                responseModel = new DataParser().getResendSmsConfCodeResponse(serverResponse);
                break;

            case 137:  // Resend Code Conf Code 2nd Phsase
                responseModel = new DataParser().getResendSmsConfCodeSecondPhaseResponse(serverResponse);
                break;

            case 138:  // Unblock Subscriber Conf Code
                responseModel = new DataParser().getUnblockSubscriberConfCodeResponse(serverResponse);
                break;

            case 139:  // Unblock Subscriber
                responseModel = new DataParser().getUnBlockSubscriberResponse(serverResponse);
                break;

            case 140: //

                /*  serverResponse="{\"agentcode\":\"237000271503\",\"accounts\":[{\"accountstatusdescription\":\"Active\",\"accountno\":\"237000271511\",\"accounttitle\":\"wdjkhdkj\",\"acctypedescription\":\"Mobile Account\",\"accounttype\":\"MA\",\"mobilebankingservice\":\"Y\"},{\"accountstatusdescription\":\"InActive\",\"accountno\":\"10F3147653701\",\"accounttitle\":\"abcdd\",\"acctypedescription\":\"Fixed Deposit Account\",\"accounttype\":\"FD\",\"mobilebankingservice\":\"N\"},{\"accountstatusdescription\":\"InActive\",\"accountno\":\"10903602101\",\"accounttitle\":\"ESTElSUB\",\"acctypedescription\":\"Current Account\",\"accounttype\":\"CA\",\"mobilebankingservice\":\"N\"},{\"accountstatusdescription\":\"InActive\",\"accountno\":\"10903602101\",\"accounttitle\":\"Ankit\",\"acctypedescription\":\"Saving Account\",\"accounttype\":\"SA\",\"mobilebankingservice\":\"N\"}],\"subname\":\"wdjkhdkj\",\"resultcode\":\"0\",\"mobileno\":\"237000271511\",\"title\":\"wdjkhdkj\",\"idproofissueplace\":\"\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"06/12/2018 11:25:06\",\"transid\":\"10520486\",\"idproof\":\"skdhcjskdhvjksd\",\"telephone\":\"237000271511\",\"phoneno\":null,\"idproofissuedate\":\"2017-01-12 00:00:00.0\"}";
                 */

                responseModel = new DataParser().getOtherDetailsResponse(serverResponse);
                break;


            case 141:  // Withdwaral Confcode

                /*serverResponse="{\"agentcode\":\"237000271501\",\"resultcode\":\"0\",\"agentname\":\"shipra Ag2\",\"fee\":\"\",\"vendorcode\":\"MICROEU\",\"amount\":\"1500\",\"confcode\":\"JYGB\",\"tax\":\"0.0\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"\",\"responsects\":\"13/11/2018 05:39:59\",\"accounttype\":\"MA\",\"transid\":\"10492154\",\"comments\":\"commentSms\",\"walletbalance\":\"9995842.0\"}";*/

                responseModel = new DataParser().withdrawalDetailsResponse(serverResponse);
                break;

            case 142:  // Withdwaral  2nd

                /*    serverResponse="{\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"\",\"prewalletbalance\":\"1.0001E7\",\"amount\":\"250.0\",\"responsValue\":\"0\",\"faxno\":\"\",\"accounttype\":\"MA\",\"idproof\":\"\",\"transid\":\"10492160\",\"destbranch\":\"EU BAFOUSSAM I\",\"walletbalance\":\"1.000125E7\",\"phoneno\":\"\",\"agentcode\":\"237000271501\",\"agentname\":\"shipra Ag2\",\"transcode\":\"2SPE\",\"destination\":\"237000271510\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"fee\":\"62.0\",\"idprooftype\":\"\",\"tax\":\"12.0\",\"source\":\"237000271501\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"shipra sub\",\"requestcts\":\"\",\"responsects\":\"13/11/2018 06:04:37\",\"language\":\"FR\",\"agenttype\":\"\",\"comments\":\"comments\",\"agentbranch\":\"EU BUEA I\"}";*/

                responseModel = new DataParser().getWithdrawalSecondPhaseResponse(serverResponse);
                break;

            case 143:  // resetMpin
                responseModel = new DataParser().getResetMpinConfcodeResponse(serverResponse);
                break;

            case 144:  // resetMpin confcode
                responseModel = new DataParser().getResetMpinResponse(serverResponse);
                break;

            case 145:  // other Account Activer  mobilebankingactivaion
                responseModel = new DataParser().getAccountActiverResponse(serverResponse);
                break;

            case 146:  //Pic Sign

                /*serverResponse="{\"idproofduedate\":\"31/12/2018 23:59:59\",\"profession\":\"\",\"state\":\"YDE\",\"resultcode\":\"0\",\"city\":\"YDE\",\"currency\":\"FCR-XAF\",\"idproofissueplace\":\"\",\"residencearea\":\"guugg\",\"gender\":\"Homme\",\"transid\":\"10519742\",\"idproof\":\"20070813\",\"fixphoneno\":\"237123456789\",\"idproofissuedate\":\"26/09/2016 00:00:00\",\"issamebranch\":\"true\",\"agentcode\":\"237000271503\",\"profilename\":\"PARTICULIER CLASIQUE\",\"image\":\"\",\"firstname\":\"nguemkam kokam ghislain\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"CAMEROUN\",\"idprooftype\":\"NIC\",\"source\":\"237696497480\",\"birthplace\":\"yyyy\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"email\":\"infos@expressunion.net\",\"address\":\"yes\",\"requestcts\":\"\",\"dob\":\"2018-11-28 00:00:00.0\",\"Sign\":\"\",\"responsects\":\"05/12/2018 06:20:46\",\"statename\":\"Yaounde\",\"agenttype\":\"SUB\",\"language\":\"French\",\"tla\":\"\",\"secondmobphoneno\":\"237123456789\",\"comments\":\"\"}";*/


                responseModel = new DataParser().getPictureSignResponse(serverResponse);
                break;

            case 147:  //Prints Receipt
                responseModel = new DataParser().getPrintResponse(serverResponse);
                break;

            case 148:  //Pic Sign

                /* serverResponse="{\"idproofduedate\":\"\",\"profession\":\"\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"ABAN\",\"currency\":\"FCR-XAF\",\"idproofissueplace\":\"\",\"residencearea\":\"rrrrrrrrrraaaaaaa\",\"gender\":\"\",\"transid\":\"10492155\",\"idproof\":\"789789999999\",\"fixphoneno\":\"\",\"idproofissuedate\":\"12/01/2017 00:00:00\",\"issamebranch\":\"false\",\"agentcode\":\"237000271501\",\"profilename\":\"Plan Agent\",\"image\":\"\",\"firstname\":\"shipra sub\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"\",\"idprooftype\":\"DRVLIC\",\"source\":\"237000271510\",\"birthplace\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"email\":\"shipra.singh@esteltelecom.com\",\"address\":\"cccccaaaaaaa\",\"requestcts\":\"\",\"dob\":\"2017-01-12 07:24:39.0\",\"Sign\":\"\",\"responsects\":\"13/11/2018 05:43:58\",\"statename\":\"AbangMinkoo\",\"agenttype\":\"SUB\",\"language\":\"English\",\"tla\":\"\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";*/


                responseModel = new DataParser().getPictureSignCahInReceiveMoneyResponse(serverResponse);
                break;

            case 149:  // prepaid electricity first phase
                responseModel = new DataParser().getprepaidElectricityResponse(serverResponse);
                break;


            case 150:  // prepaid electricity 2nd Phase
                responseModel = new DataParser().getprepaidElectricityBillpayResponse(serverResponse);
                break;


            case 151:  // Activer View Other Account

                /*  serverResponse="{\"agentcode\":\"237000271503\",\"accountno\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Invalid Account Number\",\"requestcts\":\"04/12/2018 07:01:54 AM\",\"responsects\":\"04/12/2018 07:01:54 AM\",\"resultcode\":\"26\",\"transid\":\"10519134\",\"mobileno\":\"237000271511\"}";*/

                responseModel = new DataParser().getActiverViwverResponse(serverResponse);
                break;

            case 152:  // print bill pay
                responseModel = new DataParser().getPrintBillPayResponse(serverResponse);
                break;

            case 153:  // print bill pay

              //  serverResponse="{\"transtype\":\"REMTRECV\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"YDE\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"237333333333\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"3962.655\",\"fromcity\":\"YDE\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"28/02/2020 07:40:21\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11098164\",\"comments\":\"\",\"walletbalance\":\"2849033.16\"}";

                responseModel = new DataParser().getTariffResponseSendMoneyToMobile(serverResponse);
                break;


            case 154:  // City
                responseModel = new DataParser().getCityResponse(serverResponse);
                break;

            case 155:  // getAgentIdentity create account/// send Money ERA Mobile National
                responseModel = new DataParser().getAgentIdentityResponse(serverResponse);
                break;

            case 156:  // Plan create account

                //serverResponse="{\"plans\":[{\"planname\":\"PLAN CLIENT RDC\",\"lavel\":\"CLIENT DE LA RDC\",\"agenttype\":\"SUB\",\"plancode\":\"RDCPLAN\"},{\"planname\":\"CLIENTEMIP\",\"lavel\":\"CLIENT EMI PREMIUIM\",\"agenttype\":\"SUB\",\"plancode\":\"CLIENTEMIP\"},{\"planname\":\"CLIENTFRPERIURBAIN\",\"lavel\":\"CLIENT FR PERIURBAIN\",\"agenttype\":\"SUB\",\"plancode\":\"CLIENTFRPE\"},{\"planname\":\"CLIENTFRRURAL\",\"lavel\":\"CLIENT FR RURAL\",\"agenttype\":\"SUB\",\"plancode\":\"CLIENTFRRU\"},{\"planname\":\"CLIENTFRURBAIN\",\"lavel\":\"CLIENT FR URBAIN\",\"agenttype\":\"SUB\",\"plancode\":\"CLIENTFRUR\"},{\"planname\":\"PLAN FOR TCHAD SUBS\",\"lavel\":\"CLIENTS TCHAD\",\"agenttype\":\"SUB\",\"plancode\":\"TCHADPLAN\"},{\"planname\":\"CONGOPLAN\",\"lavel\":\"CONGOPLAN\",\"agenttype\":\"SUB\",\"plancode\":\"CONGOPLAN\"},{\"planname\":\"SUPERAGENT\",\"lavel\":\"dsaddd\",\"agenttype\":\"SUB\",\"plancode\":\"SUPERAGENT\"},{\"planname\":\"PLAN ETUDIANT\",\"lavel\":\"ETUDIANT\",\"agenttype\":\"SUB\",\"plancode\":\"ETUDIANT\"},{\"planname\":\"GABONPLAN\",\"lavel\":\"GABONPLAN\",\"agenttype\":\"SUB\",\"plancode\":\"GABONPLAN\"},{\"planname\":\"nitinmodi\",\"lavel\":\"NM\",\"agenttype\":\"MER\",\"plancode\":\"NITIN\"},{\"planname\":\"NEWPLAN\",\"lavel\":\"NWPLAN\",\"agenttype\":\"SUB\",\"plancode\":\"NEWPLAN\"},{\"planname\":\"PARTENAIRE EUBILL CO\",\"lavel\":\"PARTENAIRE EUBILL COLLECTE\",\"agenttype\":\"MER\",\"plancode\":\"PAR BIL CO\"},{\"planname\":\"SUBSCRIBER\",\"lavel\":\"PARTICULIER CLASIQUE\",\"agenttype\":\"SUB\",\"plancode\":\"GOLDPLAN\"},{\"planname\":\"PERSONNEL EU\",\"lavel\":\"PERSONNEL EU\",\"agenttype\":\"SUB\",\"plancode\":\"PL EUPERSO\"},{\"planname\":\"Agent DRC du Group 1\",\"lavel\":\"PL BA DRC G1\",\"agenttype\":\"SUB\",\"plancode\":\"BADRCGI\"},{\"planname\":\"PL CDE\",\"lavel\":\"PL CDE\",\"agenttype\":\"MER\",\"plancode\":\"PL CDE\"},{\"planname\":\"PL FRAN\",\"lavel\":\"PL FRAN\",\"agenttype\":\"BANKAG\",\"plancode\":\"PL FRAN\"},{\"planname\":\"PL PACK\",\"lavel\":\"PL PACK\",\"agenttype\":\"MER\",\"plancode\":\"PL PACK\"},{\"planname\":\"PL PACK\",\"lavel\":\"PL PACK\",\"agenttype\":\"SUB\",\"plancode\":\"MICROEU\"},{\"planname\":\"Plan CLT PRI CAM\",\"lavel\":\"PL PM CAM\",\"agenttype\":\"SUB\",\"plancode\":\"PL PM CAM\"},{\"planname\":\"Plan Clt PRI GAB\",\"lavel\":\"PL PM GAB\",\"agenttype\":\"SUB\",\"plancode\":\"PL PM GAB\"},{\"planname\":\"PL TEST AGT FORMATIO\",\"lavel\":\"PL TEST AGT FORMATION\",\"agenttype\":\"SUB\",\"plancode\":\"PL TEST FO\"},{\"planname\":\"PLAN AGENT\",\"lavel\":\"Plan Agent\",\"agenttype\":\"SUB\",\"plancode\":\"PL AGT\"},{\"planname\":\"PLAN AGENT FRANCHISE\",\"lavel\":\"PLAN AGENT FRANCHISE\",\"agenttype\":\"BANKAG\",\"plancode\":\"FRANCHISE\"},{\"planname\":\"PLAN AIRTIME\",\"lavel\":\"PLAN AIRTIME\",\"agenttype\":\"MER\",\"plancode\":\"PL AIRTIME\"},{\"planname\":\"PLAN CAISSE FR\",\"lavel\":\"PLAN CAISSE FRANCHIS\",\"agenttype\":\"BANKAG\",\"plancode\":\"GUICHET CA\"},{\"planname\":\"PLAN CLIENT FORMATIO\",\"lavel\":\"PLAN CLIENT FORMATION\",\"agenttype\":\"SUB\",\"plancode\":\"PL CLT FOR\"},{\"planname\":\"PLAN FRANCHISE SP\",\"lavel\":\"PLAN FRANCHISE SPECIAL\",\"agenttype\":\"BANKAG\",\"plancode\":\"PL FR SP\"},{\"planname\":\"PLAN MAIRIE YD\",\"lavel\":\"PLAN MAIRIE YD\",\"agenttype\":\"MER\",\"plancode\":\"PL MAIRIEY\"},{\"planname\":\"plan merchant soya\",\"lavel\":\"plan merchant soya\",\"agenttype\":\"MER\",\"plancode\":\"PLAN SOYA\"},{\"planname\":\"PLAN ONLINE\",\"lavel\":\"PLAN ONLINE\",\"agenttype\":\"SUB\",\"plancode\":\"PL ONLINE\"},{\"planname\":\"PLAN SABC\",\"lavel\":\"PLAN PVL SABC\",\"agenttype\":\"MER\",\"plancode\":\"PL SABC\"},{\"planname\":\"PL TEST COM FR\",\"lavel\":\"PLAN TEST COMMISSION FR\",\"agenttype\":\"BANKAG\",\"plancode\":\"PL TEST FR\"},{\"planname\":\"MERCHANT\",\"lavel\":\"PLAN TEST MARCHAND\",\"agenttype\":\"MER\",\"plancode\":\"MERCHANT\"},{\"planname\":\"PLAN UDS\",\"lavel\":\"PLAN UDS\",\"agenttype\":\"MER\",\"plancode\":\"PL UDS\"},{\"planname\":\"PL PREMIUM CLASSIQUE\",\"lavel\":\"PREMIUM CLASSIQUE\",\"agenttype\":\"SUB\",\"plancode\":\"PREMIUM CL\"},{\"planname\":\"RCAPLAN\",\"lavel\":\"RCAPLAN\",\"agenttype\":\"SUB\",\"plancode\":\"RCAPLAN\"},{\"planname\":\"SALARIE CLASSIQUE\",\"lavel\":\"SALARIE CLASSIQUE\",\"agenttype\":\"SUB\",\"plancode\":\"SALARIE CL\"},{\"planname\":\"TEST PLAN FR COM\",\"lavel\":\"TEST PPLAN AVEC COMMISSION\",\"agenttype\":\"BANKAG\",\"plancode\":\"TES COM FR\"},{\"planname\":\"TESTPLAN\",\"lavel\":\"TESTPLAN\",\"agenttype\":\"SUB\",\"plancode\":\"TESTPLAN\"},{\"planname\":\"Transhis\",\"lavel\":\"Transhis\",\"agenttype\":\"SUB\",\"plancode\":\"TRANSHIS\"}],\"agentcode\":\"237000271503\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"01/08/2018 10:32:17\",\"resultcode\":\"0\",\"transid\":\"10413889\"}";

                responseModel = new DataParser().getPlanCreateAgentResponse(serverResponse);
                break;

            case 157:  // send Money Destination Name agnet identity
                responseModel = new DataParser().getAgentIdentityResponseDestinationName(serverResponse);
                break;

            case 158: // Biller INFO
                //serverResponse=      {"agentcode":"237000271510","billername":"ENEO","billercode":"","sourcename":"","resultcode":"0","sessionid":"66562102550011022940","destination":"237100004001","vendorcode":"MICROEU","amount":"100.0","fee":"100.0","invoiceno":"014266582700","responsValue":"Transaction Successful","source":"","resultdescription":"Transaction Successful","destinationname":"ENEO","requestcts":"16/06/2017 13:51:36","duedate":"","responsects":"16/06/2017 13:51:36","accounttype":"MA","transid":"9288859","comments":"NOSMS"}
                //  serverResponse="{\"meterid\":\"\",\"billercode\":\"FACTURE DE CONSOMMATION\",\"sourcename\":\"MBUEMBUE MAMA \",\"resultcode\":\"0\",\"amount\":\"2800.0\",\"responsValue\":\"Transaction Successful\",\"duedate\":\"12/31/2017\",\"accounttype\":\"MA\",\"transid\":\"9310497\",\"customername\":\"\",\"agentcode\":\"237785785785\",\"billername\":\"ENEO\",\"sessionid\":\"\",\"destination\":\"237100004001\",\"fee\":\"200.0\",\"vendorcode\":\"MICROEU\",\"invoiceno\":\"305790865\",\"source\":\"201341862\",\"customerid\":\"\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"03/08/2017 14:36:12\",\"destinationname\":\"ENEO\",\"responsects\":\"03/08/2017 14:36:12\",\"comments\":\"NOSMS\",\"agentbranch\":\"EU MEIDOUGOU\"}";
                responseModel = new DataParser().getBillerInfoResponse(serverResponse);
                break;

            case 159: //cash out Send Money Same country
                responseModel = new DataParser().getCastoCashSendMoneySameCountryResponse(serverResponse);
                break;

            case 160: //search cash to cash receive Money
                responseModel = new DataParser().getSearchCashToCashReceiveResponse(serverResponse);
                break;

            case 161: //search cash to cash receive Money


              //  serverResponse="{\"resultcode\":\"0\",\"resultdescription\":\"Trasaction Successful\",\"responsects\":\"\",\"destcountry\":\"Gabon\",\"destinationname\":\"Sharique\",\"destination\":\"anwar\",\"agentbranch\":\"abankmink\",\"country\":\"Cameroon\",\"referencenumber\":\"EUI673493672\"}";

                responseModel = new DataParser().getCashToCashReceiveResponse(serverResponse);
                break;

            case 162:   // Report Mini Statements
                responseModel = new DataParser().getCommissionPeriod(serverResponse);
                break;

            case 163:   // Report Mini Statements
                responseModel = new DataParser().getCurrrentDayCommision(serverResponse);
                break;

            case 164:
                responseModel = new DataParser().getuploadImageResponse(serverResponse);
                break;

            case 165:   // Report Mini Statements
                responseModel = new DataParser().getBlockSubscriberResponse(serverResponse);
                break;

            case 166:
                responseModel = new DataParser().getCashToCashTransferCancelWithoutFees(serverResponse);
                break;

            case 167:
                responseModel = new DataParser().getCashToCashTransferCancelWithFees(serverResponse);
                break;

            case 168:
                responseModel = new DataParser().getUploadImagePictureSignResponse(serverResponse);
                break;

            case 169:
                responseModel = new DataParser().serachImoneyDeposit(serverResponse);
                break;

            case 170:
                responseModel = new DataParser().getOtpVerify(serverResponse);
                break;

            case 171:

                // serverResponse="{\"transactions\":[{\"id\":\"10413615\",\"transdate\":\"Tue Jul 31 14:02:11 CEST 2018\",\"transactiontype\":\"Cash a Cash-Retrait / P012306180052/10413615\",\"transactionamount\":\"100.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"970.0\"},{\"id\":\"10413615\",\"transdate\":\"Tue Jul 31 14:02:11 CEST 2018\",\"transactiontype\":\"Commissions sur Cash a Cash- Retrait/10413615\",\"transactionamount\":\"100.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"970.0\"},{\"id\":\"10413611\",\"transdate\":\"Tue Jul 31 13:59:36 CEST 2018\",\"transactiontype\":\"Cash a Cash - envoi / P012306180052/10413611\",\"transactionamount\":\"100.0\",\"debit\":\"100.00\",\"credit\":\"100.00\",\"closingbalance\":\"870.0\"},{\"id\":\"10413559\",\"transdate\":\"Tue Jul 31 12:58:36 CEST 2018\",\"transactiontype\":\"Paiement Transfert /237000000112/10413559\",\"transactionamount\":\"50.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"950.0\"},{\"id\":\"10413556\",\"transdate\":\"Tue Jul 31 12:57:12 CEST 2018\",\"transactiontype\":\"Envoi D Argent / SourceNumber/10413556\",\"transactionamount\":\"100.0\",\"debit\":\"100.00\",\"credit\":\"100.00\",\"closingbalance\":\"900.0\"},{\"id\":\"10413522\",\"transdate\":\"Tue Jul 31 12:41:35 CEST 2018\",\"transactiontype\":\"Cash a Cash-Retrait / P012306180050/10413522\",\"transactionamount\":\"100.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"1.000001E8\"},{\"id\":\"10413522\",\"transdate\":\"Tue Jul 31 12:41:35 CEST 2018\",\"transactiontype\":\"Commissions sur Cash a Cash- Retrait/10413522\",\"transactionamount\":\"100.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"1.000001E8\"},{\"id\":\"10413499\",\"transdate\":\"Tue Jul 31 12:09:52 CEST 2018\",\"transactiontype\":\"Paiement Transfert /237000271503/10413499\",\"transactionamount\":\"50.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"1.0009944E8\"},{\"id\":\"10413497\",\"transdate\":\"Tue Jul 31 12:08:42 CEST 2018\",\"transactiontype\":\"Envoi D Argent / SourceNumber/10413497\",\"transactionamount\":\"100.0\",\"debit\":\"100.00\",\"credit\":\"100.00\",\"closingbalance\":\"1.0009939E8\"},{\"id\":\"10413480\",\"transdate\":\"Tue Jul 31 11:59:22 CEST 2018\",\"transactiontype\":\"Cash a Cash - envoi / P012306180051/10413480\",\"transactionamount\":\"100.0\",\"debit\":\"100.00\",\"credit\":\"100.00\",\"closingbalance\":\"1.0009949E8\"},{\"id\":\"10413469\",\"transdate\":\"Tue Jul 31 11:54:34 CEST 2018\",\"transactiontype\":\"Cash a Cash - envoi / P012306180050/10413469\",\"transactionamount\":\"100.0\",\"debit\":\"100.00\",\"credit\":\"100.00\",\"closingbalance\":\"1.0009962E8\"},{\"id\":\"10413463\",\"transdate\":\"Tue Jul 31 11:52:36 CEST 2018\",\"transactiontype\":\"Cash a Cash - envoi / P012306180049/10413463\",\"transactionamount\":\"100.0\",\"debit\":\"0.00\",\"credit\":\"0.00\",\"closingbalance\":\"9.979917E7\"}],\"agentcode\":\"237000271503\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"01/08/2018 09:38:48\",\"resultcode\":\"0\",\"transid\":\"10413872\"}";

                responseModel = new DataParser().getReportAgentBank(serverResponse);

                break;

            case 172:
                responseModel = new DataParser().getCashToCashTransferWithdrawal(serverResponse);
                break;

            case 173:
                responseModel = new DataParser().getCashToCashTransferDeposit(serverResponse);
                break;

            case 174: //search cash to cash receive Money
                responseModel = new DataParser().getSearchCashToCashReceiveResponseImoney(serverResponse);
                break;

            case 180: //search cash to cash receive Money New List 30 Jan 2018

                 serverResponse="{\"blockstatus\":\"\",\"nodemessage\":\"success\",\"codemandat\":\"\",\"feesupdate\":\"\",\"resultcode\":\"0\",\"responsevalue\":\"Transaction Successful\",\"currency\":\"\",\"amount\":\"\",\"nombenef\":\"\",\"transactionid\":\"\",\"transid\":\"10922599\",\"senderaccount\":\"\",\"nomexp\":\"\",\"agentcode\":\"237000271502\",\"status\":\"\",\"benefaccount\":\"\",\"idreq\":\"\",\"code\":\"0\",\"agentname\":\"shipra Ag3\",\"destination\":\"\",\"country\":\"\",\"lastupdatedate\":\"\",\"vendorcode\":\"MICROEU\",\"feesenv\":\"\",\"feesannul\":\"\",\"source\":\"\",\"resultdescription\":\"Transaction Successful\",\"destinationname\":\"\",\"destcountry\":\"\",\"records\":[],\"recordcount\":\"0\",\"requestid\":\"\",\"cancelstatus\":\"\"}";

                responseModel = new DataParser().getSearchCashToCashReceiveResponseNew(serverResponse);

                break;

            case 181: //getAgentListOrderDetails  Order Transfer
                responseModel = new DataParser().getAgentListOrderDetails(serverResponse);
                break;

            case 182: // getOrderTransferFinal OrderTransfer Final
                responseModel = new DataParser().getOrderTransferFinal(serverResponse);
                break;

            case 183: // getOrderTransferFinal listview Data  response
                responseModel = new DataParser().getOrderTransferDetailsApprovedResponse(serverResponse);
                break;

            case 184: // getOrderTransferFinal OrderTransfer Final
                responseModel = new DataParser().getOrderTransferApprovalResponse(serverResponse);
                break;

            case 185:  // commission For Transfer
                responseModel = new DataParser().getCommissionForTransfer(serverResponse);
                break;

            case 186:  // Upload Image one

              /* serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";
               */

                responseModel = new DataParser().getuploadImage_first(serverResponse);
                break;

            case 187:  // Upload Image

               /* serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";
*/
                responseModel = new DataParser().getuploadImage_second(serverResponse);
                break;

            case 188:  // Upload Image

              /*  serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";
*/
                responseModel = new DataParser().getuploadImage_third(serverResponse);
                break;

            case 189:  // Upload Image

               /* serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";
*/
                responseModel = new DataParser().getuploadImage_four(serverResponse);
                break;

            case 190:  // Upload Image   bill

                /*serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";*/


                responseModel = new DataParser().getuploadImage_five(serverResponse);
                break;

            case 191:  // Upload Image

               /* serverResponse="{\n" +
                        "  \"vendorcode\": \"MICR\",\n" +
                        "  \"agentcode\": \"237000271510\",\n" +
                        "  \"resultdescription\": \"Transaction successful\",\n" +
                        "  \"requestcts\": \"22/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"UPLOADIMAGE\",\n" +
                        "  \"responsects\": \"2018-08-24 14:22:34\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"1535100749\"\n" +
                        "}\n";*/


                responseModel = new DataParser().getuploadImage_six(serverResponse);
                break;

            case 192:  // master Data

                /*serverResponse="{\"agentplan\":\"PLAN AGENT\",\"agentcode\":\"237000271501\",\"pintype\":\"MPIN\",\"data\":\"ALL\",\"resultcode\":\"0\",\"profilecount\":\"3\",\"vendorcode\":\"MICROEU\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"23/10/2018 11:04:35\",\"branchcount\":\"19\",\"responsects\":\"23/10/2018 11:04:35\",\"idproofcount\":\"8\",\"profiles\":[{\"status\":\"SUCCESS\",\"profile\":[{\"agenttype\":\"SUB\",\"plancode\":\"PL AGT\",\"profilename\":\"Plan Agent\"},{\"agenttype\":\"SUB\",\"plancode\":\"PL KIOSQUE\",\"profilename\":\"PL KIOSQUE\"},{\"agenttype\":\"SUB\",\"plancode\":\"PREMIUM\",\"profilename\":\"CLIENT PREMIUM\"}]}],\"agenttype\":\"SUB\",\"transid\":\"10486826\",\"branches\":[{\"branch\":{\"code\":\"237000246000\",\"fixedBACode\":\"237000246100\",\"name\":\"EU DLA AKWA IDEAL\"}},{\"branch\":{\"code\":\"237000257000\",\"fixedBACode\":\"237000257100\",\"name\":\"EU YDE TSINGA ELOBI\"}},{\"branch\":{\"code\":\"237000270000\",\"fixedBACode\":\"237000270100\",\"name\":\"EU YDE SOA II\"}},{\"branch\":{\"code\":\"237000222000\",\"fixedBACode\":\"237000222100\",\"name\":\"EU YDE ETOUG EBE\"}},{\"branch\":{\"code\":\"237000172000\",\"fixedBACode\":\"237000172100\",\"name\":\"EU YDE MANGUIER\"}},{\"branch\":{\"code\":\"237000238000\",\"fixedBACode\":\"237000238100\",\"name\":\"EU YDE ELIG ESSONO\"}},{\"branch\":{\"code\":\"237000239000\",\"fixedBACode\":\"237000239100\",\"name\":\"EU YDE MVOG ADA\"}},{\"branch\":{\"code\":\"237000189000\",\"fixedBACode\":\"237000189100\",\"name\":\"EU YDE MBALLA II\"}},{\"branch\":{\"code\":\"237000234000\",\"fixedBACode\":\"237000234100\",\"name\":\"EU YDE ANGUISSA\"}},{\"branch\":{\"code\":\"237000219000\",\"fixedBACode\":\"237000219100\",\"name\":\"EU YDE BIYEM ASSI LYCEE\"}},{\"branch\":{\"code\":\"237000297000\",\"fixedBACode\":\"237000297100\",\"name\":\"EU DLA BONAMOUSSADI II\"}},{\"branch\":{\"code\":\"237000002000\",\"fixedBACode\":\"237000002100\",\"name\":\"EU DLA AKWA\"}},{\"branch\":{\"code\":\"237000001000\",\"fixedBACode\":\"237000001100\",\"name\":\"EU YDE WARDA\"}},{\"branch\":{\"code\":\"237000151000\",\"fixedBACode\":\"237000151100\",\"name\":\"EU YDE ETOA MEKI\"}},{\"branch\":{\"code\":\"237000005000\",\"fixedBACode\":\"237000005100\",\"name\":\"EU BERTOUA I\"}},{\"branch\":{\"code\":\"237000373000\",\"fixedBACode\":\"237000373100\",\"name\":\"EU EKOUNOU III\"}},{\"branch\":{\"code\":\"237000003000\",\"fixedBACode\":\"237000003100\",\"name\":\"EU BAFOUSSAM I\"}},{\"branch\":{\"code\":\"237000004000\",\"fixedBACode\":\"237000004100\",\"name\":\"EU BAMENDA I\"}},{\"branch\":{\"code\":\"237000542000\",\"fixedBACode\":\"237000542100\",\"name\":\"EU YDE RD PT NLONGKAK\"}}],\"idproofs\":[{\"idproof\":{\"code\":\"DRVLIC\",\"name\":\"Driving License\"}},{\"idproof\":{\"code\":\"VOTRID\",\"name\":\"Voter ID Card\"}},{\"idproof\":{\"code\":\"GOVTID\",\"name\":\"Government ID Card\"}},{\"idproof\":{\"code\":\"PASSPORT\",\"name\":\"Passport Details\"}},{\"idproof\":{\"code\":\"NIC\",\"name\":\"NIC\"}},{\"idproof\":{\"code\":\"RESCARD\",\"name\":\"Residence Card\"}},{\"idproof\":{\"code\":\"PROFCARD\",\"name\":\"Professional Card\"}},{\"idproof\":{\"code\":\"CNI\",\"name\":\"CNI\"}}]}";*/

                responseModel = new DataParser().getMasterDataResponse(serverResponse);


                break;


            case 193:  // agent Identity new

                responseModel = new DataParser().getAgentIdentityNewResponse(serverResponse);
                break;

            case 194:  // city list create agent

                //serverResponse="{\"agentcode\":\"237000271501\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"statelist\":[{\"statecode\":\"BAM\",\"statename\":\"Bamenda\"},{\"statecode\":\"DLA\",\"statename\":\"Douala\"},{\"statecode\":\"GAR\",\"statename\":\"Garoua\"},{\"statecode\":\"KOU\",\"statename\":\"Kousseri\"},{\"statecode\":\"YDE\",\"statename\":\"Yaounde\"},{\"statecode\":\"AMG\",\"statename\":\"AbongMbang\"},{\"statecode\":\"AKA\",\"statename\":\"Akonolinga\"},{\"statecode\":\"AMB\",\"statename\":\"Ambam\"},{\"statecode\":\"BFN\",\"statename\":\"Bafang\"},{\"statecode\":\"BFA\",\"statename\":\"Bafia\"},{\"statecode\":\"BFM\",\"statename\":\"Bafoussam\"},{\"statecode\":\"BFT\",\"statename\":\"Bafut\"},{\"statecode\":\"BGT\",\"statename\":\"Bagangte\"},{\"statecode\":\"BAL\",\"statename\":\"Bali\"},{\"statecode\":\"BJN\",\"statename\":\"Bandjoun\"},{\"statecode\":\"BYO\",\"statename\":\"Banyo\"},{\"statecode\":\"BTI\",\"statename\":\"Batouri\"},{\"statecode\":\"BLB\",\"statename\":\"Belabo\"},{\"statecode\":\"BUA\",\"statename\":\"Bertoua\"},{\"statecode\":\"BUE\",\"statename\":\"Buea\"},{\"statecode\":\"CPO\",\"statename\":\"Campo\"},{\"statecode\":\"DKO\",\"statename\":\"Dimako\"},{\"statecode\":\"DZU\",\"statename\":\"Dizangue\"},{\"statecode\":\"DJM\",\"statename\":\"Djoum\"},{\"statecode\":\"DNG\",\"statename\":\"Dschang\"},{\"statecode\":\"EWA\",\"statename\":\"Ebolowa\"},{\"statecode\":\"EEA\",\"statename\":\"Edea\"},{\"statecode\":\"FUN\",\"statename\":\"Foumban\"},{\"statecode\":\"GRA\",\"statename\":\"Goura\"},{\"statecode\":\"GER\",\"statename\":\"Guider\"},{\"statecode\":\"IAU\",\"statename\":\"Idenau\"},{\"statecode\":\"KLE\",\"statename\":\"Kaele\"},{\"statecode\":\"KBI\",\"statename\":\"Kribi\"},{\"statecode\":\"KBA\",\"statename\":\"Kumba\"},{\"statecode\":\"KBO\",\"statename\":\"Kumbo\"},{\"statecode\":\"LBE\",\"statename\":\"Limbe\"},{\"statecode\":\"LIE\",\"statename\":\"Lomie\"},{\"statecode\":\"LUM\",\"statename\":\"Loum\"},{\"statecode\":\"MFE\",\"statename\":\"Mamfe\"},{\"statecode\":\"MUA\",\"statename\":\"Maroua\"},{\"statecode\":\"MAP\",\"statename\":\"Martap\"},{\"statecode\":\"MYO\",\"statename\":\"mmbalmayo\"},{\"statecode\":\"MCK\",\"statename\":\"Mbandjock\"},{\"statecode\":\"MDA\",\"statename\":\"Mbouda\"},{\"statecode\":\"MGA\",\"statename\":\"Meiganga\"},{\"statecode\":\"MAM\",\"statename\":\"Minam\"},{\"statecode\":\"MLO\",\"statename\":\"Mokolo\"},{\"statecode\":\"MRA\",\"statename\":\"Mora\"},{\"statecode\":\"MOU\",\"statename\":\"Mouloundou\"},{\"statecode\":\"NRE\",\"statename\":\"Ngaoundere\"},{\"statecode\":\"NBA\",\"statename\":\"Nkongsamba\"},{\"statecode\":\"OLA\",\"statename\":\"Obala\"},{\"statecode\":\"SAA\",\"statename\":\"Saa\"},{\"statecode\":\"SMA\",\"statename\":\"Sangmelima\"},{\"statecode\":\"TTI\",\"statename\":\"Tibati\"},{\"statecode\":\"TKO\",\"statename\":\"Tiko\"},{\"statecode\":\"WUM\",\"statename\":\"Wum\"},{\"statecode\":\"YUA\",\"statename\":\"Yagoua\"},{\"statecode\":\"YMA\",\"statename\":\"Yokadouma\"},{\"statecode\":\"TBT\",\"statename\":\"Tibatian\"},{\"statecode\":\"ALM\",\"statename\":\"Afanloum\"},{\"statecode\":\"AKO\",\"statename\":\"Ako\"},{\"statecode\":\"AKN\",\"statename\":\"Akoeman\"},{\"statecode\":\"AKI\",\"statename\":\"AkomII\"},{\"statecode\":\"AKL\",\"statename\":\"Aklinga\"},{\"statecode\":\"AWY\",\"statename\":\"Akwaya\"},{\"statecode\":\"ALU\",\"statename\":\"Alou\"},{\"statecode\":\"ABM\",\"statename\":\"Ambamm\"},{\"statecode\":\"ADK\",\"statename\":\"Andek\"},{\"statecode\":\"AGS\",\"statename\":\"Angossas\"},{\"statecode\":\"ATK\",\"statename\":\"Atok\"},{\"statecode\":\"AWE\",\"statename\":\"Awae\"},{\"statecode\":\"AYS\",\"statename\":\"Ayos\"},{\"statecode\":\"BDJ\",\"statename\":\"Babadjou\"},{\"statecode\":\"BDS\",\"statename\":\"Babessi\"},{\"statecode\":\"BBT\",\"statename\":\"Babouantou\"},{\"statecode\":\"BFU\",\"statename\":\"Bafou\"},{\"statecode\":\"BHM\",\"statename\":\"Baham\"},{\"statecode\":\"BKB\",\"statename\":\"Balikumbat\"},{\"statecode\":\"BJU\",\"statename\":\"Bamendjou\"},{\"statecode\":\"MBS\",\"statename\":\"Bamuso\"},{\"statecode\":\"BAN\",\"statename\":\"Bana\"},{\"statecode\":\"BGG\",\"statename\":\"Bangangte\"},{\"statecode\":\"BGM\",\"statename\":\"Bangem\"},{\"statecode\":\"BGU\",\"statename\":\"Bangou\"},{\"statecode\":\"BGR\",\"statename\":\"Bangourain\"},{\"statecode\":\"BNK\",\"statename\":\"Banka\"},{\"statecode\":\"BKM\",\"statename\":\"Bankim\"},{\"statecode\":\"BAR\",\"statename\":\"Bare\"},{\"statecode\":\"BSH\",\"statename\":\"Basheo\"},{\"statecode\":\"BSM\",\"statename\":\"Bassamba\"},{\"statecode\":\"BTC\",\"statename\":\"Batcham\"},{\"statecode\":\"BTB\",\"statename\":\"Batibo\"},{\"statecode\":\"BTG\",\"statename\":\"Batchenga\"},{\"statecode\":\"BAT\",\"statename\":\"Batie\"},{\"statecode\":\"BTF\",\"statename\":\"Batoufam\"},{\"statecode\":\"BYG\",\"statename\":\"Bayangam\"},{\"statecode\":\"BZU\",\"statename\":\"Bazou\"},{\"statecode\":\"BEK\",\"statename\":\"Beka\"},{\"statecode\":\"BLL\",\"statename\":\"Belel\"},{\"statecode\":\"BLO\",\"statename\":\"Belo\"},{\"statecode\":\"BGS\",\"statename\":\"Bengbis\"},{\"statecode\":\"BBM\",\"statename\":\"Bibemi\"},{\"statecode\":\"BBY\",\"statename\":\"Bibey\"},{\"statecode\":\"BJK\",\"statename\":\"Bidjouka\"},{\"statecode\":\"BKK\",\"statename\":\"Bikok\"},{\"statecode\":\"BPD\",\"statename\":\"Bipindi\"},{\"statecode\":\"BWN\",\"statename\":\"BiwongBane\"},{\"statecode\":\"BWB\",\"statename\":\"BiwongBulu\"},{\"statecode\":\"BYH\",\"statename\":\"Biyouha\"},{\"statecode\":\"BNL\",\"statename\":\"Bonalea\"},{\"statecode\":\"BMK\",\"statename\":\"BotMakak\"},{\"statecode\":\"BRH\",\"statename\":\"Bourrha\"},{\"statecode\":\"BNJ\",\"statename\":\"Bandja\"},{\"statecode\":\"BKA\",\"statename\":\"Benakuma\"},{\"statecode\":\"BGA\",\"statename\":\"Blangoua\"},{\"statecode\":\"BKT\",\"statename\":\"Bokito\"},{\"statecode\":\"BDK\",\"statename\":\"Bondjock\"},{\"statecode\":\"DRK\",\"statename\":\"Darak\"},{\"statecode\":\"DGL\",\"statename\":\"Dargala\"},{\"statecode\":\"DHK\",\"statename\":\"Datcheka\"},{\"statecode\":\"DMB\",\"statename\":\"Dembo\"},{\"statecode\":\"DMG\",\"statename\":\"Demding\"},{\"statecode\":\"DUK\",\"statename\":\"Deuk\"},{\"statecode\":\"DBM\",\"statename\":\"Dibamba\"},{\"statecode\":\"DBG\",\"statename\":\"Dibang\"},{\"statecode\":\"DBB\",\"statename\":\"Dibombari\"},{\"statecode\":\"DKB\",\"statename\":\"DikomeBalue\"},{\"statecode\":\"DIR\",\"statename\":\"Dir\"},{\"statecode\":\"DJH\",\"statename\":\"Djohong\"},{\"statecode\":\"DMT\",\"statename\":\"Doumaintang\"},{\"statecode\":\"DUM\",\"statename\":\"Doume\"},{\"statecode\":\"DZG\",\"statename\":\"Dzeng\"},{\"statecode\":\"DUL\",\"statename\":\"Dziguilao\"},{\"statecode\":\"EBB\",\"statename\":\"Ebebda\"},{\"statecode\":\"EBN\",\"statename\":\"Ebone\"},{\"statecode\":\"EZD\",\"statename\":\"Edzendouan\"},{\"statecode\":\"EFL\",\"statename\":\"Efoulan\"},{\"statecode\":\"EDT\",\"statename\":\"EkondoTiti\"},{\"statecode\":\"EKO\",\"statename\":\"ElakOku\"},{\"statecode\":\"EMF\",\"statename\":\"EligMfomo\"},{\"statecode\":\"EDM\",\"statename\":\"Endom\"},{\"statecode\":\"ESS\",\"statename\":\"Esse\"},{\"statecode\":\"EDL\",\"statename\":\"Evodoula\"},{\"statecode\":\"EMJ\",\"statename\":\"Eyumodjock\"},{\"statecode\":\"FGL\",\"statename\":\"Figuil\"},{\"statecode\":\"FKU\",\"statename\":\"Fokoue\"},{\"statecode\":\"FJK\",\"statename\":\"Fondjomekwet\"},{\"statecode\":\"FFK\",\"statename\":\"Fonfuka\"},{\"statecode\":\"FGT\",\"statename\":\"FongoTongo\"},{\"statecode\":\"FTK\",\"statename\":\"Fotokol\"},{\"statecode\":\"FMT\",\"statename\":\"Foumbot\"},{\"statecode\":\"FDG\",\"statename\":\"Fundong\"},{\"statecode\":\"FRA\",\"statename\":\"FuruAwa\"},{\"statecode\":\"GLM\",\"statename\":\"Galim\"},{\"statecode\":\"GLT\",\"statename\":\"GalimTignere\"},{\"statecode\":\"GGB\",\"statename\":\"GariGombo\"},{\"statecode\":\"GBL\",\"statename\":\"GarouaBoulai\"},{\"statecode\":\"GSG\",\"statename\":\"Gashiga\"},{\"statecode\":\"GWZ\",\"statename\":\"Gawaza\"},{\"statecode\":\"GOB\",\"statename\":\"Gobo\"},{\"statecode\":\"GLF\",\"statename\":\"Goulfey\"},{\"statecode\":\"GEM\",\"statename\":\"Gueme\"},{\"statecode\":\"GDG\",\"statename\":\"Guidiguis\"},{\"statecode\":\"HAF\",\"statename\":\"HileAlifa\"},{\"statecode\":\"HNA\",\"statename\":\"Hina\"},{\"statecode\":\"IBT\",\"statename\":\"Idabato\"},{\"statecode\":\"IGL\",\"statename\":\"Isanguele\"},{\"statecode\":\"JKR\",\"statename\":\"Jakiri\"},{\"statecode\":\"KKI\",\"statename\":\"KaiKai\"},{\"statecode\":\"KKF\",\"statename\":\"Kalfou\"},{\"statecode\":\"KHY\",\"statename\":\"KayHay\"},{\"statecode\":\"KTZ\",\"statename\":\"Kentzou\"},{\"statecode\":\"KTT\",\"statename\":\"Kette\"},{\"statecode\":\"KDM\",\"statename\":\"Kobdombo\"},{\"statecode\":\"KFT\",\"statename\":\"Kolofata\"},{\"statecode\":\"KAD\",\"statename\":\"KomboAbedimo\"},{\"statecode\":\"KBL\",\"statename\":\"KomboIdinti\"},{\"statecode\":\"KBG\",\"statename\":\"KongsoBamougoum\"},{\"statecode\":\"KTC\",\"statename\":\"Kontcha\"},{\"statecode\":\"KYB\",\"statename\":\"KonYambetta\"},{\"statecode\":\"KNY\",\"statename\":\"Konye\"},{\"statecode\":\"KPT\",\"statename\":\"Kouoptamo\"},{\"statecode\":\"KTB\",\"statename\":\"Koutaba\"},{\"statecode\":\"KOZ\",\"statename\":\"Koza\"},{\"statecode\":\"LBL\",\"statename\":\"LafeBaleng\"},{\"statecode\":\"LGD\",\"statename\":\"Lagdo\"},{\"statecode\":\"LYZ\",\"statename\":\"LembeYezoum\"},{\"statecode\":\"LOB\",\"statename\":\"Lobo\"},{\"statecode\":\"LGB\",\"statename\":\"LogoneBirni\"},{\"statecode\":\"LKD\",\"statename\":\"Lokoundje\"},{\"statecode\":\"LLD\",\"statename\":\"Lolodorf\"},{\"statecode\":\"MAA\",\"statename\":\"Maan\"},{\"statecode\":\"MBG\",\"statename\":\"Mabanga\"},{\"statecode\":\"MAG\",\"statename\":\"Maga\"},{\"statecode\":\"MGB\",\"statename\":\"Magba\"},{\"statecode\":\"MKR\",\"statename\":\"Maikari\"},{\"statecode\":\"MKK\",\"statename\":\"Makak\"},{\"statecode\":\"MKN\",\"statename\":\"Makenene\"},{\"statecode\":\"MTN\",\"statename\":\"Malentouen\"},{\"statecode\":\"MDG\",\"statename\":\"Mandingring\"},{\"statecode\":\"MDJ\",\"statename\":\"Mandjou\"},{\"statecode\":\"MNJ\",\"statename\":\"Manjo\"},{\"statecode\":\"MNK\",\"statename\":\"Manoka\"},{\"statecode\":\"MSG\",\"statename\":\"Massangam\"},{\"statecode\":\"MSS\",\"statename\":\"Massock\"},{\"statecode\":\"MTM\",\"statename\":\"Matomb\"},{\"statecode\":\"MYB\",\"statename\":\"MayoBaleo\"},{\"statecode\":\"MYD\",\"statename\":\"MayoDarle\"},{\"statecode\":\"MYH\",\"statename\":\"MayoHourna\"},{\"statecode\":\"MBM\",\"statename\":\"Mbalmayo\"},{\"statecode\":\"MBN\",\"statename\":\"Mbanga\"},{\"statecode\":\"MGS\",\"statename\":\"Mbangassina\"},{\"statecode\":\"MKM\",\"statename\":\"Mbankomo\"},{\"statecode\":\"MBE\",\"statename\":\"Mbe\"},{\"statecode\":\"MBA\",\"statename\":\"Mboma\"},{\"statecode\":\"MNG\",\"statename\":\"Mbonge\"},{\"statecode\":\"MLN\",\"statename\":\"Melong\"},{\"statecode\":\"MGN\",\"statename\":\"Mengang\"},{\"statecode\":\"MGG\",\"statename\":\"Mengong\"},{\"statecode\":\"MGM\",\"statename\":\"Mengueme\"},{\"statecode\":\"MER\",\"statename\":\"Meri\"},{\"statecode\":\"MMN\",\"statename\":\"Messamena\"},{\"statecode\":\"MSK\",\"statename\":\"Messok\"},{\"statecode\":\"MMS\",\"statename\":\"Meyomessala\"},{\"statecode\":\"MYS\",\"statename\":\"Meyomessi\"},{\"statecode\":\"MFU\",\"statename\":\"Mfou\"},{\"statecode\":\"MDF\",\"statename\":\"Mindif\"},{\"statecode\":\"MDR\",\"statename\":\"Mindourou\"},{\"statecode\":\"MNT\",\"statename\":\"Minta\"},{\"statecode\":\"MSJ\",\"statename\":\"Misaje\"},{\"statecode\":\"MDZ\",\"statename\":\"Modzogo\"},{\"statecode\":\"MGD\",\"statename\":\"Mogode\"},{\"statecode\":\"MMB\",\"statename\":\"Mombo\"},{\"statecode\":\"MTL\",\"statename\":\"Monatele\"},{\"statecode\":\"MUK\",\"statename\":\"Mouanko\"},{\"statecode\":\"MLD\",\"statename\":\"Moulvoudaye\"},{\"statecode\":\"MTR\",\"statename\":\"Moutourwa\"},{\"statecode\":\"MDM\",\"statename\":\"Mundemba\"},{\"statecode\":\"MYK\",\"statename\":\"Muyuka\"},{\"statecode\":\"MGE\",\"statename\":\"NangaEboko\"},{\"statecode\":\"NDL\",\"statename\":\"Ndelele\"},{\"statecode\":\"NKM\",\"statename\":\"Ndikinimeki\"},{\"statecode\":\"NDB\",\"statename\":\"Ndobian\"},{\"statecode\":\"NDM\",\"statename\":\"Ndom\"},{\"statecode\":\"NDP\",\"statename\":\"Ndop\"},{\"statecode\":\"NKL\",\"statename\":\"Ndoukoula\"},{\"statecode\":\"NDU\",\"statename\":\"Ndu\"},{\"statecode\":\"NGB\",\"statename\":\"Ngambe\"},{\"statecode\":\"NGT\",\"statename\":\"NgambeTikar\"},{\"statecode\":\"NGH\",\"statename\":\"Nganha\"},{\"statecode\":\"NGU\",\"statename\":\"Ngaoui\"},{\"statecode\":\"NGM\",\"statename\":\"NgogMapubi\"},{\"statecode\":\"NGZ\",\"statename\":\"Ngomedzap\"},{\"statecode\":\"NGG\",\"statename\":\"Ngong\"},{\"statecode\":\"NGR\",\"statename\":\"Ngoro\"},{\"statecode\":\"NMK\",\"statename\":\"Ngoulemakong\"},{\"statecode\":\"NGY\",\"statename\":\"Ngoyla\"},{\"statecode\":\"NGLB\",\"statename\":\"Nguelebok\"},{\"statecode\":\"NLD\",\"statename\":\"Nguelemendouka\"},{\"statecode\":\"NBS\",\"statename\":\"NguiBassal\"},{\"statecode\":\"NGW\",\"statename\":\"Ngwei\"},{\"statecode\":\"NET\",\"statename\":\"Niete\"},{\"statecode\":\"NTK\",\"statename\":\"Nitoukou\"},{\"statecode\":\"NJK\",\"statename\":\"Njikwa\"},{\"statecode\":\"NMM\",\"statename\":\"Njimom\"},{\"statecode\":\"NMB\",\"statename\":\"Njombe\"},{\"statecode\":\"NKB\",\"statename\":\"Nkambe\"},{\"statecode\":\"NFM\",\"statename\":\"Nkolafamba\"},{\"statecode\":\"NMT\",\"statename\":\"Nkolmetet\"},{\"statecode\":\"NDJ\",\"statename\":\"Nkondjock\"},{\"statecode\":\"NKR\",\"statename\":\"Nkor\"},{\"statecode\":\"NTG\",\"statename\":\"Nkoteng\"},{\"statecode\":\"NSM\",\"statename\":\"Nsem\"},{\"statecode\":\"NTU\",\"statename\":\"Ntui\"},{\"statecode\":\"NWA\",\"statename\":\"Nwa\"},{\"statecode\":\"NYN\",\"statename\":\"Nyanon\"},{\"statecode\":\"OKL\",\"statename\":\"Okola\"},{\"statecode\":\"OKM\",\"statename\":\"Olamze\"},{\"statecode\":\"OGN\",\"statename\":\"Olanguina\"},{\"statecode\":\"OBS\",\"statename\":\"Ombessa\"},{\"statecode\":\"OUL\",\"statename\":\"Ouli\"},{\"statecode\":\"OVN\",\"statename\":\"Oveng\"},{\"statecode\":\"PNJ\",\"statename\":\"Penja\"},{\"statecode\":\"PKM\",\"statename\":\"PenkaMichel\"},{\"statecode\":\"PTT\",\"statename\":\"Pette\"},{\"statecode\":\"PTO\",\"statename\":\"Pitoa\"},{\"statecode\":\"POL\",\"statename\":\"Poli\"},{\"statecode\":\"POM\",\"statename\":\"Pouma\"},{\"statecode\":\"RUA\",\"statename\":\"Roua\"},{\"statecode\":\"SPB\",\"statename\":\"Salapoumbe\"},{\"statecode\":\"SNT\",\"statename\":\"Santa\"},{\"statecode\":\"STC\",\"statename\":\"Santchou\"},{\"statecode\":\"SOA\",\"statename\":\"Soa\"},{\"statecode\":\"SMM\",\"statename\":\"Somalomo\"},{\"statecode\":\"TTB\",\"statename\":\"TchatiBali\"},{\"statecode\":\"THL\",\"statename\":\"Tchollire\"},{\"statecode\":\"TGR\",\"statename\":\"Tignere\"},{\"statecode\":\"TIN\",\"statename\":\"Tinto\"},{\"statecode\":\"TOK\",\"statename\":\"Toko\"},{\"statecode\":\"TKM\",\"statename\":\"Tokombere\"},{\"statecode\":\"TMB\",\"statename\":\"Tombel\"},{\"statecode\":\"TON\",\"statename\":\"Tonga\"},{\"statecode\":\"TBR\",\"statename\":\"Touboro\"},{\"statecode\":\"TLU\",\"statename\":\"Touloum\"},{\"statecode\":\"TRU\",\"statename\":\"Touroua\"},{\"statecode\":\"TBH\",\"statename\":\"Tubah\"},{\"statecode\":\"WBN\",\"statename\":\"Wabane\"},{\"statecode\":\"WAS\",\"statename\":\"Wasa\"},{\"statecode\":\"WKB\",\"statename\":\"WidikumBoffe\"},{\"statecode\":\"WIN\",\"statename\":\"Wina\"},{\"statecode\":\"YAB\",\"statename\":\"Yabassi\"},{\"statecode\":\"YGU\",\"statename\":\"Yingui\"},{\"statecode\":\"ZHO\",\"statename\":\"Zhoa\"},{\"statecode\":\"ZIN\",\"statename\":\"Zina\"},{\"statecode\":\"ZTL\",\"statename\":\"Zoetele\"},{\"statecode\":\"MBOYL\",\"statename\":\"Mboumnyebel\"},{\"statecode\":\"ESK\",\"statename\":\"Eseka\"},{\"statecode\":\"KYE\",\"statename\":\"KyeOssi\"},{\"statecode\":\"SZA\",\"statename\":\"Souza\"},{\"statecode\":\"BALG\",\"statename\":\"Balesseng\"},{\"statecode\":\"BAG\",\"statename\":\"Baleveng\"},{\"statecode\":\"MIFM\",\"statename\":\"MenjiFontem\"},{\"statecode\":\"EKOK\",\"statename\":\"Ekok\"},{\"statecode\":\"MTGE\",\"statename\":\"Mutengene\"},{\"statecode\":\"NGDAL\",\"statename\":\"Ngaoundal\"},{\"statecode\":\"BAAE\",\"statename\":\"BankimAmchide\"},{\"statecode\":\"NGO\",\"statename\":\"Ngoumou\"},{\"statecode\":\"YOKO\",\"statename\":\"Yoko\"},{\"statecode\":\"NTY\",\"statename\":\"Nyete\"},{\"statecode\":\"BAMB\",\"statename\":\"Bambui\"},{\"statecode\":\"BMKA\",\"statename\":\"Bameka\"},{\"statecode\":\"BANG\",\"statename\":\"Bangang\"},{\"statecode\":\"MBEN\",\"statename\":\"Mbengwi\"},{\"statecode\":\"OKU\",\"statename\":\"Oku\"},{\"statecode\":\"BANS\",\"statename\":\"Bansoa\"},{\"statecode\":\"MBAIB\",\"statename\":\"Mbaiboum\"},{\"statecode\":\"BOGO\",\"statename\":\"Bogo\"},{\"statecode\":\"KEKEM\",\"statename\":\"Kekem\"},{\"statecode\":\"BEOYA\",\"statename\":\"BetareOya\"},{\"statecode\":\"MEDU\",\"statename\":\"Meidougou\"},{\"statecode\":\"BBL\",\"statename\":\"Bambili\"},{\"statecode\":\"MYLO\",\"statename\":\"MayoOulo\"},{\"statecode\":\"MLB\",\"statename\":\"Malarba\"},{\"statecode\":\"NDNG\",\"statename\":\"Ndoungue\"},{\"statecode\":\"NNKM\",\"statename\":\"Njinikom\"},{\"statecode\":\"SBNG\",\"statename\":\"Sabongari\"},{\"statecode\":\"ABAN\",\"statename\":\"AbangMinkoo\"},{\"statecode\":\"NGUT\",\"statename\":\"Nguti\"},{\"statecode\":\"TTM\",\"statename\":\"Tatum\"},{\"statecode\":\"KMB\",\"statename\":\"Kembong\"},{\"statecode\":\"CAM\",\"statename\":\"Cameroon\"}],\"responsects\":\"14/09/2018 20:40:40\",\"resultcode\":\"0\",\"transid\":\"7997866\"}";

                responseModel = new DataParser().getCityListCreateAgent(serverResponse);

                break;

            case 195:  // agent identity profile view new

                /* serverResponse="{\"profession\":\"Farmer\",\"state\":\"\",\"address1\":\"address delhi,residence delhi\",\"resultcode\":\"0\",\"dateofbirth\":\"2018-09-20 00:00:00.0\",\"responsevalue\":\"Transaction Successful\",\"city\":\"\",\"initiatorAgent\":\"237000271501\",\"idproofissueplace\":\"\",\"residencearea\":\"residence delhi\",\"gender\":\"M\",\"otp\":\"270061\",\"transid\":\"8000236\",\"idproof\":\"passport\",\"idproofissuedate\":\"2018-09-20 00:00:00.0\",\"fixphoneno\":\"237333333333\",\"agentcode\":\"237258025803\",\"agentname\":\"cus sharique\",\"country\":\"\",\"vendorcode\":\"MICROEU\",\"idprooftype\":\"PASSPORT\",\"nationality\":\"IND\",\"customerid\":\"cus id 12345\",\"address\":\"\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"birthplace\":\"birtplace delhi\",\"requestcts\":\"\",\"responsetype\":\"AGENTIDENTITY\",\"responsects\":\"20/09/2018 14:14:57\",\"language\":\"FR\",\"secondmobphoneno\":\"237222222222\",\"comments\":\"SMS\"}";*/


                responseModel = new DataParser().getAgentIdentityProfileViewNewResponse(serverResponse);

                break;

            case 196:

                /*  serverResponse = "{\"idproofduedate\":\"20/09/2018 00:00:00\",\"profession\":\"Farmer\",\"state\":\"ABAN\",\"resultcode\":\"0\",\"city\":\"ABAN\",\"currency\":\"FCR-XAF\",\"idproofissueplace\":\"\",\"residencearea\":\"residence delhi\",\"gender\":\"M\",\"transid\":\"8000244\",\"idproof\":\"passport\",\"fixphoneno\":\"237333333333\",\"idproofissuedate\":\"20/09/2018 00:00:00\",\"issamebranch\":\"false\",\"agentcode\":\"237000271501\",\"profilename\":\"SUPERAGENT\",\"image\":\"\",\"firstname\":\"cus sharique\",\"agentname\":\"\",\"destination\":\"\",\"country\":\"Cameroon\",\"vendorcode\":\"MICROEU\",\"nationality\":\"IND\",\"idprooftype\":\"PASSPORT\",\"source\":\"237258025803\",\"birthplace\":\"birtplace delhi\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"email\":\"sharique@gmail.com\",\"address\":\"address delhi\",\"requestcts\":\"\",\"dob\":\"2018-09-20 00:00:00.0\",\"Sign\":\"\",\"responsects\":\"20/09/2018 14:18:05\",\"statename\":\"AbangMinkoo\",\"language\":\"French\",\"tla\":\"\",\"secondmobphoneno\":\"237222222222\",\"comments\":\"\"}";
                 */

                responseModel = new DataParser().getprofileViewResponse(serverResponse);

                break;

            case 197:
                // serverResponse="{"profession":"Farmer","state":"","resultcode":"0","dateofbirth":"2018-09-14 00:00:00.0","responsevalue":"","city":"","initiatorAgent":"","idproofissueplace":"","residencearea":"","gender":"M","otp":"","transid":"7997865","idproof":"Drivingg licence","idproofissuedate":"2018-09-10 00:00:00.0","fixphoneno":"237971819","agentcode":"237202098058","agentname":"customer name annu","country":"","vendorcode":"MICROEU","idprooftype":"DRVLIC","nationality":"","customerid":"customer 1234","address":"","resultdescription":"Transaction Successful","clienttype":"GPRS","birthplace":"burth place delhi","requestcts":"","responsetype":"AGENTIDENTITY","responsects":"14/09/2018 20:39:42","language":"EN","secondmobphoneno":"237971819","comments":""}";

                responseModel = new DataParser().getAgentIdentityUpdateAccountNewResponse(serverResponse);
                break;

            case 198:
                responseModel = new DataParser().getResendOtpVerifyUpdateAccountResponse(serverResponse);
                break;

            case 199:

                /* serverResponse="{\"vendorcode\":\"MICR\",\"agentcode\":\"237533680882\",\"imagecount\":\"6\",\"resultdescription\":\"Transaction successful\",\"requestcts\":\"\",\"responsetype\":\"CHECKIMAGES\",\"responsects\":\"2018-09-21 17:23:44\",\"images\":\"pic|sign|idf|idb|form|bill|\",\"resultcode\":\"0\",\"transid\":\"1537530809\"}";*/

                responseModel = new DataParser().getCheckImageDownloadResponse(serverResponse);
                break;


            case 200:

                /*  serverResponse="{\"idfrontimage\":\"\",\"agentcode\":\"237583569242\",\"billimage\":\"\",\"resultcode\":\"0\",\"vendorcode\":\"MICR\",\"idbackimage\":\"\",\"resultdescription\":\"Transaction successful\",\"requestcts\":\"\",\"responsetype\":\"DOWNLOADIMAGE\",\"responsects\":\"2018-12-12 07:06:18\",\"agentimage\":\"\",\"formimage\":\"\",\"transid\":\"1544594460\",\"signature\":\"\"}";*/

                responseModel = new DataParser().getImageDownloadParsing(serverResponse);
                break;

            case 201:

                responseModel = new DataParser().updateAccount(serverResponse);
                break;

            case 202:


                responseModel = new DataParser().getImageDownloadParsing_updateAcoount(serverResponse);
                break;

            case 203:


                 /*serverResponse = "{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"source\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"destinationname\": \"\",\n" +
                        "  \"requestcts\": \"\",\n" +
                        "  \"responsetype\": \"AUTHORIZE\",\n" +
                        "  \"sourcename\": \"\",\n" +
                        "  \"responsects\": \"10/15/2018 11:33:47 AM\",\n" +
                        "  \"action\": \"\",\n" +
                        "  \"resultcode\": \"234\",\n" +
                        "  \"transid\": \"8012073\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";*/


               /* serverResponse = "{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"source\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Authorization, Already Initiated\",\n" +
                        "  \"destinationname\": \"\",\n" +
                        "  \"requestcts\": \"\",\n" +
                        "  \"responsetype\": \"AUTHORIZE\",\n" +
                        "  \"sourcename\": \"\",\n" +
                        "  \"responsects\": \"10/15/2018 11:33:47 AM\",\n" +
                        "  \"action\": \"\",\n" +
                        "  \"resultcode\": \"231\",\n" +
                        "  \"transid\": \"8012073\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";*/

                /*serverResponse = "{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"source\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Subscriber not Authorize\",\n" +
                        "  \"destinationname\": \"\",\n" +
                        "  \"requestcts\": \"\",\n" +
                        "  \"responsetype\": \"AUTHORIZE\",\n" +
                        "  \"sourcename\": \"\",\n" +
                        "  \"responsects\": \"10/15/2018 11:33:47 AM\",\n" +
                        "  \"action\": \"\",\n" +
                        "  \"resultcode\": \"231\",\n" +
                        "  \"transid\": \"8012073\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";*/


                responseModel = new DataParser().parsingAuthrize_initiate(serverResponse);
                break;

            case 204:

              /*  serverResponse = "{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"requestcts\": \"2/06/2018 09:27:21\",\n" +
                        "  \"responsetype\": \"AUTHORIZE\",\n" +
                        "  \"responsects\": \"22/06/2018 09:27:21\",\n" +
                        "  \"action\": \"SUCCESS FULL\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"transid\": \"10400363\",\n" +
                        "  \"destination\": \"237000272701\"\n" +
                        "}";*/


                // Subscriber not Authorize

              /* serverResponse="{\n" +
                       "  \"agentcode\": \"237000271501\",\n" +
                       "  \"source\": \"237000271501\",\n" +
                       "  \"resultdescription\": \"Subscriber not Authorize\",\n" +
                       "  \"destinationname\": \"\",\n" +
                       "  \"requestcts\": \"\",\n" +
                       "  \"responsetype\": \"AUTHORIZE\",\n" +
                       "  \"sourcename\": \"\",\n" +
                       "  \"responsects\": \"10/15/2018 11:43:57 AM\",\n" +
                       "  \"action\": \"\",\n" +
                       "  \"resultcode\": \"236\",\n" +
                       "  \"transid\": \"8012107\",\n" +
                       "  \"destination\": \"\"\n" +
                       "}";*/

                responseModel = new DataParser().parsingAuthrize_auth(serverResponse);
                break;


            case 205:

               /* serverResponse="{\n" +
                        "   \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\"\n" +
                        "}";
                */

                responseModel = new DataParser().otpGenerateResponse(serverResponse);
                break;

            // if image failed  create account

            case 206:
                responseModel = new DataParser().getuploadImage_profilePic(serverResponse);
                break;
            case 207:
                responseModel = new DataParser().getuploadImage_idFront(serverResponse);
                break;
            case 208:
                responseModel = new DataParser().getuploadImage_idBack(serverResponse);
                break;
            case 209:
                responseModel = new DataParser().getuploadImage_billHome(serverResponse);
                break;
            case 210:
                responseModel = new DataParser().getuploadImage_signature(serverResponse);
                break;
            case 211:
                responseModel = new DataParser().getuploadImage_form(serverResponse);
                break;

            case 212:

                /*  serverResponse="{\"vendorcode\":\"MICR\",\"agentcode\":\"237662555552\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsetype\":\"UPLOADIMAGE\",\"responsects\":\"2018-12-26 15:00:02\",\"resultcode\":\"0\",\"transid\":\"1545816601\"}";*/

                responseModel = new DataParser().getuploadImage_all(serverResponse);
                break;

            case 214:  // security Question   // login


                // first Time question list resposne
                // serverResponse="{\"vendorcode\":\"MICROEU\",\"agentcode\":\"237000271502\",\"source\":\"237000271502\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"22\\/10\\/2019 07:27:14\",\"responsects\":\"22\\/10\\/2019 07:27:14\",\"resultcode\":\"0\",\"language\":\"en\",\"transid\":\"11079076\",\"questionans\":[{\"questioncode\":\"Game\",\"answer\":\"****\",\"question\":\"What was Your Favorites Game?\"},{\"questioncode\":\"Teacher\",\"answer\":\"****\",\"question\":\"What was Your Favorites Teacher?\"},{\"questioncode\":\"cole\",\"answer\":\"****\",\"question\":\"What was Your First Company?\"},{\"questioncode\":\"Book\",\"answer\":\"****\",\"question\":\"What was Your Favorites Books?\"}]}";

                // after answer success
                //  serverResponse="{\"vendorcode\":\"MICROEU\",\"agentcode\":\"237000271502\",\"source\":\"237000271502\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"23\\/10\\/2019 08:04:58\",\"responsects\":\"23\\/10\\/2019 08:04:58\",\"resultcode\":\"0\",\"language\":\"en\",\"transid\":\"11079338\",\"questionans\":[{\"questioncode\":\"cole\",\"answer\":\"05089F9EB90ABEF0D7985E05A801BA67\",\"question\":\"What was Your First Company?\"}]}";

                responseModel = new DataParser().getSecurityQuestionResponse(serverResponse);
                break;

            case 215:

                /* serverResponse="{\n" +
                         "   \"resultdescription\": \"Transaction Successful\",\n" +
                         "  \"cashoutAmount\": \"1500\",\n" +
                         "  \"resultdescription\": \"Transaction Successful\"\n" +
                         "}";*/

                responseModel = new DataParser().pictureSignatureServerRequest_cashoutAmount_authorize(serverResponse);
                break;


            //  ################################################  EUI   ################################################
            // CR SEND MONEY RECEIVE MONEY  EUI
            //  ################################################  EUI   ################################################

            case 216:   // TAX ANDCOMMINT


                //  serverResponse = "{\"agentcode\":\"237000271502\",\"isfeeused\":\"1\",\"cashtellerreceiver\":\"0.0\",\"vat\":\"19.25\",\"destinationCurrency\":\"XAF\",\"resultcode\":\"0\",\"originatingcountry\":\"CM\",\"exchangerate\":\"2\",\"processdatetime\":\"\",\"originatingCurrency\":\"XAF\",\"vendorcode\":\"MICROEU\",\"message\":null,\"taxcharged\":\"700.0\",\"destinationcountry\":\"CM\",\"customercharge\":\"1500.0\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"25/07/2019 07:23:27\",\"localamount\":\"6000\",\"responsects\":\"25/07/2019 07:23:27\",\"payoutamount\":\"6000.00\",\"transid\":\"10930104\"}";

                responseModel = new DataParser().taxAndCommint(serverResponse);
                break;

            case 217:   // getCurrency


                //  serverResponse="{\"resultcode\":\"0\",\"countrycode\":\"CM\",\"resultdescription\":\"SUCCESS\",\"currencyArray\":[{\"currency\":\"XAF\"}],\"country\":\"CAMEROON\"}";

                responseModel = new DataParser().getCurrency(serverResponse);
                break;


            case 218:   // reason list

                //    serverResponse="{\"reasonArray\":[{\"REASONONE\":\"Sending / Receiving funds\",\"REASONTWO\":\"Envoi / Reception de fonds\",\"CODE\":\"9.9.9.9.9.9\"},{\"REASONONE\":\"Donations Provided / Received\",\"REASONTWO\":\"Dons fournis / Recus\",\"CODE\":\"4.2.2.6.0.2 \"},{\"REASONONE\":\"Sending workers' funds\",\"REASONTWO\":\"Envoi des fonds des travailleurs\",\"CODE\":\"4.2.1.1.0.0 \"},{\"REASONONE\":\"Mission expenses\",\"REASONTWO\":\"Frais de mission\",\"CODE\":\"2.5.1.0.0.1 \"},{\"REASONONE\":\"uition fees\",\"REASONTWO\":\"Frais de scolarite\",\"CODE\":\"2.5.2.2.0.0 T\"},{\"REASONONE\":\"Medical Travel Expenses\",\"REASONTWO\":\"Frais de voyages medicaux\",\"CODE\":\"2.5.2.1.0.0\"},{\"REASONONE\":\"Travel expenses\",\"REASONTWO\":\"Frais de voyages touristiques\",\"CODE\":\"2.5.2.3.0.0\"}],\"resultdescription\":\"SUCCESS\",\"resultcode\":\"0\"}";

                responseModel = new DataParser().getReasonList(serverResponse);
                break;

            case 219:   // getCurrency

                //   serverResponse="{\"resultdescription\":\"SUCCESS\",\"resultcode\":\"0\",\"currencyArray\":[{\"currency\":\"XAF\"}],\"country\":\"GABON\"}";

                responseModel = new DataParser().getCurrencyDestination(serverResponse);
                break;

            case 220:  // idenity Sender

                //    serverResponse="{\"profession\":\"\",\"state\":\"\",\"address1\":\"\",\"dateofbirth\":\"2017-05-02 12:03:47.0\",\"resultcode\":\"0\",\"responsevalue\":\"\",\"city\":\"\",\"kycsamebranch\":\"F\",\"initiatorAgent\":\"\",\"idproofissueplace\":\"\",\"residencearea\":\"\",\"gender\":\"F\",\"idproof\":\"65765765858\",\"transid\":\"8037211\",\"otp\":\"\",\"fixphoneno\":\"\",\"idproofissuedate\":\"2017-05-02 00:00:00.0\",\"agentcode\":\"237000271502\",\"agentname\":\"Test Cam Snder \",\"country\":\"\",\"vendorcode\":\"MICROEU\",\"nationality\":\"\",\"idprooftype\":\"DRVLIC\",\"customerid\":\"\",\"birthplace\":\"\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"address\":\"\",\"requestcts\":\"\",\"responsetype\":\"AGENTIDENTITY\",\"responsects\":\"17\\/05\\/2019 17:53:37\",\"language\":\"EN\",\"agenttype\":\"\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";

                responseModel = new DataParser().getAgentIdentity_sender(serverResponse);
                break;

            case 221:   // idenity receiver

                //  serverResponse="{\"idproofissueplace\":\" idproofissueplace Delhi\",\"idproofissuedate\":\"2017-05-02 00:00:00.0\",\"idproof\":\"id proof no 12345\",\"gender\":\"F\",\"profession\":\"Software Eng\",\"state\":\"\",\"address1\":\"address 11213\",\"resultcode\":\"0\",\"dateofbirth\":\"2017-05-02 12:03:47.0\",\"responsevalue\":\"\",\"city\":\"\",\"kycsamebranch\":\"F\",\"initiatorAgent\":\"\",\"residencearea\":\"\",\"otp\":\"\",\"transid\":\"8037105\",\"fixphoneno\":\"9717232323\",\"agentcode\":\"237000271502\",\"agentname\":\"Test Bheem Receiver\",\"country\":\"\",\"vendorcode\":\"MICROEU\",\"idprooftype\":\"DRVLIC\",\"nationality\":\"\",\"customerid\":\"\",\"address\":\"\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"birthplace\":\"\",\"requestcts\":\"\",\"responsetype\":\"AGENTIDENTITY\",\"responsects\":\"17/05/2019 14:37:17\",\"agenttype\":\"\",\"language\":\"EN\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";

                responseModel = new DataParser().getAgentIdentity_receiver(serverResponse);
                break;

            case 222:   // SEARCH


                //   serverResponse="{\"refrencenumber\":\"EUI673493672\",\"changerate\":\"0\",\"senderemail\":null,\"resultcode\":\"0\",\"beneficiarylastname\":\"koka\",\"senderphone\":\"237222222222\",\"senderdatebirth\":null,\"exchangerate\":\"\",\"senderidnumber\":null,\"sendertown\":\"city delhi\",\"destinationcountry\":\"CM\",\"beneficiaryfirstname\":\"kokam\",\"beneficiarydatebirth\":null,\"senderidtype\":null,\"destinationcurrency\":\"XAF\",\"sendercurrency\":\"XAF\",\"senderpostcode\":null,\"beneficiaryphone\":\"237333333333\",\"question\":\"School\",\"transid\":\"4311\",\"responsedescription\":\"Authorized\",\"senderiddeleveryplace\":null,\"trasactiontype\":\"\",\"agentcode\":\"237000271502\",\"senderiddeleverydate\":null,\"amountsent\":\"600\",\"status\":\"\",\"beneficiaryaddress\":null,\"answer\":\"estel\",\"beneficiarytown\":null,\"code\":\"0\",\"feettc\":\"\",\"motif\":\"Sending workers' funds\",\"sendercountry\":\"CM\",\"vendorcode\":\"\",\"message\":null,\"senderfirstname\":\"sharique\",\"beneficiarypostcode\":null,\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"2019-06-28 07:42:06\",\"devise\":null,\"amounttopay\":\"600\",\"beneficiaryemail\":null,\"senderaddress\":\"\",\"beneficiarycountry\":null,\"senderlastname\":\"anwar\"}";

                responseModel = new DataParser().get_searchInt(serverResponse);
                break;

            case 223:  // sendCashInt -- send money ---- final

                //  serverResponse="{\"destinationemail\":\"\",\"sourcefirstname\":\"Bheem test\",\"refrencenumber\":\"EUI135575241\",\"sendermobile\":\"\",\"countryofissue\":\"CM\",\"typetransaction\":\"0\",\"resultcode\":\"0\",\"sourcelastname\":\"bhim\",\"resident\":\"YES\",\"exchangerate\":\"6.0\",\"transactiondatetime\":\"07-06-2019 10:42:23\",\"agegender\":\"Mr\",\"destinationcountry\":\"CI\",\"destinationPhoneNumber\":\"225666666666\",\"placeofissue\":\"delhi\",\"destinationcurrency\":\"XOF\",\"destinationfirstname\":\"kokam\",\"sendercurrency\":\"XAF\",\"question\":\"cole\",\"transid\":\"10918755\",\"agentcode\":\"237000271503\",\"destinationlastname\":\"koka\",\"answer\":\"estel\",\"senderphonenumber\":\"237111111\",\"dataofissue\":\" 6-7-2019\",\"vendorcode\":\"MICROEU\",\"sendercountry\":\"\",\"source\":\"237222222222\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsetype\":\"SENDCASHINT\",\"localamount\":\"200\",\"responsects\":\"\",\"iddocumentnumber\":\"id123456\",\"payoutamount\":\"200.0\"}";

                responseModel = new DataParser().get_cashToCash(serverResponse);
                break;

            case 224:  // receiveCashInt --- Receive  money ---final

                //  serverResponse="{\"refrencenumber\":\"EUI689306547\",\"reason\":\"\",\"desttown\":\"\",\"vat\":\"\",\"pintype\":\"\",\"messagetype\":\"RECVCASHINT\",\"resultcode\":\"0\",\"responsevalue\":\"Transaction Successful\",\"amount\":\"250\",\"destinationcurrency\":\"XAF\",\"transid\":\"10922099\",\"destbranch\":\"\",\"walletbalance\":\"4995000.00\",\"reqcode\":\"\",\"agentcode\":\"237000271502\",\"agenttransid\":\"\",\"status\":\"\",\"additionaltax\":\"\",\"fees\":\"\",\"code\":\"0\",\"Transactionid\":\"\",\"agentname\":\"shipra Ag3\",\"SenderName\":\"\",\"resultDescription\":\"Transaction Successful\",\"vndordesc\":\"\",\"transcode\":\"\",\"country\":\"Cameroon\",\"destination\":\"237858685858\",\"confcode\":\"\",\"DestName\":\"test\",\"vendorcode\":\"MICROEU\",\"vendorresultcode\":\"\",\"SourceName\":\"\",\"source\":\"237222222222\",\"clienttype\":\"GPRS\",\"resultdescription\":\"\",\"requestcts\":\"25/06/2019 17:35:53\",\"responsects\":\"25/06/2019 17:35:53\",\"destcountry\":\"\",\"referencenumber\":\"\",\"comments\":\"\",\"agentbranch\":\"EU BUEA I\"}";


                responseModel = new DataParser().get_cashToReceive(serverResponse);
                break;

            case 225:  // question answer


                //   serverResponse="{\"vendorcode\":\"MICROEU\",\"agentcode\":\"237000271503\",\"source\":\"237000271503\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"requestcts\":\"20\\/05\\/2019 09:30:41\",\"responsects\":\"20\\/05\\/2019 09:30:41\",\"resultcode\":\"0\",\"language\":\"en\",\"transid\":\"10731696\",\"questionans\":[{\"answer\":\"05089F9EB90ABEF0D7985E05A801BA67\",\"questioncode\":\"cole\",\"question\":\"What was Your First Company?\"}]}";

                responseModel = new DataParser().getQuestionAnswer_recievCash(serverResponse);
                break;

            case 226:  // Re send OTp

                /* serverResponse="{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"requestcts\": \"13/09/2018 14:14:44\",\n" +
                        "  \"responsects\": \"13/09/2018 14:14:44\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"otpflag\": \"\",\n" +
                        "  \"transid\": \"7996946\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";
*/
                responseModel = new DataParser().getResendOtpResponse(serverResponse);
                break;

            case 227:  // Re send OTp

                /*serverResponse="{\n" +
                        "  \"agentcode\": \"237000271501\",\n" +
                        "  \"resultdescription\": \"Transaction Successful\",\n" +
                        "  \"requestcts\": \"13/09/2018 14:14:44\",\n" +
                        "  \"responsects\": \"13/09/2018 14:14:44\",\n" +
                        "  \"resultcode\": \"0\",\n" +
                        "  \"otpflag\": \"\",\n" +
                        "  \"transid\": \"7996946\",\n" +
                        "  \"destination\": \"\"\n" +
                        "}";*/

                responseModel = new DataParser().getResendOtpResponse(serverResponse);
                break;

            case 228: //country data List EUI


                //  serverResponse="{\"appversionDRCBA\":\"ANDROID,true,1,1.0,1.0,0,0\",\"agentcode\":\"237100001012\",\"appversion\":\"ANDROID,true,3,1.2,1.0,0,1-1.11\",\"appversionfr\":\"ANDROID,true,2,1.1,1.0,0,1-1.2\",\"resultcode\":\"0\",\"countrylist\":[{\"countrycode\":\"CM\",\"isocode\":\"237\",\"countryname\":\"Cameroon\",\"mobilelength\":\"9\",\"currency\":\"XAF\"},{\"countrycode\":\"GA\",\"isocode\":\"241\",\"countryname\":\"Gabon\",\"mobilelength\":\"8\",\"currency\":\"XAF\"},{\"countrycode\":\"CG\",\"isocode\":\"242\",\"countryname\":\"Congo\",\"mobilelength\":\"9\",\"currency\":\"XAF\"},{\"countrycode\":\"TD\",\"isocode\":\"235\",\"countryname\":\"Chad\",\"mobilelength\":\"8\",\"currency\":\"XAF\"},{\"countrycode\":\"CF\",\"isocode\":\"236\",\"countryname\":\"Central African Republic\",\"mobilelength\":\"8\",\"currency\":\"XAF\"},{\"countrycode\":\"BJ\",\"isocode\":\"229\",\"countryname\":\"BENIN\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"CI\",\"isocode\":\"225\",\"countryname\":\"COTE DIVOIRE\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"BF\",\"isocode\":\"226\",\"countryname\":\"BURKINA FASO\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"ML\",\"isocode\":\"223\",\"countryname\":\"MALI\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"NE\",\"isocode\":\"227\",\"countryname\":\"NIGER\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"TG\",\"isocode\":\"228\",\"countryname\":\"TOGO\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"SN\",\"isocode\":\"221\",\"countryname\":\"SENEGAL\",\"mobilelength\":\"9\",\"currency\":\"XOF\"},{\"countrycode\":\"GW\",\"isocode\":\"224\",\"countryname\":\"GUINEA CONAKRY\",\"mobilelength\":\"9\",\"currency\":\"GNF\"},{\"countrycode\":\"FR\",\"isocode\":\"33\",\"countryname\":\"FRANCE\",\"mobilelength\":\"9\",\"currency\":\"EUR\"},{\"countrycode\":\"CH\",\"isocode\":\"41\",\"countryname\":\"SUISSE\",\"mobilelength\":\"9\",\"currency\":\"CHF\"},{\"countrycode\":\"CA\",\"isocode\":\"1\",\"countryname\":\"CANADA\",\"mobilelength\":\"9\",\"currency\":\"CAD\"},{\"countrycode\":\"CD\",\"isocode\":\"243\",\"countryname\":\"Democratic Republic of Congo\",\"mobilelength\":\"9\",\"currency\":\"USD: XAF\"}],\"apversionmer\":\"ANDROID,true,1,1.0,1.0,0,0\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"25/05/2016 18:01:51\",\"apversionbrc\":\"ANDROID,true,1,1.1,1.0,0,0\",\"responsects\":\"30/05/2019 12:40:52\",\"apversionba\":\"ANDROID,true,1,1.0,1.0,0,0\",\"transid\":\"10916143\"}";

                //  serverResponse="{\"resultcode\":\"0\",\"resultdescription\":\"SUCCESS\",\"countrylist\":[{\"countrycode\":\"CM\",\"isocode\":\"237\",\"countryname\":\"CAMEROON\",\"mobilelength\":\"9\",\"currency\":\"XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"GA\",\"isocode\":\"241\",\"countryname\":\"GABON\",\"mobilelength\":\"8\",\"currency\":\"XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"TD\",\"isocode\":\"235\",\"countryname\":\"TCHAD\",\"mobilelength\":\"8\",\"currency\":\"XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CF\",\"isocode\":\"236\",\"countryname\":\"REPUBLIQUE CENTRAFRICAINE\",\"mobilelength\":\"8\",\"currency\":\"XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CG\",\"isocode\":\"242\",\"countryname\":\"CONGO\",\"mobilelength\":\"9\",\"currency\":\"XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CD\",\"isocode\":\"243\",\"countryname\":\"REPUBLIQUE DEMOCRATIQUE DU CONGO\",\"mobilelength\":\"9\",\"currency\":\"USD:XAF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"BJ\",\"isocode\":\"229\",\"countryname\":\"BENIN\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CI\",\"isocode\":\"225\",\"countryname\":\"COTE D'IVOIRE\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"BF\",\"isocode\":\"226\",\"countryname\":\"BUKINA FASO\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"ML\",\"isocode\":\"223\",\"countryname\":\"MALI\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"NE\",\"isocode\":\"227\",\"countryname\":\"NIGER\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"TG\",\"isocode\":\"228\",\"countryname\":\"TOGO\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"SN\",\"isocode\":\"221\",\"countryname\":\"SENEGAL\",\"mobilelength\":\"9\",\"currency\":\"XOF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"GW\",\"isocode\":\"224\",\"countryname\":\"GUINEE CONAKRY\",\"mobilelength\":\"9\",\"currency\":\"GNF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"FR\",\"isocode\":\"33\",\"countryname\":\"FRANCE\",\"mobilelength\":\"9\",\"currency\":\"EUR\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CH\",\"isocode\":\"1\",\"countryname\":\"SUISSE\",\"mobilelength\":\"9\",\"currency\":\"CHF\",\"euithreshold\":\"9000\"},{\"countrycode\":\"CA\",\"isocode\":\"237\",\"countryname\":\"CANADA\",\"mobilelength\":\"10\",\"currency\":\"CAD\",\"euithreshold\":\"9000\"}]}";

                responseModel = new DataParser().getCountryData_EUI(serverResponse);
                break;

            case 229: // id proof Type

                // set in IdprooftypeRequestUrl
                // java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.


                // english
                // serverResponse="[{\"code\":\"NIC\",\"libelle\":\"National Identity Card\"},{\"code\":\"PASSPORT\",\"libelle\":\"Passport\"},{\"code\":\"DRVLIC\",\"libelle\":\"Driver license\"},{\"code\":\"VOTRID\",\"libelle\":\"Voter Card\"}]";

                // french
                //  serverResponse="[{\"code\":\"NIC\",\"libelle\":\"Carte Nationale d'Identité\"},{\"code\":\"PASSPORT\",\"libelle\":\"Passeport\"},{\"code\":\"DRVLIC\",\"libelle\":\"Permi de Conduire\"},{\"code\":\"VOTRID\",\"libelle\":\"Carte d Electeur\"}]";

                responseModel = new DataParser().getIdproofType(serverResponse);
                break;

            //  ################################################  Tuition  Fees   ################################################
            //  Tuition  Fees
            //  ################################################  Tuition  Fees   ################################################

            case 230:

                //   serverResponse="{\"status\":200,\"result\":[{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37223A25\",\"school_name\":\"CES DE BAKUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"MBONGE\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177A17\",\"school_name\":\"C.C.A.S. DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA2\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175F49\",\"school_name\":\"ENIEG BILINGUE DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177G51\",\"school_name\":\"ENIET DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027H19\",\"school_name\":\"LYCEE BILINGUE DE BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"BALIKUMBAT\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177H19\",\"school_name\":\"LYCEE BILINGUE DE MAMBANDA-KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175H11\",\"school_name\":\"LYCEE BILINGUE DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175I13\",\"school_name\":\"LYCEE DE KUMBA-MBENG\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027I20\",\"school_name\":\"LYCEE DE BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"BALIKUMBAT\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177I20\",\"school_name\":\"LYCEE DE MALENDE-KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027L42\",\"school_name\":\"LYCEE TECHNIQUE \\/ GTHS BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"BALIKUMBAT\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177L52\",\"school_name\":\"LYCEE TECHNIQUE \\/ GTHS KANG BAROMBI KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177L53\",\"school_name\":\"LYCEE TECHNIQUE \\/ GTHS KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\"}]}";

                responseModel = new DataParser().getFindSchool(serverResponse);

                break;

            case 231:   // school  name

                //  serverResponse="{\"status\":200,\"result\":[{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175F49\",\"school_name\":\"ENIEG BILINGUE DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\"}]}";

                responseModel = new DataParser().getSchoolDetails(serverResponse);

                break;

            case 232:   // Find registration number

                //  serverResponse = "{\"status\":200,\"result\":[{\"student_regnumber\":\"102188510549\",\"student_name\":\"AKWA JOEL ABONGH\",\"student_birthdate\":\"2019-01-29\",\"student_gender\":\"ND\",\"student_class_id\":\"8\",\"student_class_name\":\"Form 1\",\"student_phone\":\"651123456\",\"student_email\":\"\",\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177A17\",\"school_name\":\"C.C.A.S. DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA2\",\"city\":\"ND\"}]}";

                responseModel = new DataParser().getStudentDetails(serverResponse);

                break;

            case 233:   //   Level

                //  serverResponse="{\"status\":200,\"result\":[{\"class_id\":\"21\",\"class_name\":\"1er cycle ESG\"},{\"class_id\":\"4\",\"class_name\":\"3eme\"},{\"class_id\":\"3\",\"class_name\":\"4eme\"},{\"class_id\":\"2\",\"class_name\":\"5eme\"},{\"class_id\":\"1\",\"class_name\":\"6eme\"},{\"class_id\":\"0\",\"class_name\":\"non defini\"}]}";

                responseModel = new DataParser().getLevelDetails(serverResponse);
                break;

            case 234:    // option

                //   serverResponse="{\"status\":200,\"result\":[{\"option_id\":\"10\",\"option_name\":\"Allemand\"},{\"option_id\":\"24\",\"option_name\":\"Allemand\"},{\"option_id\":\"9\",\"option_name\":\"Espagnol\"},{\"option_id\":\"23\",\"option_name\":\"Espagnol\"}]}";

                responseModel = new DataParser().getOptionDetails(serverResponse);
                break;

            case 235:    // Fees


                // One Fees

                //serverResponse ="{\"status\":200,\"result\":[{\"fee_id\":\"19\",\"fee_name\":\"FRAIS EXIGIBLES\",\"fee_amount\":\"7500\",\"partiality\":\"1\"}]}";

                // two fees

                //    serverResponse="{\"status\":200,\"result\":[{\"fee_id\":\"5\",\"fee_name\":\"FRAIS EXAMEN BEPC\",\"fee_amount\":\"3500\",\"partiality\":\"0\"},{\"fee_id\":\"4\",\"fee_name\":\"FRAIS EXIGIBLES\",\"fee_amount\":\"7500\",\"partiality\":\"1\"}]}";

                responseModel = new DataParser().getFeesDetails(serverResponse);
                break;


            case 236:   // tariff tuition fees


                //    serverResponse="{\"transtype\":\"FEEPAYMENT\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"YDE\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"237222222222\",\"fee\":\"5.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"2000\",\"fromcity\":\"YDE\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"04/07/2019 08:21:50\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"10925155\",\"comments\":\"\",\"walletbalance\":\"4949169\"}";

                responseModel = new DataParser().tariffTutionFees(serverResponse);
                break;

            case 237:   // mpin page Tuition fees final page // Cash to M

                //   serverResponse="{\"vendorCode\":\"MICROEU\",\"agentBranch\":\"EU BUEA I\",\"responsevalue\":\"Transaction Successful\",\"billerCode\":\"237100001080\",\"destinationName\":\"gxgxcgcgch\",\"expirydate\":\"05/08/2019 08:40:21\",\"billerName\":\"MINESEC\",\"amount\":\"11100.0\",\"sourceName\":\"AKWA JOEL ABONGH\",\"resultCode\":\"0\",\"cin\":\"\",\"transid\":\"10925476\",\"feesupportedby\":\"SUB\",\"udv1\":\"534774.00\",\"transcode\":\"Q6FG\",\"resultDescription\":\"Transaction Successful\",\"agentCode\":\"237000271502\",\"udv2\":\"\",\"fee\":\"5.0\",\"invoiceno\":\"0123445\",\"source\":\"237000271502\",\"pin\":\"\",\"requestcts\":\"05/07/2019 08:40:21\",\"responsects\":\"05/07/2019 08:40:21\",\"pinType\":\"\",\"labelName\":\"\",\"comments\":\"\"}";

                responseModel = new DataParser().getFeePaymentTransaction(serverResponse);
                break;

            case 238:   // resend otp new Receive Cash

                responseModel = new DataParser().resendOtpNew_cashtoCash(serverResponse);
                break;

            case 239:   //  otp verify new Receive Cash

              //  serverResponse="{\"resultcode\":\"0\",\"resultdescription\":\"Trasaction Successful\"}";

                responseModel = new DataParser().resendOtp_verify(serverResponse);

                break;

            case 240:  //


                //   serverResponse="{\"status\":200,\"result\":[{\"student_regnumber\":\"556316215631\",\"student_name\":\"KOENGNE MAKOUBO YVANNA ORCHELLE\",\"student_birthdate\":\"2018-12-04\",\"student_gender\":\"ND\",\"student_class_id\":\"2\",\"student_class_name\":\"5eme\",\"student_phone\":\"\",\"student_email\":\"\",\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"CE41357H04\",\"school_name\":\"LYCEE BILINGUE DE NKOL-ETON\",\"region_code\":\"CE\",\"region_name\":\"CENTRE\",\"division\":\"MFOUNDI\",\"subdivision\":\"YAOUNDE1\",\"city\":\"ND\"}]}";

                responseModel = new DataParser().getDetailSelectionStudentName(serverResponse);
                break;

            case 241:    // inner seacr by name  name List


                responseModel = new DataParser().getStudentDetails_list(serverResponse);
                break;


            case 242:

                //   serverResponse="{\"status\":200,\"result\":[{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37223A25\",\"school_name\":\"CES DE BAKUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"city\":\"ND\",\"subdivision\":\"MBONGE\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177A17\",\"school_name\":\"C.C.A.S. DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA2\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175F49\",\"school_name\":\"ENIEG BILINGUE DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177G51\",\"school_name\":\"ENIET DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"MBONGE\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027H19\",\"school_name\":\"LYCEE BILINGUE DE BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"BALIKUMBAT\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177H19\",\"school_name\":\"LYCEE BILINGUE DE MAMBANDA-KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175H11\",\"school_name\":\"LYCEE BILINGUE DE KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"MBONGE\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37175I13\",\"school_name\":\"LYCEE DE KUMBA-MBENG\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA1\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027I20\",\"school_name\":\"LYCEE DE BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"BALIKUMBAT\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177I20\",\"school_name\":\"LYCEE DE MALENDE-KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"KUMBA3\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"NW48027L42\",\"school_name\":\"LYCEE TECHNIQUE / GTHS BALIKUMBAT\",\"region_code\":\"NO\",\"region_name\":\"NORD OUEST\",\"division\":\"NGO\",\"subdivision\":\"MBONGE\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177L52\",\"school_name\":\"LYCEE TECHNIQUE / GTHS KANG BAROMBI KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"MBONGE\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"},{\"country_code\":\"237\",\"country_name\":\"CAMEROUN\",\"school_code\":\"SW37177L53\",\"school_name\":\"LYCEE TECHNIQUE / GTHS KUMBA\",\"region_code\":\"SO\",\"region_name\":\"SUD OUEST\",\"division\":\"MEME\",\"subdivision\":\"MBONGE\",\"city\":\"ND\",\"student_regnumber\":\"12345688\",\"student_name\":\"sharique\",\"student_birthdate\":\"2019-05-21\",\"student_gender\":\"M\",\"student_phone\":\"323232652\",\"student_email\":\"sharique@gmail.com\"}]}";

                responseModel = new DataParser().getFindSchool_byRegistrationNumber(serverResponse);
                break;

            case 243:

                //   serverResponse="{\"idproofissueplace\":\" idproofissueplace Delhi\",\"idproofissuedate\":\"2017-05-02 00:00:00.0\",\"idproof\":\"id proof no 12345\",\"gender\":\"F\",\"profession\":\"Software Eng\",\"state\":\"\",\"address1\":\"address 11213\",\"resultcode\":\"0\",\"dateofbirth\":\"2017-05-02 12:03:47.0\",\"responsevalue\":\"\",\"city\":\"\",\"kycsamebranch\":\"F\",\"initiatorAgent\":\"\",\"residencearea\":\"\",\"otp\":\"\",\"transid\":\"8037105\",\"fixphoneno\":\"9717232323\",\"agentcode\":\"237000271502\",\"agentname\":\"Test Bheem Receiver\",\"country\":\"\",\"vendorcode\":\"MICROEU\",\"idprooftype\":\"DRVLIC\",\"nationality\":\"\",\"customerid\":\"\",\"address\":\"\",\"resultdescription\":\"Transaction Successful\",\"clienttype\":\"GPRS\",\"birthplace\":\"\",\"requestcts\":\"\",\"responsetype\":\"AGENTIDENTITY\",\"responsects\":\"17/05/2019 14:37:17\",\"agenttype\":\"\",\"language\":\"EN\",\"secondmobphoneno\":\"\",\"comments\":\"\"}";

                responseModel = new DataParser().getAgentIdentity_receiver_tutionfees(serverResponse);
                break;


            //    ############################### Purchase Menu Airtime  6 Dec 2019 ###############################


            case 244:
                responseModel = new DataParser().get_airtime_Response(serverResponse);
                break;


            case 245:
                responseModel = new DataParser().get_electricityCompany_Response(serverResponse);
                break;

            case 246:
                responseModel = new DataParser().get_searchBillnumber_Response(serverResponse);
                break;

            case 247:

              //  serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";
                responseModel = new DataParser().get_checkBoxData_Response(serverResponse);
                break;


            case 248:
                responseModel = new DataParser().get_tvCompanyDistributor_Response(serverResponse);
                break;

            case 249:
              //  serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_subscriptionType_Response(serverResponse);
                break;

            case 250:
             //   serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_optionType_Response(serverResponse);
                break;


            case 251:
             //   serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_tvSubscriptionFinal_Response(serverResponse);
                break;

            case 252:
              //  serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_taxpayerType_Response(serverResponse);
                break;

            case 253:
               // serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_rechargeDetails_Response(serverResponse);
                break;

            case 254:
              //  serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_categoryType_Response(serverResponse);
                break;

            case 255:
               // serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_companyPartner_Response(serverResponse);
                break;

            case 256:
               // serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_cashDepositType_Response(serverResponse);
                break;


            case 257:
                responseModel = new DataParser().get_electricityCompany_recharge_Response(serverResponse);
                break;


            case 258:

               // serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_search_recharge_Response(serverResponse);
                break;

            case 259:

              //  serverResponse="{\"transtype\":\"CASHIN\",\"tariffs\":[],\"agentcode\":\"237000271502\",\"billercode\":\"\",\"tocity\":\"\",\"feesupportedby\":\"\",\"vat\":\"19.25\",\"resultcode\":\"0\",\"destination\":\"\",\"fee\":\"0.0\",\"vendorcode\":\"MICROEU\",\"amount\":\"\",\"fromcity\":\"ABAN\",\"clienttype\":\"GPRS\",\"resultdescription\":\"Transaction Successful\",\"requestcts\":\"\",\"responsects\":\"13/12/2019 09:02:33\",\"accounttype\":\"\",\"language\":\"EN\",\"transid\":\"11088386\",\"comments\":\"\",\"walletbalance\":\"2873625\"}";

                responseModel = new DataParser().get_electricCompany_Response(serverResponse);
                break;


            // #####################################################################################################


        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (parseCompletedNotifier == null) {
            parseCompletedNotifier = (ServerResponseParseCompletedNotifier) ctx;
        }
        parseCompletedNotifier.onParsingCompleted(responseModel, customResponseList, requestNo);
    }
}
