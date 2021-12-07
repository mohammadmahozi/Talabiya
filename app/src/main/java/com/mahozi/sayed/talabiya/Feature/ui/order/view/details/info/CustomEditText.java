package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

    private CustomEditTextInterface mCustomEditTextInterface;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            mCustomEditTextInterface.onBackButtonPressed();

            return true;
        }
        return false;
    }

    public void setCustomEditTextInterface(CustomEditTextInterface mCustomEditTextInterface) {
        this.mCustomEditTextInterface = mCustomEditTextInterface;
    }

    public interface CustomEditTextInterface{
        void onBackButtonPressed();
    }

}