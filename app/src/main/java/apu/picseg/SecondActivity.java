package apu.picseg;

//
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;
//
//

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
//

public class SecondActivity extends AppCompatActivity {
    private Bitmap bmp;
    private int PICK_IMAGE_REQUEST = 2;
    private ImageView imageView;
    private Bitmap operation;
    private ImageView imgSpecimenPhoto;

    private GoogleApiClient client;
    private GoogleApiClient client2;
    private GoogleApiClient client3;
    private File imageFileSaved;
    private File imageFile;
    private String array_spinner_combobox[];//for combo box
    int[][] kernal = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
    };
    final static int KERNAL_WIDTH = 3;
    final static int KERNAL_HEIGHT = 3;
    private Spinner s;
    private ProgressBar progressBarImgAct1;

    double[][] intImg;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Intent rec_inttent = getIntent();//this is for main activity to sencode activioty come

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//
        imgSpecimenPhoto = (ImageView) findViewById(R.id.imageViewGallery);
        progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

        array_spinner_combobox = new String[5];
        array_spinner_combobox[0] = "RGB Based";
        array_spinner_combobox[1] = "Mean Thresholding";
        // array_spinner_combobox[2]="Binary Thresholding";
        array_spinner_combobox[2] = "GrayScale Segmentation";
        array_spinner_combobox[3] = "Color Based Thresholding";
        array_spinner_combobox[4] = "Edge Based Segmentation Technique";

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

        //this is for go to image activity

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client4 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public int truncate(int a) {
        if (a < 0) return 0;
        else if (a > 255) return 255;
        else return a;
    }


    public void goToTakeImage(View view) {
        Log.i("SecondActivity", "goToTakeImage");
        Intent image_intent = new Intent(this, ImageActivity.class);
        Log.i("SecondActivity", "goToTakeImage1");
        startActivity(image_intent);
        Log.i("SecondActivity", "goToTakeImage2");
        // setContentView(R.layout.activity_image);

    }

    public void takePictureGallery(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                imageView = (ImageView) findViewById(R.id.imageViewGallery);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        operation = bmap;

    }

    public void startSegment_na(View view) throws IOException {

        //      white=new Color();
        //    white.
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        bmp = bitmapDrawable.getBitmap();
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getHeight() - 1; i++) {
            for (int j = 0; j < bmp.getWidth() - 1; j++) {
                int p = bmp.getPixel(j, i);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                //r =  0;
                //g =  g+150;
                //b =  0;
                alpha = 0;
                //operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                int tc = bmp.getPixel(j, i);
                int bc = bmp.getPixel(j, i + 1);
                //float ta=(r+g+b)/3;

                float ta = (Color.red(tc) + Color.green(tc) + Color.blue(tc)) / 3;
                float ba = (Color.red(bc) + Color.green(bc) + Color.blue(bc)) / 3;

                /*if (Math.abs(ta - ba) < Thresehold) {
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 255, 255));
                   // imgSpecimenPhoto.setImageBitmap(operation);
                } else {

                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 0, 0));
                    //imgSpecimenPhoto.setImageBitmap(operation);
                }*/
                if (ta >= 0 && ta <= 85)
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 255, 0, 0));
                else if (ta > 85 && ta <= 170)
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 255, 0));
                else
                    operation.setPixel(j, i, Color.argb(Color.alpha(p), 0, 0, 255));
                //float ba=(red(bc)+green(bc)+blue(bc))/3.0;
            }
        }
        imageView.setImageBitmap(operation);

        //saving image file into memory
        long time1 = System.currentTimeMillis();
        String str1;
        str1 = "" + time1 + "segmentedphoto.jpg";
        File imageFileSavedimageAcitvity = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str1);
        FileOutputStream output;
        output = new FileOutputStream(imageFileSavedimageAcitvity);
        operation.compress(Bitmap.CompressFormat.PNG, 0, output);
        output.close();
        Toast.makeText(this, "The image file is saved at " + imageFileSavedimageAcitvity.getAbsolutePath(), Toast.LENGTH_LONG).show();

    }

    public void AdaptiveThresHolding(View view) {

        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        //Bitmap operation=Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), bp.getConfig());
        //  Bitmap bp=operation;

        //BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        // bmp = bitmapDrawable.getBitmap();
        //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Bitmap bp = operation;
        int row = bp.getWidth();
        int col = bp.getHeight();
        // double intImg[][]=new double[col+1][row+1];
        intImg = new double[col + 1][row + 1];

        double sum = 0;
        for (int j = 1; j < col - 1; j++) {
            sum = 0;
            for (int i = 1; i < row - 1; i++) {
                int p = bp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                sum = sum + ta;
                if (j == 0)
                    intImg[i][j] = sum;
                else
                    intImg[i][j] = intImg[i][j - 1] + sum;
            }
        }
        int s = row / 8;
        int t = 15;
        int x1, y1, x2, y2, count;
        for (int j = 1; j < row - 1; j++) {
            for (int i = 1; i < col - 1; i++) {
                x1 = j - (s / 2);
                x2 = j + (s / 2);
                y1 = i - (s / 2);
                y2 = i + (s / 2);
                count = (x2 - x1) * (y2 - y1);
                sum = intImg[x2][y2] - intImg[x2][y1 - 1] - intImg[x1 - 1][y2] + intImg[x1 - 1][y1 - 1];
                int p = bp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                if ((ta * count) <= (sum * (100 - t) / 100))
                    bp.setPixel(i, j, Color.argb(Color.alpha(p), 0, 0, 0));
                else
                    bp.setPixel(i, j, Color.argb(Color.alpha(p), 255, 255, 255));
            }
        }
        imageView.setImageBitmap(bp);
    }


    public void Total_segmentation1(View view) throws IOException {
        Integer indexValue = s.getSelectedItemPosition();
        // int indexValue=0;
        if (indexValue == 0) {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            //RGB based segmentation
            //  public void ThreeColor(View view) throws IOException {
            Bitmap Y, I, Q, pic;
            Y = operation;
            I = operation;
            Q = operation;
            pic = operation;
            int width = pic.getWidth();
            int height = pic.getHeight();
            int progress = 1;//for progressber

            int r, g, b;
            int Y0 = 0, I0 = 0, Q0 = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pix = pic.getPixel(x, y);
                    r = Color.red(pix);
                    g = Color.green(pix);
                    b = Color.blue(pix);

                    Y0 = (int) (.299 * r + .587 * g + .114 * b);
                    I0 = (int) (.596 * r - .275 * g - .321 * b);
                    Q0 = (int) (.212 * r - .523 * g + .311 * b);

                    Y.setPixel(x, y, (int) Y0);
                    I.setPixel(x, y, (int) I0);
                    Q.setPixel(x, y, (int) Q0);


                }
            }

            progress = 50;//for Progressber
            //progressBarImgAct1.setProgress(progress);
            //progressBarImgAct2.setProgress(progress);

            double red = 0, green = 0, blue = 0, yin, iin, qin;
            int wid = I.getWidth();
            int hei = I.getHeight();

            Bitmap outputThree = operation;


            for (int y = 0; y < hei; y++) {
                for (int x = 0; x < wid; x++) {
                    int pixOut = outputThree.getPixel(x, y);
                    int alphaOut = Color.alpha(pixOut);

                    yin = ((double) Y.getPixel(x, y));
                    iin = ((double) I.getPixel(x, y));
                    qin = ((double) Q.getPixel(x, y));

                    red = (1 * yin + .956 * iin + .621 * qin);
                    green = (1 * yin - .272 * iin - .647 * qin);
                    blue = (1 * yin - 1.105 * iin + 1.702 * qin);

                    outputThree.setPixel(x, y, Color.argb(Color.alpha(pixOut), (int) red, (int) green, (int) blue));

                }
            }
            progress = 100;//for progressber
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
        } else if (indexValue == 1) {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);


            //mean thresholding
            //  public void SegmentNafis(View view){
            //Bitmap bp = (Bitmap) data.getExtras().get("data");
            Bitmap bp;
            bp = operation;
            int row = bp.getWidth();
            int col = bp.getHeight();
            double sum = 0;
            int count = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    int p = bp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    sum = sum + ta;
                    count++;
                }
            }
            double thresh = sum / count;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    int p = bp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    if (ta < thresh)
                        bp.setPixel(i, j, Color.argb(Color.alpha(p), 255, 255, 255));
                    else
                        bp.setPixel(i, j, Color.argb(Color.alpha(p), 0, 0, 0));
                }
            }
            imgSpecimenPhoto.setImageBitmap(bp);

//            }


        } else if (indexValue == 5) {

//Binary thresholding
            // public void startSegment_na1(View view) throws IOException {

            //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
            //bmp = bitmapDrawable.getBitmap();
            //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            Bitmap abmp = operation;
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
                    if (Math.abs(ta - ba) <= 15)
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
        } else if (indexValue == 2) {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);


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
            pic1 = operation;

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
            for (int i = 0; i < 10000; i++) ;


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

                    if (r == 0 && g == 0 && b == 0) {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), 255, 255, 255));

                    } else {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), r2, g2, b2));

                    }

                    //pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));

                }


            }

            imgSpecimenPhoto.setImageBitmap(pic1);

            // }


        } else if (indexValue == 3) {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            //Color based thres holding

            //   public void startSegment(View view) throws IOException {
            Log.i("APU", "EnterStartsegment");
            int tr, tg, tb;
            tr = 90;
            tg = 127;
            tb = 127;
            Bitmap ope;
            ope = operation;
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
        if (indexValue == 4) {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);


            //Edge Detector Technique
            // private Bitmap processingBitmap(Bitmap src, int[][] knl) {
            //public void processingBitmap(View view) {
            //Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            Bitmap src = operation;
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
            //}
        }


    }

    public void AdaptiveThresHolding1(View view) {

        // Bitmap bp = (Bitmap) data.getExtras().get("data");
        //Bitmap operation=Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), bp.getConfig());
        Bitmap bp = operation;
        int row = bp.getWidth();
        int col = bp.getHeight();
        double intImg[][] = new double[row + 100][col + 100];
        double sum = 0;
        for (int i = 0; i < row; i++) {
            sum = 0;
            for (int j = 0; j < col; j++) {
                int p = bp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                sum = sum + ta;
                if (i == 0)
                    intImg[i][j] = sum;
                else
                    intImg[i][j] = intImg[i - 1][j] + sum;
            }
        }
        int s = row / 8;
        int t = 15;
        int x1, y1, x2, y2, count;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                x1 = i - (s / 2)+68;
                x2 = i + (s / 2)+68;
                y1 = j - (s / 2)+68;
                y2 = j + (s / 2)+68;
                count = (x2 - x1) * (y2 - y1);
                sum = intImg[x2][y2] - intImg[x2][y1 - 1] - intImg[x1 - 1][y2] + intImg[x1 - 1][y1 - 1];
                int p = bp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                if ((ta * count) <= (sum * (100 - t) / 100))
                    operation.setPixel(i, j, Color.argb(Color.alpha(p), 0, 0, 0));
                else
                    operation.setPixel(i, j, Color.argb(Color.alpha(p), 255, 255, 255));
            }
        }
        imgSpecimenPhoto.setImageBitmap(operation);


        // imgSpecimenPhoto.setImageBitmap(bp);
    }


    //
    public void Total_segmentation(View view) throws IOException {
        Integer indexValue = s.getSelectedItemPosition();
        // int indexValue=0;
        if (indexValue == 0) {
            //RGB based segmentation
            //  public void ThreeColor(View view) throws IOException {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            Bitmap Y, I, Q, pic;
            Y = operation;
            I = operation;
            Q = operation;
            pic = operation;
            int width = pic.getWidth();
            int height = pic.getHeight();
            int progress = 1;//for progressber

            int r, g, b;
            int Y0 = 0, I0 = 0, Q0 = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pix = pic.getPixel(x, y);
                    r = Color.red(pix);
                    g = Color.green(pix);
                    b = Color.blue(pix);

                    Y0 = (int) (.299 * r + .587 * g + .114 * b);
                    I0 = (int) (.596 * r - .275 * g - .321 * b);
                    Q0 = (int) (.212 * r - .523 * g + .311 * b);

                    Y.setPixel(x, y, (int) Y0);
                    I.setPixel(x, y, (int) I0);
                    Q.setPixel(x, y, (int) Q0);


                }
                if (y == ((height) / 2)) {

                }
            }

            progress = 50;//for Progressber
            //progressBarImgAct1.setProgress(progress);
            //progressBarImgAct2.setProgress(progress);

            double red = 0, green = 0, blue = 0, yin, iin, qin;
            int wid = I.getWidth();
            int hei = I.getHeight();

            Bitmap outputThree = operation;


            for (int y = 0; y < hei; y++) {
                for (int x = 0; x < wid; x++) {
                    int pixOut = outputThree.getPixel(x, y);
                    int alphaOut = Color.alpha(pixOut);

                    yin = ((double) Y.getPixel(x, y));
                    iin = ((double) I.getPixel(x, y));
                    qin = ((double) Q.getPixel(x, y));

                    red = (1 * yin + .956 * iin + .621 * qin);
                    green = (1 * yin - .272 * iin - .647 * qin);
                    blue = (1 * yin - 1.105 * iin + 1.702 * qin);

                    outputThree.setPixel(x, y, Color.argb(Color.alpha(pixOut), (int) red, (int) green, (int) blue));

                }
            }
            progress = 100;//for progressber
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
        } else if (indexValue == 1) {


            //mean thresholding
            //  public void SegmentNafis(View view){
            //Bitmap bp = (Bitmap) data.getExtras().get("data");
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            Bitmap bp;
            bp = operation;
            int row = bp.getWidth();
            int col = bp.getHeight();
            double sum = 0;
            int count = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    int p = bp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    sum = sum + ta;
                    count++;
                }
            }
            //if(i==((ope.getHeight())/2)){

            //}
            double thresh = sum / count;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    int p = bp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);
                    double ta = (Color.red(p) + Color.green(p) + Color.blue(p)) / 3;
                    if (ta < thresh)
                        bp.setPixel(i, j, Color.argb(Color.alpha(p), 255, 255, 255));
                    else
                        bp.setPixel(i, j, Color.argb(Color.alpha(p), 0, 0, 0));
                }
            }

            imgSpecimenPhoto.setImageBitmap(bp);
//            }


        } else if (indexValue == 5) {
//Binary thresholding
            // public void startSegment_na1(View view) throws IOException {

            //BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSpecimenPhoto.getDrawable();
            //bmp = bitmapDrawable.getBitmap();
            //operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            Bitmap abmp = operation;
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
                    if (Math.abs(ta - ba) <= 15)
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
        } else if (indexValue == 2) {

            //public void EdgeDetector(View view) {
            // truncate color component to be between 0 and 255
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

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
            pic1 = operation;

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
                if (y == ((height) / 2)) {

                }
            }
            //pic0.show();
            //pic1.show();
            imgSpecimenPhoto.setImageBitmap(pic0);
            // pic1.save("baboon-edge.jpg");
            for (int i = 0; i < 10000; i++) ;
            //

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

                    if (r == 0 && g == 0 && b == 0) {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), 255, 255, 255));

                    } else {
                        pic1.setPixel(x, y, Color.argb(Color.alpha(newPix2), r2, g2, b2));

                    }

                    //pic0.setPixel(x, y, Color.argb(Color.alpha(p), magnitude, magnitude, magnitude));

                }


            }

            imgSpecimenPhoto.setImageBitmap(pic1);
            // }


        } else if (indexValue == 3) {

            //Color based thres holding

            //   public void startSegment(View view) throws IOException {
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            Log.i("APU", "EnterStartsegment");
            int tr, tg, tb;
            tr = 90;
            tg = 127;
            tb = 127;
            Bitmap ope;
            ope = operation;
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
                if (i == ((ope.getHeight()) / 2)) {

                }
            }
            //imageView.setImageBitmap(operation);

            imgSpecimenPhoto.setImageBitmap(ope);

            Log.i("APU", "Segment finish");

        }
        if (indexValue == 4) {

            //Edge Detector Technique
            // private Bitmap processingBitmap(Bitmap src, int[][] knl) {
            //public void processingBitmap(View view) {
            //Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            progressBarImgAct1 = (ProgressBar) findViewById(R.id.progressBar1);

            Bitmap src = operation;
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
                if (i == (bmWidth / 2)) {

                }
            }

            //return dest;

            imgSpecimenPhoto.setImageBitmap(src);
            //}
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client4.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://apu.picseg/http/host/path")
        );
        AppIndex.AppIndexApi.start(client4, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Second Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://apu.picseg/http/host/path")
        );
        AppIndex.AppIndexApi.end(client4, viewAction);
        client4.disconnect();
    }


    //


}



