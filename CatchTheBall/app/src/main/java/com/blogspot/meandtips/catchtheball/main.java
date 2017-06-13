package com.blogspot.meandtips.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView orange;
    private ImageView box;
    private ImageView pink;
    private ImageView black;

    //Button pause
    private Button pauseBtn;

    //Kích cỡ
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //Tọa độ các đối tượng
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    //Tốc độ game
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    //Điểm số
    private int score = 0;

    //Khởi tạo
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;
    //Kiểm tra trạng thái
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Kiểm tra trạng thái nút Pause
    private boolean pause_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        scoreLabel = (TextView)findViewById(R.id.scoreLabel);
        startLabel = (TextView)findViewById(R.id.startLabel);
        box = (ImageView)findViewById(R.id.box);
        orange = (ImageView)findViewById(R.id.orange);
        pink = (ImageView)findViewById(R.id.pink);
        black = (ImageView)findViewById(R.id.black);

        pauseBtn = (Button)findViewById(R.id.pauseBtn);

        //Lấy kích cỡ màn hình
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Với loại màn hình Nexus width 768 height 1184
        //Cho Speed của box là 20, orange là 12, pink là 20, black là 16

        boxSpeed = Math.round(screenHeight / 60F); // 1184 / 60 = 19.733 => 20
        orangeSpeed = Math.round(screenWidth / 60F);
        pinkSpeed = Math.round(screenWidth / 36F);
        blackSpeed = Math.round(screenWidth / 45F);

        //Log.v("SPEED_BOX", boxSpeed+"");
        //Log.v("SPEED_ORANGE", orangeSpeed+"");
        //Log.v("SPEED_PINK", pinkSpeed+"");
        //Log.v("SPEED_BLACK", blackSpeed+"");

        //Di chuyển các đối tượng ẩn khỏi màn hình chơi
        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setY(-80.0f);
        pink.setY(-80.0f);
        black.setY(-80.0f);
        black.setY(-80.0f);

        scoreLabel.setText("Score : 0");
    }

    public void changePos(){

        hitCheck();

        //Đối tượng orange
        orangeX -= orangeSpeed;
        if (orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int)Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Đối tượng black
        blackX -= blackSpeed;
        if (blackX < 0){
            blackX = screenWidth + 10;
            blackY = (int)Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //Đối tượng pink
        pinkX -= pinkSpeed;
        if (pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (int)Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);


        // Di chuyển đối tượng
        if (action_flg == true){
            boxY -= boxSpeed;
        } else{
            boxY += boxSpeed;
        }

        // Kiểm tra vị trí của đối tượng
        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;
        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void pausePushed(View view){
        if(pause_flg == false){
            pause_flg = true;
            //Dừng lại timer;
            timer.cancel();
            timer = null;

            pauseBtn.setText("PLAY");

        } else{
            pause_flg = false;

            pauseBtn.setText("PAUSE");

            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
    }

    public void hitCheck(){
        //Nếu tâm quả bóng nằm trong con box (con ăn) thì cộng điểm


        //Đối tượng Orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        if (0 <= orangeCenterX && orangeCenterX <= boxSize &&
                boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){
            score += 10;
            orangeX = -10;
            sound.playHitSound();
        }

        //Đối tượng Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize &&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){
            score += 30;
            pinkX = -10;
            sound.playHitSound();
        }

        //Đối tượng Black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if (0 <= blackCenterX && blackCenterX <= boxSize &&
                boxY <= blackCenterY && blackCenterY <= boxY + boxSize){
            timer.cancel();
            timer = null;

            sound.playOverSound();

            //Hiển thị kết quả
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }

    }

    public boolean onTouchEvent(MotionEvent me){
        if (start_flg == false){

            start_flg = true;

            // Lấy kích cỡ các đối tượng

            FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);


        }else{
            if (me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
