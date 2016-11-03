package apu.picseg;
import android.content.Intent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.Image;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Bitmap;


import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.common.api.GoogleApiClient;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
//
public class ImageActivity extends AppCompatActivity {


    private ImageView imageView;
    private String array_spinner_combobox[];//for combo box

    private ProgressBar progressBarImgAct1;//
    private ProgressBar progressBarImgAct2;

    private File imageFileSaved;
    private File imageFile;
    public static final int CAMERA_REQUEST = 10;
    private ImageView imgSpecimenPhoto;
    private Uri photo;
    //jiban
    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;
    int[][] kernal = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
    };
    ImageView imageSource, imageAfter;
    Bitmap bitmap_Source;
    ProgressBar progressBar;
    private Handler handler;
    Bitmap afterProcess;
    //jiban

    int[][] visit;
Spinner s;
    final float Thresehold = (float) 12;
    //
    private Bitmap bmp;
    private Bitmap operation;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgSpecimenPhoto=(ImageView) findViewById(R.id.captureImageView);
        imageView=(ImageView) findViewById(R.id.captureImageView);

        progressBarImgAct1=(ProgressBar)findViewById(R.id.progressBar1);
        // progressBarImgAct2=(ProgressBar)findViewById(R.id.progressBar2);


        /*String imagename = "takephoto";
        int res = getResources().getIdentifier(imagename, "drawable", this.getPackageName());
        imgSpecimenPhoto.setImageResource(res);*/

        //for combo box
       /*array_spinner_combobox=new String[5];
        array_spinner_combobox[0]="Mean Thresholding";
        array_spinner_combobox[1]="RGB Based";
        array_spinner_combobox[2]="Color Based Thresholding";
        array_spinner_combobox[3]="GrayScale Segmentation";
        array_spinner_combobox[4]="option 5";
        //*/

        array_spinner_combobox=new String[5];
        array_spinner_combobox[0]="RGB Based";
        array_spinner_combobox[1]="Mean Thresholding";
       // array_spinner_combobox[2]="Binary Thresholding";
        array_spinner_combobox[2]="GrayScale Segmentation";
        array_spinner_combobox[3]="Color Based Thresholding";
        array_spinner_combobox[4]="Edge Based Segmentation Technique";
        //


         s = (Spinner) findViewById(R.id.SpinnerComboBox);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner_combobox);
        s.setAdapter(adapter);

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //grayScale Segmentation
    public  int truncate(int a) {
        if      (a <   0) return 0;
        else if (a > 255) return 255;
        else              return a;
    }
    public void EdgeDetector(View view) {
        // truncate color component to be between 0 and 255
        int[][] filter1 = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] filter2 = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        Bitmap pic0;
        Bitmap pic1;
        pic0 = operation;
        int magnitude = 0;
        pic1=operation;

        //Picture pic0 = new Picture(args[0]);

        //int height   = pic0.height();
        //Picture pic1 = new Picture(width, height);
        int width = pic0.getWidth();
        int height = pic0.getHeight();
        //

        //

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // get 3-by-3 array of colors in neighborhood
                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        //luminance = 0.27 red + 0.67 green + 0.06 blue.
                        int pix = pic0.getPixel(x - 1 + i, y - 1 + j);
                        int lum = (int) (Color.red(pix) * .27 + .67 * Color.green(pix) + Color.blue(pix) * .06);
                        //gray[i][j] = (int) Luminance.lum(pic0.get(x-1+i, y-1+j));
                        gray[i][j] = lum;
                    }
                }

                // apply filter
                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }
                // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
                magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                int p = pic0.getPixel(x, y);

                pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));
                //Color grayscale = new Color(magnitude, magnitude, magnitude);

                //pic1.set(x, y, grayscale);
            }
        }
        //pic0.show();
        //pic1.show();
        imgSpecimenPhoto.setImageBitmap(pic0);
        // pic1.save("baboon-edge.jpg");
        for(int i=0;i<10000;i++);


        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                int newPix = pic0.getPixel(x, y);
                int r = Color.red(newPix);
                int g = Color.green(newPix);
                int b = Color.blue(newPix);

                int newPix2 = pic1.getPixel(x, y);
                int r2 = Color.red(newPix2);
                int g2 = Color.green(newPix2);
                int b2 = Color.blue(newPix2);

                if (r ==0 && g == 0 && b == 0) {
                    pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), 255, 255, 255));

                } else {
                    pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), r2, g2, b2));

                }

                //pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));

            }


        }

        imgSpecimenPhoto.setImageBitmap(pic1);
    }

    //
public void AdaptiveThresHolding(View view){

    //Bitmap bp = (Bitmap) data.getExtras().get("data");
    //Bitmap operation=Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), bp.getConfig());
    Bitmap bp=operation;
    int row=bp.getWidth();
    int col=bp.getHeight();
    double intImg[][]=new double[row+1][col+1];
    double sum=0;
    for(int j=0;j<col;j++){
        sum=0;
        for(int i=0;i<row;i++){
            int p = bp.getPixel(i,j);
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);
            int alpha = Color.alpha(p);
            double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
            sum=sum+ta;
            if(i==0)
                intImg[i][j]=sum;
            else
                intImg[i][j]=intImg[i-1][j]+sum;
        }
    }
    int s=row/8;
    int t=15;
    int x1,y1,x2,y2,count;
    for(int i=0;i<row;i++)
    {
        for(int j=0;j<col;j++)
        {
            x1=i-(s/2);
            x2=i+(s/2);
            y1=j-(s/2);
            y2=j+(s/2);
            count=(x2-x1)*(y2-y1);
            sum=intImg[x2][y2]-intImg[x2][y1-1]-intImg[x1-1][y2]+intImg[x1-1][y1-1];
            int p = bp.getPixel(i, j);
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);
            int alpha = Color.alpha(p);
            double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
            if((ta*count)<=(sum*(100-t)/100))
                bp.setPixel(i, j, Color.argb(Color.alpha(p), 0,0,0));
            else
                bp.setPixel(i, j, Color.argb(Color.alpha(p),255,255,255));
        }
    }
    imgSpecimenPhoto.setImageBitmap(bp);
}

    public void AdaptiveThresHolding1(View view){

       // Bitmap bp = (Bitmap) data.getExtras().get("data");
        //Bitmap operation=Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), bp.getConfig());
        Bitmap bp=operation;
        int row=bp.getWidth();
        int col=bp.getHeight();
        double intImg[][]=new double[row+1][col+1];
        double sum=0;
        for(int i=0;i<row;i++){
            sum=0;
            for(int j=0;j<col;j++){
                int p = bp.getPixel(i,j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                sum=sum+ta;
                if(i==0)
                    intImg[i][j]=sum;
                else
                    intImg[i][j]=intImg[i-1][j]+sum;
            }
        }
        int s=row/8;
        int t=15;
        int x1,y1,x2,y2,count;
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                x1=i-(s/2);
                x2=i+(s/2);
                y1=j-(s/2);
                y2=j+(s/2);
                count=(x2-x1)*(y2-y1);
                sum=intImg[x2][y2]-intImg[x2][y1-1]-intImg[x1-1][y2]+intImg[x1-1][y1-1];
                int p = bp.getPixel(i,j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                if((ta*count)<=(sum*(100-t)/100))
                    operation.setPixel(i, j, Color.argb(Color.alpha(p), 0,0,0));
                else
                    operation.setPixel(i, j, Color.argb(Color.alpha(p),255,255,255));
            }
        }
        imgSpecimenPhoto.setImageBitmap(operation);


       // imgSpecimenPhoto.setImageBitmap(bp);
    }



    //
    Color white;
    public void startSegment(View view) throws IOException {
        Log.i("APU", "EnterStartsegment");
        int tr, tg, tb;
        tr = 90;
        tg = 127;
        tb = 127;
        Bitmap ope;
        ope=operation;
        Log.i("APU", "creation1stBitmap");
        // backOperation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Log.i("APU", "creation2ndBitmap");
        //mainOperation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Log.i("APU", "creation3rdBitmap");

        for (int i = 0; i < ope.getHeight() - 1; i++) {
            for (int j = 0; j < ope.getWidth() - 1; j++) {
                int p = ope.getPixel(j, i);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                int tc = ope.getPixel(j, i);
                int bc = ope.getPixel(j, i + 1);
                float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;
                /*
                if (Math.abs(ta - ba) < Thresehold) {
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                   // imgSpecimenPhoto.setImageBitmap(operation);
                } else {

                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 0, 0));
                    //imgSpecimenPhoto.setImageBitmap(operation);
                }*/
                int red = (int) (0.21 * r + 0.71 * g + 0.07 * b);
                if (r >= 0 && r <= tr) {
                    ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                } else if (r > tr) {
                    ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                    //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));

                }
                if (g >= tg && g <= 255) {

                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));

                    ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                } else if (g < tg) {
                    ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));

                    // backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                }
                if (b >= tb && b <= 255) {
                    ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                } else if (b < tb) {
                    ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                    //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                }
                //operation.setPixel(j, i, Color.argb(Color.alpha(p), red, red, red));
                //float ba=(red(bc)+green(bc)+blue(bc))/3.0;
            }
        }
        //imageView.setImageBitmap(operation);
        imgSpecimenPhoto.setImageBitmap(ope);

        Log.i("APU", "Segment finish");

    }

    //this is the end part of method A*/
    public void startCamera(View view) {
        //imgSpecimenPhoto = (ImageView) findViewById(R.id.captureImageView);
        Log.i("image activity", "start camera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        long time = System.currentTimeMillis();
        String str;
        str = "" + time + "photo.jpg";

        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str);
        Uri tempUri = Uri.fromFile(imageFile);
        photo = Uri.fromFile(imageFile);//for global variable
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        // cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                if (imageFile.exists()) {


                    imgSpecimenPhoto.setImageURI(photo);
                    Toast.makeText(this, "The image file is saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "The image file is not saved at ", Toast.LENGTH_LONG).show();
                }
            }
        }
        imgSpecimenPhoto.buildDrawingCache();
        Bitmap bmap = imgSpecimenPhoto.getDrawingCache();
        operation=bmap;
        //String imagename = "takephoto";
        // int res = getResources().getIdentifier(imagename, "drawable", this.getPackageName());
        // imgSpecimenPhoto.setImageResource(res);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Image Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://apu.picseg/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Image Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://apu.picseg/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void startSegment_na(View view) throws IOException {
        //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
        //bmp = bitmapDrawable.getBitmap();
        //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Bitmap abmp=operation;
        for (int i = 1; i < abmp.getHeight() - 2; i++) {
            for (int j = 1; j < abmp.getWidth() - 2; j++) {
                int p = abmp.getPixel(j, i);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                alpha = 0;
                int tc = abmp.getPixel(j, i);
                int bc = abmp.getPixel(j, i + 1);
                float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;
                if (Math.abs(ta-ba)>15){

                    abmp.setPixel(j, i-1, Color.argb(Color.alpha(p), 255, 255, 255));
                    abmp.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                    abmp.setPixel(j, i+1, Color.argb(Color.alpha(p), 255, 255, 255));
                }
                else{

                    abmp.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                }
                /*
                else if (ta >= 85 && ta <= 170)
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 255, 0));
                else
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 255));*/
            }
        }
        imgSpecimenPhoto.setImageBitmap(abmp);

        //save the segmented image
        long time1 = System.currentTimeMillis();
        String str1;
        str1 = "" + time1 + "simg.jpg";
        //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        FileOutputStream output;
        output = new FileOutputStream(imageFileSaved);
        operation.compress(Bitmap.CompressFormat.PNG, 0, output);
        output.close();
        Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();
        //

//
    }

    //Binary thresholding
    public void startSegment_na1(View view) throws IOException {

        //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
        //bmp = bitmapDrawable.getBitmap();
        //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Bitmap abmp=operation;
        int t = 30;
        for (int i = 0; i < abmp.getHeight() - 1; i++) {
            for (int j = 0; j < abmp.getWidth() - 1; j++) {
                int p = operation.getPixel(j, i);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                alpha = 0;
                int tc = operation.getPixel(j, i);
                int bc = operation.getPixel(j, i + 1);
                float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;
                if (Math.abs(ta-ba) <=15 )
                    abmp.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                else
                    abmp.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
            }
        }
        imgSpecimenPhoto.setImageBitmap(abmp);

        //save the segmented image
        long time1 = System.currentTimeMillis();
        String str1;
        str1 = "" + time1 + "simg.jpg";
        //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        FileOutputStream output;
        output = new FileOutputStream(imageFileSaved);
        operation.compress(Bitmap.CompressFormat.PNG, 0, output);
        output.close();
        Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();
        //
//
    }

    // private Bitmap processingBitmap(Bitmap src, int[][] knl) {
    public void processingBitmap(View view) {
        //Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Bitmap src=operation;
        int bmWidth = src.getWidth();
        int bmHeight = src.getHeight();
        int bmWidth_MINUS_2 = bmWidth - 2;
        int bmHeight_MINUS_2 = bmHeight - 2;

        for (int i = 1; i <= bmWidth_MINUS_2; i++) {
            for (int j = 1; j <= bmHeight_MINUS_2; j++) {

                //get the surround 3*3 pixel of current src[i][j] into a matrix subSrc[][]
                int[][] subSrc = new int[KERNAL_WIDTH][KERNAL_HEIGHT];
                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        subSrc[k][l] = src.getPixel(i - 1 + k, j - 1 + l);
                    }
                }

                //subSum = subSrc[][] * knl[][]
                int subSumA = 0;
                int subSumR = 0;
                int subSumG = 0;
                int subSumB = 0;

                for (int k = 0; k < KERNAL_WIDTH; k++) {
                    for (int l = 0; l < KERNAL_HEIGHT; l++) {
                        subSumR += Color.red(subSrc[k][l]) * kernal[k][l];
                        subSumG += Color.green(subSrc[k][l]) * kernal[k][l];
                        subSumB += Color.blue(subSrc[k][l]) * kernal[k][l];
                    }
                }

                subSumA = Color.alpha(src.getPixel(i, j));

                if (subSumR < 0) {
                    subSumR = 0;
                } else if (subSumR > 255) {
                    subSumR = 255;
                }

                if (subSumG < 0) {
                    subSumG = 0;
                } else if (subSumG > 255) {
                    subSumG = 255;
                }

                if (subSumB < 0) {
                    subSumB = 0;
                } else if (subSumB > 255) {
                    subSumB = 255;
                }

                src.setPixel(i, j, Color.argb(
                        subSumA,
                        subSumR,
                        subSumG,
                        subSumB));
            }
        }

        //return dest;
        imgSpecimenPhoto.setImageBitmap(src);
    }

    public void dfs(View view) {

        Log.i("APU", "EnterStartsegment");
        int tr, tg, tb;
        tr = 90;
        tg = 127;
        tb = 127;

        //BitmapDrawable bitmapDrawable1 = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
        //bmp = bitmapDrawable1.getBitmap();
        Bitmap abmp=operation;
        int w = abmp.getWidth();
        int h = abmp.getHeight();
        //operation = Bitmap.createBitmap(w, h, bmp.getConfig());

        visit = new int[h + 10][w + 10];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                visit[j][i] = 0;
            }
        }

        int cluster = 0;
        for (int j = 0; j < w - 3; j++) {
            for (int i = 0; i < h - 3; i++) {
                if (visit[i][j] != 1) {
                    dfs_visit(i, j, cluster);
                }
                cluster++;
            }
        }
        imgSpecimenPhoto.setImageBitmap(operation);
        Log.i("APU", "Segment finish");




    }

    public void dfs_visit(int i, int j, int clus) {

        int ii=i,jj=j;
        int p1 = operation.getPixel(i, j);
        int r1 = Color.red(p1);
        int g1 = Color.green(p1);
        int b1 = Color.blue(p1);
        int alpha1 = Color.alpha(p1);
        int ave1= (int) (Color.red(p1) * .27 + .67 * Color.green(p1) + Color.blue(p1) * .06);

        int p2 = operation.getPixel(i, j + 1);
        int r2 = Color.red(p2);
        int g2 = Color.green(p2);
        int b2 = Color.blue(p2);
        int alpha2 = Color.alpha(p2);
        int ave2= (int) (Color.red(p2) * .27 + .67 * Color.green(p2) + Color.blue(p2) * .06);
        if(Math.abs(ave1-ave2)<15){
            visit[i][j]=1;
            operation.setPixel(i, j, Color.argb(Color.alpha(p1), clus+10, clus+10, clus+10));
            if(visit[i][j+1]!=1){
                dfs_visit(ii,jj+1,clus);
            }
        }
    }


    public void ThreeColor(View view) throws IOException {
        Bitmap Y,I,Q,pic;
        Y=operation;
        I=operation;
        Q=operation;
        pic=operation;
        int width=pic.getWidth();
        int height=pic.getHeight();
        int progress=1;//for progressber

        int r,g,b;
        int Y0=0,I0=0,Q0=0;

        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                int pix=pic.getPixel(x,y);
                r=Color.red(pix);
                g=Color.green(pix);
                b=Color.blue(pix);

                Y0=(int)(.299*r+.587*g+.114*b);
                I0=(int)(.596*r-.275*g-.321*b);
                Q0=(int)(.212*r-.523*g+.311*b);

                Y.setPixel(x,y,(int)Y0);
                I.setPixel(x, y, (int) I0);
                Q.setPixel(x, y, (int)Q0);



            }
        }

        progress=50;//for Progressber
        //progressBarImgAct1.setProgress(progress);
        //progressBarImgAct2.setProgress(progress);

        double red=0,green=0,blue=0,yin,iin,qin;
        int wid=I.getWidth();
        int hei=I.getHeight();

        Bitmap outputThree=operation;


        for(int y=0;y<hei;y++){
            for(int x=0;x<wid;x++){
                int pixOut=outputThree.getPixel(x, y);
                int alphaOut=Color.alpha(pixOut);

                yin=((double)Y.getPixel(x,y));
                iin=((double)I.getPixel(x,y));
                qin=((double)Q.getPixel(x,y));

                red=(1*yin+.956*iin+.621*qin);
                green=(1*yin-.272*iin-.647*qin);
                blue=(1*yin-1.105*iin+1.702*qin);

                outputThree.setPixel(x, y, Color.argb(Color.alpha(pixOut), (int) red, (int) green, (int) blue));

            }
        }
        progress=100;//for progressber
        //progressBarImgAct1.setProgress(progress);
        // progressBarImgAct2.setProgress(progress);

        imgSpecimenPhoto.setImageBitmap(outputThree);
        long time1 = System.currentTimeMillis();
        String str1;
        str1 = "" + time1 + "segmentedphoto.jpg";
        //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        FileOutputStream output;
        output = new FileOutputStream(imageFileSaved);
        operation.compress(Bitmap.CompressFormat.PNG, 0, output);
        output.close();
        Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();



    }

    public void SegmentNafis(View view){
        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        Bitmap bp;
        bp=operation;
        int row=bp.getWidth();
        int col=bp.getHeight();
        double sum=0;
        int count=0;
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++){
                int p = bp.getPixel(i,j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                sum=sum+ta;
                count++;
            }
        }
        double thresh =sum/count;
        for(int i=0;i<row;i++) {
            for (int j = 0; j < col; j++) {
                int p = bp.getPixel(i,j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                if(ta<thresh)
                    bp.setPixel(i, j, Color.argb(Color.alpha(p), 255,255,255));
                else
                    bp.setPixel(i,j, Color.argb(Color.alpha(p), 0,0,0));
            }
        }
        imgSpecimenPhoto.setImageBitmap(bp);
    }

//

    public void ImageOperations(View view) {

        //  public static BufferedImage Threshold(BufferedImage img,int requiredThresholdValue) {

        Bitmap img=operation;
        int height = img.getHeight();
        int width = img.getWidth();
        int requiredThresholdValue=30;
        // BufferedImage finalThresholdImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB) ;

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int x = 0; x < width; x++) {
//			System.out.println("Row: " + x);
            try {

                for (int y = 0; y < height; y++) {
                    //int color = img.getRGB(x, y);
                    int color = img.getPixel(x, y);
                    //red = ImageOperations.getRed(color);
                    //green = ImageOperations.getGreen(color);
                    //blue = ImageOperations.getBlue(color);
                    red = Color.red(color);
                    green = Color.green(color);
                    blue = Color.blue(color);


//					System.out.println("Threshold : " + requiredThresholdValue);
                    if((red+green+green)/3 < (int) (requiredThresholdValue)) {
                        //finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(0, 0,0));
                        img.setPixel(x, y, Color.argb(Color.alpha(color),0,0,0));
                    }
                    else {
                        // finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(255, 255,255));
                        img.setPixel(x,y, Color.argb(Color.alpha(color), 255,255,255));
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }
        }


    }

    private  int mixColor(int red, int green, int blue) {
        return red<<16|green<<8|blue;
    }

    public  int getRed(int color) {
        return (color & 0x00ff0000)  >> 16;
    }

    public  int getGreen(int color) {
        return	(color & 0x0000ff00)  >> 8;
    }

    public  int getBlue(int color) {
        return (color & 0x000000ff)  >> 0;

    }

    public void add(View view){

        int add=10;
        addi(add);
    }
    public void addi(int add){

        if(add>100){
            Log.i("APU","addition complete");
        }
        addi(add+2);
    }


    //

    public void Total_segmentation(View view) throws IOException {
        Integer indexValue = s.getSelectedItemPosition();
       // int indexValue=0;
        if(indexValue==0){
            //RGB based segmentation
            //  public void ThreeColor(View view) throws IOException {

            Bitmap Y,I,Q,pic;
            Y=operation;
            I=operation;
            Q=operation;
            pic=operation;
            int width=pic.getWidth();
            int height=pic.getHeight();
            int progress=1;//for progressber

            int r,g,b;
            int Y0=0,I0=0,Q0=0;

            for(int y=0;y<height;y++){
                for(int x=0;x<width;x++){
                    int pix=pic.getPixel(x,y);
                    r=Color.red(pix);
                    g=Color.green(pix);
                    b=Color.blue(pix);

                    Y0=(int)(.299*r+.587*g+.114*b);
                    I0=(int)(.596*r-.275*g-.321*b);
                    Q0=(int)(.212*r-.523*g+.311*b);

                    Y.setPixel(x,y,(int)Y0);
                    I.setPixel(x, y, (int) I0);
                    Q.setPixel(x, y, (int)Q0);



                }
                if(y==((height)/2)){

                }
            }

            progress=50;//for Progressber
            //progressBarImgAct1.setProgress(progress);
            //progressBarImgAct2.setProgress(progress);

            double red=0,green=0,blue=0,yin,iin,qin;
            int wid=I.getWidth();
            int hei=I.getHeight();

            Bitmap outputThree=operation;


            for(int y=0;y<hei;y++){
                for(int x=0;x<wid;x++){
                    int pixOut=outputThree.getPixel(x, y);
                    int alphaOut=Color.alpha(pixOut);

                    yin=((double)Y.getPixel(x,y));
                    iin=((double)I.getPixel(x,y));
                    qin=((double)Q.getPixel(x,y));

                    red=(1*yin+.956*iin+.621*qin);
                    green=(1*yin-.272*iin-.647*qin);
                    blue=(1*yin-1.105*iin+1.702*qin);

                    outputThree.setPixel(x, y, Color.argb(Color.alpha(pixOut), (int) red, (int) green, (int) blue));

                }
            }
            progress=100;//for progressber
            //progressBarImgAct1.setProgress(progress);
            // progressBarImgAct2.setProgress(progress);

            imgSpecimenPhoto.setImageBitmap(outputThree);

            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "segmentedphoto.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();



            //  }
        }
        else if(indexValue==1){


            //mean thresholding
            //  public void SegmentNafis(View view){
            //Bitmap bp = (Bitmap) data.getExtras().get("data");

            Bitmap bp;
            bp=operation;
            int row=bp.getWidth();
            int col=bp.getHeight();
            double sum=0;
            int count=0;
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++){
                    int p = bp.getPixel(i,j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    sum=sum+ta;
                    count++;
                }
            }
            //if(i==((ope.getHeight())/2)){

            //}
            double thresh =sum/count;
            for(int i=0;i<row;i++) {
                for (int j = 0; j < col; j++) {
                    int p = bp.getPixel(i,j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    if(ta<thresh)
                        bp.setPixel(i, j, Color.argb(Color.alpha(p), 255,255,255));
                    else
                        bp.setPixel(i,j, Color.argb(Color.alpha(p), 0,0,0));
                }
            }

            imgSpecimenPhoto.setImageBitmap(bp);
//            }


            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "segmentedphoto.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();




        }
        else if(indexValue==5){
//Binary thresholding
            // public void startSegment_na1(View view) throws IOException {

            //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
            //bmp = bitmapDrawable.getBitmap();
            //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            Bitmap abmp=operation;
            int t = 30;
            for (int i = 0; i < abmp.getHeight() - 1; i++) {
                for (int j = 0; j < abmp.getWidth() - 1; j++) {
                    int p = operation.getPixel(j, i);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    alpha = 0;
                    int tc = operation.getPixel(j, i);
                    int bc = operation.getPixel(j, i + 1);
                    float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                    float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;
                    if (Math.abs(ta-ba) <=15 )
                        abmp.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                    else
                        abmp.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                }
            }
            imgSpecimenPhoto.setImageBitmap(abmp);

            //save the segmented image
            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "simg.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();
            //
//
            //  }
        }
        else if(indexValue==2){

            //public void EdgeDetector(View view) {
            // truncate color component to be between 0 and 255

            int[][] filter1 = {
                    {-1, 0, 1},
                    {-2, 0, 2},
                    {-1, 0, 1}
            };

            int[][] filter2 = {
                    {1, 2, 1},
                    {0, 0, 0},
                    {-1, -2, -1}
            };

            Bitmap pic0;
            Bitmap pic1;
            pic0 = operation;
            int magnitude = 0;
            pic1=operation;

            //Picture pic0 = new Picture(args[0]);

            //int height   = pic0.height();
            //Picture pic1 = new Picture(width, height);
            int width = pic0.getWidth();
            int height = pic0.getHeight();
            //

            //

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {

                    // get 3-by-3 array of colors in neighborhood
                    int[][] gray = new int[3][3];
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            //luminance = 0.27 red + 0.67 green + 0.06 blue.
                            int pix = pic0.getPixel(x - 1 + i, y - 1 + j);
                            int lum = (int) (Color.red(pix) * .27 + .67 * Color.green(pix) + Color.blue(pix) * .06);
                            //gray[i][j] = (int) Luminance.lum(pic0.get(x-1+i, y-1+j));
                            gray[i][j] = lum;
                        }
                    }

                    // apply filter
                    int gray1 = 0, gray2 = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            gray1 += gray[i][j] * filter1[i][j];
                            gray2 += gray[i][j] * filter2[i][j];
                        }
                    }
                    // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
                    magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                    int p = pic0.getPixel(x, y);

                    pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));
                    //Color grayscale = new Color(magnitude, magnitude, magnitude);

                    //pic1.set(x, y, grayscale);
                }
                if(y==((height)/2)){

                }
            }
            //pic0.show();
            //pic1.show();
            imgSpecimenPhoto.setImageBitmap(pic0);
            // pic1.save("baboon-edge.jpg");
            for(int i=0;i<10000;i++);


            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {

                    int newPix = pic0.getPixel(x, y);
                    int r = Color.red(newPix);
                    int g = Color.green(newPix);
                    int b = Color.blue(newPix);

                    int newPix2 = pic1.getPixel(x, y);
                    int r2 = Color.red(newPix2);
                    int g2 = Color.green(newPix2);
                    int b2 = Color.blue(newPix2);

                    if (r ==0 && g == 0 && b == 0) {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), 255, 255, 255));

                    } else {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), r2, g2, b2));

                    }

                    //pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));

                }


            }

            imgSpecimenPhoto.setImageBitmap(pic1);
            // }

            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "segmentedphoto.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();



        }
        else if(indexValue==3){

            //Color based thres holding

            //   public void startSegment(View view) throws IOException {

            Log.i("APU", "EnterStartsegment");
            int tr, tg, tb;
            tr = 90;
            tg = 127;
            tb = 127;
            Bitmap ope;
            ope=operation;
            Log.i("APU", "creation1stBitmap");
            // backOperation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            Log.i("APU", "creation2ndBitmap");
            //mainOperation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            Log.i("APU", "creation3rdBitmap");

            for (int i = 0; i < ope.getHeight() - 1; i++) {
                for (int j = 0; j < ope.getWidth() - 1; j++) {
                    int p = ope.getPixel(j, i);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    int tc = ope.getPixel(j, i);
                    int bc = ope.getPixel(j, i + 1);
                    float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                    float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;
                /*
                if (Math.abs(ta - ba) < Thresehold) {
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                   // imgSpecimenPhoto.setImageBitmap(operation);
                } else {

                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 0, 0));
                    //imgSpecimenPhoto.setImageBitmap(operation);
                }*/
                    int red = (int) (0.21 * r + 0.71 * g + 0.07 * b);
                    if (r >= 0 && r <= tr) {
                        ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                    } else if (r > tr) {
                        ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                        //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));

                    }
                    if (g >= tg && g <= 255) {

                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));

                        ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                    } else if (g < tg) {
                        ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));

                        // backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                    }
                    if (b >= tb && b <= 255) {
                        ope.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                    } else if (b < tb) {
                        ope.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                        //backOperation.setPixel(j, i, Color.argb(Color.alpha(p), r, g, b));
                        //mainOperation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 0));
                    }
                    //operation.setPixel(j, i, Color.argb(Color.alpha(p), red, red, red));
                    //float ba=(red(bc)+green(bc)+blue(bc))/3.0;
                }
                if(i==((ope.getHeight())/2)){

                }
            }
            //imageView.setImageBitmap(operation);

            imgSpecimenPhoto.setImageBitmap(ope);

            Log.i("APU", "Segment finish");

            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "segmentedphoto.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();


        }
        if(indexValue==4){

            //Edge Detector Technique
            // private Bitmap processingBitmap(Bitmap src, int[][] knl) {
            //public void processingBitmap(View view) {
            //Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            Bitmap src=operation;
            int bmWidth = src.getWidth();
            int bmHeight = src.getHeight();
            int bmWidth_MINUS_2 = bmWidth - 2;
            int bmHeight_MINUS_2 = bmHeight - 2;

            for (int i = 1; i <= bmWidth_MINUS_2; i++) {
                for (int j = 1; j <= bmHeight_MINUS_2; j++) {

                    //get the surround 3*3 pixel of current src[i][j] into a matrix subSrc[][]
                    int[][] subSrc = new int[KERNAL_WIDTH][KERNAL_HEIGHT];
                    for (int k = 0; k < KERNAL_WIDTH; k++) {
                        for (int l = 0; l < KERNAL_HEIGHT; l++) {
                            subSrc[k][l] = src.getPixel(i - 1 + k, j - 1 + l);
                        }
                    }

                    //subSum = subSrc[][] * knl[][]
                    int subSumA = 0;
                    int subSumR = 0;
                    int subSumG = 0;
                    int subSumB = 0;

                    for (int k = 0; k < KERNAL_WIDTH; k++) {
                        for (int l = 0; l < KERNAL_HEIGHT; l++) {
                            subSumR += Color.red(subSrc[k][l]) * kernal[k][l];
                            subSumG += Color.green(subSrc[k][l]) * kernal[k][l];
                            subSumB += Color.blue(subSrc[k][l]) * kernal[k][l];
                        }
                    }

                    subSumA = Color.alpha(src.getPixel(i, j));

                    if (subSumR < 0) {
                        subSumR = 0;
                    } else if (subSumR > 255) {
                        subSumR = 255;
                    }

                    if (subSumG < 0) {
                        subSumG = 0;
                    } else if (subSumG > 255) {
                        subSumG = 255;
                    }

                    if (subSumB < 0) {
                        subSumB = 0;
                    } else if (subSumB > 255) {
                        subSumB = 255;
                    }

                    src.setPixel(i, j, Color.argb(
                            subSumA,
                            subSumR,
                            subSumG,
                            subSumB));
                }
                if(i==(bmWidth/2)){

                }
            }

            //return dest;

            imgSpecimenPhoto.setImageBitmap(src);

            long time1 = System.currentTimeMillis();
            String str1;
            str1 = "" + time1 + "segmentedphoto.jpg";
            //imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            imageFileSaved = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
            FileOutputStream output;
            output = new FileOutputStream(imageFileSaved);
            operation.compress(Bitmap.CompressFormat.PNG, 0, output);
            output.close();
            Toast.makeText(this, "The image file is saved at " + imageFileSaved.getAbsolutePath(), Toast.LENGTH_LONG).show();


            //}
        }

    }






    //



}

//




