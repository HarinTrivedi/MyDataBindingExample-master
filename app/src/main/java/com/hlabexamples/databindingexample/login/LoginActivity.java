package com.hlabexamples.databindingexample.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hlabexamples.databindingexample.R;
import com.hlabexamples.databindingexample.databinding.ActivityLoginBinding;
import com.hlabexamples.databindingexample.main.MainActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    //    ListPopupWindow listPopupWindow;
    ArrayList<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setListener(this);
        loginModel = new LoginModel();
        binding.setLoginModel(loginModel);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnLogin) {
                // accessing components
            if (TextUtils.isEmpty(binding.edtUsername.getText().toString()))
                Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
                // accessing via two-way binding
            else if (TextUtils.isEmpty(loginModel.getPassword()))
                Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            else {
                goToMainPage(loginModel.getUsername());
            }
        }
    }

    private void goToMainPage(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.arg_name), username);
        startActivity(intent);
        finish();
    }
}
