package apu.picseg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class CaptureToSegmentActivity extends AppCompatActivity {

    ImageView mainComponent;
    ImageView backComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_to_segment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        Intent intent=getIntent();
        Bitmap bmp1 = (Bitmap) intent.getParcelableExtra("mainOperation");
        // mainComponent.setImageBitmap(bmp1);

        mainComponent=(ImageView) findViewById(R.id.mainComponentImage);
        backComponent=(ImageView) findViewById(R.id.subComponentImage);


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
    }

}
