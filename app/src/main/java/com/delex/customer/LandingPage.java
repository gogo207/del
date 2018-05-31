package com.delex.customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delex.ParentActivity;

public class LandingPage extends ParentActivity {

    private Button btnSignin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        btnSignin=(Button)findViewById(R.id.btn_landing_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        tvSignup=(TextView)findViewById(R.id.btn_landing_signup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
