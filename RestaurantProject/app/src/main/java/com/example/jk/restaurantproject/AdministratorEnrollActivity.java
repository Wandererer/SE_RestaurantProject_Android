package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
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
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by JK on 2015. 11. 30..
 */
public class AdministratorEnrollActivity extends Activity {

    EditText id,passwd,resname,licenseNumber,ownerName,phoneNum;
    String flag = "A";
    Caesar caesar = new Caesar();
    String sid,spasswd,sresname,slicenseNumber,sownerName,sphoneNum;
    int toast=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administratorenroll);

        id=(EditText)findViewById(R.id.idEditText);
        passwd=(EditText)findViewById(R.id.passwdEditText);
        resname=(EditText)findViewById(R.id.restaurantNameEditText);
        licenseNumber=(EditText)findViewById(R.id.licenseNumberEditText);
        ownerName=(EditText)findViewById(R.id.nameEditText);
        phoneNum=(EditText)findViewById(R.id.phoneNumberEditText);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.adminEnrollButton:
            {
                // 가입버튼을 누르면 입력한 정보들이 db에 저장된다.
                sid = id.getText().toString();
                spasswd = passwd.getText().toString();
                sresname = resname.getText().toString();
                slicenseNumber = licenseNumber.getText().toString();
                sownerName = ownerName.getText().toString();
                sphoneNum = phoneNum.getText().toString();


                new AdminTableInsert(sid, spasswd,sresname,slicenseNumber, sownerName,sphoneNum).execute();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            }
            case R.id.idCheckbutton:
            {
                toast=1; // 토스트 메시지 구분해주려고
                String check = "id";
                sid = id.getText().toString();
                try{
                    sid = new String(URLEncoder.encode(sid, "UTF-8").getBytes("UTF-8"));
                }catch (Exception e){}

                new DbCheck().execute("http://203.249.22.53:8080/dbcheck.php?flag="+flag+"&check="+check+"&id="+sid);
                break;
            }
            case R.id.restaurantNameCheckbutton: {
                toast=0; // 토스트 메시지 구분해주려고
                String check = "resname";
                sresname = resname.getText().toString();
                try{
                    sresname = new String(URLEncoder.encode(sresname, "UTF-8").getBytes("UTF-8"));
                }catch (Exception e){}

                new DbCheck().execute("http://203.249.22.53:8080/dbcheck.php?flag="+flag+"&check="+check+"&resname="+sresname);
                break;
            }

        }
    }
    private class AdminTableInsert extends AsyncTask<Void, Void, Void> {
        String id,passwd,resname,licenseNumber,ownerName,phoneNum;

        public AdminTableInsert(String id, String passwd, String resname, String licenseNumber, String ownerName, String phoneNum ) {
            this.id = id;
            this.passwd = passwd;
            this.resname = resname;
            this.licenseNumber = licenseNumber;
            this.ownerName = ownerName;
            this.phoneNum = phoneNum;
            Log.d("AD", this.id);
            Log.d("AD", this.passwd);
            Log.d("AD", this.resname);
            Log.d("AD", this.licenseNumber);
            Log.d("AD", this.ownerName);
            Log.d("AD", this.phoneNum);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();

            dataToSend.add(new BasicNameValuePair("flag",flag));
            dataToSend.add(new BasicNameValuePair("id", id));
            dataToSend.add(new BasicNameValuePair("passwd",caesar.crypt(passwd)));
            dataToSend.add(new BasicNameValuePair("resname", resname));
            dataToSend.add(new BasicNameValuePair("licenseNumber", licenseNumber));
            dataToSend.add(new BasicNameValuePair("ownerName", ownerName));
            dataToSend.add(new BasicNameValuePair("phoneNum", phoneNum));

            Log.d("AR", "db");
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(getHttpRequestParams());
            HttpPost post = new HttpPost("http://203.249.22.53:8080/dbinsert.php");

            Log.d("AR", "db");

            try {
                UrlEncodedFormEntity form = new UrlEncodedFormEntity(dataToSend,"UTF-8");
                post.setEntity(form);
                client.execute(post);
                Log.d("AR","tryenter");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("AR", "tryfinish");

/* 위에꺼 안될땐 이걸 써봐라.
           HttpClient httpclient = new DefaultHttpClient();
            try {

                List<NameValuePair> dataToSend = new ArrayList<NameValuePair>(2);
                dataToSend.add(new BasicNameValuePair("flag",flag));
                dataToSend.add(new BasicNameValuePair("id", id));
                dataToSend.add(new BasicNameValuePair("passwd", caesar.crypt(passwd)));
                dataToSend.add(new BasicNameValuePair("resname", resname));
                dataToSend.add(new BasicNameValuePair("licenseNumber", licenseNumber));
                dataToSend.add(new BasicNameValuePair("ownerName", ownerName));
                dataToSend.add(new BasicNameValuePair("phoneNum", phoneNum));
                HttpPost httpPost = new HttpPost("http://203.249.22.53:8080/dbinsert.php");
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(dataToSend, "UTF-8");
                httpPost.setEntity(entity);
                httpclient.execute(httpPost);
            }
            catch (Exception e) {
                System.out.println(e);
            }*/
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

    private class DbCheck extends AsyncTask<String, Integer,String>{

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
            Log.d("AR",str);
            if(str.equals("\uFEFF1")){
                if(toast==1) Toast.makeText(getApplicationContext(), "이미 사용중인 ID 입니다.", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "이미 등록된 레스토랑 입니다.", Toast.LENGTH_LONG).show();
            }else{
                if(toast==1) Toast.makeText(getApplicationContext(),"사용 가능한 ID 입니다.",Toast.LENGTH_LONG).show();
                else  Toast.makeText(getApplicationContext(), "등록 가능한 레스토랑 입니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
