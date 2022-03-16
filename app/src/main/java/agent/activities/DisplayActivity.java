package agent.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Locale;

import adapter.AdapterGridView;
import agent.cashin.CashInMenu;
import agent.cashout.CashOutMenuNew;
import agent.commision_pullback.CommissionPullbackMenu;
import agent.create_account.AccountsMenu;
import agent.other_services.OtherServiceNew;
import agent.sendmoney_receivemoney.ReceiveMoneyMenu;
import agent.eui.SendMoneyCashtoMobileCashtoCashMenu;
import commonutilities.ComponentMd5SharedPre;


public class DisplayActivity extends AppCompatActivity {
    // public static String [] prgmNameList={"Money Transfer","Bill Payment","Purchase Goods and Services","Request for Payment Transfer","Authorisation","Cash Withdrwal & Deposit","Account Mangement"};
    public static int[] prgmImages =
            {
                    R.drawable.new_balance,
                    R.drawable.new_cashin,
                    R.drawable.new_cashout,
                    R.drawable.new_sendmoney,  //  3 send money
                    R.drawable.new_receivemoney,  // 4 receive
                    R.drawable.billpay, // 5 bill pay
                    R.drawable.new_accounts, // 6 account menu
                    R.drawable.commision,    // 7 commision
                    R.drawable.customerservice,   // 8


            };
    GridView gridview;
    Context context;

    Intent intent;


    ComponentMd5SharedPre mComponentInfo;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponentInfo = (ComponentMd5SharedPre) getApplicationContext();
        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.display_activity);
        gridview = (GridView) findViewById(R.id.gridview);


        String accountbalance = (getString(R.string.accountBalanceNew));   // 0
        String cashIn = (getString(R.string.cashInNewDisplaypage));        // 1
        String CashOut = (getString(R.string.cashOutNew));                 // 2
        String sendMoney = (getString(R.string.sendMoneyNew));             // 3
        String receiveMoney = (getString(R.string.remmitance_receive_money_menu));      // 4
        String purchaseBilpayments_capital = (getString(R.string.purchaseBilpayments_cap));     // 5
        String createAccount = (getString(R.string.accounts_capital));                               // 6

      //  String remmitence = (getString(R.string.moneyTransferDisplayActivity));

        String commisionForTransfer = (getString(R.string.commission_pullback_cap));     //  7

        String otherServices = (getString(R.string.otherservice_cap));                     //  8

     //   String cashToCashImoney = (getString(R.string.cashtocashImoney_capital));                //   9
     //   String orderTransfer = (getString(R.string.orderTransfer_capital));                      //  10
     //   String orderTransferApproval = (getString(R.string.orderTransferApproval_capital));     //   11


        String[] prgmNameList = {accountbalance, cashIn, CashOut,
                sendMoney, receiveMoney, purchaseBilpayments_capital,createAccount,commisionForTransfer,otherServices,};


        gridview.setAdapter(new AdapterGridView(this, prgmNameList, prgmImages));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                switch (position) {

                    case 0:
                        intent = new Intent(DisplayActivity.this, AccountBalanceSpinner.class);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(DisplayActivity.this, CashInMenu.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(DisplayActivity.this, CashOutMenuNew.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(DisplayActivity.this, SendMoneyCashtoMobileCashtoCashMenu.class);
                        startActivity(intent);
                        break;

                    case 4:

                        intent = new Intent(DisplayActivity.this, ReceiveMoneyMenu.class);
                        startActivity(intent);


                       /* int  activityFinish=  mComponentInfo.getFinishActivity_receiveCash();


                        if(activityFinish==1)
                        {
                            Intent intent1= new Intent(DisplayActivity.this, ReceiveMoneyMenu.class);
                            startActivity(intent1);
                            DisplayActivity.this.finish();
                        }
                        else {

                            intent = new Intent(DisplayActivity.this, ReceiveMoneyMenu.class);
                            startActivity(intent);

                        }
*/


                        break;

                    case 5:
                        intent = new Intent(DisplayActivity.this, BillpayMenuNew.class);
                        startActivity(intent);
                        break;

                    case 6:
                        intent = new Intent(DisplayActivity.this, AccountsMenu.class);
                        startActivity(intent);
                        break;

                    case 7:
                        intent = new Intent(DisplayActivity.this, CommissionPullbackMenu.class);
                        startActivity(intent);
                        break;


                    case 8:
                        intent = new Intent(DisplayActivity.this, OtherServiceNew.class);
                        startActivity(intent);
                        break;


                      //  discussed

                 /*   case 9:
                        intent = new Intent(DisplayActivity.this, CashToCashImoneyMenu.class);
                        startActivity(intent);
                        break;

                    case 10:
                        intent = new Intent(DisplayActivity.this, OrderTransfer.class);
                        startActivity(intent);
                        break;

                    case 11:

                        intent = new Intent(DisplayActivity.this, OrderTransferApproval.class);
                        startActivity(intent);

                        break;*/


                }
                return;

            }
        });
    }

    @Override
    public void onResume() {


        super.onResume();
    }


}