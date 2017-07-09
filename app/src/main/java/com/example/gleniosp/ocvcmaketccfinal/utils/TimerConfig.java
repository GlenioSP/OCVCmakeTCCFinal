package com.example.gleniosp.ocvcmaketccfinal.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.R;

public class TimerConfig extends Activity {

    private TextView tvTimerDialogMinutesTitle;
    private ImageButton imgBtnTimerDialogMinutesArrowUp;
    private EditText edtTimerDialogMinutesSetBox;
    private ImageButton imgBtnTimerDialogMinutesArrowDown;

    private TextView tvTimerDialogSecondsTitle;
    private ImageButton imgBtnTimerDialogSecondsArrowUp;
    private EditText edtTimerDialogSecondsSetBox;
    private ImageButton imgBtnTimerDialogSecondsArrowDown;

    private TextView tvTimerDialogIntervalTitle;
    private ImageButton imgBtnTimerDialogIntervalArrowUp;
    private EditText edtTimerDialogIntervalSetBox;
    private ImageButton imgBtnTimerDialogIntervalArrowDown;

    private Button btnTimerDialogSet;

    private final int MAX_MINUTES = 5;
    private final int MIN_MINUTES = 0;

    private final int MAX_SECONDS = 60;
    private final int MIN_SECONDS = 1;

    private final int MIN_INTERVAL = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_timer_config);

        setResult(Activity.RESULT_CANCELED);

        getWidgetReferences();
        bindEventHandler();
        initializeValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getWidgetReferences() {
        tvTimerDialogMinutesTitle = (TextView) findViewById(R.id.tvTimerDialogMinutesTitle);
        imgBtnTimerDialogMinutesArrowUp = (ImageButton) findViewById(R.id.imgBtnTimerDialogMinutesArrowUp);
        edtTimerDialogMinutesSetBox = (EditText) findViewById(R.id.edtTimerDialogMinutesSetBox);
        imgBtnTimerDialogMinutesArrowDown = (ImageButton) findViewById(R.id.imgBtnTimerDialogMinutesArrowDown);

        tvTimerDialogSecondsTitle = (TextView) findViewById(R.id.tvTimerDialogSecondsTitle);
        imgBtnTimerDialogSecondsArrowUp = (ImageButton) findViewById(R.id.imgBtnTimerDialogSecondsArrowUp);
        edtTimerDialogSecondsSetBox = (EditText) findViewById(R.id.edtTimerDialogSecondsSetBox);
        imgBtnTimerDialogSecondsArrowDown = (ImageButton) findViewById(R.id.imgBtnTimerDialogSecondsArrowDown);

        tvTimerDialogIntervalTitle = (TextView) findViewById(R.id.tvTimerDialogIntervalTitle);
        imgBtnTimerDialogIntervalArrowUp = (ImageButton) findViewById(R.id.imgBtnTimerDialogIntervalArrowUp);
        edtTimerDialogIntervalSetBox = (EditText) findViewById(R.id.edtTimerDialogIntervalSetBox);
        imgBtnTimerDialogIntervalArrowDown = (ImageButton) findViewById(R.id.imgBtnTimerDialogIntervalArrowDown);

        btnTimerDialogSet = (Button) findViewById(R.id.btnTimerDialogSet);

    }

    private void bindEventHandler() {

        imgBtnTimerDialogMinutesArrowUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());

                if (minutes < MAX_MINUTES) {
                    minutes++;
                    edtTimerDialogMinutesSetBox.setText(String.valueOf(minutes));
                }
            }
        });

        edtTimerDialogMinutesSetBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());

                if (minutes < MIN_MINUTES) {
                    edtTimerDialogMinutesSetBox.setText(String.valueOf(MIN_MINUTES));
                }
                else if (minutes > MAX_MINUTES) {
                    edtTimerDialogMinutesSetBox.setText(String.valueOf(MAX_MINUTES));
                }
                else {
                    edtTimerDialogMinutesSetBox.setText(String.valueOf(minutes));
                }
            }
        });

        imgBtnTimerDialogMinutesArrowDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());

                if (minutes > MIN_MINUTES) {
                    minutes--;
                    edtTimerDialogMinutesSetBox.setText(String.valueOf(minutes));
                }
            }
        });

        imgBtnTimerDialogSecondsArrowUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                if (seconds < MAX_SECONDS) {
                    seconds++;
                    edtTimerDialogSecondsSetBox.setText(String.valueOf(seconds));
                }
            }
        });

        edtTimerDialogSecondsSetBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                if (seconds < MIN_SECONDS) {
                    edtTimerDialogSecondsSetBox.setText(String.valueOf(MIN_SECONDS));
                }
                else if (seconds > MAX_SECONDS) {
                    edtTimerDialogSecondsSetBox.setText(String.valueOf(MAX_SECONDS));
                }
                else {
                    edtTimerDialogSecondsSetBox.setText(String.valueOf(seconds));
                }
            }
        });

        imgBtnTimerDialogSecondsArrowDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                if (seconds > MIN_SECONDS) {
                    seconds--;
                    edtTimerDialogSecondsSetBox.setText(String.valueOf(seconds));
                }
            }
        });

        imgBtnTimerDialogIntervalArrowUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int interval = Integer.parseInt(edtTimerDialogIntervalSetBox.getText().toString());

                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());
                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                final int MAX_INTERVAL = minutes*60 + seconds;

                if (interval < MAX_INTERVAL) {
                    interval++;
                    edtTimerDialogIntervalSetBox.setText(String.valueOf(interval));
                }
            }
        });

        edtTimerDialogIntervalSetBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int interval = Integer.parseInt(edtTimerDialogIntervalSetBox.getText().toString());

                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());
                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                final int MAX_INTERVAL = minutes*60 + seconds;

                if (interval < MIN_MINUTES) {
                    edtTimerDialogIntervalSetBox.setText(String.valueOf(MIN_INTERVAL));
                }
                else if (interval > MAX_MINUTES) {
                    edtTimerDialogIntervalSetBox.setText(String.valueOf(MAX_INTERVAL));
                }
                else {
                    edtTimerDialogIntervalSetBox.setText(String.valueOf(interval));
                }
            }
        });

        imgBtnTimerDialogIntervalArrowDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                int interval = Integer.parseInt(edtTimerDialogIntervalSetBox.getText().toString());

                if (interval > MIN_INTERVAL) {
                    interval--;
                    edtTimerDialogIntervalSetBox.setText(String.valueOf(interval));
                }
            }
        });

        btnTimerDialogSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                checkEmptyField();

                long totalTimeInMilli;
                long intervalInMilli;

                int interval = Integer.parseInt(edtTimerDialogIntervalSetBox.getText().toString());
                int minutes = Integer.parseInt(edtTimerDialogMinutesSetBox.getText().toString());
                int seconds = Integer.parseInt(edtTimerDialogSecondsSetBox.getText().toString());

                totalTimeInMilli = minutes*60*1000;

                totalTimeInMilli = totalTimeInMilli + seconds*1000;

                intervalInMilli = interval*1000;

                Intent intent = new Intent();
                intent.putExtra("TOTALTIME", String.valueOf(totalTimeInMilli));
                intent.putExtra("INTERVAL", String.valueOf(intervalInMilli));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initializeValues() {

        edtTimerDialogMinutesSetBox.setText(String.valueOf(MIN_MINUTES));
        edtTimerDialogMinutesSetBox.setSelection(1);

        edtTimerDialogSecondsSetBox.setText(String.valueOf(MIN_SECONDS));
        edtTimerDialogSecondsSetBox.setSelection(1);

        edtTimerDialogIntervalSetBox.setText(String.valueOf(MIN_INTERVAL));
        edtTimerDialogIntervalSetBox.setSelection(1);
    }

    private void checkEmptyField() {
        if (edtTimerDialogMinutesSetBox.getText().toString().isEmpty()) {
            edtTimerDialogMinutesSetBox.setText(String.valueOf(MIN_MINUTES));
        }
        if (edtTimerDialogSecondsSetBox.getText().toString().isEmpty()) {
            edtTimerDialogSecondsSetBox.setText(String.valueOf(MIN_SECONDS));
        }
        if (edtTimerDialogIntervalSetBox.getText().toString().isEmpty()) {
            edtTimerDialogIntervalSetBox.setText(String.valueOf(MIN_INTERVAL));
        }
    }

}