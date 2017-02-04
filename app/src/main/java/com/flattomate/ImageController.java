package com.flattomate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;


public class ImageController extends AppCompatActivity implements Parcelable{

    public File destination;
    public Bitmap thumbnail;
    public Transformation transformation;
    String userChoosenTask;
    final int REQUEST_CAMERA = 0;
    final int SELECT_FILE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_IMAGE_CAPTURE = 3;

    int idUser;
    public ImageView ivProfileImg;
    Context context;
    Activity activity;
    Intent intent = new Intent(ACTION_IMAGE_CAPTURE);

    static public ArrayList<Uri> images = new ArrayList<>();

    public ImageController(Context c, Activity activity, int id, ImageView iv){
        context = c;
        idUser = id;
        ivProfileImg = iv;
        this.activity = activity;

        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(2)
                .oval(false)
                .build();
    }

    public ImageController(Parcel in) {
        destination = new File(in.readString());
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    //intent for launch camera
   /*public void cameraIntent() throws IOException {
        Uri imageUri = Uri.fromFile(createImageFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(EXTRA_OUTPUT, imageUri);
        Log.d("extra", intent.getExtras().get("output").toString());
        //activity.startActivityForResult(intent, REQUEST_CAMERA);

       if (intent.resolveActivity(getPackageManager()) != null)
           activity.startActivityForResult(intent, REQUEST_CAMERA);
    }*/

    //intent for launch gallery
    public void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"),SELECT_FILE);
    }

    @SuppressWarnings("deprecation")
    public void onSelectFromGalleryResult(Intent data) throws IOException {
        Uri imagePath = data.getData();
        destination = new File(imagePath.getPath());

        if (data != null) {
            putImage(imagePath);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*public void savePhoto(Bitmap photo, File destinationFile){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        FileOutputStream fo;

        //if folder flattomate does not exist we create it
        File folder = new File(Environment.getExternalStorageDirectory() + "/flattomate");
        if (!folder.exists()) {
            folder.mkdir();
        }

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

        images.add(Uri.fromFile(destinationFile));
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case Activity.RESULT_OK:

                try {
                    if (requestCode == SELECT_FILE)
                        onSelectFromGalleryResult(data);
                    else if (requestCode == REQUEST_CAMERA) { //the photo is returned and positioned
                        if(data != null){
                            Uri imagePath = (Uri)data.getExtras().get("output");
                            Uri imageThumbnail = (Uri)data.getData();
                            images.add(imagePath);

                            if(imageThumbnail == null)
                                putImage(imagePath);
                            else
                                putImage(imageThumbnail);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    /*public File createImageFile() throws IOException {
        // Create an image file name
        Calendar cal = Calendar.getInstance();
        String imageFileName =  ""+ cal.getTimeInMillis();
        Log.d("storagedir", Environment.getExternalStorageDirectory() + "/flattomate");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
            //    ".jpg",         /* suffix */
          //      storageDir      /* directory */
        //);

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        //return image;
    //}*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                Log.d("permissions", "checking");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Hacer foto con la cámara"))
                        startActivityForResult(intent, REQUEST_CAMERA); //cameraIntent();
                    else if(userChoosenTask.equals("Elegir de la galería"))
                        galleryIntent();
                } else {
                    //code for deny
                    Toast.makeText(context, "Debes de dar permisos a la aplicación para poder insertar fotos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    File getDestination(){ return destination;}

    public void setImageView(ImageView im) { ivProfileImg = im; }

    public void putImage(Uri imagePath) {
        Picasso.with(context)
                .load(imagePath)
                .fit()
                .centerCrop()
                .transform(transformation)
                .into(ivProfileImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destination.getPath());
    }

    public static final Parcelable.Creator<ImageController> CREATOR
            = new Parcelable.Creator<ImageController>() {
        public ImageController createFromParcel(Parcel in) {
            return new ImageController(in);
        }

        public ImageController[] newArray(int size) {
            return new ImageController[size];
        }
    };

    //Save photo in device
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

}
