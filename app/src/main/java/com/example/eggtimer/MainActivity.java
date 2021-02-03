package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SeekBar hrsSeekBar, minsSeekBar, secsSeekBar;
    TextView timerDisplay;
    Button control;
    Button pause;
    Button resetTime;
    Button addMin;
    boolean counterOn = false;
    boolean paused = false;
    CountDownTimer countDownTimer;
    int hrs, mins, secs, hr, min, sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hrsSeekBar = findViewById(R.id.hrsSeekBar);
        minsSeekBar = findViewById(R.id.minsSeekBar);
        secsSeekBar = findViewById(R.id.secsSeekBar);
        timerDisplay = findViewById(R.id.timerDisplay);

        hrsSeekBar.setMax(99);
        hrsSeekBar.setProgress(0);

        minsSeekBar.setMax(59);
        minsSeekBar.setProgress(0);

        secsSeekBar.setMax(59);
        secsSeekBar.setProgress(0);

        control = findViewById(R.id.startstop);
        control.setText("Start");
        pause = findViewById(R.id.pause);
        resetTime = findViewById(R.id.resetButton);
        resetTime.setEnabled(false);
        pause.setText("Pause");
        pause.setEnabled(false);
        addMin = findViewById(R.id.plusOne);
        addMin.setVisibility(View.INVISIBLE);
        hrsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hrs = progress;
                dispUpdate(format(hrs), format(mins), format(secs));
                resetEnabler(hrs,mins,secs);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        minsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mins = progress;
                dispUpdate(format(hrs), format(mins), format(secs));
                resetEnabler(hrs,mins,secs);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        secsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                secs = progress;
                dispUpdate(format(hrs), format(mins), format(secs));
                resetEnabler(hrs,mins,secs);;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void control(View view){
        if (timerDisplay.getText().toString().equals("00 : 00 : 00")){
            Toast.makeText(this, "Time set has to be greater than 0 seconds!", Toast.LENGTH_SHORT).show();
        }
        else if (counterOn == false) {
            counterOn = true;
            addMin.setVisibility(View.VISIBLE);
            pause.setEnabled(true);
            hrsSeekBar.setEnabled(false);
            minsSeekBar.setEnabled(false);
            secsSeekBar.setEnabled(false);
            countDownTimer = new CountDownTimer((hrsSeekBar.getProgress()*3600 + minsSeekBar.getProgress()*60 + secsSeekBar.getProgress()) * 1000 + 200, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerUpdate((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    timerDisplay.setText("00 : 00 : 00");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.schoolbell);
                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_LONG).show();
                    reset();
                }
            }.start();
            control.setText("Stop");
        }else{
            reset();
        }
    }

    public void dispUpdate(String hrsDisp, String minsDisp, String secsDisp){
        timerDisplay.setText(hrsDisp + " : " + minsDisp + " : " + secsDisp);
    }

    public void timerUpdate(int progress){
        hr = (int) progress/3600;
        min = (int) (progress - hr*3600)/60;
        sec = progress - min*60 - hr*3600;

        dispUpdate(format(hr), format(min), format(sec));
    }

    public void reset(){
        dispUpdate(format(hrs), format(mins), format(secs));
        addMin.setVisibility(View.INVISIBLE);
        hrsSeekBar.setProgress(hrs);
        minsSeekBar.setProgress(mins);
        secsSeekBar.setProgress(secs);
        countDownTimer.cancel();
        control.setText("Start");
        counterOn = false;
        paused  = false;
        pause.setEnabled(false);
        pause.setText("Pause");
        hrsSeekBar.setEnabled(true);
        minsSeekBar.setEnabled(true);
        secsSeekBar.setEnabled(true);
    }

    public String format(int time){
        String timeDisp;
        if (time<10){
            timeDisp = "0" + time;
        }else{
            timeDisp = Integer.toString(time);
        }
        return timeDisp;
    }

    public void pause(View view){
        if (paused == false) {
            paused = true;
            countDownTimer.cancel();
            pause.setText("Continue");
        }else{
            countDownTimer = new CountDownTimer((hr * 3600 + min * 60 + sec) * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerUpdate((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    timerDisplay.setText("00 : 00 : 00");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.schoolbell);
                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_LONG).show();
                    reset();
                }
            }.start();
            paused = false;
            pause.setText("Pause");
        }
    }

    public void resetTimer(View view){
        timerDisplay.setText("00 : 00 : 00");
        hrsSeekBar.setProgress(0);
        minsSeekBar.setProgress(0);
        secsSeekBar.setProgress(0);
        if (counterOn) {
            countDownTimer.cancel();
        }
        control.setText("Start");
        addMin.setVisibility(View.INVISIBLE);
        counterOn = false;
        paused  = false;
        pause.setEnabled(false);
        pause.setText("Pause");
        hrsSeekBar.setEnabled(true);
        minsSeekBar.setEnabled(true);
        secsSeekBar.setEnabled(true);
        resetTime.setEnabled(false);
    }

    public void resetEnabler(int hrs, int mins, int secs){
        if (hrs == 0 && mins == 0 && secs == 0){
            resetTime.setEnabled(false);
        }else{
            resetTime.setEnabled(true);
        }
    }

    public void addMinute(View view){
        countDownTimer.cancel();
        min++;
        dispUpdate(format(hrs), format(mins), format(secs));
        countDownTimer = new CountDownTimer((hr * 3600 + min * 60 + sec) * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerUpdate((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timerDisplay.setText("00 : 00 : 00");
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.schoolbell);
                mediaPlayer.start();
                Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_LONG).show();
                reset();
            }
        }.start();
    }
}