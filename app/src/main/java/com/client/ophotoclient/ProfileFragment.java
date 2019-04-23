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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.client.ophotoclient.objects.AuthUser;
import com.client.ophotoclient.objects.OphotoMessage;
import com.client.ophotoclient.objects.UserResponse;

import java.io.FileDescriptor;
import java.io.IOException;

import io.realm.Realm;

public class ProfileFragment extends Fragment {
    private Realm realm = Realm.getDefaultInstance();
    private ImageView image;
    private EditText bio;
    private TextView text;
    private Button followButton;
    private String user;
    private int READ_REQUEST_CODE = 444;

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Profile frag created");
        final AuthUser user = realm.where(AuthUser.class).findFirst();
        if (getActivity() == null)
            return;
        image = getActivity().findViewById(R.id.profile_image);
        bio = getActivity().findViewById(R.id.profile_bio);
        text = getActivity().findViewById(R.id.profile_followers);
        followButton = getActivity().findViewById(R.id.profile_follow_button);
        text.append(" of " + (user != null ? user.getName() : "Unknown"));
        this.user = user.getName();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        NetRequest.getUser(user.getToken(), user.getName(), new Response.Listener<UserResponse>() {
            @Override
            public void onResponse(UserResponse userResponse) {
                bio.setText(userResponse.getBio());
                text.setText(String.format("Followers: %d", userResponse.getFollows().size()));
                if (!userResponse.getImage_id().isEmpty()) {
                    NetRequest.getImage(user.getToken(), userResponse.getImage_id(), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap imageResponse) {
                            image.setImageBitmap(imageResponse);
                        }
                    }, null, getContext());
                }
            }

        }, null, getContext());
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
                    scaledBitmap = Bitmap.createScaledBitmap(getBitmapFromUri(data.getData()), 100, 100, true);
                    image.setImageBitmap(scaledBitmap);
                    AuthUser user = realm.where(AuthUser.class).findFirst();
                    NetRequest.setUserImage(user.getToken(), scaledBitmap, new Response.Listener<OphotoMessage>() {
                        @Override
                        public void onResponse(OphotoMessage response) {

                        }
                    }, null, getContext());
                } catch (IOException ignored) {}
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            user = realm.where(AuthUser.class).findFirst().getName();
        } else {

        }
    }
}
