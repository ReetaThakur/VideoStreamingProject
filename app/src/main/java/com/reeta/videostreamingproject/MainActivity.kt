package com.reeta.videostreamingproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),VideoClickListner {

    lateinit var videoAdapter: VideoAdapter
    var videoList=ArrayList<VideoModel>()
    private var STORAGE_PERMISSION=101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRecyclerView()
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_PERMISSION)
        }else{
            getVideos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==STORAGE_PERMISSION){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show()
                getVideos()
            }else{
                Toast.makeText(this,"The App will not work without permission",Toast.LENGTH_LONG).show()
                 finish()
            }
        }
    }

   @SuppressLint("Range")
    fun getVideos(){
        val contentResolver:ContentResolver= contentResolver
        var uri:Uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        var cursor =contentResolver.query(uri,null,null,null,null)
         if (cursor!=null && cursor.moveToFirst()){
             do {
                 var videoTitle=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                 var videoPath=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                 var videoThumbnail= ThumbnailUtils.createVideoThumbnail(videoPath,MediaStore.Images.Thumbnails.MINI_KIND)
                 videoThumbnail?.let { VideoModel(videoTitle,videoPath, it) }
                     ?.let { videoList.add(it) }
             }while (cursor.moveToNext())
         }
       videoAdapter.notifyDataSetChanged()
    }

    fun setRecyclerView(){
        videoAdapter= VideoAdapter(videoList,this)
        val gridLayoutManager=GridLayoutManager(this,2)
        recyclerView.apply {
            adapter=videoAdapter
            layoutManager=gridLayoutManager
        }
    }

    override fun onVideoClick(position: Int) {
        val intent=Intent(this,VideoPlayerActivity::class.java)
        intent.putExtra("videoName",videoList[position].videoName)
        intent.putExtra("videoPath",videoList[position].videoPath)
        startActivity(intent)
    }
}