package com.flattomate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageController extends AppCompatActivity {

    File destination;
    Bitmap thumbnail;
    Transformation transformation;
    String userChoosenTask;
    final int REQUEST_CAMERA = 0;
    final int SELECT_FILE = 1;

    int idUser;
    ImageView ivProfileImg;
    Context context;

    public ImageController(Context c, int id, ImageView iv){
        context = c;
        idUser = id;
        ivProfileImg = iv;
    }


    public void selectImage() {
        final CharSequence[] items = { "Hacer foto con la cámara", "Elegir de la galería",
                "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(context);
                if (items[item].equals("Hacer foto con la cámara")) {
                    userChoosenTask="Hacer foto con la cámara";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Elegir de la galería")) {
                    userChoosenTask="Elegir de la galería";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //intent for launch camera
    public void cameraIntent()
    {
        //        destination = new File(Environment.getExternalStorageDirectory(),
        //                idUser + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, destination.toString());
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    //intent for launch gallery
    public void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                try {
                    onSelectFromGalleryResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(Intent data) throws IOException {
        thumbnail = null;
        Uri imagePath = data.getData();

        if (data != null) {

            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imagePath);
            destination = new File(Environment.getExternalStorageDirectory(), idUser+".jpg");

            savePhoto(thumbnail, destination);

            Picasso.with(getApplicationContext())
                    .load(imagePath)
                    .fit()
                    .centerCrop()
                    .transform(transformation)
                    .into(ivProfileImg);
        }
    }

    public void savePhoto(Bitmap photo, File destinationFile){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        FileOutputStream fo;

        try {
            destinationFile.createNewFile();
            fo = new FileOutputStream(destinationFile);
            fo.write(bytes.toByteArray());
            fo.close();
            Log.d("savePhoto()","Photo -> "+destinationFile.getPath()+" saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        destination = new File(Environment.getExternalStorageDirectory(),
                idUser + ".jpg");

        savePhoto(thumbnail, destination);

        Uri imagePath = Uri.fromFile(destination);
        Log.d("CAMERA",imagePath.toString());
        Picasso.with(getApplicationContext())
                .load(imagePath)
                .fit()
                .centerCrop()
                .transform(transformation)
                .into(ivProfileImg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    File getDestination(){ return destination;}

    void setImageView(ImageView im) { ivProfileImg = im; }
}
