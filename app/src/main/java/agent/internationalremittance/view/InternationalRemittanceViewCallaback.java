package agent.internationalremittance.view;

import java.util.HashMap;

public interface InternationalRemittanceViewCallaback {



    void setValuesLayout(HashMap<String, String> valuesLayout, String[] iroArr, String[] countryNamesArr, String[] currencyArr, boolean isRemitSend);

   void updateAmountConverted(String message);

    void updateAmountHint(String message);
    void onValidationFailed(String message);
    void onValidationSuccess(HashMap<String, String> successValues);
    void showProgress(String message);
}
