package com.example.calculatorforprogrammer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class StartActivity extends Activity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Thread thread = new Thread(this);
        thread.start();
    }


    public void run(){
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(StartActivity.this,AdActivity.class);
        startActivity(intent);
        finish();
    }
}
