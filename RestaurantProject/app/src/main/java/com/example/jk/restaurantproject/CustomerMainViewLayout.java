package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by JK on 2015. 12. 5..
 */
public class CustomerMainViewLayout extends Activity {

    ImageView imgView;
    TextView resName;
    String imgUrl,resname,resname_url;
    Bitmap imgBitmap;
    ArrayList<ListItem> listItem = new ArrayList<>();
    GetImgUrl getImgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customermainlayout);

        Intent intent = getIntent();
        getImgUrl = new GetImgUrl();
        imgView=(ImageView)findViewById(R.id.customerMainRestaurantPicture);
        resname = intent.getStringExtra("resname");
        try {
            resname_url = new String(URLEncoder.encode(resname, "UTF-8").getBytes("UTF-8"));
        }catch (Exception e){}
        getImgUrl.execute("http://203.249.22.53:8080/getImgURL.php?resname=" + resname_url);
        resName=(TextView)findViewById(R.id.customerMainLayoutRestaurantName);
        resName.setText(resname);

    }

    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.customerInterriorButton:
            {
                Intent intent=new Intent(this,CustomerInterriorViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menuManagingButton:
            {
                Intent intent=new Intent(this,CustomerMenuViewActivity.class);
                intent.putExtra("resname",resname);
                startActivity(intent);

                break;
            }

            case R.id.customerReservationButton:
            {
                Intent intent=new Intent(this,CustomerReservationActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.customerReservationConfirmationButton:
            {
                //여기서 누르면 해당 아이디에 예약 없으면 토스트로 예약이 없습니다라고 뜸
                Intent intent=new Intent(this,CustomerReservationConfirmActivity.class);
                startActivity(intent);
                break;
            }
        }

    }
    public void imageLoader(String url) {
        final String fileurl = url;
        Thread u1 = new Thread() {
            public void run() {
                imgBitmap = GetImageFromURL(fileurl);
                final int i = 1;
                handler1.sendEmptyMessage(i);
            }
        };
        u1.start();
    }

    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (imgBitmap != null) {
                ImageView imgView2 = (ImageView) findViewById(R.id.customerMainRestaurantPicture);
                imgView2.setImageBitmap(imgBitmap);
            }
            else{
                //imgView.setImageResource(R.drawable.mainimage);
            }
        };
    };
    private Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap1 = null;

        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(
                    conn.getInputStream(), nSize);
            imgBitmap1 = BitmapFactory.decodeStream(bis);

            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap1;
    }
    private class GetImgUrl extends AsyncTask<String, Integer, String> {

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
            String imgurl;
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                     imgurl= jo.getString("imgUrl");
                    listItem.add(new ListItem(imgurl));
                }
                imgUrl = listItem.get(0).getData(0);
                imageLoader("http://203.249.22.53:8080/"+ imgUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
