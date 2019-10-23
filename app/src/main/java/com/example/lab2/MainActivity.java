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

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.lab2.REGISTRATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void signIn(View view) {
        EditText usernameTextInput = findViewById(R.id.user_name_text_input);
        String username = usernameTextInput.getText().toString();

        EditText pwdTextInput = findViewById(R.id.pwd_text_input);
        String pwd = pwdTextInput.getText().toString();
        SQLiteConnector sqLiteConnector = new SQLiteConnector(MainActivity.this);
        String hashedPwd = "";
        try {
            hashedPwd = this.generateMD5(pwd);
        } catch (NoSuchAlgorithmException e) {
            // Just enough
        }
        Boolean isAuthenticated = sqLiteConnector.checkUser(username, hashedPwd);

        if (isAuthenticated) {
            Intent intent = new Intent(this, SampleApplication.class);
            String message = "Welcome " + username;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            return;
        }
        Context context = getApplicationContext();
        CharSequence message = "Wrong user name or password";
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        return;
    }

    public void register(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}
