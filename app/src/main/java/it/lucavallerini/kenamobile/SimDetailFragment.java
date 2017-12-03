package it.lucavallerini.kenamobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.TextView;

public class SimDetailFragment extends Fragment {

    static final String SIM_DETAIL_FRAGMENT_TAG = "SIM_DETAIL_FRAGMENT";

    View mCodesLayout;
    View mSerialLayout;
    Button mShowCodesButton;
    Button mHideCodesButton;
    Button mShowSerialButton;
    Button mHideSerialButton;
    TextView mSerialText;

    View.OnClickListener mShowSerialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expand(mSerialLayout);
            mShowSerialButton.setVisibility(View.GONE);
            mSerialText.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener mHideSerialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            collapse(mSerialLayout);
            mSerialText.setVisibility(View.GONE);
            mShowSerialButton.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener mShowCodesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mShowCodesButton.setVisibility(View.GONE);
            expand(mCodesLayout);
        }
    };

    View.OnClickListener mHideCodesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            collapse(mCodesLayout);
            mShowCodesButton.setVisibility(View.VISIBLE);
        }
    };

    public static void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.setVisibility(View.VISIBLE);
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }
        };

        a.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density) * 2);
        view.startAnimation(a);
    }

    public static void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };

        a.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density) * 2);
        view.startAnimation(a);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return layoutInflater.inflate(R.layout.sim_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCodesLayout = view.findViewById(R.id.sim_codes_show_layout);
        mSerialLayout = view.findViewById(R.id.sim_serial_number_show_layout);

        mSerialText = view.findViewById(R.id.sim_serial_number);
        mShowCodesButton = view.findViewById(R.id.buttonShowCodes);
        mHideCodesButton = view.findViewById(R.id.buttonHideCodes);
        mShowSerialButton = view.findViewById(R.id.buttonShowSerial);
        mHideSerialButton = view.findViewById(R.id.buttonHideSerial);
        mSerialText = view.findViewById(R.id.sim_serial_number);

        mShowCodesButton.setOnClickListener(mShowCodesListener);
        mHideCodesButton.setOnClickListener(mHideCodesListener);
        mShowSerialButton.setOnClickListener(mShowSerialListener);
        mHideSerialButton.setOnClickListener(mHideSerialListener);
    }
}
