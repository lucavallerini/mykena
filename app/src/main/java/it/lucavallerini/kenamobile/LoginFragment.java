package it.lucavallerini.kenamobile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class LoginFragment extends Fragment {

    private OnRechargeButtonClickListener mRechargeButtonListener;

    private Button mRechargeButton;
    private Button mLoginButton;
    private Button mClearEntries;
    private TextView mUsernameTextView;
    private TextView mPasswordTextView;
    private CheckBox mRememberMeCheckBox;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return layoutInflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUsernameTextView = view.findViewById(R.id.usernameEditText);
        mPasswordTextView = view.findViewById(R.id.passwordEditText);
        mRememberMeCheckBox = view.findViewById(R.id.rememberMe);

        mRechargeButton = view.findViewById(R.id.rechargeButton);
        mRechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRechargeButtonListener.onRechargeButtonClick();
            }
        });

        mClearEntries = view.findViewById(R.id.clearButton);
        mClearEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEntries();
            }
        });

        mLoginButton = view.findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO login action
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRechargeButtonListener = (OnRechargeButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRechargeButtonClickListener");
        }
    }

    /**
     * Clear all login form entries.
     */
    private void clearEntries() {
        if (mUsernameTextView != null) {
            mUsernameTextView.setText("");
            mUsernameTextView.requestFocus();
        }
        
        if (mPasswordTextView != null) {
            mPasswordTextView.setText("");
        }

        if (mRememberMeCheckBox != null) {
            mRememberMeCheckBox.setChecked(false);
        }
    }

    /**
     * Interface that the host activity needs to implement in
     * order to receive a callback when the Recharge button
     * is clicked.
     */
    public interface OnRechargeButtonClickListener {
        void onRechargeButtonClick();
    }
}
