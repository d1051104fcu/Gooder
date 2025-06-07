package com.example.gooder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gooder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_Email, edt_Name, edt_Password, edt_ConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edt_Email = findViewById(R.id.register_edt_email);
        edt_Name = findViewById(R.id.register_edt_name);
        edt_Password = findViewById(R.id.register_edt_password);
        edt_ConfirmPassword = findViewById(R.id.register_edt_ConfirmPassword);
        Button register = findViewById(R.id.register_btn_register);
        Button toLogin = findViewById(R.id.register_btn_toLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_Email.getText().toString().trim();
                String name = edt_Name.getText().toString().trim();
                String password = edt_Password.getText().toString().trim();
                String confirmPassword = edt_ConfirmPassword.getText().toString().trim();
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                if (!email.isEmpty() && !name.isEmpty() && !password.isEmpty()){
                    if (password.equals(confirmPassword)){
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            if (user != null){
                                                String uid = user.getUid();
                                                User userData = new User(email, name);
                                                db.collection("Users")
                                                        .document(uid)
                                                        .set(userData)
                                                        .addOnSuccessListener( a -> {
                                                            Log.i("User", "註冊成功！ " + uid);

                                                            SharedPreferences.Editor editor = prefs.edit();
                                                            editor.putBoolean("isLogin", true);
                                                            editor.apply();

                                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                            finish();
                                                        })
                                                        .addOnFailureListener( e -> {
                                                            Toast.makeText(RegisterActivity.this, "FireStore 寫入失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            Log.e("User", "註冊失敗！ " + e.getMessage());
                                                        });
                                            }
                                        }else {
                                            Toast.makeText(RegisterActivity.this, "註冊失敗" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(RegisterActivity.this, "確認密碼與密碼不同，請重新輸入", Toast.LENGTH_SHORT).show();
                        edt_ConfirmPassword.setText("");
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "輸入欄位不得為空", Toast.LENGTH_SHORT).show();
                }

            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();

                    // 隱藏鍵盤
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}