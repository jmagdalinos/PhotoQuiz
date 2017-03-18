/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.photoquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * This app is a photography quiz which returns a score to the user
 */
public class MainActivity extends AppCompatActivity {
    //Declare variables and prepare to save instance
    int finalScore = 0;
    final int[] RES_ID = {100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127};
    final int[] ANSWERS = {0,0,1,0,1,0,0,0,1,0,1,0,0,1,0,1,0,1,0,0,1,1,0,0,1,0,0,1};
    int[] userInput = new int[28];
    int lastIndexUsed = 0;
    int[] editTextAnswers = {0,0,0};

    private static final String STATE_FINAL_SCORE = "finalScore";
    private static final String STATE_USER_INPUT = "userInput";
    private static final String STATE_EDIT_TEXT_ANSWERS = "editTextAnswers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initArray();
        assignResIds();
        displayScore();
        disableAnswersButton();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_FINAL_SCORE, finalScore);
        savedInstanceState.putIntArray(STATE_USER_INPUT, userInput);
        savedInstanceState.putIntArray(STATE_EDIT_TEXT_ANSWERS, editTextAnswers);

        //Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        //Retrieve variable values
        finalScore = savedInstanceState.getInt(STATE_FINAL_SCORE);
        userInput = savedInstanceState.getIntArray(STATE_USER_INPUT);
        editTextAnswers = savedInstanceState.getIntArray(STATE_EDIT_TEXT_ANSWERS);

        //Display variables
        displayScore();
    }

    public void submitScoreButton (View view) {
        //Checks EditText answers
        checkQuestion4();
        checkQuestion6();
        checkQuestion8();

        //Displays score, enables "Show Results" Button, disables "Submit Your Score!" Button
        Log.v("MainActivity", "Score: " + finalScore);
        displayScore();
        enableAnswersButton();
        disableSubmitButton();
        disableViews();

    }

    public void showAnswers (View view) {
        for (int i = 0; i < 28; i++) {
            int ind = RES_ID[i];
            View view1 = findViewById(ind);
            if (userInput[i] == 1 && userInput[i] == ANSWERS[i]) {
                view1.setBackgroundColor(Color.rgb(129, 199, 132));
            } else if (userInput[i] == 0 && ANSWERS[i] == 1) {
                view1.setBackgroundColor(Color.rgb(229, 115, 115));
            }
        }

        EditText view2 = (EditText) findViewById(R.id.question_4_Edit_Text);
        EditText view3 = (EditText) findViewById(R.id.question_6_Edit_Text);
        EditText view4 = (EditText) findViewById(R.id.question_8_Edit_Text);

        if (editTextAnswers[0] == 1) {
            view2.setBackgroundColor(Color.rgb(129, 199, 132));
        } else if (editTextAnswers[0] == 0) {
            view2.setBackgroundColor(Color.rgb(229, 115, 115));
            view2.setText("Shutter Speed");
        }

        if (editTextAnswers[1] == 1) {
            view3.setBackgroundColor(Color.rgb(129, 199, 132));
        } else if (editTextAnswers[1] == 0) {
            view3.setBackgroundColor(Color.rgb(229, 115, 115));
            view3.setText("Single-Lens Reflex");
        }

        if (editTextAnswers[2] == 1) {
            view4.setBackgroundColor(Color.rgb(129, 199, 132));
        } else if (editTextAnswers[2] == 0) {
            view4.setBackgroundColor(Color.rgb(229, 115, 115));
            view4.setText("Guide Number");
        }
    }

    public void resetButton (View view) {
        //Resets and shows all variables, disables "Show Results" Button, enables "Submit Your Score!" Button
        finalScore = 0;

        initArray();
        displayScore();
        enableSubmitButton();
        disableAnswersButton();
        enableViews();
        resetBackgroundColors();
    }


    //Resets userInput and editTextAnswers array to 0
    public void initArray() {
        for (int i = 0; i < 28; i++) {
            userInput[i] = 0;
        }

        for (int j = 0; j < 3; j ++) {
            editTextAnswers[j] = 0;
        }
    }


    //Assigns Resource Ids to all CheckBoxes and RadioButtons from resId array
    public void assignResIds () {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
        int counterViews = viewGroup.getChildCount();

        for (int i = 0; i < counterViews; i++) {
            View v = viewGroup.getChildAt(i);

            if (v instanceof CheckBox) {
                v.setId(RES_ID[lastIndexUsed]);
                lastIndexUsed ++;
            }
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                int radioButtons = rdgp.getChildCount();

                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    vr.setId(RES_ID[lastIndexUsed]);
                    lastIndexUsed ++;
                }
            }
        }

    }


    //Checks RadioButton Answers and calls resIdIndex
    public void checkQuestionRadioButton(View view) {
        //Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        //Check which RadioButton was clicked
        switch (view.getId()) {
            // Question 1
            case 100:
                if (checked) {
                    resIdIndexChecked(100);
                }
                else {
                    resIdIndexUnchecked(100);
                }
                break;
            case 101:
                if (checked) {
                    resIdIndexChecked(101);
                }
                else {
                    resIdIndexUnchecked(101);
                }
                break;
            case 102:
                if (checked) {
                    resIdIndexChecked(102);
                }
                else {
                    resIdIndexUnchecked(102);
                }
                break;

            case 103:
                if (checked) {
                    resIdIndexChecked(103);
                }
                else {
                    resIdIndexUnchecked(103);
                }
                break;
            // Question 2
            case 104:
                if (checked) {
                    resIdIndexChecked(104);
                }
                else {
                    resIdIndexUnchecked(104);
                }
                break;
            case 105:
                if (checked) {
                    resIdIndexChecked(105);
                }
                else {
                    resIdIndexUnchecked(105);
                }
                break;
            case 106:
                if (checked) {
                    resIdIndexChecked(106);
                }
                else {
                    resIdIndexUnchecked(106);
                }
                break;
            case 107:
                if (checked) {
                    resIdIndexChecked(107);
                }
                else {
                    resIdIndexUnchecked(107);
                }
                break;
            // Question 7
            case 116:
                if (checked) {
                    resIdIndexChecked(116);
                }
                else {
                    resIdIndexUnchecked(116);
                }
                break;
            case 117:
                if (checked) {
                    resIdIndexChecked(117);
                }
                else {
                    resIdIndexUnchecked(117);
                }
                break;
            case 118:
                if (checked) {
                    resIdIndexChecked(118);
                }
                else {
                    resIdIndexUnchecked(118);
                }
                break;
            case 119:
                if (checked) {
                    resIdIndexChecked(119);
                }
                else {
                    resIdIndexUnchecked(119);
                }
                break;
        }
    }


    //Checks CheckBox Answers
    public void checkQuestionCheckBoxes(View view) {
        //Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        //Check which CheckBox was clicked
        switch (view.getId()) {
            // Question 3
            case 108:
                if (checked) {
                    resIdIndexChecked(108);
                }
                else {
                    resIdIndexUnchecked(108);
                }
                break;
            case 109:
                if (checked) {
                    resIdIndexChecked(109);
                }
                else {
                    resIdIndexUnchecked(109);
                }
                break;
            case 110:
                if (checked) {
                    resIdIndexChecked(110);
                }
                else {
                    resIdIndexUnchecked(110);
                }
                break;
            case 111:
                if (checked) {
                    resIdIndexChecked(111);
                }
                else {
                    resIdIndexUnchecked(111);
                }
                break;
            // Question 5
            case 112:
                if (checked) {
                    resIdIndexChecked(112);
                }
                else {
                    resIdIndexUnchecked(112);
                }
                break;
            case 113:
                if (checked) {
                    resIdIndexChecked(113);
                }
                else {
                    resIdIndexUnchecked(113);
                }
                break;
            case 114:
                if (checked) {
                    resIdIndexChecked(114);
                }
                else {
                    resIdIndexUnchecked(114);
                }
                break;
            case 115:
                if (checked) {
                    resIdIndexChecked(115);
                }
                else {
                    resIdIndexUnchecked(115);
                }
                break;
            // Question 9
            case 120:
                if (checked) {
                    resIdIndexChecked(120);
                }
                else {
                    resIdIndexUnchecked(120);
                }
                break;
            case 121:
                if (checked) {
                    resIdIndexChecked(121);
                }
                else {
                    resIdIndexUnchecked(121);
                }
                break;
            case 122:
                if (checked) {
                    resIdIndexChecked(122);
                }
                else {
                    resIdIndexUnchecked(122);
                }
                break;
            case 123:
                if (checked) {
                    resIdIndexChecked(123);
                }
                else {
                    resIdIndexUnchecked(123);
                }
                break;
            // Question 10
            case 124:
                if (checked) {
                    resIdIndexChecked(124);
                }
                else {
                    resIdIndexUnchecked(124);
                }
                break;
            case 125:
                if (checked) {
                    resIdIndexChecked(125);
                }
                else {
                    resIdIndexUnchecked(125);
                }
                break;
            case 126:
                if (checked) {
                    resIdIndexChecked(126);
                }
                else {
                    resIdIndexUnchecked(126);
                }
                break;
            case 127:
                if (checked) {
                    resIdIndexChecked(127);
                }
                else {
                    resIdIndexUnchecked(127);
                }
                break;
        }

    }


    // Checks EditText Answers
    public void checkQuestion4() {
        EditText question4EditText = (EditText) findViewById(R.id.question_4_Edit_Text);
        String question4Answer = question4EditText.getText().toString();
        question4Answer = question4Answer.toUpperCase();

        if (question4Answer.equals("SHUTTER SPEED")) {
            editTextAnswers[0] = 1;
        }
    }

    public void checkQuestion6() {
        EditText question6EditText = (EditText) findViewById(R.id.question_6_Edit_Text);
        String question6Answer = question6EditText.getText().toString();
        question6Answer = question6Answer.toUpperCase();

        if (question6Answer.equals("SINGLE LENS REFLEX") || question6Answer.equals("SINGLE-LENS REFLEX")) {
            editTextAnswers[1] = 1;
        }
    }

    public void checkQuestion8() {
        EditText question8EditText = (EditText) findViewById(R.id.question_8_Edit_Text);
        String question8Answer = question8EditText.getText().toString();
        question8Answer = question8Answer.toUpperCase();

        if (question8Answer.equals("GUIDE NUMBER")) {
            editTextAnswers[2] = 1;
        }
    }


    //Finds index of Resource Id in resID array
    public void resIdIndexChecked(int number) {
        for (int i = 0; i < 28; i ++) {
            if (RES_ID[i] == number) {
                userInput[i] = 1;
                break;
            }
        }
    }

    public void resIdIndexUnchecked(int number) {
        for (int i = 0; i < 28; i ++) {
            if (RES_ID[i] == number) {
                Log.v("MainActivity",number + " " + i);
                userInput[i] = 0;
                break;
            }
        }
    }


    //Display score
    public void displayScore () {
        for (int i = 0; i < 28; i ++) {
            if (userInput[i] == ANSWERS[i] && ANSWERS[i] == 1){
                finalScore += 2;
            }
        }

        for (int j = 0; j < 3; j ++) {
            if (editTextAnswers[j] == 1) {
                finalScore += 6;
            }
        }

        TextView finalScoreTextView = (TextView) findViewById(R.id.score_Box);
        finalScoreTextView.setText("" + finalScore + "/40");
    }


    //Enable/Disable Buttons & Views
    public void enableAnswersButton() {
        //Enable "Show Answers" button
        Button enableAnswersButton = (Button) findViewById(R.id.answers_Button);
        enableAnswersButton.setEnabled(true);
    }

    public void disableAnswersButton () {
        //Disable "Show Answers" button
        Button disableAnswersButton = (Button) findViewById(R.id.answers_Button);
        disableAnswersButton.setEnabled(false);
    }

    public void enableSubmitButton () {
        //Enable "Submit Your Score!" button
        Button disableSubmitButton = (Button) findViewById(R.id.submit_Button);
        disableSubmitButton.setEnabled(true);
    }

    public void disableViews () {
        //Disables all CheckBoxes, RadioButtons and EditViews
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
        int counterViews = viewGroup.getChildCount();

        for (int i = 0; i < counterViews; i++) {
            View v = viewGroup.getChildAt(i);

            if (v instanceof CheckBox) {
                CheckBox chk = (CheckBox) v;
                chk.setEnabled(false);

            }
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                int radioButtons = rdgp.getChildCount();

                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    RadioButton rdb = (RadioButton) vr;
                    rdb.setEnabled(false);
                }
            }
            else if (v instanceof EditText) {
                EditText edt = (EditText) v;
                edt.setEnabled(false);
            }
        }
    }

    public void enableViews () {
        //Enables all CheckBoxes, RadioButtons and EditViews and resets them
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
        int counterViews = viewGroup.getChildCount();

        for (int i = 0; i < counterViews; i++) {
            View v = viewGroup.getChildAt(i);

            if (v instanceof CheckBox) {
                CheckBox chk = (CheckBox) v;
                chk.setEnabled(true);
                chk.setChecked(false);
            }
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                rdgp.clearCheck();
                int radioButtons = rdgp.getChildCount();
                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    RadioButton rdb = (RadioButton) vr;
                    rdb.setEnabled(true);
                }
            }
            else if (v instanceof EditText) {
                EditText edt = (EditText) v;
                edt.setEnabled(true);
                edt.setText("");
            }
        }
    }

    public void disableSubmitButton () {
        //Disable "Submit Your Score!" button
        Button disableSubmitButton = (Button) findViewById(R.id.submit_Button);
        disableSubmitButton.setEnabled(false);
    }


    //Resets background colors
    public void resetBackgroundColors () {
        for (int i = 0; i < 28; i ++) {
            int ind = RES_ID[i];
            View view1 = findViewById(ind);
            view1.setBackgroundColor(Color.TRANSPARENT);
        }

        View view2 = findViewById(R.id.question_4_Edit_Text);
        View view3 = findViewById(R.id.question_6_Edit_Text);
        View view4 = findViewById(R.id.question_8_Edit_Text);

        view2.setBackgroundColor(Color.TRANSPARENT);
        view3.setBackgroundColor(Color.TRANSPARENT);
        view4.setBackgroundColor(Color.TRANSPARENT);
    }
}
