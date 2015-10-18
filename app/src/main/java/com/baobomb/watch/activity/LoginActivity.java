package com.baobomb.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baobomb.watch.R;
import com.baobomb.watch.parse.ParseUtil;
import com.baobomb.watch.util.dialog.Progress;
import com.parse.ParseUser;

/**
 * Created by baobomb on 2015/10/17.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    EditText login_email, login_password, login_userName;
    Button login, signUp;
    Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_userName = (EditText) findViewById(R.id.login_userName);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);
        progress = new Progress();
        progress.bind(this);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                if (!login_userName.getText().toString().equals("") && !login_email.getText()
                        .toString().equals("") && !login_password.getText().toString().equals("")) {
                    progress.showProgress("註冊", "註冊中...");
                    ParseUtil.signUp(login_userName.getText().toString(), login_password.getText().toString(),
                            login_email.getText().toString(),
                            new ParseUtil.OnSignUpListener() {
                                @Override
                                public void onFinish() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    progress.dismiss();
                                }

                                @Override
                                public void onFail() {
                                    progress.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "請確認資料", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login:
                if (!login_userName.getText().toString().equals("") && !login_password.getText().toString().equals("")) {
                    progress.showProgress("登入", "登入中...");
                    ParseUtil.login(login_userName.getText().toString(), login_password.getText().toString(),
                            new ParseUtil.OnLoginListener() {
                                @Override
                                public void onFinish() {
                                    progress.dismiss();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }

                                @Override
                                public void onFail() {
                                    progress.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "請確認資料", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
