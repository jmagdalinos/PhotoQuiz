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
    //Declare variables
    int finalScore;
    final int[] RES_ID = {100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127};
    final int[] ANSWERS = {0,0,1,0,1,0,0,0,1,0,1,0,0,1,0,1,0,1,0,0,1,1,0,0,1,0,0,1};
    int[] userInput = new int[28];
    int lastIndexUsed = 0;
    int[] editTextAnswers = {0,0,0};
    int enableSubmitButtonSwitch = 0; //enableSubmitButtonSwitch = 0 --> Submit button has not been pressed
    int enableShowAnswersSwitch = 0; //enableShowAnswersSwitch = 0 --> Show Answers button has not been pressed

    //Declare Views
    ViewGroup viewGroup;
    TextView finalScoreTextView;
    Button answersButton;
    Button submitButton;
    EditText question4EditText;
    EditText question6EditText;
    EditText question8EditText;


    //Prepare to save instance
    private static final String STATE_FINAL_SCORE = "finalScore";
    private static final String STATE_USER_INPUT = "userInput";
    private static final String STATE_EDIT_TEXT_ANSWERS = "editTextAnswers";
    private static final String STATE_ENABLE_SUBMIT_SWITCH = "enableSubmitButtonSwitch";
    private static final String STATE_ENABLE_SHOW_ANSWERS_SWITCH = "enableShowAnswersSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finalScore = 0;

        //Initialize Views
        initializeViews();

        initArray();
        assignResIds();
        displayScore();

        if (enableSubmitButtonSwitch == 1) {
            disableSubmitButton();
            enableAnswersButton();
        } else if (enableSubmitButtonSwitch == 0) {
            enableSubmitButton();
            disableAnswersButton();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_FINAL_SCORE, finalScore);
        savedInstanceState.putIntArray(STATE_USER_INPUT, userInput);
        savedInstanceState.putIntArray(STATE_EDIT_TEXT_ANSWERS, editTextAnswers);
        savedInstanceState.putInt(STATE_ENABLE_SUBMIT_SWITCH, enableSubmitButtonSwitch);
        savedInstanceState.putInt(STATE_ENABLE_SHOW_ANSWERS_SWITCH, enableShowAnswersSwitch);

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
        enableSubmitButtonSwitch = savedInstanceState.getInt(STATE_ENABLE_SUBMIT_SWITCH);
        enableShowAnswersSwitch = savedInstanceState.getInt(STATE_ENABLE_SHOW_ANSWERS_SWITCH);
        //Display variables
        displayScore();

        if (enableSubmitButtonSwitch == 1) {
            disableSubmitButton();
            enableAnswersButton();
        } else if (enableSubmitButtonSwitch == 0) {
            enableSubmitButton();
            disableAnswersButton();
        }

        if (enableShowAnswersSwitch == 1) {
            answersButton.performClick();
        }
    }

    public void submitScoreButton (View view) {
        //Checks EditText answers
        checkQuestion4();
        checkQuestion6();
        checkQuestion8();

        //Displays score, enables "Show Results" Button, disables "Submit Your Score!" Button
        Log.v("MainActivity", "Score: " + finalScore);
        calcScore();
        displayScore();
        enableAnswersButton();
        disableSubmitButton();
        disableViews();

        enableSubmitButtonSwitch = 1;

    }

    public void showAnswers (View view) {
        for (int i = 0; i < 28; i++) {
            int ind = RES_ID[i];
            View view1 = findViewById(ind);
            if (userInput[i] == 1 && userInput[i] == ANSWERS[i]) {
                view1.setBackgroundColor(getColor(R.color.correctAnswer));
            } else if (userInput[i] == 0 && ANSWERS[i] == 1) {
                view1.setBackgroundColor(getColor(R.color.wrongAnswer));
            }
        }

        if (editTextAnswers[0] == 1) {
            question4EditText.setBackgroundColor(getColor(R.color.correctAnswer));
        } else if (editTextAnswers[0] == 0) {
            question4EditText.setBackgroundColor(getColor(R.color.wrongAnswer));
            question4EditText.setText("Shutter Speed");
        }

        if (editTextAnswers[1] == 1) {
            question6EditText.setBackgroundColor(getColor(R.color.correctAnswer));
        } else if (editTextAnswers[1] == 0) {
            question6EditText.setBackgroundColor(getColor(R.color.wrongAnswer));
            question6EditText.setText("Single-Lens Reflex");
        }

        if (editTextAnswers[2] == 1) {
            question8EditText.setBackgroundColor(getColor(R.color.correctAnswer));
        } else if (editTextAnswers[2] == 0) {
            question8EditText.setBackgroundColor(getColor(R.color.wrongAnswer));
            question8EditText.setText("Guide Number");
        }

        enableShowAnswersSwitch = 1;
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

        enableSubmitButtonSwitch = 0;
        enableShowAnswersSwitch = 0;
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
//        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
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
//        EditText question4EditText = (EditText) findViewById(R.id.question_4_Edit_Text);
        String question4Answer = question4EditText.getText().toString();
        question4Answer = question4Answer.toUpperCase();

        if (question4Answer.equals("SHUTTER SPEED")) {
            editTextAnswers[0] = 1;
        }
    }

    public void checkQuestion6() {
//        EditText question6EditText = (EditText) findViewById(R.id.question_6_Edit_Text);
        String question6Answer = question6EditText.getText().toString();
        question6Answer = question6Answer.toUpperCase();

        if (question6Answer.equals("SINGLE LENS REFLEX") || question6Answer.equals("SINGLE-LENS REFLEX")) {
            editTextAnswers[1] = 1;
        }
    }

    public void checkQuestion8() {
//        EditText question8EditText = (EditText) findViewById(R.id.question_8_Edit_Text);
        String question8Answer = question8EditText.getText().toString();
        question8Answer = question8Answer.toUpperCase();

        if (question8Answer.equals("GUIDE NUMBER")) {
            editTextAnswers[2] = 1;
        }
    }


    //Finds index of Resource Id in resID array and assign value in userInput array based on user's answer
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
                userInput[i] = 0;
                break;
            }
        }
    }


    //Calculate score
    public void calcScore () {
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
    }


    //Display score
    public void displayScore () {
//        TextView finalScoreTextView = (TextView) findViewById(R.id.score_Box);
        finalScoreTextView.setText("" + finalScore + "/40");
    }


    //Enable/Disable Buttons & Views
    public void enableAnswersButton() {
        //Enable "Show Answers" button
//        Button enableAnswersButton = (Button) findViewById(R.id.answers_Button);
        answersButton.setEnabled(true);
    }

    public void disableAnswersButton () {
        //Disable "Show Answers" button
//        Button disableAnswersButton = (Button) findViewById(R.id.answers_Button);
        answersButton.setEnabled(false);
    }

    public void enableSubmitButton () {
        //Enable "Submit Your Score!" button
//        Button disableSubmitButton = (Button) findViewById(R.id.submit_Button);
        submitButton.setEnabled(true);
    }

    public void disableSubmitButton () {
        //Disable "Submit Your Score!" button
//        Button disableSubmitButton = (Button) findViewById(R.id.submit_Button);
        submitButton.setEnabled(false);
    }

    public void disableViews () {
        //Disables all CheckBoxes, RadioButtons and EditViews
//        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
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
//        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.questions);
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


    //Resets background colors
    public void resetBackgroundColors () {
        for (int i = 0; i < 28; i ++) {
            int ind = RES_ID[i];
            View view1 = findViewById(ind);
            view1.setBackgroundColor(Color.TRANSPARENT);
        }

//        View view2 = findViewById(R.id.question_4_Edit_Text);
//        View view3 = findViewById(R.id.question_6_Edit_Text);
//        View view4 = findViewById(R.id.question_8_Edit_Text);

        question4EditText.setBackgroundColor(Color.TRANSPARENT);
        question6EditText.setBackgroundColor(Color.TRANSPARENT);
        question8EditText.setBackgroundColor(Color.TRANSPARENT);
    }

    // Initializes all views
    public void initializeViews() {
        answersButton = (Button) findViewById(R.id.answers_Button);
        submitButton = (Button) findViewById(R.id.submit_Button);
        viewGroup = (ViewGroup) findViewById(R.id.questions);
        finalScoreTextView = (TextView) findViewById(R.id.score_Box);
        question4EditText = (EditText) findViewById(R.id.question_4_Edit_Text);
        question6EditText = (EditText) findViewById(R.id.question_6_Edit_Text);
        question8EditText = (EditText) findViewById(R.id.question_8_Edit_Text);

    }
}
