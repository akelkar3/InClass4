package com.example.shubhra.inclass4;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Launcher extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_launcher );

        setTitle( "Password Generator" );

        Handler handler = new Handler(  );
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent( Launcher.this, MainActivity.class);
                startActivity( intent );
            }
        },3000 );

    }
}
