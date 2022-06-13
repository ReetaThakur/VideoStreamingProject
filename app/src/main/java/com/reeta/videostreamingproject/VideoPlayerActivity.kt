package com.reeta.videostreamingproject

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener

class VideoPlayerActivity : AppCompatActivity() {

    lateinit var videoName:String
    lateinit var videoPath:String
    var isOpen :Boolean =true
    lateinit var videoNameTV:TextView
    lateinit var backIB:ImageButton
    lateinit var forwordIB:ImageButton
    lateinit var playPauseIB:ImageButton
    lateinit var videoSeekbar:SeekBar
    lateinit var videoTimeTv:TextView
    lateinit var controlRL:RelativeLayout
    lateinit var videoRL:RelativeLayout
    lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        videoNameTV=findViewById(R.id.videoName)
        videoTimeTv=findViewById(R.id.idTVTime)
        backIB=findViewById(R.id.idIBBack)
        playPauseIB=findViewById(R.id.idIBPlay)
        forwordIB=findViewById(R.id.idIBForword)
        videoSeekbar=findViewById(R.id.idSeelBarProgress)
        videoView=findViewById(R.id.playVideo)
        controlRL=findViewById(R.id.idRLControll)
        videoRL=findViewById(R.id.idRLVideo)

        videoName= intent.getStringExtra("videoName").toString()
        videoPath=intent.getStringExtra("videoPath").toString()

        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener {
            videoSeekbar.max=videoView.duration
            videoView.start()
        }

        videoNameTV.text=videoName
        backIB.setOnClickListener {
            videoView.seekTo(videoView.currentPosition-10000)

        }
        forwordIB.setOnClickListener {
            videoView.seekTo(videoView.currentPosition+10000)
        }
        playPauseIB.setOnClickListener {
            if (videoView.isPlaying){
                videoView.pause()
                playPauseIB.setImageDrawable(resources.getDrawable(R.drawable.play))
            }else{
                videoView.start()
                playPauseIB.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
            }
        }

        videoRL.setOnClickListener {
            if (isOpen){
                hideControls()
                isOpen=false
            }else{
                showControls()
                isOpen=true
            }
        }
        setHandler()
        initilizeSeekbar()
    }
    private fun setHandler() {
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (videoView.duration > 0) {
                    val curPos = videoView.currentPosition
                    videoSeekbar.progress = curPos
                    videoTimeTv.setText(""+convertTime(videoView.duration-curPos))
                }
                handler.postDelayed(this, 0)
            }
        }
        handler.postDelayed(runnable, 500)
    }

    private fun convertTime(ms: Int): String? {
        val time: String
        val seconds: Int
        val minutes: Int
        val hours: Int
        var x: Int = ms / 1000
        seconds = x % 60
        x /= 60
        minutes = x % 60
        x /= 60
        hours = x % 24
        time = if (hours != 0) {
            String.format("%02d", hours) + ":" + String.format(
                "%02d",
                minutes
            ) + ":" + String.format("%02d", seconds)
        } else {
            String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
        }
        return time
    }
    private fun initilizeSeekbar() {
        videoSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (videoSeekbar.id==R.id.idSeelBarProgress){
                    if (fromUser){
                        videoView.seekTo(progress)
                        videoView.start()
                        var curPos:Int=videoView.currentPosition
                        videoTimeTv.text = convertTime(videoView.duration-curPos)
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
    fun hideControls(){
        controlRL.visibility=View.GONE
        val window:Window=this.window
        if (window==null){
            return
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        var decordView:View=window.decorView
        if (decordView!=null){
            var uiOption:Int=decordView.systemUiVisibility
            if (Build.VERSION.SDK_INT>=14){
                uiOption = View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
            decordView.systemUiVisibility=uiOption
        }
    }

    fun showControls(){
        controlRL.visibility=View.VISIBLE
        val window:Window=this.window
        if (window==null){
            return
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        var decordView:View=window.decorView
        if (decordView!=null){
            var uiOption:Int=decordView.systemUiVisibility
            if (Build.VERSION.SDK_INT>=14){
                uiOption = View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
            if (Build.VERSION.SDK_INT>=16){
                uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            if (Build.VERSION.SDK_INT>=19){
                uiOption = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
            decordView.systemUiVisibility=uiOption
        }
    }
}