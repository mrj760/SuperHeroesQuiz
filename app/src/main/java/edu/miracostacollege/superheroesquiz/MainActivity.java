package edu.miracostacollege.superheroesquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.miracostacollege.superheroesquiz.Model.CurrentSettings;
import edu.miracostacollege.superheroesquiz.Model.JSONLoader;
import edu.miracostacollege.superheroesquiz.Model.Superhero;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Superheroes Quiz";
    private static final int HEROES_IN_QUIZ = 10;


    private Button[] buttons = new Button[4];
    // all of the superheroes that are candidates to be in the quiz, pulled from JSON
    private List<Superhero> allSuperHeroesList;
    // the heroes that will be put into our 10 question quiz
    private List<Superhero> quizSuperheroesList;
    // the currently correct object
    private Superhero correctSuperHero;
    // static count over the course of the quiz, goes up with every button press
    private int totalGuesses;
    // static count that goes up over the course of the quiz, goes up with every correct guess
    private int correctGuesses;
    // used to randomize the contents of the quiz
    private SecureRandom rng;
    // used to make the app pause for a second after a correct answer so that it doesn't move right on
    private Handler handler;

    // shows which question we're on (e.g. Question 1 out of 10)
    private TextView currentQuestionTV;
    // the image of the current superhero
    private ImageView currentHeroImage;
    // will tell the user whether to guess the name, the one thing to know, or the superpower
    private TextView guessInstructionsTV;
    // will say either "Incorrect!" or "Correct!"
    private TextView guessResultTV;

    CurrentSettings currentSettings;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        quizSuperheroesList = new ArrayList<>(HEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();


        currentQuestionTV = findViewById(R.id.question);
        currentHeroImage = findViewById(R.id.superheroImage);
        guessInstructionsTV = findViewById(R.id.guessInstructions);
        guessResultTV = findViewById(R.id.guessResult);
        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);




        try {
            allSuperHeroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }


        currentSettings = new CurrentSettings(CurrentSettings.QuizType.NAMES);
        for (Button b : buttons)
            b.setTag(currentSettings);

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeGuess(v);
                }
            });

        }

        resetQuiz();


    }



    public void resetQuiz() {

        correctGuesses =0;
        totalGuesses =0;

        quizSuperheroesList.clear();

        int allSuperheroesSize = allSuperHeroesList.size();
        int randomPos;
        Superhero randomHero;

        while (quizSuperheroesList.size() < HEROES_IN_QUIZ) {
            randomPos = rng.nextInt(allSuperheroesSize);
            randomHero = allSuperHeroesList.get(randomPos);

            if (!quizSuperheroesList.contains(randomHero))
                quizSuperheroesList.add(randomHero);
        }

        loadNextHero();
    }


    public void loadNextHero() {

        correctSuperHero = quizSuperheroesList.get(0);
        quizSuperheroesList.remove(0);

        guessResultTV.setText("");

        currentQuestionTV.setText(getString(R.string.question, (correctGuesses+1), HEROES_IN_QUIZ));

        AssetManager am = getAssets();

        try {
            InputStream is = am.open(correctSuperHero.getImageName());
            Drawable image = Drawable.createFromStream(is, correctSuperHero.getName()); // the second part is a description, the actual image name is all that matters
            currentHeroImage.setImageDrawable(image);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }



        // set all buttons to random names that aren't the same as the correct one
        //      the correct one comes next, this is just first
        do {
            Collections.shuffle(allSuperHeroesList);
        } while (allSuperHeroesList.subList(0,buttons.length).contains(correctSuperHero));
        // now the first 4 (or however many buttons we have) in the list don't contain the correct hero

        for (int i=0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
            buttons[i].setText(allSuperHeroesList.get(i).getName());
            // now each button is set to the names of the first 4 in the shuffles list
        }

        buttons[rng.nextInt(buttons.length)].setText(correctSuperHero.getName());
    }


    public void makeGuess(View v) {

        Button clickedButton = (Button) v;

        currentSettings = (CurrentSettings) clickedButton.getTag();

        totalGuesses++;

        Button b = (Button) v;

        String guess = b.getText().toString();

        String answer="";

        // check which quiz type it is and set the correct answer appropriately
        if (currentSettings.getQuizType()== CurrentSettings.QuizType.NAMES) {
            answer = correctSuperHero.getName();
        }
        else if (currentSettings.getQuizType()==CurrentSettings.QuizType.ONE_THING) {
            answer = correctSuperHero.getThingToKnow();
        }
        else if (currentSettings.getQuizType()== CurrentSettings.QuizType.SUPERPOWERS) {
            answer = correctSuperHero.getSuperpower();
        }


        // if they're correct
        if (guess.equals(answer)) {
            correctGuesses++;
            // if the game isn't over
            if (correctGuesses<HEROES_IN_QUIZ) {
                // disable the buttons
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setEnabled(false);
                } // end disable buttons
                guessResultTV.setText(R.string.correct_answer);
                guessResultTV.setTextColor(getResources().getColor(R.color.correct_answer));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextHero();
                    }
                }, 2000);
            } // if game is over
            // else the game is over
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                double percentCorrect = (double) correctGuesses/totalGuesses*100;
                builder.setMessage(getString(R.string.results, totalGuesses, percentCorrect));
                builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetQuiz();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            } // end else game is over
        }
        // else they guess wrong
        else {

            totalGuesses++;
            guessResultTV.setText(R.string.incorrect_answer);
            guessResultTV.setTextColor(getResources().getColor(R.color.incorrect_answer));
            b.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim));

        } // end else they guess wrong



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        if (currentSettings==null)
            System.out.println("It's null in  'OnOptionsItemSelected'");

        settingsIntent.putExtra("settings", currentSettings);
        startActivity(settingsIntent);


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Intent getNewSettings = getIntent();
        currentSettings = getNewSettings.getParcelableExtra("settings");
        resetQuiz();
        super.onResume();
    }

}
