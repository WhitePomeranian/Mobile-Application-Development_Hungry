package com.fcu.hungry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView tvRegisterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvRegisterLogin = findViewById(R.id.tv_register_login);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // 設置底線
                ds.setColor(Color.parseColor("#4BB2F9"));
            }
        };
        SpannableString spannableString = new SpannableString(tvRegisterLogin.getText().toString());
        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE); // 設置監聽器並包含頭尾兩字
        tvRegisterLogin.setText(spannableString);
        tvRegisterLogin.setMovementMethod(LinkMovementMethod.getInstance()); // 使TextView中的超連結能夠被點擊
    }
}