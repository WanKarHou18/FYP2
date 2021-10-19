package com.example.fyp.animation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp.R;
import com.example.fyp.profile.UserLoginActivity;


public class splash extends AppCompatActivity {

    // logo, text1, text2
    private TextView ivSplash1;
    //private TextView tvSplash1;
    private TextView tvSplash2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash1 = findViewById(R.id.tv_logo);


        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.transition);
        ivSplash1.startAnimation(myAnim);


        final Intent i = new Intent(this, UserLoginActivity.class);

        Thread timer = new Thread(){
            public void run()
            {
                try{
                    sleep(3000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }

}