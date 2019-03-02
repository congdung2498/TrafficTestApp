package com.example.traffictest;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.startingscreen.R;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPaper;
    private int[] layouts= {R.layout.activity_first_slide,R.layout.activity_second_slide,R.layout.activity_third_slide,R.layout.activity_fourth_slide};
    private MpaperAdapter mpaperAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button bnNext,bnSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new PreferenceManager(this).checkPreference()){
            loadHome();
        }

        if(Build.VERSION.SDK_INT >=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        mPaper = (ViewPager) findViewById(R.id.viewPager);
        mpaperAdapter = new MpaperAdapter(layouts,this);
        mPaper.setAdapter(mpaperAdapter);

        Dots_Layout = (LinearLayout)findViewById(R.id.dotsLayout);

        bnNext = (Button)findViewById(R.id.bnNext);
        bnSkip = (Button)findViewById(R.id.bnSkip);
        bnNext.setOnClickListener(this);
        bnSkip.setOnClickListener(this);

        createDots(0);

        mPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if(i==layouts.length-1){
                    bnNext.setText("Start");
                    bnSkip.setVisibility(View.INVISIBLE);
                }
                else {
                    bnNext.setText("Next");
                    bnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void createDots(int current_position){
        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();
        dots = new ImageView[layouts.length];
        for(int i=0; i< layouts.length;i++){
            dots[i] = new ImageView(this);
            if(i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnNext:
                loadNextSlide();
                break;
            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;

        }
    }

    private void loadHome(){
        startActivity(new Intent(this,Dashboard.class));
        finish();
    }
    private void loadNextSlide(){
        int next_slide = mPaper.getCurrentItem()+1;
        if(next_slide<layouts.length){
            mPaper.setCurrentItem(next_slide);
        }
        else {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }

}
