package com.example.questans.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.activity.login_signup.SignupActivity;
import com.example.questans.activity.result.UserTestResultActivity;
import com.example.questans.data.ApiClient;
import com.example.questans.model.User;
import com.example.questans.user.AccountManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Menu mainMenu;
    BottomNavigationView bottomNavigationView;

    EditText txtDisplayName, txtEmail, txtUni, txtPhone;
    TextView txtDoB;
    ImageButton btnDiplayName, btnEmail, btnDoB, btnUni, btnPhone;
    Button btnEdit;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!AccountManager.islogged()) finish();

        txtDisplayName = findViewById(R.id.txtDisplayName);
        txtEmail = findViewById(R.id.txtEmail);
        txtDoB = findViewById(R.id.txtDoB);
        txtUni = findViewById(R.id.txtUni);
        txtPhone = findViewById(R.id.txtPhoneNum);

        btnDiplayName = findViewById(R.id.btnDN);
        btnEmail = findViewById(R.id.btnEmail);
        btnDoB = findViewById(R.id.btnDoB);
        btnUni = findViewById(R.id.btnUni);
        btnPhone = findViewById(R.id.btnPhone);

        btnEdit = findViewById(R.id.btnEdit);

        AccountManager.getUser(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user.getDisplayName() != null)
                    txtDisplayName.setText(user.getDisplayName());
                if (user.getEmail() != null)
                    txtEmail.setText(user.getEmail());
                if (user.getDateOB() != null) {
                    String strDate = new SimpleDateFormat("dd/MM/yyyy").format(user.getDateOB());
                    System.out.println(strDate);
                    txtDoB.setText(strDate);
                }
                if (user.getSchool() != null)
                    txtUni.setText(user.getSchool());
                if (user.getPhoneNum() != null)
                    txtPhone.setText(user.getPhoneNum());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        if (!getIntent().getBooleanExtra("first_time", false)) {
            txtDisplayName.setEnabled(false);
            txtEmail.setEnabled(false);
            txtDoB.setEnabled(false);
            txtUni.setEnabled(false);
            txtPhone.setEnabled(false);
        }

        btnDiplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDisplayName.setEnabled(true);
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.setEnabled(true);
            }
        });
        btnDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDoB.setEnabled(true);
            }
        });
        btnUni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUni.setEnabled(true);
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPhone.setEnabled(true);
            }
        });



        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
                txtDoB.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        txtDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    myCalendar.setTime(sdf.parse(txtDoB.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (txtDoB.isEnabled())
                    new DatePickerDialog(ProfileActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        bottomNavigationView = findViewById(R.id.bottomNav);
        setUserDisplayName();
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.action_history:
                        startActivity(new Intent(getApplicationContext(), UserTestResultActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.action_profile:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        mainMenu = menu;
        MenuItem item = mainMenu.findItem(R.id.action_login);
        if (AccountManager.islogged()) {
            item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
        }
        setUserDisplayName();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            if (AccountManager.islogged()) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AccountManager.logout();
                                item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUserDisplayName() {
        if (AccountManager.islogged())
            bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle(AccountManager.getDisplayName());
    }

    public void editProfile(View view) {
        if (txtDisplayName.isEnabled() || txtEmail.isEnabled() || txtPhone.isEnabled() || txtUni.isEnabled() || txtDoB.isEnabled()) {
            System.out.println("Send!");

            AccountManager.getUser(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();

                    user.setDisplayName(txtDisplayName.getText().toString());
                    user.setEmail(txtEmail.getText().toString());

                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(txtDoB.getText().toString());
                        user.setDateOB(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    user.setSchool(txtUni.getText().toString());
                    user.setPhoneNum(txtPhone.getText().toString());

                    ApiClient.getAPI().editUser(user.getUid(), user).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("Lưu thông tin")
                                    .setMessage("Bạn đã lưu thông tin cá nhân thành công!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            AccountManager.setDisplayName(user.getDisplayName());
                                            setUserDisplayName();
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
            txtDisplayName.setEnabled(false);
            txtEmail.setEnabled(false);
            txtDoB.setEnabled(false);
            txtUni.setEnabled(false);
            txtPhone.setEnabled(false);
        }
    }
}