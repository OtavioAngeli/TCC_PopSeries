package uniandrade.br.edu.com.popseries.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uniandrade.br.edu.com.popseries.R;

public class SplashScreen extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(this, 3000);

    }

    @Override
    public void run(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
