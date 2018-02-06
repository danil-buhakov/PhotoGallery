package com.book.dan.photogallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class PhotoPageActivity extends SingleFragmentActivity {
    PhotoPageFragment mPhotoPageFragment;

    public static Intent newIntent(Context context, Uri uri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(uri);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        mPhotoPageFragment = PhotoPageFragment.newInstance(getIntent().getData());
        return mPhotoPageFragment;
    }

    @Override
    public void onBackPressed() {
        if (mPhotoPageFragment.mWebView.canGoBack()) {
            mPhotoPageFragment.mWebView.goBack();
        } else
            super.onBackPressed();
    }
}
