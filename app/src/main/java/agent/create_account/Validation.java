package agent.create_account;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import agent.activities.R;

public class Validation extends AppCompatActivity implements View.OnClickListener {
    Bitmap imageToSave;
    EditText edittext_firstName, edittext_secondName, edittext_adddress;
    Button button_click;
    String firstNameString, secondNameString, addressString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation);

        // createDirectoryAndSaveFile(imageToSave,"dsfdf");

        edittext_firstName = (EditText) findViewById(R.id.edittext_firstName);
        edittext_secondName = (EditText) findViewById(R.id.edittext_secondName);
        edittext_adddress = (EditText) findViewById(R.id.edittext_adddress);

        button_click = (Button) findViewById(R.id.button_click);
        button_click.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_click: {

                firstNameString = edittext_firstName.getText().toString();
                secondNameString = edittext_secondName.getText().toString();
                addressString = edittext_adddress.getText().toString();


                if (validation_third()) {
                    proceedButton();
                }

                 // validation_details();

            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////

    boolean validation_first() {

        boolean valid = true;

        firstNameString = edittext_firstName.getText().toString();
        secondNameString = edittext_secondName.getText().toString();
        addressString = edittext_adddress.getText().toString();

        if (firstNameString.isEmpty() || firstNameString.length() < 3) {
            Toast.makeText(Validation.this, "First", Toast.LENGTH_LONG).show();
            valid = false;
        } else if (secondNameString.isEmpty() || secondNameString.length() < 3) {
            Toast.makeText(Validation.this, "Second", Toast.LENGTH_LONG).show();
            valid = false;

        } else if (addressString.isEmpty() || addressString.length() < 5 || addressString.length() > 10) {
            Toast.makeText(Validation.this, "Address 5 To 10", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;

    }

    ////////////////////////////////////////////////////////////////////////////////////

    private boolean validation_second() {

        if (firstNameString.isEmpty()) {

            Toast.makeText(Validation.this, "First Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (secondNameString.isEmpty()) {
            Toast.makeText(Validation.this, "Second Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (addressString.isEmpty()) {
            Toast.makeText(Validation.this, "Address", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////

    boolean validation_third() {

        boolean ret = false;

        if (!firstNameString.isEmpty()) {

            if (!secondNameString.isEmpty()) {

                if (!addressString.isEmpty()) {

                    ret = true;

                } else {
                    Toast.makeText(Validation.this, "Address ....", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Validation.this, "Second Name ....", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(Validation.this, "First Name ....", Toast.LENGTH_LONG).show();
        }

        return ret;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    void validation_details() {

        if (firstNameString.length() == 0) {
            edittext_firstName.requestFocus();
            edittext_firstName.setError("Enter First mobile Number");
        }

        else if (secondNameString.length()==0) {
            edittext_secondName.requestFocus();
            edittext_secondName.setError("Enter Second Mobile Number");
        }

        else if (addressString.length() == 0) {
            edittext_adddress.requestFocus();
            edittext_adddress.setError("Enter Address");
        }

        else {
            proceedButton();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    void proceedButton() {
        Toast.makeText(Validation.this, "Next page", Toast.LENGTH_LONG).show();
    }
}