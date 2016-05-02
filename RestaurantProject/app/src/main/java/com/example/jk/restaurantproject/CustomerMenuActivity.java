package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by JK on 2015. 12. 6..
 */
public class CustomerMenuActivity extends Activity {

    TextView courseName;
    ListView listView;
    ArrayList<MenuDataProvider> menuData=new ArrayList<MenuDataProvider>();
    String _menu,_price,_menuinfo,course,resname;
    MenuList menuList = new MenuList();
    CustomListviewAdapter myAdapter;
   // MenuDataProvider menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customermenulayout);

        courseName=(TextView)findViewById(R.id.customerCoursenameTextView);
        listView=(ListView)findViewById(R.id.customerCourseListView);

        Intent intent=getIntent();
        course = intent.getStringExtra("course");
        resname = intent.getStringExtra("resname");
        courseName.setText(intent.getStringExtra("course"));

        try {
            menuList.execute("http://203.249.22.53:8080/menuList.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&course=" + new String(URLEncoder.encode(course, "UTF-8").getBytes("UTF-8")));
        }catch(Exception e){ }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent=new Intent(CustomerMenuViewActivity.this,넣을곳.class);
                //intent.putExtra("COURSE",adapter.get(position));
            }
        });
    }
    private class MenuList extends AsyncTask<String, Integer, String> {

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

            menuData.clear();

            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    _menu = jo.getString("menu");
                    _price = jo.getString("price");
                    _menuinfo = jo.getString("menuinfo");
                    menuData.add(new MenuDataProvider(_menu, _price, _menuinfo));
                }

                myAdapter=new CustomListviewAdapter(getApplicationContext(),menuData);
                listView.setAdapter(myAdapter);
                //txt += "name : " + listItem.get(i).getData(0) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
