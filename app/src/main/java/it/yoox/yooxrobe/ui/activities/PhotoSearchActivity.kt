package it.yoox.yooxrobe.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import it.yoox.yooxrobe.R
import it.yoox.yooxrobe.ui.viewmodels.PhotoSearchViewModel
import kotlinx.android.synthetic.main.activity_photo_search.*
import java.io.ByteArrayOutputStream
import androidx.lifecycle.Observer
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*
import it.yoox.yooxrobe.network.NetworkAdapter
import it.yoox.yooxrobe.util.NetworkUtil

class PhotoSearchActivity : AppCompatActivity(), LifecycleOwner{

    lateinit var viewModel: PhotoSearchViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_search)

        viewModel = ViewModelProviders.of(this).get(PhotoSearchViewModel::class.java)

        action_open_gallery.setOnClickListener {
            openGallery()
        }

        action_open_camera.setOnClickListener {
            openCamera()
        }

        action_go_back.setOnClickListener {
            onBackPressed()
        }

        viewModel.image_uri.observe(this, Observer { image_uri ->
            image_uri?.let {
                cropImageView.setImageUriAsync(image_uri)
                cropImageView.isVisible = true
                button_container.isVisible = false
                action_search.isVisible = true
            } ?: run {
                button_container.isVisible = true
                cropImageView.isVisible = false
                action_search.isVisible = false
            }
        })

        action_search.setOnClickListener {
            val cropped = cropImageView.croppedImage
            val resizedBitmap = resizeBitmap(cropped, 1024, 1024)

            viewModel.startUploading()
            compositeDisposable.add(
                NetworkAdapter(this).upload(resizedBitmap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.isSuccessful) {
                            viewModel.stopUploading()
                            Log.d("yoox", Gson().toJson(result.body()))
                            // viewModel.setResults(result.body())
                        } else {
                            NetworkUtil.handleNetworkError(this, result.errorBody())
                            viewModel.stopUploading()
                        }
                    }, {
                        viewModel.stopUploading()
                        Toast.makeText(this, getString(R.string.connection_error_message), Toast.LENGTH_SHORT).show()
                    }
                    )
            )
        }

        viewModel.uploading.observe(this, Observer {
            loader.isVisible = it
        })
    }

    private fun resizeBitmap(bitmap:Bitmap, width:Int, height:Int):Bitmap{
        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    /**
     * needed to start the camera
     */
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) { //data from gallery
            viewModel.setImageURI(data?.data)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //data from camera
            val extras = data?.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            viewModel.setImageURI(getImageUri(this@PhotoSearchActivity, imageBitmap))
        }
    }

    /**
     * retrieve the URI from a bitmap
     * TO do this is necessary dealing with PERMISSION (to write on external storage)
     */
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        //retrieve the path of bitmap
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onBackPressed() {

        when {
            viewModel.results.value != null -> viewModel.clearResults()
            viewModel.image_uri.value != null -> viewModel.clearImageURI()
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {

        private val PICK_IMAGE = 1
        private val REQUEST_IMAGE_CAPTURE = 2
    }

}