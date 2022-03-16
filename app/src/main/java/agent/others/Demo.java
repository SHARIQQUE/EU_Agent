/*
package shariq.eu_agent_new;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

*/
/**
 * Created by Shariq on 01-06-2017.
 *//*


public class Demo extends AppCompatActivity {

    TableLayout tableLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        tableLayout = (TableLayout)findViewById(R.id.main_table);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);        // part1
        tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

        TextView label_hello = new TextView(this);
        label_hello.setId(20);
        label_hello.setText("HELLO");
        label_hello.setTextColor(Color.WHITE);          // part2
        label_hello.setPadding(5, 5, 5, 5);
        tr_head.addView(label_hello);// add the column to the table row here

        TextView label_android = new TextView(this);    // part3
        label_android.setId(21);// define id that must be unique
        label_android.setText("ANDROID..!!"); // set the text for the header
        label_android.setTextColor(Color.WHITE); // set the color
        label_android.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_android);

        tableLayout.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));
    }
}
*/
