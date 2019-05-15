package com.psychotherapeutic_app.activities;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.psychotherapeutic_app.R;
import com.psychotherapeutic_app.utils.SizeUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener {

    private static final String[] sNumbers = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    };

    private ArrayAdapter<String> mAdapterForEmotions;
    private int[] mSadStates = new int[4];
    private int[] mHappyStates = new int[4];

    private RelativeLayout mMainLayout;

    private Spinner mEmotionSpinner;
    private Spinner mHeadSpinner;
    private Spinner mHeartSpinner;
    private Spinner mBodySpinner;

    private ImageView mMaskImageView;
    private Button mSwitchButton;

    private TextView mMaskTextView;
    private TextView mEmotionTextView;
    private TextView mStateTextView;
    private TextView mHeadTextView;
    private TextView mHeartTextView;
    private TextView mBodyTextView;
    private TextView mSwitchTextView;

    private boolean isHappyState = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = findViewById(R.id.mainLayout);

        mEmotionSpinner = findViewById(R.id.emotionSpinner);
        mHeadSpinner = findViewById(R.id.headStateSpinner);
        mHeartSpinner = findViewById(R.id.heartStateSpinner);
        mBodySpinner = findViewById(R.id.bodyStateSpinner);

        mMaskImageView = findViewById(R.id.maskImageView);
        mSwitchButton = findViewById(R.id.switchButton);

        mMaskTextView = findViewById(R.id.maskTextView);
        mEmotionTextView = findViewById(R.id.emotionTextView);
        mStateTextView = findViewById(R.id.stateTextView);
        mHeadTextView = findViewById(R.id.headStateTextView);
        mHeartTextView = findViewById(R.id.heartStateTextView);
        mBodyTextView = findViewById(R.id.bodyStateTextView);
        mSwitchTextView = findViewById(R.id.switchTextView);

        final String[] emotionSpinnerItems = getResources().getStringArray(R.array.negative_spinner_items_ru);

        mAdapterForEmotions = new ArrayAdapter<>(this,
                R.layout.spinner_item, new ArrayList<>(Arrays.asList(emotionSpinnerItems)));

        ArrayAdapter<String> adapterForNumbers = new ArrayAdapter<>(this,
                R.layout.spinner_item, sNumbers);

        mAdapterForEmotions.setDropDownViewResource(R.layout.spinner_item);
        adapterForNumbers.setDropDownViewResource(R.layout.spinner_item);

        mEmotionSpinner.setAdapter(mAdapterForEmotions);
        mHeadSpinner.setAdapter(adapterForNumbers);
        mHeartSpinner.setAdapter(adapterForNumbers);
        mBodySpinner.setAdapter(adapterForNumbers);

        mSwitchButton.setOnTouchListener(this);
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
        if (view.getId() == R.id.switchButton) {
            switchState();
        }
    }


    private void adjust() {
        final WindowManager windowManager = getWindowManager();
        if (windowManager == null) return;
        SizeUtil.initScreenSize(getWindowManager());

        final float textSize18 = SizeUtil.getTextSize(windowManager, 18);
        final float textSize20 = SizeUtil.getTextSize(windowManager, 20);
        final float textSize22 = SizeUtil.getTextSize(windowManager, 22);

        ViewGroup.LayoutParams maskLayoutParams = findViewById(R.id.maskLayout).getLayoutParams();
        RelativeLayout.LayoutParams maskImageViewParams =
                (RelativeLayout.LayoutParams) mMaskImageView.getLayoutParams();
        RelativeLayout.LayoutParams maskTextViewParams =
                (RelativeLayout.LayoutParams) mMaskTextView.getLayoutParams();

        ViewGroup.LayoutParams emotionLayoutParams = findViewById(R.id.emotionLayout).getLayoutParams();
        RelativeLayout.LayoutParams emotionImageViewParams =
                (RelativeLayout.LayoutParams) findViewById(R.id.emotionImageView).getLayoutParams();
        RelativeLayout.LayoutParams emotionSpinnerParams =
                (RelativeLayout.LayoutParams) mEmotionSpinner.getLayoutParams();

        ViewGroup.LayoutParams stateTextViewParams = mStateTextView.getLayoutParams();
        ViewGroup.LayoutParams statesLayoutParams = findViewById(R.id.statesLayout).getLayoutParams();
        ViewGroup.LayoutParams headSpinnerParams = mHeadSpinner.getLayoutParams();
        ViewGroup.LayoutParams heartSpinnerParams = mHeartSpinner.getLayoutParams();
        ViewGroup.LayoutParams bodySpinnerParams = mBodySpinner.getLayoutParams();

        RelativeLayout.LayoutParams switchButtonParams =
                (RelativeLayout.LayoutParams) mSwitchButton.getLayoutParams();


        maskLayoutParams.height = SizeUtil.relativeH(20);

        maskImageViewParams.width = SizeUtil.relativeW(29);
        maskImageViewParams.height = (int) (1.084 * maskImageViewParams.width);
        maskImageViewParams.leftMargin = maskImageViewParams.topMargin =
                (int) (0.167 * maskImageViewParams.width);

        maskTextViewParams.leftMargin = (int) (-0.25 * maskImageViewParams.width);
        mMaskTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize22);

        emotionLayoutParams.height = SizeUtil.relativeH(16.43);

        mEmotionTextView.setPadding(
                (int) (0.084 * maskImageViewParams.width),
                0,
                (int) (0.084 * maskImageViewParams.width),
                0
        );
        mEmotionTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize22);

        emotionImageViewParams.width = (int) (0.767 * maskImageViewParams.width);
        emotionImageViewParams.height = (int) (1.087 * emotionImageViewParams.width);
        emotionImageViewParams.rightMargin = (int) (0.084 * maskImageViewParams.width);

        emotionSpinnerParams.width = SizeUtil.relativeW(60);
        emotionSpinnerParams.height = (int) (emotionSpinnerParams.width / 6.279);
        emotionSpinnerParams.leftMargin = (int) (0.074 * emotionSpinnerParams.width);

        stateTextViewParams.height = SizeUtil.relativeH(19);
        mStateTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize20);

        statesLayoutParams.height = SizeUtil.relativeH(21);

        mHeadTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize18);
        mHeartTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize18);
        mBodyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize18);

        headSpinnerParams.width = heartSpinnerParams.width = bodySpinnerParams.width =
                SizeUtil.relativeW(28);

        mSwitchTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize18);

        switchButtonParams.width = switchButtonParams.height = SizeUtil.relativeW(12.14);
        switchButtonParams.rightMargin = (int) (0.4 * switchButtonParams.width);
    }

    private void switchState() {
        isHappyState = !isHappyState;

        mAdapterForEmotions.clear();
        mAdapterForEmotions.addAll(getResources().getStringArray(isHappyState ?
                R.array.positive_spinner_items_ru : R.array.negative_spinner_items_ru));
        mAdapterForEmotions.notifyDataSetChanged();

        if (isHappyState) {
            int res = mHeadSpinner.getSelectedItemPosition()
                    + mHeartSpinner.getSelectedItemPosition()
                    + mBodySpinner.getSelectedItemPosition();
            if (res <= 6) {
                // TODO: 5/15/19 completed state
            }

            mMainLayout.setBackgroundResource(R.color.happyBgColor);
            mMaskImageView.setBackgroundResource(R.drawable.mask_happy);
            mMaskTextView.setText(getString(R.string.positive_text_ru));
            mSwitchButton.setBackgroundResource(R.drawable.arrow_left);
            mSwitchTextView.setText(getString(R.string.switch_happy_text_ru));

            mSadStates[0] = mEmotionSpinner.getSelectedItemPosition();
            mSadStates[1] = mHeadSpinner.getSelectedItemPosition();
            mSadStates[2] = mHeartSpinner.getSelectedItemPosition();
            mSadStates[3] = mBodySpinner.getSelectedItemPosition();
            mEmotionSpinner.setSelection(mHappyStates[0]);
            mHeadSpinner.setSelection(mHappyStates[1]);
            mHeartSpinner.setSelection(mHappyStates[2]);
            mBodySpinner.setSelection(mHappyStates[3]);
        } else {
            mMainLayout.setBackgroundResource(R.color.sadBgColor);
            mMaskImageView.setBackgroundResource(R.drawable.mask_sad);
            mMaskTextView.setText(getString(R.string.negative_text_ru));
            mSwitchButton.setBackgroundResource(R.drawable.arrow_right);
            mSwitchTextView.setText(getString(R.string.switch_sad_text_ru));

            mHappyStates[0] = mEmotionSpinner.getSelectedItemPosition();
            mHappyStates[1] = mHeadSpinner.getSelectedItemPosition();
            mHappyStates[2] = mHeartSpinner.getSelectedItemPosition();
            mHappyStates[3] = mBodySpinner.getSelectedItemPosition();
            mEmotionSpinner.setSelection(mSadStates[0]);
            mHeadSpinner.setSelection(mSadStates[1]);
            mHeartSpinner.setSelection(mSadStates[2]);
            mBodySpinner.setSelection(mSadStates[3]);
        }
    }

}
