package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
 * Created by JK on 2015. 12. 4..
 */
public class FirstMenuManageActivity extends Activity {

    TextView courseName;

    ListView listView;
    ArrayList<MenuDataProvider> menuData=new ArrayList<MenuDataProvider>();
    ArrayList<ListItem> listItem = new ArrayList<>();
    CustomListviewAdapter myAdapter;
    MenuDataProvider menu;
    MenuManage menuManage;

    String insertName="";
    String changeName="";
    String resname,course;
    String _menu,_price,_menuinfo;
    MenuList menuList = new MenuList();

    ArrayList<MenuDataProvider> menus=new ArrayList<MenuDataProvider>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstmenumanage);
        Intent intent = getIntent();
        resname = intent.getStringExtra("resname").toString();
        course = intent.getStringExtra("CourseName").toString();
        menu=new MenuDataProvider();
        courseName=(TextView)findViewById(R.id.firstCourseName);
        listView=(ListView)findViewById(R.id.firstMenuListView);
        try {
            menuList.execute("http://203.249.22.53:8080/menuList.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&course=" + new String(URLEncoder.encode(course, "UTF-8").getBytes("UTF-8")));
        }catch(Exception e){ }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        // CourseName

        courseName.setText(course);

    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.firstMenuInsertButton:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("INSERT");
                builder.setCancelable(false);



                final EditText menuInput = new EditText(this);
                menuInput.setHint("메뉴 이름");
                menuInput.setSingleLine(true);
                // builder.setView(input);

                final EditText moneyInput = new EditText(this);
                moneyInput.setHint("가격");
                moneyInput.setSingleLine(true);
                moneyInput.setKeyListener(DigitsKeyListener.getInstance(false,false));


                final EditText menuInfoInput = new EditText(this);
                menuInfoInput.setHint("메뉴 정보");
                menuInfoInput.setSingleLine(true);
                // builder.setView(change);

                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(menuInput);
                ll.addView(moneyInput);
                ll.addView(menuInfoInput);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String menuNameString=menuInput.getText().toString();
                        String menuPriceString=moneyInput.getText().toString();
                        String menuInfoString=menuInfoInput.getText().toString();
                        String menuName;
                        String menuPrice;
                        String menuInfo;
                        menu=new MenuDataProvider(menuNameString,menuPriceString,menuInfoString);

                        Boolean same=false;

                        for(int i=0;i<myAdapter.getCount();i++)
                        {
                            if(menuNameString.equals(myAdapter.getItem(i).getMenuName()))
                            {
                                same=true;
                                Toast.makeText(FirstMenuManageActivity.this, "똑같은 메뉴 이름이 있습니다", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        if(menuNameString.length()>0 && menuPriceString.length()>0 && menuInfoString.length()>0 && !same) {
                            myAdapter.add(menu);
                            menu.getMenuName();
                            menuManage = new MenuManage();
                            try{
                                resname = new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8"));
                                course =  new String(URLEncoder.encode(course, "UTF-8").getBytes("UTF-8"));
                                menuName =    new String(URLEncoder.encode(menuNameString, "UTF-8").getBytes("UTF-8"));
                                menuPrice =    new String(URLEncoder.encode(menuPriceString, "UTF-8").getBytes("UTF-8"));
                                menuInfo  =    new String(URLEncoder.encode(menuInfoString, "UTF-8").getBytes("UTF-8"));
                                menuManage.execute("http://203.249.22.53:8080/addMenu.php?resname="+resname+"&course="+course+"&menu="+menuName+"&price="+menuPrice+"&menuinfo="+menuInfo);
                            }catch(Exception e){
                            }
                            Toast.makeText(FirstMenuManageActivity.this, "추가 되었습니다", Toast.LENGTH_SHORT).show();
                            myAdapter.notifyDataSetChanged();
                        }
                        else if(menuNameString.length()==0 || menuPriceString.length()==0 || menuInfoString.length()==0)
                            Toast.makeText(FirstMenuManageActivity.this, "모든 영역을 다 채우셔야합니다", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();


                break;
            }
            case R.id.firstMenuDeleteButton:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("DELETE");
                builder.setCancelable(false);



                final EditText menuInput = new EditText(this);
                menuInput.setHint("삭제할 메뉴 이름");
                menuInput.setSingleLine(true);

                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(menuInput);
                builder.setView(ll);



                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String menuNameString = menuInput.getText().toString();
                        String menuName;

                        for (int i = 0; i < myAdapter.getCount(); i++) {
                            if (menuNameString.equals(myAdapter.getItem(i).getMenuName())) {
                                myAdapter.remove(myAdapter.getItem(i));
                                menuManage = new MenuManage();
                                try{
                                    resname = new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8"));
                                    course =  new String(URLEncoder.encode(course, "UTF-8").getBytes("UTF-8"));
                                    menuName =    new String(URLEncoder.encode(menuNameString, "UTF-8").getBytes("UTF-8"));
                                  /*  menuPrice =    new String(URLEncoder.encode(menuPriceString, "UTF-8").getBytes("UTF-8"));
                                    menuInfo  =    new String(URLEncoder.encode(menuInfoString, "UTF-8").getBytes("UTF-8"));*/
                                    menuManage.execute("http://203.249.22.53:8080/deleteMenu.php?resname="+resname+"&course="+course+"&menu="+menuName);
                                }catch(Exception e){
                                }
                                //dbCheck = new DbCheck();
                                //dbCheck.execute("http://203.249.22.53:8080/deleteMenu.php?resname=v");
                                //menuManage = new MenuManage(resname,course,"D",menuNameString,"none","none");
                                Toast.makeText(FirstMenuManageActivity.this, "제거 되었습니다", Toast.LENGTH_SHORT).show();
                                myAdapter.notifyDataSetChanged();
                                dialog.cancel();
                                break;
                            }
                        }

                        Toast.makeText(FirstMenuManageActivity.this, "제거 할 것이 없습니다", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();



                break;
            }

            case R.id.fisrtMenuUpdateButton:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("UPDATE");
                builder.setCancelable(false);



                final EditText menuInput = new EditText(this);
                menuInput.setHint("갱신 할 메뉴 이름");
                menuInput.setSingleLine(true);
                // builder.setView(input);

                final EditText moneyInput = new EditText(this);
                moneyInput.setHint("가격");
                moneyInput.setSingleLine(true);
                moneyInput.setKeyListener(DigitsKeyListener.getInstance(false,false));


                final EditText menuInfoInput = new EditText(this);
                menuInfoInput.setHint("메뉴 정보");
                menuInfoInput.setSingleLine(true);
                // builder.setView(change);

                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(menuInput);
                ll.addView(moneyInput);
                ll.addView(menuInfoInput);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String menuNameString = menuInput.getText().toString();
                        String menuPriceString = moneyInput.getText().toString();
                        String menuInfoString = menuInfoInput.getText().toString();
                        String menuName;
                        String menuPrice;
                        String menuInfo;
                        menu = new MenuDataProvider(menuNameString, menuPriceString, menuInfoString);


                        if (menuNameString.length() == 0 || menuPriceString.length() == 0 || menuInfoString.length() == 0) {
                            Toast.makeText(FirstMenuManageActivity.this, "모든 영역을 다 채우셔야합니다", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }

                        for (int i = 0; i < myAdapter.getCount(); i++) {
                            if (menuNameString.equals(myAdapter.getItem(i).getMenuName())) {
                                myAdapter.getItem(i).setMenuName(menuNameString);
                                myAdapter.getItem(i).setPriceName(menuPriceString);
                                myAdapter.getItem(i).setContentName(menuInfoString);
                                //changeName = menuNameString;
                                menuManage = new MenuManage();
                                try{
                                    resname = new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8"));
                                    course =  new String(URLEncoder.encode(course, "UTF-8").getBytes("UTF-8"));
                                    menuName =    new String(URLEncoder.encode(menuNameString, "UTF-8").getBytes("UTF-8"));
                                    menuPrice =    new String(URLEncoder.encode(menuPriceString, "UTF-8").getBytes("UTF-8"));
                                    menuInfo  =    new String(URLEncoder.encode(menuInfoString, "UTF-8").getBytes("UTF-8"));
                                    menuManage.execute("http://203.249.22.53:8080/changeMenu.php?resname="+resname+"&course="+course+"&menu="+menuName+"&price="+menuPrice+"&menuinfo="+menuInfo);
                                }catch(Exception e){
                                }
                               // menuManage = new MenuManage(resname,course,"C",menuNameString,menuPriceString,menuInfoString);
                                Toast.makeText(FirstMenuManageActivity.this, "변경 되었습니다", Toast.LENGTH_SHORT).show();
                                myAdapter.notifyDataSetChanged();
                                dialog.cancel();
                                break;

                            }
                        }
                        Toast.makeText(FirstMenuManageActivity.this, "변경 할 것이 없습니다", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();



                break;
            }
        }
    }
    /*private class MenuManage extends AsyncTask<Void, Void, Void> {
        String resname,course,flag,changed,menu,price,menuinfo;

        public MenuManage(String resname,String course, String flag,String menu,String price,String menuinfo) {
            this.course = course;
            this.resname = resname;
            this.flag = flag;
            this.changed = changeName;
            this.menu = menu;
            this.price = price;
            this.menuinfo = menuinfo;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("resname",resname));
            dataToSend.add(new BasicNameValuePair("course",course));
            dataToSend.add(new BasicNameValuePair("menu",menu));
            dataToSend.add(new BasicNameValuePair("price",price));
            dataToSend.add(new BasicNameValuePair("menuinfo",menuinfo));
            dataToSend.add(new BasicNameValuePair("changed",changed));

            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(getHttpRequestParams());
            HttpPost post;
            if(flag.equals("I")) {
                post = new HttpPost("http://203.249.22.53:8080/addMenu.php");
            }
            else if(flag.equals("D")){
                post = new HttpPost("http://203.249.22.53:8080/deleteMenu.php");
            }
            else post = new HttpPost("http://203.249.22.53:8080/changeMenu.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend,"UTF-8"));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(flag.equals("I"))            Toast.makeText(getApplicationContext(), "메뉴 등록", Toast.LENGTH_SHORT).show();
            else if(flag.equals("D")) Toast.makeText(getApplicationContext(), "메뉴 삭제", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "메뉴 변경", Toast.LENGTH_SHORT).show();
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
            return httpRequestParams;
        }
    }*/
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
    private class MenuManage extends AsyncTask<String, Integer,String>{

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
            Log.d("AAAA", str);
        }
    }
}
