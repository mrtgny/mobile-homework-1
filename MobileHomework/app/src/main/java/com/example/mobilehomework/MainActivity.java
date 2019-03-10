package com.example.mobilehomework;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int randomNumber1;
    private int randomNumber2;
    private Button button1;
    private Button button2;
    int point = 0;
    int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.rndm1);
        button2 = findViewById(R.id.rndm2);

        this.initGame();
    }

    private void initGame() {
        point = 0;
        level = 1;
        updateHighScore();
        this.nextLevel();
    }

    private void updateHighScore() {
        String _level = readFromFile(this);
        String level = "0";
        if (!_level.equals(""))
            level = _level;
        TextView levelText = findViewById(R.id.highScore);
        levelText.setText("High Score: " + level);
    }

    private void nextLevel() {
        level = (point / 5) + 1;
        point++;
        generateRandomNumbers(level);
        setRandomNumbers();
        setPointText(point);
        setLevelText(level);
    }

    private void setPointText(int point) {
        TextView levelText = findViewById(R.id.point);
        levelText.setText(point + "");
    }

    private void setLevelText(int level) {
        TextView levelText = findViewById(R.id.level);
        levelText.setText("Level: " + level);
    }

    private void generateRandomNumbers(int level) {
        Random r = new Random();
        int low;
        int high;
        switch (level) {
            case 1:
                low = 0;
                high = 10;
                break;
            case 2:
                low = 0;
                high = 20;
                break;
            case 3:
                low = 0;
                high = 30;
                break;
            case 4:
                low = 0;
                high = 40;
                break;
            case 5:
                low = -40;
                high = 40;
                break;
            case 6:
                low = -60;
                high = 60;
                break;
            case 7:
                low = -100;
                high = 100;
                break;
            case 8:
                low = -100;
                high = 100;
                break;
            case 9:
                low = -100;
                high = 100;
                break;
            default:
                low = -100;
                high = 100;
                break;
        }
        randomNumber1 = r.nextInt(high - low) + low;
        int secondRandomNumber = r.nextInt(high - low) + low;
        while (secondRandomNumber == randomNumber1)
            secondRandomNumber = r.nextInt(high - low) + low;
        randomNumber2 = secondRandomNumber;
    }


    private void setRandomNumbers() {
        button1.setText(String.valueOf(randomNumber1));
        button2.setText(String.valueOf(randomNumber2));
    }

    private void compareNumbers(int bigger, int smaller) {
        if (bigger > smaller) {
            this.setHighScore();
            this.nextLevel();
        } else {
            this.onGameOver();
        }
    }

    public void btn1Clicked(View v) {
        this.compareNumbers(randomNumber1, randomNumber2);
    }

    public void btn2Clicked(View v) {
        this.compareNumbers(randomNumber2, randomNumber1);
    }

    private void setHighScore() {
        String lastHighScore = readFromFile(this);
        if (!lastHighScore.equals("")) {
            if (Integer.parseInt(lastHighScore) < point) {
                writeToFile(String.valueOf(point), this);
            }
        } else {
            writeToFile(String.valueOf(point), this);
        }
        updateHighScore();
    }


    private void writeToFile(String data, Context context) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("highscore.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "0";

        try {
            InputStream inputStream = context.openFileInput("highscore.txt");
            ;

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        Log.i("FileOutput", ret);
        return ret;
    }

    private void onGameOver() {
        int highScore = Integer.parseInt(readFromFile(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("High Score: " + highScore + "\nCurrent Score: " + (point - 1) + "\nCurrent Level: " + level);
        builder.setCancelable(false);

        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Tamam butonuna basılınca yapılacaklar
                initGame();
            }
        });


        builder.show();
    }

}
