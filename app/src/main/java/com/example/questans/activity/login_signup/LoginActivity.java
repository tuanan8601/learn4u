package com.example.questans.activity.login_signup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.model.User;
import com.example.questans.user.AccountManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView txtThongbao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.idEdtUserName);
        password = findViewById(R.id.idEdtPassword);
        txtThongbao = findViewById(R.id.txtThongBao);
    }
    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent,1);
    }

    public void login(View view) {
        if(username.getText().toString().equals("")||password.getText().toString().equals(""))
            txtThongbao.setText("Xin hãy điền đầy đủ thông tin!");
        else {
            AccountManager.login(username.getText().toString(), password.getText().toString(), new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user == null) {
                        txtThongbao.setText("Không tồn tại tài khoản như vậy!");
                    } else if (user.getPassword().equals(password.getText().toString())) {
                        AccountManager.setUid(user.getUid());
                        AccountManager.setDisplayName(user.getDisplayName());
                        System.out.println(user.getUid());
                        txtThongbao.setText("");
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Đăng nhập")
                                .setMessage("Bạn đã đăng nhập thành công!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        txtThongbao.setText("Sai mật khẩu!");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(AccountManager.islogged()) finish();
                break;
        }
    }
}