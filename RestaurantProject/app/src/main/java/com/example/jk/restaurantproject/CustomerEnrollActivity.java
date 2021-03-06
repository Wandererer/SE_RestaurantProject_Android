package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by JK on 2015. 11. 30..
 */
public class CustomerEnrollActivity extends Activity {

    DatePicker date;
    String flag = "C", birth;
    EditText id, passwd, name, phoneNum;
    Caesar caesar = new Caesar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerenroll)
        ;
        id=(EditText)findViewById(R.id.customeridEditText);
        passwd=(EditText)findViewById(R.id.customerPasswdEditText);
        name=(EditText)findViewById(R.id.customerNameEditText);
        phoneNum=(EditText)findViewById(R.id.customerPhoneNumEditText);
        date=(DatePicker)findViewById(R.id.datePicker);
        date.init(date.getYear(),date.getMonth(),date.getDayOfMonth(),new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birth = String.format("%d.%d.%d",year,monthOfYear+1,dayOfMonth);
                //Toast.makeText(CustomerEnrollActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.customeridCheckbutton:
            {
                new DdCheck().execute("http://203.249.22.53:8080/dbcheck.php?flag="+flag+"&id="+id.getText().toString());
                break;
            }
            case R.id.customerEnrollButton:
            {
                new CustomerTableInsert(id.getText().toString(),passwd.getText().toString(),name.getText().toString(),
                        phoneNum.getText().toString(),birth).execute();

                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }
    private class CustomerTableInsert extends AsyncTask<Void, Void, Void> {
        String id, passwd, name, phoneNum,birth;

        public CustomerTableInsert(String id, String passwd, String name, String phoneNum, String birth ) {
            this.id = id;
            this.passwd = passwd;
            this.name = name;
            this.phoneNum = phoneNum;
            this.birth = birth;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("flag",flag));
            dataToSend.add(new BasicNameValuePair("id", id));
            dataToSend.add(new BasicNameValuePair("passwd",caesar.crypt(passwd)));
            dataToSend.add(new BasicNameValuePair("name", name));
            dataToSend.add(new BasicNameValuePair("phoneNum", phoneNum));
            dataToSend.add(new BasicNameValuePair("birth", birth));

            Log.d("AR", "db");
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(getHttpRequestParams());
            HttpPost post = new HttpPost("http://203.249.22.53:8080/dbinsert.php");
            Log.d("AR","db");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend,"UTF-8"));
                client.execute(post);
                Log.d("AR","tryenter");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("AR","tryfinish");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Enroll complete!", Toast.LENGTH_SHORT).show();
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
            return httpRequestParams;
        }
    }
    private class DdCheck extends AsyncTask<String, Integer,String>{

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
            Log.d("AR", str);
            if(str.equals("\uFEFF1")){
                Toast.makeText(getApplicationContext(), "이미 사용중인 ID 입니다.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"사용 가능한 ID 입니다.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
