package uk.co.ryanmoss.footballgroundguide_android;

/**
 * Created by ryanmoss on 28/03/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> imageUrls;


    public ImageAdapter(Context c, ArrayList<String> imageUrls) {
        mContext = c;
        this.imageUrls = imageUrls;


    }

    public int getCount() {
        return imageUrls.size();
    }

    public String getItem(int position) {
        return imageUrls.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        String url = getItem(position);

        Picasso
                .with(mContext)
                .load(getItem(position))
                .rotate(90f)
                .into(imageView);
        return imageView;
    }



}
