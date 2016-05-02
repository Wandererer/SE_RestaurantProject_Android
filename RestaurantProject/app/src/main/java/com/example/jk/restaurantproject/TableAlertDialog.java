package com.example.jk.restaurantproject;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JK on 2015. 12. 3..
 */
public class TableAlertDialog extends DialogFragment implements Runnable {
    TextView tableNumber;
    ImageView tablePictureView;
    TextView covers;
    Button save;

    int targetImageWidth;
    int targetImageHeight;

    private static final int ACTIVITY_START_CAMERA_APP=0;
    private String imageLocation="";

    public TableAlertDialog()
    {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_tablesetting, null);
        tablePictureView=(ImageView)view.findViewById(R.id.tablePicture);
        //setStyle(STYLE_NO_TITLE, 0);
       // tableNumber=(TextView)view.findViewById(R.id.tableNumberName);
        //covers=(TextView)view.findViewById(R.id.coversTextView);
        save=(Button)view.findViewById(R.id.tableSaveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCancelable(false);

        tablePictureView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent,0);
                /*
                Intent callCamera = new Intent();
                callCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(callCamera, ACTIVITY_START_CAMERA_APP);
                */

                return true;
            }
        });
        return view;
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams window=new WindowManager.LayoutParams();
        window.flags=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.dimAmount=0.8f;
        getWindow().setAttributes(window);

        setContentView(R.layout.activity_tablesetting);

        imgView =(ImageView)findViewById(R.id.tablePicture);
        imgView.setImageResource(R.drawable.tablemainpicture);
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent callCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile=null;
                try{
                    photoFile=createImageFile();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
                callCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
               // startActivityForResult(callCamera, ACTIVITY_START_CAMERA_APP);
                getOwnerActivity().startActivityForResult(callCamera,ACTIVITY_START_CAMERA_APP);
                return true;
            }
        });
    }
    */



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==ACTIVITY_START_CAMERA_APP && resultCode== Activity.RESULT_OK)
        {
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

        int i=targetImageHeight;
        int j=targetImageWidth;

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


    @Override
    public void run() {

        targetImageWidth= tablePictureView.getWidth();
        targetImageHeight=tablePictureView.getHeight();

    }
}
