package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class ListMusicActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 99;
    ArrayList<Song> songArrayList;
    ListView lvSongs;
    SongsAdapter songsAdapter;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        lvSongs = findViewById(R.id.lvSongs);

        songArrayList = new ArrayList<>();
        songsAdapter = new SongsAdapter(this, songArrayList);
        lvSongs.setAdapter(songsAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, REQUEST_PERMISSION);

            } else {
                getSongs();
            }
        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = songArrayList.get(i);
                Intent openMusicPlayer = new Intent(ListMusicActivity.this, MusicPlayerActivity.class);
                openMusicPlayer.putExtra("song", song );
                startActivity(openMusicPlayer);
            }
        });

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_PERMISSION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongs();
            } else Toast.makeText(this, "Permission Denied to read your storage", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSongs() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor songCursor = null;

        try {
            songCursor = contentResolver.query(songUri, null, selection, null, null);
            if (songCursor != null && songCursor.moveToFirst()) {

                int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int indexData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {
                    String title = songCursor.getString(indexTitle);
                    String artist = songCursor.getString(indexArtist);
                    String path = songCursor.getString(indexData);
                    songArrayList.add(new Song(title, artist, path));
                } while (songCursor.moveToNext());
            } else Log.d("ListMusicActivity", "No songs found");
        } finally {
            if (songCursor != null) songCursor.close();
        }
        songsAdapter.notifyDataSetChanged();
        }
}
