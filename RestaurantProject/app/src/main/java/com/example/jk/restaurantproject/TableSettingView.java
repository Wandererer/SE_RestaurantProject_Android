package com.example.jk.restaurantproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JK on 2015. 12. 2..
 */
public class TableSettingView extends Activity {

    TextView tableNumber;
    ImageView tablePictureView;
    TextView covers;


    private static final int ACTIVITY_START_CAMERA_APP=0;
    private String imageLocation="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablesetting);

        tableNumber=(TextView)findViewById(R.id.tableNumberName);
        tablePictureView=(ImageView)findViewById(R.id.tablePicture);
        tablePictureView.setImageResource(R.drawable.tablemainpicture);
        covers=(TextView)findViewById(R.id.coversTextView);

        Intent intent=getIntent();
        String tablename;
        tablename=intent.getStringExtra("TableStringID");
        String tableCovers=intent.getStringExtra("Covers");
        tableNumber.setText(tablename);
        covers.setText("테이블인원 : "+tableCovers);

        tablePictureView.setOnLongClickListener(new View.OnLongClickListener() {
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
        int targetImageWidth= tablePictureView.getWidth();
        int targetImageHeight=tablePictureView.getHeight();

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



    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        if(requestCode==ACTIVITY_START_CAMERA_APP && resultCode==RESULT_OK)
        {
            rotateImage(setReduceImageSize());
        }
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
                Log.d("CAMERA", "180");
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

        Log.d("CAMERA",bitmap.getWidth() +" bitamp "+bitmap.getHeight());
        Log.d("CAMERA",rotateBitmap.getWidth()+" rotate "+ rotateBitmap.getHeight());
        tablePictureView.setImageBitmap(rotateBitmap);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tableSaveButton:
            {
                Intent intent=new Intent(this,InterriorActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


}
