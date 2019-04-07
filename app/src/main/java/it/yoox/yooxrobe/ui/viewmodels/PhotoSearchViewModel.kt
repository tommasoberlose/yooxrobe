package it.yoox.yooxrobe.ui.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoSearchViewModel : ViewModel() {

    internal val image_uri: MutableLiveData<Uri> = MutableLiveData()
    internal val results: MutableLiveData<ArrayList<HashMap<String, Any>>> = MutableLiveData()
    internal val uploading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        image_uri.value = null
        results.value = null
        uploading.value = false
    }

    fun setImageURI(newImageUri: Uri?) {
        newImageUri?.let {
            this.image_uri.value = newImageUri
        }
    }

    fun setResults(results: ArrayList<HashMap<String, Any>>) {
        this.results.value = results
    }

    fun clearResults() {
        results.value = null
    }

    fun clearImageURI() {
        image_uri.value = null
    }

    fun startUploading() {
        uploading.value = true
    }

    fun stopUploading() {
        uploading.value = false
    }
}
