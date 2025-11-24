package com.example.bloodlink.retrofit

import androidx.lifecycle.ViewModel
import com.example.bloodlink.data.model.metiers.User

class SharedModel : ViewModel() {
    var connectedUser: User? = null
}