package com.example.questans.activity.login_signup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.activity.ProfileActivity;
import com.example.questans.data.ApiClient;
import com.example.questans.model.User;
import com.example.questans.user.AccountManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText txtFirstname;
    EditText txtLastname;
    EditText txtUsername;
    EditText txtPassword;
    EditText txtPassAgain;
    EditText txtPhone;
    TextView txtThongbao2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txtFirstname = findViewById(R.id.txtFirstName);
        txtLastname = findViewById(R.id.txtLastName);
        txtUsername = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtPassAgain = findViewById(R.id.txtPasswordAgain);
        txtPhone = findViewById(R.id.txtPhoneNum);
        txtThongbao2 = findViewById(R.id.txtThongBao2);
    }

    public void signup(View view) {
        if(txtUsername.getText().toString().equals("")||
                txtPassword.getText().toString().equals("")||
                txtLastname.getText().toString().equals("")||
                txtFirstname.getText().toString().equals("")||
                txtPassAgain.getText().toString().equals("")||
                txtPhone.getText().toString().equals(""))
            txtThongbao2.setText("Xin hãy điền đầy đủ thông tin!");
        else if(!txtPassAgain.getText().toString().equals(txtPassword.getText().toString())){
            txtThongbao2.setText("Mật khẩu nhập lại không đúng. Xin hãy nhập lại!");
        }
        else{
            ApiClient.getAPI().findUserbyUsername(txtUsername.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body()!=null){
                        txtThongbao2.setText("Tên đăng nhập đã tồn tại!");
                    }
                    else{
                        txtThongbao2.setText("");
                        User user = new User();
                        user.setDisplayName(""+txtLastname.getText()+" "+txtFirstname.getText());
                        user.setUsername(txtUsername.getText().toString());
                        user.setPassword(txtPassword.getText().toString());
                        user.setPhoneNum(txtPhone.getText().toString());
                        ApiClient.getAPI().addUser(user).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setTitle("Đăng kí")
                                        .setMessage("Bạn đã đăng kí tài khoản thành công!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                AccountManager.setUid(response.body());
                                                AccountManager.getUser(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        AccountManager.setDisplayName(response.body().getDisplayName());
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {

                                                    }
                                                });
                                                Intent intent = new Intent(SignupActivity.this, ProfileActivity.class);
                                                intent.putExtra("first_time",true);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}