package study.lily.mvpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import study.lily.mvpro.demo.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view) {
        startActivity(new Intent(MainActivity.this, TestActivity.class));
    }
}
