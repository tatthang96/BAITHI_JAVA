package com.blogspot.meandtips.catchtheball;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class post extends AppCompatActivity {
    JSONObject response, profile_pic_data, profile_pic_url;
    TextView username, user_email, nameLabel;
    ImageView user_picture, pic;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_post);
        Button button = (Button) findViewById(R.id.button);
        Button postBtn = (Button)findViewById(R.id.postBtn);
        //Button imageShare = (Button) findViewById(R.id.imageShare);
        nameLabel = (TextView) findViewById(R.id.nameLabel);
        pic = (ImageView) findViewById(R.id.pic);
        Intent intent = getIntent();
        //String jsondata = intent.getStringExtra("jsondata");

        shareDialog = new ShareDialog(this);
        postBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (shareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setImageUrl(Uri.parse("https://4.bp.blogspot.com/-BRlXg7ky-mc/WUAu1zXNPnI/AAAAAAAACOs/tSz143p6w30DSOlDrzBh3Mfyf59jwf_uACLcB/s320/icon.PNG"))
                            .setContentDescription("Ứng dụng Game Catch The Ball")
                            .setContentUrl(Uri.parse("https://meandtips.blogspot.com/2017/06/bai-thi-tran-tat-thang-catch-ball-game.html"))
                            .setContentTitle("Tao thu title")
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });

        SharedPreferences settings = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String jsondata = settings.getString("JSON_DATA", null);
        //nameLabel.setText(jsonData);

        try {
            response = new JSONObject(jsondata);
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            nameLabel.setText(response.get("name").toString());
            //pic.setBackground(E);
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(pic);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }




    public void ShareDialog(Bitmap imagePath) {

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("Testing")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content);

    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
    }

}
