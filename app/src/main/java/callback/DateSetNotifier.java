package callback;

import android.widget.DatePicker;

/**
 * Created by sharique on 07/03/17.
 */
public interface DateSetNotifier {


    void onDateSet(DatePicker var1, String year, String month, String day);
}
