package edu.bluejack23_2.verky.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPhotoViewModel : ViewModel() {
    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>> get() = _photos

    fun setPhotos(galleryPictures: List<String>) {
        _photos.value = galleryPictures
    }
}