package it.yoox.yooxrobe.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import it.yoox.yooxrobe.R
import it.yoox.yooxrobe.ui.viewmodels.PhotoSearchViewModel
import kotlinx.android.synthetic.main.activity_photo_search.*
import java.io.ByteArrayOutputStream
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.disposables.CompositeDisposable
import it.yoox.yooxrobe.models.YOOXItem
import net.idik.lib.slimadapter.SlimAdapter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PhotoSearchActivity : AppCompatActivity(), LifecycleOwner{

    lateinit var viewModel: PhotoSearchViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var adapter: SlimAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_search)

        viewModel = ViewModelProviders.of(this).get(PhotoSearchViewModel::class.java)

        BottomSheetBehavior.from(bottom_list).state = BottomSheetBehavior.STATE_HIDDEN

        list.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?
        adapter = SlimAdapter.create()
        adapter
            .register<YOOXItem>(R.layout.item_layout) { item, injector ->
                injector
                    .text(R.id.label, item.label)
                    .text(R.id.main_category, "CIAO")
                    .text(R.id.full_price, "${item.categories[0].products[0].price["currency"]} ${item.categories[0].products[0].price["fullPrice"]}")
                    .text(R.id.retail_price, "${item.categories[0].products[0].price["currency"]} ${item.categories[0].products[0].price["retailPrice"]}")
                    .with<TextView>(R.id.full_price) {
                        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    .with<ImageView>(R.id.image) {
                        Log.d("yoox", item.categories[0].products[0].images[0]["urlTemplate"])
                        Glide
                            .with(this)
                            .load(item.categories[0].products[0].images[0]["urlTemplate"])
                            .centerCrop()
                            .into(it)
                    }
            }
            .attachTo(list)

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
            viewModel.startUploading()

            val array = ArrayList<YOOXItem>()
            array.add(YOOXItem("Logo-Print Fleece-Back Cotton-Jersey Hoodie", "sweatshirt", 165L, 165f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1113675.jpg"))
            array.add(YOOXItem("Acid-Wash Fleeceback Cotton-Jersey Hoodie", "sweatshirt", 230L, 230f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1109054.jpg"))
            array.add(YOOXItem("Loopback Cotton-Jersey Hoodie", "sweatshirt", 100L, 100f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1066154.jpg"))
            array.add(YOOXItem("Steel grey Sweatshirt", "sweatshirt", 339L, 264f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/12186027VI.jpg"))
            array.add(YOOXItem("Grey Sweatshirt", "sweatshirt", 124L, 124f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/12226528XG.jpg"))
            array.add(YOOXItem("Cloud Oversized Loopback Cotton-Jersey Hoodie", "sweatshirt", 315L, 315f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1103954.jpg"))
            array.add(YOOXItem("LEAD SWEATSHIRT", "sweatshirt", 84L, 84f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/12277849GF.jpg"))
            array.add(YOOXItem("Logo-Embroidered Cotton-Jersey Hoodie", "sweatshirt", 315L, 315f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1088068.jpg"))
            array.add(YOOXItem("BLACK SWEATSHIRT", "sweatshirt", 319L, 319f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/12276070JE.jpg"))
            array.add(YOOXItem("GREY SWEATSHIRT", "sweatshirt", 48L, 48f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/12237700VQ.jpg"))
            array.add(YOOXItem("Printed Fleece-Back Cotton-Jersey Hoodie", "sweatshirt", 380L, 380f, "http://ypic.yoox.biz/ypic/mrp/-resize/640x480/in/1108931.jpg"))
            array.add(YOOXItem("Grey Synthetic Down Jacket", "jacket", 109L, 109f, "http://ypic.yoox.biz/ypic/yoox/-resize/640x480/f/41868206AU.jpg"))

            Handler().postDelayed({
                viewModel.stopUploading()
                viewModel.setResults(array)
            }, 500)
//            compositeDisposable.add(
//                NetworkAdapter(this).upload(cropped)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ result ->
//                        if (result.isSuccessful) {
//                            viewModel.stopUploading()
//                            Log.d("yoox", Gson().toJson(result.body()))
//                            // viewModel.setResults(result.body())
//                        } else {
//                            NetworkUtil.handleNetworkError(this, result.errorBody())
//                            viewModel.stopUploading()
//                        }
//                    }, {
//                        it.printStackTrace()
//                        viewModel.stopUploading()
//                        Toast.makeText(this, getString(R.string.connection_error_message), Toast.LENGTH_SHORT).show()
//                    }
//                    )
//            )
        }

        viewModel.uploading.observe(this, Observer {
            loader.isVisible = it
        })

        viewModel.results.observe(this, Observer {
            adapter.updateData(it)
            BottomSheetBehavior.from(bottom_list).state = if (it.size > 0) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
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
            viewModel.setImageURI(getImageUri(this@PhotoSearchActivity, resizeBitmap(imageBitmap, 1024, 1024)))
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
            viewModel.results.value!!.size > 0 -> viewModel.clearResults()
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
