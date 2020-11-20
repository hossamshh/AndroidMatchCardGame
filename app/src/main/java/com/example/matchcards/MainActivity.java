package com.example.matchcards;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;

enum Animal {lion, tiger, elephant, cow};

class Card implements View.OnClickListener {
    static AppCompatActivity ref;
    static Animal found = null;
    static MediaPlayer soundPlayer = null;
    static Card[] cards;

    ImageView img;
    Drawable drawableHidden, drawableShown;
    Animal animal;
    int file;
    boolean clickable = true, solved = false;

    Runnable flipMismatch = new Runnable() {
        @Override
        public void run() {
            for(Card c: cards){
                if(!c.solved && !c.clickable){
                    c.img.setImageDrawable(drawableHidden);
                    c.clickable = true;
                }
            }
            found = null;
        }
    };

    public Card(ImageView img, Drawable drawableHidden, Drawable drawableShown, Animal animal, int file){
        this.img = img;
        this.drawableHidden = drawableHidden;
        this.drawableShown = drawableShown;
        this.animal = animal;
        this.file = file;

        img.setImageDrawable(drawableHidden);
        img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        img.setOnClickListener(this);
    }

    public static void setRef(AppCompatActivity act){
        ref = act;
    }
    public static void setCards(Card[] c) { cards = c; }
    public static void releasePlayer() {
        if(soundPlayer != null) {
            soundPlayer.release();
            soundPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        if(clickable) {
            img.setImageDrawable(drawableShown);
            clickable = false;

            releasePlayer();
            soundPlayer = MediaPlayer.create(ref, file);
            soundPlayer.start();
            soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    releasePlayer();
                }
            });

            if (found == null) {
                found = animal;
            }
            else if (animal == found) {
                for(Card c: cards) {
                    if (c.animal == animal) c.solved = true;
                }
                found = null;
            }
            else {
                img.postDelayed(flipMismatch, 500);
                found = null;
            }
        }
        else if(!solved) {
            img.setImageDrawable(drawableHidden);
            clickable = true;
            found = null;
        }
    }
}

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    LinearLayout mainGridLayout;

    Card[] cards = new Card[8];
    Runnable timer;

    ArrayList<Integer> cardsPositions;
    int seconds;

    boolean playing;

    void initCards() {
        Drawable hiddenCard = getDrawable(R.drawable.back);
        cards[0] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.lion), Animal.lion, R.raw.lion);
        cards[1] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.lion), Animal.lion, R.raw.lion);
        cards[2] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.tiger), Animal.tiger, R.raw.tiger);
        cards[3] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.tiger), Animal.tiger, R.raw.tiger);
        cards[4] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.elephant), Animal.elephant, R.raw.elephant);
        cards[5] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.elephant), Animal.elephant, R.raw.elephant);
        cards[6] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.cow), Animal.cow, R.raw.cow);
        cards[7] = new Card(new ImageView(getApplicationContext()), hiddenCard, getDrawable(R.drawable.cow), Animal.cow, R.raw.cow);
        Card.setCards(cards);
        Card.setRef(this);
    }

    private boolean isUnique(int key) {
        for(int i: cardsPositions){
            if(i == key) return false;
        }
        return true;
    }

    private void initCardsPositions() {
        cardsPositions = new ArrayList<>();
        for(int i=0; i<8; i++){
            int index = (int)(Math.random()*100)%8;
            while(!isUnique(index)) {
                index = (int)(Math.random()*100)%8;
            }
            cardsPositions.add(index);
        }
    }

    private void initUI() {
        int orientation =  this.getResources().getConfiguration().orientation;
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(0, 0, 0, 20);
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout row1 = new LinearLayout(this);
            row1.setOrientation(LinearLayout.HORIZONTAL);
            row1.setLayoutParams(lp);
            row1.addView(cards[cardsPositions.get(0)].img);
            row1.addView(cards[cardsPositions.get(1)].img);
            mainGridLayout.addView(row1);

            LinearLayout row2 = new LinearLayout(this);
            row2.setOrientation(LinearLayout.HORIZONTAL);
            row2.setLayoutParams(lp);
            row2.addView(cards[cardsPositions.get(2)].img);
            row2.addView(cards[cardsPositions.get(3)].img);
            mainGridLayout.addView(row2);

            LinearLayout row3 = new LinearLayout(this);
            row3.setOrientation(LinearLayout.HORIZONTAL);
            row3.setLayoutParams(lp);
            row3.addView(cards[cardsPositions.get(4)].img);
            row3.addView(cards[cardsPositions.get(5)].img);
            mainGridLayout.addView(row3);

            LinearLayout row4 = new LinearLayout(this);
            row4.setOrientation(LinearLayout.HORIZONTAL);
            row4.setLayoutParams(lp);
            row4.addView(cards[cardsPositions.get(6)].img);
            row4.addView(cards[cardsPositions.get(7)].img);
            mainGridLayout.addView(row4);
        }
        else {
            LinearLayout row1 = new LinearLayout(this);
            row1.setOrientation(LinearLayout.HORIZONTAL);
            row1.setLayoutParams(lp);
            row1.addView(cards[cardsPositions.get(0)].img);
            row1.addView(cards[cardsPositions.get(1)].img);
            row1.addView(cards[cardsPositions.get(2)].img);
            row1.addView(cards[cardsPositions.get(3)].img);
            mainGridLayout.addView(row1);

            LinearLayout row2 = new LinearLayout(this);
            row2.setOrientation(LinearLayout.HORIZONTAL);
            row2.setLayoutParams(lp);
            row2.addView(cards[cardsPositions.get(4)].img);
            row2.addView(cards[cardsPositions.get(5)].img);
            row2.addView(cards[cardsPositions.get(6)].img);
            row2.addView(cards[cardsPositions.get(7)].img);
            mainGridLayout.addView(row2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.time);
        mainGridLayout = findViewById(R.id.mainLayout);

        timer = new Runnable() {
            @Override
            public void run() {

                boolean allSolved = true;
                for(Card c: cards) allSolved &= c.solved;

                if(allSolved) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("time", seconds-1);
                    startActivity(intent);
                    playing = false;
                    return;
                }

                String str = (seconds/60) + ":" + (seconds%60 < 10? "0" : "") + (seconds++%60);
                timeView.setText(str);

                timeView.postDelayed(timer, 1000);
            }
        };

        Button startGame = new Button(this);
        startGame.setText(R.string.start);
        startGame.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mainGridLayout.addView(startGame);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLayout(); playing = true;
            }
        });


    }

    private void initLayout() {
        if(!playing){
            seconds = 0;
            initCardsPositions();
            initCards();
            mainGridLayout.removeAllViews();
            initUI();
            timer.run();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initLayout();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Card.releasePlayer();
        timeView.removeCallbacks(timer);
    }

}