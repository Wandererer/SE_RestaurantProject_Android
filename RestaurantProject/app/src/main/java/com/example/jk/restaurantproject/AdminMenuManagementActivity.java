package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
public class AdminMenuManagementActivity extends Activity {

    ListView menuGridView;
    Button insertButton;
    Button deleteButton;
    ArrayList<String> names=new ArrayList<String>();
    ArrayList<ListItem> listItem = new ArrayList<>();
    ArrayAdapter<String>adapter;
    CourseMenuManage courseMenuManage;
    String insertName="";
    String changeName="";
    String resname;
    CourseList courseList = new CourseList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managemenu);
        Intent intent = getIntent();
        resname = intent.getStringExtra("resname");
        menuGridView=(ListView)findViewById(R.id.menuView);
        insertButton=(Button)findViewById(R.id.courseInsertButton);
        deleteButton=(Button)findViewById(R.id.courseDeleteButton);
        try {
            courseList.execute("http://203.249.22.53:8080/courseList.php?resname=" +  new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")));
        }catch(Exception e){}


        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"1",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(0));
                        startActivity(intent);

                        break;
                    }
                    case 1:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"2",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(1));
                        startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"3",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(2));
                        startActivity(intent);
                        break;
                    } case 3:
                     {
                         Toast.makeText(AdminMenuManagementActivity.this,"4",Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                         intent.putExtra("resname",resname);
                         intent.putExtra("CourseName",names.get(3));
                         startActivity(intent);
                         break;
                     }
                    case 4:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"5",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(4));
                        startActivity(intent);
                        break;
                    }
                    case 5:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"6",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(5));
                        startActivity(intent);
                        break;
                    }
                    case 6:
                    {
                        Toast.makeText(AdminMenuManagementActivity.this,"7",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMenuManagementActivity.this,FirstMenuManageActivity.class);
                        intent.putExtra("resname",resname);
                        intent.putExtra("CourseName",names.get(6));
                        startActivity(intent);
                        break;
                    }
                }
            }
        });


    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.courseInsertButton: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("INSERT");
                builder.setCancelable(false);
                final EditText input = new EditText(this);
                input.setSingleLine(true);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertName = input.getText().toString();
                        int addnot=0;
                        for(int i=0;i<names.size();i++)
                        {
                            if(insertName.equals(names.get(i)))
                                addnot=1;

                        }

                        if (!insertName.isEmpty() && insertName.length() > 0 && adapter.getCount() < 7 && addnot==0) {
                            try {
                                courseMenuManage.execute("http://203.249.22.53:8080/addCourse.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&course=" + new String(URLEncoder.encode(insertName, "UTF-8").getBytes("UTF-8")));
                            } catch (Exception e) {
                            }
                            Toast.makeText(AdminMenuManagementActivity.this, "추가 되었습니다", Toast.LENGTH_SHORT).show();
                            add();
                            dialog.cancel();
                        }
                        else
                            Toast.makeText(AdminMenuManagementActivity.this, "추가 할 수 없습니다", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();


                alert.show();


                break;
            }

            case R.id.courseUpdateButton:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("UPDATE");
                builder.setCancelable(false);



                final EditText input = new EditText(this);
                input.setSingleLine(true);
                input.setHint("바꿀거 이름");
               // builder.setView(input);

                final EditText change = new EditText(this);
                change.setSingleLine(true);
                change.setHint("바꿀 이름");
               // builder.setView(change);

                LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(input);
                ll.addView(change);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertName = input.getText().toString();
                        changeName=change.getText().toString();
                        if (!insertName.isEmpty() && insertName.length() > 0) {
                            for(int i=0;i<names.size();i++)
                            {
                                if(insertName.equals(names.get(i)))
                                {
                                    int addnot=0;
                                    for(int j=0;j<names.size();j++)
                                    {
                                        if(changeName.equals(names.get(j)))
                                            addnot=1;
                                    }

                                    if(!changeName.isEmpty() && changeName.length()>0 && addnot==0) {
                                        names.remove(i);
                                        names.add(i, changeName);
                                        courseMenuManage = new CourseMenuManage();
                                        try {
                                            courseMenuManage.execute("http://203.249.22.53:8080/changeCourse.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&course=" + new String(URLEncoder.encode(insertName, "UTF-8").getBytes("UTF-8"))+"&change="+new String(URLEncoder.encode(changeName, "UTF-8").getBytes("UTF-8")));
                                        } catch (Exception e) {
                                        }
                                        Toast.makeText(AdminMenuManagementActivity.this, "변경 되었습니다", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();

                                        break;

                                    }


                                }


                            }


                        }
                        else
                            Toast.makeText(AdminMenuManagementActivity.this, "변경 할 것이 없습니다", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();

                break;
            }

            case R.id.courseDeleteButton: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder = new AlertDialog.Builder(this);
                builder.setMessage("DELETE");
                builder.setCancelable(false);
                final EditText input = new EditText(this);
                input.setHint("지울거 이름");
                input.setSingleLine(true);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertName = input.getText().toString();
                        if (!insertName.isEmpty() && insertName.length() > 0) {
                            for (String a : names) {
                                if (insertName.equals(a)) {
                                    names.remove(insertName);
                                    adapter.remove(a);
                                    courseMenuManage = new CourseMenuManage();
                                    try {
                                        courseMenuManage.execute("http://203.249.22.53:8080/deleteCourse.php?resname=" + new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8")) + "&course=" + new String(URLEncoder.encode(insertName, "UTF-8").getBytes("UTF-8")));
                                    } catch (Exception e) {
                                    }
                                    Toast.makeText(AdminMenuManagementActivity.this, "제거 되었습니다", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    dialog.cancel();
                                    break;

                                }

                            }


                        } else
                            Toast.makeText(AdminMenuManagementActivity.this, "제거 할 것이 없습니다", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });
                AlertDialog alert = builder.create();


                alert.show();

                break;
            }
        }
    }


    private void add()
    {
        String name=insertName;
        if(!name.isEmpty() && name.length()>0 )
        {
            adapter.add(name);
            adapter.notifyDataSetChanged();
        }
    }


    private void update()
    {
        String name=insertName;

        int pos=menuGridView.getCheckedItemPosition();
        if(!name.isEmpty() && name.length()>0 )
        {
            adapter.remove(names.get(pos));
            adapter.insert(name, pos);
            adapter.notifyDataSetChanged();
        }
    }


    private void delete()
    {
        int pos=menuGridView.getCheckedItemPosition();

        if(pos>-1)
        {
            adapter.remove(names.get(pos));

        }
    }

    public void clear()
    {
        adapter.clear();
    }

   /* private class CourseMenuManage extends AsyncTask<Void, Void, Void> {
        String resname,course,flag,change;

        public CourseMenuManage(String resname,String course, String flag) {
            this.course = course;
            this.resname = resname;
            this.flag = flag;
            this.change = changeName;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("resname",resname));
            dataToSend.add(new BasicNameValuePair("course",course));
            dataToSend.add(new BasicNameValuePair("change",change));
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(getHttpRequestParams());
            HttpPost post;
            if(flag.equals("I")) {
                 post = new HttpPost("http://203.249.22.53:8080/addCourse.php");
            }
            else if(flag.equals("D")){
                post = new HttpPost("http://203.249.22.53:8080/deleteCourse.php");
            }
            else post = new HttpPost("http://203.249.22.53:8080/changeCourse.php");
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
            if(flag.equals("I"))            Toast.makeText(getApplicationContext(), "코스 등록", Toast.LENGTH_SHORT).show();
            else if(flag.equals("D")) Toast.makeText(getApplicationContext(), "코스 삭제", Toast.LENGTH_SHORT).show();
            else Toast.makeText(getApplicationContext(), "코스 변경", Toast.LENGTH_SHORT).show();
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
            return httpRequestParams;
        }
    }*/

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
                    Log.d("LOGIN", listItem.get(i).getData(0));
                    names.add(listItem.get(i).getData(0));
                }
                adapter=new ArrayAdapter<String>(AdminMenuManagementActivity.this,android.R.layout.simple_list_item_1,names);
                menuGridView.setAdapter(adapter);
                //txt += "name : " + listItem.get(i).getData(0) + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class CourseMenuManage extends AsyncTask<String, Integer,String>{

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
               Toast.makeText(getApplicationContext(), "이미 사용중인 ID 입니다.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"사용 가능한 ID 입니다.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
