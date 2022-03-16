package commonutilities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import callback.DateSetNotifier;


/**
 * Created by AdityaBugalia on 04/09/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month=++month;
        String m,d,y;
        m=""+month;
        d=""+day;
        y=""+year;

        if(m.length()==1){
            m="0"+m;
        }
        if (d.length()==1){
            d="0"+d;
        }

        ((DateSetNotifier)getActivity()).onDateSet(view,y,m,d);
    }
}