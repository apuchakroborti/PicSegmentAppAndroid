package apu.picseg;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;


//
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

//

public class ShowImageActivity extends AppCompatActivity {


    private Cursor cursor;
    private int columnIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this is for show all image in grid view from gallery
        //String[] list = {MediaStore.Images.Media._ID};
        String[] list = {MediaStore.Images.Media._ID};
        //Retriving Images from Database(SD CARD) by Cursor.
       // cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, list, null, null, MediaStore.Images.Thumbnails._ID);
        cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, list, null, null, MediaStore.Images.Thumbnails._ID);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        GridView sdcardimage = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter=new ImageAdapter(this);
        sdcardimage.setAdapter(adapter);
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



private class ImageAdapter extends BaseAdapter {

    private Context context;

    public ImageAdapter(Context localContext) {

        context = localContext;

    }

    public int getCount() {

        return cursor.getCount();

    }

    public Object getItem(int position) {

        return position;

    }

    public long getItemId(int position) {

        return position;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();


        if (convertView == null) {
            holder.picturesView = new ImageView(context);
            //Converting the Row Layout to be used in Grid View
            convertView = getLayoutInflater().inflate(R.layout.row, parent, false);

            //You can convert Layout in this Way with the Help of View Stub. View Stub is newer. Read about ViewStub.Inflate
            // and its parameter.
            //convertView= ViewStub.inflate(context,R.layout.row,null);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        cursor.moveToPosition(position);
        int imageID = cursor.getInt(columnIndex);

        //In Uri "" + imageID is to convert int into String as it only take String Parameter and imageID is in Integer format.
        //You can use String.valueOf(imageID) instead.
        /*Uri uri = Uri.withAppendedPath(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
*/
        Uri uri = Uri.withAppendedPath(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);

        //Setting Image to View Holder Image View.
        holder.picturesView = (ImageView) convertView.findViewById(R.id.imageview);
        holder.picturesView.setImageURI(uri);
        holder.picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        return convertView;

    }
    // View Holder pattern used for Smooth Scrolling. As View Holder pattern recycle the findViewById() object.
    class ViewHolder {
        private ImageView picturesView;
    }
}
}

