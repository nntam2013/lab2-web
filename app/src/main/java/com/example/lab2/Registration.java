package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Intent intent = getIntent();

    }

    public String generateMD5(String string) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(string.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xff)));
        }
        return sb.toString();
    }


    public void register(View view) {
        EditText userNameTextInput = findViewById(R.id.user_name_text_input);
        EditText pwdTextInput = findViewById(R.id.pwd_text_input);
        EditText emailTextInput = findViewById(R.id.email_text_input);
        String userName = userNameTextInput.getText().toString();
        String pwd = pwdTextInput.getText().toString();
        String email = emailTextInput.getText().toString();

        if (userName.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
            CharSequence message = "Please fill out all input";
            Context context = getApplicationContext();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return;
        }
        String hashedPwd = "";
        try {
            hashedPwd = this.generateMD5(pwd);
        } catch (NoSuchAlgorithmException e) {
            // Just enough
        }

        SQLiteConnector sqLiteConnector = new SQLiteConnector(Registration.this);
        User newUser = new User(userName, email, hashedPwd);
        sqLiteConnector.addUser(newUser);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
