package agent.eui.receivemoney_cashtocash_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import agent.activities.R;
import callback.ServerResponseParseCompletedNotifier;
import commonutilities.ComponentMd5SharedPre;
import model.GeneralResponseModel;
import agent.thread.DataParserThread;

import static android.content.Context.MODE_PRIVATE;

public class QuestionAnswerReceiveMoneyFragment extends Fragment implements
        View.OnClickListener, ServerResponseParseCompletedNotifier, TextView.OnEditorActionListener {


    View view;
    TextView question_textview;
    ModalReceiveMoney modalReceiveMoney = new ModalReceiveMoney();
    Button nextButton_question,nextButton_answer;
    String[] countryCodeArray;
    Toolbar mToolbar;
    TextView answerTextview;
    ComponentMd5SharedPre mComponentInfo;
    String emailString, idDocumentTypeSpinnerString, testQuestionString, firstNameString, nameString, agentName, agentCode, answerString, countrySelectionString = "";
    boolean isServerOperationInProcess;
    private ScrollView scrollview_question_page,scrollview_answer_page;
    private String[] countryArray, countryPrefixArray, countryMobileNoLengthArray, payerBankAccountsArray, payerAccountCodeArray;
    private ProgressDialog mDialog;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Log.e("", "" + message.obj.toString());
            if (message.obj.toString().equals("Internet")) {
                hideProgressDialog();
                return;
            } else if (message.obj.toString().equals("")) {
                hideProgressDialog();
            } else {
                DataParserThread thread = new DataParserThread(getActivity(), mComponentInfo, QuestionAnswerReceiveMoneyFragment.this, message.arg1, message.obj.toString());
                thread.execute();
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mComponentInfo = (ComponentMd5SharedPre) getActivity().getApplicationContext();

        String languageToUse = mComponentInfo.getmSharedPreferences().getString("languageToUse", "");
        if (languageToUse.trim().length() == 0) {
            languageToUse = "fr";
        }
        Locale locale = new Locale(languageToUse);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());


        view = inflater.inflate(R.layout.receiovemoney_cashtocash_questionanswar_fragment, container, false); // Inflate the layout for this fragment


        agentName = mComponentInfo.getmSharedPreferences().getString("agentName", "");
        agentCode = mComponentInfo.getmSharedPreferences().getString("agentCode", "");
        //   countrySelectionString = mComponentInfo.getmSharedPreferences().getString("country", "");
        // currencySenderSelectionString = mComponentInfo.getmSharedPreferences().getString("currency", "");

        SharedPreferences prefs = getActivity().getSharedPreferences("countrySelectionString", MODE_PRIVATE);
        countrySelectionString = prefs.getString("countrySelectionString", null);



        try {
            countryArray = mComponentInfo.getmSharedPreferences().getString("countryList", "").split("\\|");
            countryCodeArray = mComponentInfo.getmSharedPreferences().getString("countryCodeList", "").split("\\|");
            countryPrefixArray = mComponentInfo.getmSharedPreferences().getString("countryPrefixCodeList", "").split("\\|");
            countryMobileNoLengthArray = mComponentInfo.getmSharedPreferences().getString("countryMobileNoLength", "").split("\\|");

        } catch (Exception e) {

            getActivity().finish();
        }

        nextButton_question = (Button) view.findViewById(R.id.nextButton_question);
        nextButton_question.setOnClickListener(this);

        nextButton_answer = (Button) view.findViewById(R.id.nextButton_answer);
        nextButton_answer.setOnClickListener(this);

        answerTextview = (TextView) view.findViewById(R.id.answerTextview);
        scrollview_question_page = (ScrollView) view.findViewById(R.id.scrollview_question_page);
        scrollview_answer_page = (ScrollView) view.findViewById(R.id.scrollview_answer_page);
        question_textview = (TextView) view.findViewById(R.id.question_textview);

        question_textview.setText(modalReceiveMoney.getQuestion());

        answerString=modalReceiveMoney.getAnswer();
        answerTextview.setText(answerString);

        mComponentInfo.setArrowBackButton_receiveCash(2);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {//confCode_EditText_BankingActivation 2131558542 / 52

            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();

              //  validationDetails();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getCountrySelection() {
        int ret = 0;
        for (int i = 0; i < countryArray.length; i++) {
            if (countryArray[i].equalsIgnoreCase(countrySelectionString)) {


                ret = i;
            }
        }
        return ret;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.nextButton_question:

                validation_questionPage();

                break;

            case R.id.nextButton_answer:

                validate_answerPage();

                break;



        }
    }


    void validate_answerPage() {

            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("frag_sender_Receipt_transfer").replace(R.id.frameLayout_cashtocash_receivemoney, new ReceiveMoneySenderDetailsRecipientDetailTransferDetailsFragment()).commit();
    }

    void validation_questionPage() {

       nextButton_question.setVisibility(View.GONE);
        scrollview_question_page.setVisibility(View.GONE);

        nextButton_answer.setVisibility(View.VISIBLE);
       scrollview_answer_page.setVisibility(View.VISIBLE);
       }







    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showProgressDialog(String message) {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(message);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
    }

    private void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    @Override
    public void onParsingCompleted(GeneralResponseModel generalResponseModel, ArrayList<Object> customResponseList, int requestNo) {
        hideProgressDialog();
        isServerOperationInProcess = false;

        if (generalResponseModel.getResponseCode() == 0) {

           // Toast.makeText(getActivity(), "Server Response " + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();

        } else {
            hideProgressDialog();
            Toast.makeText(getActivity(), "" + generalResponseModel.getUserDefinedString(), Toast.LENGTH_SHORT).show();
            // Subscriber/Agent Not Found
        }
    }


}