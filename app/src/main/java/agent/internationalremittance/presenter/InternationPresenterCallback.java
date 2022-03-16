package agent.internationalremittance.presenter;

import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;

public interface InternationPresenterCallback {

    void onViewCreated(View v);

    void onClick(View v, HashMap<String, String> enteredValues);

    void onItemSelected(AdapterView<?> parent, View view, int position, long id);

    void functionalitySelected(boolean isRemitSend);

    void onTextChanged(CharSequence s, int start, int before, int count);

}
