package edu.miracostacollege.superheroesquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.miracostacollege.superheroesquiz.Model.CurrentSettings;

public class SettingsActivity extends AppCompatActivity {


    private RadioButton names;
    private RadioButton oneThing;
    private RadioButton superpowers;
    private TextView currentQuizTypeTV;
    private CurrentSettings currentSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        names = findViewById(R.id.names);
        oneThing = findViewById(R.id.oneThing);
        superpowers = findViewById(R.id.superpowers);
        currentQuizTypeTV = findViewById(R.id.currentSetting);

        Intent fromMain = getIntent();
        currentSettings = fromMain.getExtras().getParcelable("settings");
        if (currentSettings == null)
            System.out.println("It's going null upon getting intent");

        if (currentSettings.getQuizType()==CurrentSettings.QuizType.NAMES) {
            names.setChecked(true);
            currentQuizTypeTV.setText(R.string.current_setting + R.string.name_quiz_setting);
        }
        else if (currentSettings.getQuizType()==CurrentSettings.QuizType.SUPERPOWERS) {
            oneThing.setChecked(true);
            currentQuizTypeTV.setText(R.string.current_setting + R.string.one_thing_quiz_setting);
        }
        else if (currentSettings.getQuizType()==CurrentSettings.QuizType.ONE_THING) {
            superpowers.setChecked(true);
            currentQuizTypeTV.setText(R.string.current_setting + R.string.superpower_quiz_setting);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }





    public void chooseSetting(View v) {

        Intent back = new Intent(this, MainActivity.class);

        RadioButton rb = (RadioButton) v;

        names.setChecked(false);
        oneThing.setChecked(false);
        superpowers.setChecked(false);
        rb.setChecked(true);


        Handler handler = new Handler();

        if (names.isChecked()) {
            currentSettings.setQuizType(CurrentSettings.QuizType.NAMES);
        } else if (oneThing.isChecked()) {
            currentSettings.setQuizType(CurrentSettings.QuizType.ONE_THING);
        }
        else if (superpowers.isChecked()) {
            currentSettings.setQuizType(CurrentSettings.QuizType.SUPERPOWERS);
        }

        back.putExtra("settings", currentSettings);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);


        startActivity(back);



        this.finish();
    }



}
