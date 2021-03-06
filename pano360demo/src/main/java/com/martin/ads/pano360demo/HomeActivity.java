package com.martin.ads.pano360demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.github.rubensousa.viewpagercards.CardItem;
import com.github.rubensousa.viewpagercards.CardPagerAdapter;
import com.github.rubensousa.viewpagercards.ShadowTransformer;
import com.martin.ads.vrlib.ui.Pano360ConfigBundle;
import com.martin.ads.vrlib.ui.PanoPlayerActivity;
import com.martin.ads.vrlib.ext.GirlFriendNotFoundException;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private CheckBox planeMode;
    private boolean flag;

    private String filePath="~(～￣▽￣)～";
    private String videoHotspotPath;
    private boolean imageModeEnabled;
    private boolean planeModeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.content_text_1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.content_text_2));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.content_text_3));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.content_text_4));
        mCardAdapter.addCardItem(new CardItem(R.string.title_5, R.string.content_text_5));
        mCardAdapter.addCardItem(new CardItem(R.string.title_6, R.string.content_text_6));

        planeMode= (CheckBox) findViewById(R.id.plane_mode);

        mCardAdapter.setOnClickCallback(new CardPagerAdapter.OnClickCallback() {
            @Override
            public void onClick(int position) {
                imageModeEnabled=false;
                videoHotspotPath=null;
                switch (position){
                    case 0:
                        filePath= "android.resource://" + getPackageName() + "/" + R.raw.demo_video;
                        break;
                    case 1:
                        Intent intent=new Intent(HomeActivity.this, FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.ARG_FILTER, Pattern.compile("(.*\\.mp4$)||(.*\\.avi$)||(.*\\.wmv$)"));
                        startActivityForResult(intent, 1);
                        return;
                    case 2:
                        filePath="images/vr_cinema.jpg";
                        videoHotspotPath="android.resource://" + getPackageName() + "/" + R.raw.demo_video;
                        imageModeEnabled=true;
                        break;
                    case 3:
                        filePath="images/texture_360_n.jpg";
                        imageModeEnabled=true;
                        break;
                    case 4:
                        filePath="http://cache.utovr.com/201508270528174780.m3u8";
                        break;
                    case 5:
                        if(flag) throw new GirlFriendNotFoundException();
                        else {
                            Toast.makeText(HomeActivity.this,"再点会点坏的哦~",Toast.LENGTH_LONG).show();
                            flag=true;
                        }
                        return;
                }
                planeModeEnabled=planeMode.isChecked();
                start(true);
            }
        });
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);

        mViewPager.setOffscreenPageLimit(3);

        mCardShadowTransformer.enableScaling(true);
    }

    private void start(boolean usingDefaultActivity){
        Pano360ConfigBundle configBundle=Pano360ConfigBundle
                .newInstance()
                .setFilePath(filePath)
                .setImageModeEnabled(imageModeEnabled)
                .setPlaneModeEnabled(planeModeEnabled)
                .setRemoveHotspot(false)
                .setVideoHotspotPath(videoHotspotPath);
        if(usingDefaultActivity)
            configBundle.startEmbeddedActivity(this);
        else {
            Intent intent=new Intent(this,DemoWithGLSurfaceView.class);
            intent.putExtra(PanoPlayerActivity.CONFIG_BUNDLE,configBundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            planeModeEnabled=planeMode.isChecked();
            start(true);
        }
    }

}
