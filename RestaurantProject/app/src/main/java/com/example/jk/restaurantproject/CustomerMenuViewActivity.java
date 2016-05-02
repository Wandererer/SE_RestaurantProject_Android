package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 2015. 12. 6..
 */
public class CustomerMenuViewActivity extends Activity {

    ListView listView;
    ArrayList<String> names=new ArrayList<String>();
    ArrayList<ListItem> listItem = new ArrayList<>();
    ArrayAdapter<String> adapter;
    CourseList courseList=new CourseList();
    String resname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customermenuviewlayout);

        listView=(ListView)findViewById(R.id.customerMenuView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        Intent intent = getIntent();
        resname = intent.getStringExtra("resname");
        try {
            courseList.execute("http://203.249.22.53:8080/courseList.php?resname=" +new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")));
        }catch (Exception e){}
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CustomerMenuViewActivity.this,CustomerMenuActivity.class);
                intent.putExtra("resname",resname);
                intent.putExtra("course",adapter.getItem(position));
                startActivity(intent);
            }
        });


    }
    private class CourseList extends AsyncTask<String, Integer, String> {

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

            String course;

            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    course = jo.getString("course");
                    listItem.add(new ListItem(course));
                }
                names.clear();
                for (int i = 0; i < ja.length(); i++) {
                    //Log.d("LOGIN", listItem.get(i).getData(0));
                    names.add(listItem.get(i).getData(0));
                }
                adapter=new ArrayAdapter<String>(CustomerMenuViewActivity.this,android.R.layout.simple_list_item_1,names);
                listView.setAdapter(adapter);
                //txt += "name : " + listItem.get(i).getData(0) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
