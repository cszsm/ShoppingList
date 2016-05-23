package com.zscseh93;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.zscseh93.data.Item;

import java.io.File;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Item item = getIntent().getExtras().getParcelable(ItemDetailFragment.ARG_ITEM);

        if (item.getPhotoFileName() != null) {
            showImage(item);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_ITEM, item);

            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    private void showImage(Item item) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File file = null;
        File storageDir = getExternalFilesDir(null);
        File[] asd = storageDir.listFiles();
        for (File s :
                asd) {
            if (s.getName().contains(item.getPhotoFileName())) {
                file = s;
            }
        }

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        ImageView imageView = (ImageView) findViewById(R.id.toolbarImage);
        imageView.setImageBitmap(bitmap);
        imageView.requestLayout();
    }
}
