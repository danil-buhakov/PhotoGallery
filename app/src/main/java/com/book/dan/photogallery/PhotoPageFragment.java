package com.book.dan.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class PhotoPageFragment extends VisibleFragment {
    private static final String ARG_URI = "photo_page_uri";

    private Uri mUri;
    private WebView mWebView;

    public static PhotoPageFragment newInstance(Uri uri){
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI,uri);
        PhotoPageFragment photoPageFragment = new PhotoPageFragment();
        photoPageFragment.setArguments(args);
        return photoPageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = (Uri) getArguments().getParcelable(ARG_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_page,container,false);
        mWebView = (WebView) v.findViewById(R.id.web_view);
        return v;
    }
}
