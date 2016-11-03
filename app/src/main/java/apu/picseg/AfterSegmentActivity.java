package apu.picseg;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

//

import android.content.Intent;

import java.io.File;
import android.media.Image;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;

//
//
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
//


public class AfterSegmentActivity extends AppCompatActivity {
    ImageView mainComponent;
    ImageView backComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_segment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        mainComponent=(ImageView) findViewById(R.id.mainComponentImage);
        backComponent=(ImageView) findViewById(R.id.subComponentImage);

        Bitmap bmp1 = (Bitmap) getIntent().getParcelableExtra("mainOperation");
        mainComponent.setImageBitmap(bmp1);
       // Bitmap bmp2 = (Bitmap) getIntent().getParcelableExtra("backOperation");
        //backComponent.setImageBitmap(bmp2);
        //
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
    }

}
