package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by JK on 2015. 11. 30..
 */
public class LoginActivity extends Activity
{
    RadioButton adminRadioButton,staffRadioButton,customerRadioButton;
    Button searchButton,enrollButton,loginButton;
    AutoCompleteTextView autoSearch;
    ArrayList<String> restaurant=new ArrayList<>();
    ResList resList = new ResList();
    String resname,_id,_passwd;
    Boolean loginCheck = false;
    ArrayList<ListItem> listItem = new ArrayList<>();
    EditText id,passwd;
    Caesar caesar = new Caesar();
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.memberId);
        passwd=(EditText)findViewById(R.id.memberPasswd);
        loginCheck = false;
        resList.execute("http://203.249.22.53:8080/reslist.php");

        adminRadioButton=(RadioButton)findViewById(R.id.administratorRadioButton);
        staffRadioButton=(RadioButton)findViewById(R.id.staffRadioButton);
        customerRadioButton=(RadioButton)findViewById(R.id.customerRadioButton);

        //searchButton=(Button)findViewById(R.id.searchButton);
        enrollButton=(Button)findViewById(R.id.enrollButton);
        loginButton=(Button)findViewById(R.id.loginButton);

        autoSearch=(AutoCompleteTextView)findViewById(R.id.autoSearchText);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,restaurant);
        autoSearch.setAdapter(adapter);
        autoSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.d("TXT", (String) arg0.getItemAtPosition(arg2));
                resname = (String) arg0.getItemAtPosition(arg2);
            }
        });




    }

    public void radioClick(View view) {
        switch(view.getId())
        {
            case R.id.administratorRadioButton:
            {
                //adminRadioButton.setChecked(true);
                if(adminRadioButton.isChecked()) {
                    staffRadioButton.setChecked(false);
                    customerRadioButton.setChecked(false);
                    enrollButton.setVisibility(View.VISIBLE);
                }
               // staffRadioButton.setChecked(true);
                //customerRadioButton.setChecked(true);

            }
            case R.id.staffRadioButton:
            {
                //staffRadioButton.setChecked(true);
                if(staffRadioButton.isChecked()) {
                    adminRadioButton.setChecked(false);
                    customerRadioButton.setChecked(false);
                    enrollButton.setVisibility(View.GONE);
                }
                //adminRadioButton.setChecked(true);
               // customerRadioButton.setChecked(true);

            }
            case R.id.customerRadioButton:
            {
                if(customerRadioButton.isChecked()) {
                    //customerRadioButton.setChecked(true);
                    staffRadioButton.setChecked(false);
                    adminRadioButton.setChecked(false);
                    enrollButton.setVisibility(View.VISIBLE);
                }

               // staffRadioButton.setChecked(true);
                //adminRadioButton.setChecked(true);
            }

        }
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.loginButton:
            {

                if(adminRadioButton.isChecked())
                {
                    flag="A";
                    _id = id.getText().toString();
                    _passwd = passwd.getText().toString();
                    _passwd = caesar.crypt(_passwd);
                    try{
                        new Login().execute("http://203.249.22.53:8080/login.php?flag=" + flag + "&id=" + new String(URLEncoder.encode(_id, "UTF-8").getBytes("UTF-8")) + "&passwd=" + new String(URLEncoder.encode(_passwd, "UTF-8").getBytes("UTF-8")));
                    }catch (Exception e){}
                    /*if(loginCheck ==false) break;
                    Intent intent=new Intent(this,AdminMainViewActivity.class);
                    intent.putExtra("resname",resname);
                    startActivity(intent);
                    finish();*/
                }

                else if(customerRadioButton.isChecked())
                {
                    flag="C";
                    _id = id.getText().toString();
                    _passwd = passwd.getText().toString();
                    _passwd = caesar.crypt(_passwd);
                    try{
                        new Login().execute("http://203.249.22.53:8080/login.php?flag=" + flag + "&id=" + new String(URLEncoder.encode(_id, "UTF-8").getBytes("UTF-8")) + "&passwd=" + new String(URLEncoder.encode(_passwd, "UTF-8").getBytes("UTF-8")));
                    }catch (Exception e){}
                    /*if(loginCheck == false) break;
                    Intent intent=new Intent(this,AdminMainViewActivity.class);
                    intent.putExtra("resname",resname);
                    startActivity(intent);
                    finish();*/
                }

                else if(staffRadioButton.isChecked())
                {
                    flag="S";
                    _id = id.getText().toString();
                    _passwd = passwd.getText().toString();
                    _passwd = caesar.crypt(_passwd);

                    try{
                        new Login().execute("http://203.249.22.53:8080/login.php?flag=" + flag + "&id=" + new String(URLEncoder.encode(_id, "UTF-8").getBytes("UTF-8")) + "&passwd=" + new String(URLEncoder.encode(_passwd, "UTF-8").getBytes("UTF-8"))+"&resname="+ new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")));
                    }catch (Exception e){}

                }


                break;
            }

            case R.id.enrollButton:
            {
                if(adminRadioButton.isChecked())
                {
                    Intent intent=new Intent(this,AdministratorEnrollActivity.class);
                    startActivity(intent);
                }
                else if(customerRadioButton.isChecked())
                {
                    Intent intent=new Intent(this,CustomerEnrollActivity.class);
                    startActivity(intent);

                }

                break;
            }
/*
            case R.id.searchButton:
            {
                Intent intent=new Intent(this,SearchRestaurantActivity.class);
                startActivity(intent);

                break;
            }
            */
        }
    }

    private class ResList extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) { // 이놈의 반환값이 onPostExecute의 매개변수가 된다. jsonarray가 반환.
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {

            String resname;

            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    resname = jo.getString("resname");
                    listItem.add(new ListItem(resname));
                }
                restaurant.clear();
                for (int i = 0; i < ja.length(); i++) {
                    Log.d("LOGIN", listItem.get(i).getData(0));
                    restaurant.add(listItem.get(i).getData(0));
                }
                //txt += "name : " + listItem.get(i).getData(0) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class Login extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return resultText.toString();
        }

        protected void onPostExecute(String str){
            Log.d("LOGIN", str);
            if(str.equals("\uFEFF1")){
                loginCheck = true;
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                if(loginCheck == true){
                    if(flag == "A"){
                        Intent intent=new Intent(LoginActivity.this,AdminMainViewActivity.class);
                        intent.putExtra("id", _id);
                        //intent.putExtra("passwd",_passwd);
                        startActivity(intent);
                        //finish();
                    }
                     //손님 메인뷰 만들어지면 이거 주석 풀기.
                    else if(flag == "C"){
                        Intent intent=new Intent(LoginActivity.this,CustomerMainViewLayout.class);
                        intent.putExtra("resname",resname);
                        startActivity(intent);
                        //finish();
                    }
                     //직원 메인뷰 만들어지면 이거 주석 풀기.
                    else if(flag == "S"){
                        Intent intent=new Intent(LoginActivity.this,StaffMainLayout.class);
                        intent.putExtra("resname",resname);
                        startActivity(intent);
                        //finish();
                    }
                }
            }else  {
                loginCheck=false;
                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
            }
        }
    }
}
