package com.example.administrador.myapplication.controllers.material;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.persistence.ServiceOrdersRepositoryUser;
import com.example.administrador.myapplication.util.AppUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material);

        final EditText txtLogin = AppUtil.get(findViewById(R.id.editTextLogin));
        final EditText txtPass = AppUtil.get(findViewById(R.id.editTextPass));

        txtPass.setTypeface(Typeface.DEFAULT);
        txtPass.setTransformationMethod(new PasswordTransformationMethod());

        final Button btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ServiceOrdersRepositoryUser.getInstance().login(txtLogin.getText().toString(), txtPass.getText().toString())) {
                    Toast.makeText(MainActivity.this, R.string.lbl_login_success, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, ServiceOrderListActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, R.string.lbl_login_no_success, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
