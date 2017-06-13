package com.blogspot.meandtips.catchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = (TextView)findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView)findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore){
            highScoreLabel.setText("High Score : " + score);

            //Lưu lại
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);

            editor.commit();
        }else{
            highScoreLabel.setText("High Score : " + highScore);
        }

    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), start.class));
    }

    //Không cho nút Return hoạt động
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void shareFacebook(View view){
        startActivity(new Intent(getApplicationContext(), post.class));
    }
}
