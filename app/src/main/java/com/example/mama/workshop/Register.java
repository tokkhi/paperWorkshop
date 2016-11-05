package com.example.mama.workshop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    private EditText eddisname;
    private EditText edReUser;
    private EditText edRepass;
    private EditText edReCon;
    private Button btnReRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eddisname = (EditText) findViewById(R.id.eddisname);
        edReUser = (EditText) findViewById(R.id.edReUser);
        edRepass = (EditText) findViewById(R.id.edRePass);
        edReCon = (EditText) findViewById(R.id.edReCon);
        btnReRegis = (Button) findViewById(R.id.btReRegis);

        btnReRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkededitText()) {
                    new Registerl(edReUser.getText().toString(),
                            edRepass.getText().toString(),
                            edReCon.getText().toString(),
                            eddisname.getText().toString()).execute();
                } else {
                    Toast.makeText(Register.this, "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean checkededitText() {
        String disname = eddisname.getText().toString();
        String username = edReUser.getText().toString();
        String pass = edRepass.getText().toString();
        String passcon = edReCon.getText().toString();

        if (username.isEmpty()) return false;

        if (pass.isEmpty()) return false;

        if (passcon.isEmpty()) return false;

        if (!pass.equals(passcon)) return false;

        if (disname.isEmpty()) return false;
        else

            return true;

    }

    private class Registerl extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;
        private String passwordCon;
        private String displayName;

        public Registerl(String username, String password, String passwordCon, String displayName) {
            this.username = username;
            this.password = password;
            this.passwordCon = passwordCon;
            this.displayName = displayName;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient Client = new OkHttpClient();
            Request request; //ส่งค่า
            Response response; //รับค่า

            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("password_con", passwordCon)
                    .add("display_name", displayName)
                    .build();

            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requestBody)
                    .build();

            try {

                response = Client.newCall(request).execute();

                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Register.this, "เรียบร้อยแล้วจ๊ะ", Toast.LENGTH_SHORT).show();
            try {
                JSONObject rootObj = new JSONObject(s);
                if (rootObj.has("result")) {
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    if (resultObj.getInt("result") == 1) {
                        Toast.makeText(Register.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Register.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException ex) {

            }
        }
    }
}