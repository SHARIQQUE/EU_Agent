package agent.purchase_billpayment_deposit_menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import agent.activities.R;

public class LayoutDesignTemp extends Activity {

    ConstraintLayout constraintLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_design_temp);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                constraintLayout =(ConstraintLayout)findViewById(R.id.root_Layout);  // Root Layout ID

               // snackbar.make(constraintLayout, "Simple Snackbar......", Snackbar.LENGTH_LONG).show();   // Simple Snackbar

                snackbar.make(constraintLayout, "Snackbar...... ", Snackbar.LENGTH_LONG)  // snackbar click Listener
                        .setAction("action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LayoutDesignTemp.this,"Snackbar Click Listener",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }
}