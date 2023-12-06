@file:Suppress("DEPRECATION")

package com.example.casemanagment

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.casemanagment.Remote.DownloadFile

import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.ortiz.touchview.TouchImageView
import java.io.File

class PdfViewer : AppCompatActivity() {

    private lateinit var imageToDownload :String
    private val PERMISSION_REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)


        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        val filePathFrom: String? = intent.getStringExtra("document")

        val sharedPreferences= getSharedPreferences("Setting", Context.MODE_PRIVATE);
        val ipAdd= sharedPreferences.getString("IpAddress", null)
        val pub = sharedPreferences.getString("PubName", null)
        val port =sharedPreferences.getString("Port", null)


        var url = "";
        if (pub!=""){


            url ="http://$ipAdd:$port/$pub/$filePathFrom";
            imageToDownload = url
        }
        else {
            url =  "http://$ipAdd:$port/$filePathFrom";
            imageToDownload = url
        }

        PRDownloader.initialize(applicationContext)


        val imageView: TouchImageView = findViewById(R.id.fileImageView)


        imageView.setVisibility(View.INVISIBLE)


        if (filePathFrom?.split('.')!![1].toLowerCase()=="pdf"){
            val fileName = "myFile.pdf"

            imageView.setVisibility(View.INVISIBLE)
            setTitle("PDF Viewer")
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(urlIntent)





        }else
        {
            setTitle("Image Viewer")

            imageView.setVisibility(View.VISIBLE)

            val options = RequestOptions()
                .fitCenter()

            Glide.with(this)
                .load(url)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }

        registerForContextMenu(imageView)

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.image_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.download_option  -> {
                val imageUrl = imageToDownload
                val request = DownloadManager.Request(Uri.parse(imageUrl))
                    .setTitle("Case Attachment")
                    .setDescription("Downloading image")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "my_image.jpg")

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to write to external storage

                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                } else {
                    // Permission has already been granted
                    // Your code for accessing external storage here
                    downloadManager.enqueue(request)
                }
                

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                    context.applicationContext,
                    null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }






    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    }




