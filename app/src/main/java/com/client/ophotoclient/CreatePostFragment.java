package com.client.ophotoclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.OphotoMessage;

import java.io.FileDescriptor;
import java.io.IOException;

import io.realm.Realm;

public class CreatePostFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();
    ImageView mImageView;
    Bitmap newImage = null;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_post_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageView = getActivity().findViewById(R.id.new_image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewImageClickListener(v);
            }
        });
        getActivity().findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onPost(); }
        });
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void onPost() {
        if (newImage == null)
            return;
        final AuthUser user = realm.where(AuthUser.class).findFirst();
        EditText editText = getActivity().findViewById(R.id.description_text);
        String description = editText.getText().toString();
        NetRequest.post(user.getToken(), newImage, description, new Response.Listener<OphotoMessage>() {
            @Override
            public void onResponse(OphotoMessage response) {
                onPosted();
            }
        }, null, getContext());
    }

    public void onPosted() {
        Toast toast = Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onNewImageClickListener(View v) {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (data != null) {
                Bitmap scaledBitmap;
                try {
                    newImage = getBitmapFromUri(data.getData());
                    scaledBitmap = Bitmap.createScaledBitmap(newImage, 100, 100, true);
                    mImageView.setImageBitmap(scaledBitmap);
                } catch (IOException ignored) {}
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageView = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
