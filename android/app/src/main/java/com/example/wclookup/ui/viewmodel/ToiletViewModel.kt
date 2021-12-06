package com.example.wclookup.ui.viewmodel;

import androidx.lifecycle.ViewModel
import com.example.wclookup.core.model.Toilet
import javax.inject.Inject

class ToiletViewModel @Inject constructor(

): ViewModel() {
    lateinit var toilet: Toilet
}
