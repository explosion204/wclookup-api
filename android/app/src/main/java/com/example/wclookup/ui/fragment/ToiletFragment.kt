package com.example.wclookup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.wclookup.R
import com.example.wclookup.databinding.FragmentToiletBinding
import com.example.wclookup.ui.viewmodel.ToiletViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ToiletFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val toiletViewModel: ToiletViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_toilet, container, false)
        val binding = FragmentToiletBinding.inflate(layoutInflater)
        val addressTextView: TextView = binding.addressTextView
        addressTextView.text = toiletViewModel.toilet.address
        return view
    }
}