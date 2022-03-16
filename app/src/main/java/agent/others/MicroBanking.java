/*
package shariq.eu_agent_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

*/
/**
 * Created by shariq on 07-Mar-17.
 *//*


public class MicroBanking extends AppCompatActivity implements View.OnClickListener{
    Button accountBalance,customerService,createAccount,cashIn,cashOutMenu,remmittanceMenu,billPayMenu,transactionApproval,transactionCancel,tariff,reports,prints;

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.microbanking);

        accountBalance=(Button)findViewById(R.id.accountBalance);
        accountBalance.setOnClickListener(this);

        customerService=(Button)findViewById(R.id.customerService);
        customerService.setOnClickListener(this);

        cashIn=(Button)findViewById(R.id.cashIn);
        cashIn.setOnClickListener(this);

        createAccount=(Button)findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);

        cashOutMenu=(Button)findViewById(R.id.cashOutMenu);
        cashOutMenu.setOnClickListener(this);

        remmittanceMenu=(Button)findViewById(R.id.remmittanceMenu);
        remmittanceMenu.setOnClickListener(this);

        billPayMenu=(Button)findViewById(R.id.billPayMenu);
        billPayMenu.setOnClickListener(this);


        transactionApproval=(Button)findViewById(R.id.transactionApproval);
        transactionApproval.setOnClickListener(this);

        transactionCancel=(Button)findViewById(R.id.transactionCancel);
        transactionCancel.setOnClickListener(this);

        tariff=(Button)findViewById(R.id.tariff);
        tariff.setOnClickListener(this);

        reports=(Button)findViewById(R.id.reports);
        reports.setOnClickListener(this);

        prints=(Button)findViewById(R.id.prints);
        prints.setOnClickListener(this);

      }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.accountBalance:

                 intent=new Intent(MicroBanking.this,AccountBalance.class);
                startActivity(intent);
               // MicroBanking.this.finish();
                break;

            case R.id.customerService:

                 intent=new Intent(MicroBanking.this,CustomerService.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;

            case R.id.cashIn:

                 intent=new Intent(MicroBanking.this,CashIn.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;

            case R.id.createAccount:

                 intent=new Intent(MicroBanking.this,CreateAccount.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.cashOutMenu:

                intent=new Intent(MicroBanking.this,CashOutMenu.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.remmittanceMenu:

                Intent intentCashin=new Intent(MicroBanking.this,RemmettanceMenu.class);
                startActivity(intentCashin);
                // MicroBanking.this.finish();
                break;
            case R.id.billPayMenu:

                 intent=new Intent(MicroBanking.this,BillPayMenu.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.transactionApproval:

                intent=new Intent(MicroBanking.this,TransactionApproval.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.transactionCancel:

                intent=new Intent(MicroBanking.this,TransactionCancel.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.tariff:

                 intent=new Intent(MicroBanking.this,Tariff.class);
                 startActivity(intent);
                // MicroBanking.this.finish();
                break;

            case R.id.reports:

                intent=new Intent(MicroBanking.this,ReportTransationHistory.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;
            case R.id.prints:

                intent=new Intent(MicroBanking.this,Prints.class);
                startActivity(intent);
                // MicroBanking.this.finish();
                break;

        }
    }
}


*/
