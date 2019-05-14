package com.psychotherapeutic_app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psychotherapeutic_app.R;
import com.psychotherapeutic_app.dialogs.InfoDialog;
import com.psychotherapeutic_app.dialogs.SettingsDialog;
import com.psychotherapeutic_app.utils.SizeUtil;

public class MenuActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {

    private TextView mDescriptionTextView;
    private Button mInfoButton;
    private Button mStartButton;
    private Button mSettingsButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mDescriptionTextView = findViewById(R.id.descriptionTextView);
        mInfoButton = findViewById(R.id.infoButton);
        mStartButton = findViewById(R.id.startButton);
        mSettingsButton = findViewById(R.id.settingsButton);

        mInfoButton.setOnTouchListener(this);
        mStartButton.setOnTouchListener(this);
        mSettingsButton.setOnTouchListener(this);

        adjust();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        final int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            Animation bounceIn = AnimationUtils.loadAnimation(this, R.anim.bounce_in);
            v.startAnimation(bounceIn);
        } else if (action == MotionEvent.ACTION_UP) {
            Animation bounceOut = AnimationUtils.loadAnimation(this, R.anim.bounce_out);
            v.startAnimation(bounceOut);

            final Rect rect = new Rect();
            v.getDrawingRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                onClick(v);
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoButton:
                InfoDialog infoDialog = new InfoDialog();
                infoDialog.show(getSupportFragmentManager(), "InfoDialog");
                break;

            case R.id.startButton:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.settingsButton:
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(getSupportFragmentManager(), "SettingsDialog");
                break;
        }
    }


    private void adjust() {
        final WindowManager windowManager = getWindowManager();
        if (windowManager == null) return;
        SizeUtil.initScreenSize(getWindowManager());

        final float textSize22 = SizeUtil.getTextSize(windowManager, 22);
        final float textSize24 = SizeUtil.getTextSize(windowManager, 24);

        RelativeLayout.LayoutParams logoParams =
                (RelativeLayout.LayoutParams) findViewById(R.id.logoImageView).getLayoutParams();
        ViewGroup.LayoutParams descriptionParams = mDescriptionTextView.getLayoutParams();
        ViewGroup.LayoutParams bottomLayoutParams = findViewById(R.id.bottomLayout).getLayoutParams();
        RelativeLayout.LayoutParams infoButtonParams =
                (RelativeLayout.LayoutParams) mInfoButton.getLayoutParams();
        RelativeLayout.LayoutParams startButtonParams =
                (RelativeLayout.LayoutParams) mStartButton.getLayoutParams();
        RelativeLayout.LayoutParams settingsButtonParams =
                (RelativeLayout.LayoutParams) mSettingsButton.getLayoutParams();

        logoParams.width = SizeUtil.relativeW(92.2);
        logoParams.height = (int) (logoParams.width / 1.63);
        logoParams.topMargin = (int) (0.048 * logoParams.height);

        descriptionParams.width = logoParams.width;
        mDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize24);

        bottomLayoutParams.height = (int) (0.21 * logoParams.width);

        infoButtonParams.width = infoButtonParams.height = (int) (0.13 * logoParams.width);
        infoButtonParams.rightMargin = (int) (0.5 * infoButtonParams.width);
        settingsButtonParams.width = settingsButtonParams.height = infoButtonParams.height;
        settingsButtonParams.leftMargin = infoButtonParams.rightMargin;

        startButtonParams.width = (int) (0.553 * logoParams.width);
        startButtonParams.height = (int) (startButtonParams.width / 3.846);
        mStartButton.setTextSize(textSize22);
        mStartButton.setPadding(0, 0, 0, (int) (0.14 * startButtonParams.height));
    }

}
