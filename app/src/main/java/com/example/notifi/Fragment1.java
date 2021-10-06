package com.example.notifi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Fragment1 extends Fragment {
    private ImageView ivImage;
    private Context context;
    Button button;
    Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1,container,false);
        context = container.getContext();
        button = (Button)view.findViewById(R.id.fragBtn);
        ivImage = view.findViewById(R.id.frag1_image);

        String imageUrl = "http://121.152.234.192:8888/image";
        Glide.with(this).load(imageUrl).into(ivImage);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImageRequest();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void sendImageRequest(){
        String url = "http://121.152.234.192:8888/image";
        ImageLoadTask task = new ImageLoadTask(url,ivImage);
        task.execute();
    }
    public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlStr;
        private ImageView imageView;

        private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();

        public ImageLoadTask(String urlStr, ImageView imageView){
            this.urlStr = urlStr;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;

            try {
                // 이미 url을 통해 불러온 적이 있다면 이전 bitmap을 삭제
                if(bitmapHash.containsKey(urlStr)) {
                    Bitmap oldBitmap = bitmapHash.remove(urlStr);
                    if(oldBitmap != null){
                        oldBitmap.recycle();
                        oldBitmap = null;
                    }
                }

                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                bitmapHash.put(urlStr, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }

    }


}