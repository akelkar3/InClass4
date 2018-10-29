/*
a. Assignment: In Class 4.
b. File Name: InClass04_Group20
c. Full name of all students in your group: Shubhra Mishra , Ankit Kelkar*/

package com.example.shubhra.inclass4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    SeekBar sbCnt;
    SeekBar sbLen;
    TextView tvCnt;
    TextView tvLen;
    TextView tvFinalPassword;
    Button btnAsync;
    Button btnThread;

    public static int passwordCnt = 1, passwordLen = 8;
    Handler handler;
    ExecutorService threadPool;
    ProgressDialog progressDialog;
    ArrayList<String> passwords = new ArrayList<>(  );

    //Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle( "Password Generator..." );


        threadPool = Executors.newFixedThreadPool( 2 );

        Log.d("demo", "Main thread ID:" +Thread.currentThread().getId());
        sbCnt=findViewById(R.id.sbPwdCnt);
        sbLen=findViewById(R.id.sbPwdLength);
        tvCnt=findViewById(R.id.textSetPwdCnt);
        tvLen=findViewById(R.id.textSetPwdLength);
        btnAsync=findViewById(R.id.btnPwdAsync);
        btnThread=findViewById(R.id.btnPwdThread);
        tvFinalPassword=findViewById(R.id.tvFinalPwd);

        //Setting Maximum Values of the SeekBar
        /*sbCnt.setMax(10);
        sbLen.setMax(23);*/

        // Handling the behavior of Seekbar Count
        sbCnt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passwordCnt= sbCnt.getProgress()+1;
                tvCnt.setText(String.valueOf(passwordCnt));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Handling the seekbar length
        sbLen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passwordLen= sbLen.getProgress()+8;
                tvLen.setText(String.valueOf(passwordLen));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Handling of Thread Button
        btnThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwords.clear();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMax(passwordCnt);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                for (int i = 0; i < passwordCnt; i++){
                    threadPool.execute( new Runnable() {
                        @Override
                        public void run() {
                            String password = Util.getPassword(passwordLen);
                            Message msg = new Message();
                            msg.obj = password;
                            handler.sendMessage( msg );
                        }
                    } );
                }

            }
        });

        //Handler Tasks
        handler = new Handler( new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                passwords.add((String) message.obj);
                progressDialog.incrementProgressBy(1);

                if(passwords.size() == passwordCnt){
                    progressDialog.dismiss();
                    final String[] generatedPasswords = passwords.toArray(new String[passwords.size()]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Passwords").
                            setItems(generatedPasswords, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tvFinalPassword.setText(generatedPasswords[i]);

                                }
                            }).show();
                }
                return false;
            }
        } );




        // Handling of Async Button
        btnAsync.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwords.clear();
                new DoWorkAsync().execute();
            }
        } );

    }


//AsyncTask
    class DoWorkAsync extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Generating passwords...");
            progressDialog.setCancelable(false);
            progressDialog.setMax(passwordCnt);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

            Log.d("demo", "onPreExecute thread ID:" +Thread.currentThread().getId());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            progressDialog.dismiss();
            final String[] generatedPasswords = passwords.toArray(new String[passwords.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Passwords").
                    setItems(generatedPasswords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvFinalPassword.setText(generatedPasswords[i]);
                        }
                    }).show();

            Log.d("demo", "onPostExecute thread ID:" +Thread.currentThread().getId());
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //super.onProgressUpdate(values);
            Log.d("demo", "onProgressUpdate thread ID:" +Thread.currentThread().getId());
            progressDialog.incrementProgressBy(1);
            passwords.add(values[0]);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Log.d("demo", "doInBackground thread ID:" +Thread.currentThread().getId());
            String newPassword;
            for(int i =0;i<passwordCnt;i++)
            {
                newPassword = Util.getPassword(passwordLen);
                publishProgress(newPassword);
            }
            return 0;
        }
    }

}
