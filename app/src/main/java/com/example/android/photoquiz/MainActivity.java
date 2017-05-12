/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.photoquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.photoquiz.R.string.question4Answer;
import static com.example.android.photoquiz.R.string.question6Answer1;
import static com.example.android.photoquiz.R.string.question8Answer;


/**
 * This app is a photography quiz which returns a score to the user.
 * The correct answers are stored in a final array (@CORRECT_ANSWERS) while another array
 * (@userInput) stores the user's answers. The two arrays are then crosschecked and a score is
 * produced based on the user's answers
 *
 * When the user pressed the Submit Button, all CheckBoxes, RadioButtons and EditTexts are
 * disabled so the user cannot change his answers and the score is calculated. Then, the Show
 * Answers Button is enabled and the Submit Button is disabled.
 *
 * When the Answers Button is pressed, the correct answers are displayed by changing the
 * backgroud color of each answer to red if it is incorrect and green if it is correct.
 *
 * When the Reset Button is pressed, the score is reset, as are all the background colors, the
 * Submit Button is enabled and the Show Answers is disabled.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    
    /** We assign static Resource Ids to all CheckBoxes and RadioButtons with values from 
     * @RES_ID */
    private final int[] RES_ID = {100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,
            116,117,118,119,120,121,122,123,124,125,126,127};

    /** @CORRECT_ANSWERS informs us whether a CheckBox or RadioButton contains a correct answer.
     * We use it to check against user answers */
    private final boolean[] CORRECT_ANSWERS = {false,false,true,false,true,false,false,false,true,false,true,false,false,true,false,true,false,true,false,false,true,true,false,false,true,false,
            false,true};

    /** @userInput stores the answers given by the user. If all are correct, all its values will
     * be equal to those of @CORRECT_ANSWERS */
    boolean[] userInput = new boolean[28];

    /** @lastIndexUsed stores the index position of the last Resource Id used so the the same
     * Resource Id is not used twice*/
    int lastIndexUsed = 0;

    int finalScore;
    boolean[] editTextAnswers = {false,false,false};
    
    /** Variable @pressedSubmitButtonSwitch controls whether or not the Submit button has been
     * pressed. 
     * If false, the button has not been pressed. We use later later as a switch in the code to 
     * enable or disable both the submitButton and the showAnswersButton */
    boolean pressedSubmitButtonSwitch = false;

    /** Variable @pressedShowAnswersButtonSwitch controls whether or not the showAnswers button has been
     * pressed. If false, the button has not been pressed. We use later later as a switch in the code to 
     * enable or disable both the submitButton and the showAnswersButton */
    boolean pressedShowAnswersButtonSwitch = false;

    // Declare Views
    ViewGroup viewGroup;
    TextView finalScoreTextView;
    Button answersButton;
    Button submitButton;
    Button resetButton;
    EditText question4EditText;
    EditText question6EditText;
    EditText question8EditText;

    // Declare final strings to be used to save instance
    private static final String STATE_FINAL_SCORE = "finalScore";
    private static final String STATE_USER_INPUT = "userInput";
    private static final String STATE_EDIT_TEXT_ANSWERS = "editTextAnswers";
    private static final String STATE_PRESSED_SUBMIT_SWITCH = "pressedSubmitButtonSwitch";
    private static final String STATE_PRESSED_SHOW_ANSWERS_BUTTON_SWITCH = "pressedShowAnswersButtonSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set final score to 0
        finalScore = 0;

        // Initialize Views
        initializeViews();
        // Set the user's answers to false
        initArray();
        // Assign Resource Ids to the Views using @RES_ID
        assignResIds();
        // Display score
        displayScore();

        // Initialize OnClickListeners
        initializeButtonOnClickListeners();
        initializeRdbChkbOnClickListeners();

        // Disable Answers Button and Reset Button
        disableAnswersButton();
        disableResetButton();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Store variable values
        savedInstanceState.putInt(STATE_FINAL_SCORE, finalScore);
        savedInstanceState.putBooleanArray(STATE_USER_INPUT, userInput);
        savedInstanceState.putBooleanArray(STATE_EDIT_TEXT_ANSWERS, editTextAnswers);
        savedInstanceState.putBoolean(STATE_PRESSED_SUBMIT_SWITCH, pressedSubmitButtonSwitch);
        savedInstanceState.putBoolean(STATE_PRESSED_SHOW_ANSWERS_BUTTON_SWITCH, pressedShowAnswersButtonSwitch);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve variable values
        finalScore = savedInstanceState.getInt(STATE_FINAL_SCORE);
        userInput = savedInstanceState.getBooleanArray(STATE_USER_INPUT);
        editTextAnswers = savedInstanceState.getBooleanArray(STATE_EDIT_TEXT_ANSWERS);
        pressedSubmitButtonSwitch = savedInstanceState.getBoolean(STATE_PRESSED_SUBMIT_SWITCH);
        pressedShowAnswersButtonSwitch = savedInstanceState.getBoolean(STATE_PRESSED_SHOW_ANSWERS_BUTTON_SWITCH);

        //Display variables
        displayScore();

        // Check if the Submit Button has been pressed and enable/disable buttons accordingly
        if (pressedSubmitButtonSwitch == true) {
            disableSubmitButton();
            enableAnswersButton();
            enableResetButton();
            disableViews();

        } else if (pressedSubmitButtonSwitch == false) {
            enableSubmitButton();
            disableAnswersButton();
        }
        // Check if the Show Answers has been pressed and enable/disable buttons accordingly
        if (pressedShowAnswersButtonSwitch == true) {
            answersButton.performClick();
        }
    }

    public void submitScoreButton() {
        // Checks EditText answers
        checkQuestion4();
        checkQuestion6();
        checkQuestion8();

        // Calculate score
        calcScore();
        // Display score
        displayScore();
        // Display score toast
        Toast.makeText(this, "Score: " + finalScore + "/40", Toast.LENGTH_SHORT).show();
        // Enable the Show Answers Button
        enableAnswersButton();
        // Enable the Reset Button
        enableResetButton();
        // Disable the Submit Button
        disableSubmitButton();
        // Disable all views so the user cannot change his answers
        disableViews();

        // Stores information that the Submit Button has been pressed
        pressedSubmitButtonSwitch = true;
    }

    public void showAnswers() {
        // Use for loop to get all CheckBoxes and RadioButton sequentially
        for (int i = 0; i < 28; i++) {
            // Get Resource Id
            int ind = RES_ID[i];
            // Get view
            View view1 = findViewById(ind);
            if (userInput[i] == true && userInput[i] == CORRECT_ANSWERS[i]) {
                // Assign green background
                view1.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer));
            } else if (userInput[i] == false && CORRECT_ANSWERS[i] == true) {
                // Assign red background
                view1.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer));
            }
        }
        // Check if user's answer is correct
        if (editTextAnswers[0] == true) {
            // Assign green background
            question4EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer));
        } else if (editTextAnswers[0] == false) {
            // Assign red background
            question4EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer));
            // Show correct answer
            question4EditText.setText(question4Answer);
        }
        // Check if user's answer is correct
        if (editTextAnswers[1] == true) {
            // Assign green background
            question6EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer));
        } else if (editTextAnswers[1] == false) {
            // Assign red background
            question6EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer));
            // Show correct answer
            question6EditText.setText(question6Answer1);
        }
        // Check if user's answer is correct
        if (editTextAnswers[2] == true) {
            // Assign green background
            question8EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.correctAnswer));
        } else if (editTextAnswers[2] == false) {
            // Assign red background
            question8EditText.setBackgroundColor(ContextCompat.getColor(this, R.color.wrongAnswer));
            // Show correct answer
            question8EditText.setText(question8Answer);
        }

        // Disable Show Answers Button
        disableAnswersButton();
        // Stores information that the Show Answers Button has been pressed
        pressedShowAnswersButtonSwitch = true;
    }

    public void resetButton() {
        // Reset score
        finalScore = 0;

        // Reset all user answers
        initArray();
        // Display score
        displayScore();
        // Enable the Submit Score Button
        enableSubmitButton();
        // Disable the Show Answers Button
        disableAnswersButton();
        // Disable the Reset Button
        disableResetButton();
        // Enable all views
        enableViews();
        // Remove red and green background color
        resetBackgroundColors();

        // Stores information that the Submit Button has not been pressed
        pressedSubmitButtonSwitch = false;
        // Stores information that the Show Answers Button has been pressed
        pressedShowAnswersButtonSwitch = false;;
    }


    // Reset userInput and editTextAnswers array to false to show that the user has not answered
    // any questions
    public void initArray() {
        // Use for loop to select all CheckBoxes and RadioButtons sequentially
        for (int i = 0; i < 28; i++) {
            // Set value to false
            userInput[i] = false;
        }

        // Use for loop to select all EditTexts sequentially
        for (int j = 0; j < 3; j ++) {
            // Set value to false
            editTextAnswers[j] = false;
        }
    }

    // Assign Resource Ids to all CheckBoxes and RadioButtons from resId array
    public void assignResIds () {
        // Get the number of views in the layout
        int counterViews = viewGroup.getChildCount();

        // Use for loop to select all CheckBoxes and RadioButtons sequentially
        for (int i = 0; i < counterViews; i++) {
            // Get child i of viewGroup
            View v = viewGroup.getChildAt(i);

            // Check if child is CheckBox
            if (v instanceof CheckBox) {
                // Select the last used Resource ID and assign it to the CheckBox
                v.setId(RES_ID[lastIndexUsed]);
                // Increase lastIndexUsed so it is not used again
                lastIndexUsed ++;
            }
            // Check if child is RadioGroup
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                // Get the number of RadioButtons in the RadioGroup
                int radioButtons = rdgp.getChildCount();

                // Use for loop to select all RadioButtons sequentially
                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    // Select the last used Resource ID and assign it to the RadioButton
                    vr.setId(RES_ID[lastIndexUsed]);
                    // Increase lastIndexUsed so it is not used again
                    lastIndexUsed ++;
                }
            }
        }

    }

    // Check EditText Answers
    public void checkQuestion4() {
        // Get user's answer
        String question4UserAnswer = question4EditText.getText().toString();
        // Convert user's answer to uppercase to in case the user has no capital letters
        question4UserAnswer = question4UserAnswer.toUpperCase();
        // Get correct answer
        String questionCorrect4Answer = getString(R.string.question4Answer);
        // Convert correct answer to uppercase to in case the user has no capital letters
        questionCorrect4Answer = questionCorrect4Answer.toUpperCase();

        // Crosscheck answer and store true if the user answered correctly
        if (question4UserAnswer.equals(questionCorrect4Answer)) {
            editTextAnswers[0] = true;
        }
    }

    public void checkQuestion6() {
        // Get user's answer
        String question6UserAnswer = question6EditText.getText().toString();
        // Convert user's answer to uppercase to in case the user has no capital letters
        question6UserAnswer = question6UserAnswer.toUpperCase();

        // Since the answer is correct whether the dash is used or not, 2 correct answers are
        // provided
        // Get correct answer 1
        String question6CorrectAnswer1 = getString(R.string.question6Answer1);
        // Get correct answer 2
        String question6CorrectAnswer2 = getString(R.string.question6Answer2);
        // Convert correct answers to uppercase to in case the user has no capital letters
        question6CorrectAnswer1 = question6CorrectAnswer1.toUpperCase();
        question6CorrectAnswer2 = question6CorrectAnswer2.toUpperCase();

        // Crosscheck answer and store true if the user answered correctly
        if (question6UserAnswer.equals(question6CorrectAnswer1) || question6UserAnswer.equals(question6CorrectAnswer2)) {
            editTextAnswers[1] = true;
        }
    }

    public void checkQuestion8() {
        // Get user's answer
        String question8UserAnswer = question8EditText.getText().toString();
        // Convert user's answer to uppercase to in case the user has no capital letters
        question8UserAnswer = question8UserAnswer.toUpperCase();

        // Get correct answer
        String question8CorrectAnswer = getString(R.string.question8Answer);
        // Convert correct answer to uppercase to in case the user has no capital letters
        question8CorrectAnswer = question8CorrectAnswer.toUpperCase();

        // Crosscheck answer and store true if the user answered correctly
        if (question8UserAnswer.equals(question8CorrectAnswer)) {
            editTextAnswers[2] = true;
        }
    }

    // Find index of Resource Id in resID array and assign value in userInput array based on
    // user's answer - true for correct
    public void resIdIndexChecked(int number) {
        // Use for loop to check the position in RES_ID of number
        for (int i = 0; i < 28; i ++) {
            if (RES_ID[i] == number) {
                // Assign true to userInput to indicate a correct answer
                userInput[i] = true;
                break;
            }
        }
    }

    // Find index of Resource Id in resID array and assign value in userInput array based on
    // user's answer - false for incorrect
    public void resIdIndexUnchecked(int number) {
        // Use for loop to check the position in RES_ID of number
        for (int i = 0; i < 28; i ++) {
            if (RES_ID[i] == number) {
                // Assign false to userInput to indicate an incorrect answer
                userInput[i] = false;
                break;
            }
        }
    }

    // Calculate score
    public void calcScore () {
        // Use for loop to get all CheckBoxes and RadioButtons sequentially
        for (int i = 0; i < 28; i ++) {
            // Check if the user's answer is the same as the correct one
            if (userInput[i] == CORRECT_ANSWERS[i] && CORRECT_ANSWERS[i] == true){
                // Increase score by 2
                finalScore += 2;
            }
        }
        // Use for loop to get all EditBoxes sequentially
        for (int j = 0; j < 3; j ++) {
            // Check if the user's answer is the same as the correct one
            if (editTextAnswers[j] == true) {
                // Increase score by 6
                finalScore += 6;
            }
        }
    }

    // Display score
    public void displayScore () {
        finalScoreTextView.setText("" + finalScore + "/40");
    }

    // Enable Show Answer Button
    public void enableAnswersButton() {
        answersButton.setEnabled(true);
    }

    // Disable Show Answer Button
    public void disableAnswersButton () {
        answersButton.setEnabled(false);
    }

    // Enable Submit Button
    public void enableSubmitButton () {
        submitButton.setEnabled(true);
    }

    // Disable Submit Button
    public void disableSubmitButton () {
        submitButton.setEnabled(false);
    }

    // Enable Reset Button
    public void enableResetButton () {
        resetButton.setEnabled(true);
    }

    // Disable Reset Button
    public void disableResetButton () {
        resetButton.setEnabled(false);
    }

    // Disables all TextBoxes, RadioButtons and EditTexts so the user cannot change them after
    // pressing the Submit Button
    public void disableViews () {
        // Get the number of views in the layout
        int counterViews = viewGroup.getChildCount();

        // Use for loop to select all views sequentially
        for (int i = 0; i < counterViews; i++) {
            // Get child i of viewGroup
            View v = viewGroup.getChildAt(i);
            // Check if child is CheckBox
            if (v instanceof CheckBox) {
                CheckBox chk = (CheckBox) v;
                // Disable CheckBox
                chk.setEnabled(false);
            }
            // Check if child is RadioGroup
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                // Get the number of RadioButtons in the RadioGroup
                int radioButtons = rdgp.getChildCount();
                // Use for loop to select all RadioButtons sequentially
                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    RadioButton rdb = (RadioButton) vr;
                    // Disable RadioButton
                    rdb.setEnabled(false);
                }
            }
            // Check if child is EditText
            else if (v instanceof EditText) {
                EditText edt = (EditText) v;
                // Disable EditText
                edt.setEnabled(false);
            }
        }
    }

    // Enables all TextBoxes, RadioButtons and EditTexts to prepare for a new quiz
    public void enableViews () {
        // Get the number of views in the layout
        int counterViews = viewGroup.getChildCount();

        // Use for loop to select all views sequentially
        for (int i = 0; i < counterViews; i++) {
            // Get child i of viewGroup
            View v = viewGroup.getChildAt(i);
            //Check if child is CheckBox
            if (v instanceof CheckBox) {
                CheckBox chk = (CheckBox) v;
                // Enable CheckBox
                chk.setEnabled(true);
                // Un-check CheckBox
                chk.setChecked(false);
            }
            //Check if child is RadioGroup
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                // Un-check RadioGroup
                rdgp.clearCheck();
                // Get the number of RadioButtons in the RadioGroup
                int radioButtons = rdgp.getChildCount();
                // Use for loop to select all RadioButtons sequentially
                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    RadioButton rdb = (RadioButton) vr;
                    // Enable RadioButton
                    rdb.setEnabled(true);
                }
            }
            // Check if child is EditText
            else if (v instanceof EditText) {
                EditText edt = (EditText) v;
                // Disable EditText
                edt.setEnabled(true);
                // Set Text to ""
                edt.setText("");
            }
        }
    }

    //Removes background colors for all TextBoxes, RadioButtons and EditTexts to prepare for a new
    // quiz
    public void resetBackgroundColors () {
        // Use for loop to select all CheckBoxes and RadioButtons sequentially
        for (int i = 0; i < 28; i ++) {
            int ind = RES_ID[i];
            // Get view
            View view1 = findViewById(ind);
            // Set color to transparent
            view1.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set background color of EditBoxes to transparent
        question4EditText.setBackgroundColor(Color.TRANSPARENT);
        question6EditText.setBackgroundColor(Color.TRANSPARENT);
        question8EditText.setBackgroundColor(Color.TRANSPARENT);
    }

    // Initializes all views
    public void initializeViews() {
        answersButton = (Button) findViewById(R.id.answers_Button);
        submitButton = (Button) findViewById(R.id.submit_Button);
        resetButton = (Button) findViewById(R.id.reset_Button);
        viewGroup = (ViewGroup) findViewById(R.id.questions);
        finalScoreTextView = (TextView) findViewById(R.id.score_Box);
        question4EditText = (EditText) findViewById(R.id.question_4_Edit_Text);
        question6EditText = (EditText) findViewById(R.id.question_6_Edit_Text);
        question8EditText = (EditText) findViewById(R.id.question_8_Edit_Text);
    }

    // Initialize Button OnClickListeners
    public void initializeButtonOnClickListeners(){
        answersButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    // Initialize Radio Button and CheckBox OnClickListeners
    public void initializeRdbChkbOnClickListeners(){
        // Get the number of views in the layout
        int counterViews = viewGroup.getChildCount();

        // Use for loop to select all views sequentially
        for (int i = 0; i < counterViews; i++) {
            // Get child i of viewGroup
            View v = viewGroup.getChildAt(i);
            // Check if child is CheckBox
            if (v instanceof CheckBox) {
                CheckBox chk = (CheckBox) v;
                // Attach OnclickListener
                chk.setOnClickListener(this);
            }
            // Check if child is RadioGroup
            else if (v instanceof RadioGroup) {
                RadioGroup rdgp = (RadioGroup) v;
                // Get the number of RadioButtons in the RadioGroup
                int radioButtons = rdgp.getChildCount();
                // Use for loop to select all RadioButtons sequentially
                for (int j = 0; j < radioButtons; j++) {
                    View vr = rdgp.getChildAt(j);
                    RadioButton rdb = (RadioButton) vr;
                    // Attach OnclickListener
                    rdb.setOnClickListener(this);
                }
            }
        }
    }

    // Implement onClick method
    @Override
    public void onClick(View view) {
        // In the case of Buttons, we simply call the appropriate method
        // In the case of RadioButtons, we have to create a new Radiobutton rdb each time, check
        // if is checked and then call resIdIndexChecked or resIdIndexUnchecked
        // In the case of CheckBoxes, we have to create a new Checkbox chk each time, check if is
        // checked and then call resIdIndexChecked or resIdIndexUnchecked
        switch (view.getId()) {
            // Buttons
            case R.id.answers_Button:
                showAnswers();
                break;
            case R.id.submit_Button:
                submitScoreButton();
                break;
            case R.id.reset_Button:
                resetButton();
                break;
            // RadioButtons
            // Question 1
            case 100:
                RadioButton rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(100);
                }
                else {
                    resIdIndexUnchecked(100);
                }
                break;
            case 101:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(101);
                }
                else {
                    resIdIndexUnchecked(101);
                }
                break;
            case 102:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(102);
                }
                else {
                    resIdIndexUnchecked(102);
                }
                break;

            case 103:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(103);
                }
                else {
                    resIdIndexUnchecked(103);
                }
                break;
            // Question 2
            case 104:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(104);
                }
                else {
                    resIdIndexUnchecked(104);
                }
                break;
            case 105:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(105);
                }
                else {
                    resIdIndexUnchecked(105);
                }
                break;
            case 106:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(106);
                }
                else {
                    resIdIndexUnchecked(106);
                }
                break;
            case 107:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(107);
                }
                else {
                    resIdIndexUnchecked(107);
                }
                break;
            // Question 7
            case 116:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(116);
                }
                else {
                    resIdIndexUnchecked(116);
                }
                break;
            case 117:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(117);
                }
                else {
                    resIdIndexUnchecked(117);
                }
                break;
            case 118:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(118);
                }
                else {
                    resIdIndexUnchecked(118);
                }
                break;
            case 119:
                rdb = (RadioButton) findViewById(view.getId());
                if (rdb.isChecked()) {
                    resIdIndexChecked(119);
                }
                else {
                    resIdIndexUnchecked(119);
                }
                break;
            // CheckBoxes
            // Question 3
            case 108:
                CheckBox chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(108);
                }
                else {
                    resIdIndexUnchecked(108);
                }
                break;
            case 109:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(109);
                }
                else {
                    resIdIndexUnchecked(109);
                }
                break;
            case 110:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(110);
                }
                else {
                    resIdIndexUnchecked(110);
                }
                break;
            case 111:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(111);
                }
                else {
                    resIdIndexUnchecked(111);
                }
                break;
            // Question 5
            case 112:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(112);
                }
                else {
                    resIdIndexUnchecked(112);
                }
                break;
            case 113:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(113);
                }
                else {
                    resIdIndexUnchecked(113);
                }
                break;
            case 114:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(114);
                }
                else {
                    resIdIndexUnchecked(114);
                }
                break;
            case 115:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(115);
                }
                else {
                    resIdIndexUnchecked(115);
                }
                break;
            // Question 9
            case 120:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(120);
                }
                else {
                    resIdIndexUnchecked(120);
                }
                break;
            case 121:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(121);
                }
                else {
                    resIdIndexUnchecked(121);
                }
                break;
            case 122:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(122);
                }
                else {
                    resIdIndexUnchecked(122);
                }
                break;
            case 123:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(123);
                }
                else {
                    resIdIndexUnchecked(123);
                }
                break;
            // Question 10
            case 124:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(124);
                }
                else {
                    resIdIndexUnchecked(124);
                }
                break;
            case 125:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(125);
                }
                else {
                    resIdIndexUnchecked(125);
                }
                break;
            case 126:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(126);
                }
                else {
                    resIdIndexUnchecked(126);
                }
                break;
            case 127:
                chk = (CheckBox) findViewById(view.getId());
                if (chk.isChecked()) {
                    resIdIndexChecked(127);
                }
                else {
                    resIdIndexUnchecked(127);
                }
                break;
        }
    }
}
