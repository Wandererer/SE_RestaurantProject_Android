package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.renderscript.Matrix2f;
import android.renderscript.Matrix3f;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JK on 2015. 12. 1..
 */
public class AdminMainViewActivity extends Activity {

    private static final int ACTIVITY_START_CAMERA_APP=0;

    ImageView imgView;
    TextView resName;
    private String imageLocation = "";
    UploadImage uploadImage;
    DirCheck dirCheck;
    Bitmap imgBitmap;
    Boolean isExist = false;
    String id,resname="";
    ArrayList<ListItem> listItem = new ArrayList<>();
    ResName getResName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmainlayout);

        Intent intent = getIntent();
        //intent.getStringExtra("resname");
        id = intent.getStringExtra("id");

        imgView = (ImageView) findViewById(R.id.mainRestaurantPicture);
        //imgView.setImageBitmap();
        resName = (TextView) findViewById(R.id.mainLayoutRestaurantName);
        getResName = new ResName();
        getResName.execute("http://203.249.22.53:8080/getRN.php?id=" + id);
        dirCheck = new DirCheck();
        dirCheck.execute("http://203.249.22.53:8080/dirCheck.php?id=" + id);
        imageLoader("http://203.249.22.53:8080/" + id + "/" + id + "_res.jpg");

        resName = (TextView) findViewById(R.id.mainLayoutRestaurantName);
        resName.setText(resname);
        //imgView.getLayoutParams().height=imgView.getHeight();
        //imgView.getLayoutParams().width=imgView.getWidth();

        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent callCamera=new Intent();
                callCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile=null;
                try{
                    photoFile=createImageFile();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
                callCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(callCamera, ACTIVITY_START_CAMERA_APP);

                return true;
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        if(requestCode==ACTIVITY_START_CAMERA_APP && resultCode==RESULT_OK)
        {
           // Bundle extras=data.getExtras();
            //Bitmap photoBitmap=(Bitmap)extras.get("data");
           // photoBitmap.setWidth(360);
           // photoBitmap.setHeight(250);
           // imgView.setImageBitmap(photoBitmap);
            //Bitmap photoBitmap=BitmapFactory.decodeFile(imageLocation);
           // imgView.setImageBitmap(photoBitmap);
           rotateImage(setReduceImageSize());
        }
    }

    File createImageFile() throws IOException {
        String timeStmap=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String ImageFileName="IMAGE_"+timeStmap+"_";
        File storageDirecory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(ImageFileName,".jgp",storageDirecory);

        imageLocation=image.getAbsolutePath();


        return image;
    }

    private Bitmap setReduceImageSize()
    {
        int targetImageWidth= imgView.getWidth();
        int targetImageHeight=imgView.getHeight();

        BitmapFactory.Options bmOptions=new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imageLocation,bmOptions);
        int cameraImageWidth=bmOptions.outWidth;
        int cameraImageHeight=bmOptions.outHeight;

        int scaleFactor= Math.min(cameraImageWidth / targetImageWidth, cameraImageHeight / targetImageHeight);
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inJustDecodeBounds=false;

       // Bitmap photoReducedBitmap=BitmapFactory.decodeFile(imageLocation, bmOptions);
       // imgView.setImageBitmap(photoReducedBitmap);

        return BitmapFactory.decodeFile(imageLocation,bmOptions);

    }


    private  void rotateImage(Bitmap bitmap)
    {
        ExifInterface exifInterface=null;



        try{
            exifInterface=new ExifInterface(imageLocation);
        }catch(IOException e)
        {
            e.printStackTrace();
        }

        int orientation= exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();
        int rotate=0;
        //matrix.postScale(bitmap.getWidth(),bitmap.getHeight());

        switch (orientation)
        {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                //matrix.setRotate(90,bitmap.getWidth(),bitmap.getHeight());
                //matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                Log.d("CAMERA","180");
                rotate=180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                Log.d("CAMERA","270");
                rotate=270;
                break;


        }


        Bitmap rotateBitmap  ;


        if(rotate==180)
              Log.d("CAMERA","180");



            rotateBitmap=android.graphics.Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        new UploadImage(rotateBitmap, id).execute(); //idid에 원래는 그사람 id를 넣어줘야해.
        Log.d("CAMERA",bitmap.getWidth() +" bitamp "+bitmap.getHeight());
        Log.d("CAMERA",rotateBitmap.getWidth()+" rotate "+ rotateBitmap.getHeight());
        imgView.setImageBitmap(rotateBitmap);
    }


    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.interriorButton:
            {
                Intent intent =new Intent(this,testActivity.class);
                intent.putExtra("resname",resName.getText().toString());
                startActivity(intent);
                break;
            }

            case R.id.menuManagingButton: {
                Intent intent = new Intent(this, AdminMenuManagementActivity.class);
                intent.putExtra("resname",resName.getText().toString());
                startActivity(intent);

                break;
            }
            case R.id.staffIdSettinButton:
            {
                Intent intent=new Intent(this,MakeNewStaffIdOrFindActivity.class);
                intent.putExtra("resname",resName.getText().toString());
                startActivity(intent);
                break;
            }
            case R.id.reservationManageButton:
            {
                Intent intent=new Intent(this,AdminReservationManageActivity.class);
                intent.putExtra("resname",resName.getText().toString());
                intent.putExtra("FROM","admin");
                startActivity(intent);
                break;
            }

            case R.id.adminOrderButton:
            {
                Intent intent=new Intent(this,OrderTableList.class);
                intent.putExtra("resname",resName.getText().toString());
                intent.putExtra("FROM","admin");
                startActivity(intent);
                break;
            }
        }
    }
    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String id;

        public UploadImage(Bitmap image, String id) {
            this.image = image;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("id", id));
            Log.d("HTTP", "datasend");
            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(getHttpRequestParams());
            HttpPost post = new HttpPost("http://203.249.22.53:8080/SavePichure.php");
            Log.d("HTTP","trybefore");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
                Log.d("HTTP","tryenter");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("HTTP","tryfinish");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
            return httpRequestParams;
        }
    }
    private class DirCheck extends AsyncTask<String, Integer,String>{

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
            /*if(str.equals("\uFEFF1")){
                Toast.makeText(getApplicationContext(), "DIR is exist.", Toast.LENGTH_LONG).show();
            }else{
               Toast.makeText(getApplicationContext(),"DIR is not exist.",Toast.LENGTH_LONG).show();
            }*/
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
                ImageView imgView2 = (ImageView) findViewById(R.id.mainRestaurantPicture);
                imgView2.setImageBitmap(imgBitmap);
            }
            else{
                imgView.setImageResource(R.drawable.mainimage);
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
    private class ResName extends AsyncTask<String, Integer, String> {

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
                resname = listItem.get(0).getData(0);
                resName.setText(resname);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
