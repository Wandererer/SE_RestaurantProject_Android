package com.example.jk.restaurantproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JK on 2015. 12. 5..
 */
public class MakeNewStaffIdOrFindActivity extends Activity {

    EditText staffIdEditText, staffPasswdEditText, staffFindIndEdittext;
    TextView staffFindResultTextView;
    Button staffMakeIdButton, staffFindPasswdButton;
    StaffManage staffManage;
    PasswdConfirm passwdConfirm;
    String resname, _id, _passwd;
    Caesar caesar = new Caesar();
    ArrayList<ListItem> listItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makestaffid);

        staffMakeIdButton = (Button) findViewById(R.id.staffIdMakeButton);
        staffFindPasswdButton = (Button) findViewById(R.id.findStaffPasswdButton);

        staffIdEditText = (EditText) findViewById(R.id.staffIdEditText);
        staffPasswdEditText = (EditText) findViewById(R.id.staffPasswdEditText);
        staffFindIndEdittext = (EditText) findViewById(R.id.findStaffPasswdEditText);
        staffFindResultTextView = (TextView) findViewById(R.id.findPasswdResultTextView);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.staffIdMakeButton: {
                Intent intent = getIntent();
                resname = intent.getStringExtra("resname");
                _id = staffIdEditText.getText().toString();
                _passwd = staffPasswdEditText.getText().toString();
                _passwd = caesar.crypt(_passwd);
                staffManage = new StaffManage();
                try {
                    staffManage.execute("http://203.249.22.53:8080/addStaff.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&id=" + new String(URLEncoder.encode(_id, "UTF-8").getBytes("UTF-8")) + "&passwd=" + new String(URLEncoder.encode(_passwd, "UTF-8").getBytes("UTF-8")));
                }catch(Exception e){ }

                break;

            }
            case R.id.findStaffPasswdButton: {
                Intent intent = getIntent();
                resname = intent.getStringExtra("resname");
                _id = staffFindIndEdittext.getText().toString();
                passwdConfirm = new PasswdConfirm();
                try {
                    passwdConfirm.execute("http://203.249.22.53:8080/getPasswd.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&id=" + new String(URLEncoder.encode(_id, "UTF-8").getBytes("UTF-8")));
                }catch(Exception e){ }

                break;
            }
        }
    }

    private class StaffManage extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
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
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultText.toString();
        }

        protected void onPostExecute(String str) {
            Log.d("AAAA",str);
            if (str.equals("\uFEFF1")) {
                staffIdEditText.setText("");
                staffPasswdEditText.setText("");
                Toast.makeText(getApplicationContext(), "등록 성공.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "등록 실패", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class PasswdConfirm extends AsyncTask<String, Integer, String> {

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
            String passwd;
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    passwd = jo.getString("passwd");
                    listItem.add(new ListItem(passwd));
                }
                passwd = listItem.get(0).getData(0);
                passwd = caesar.decrypt(passwd);
                staffFindResultTextView.setText(passwd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
