package com.example.wclookup.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wclookup.R
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.ui.adapter.ToiletsInRadiusAdapter
import com.example.wclookup.ui.viewmodel.MapsViewModel
import com.example.wclookup.ui.viewmodel.ToiletViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ToiletsInRadiusFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mapsViewModel: MapsViewModel by activityViewModels {
        viewModelFactory
    }

    private val toiletViewModel: ToiletViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_toilets_in_radius, container, false)
        if (view is RecyclerView) {
            mapsViewModel.searchInRadius()
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = mapsViewModel.toilets.value?.let {
                ToiletsInRadiusAdapter(it, object: ToiletsInRadiusAdapter.OnItemClickListener {
                    override fun onItemClick(toilet: Toilet) {
                        toiletViewModel.toilet = toilet
                        findNavController().navigate(R.id.nav_toilet)
                    }
                })
            }
        }
        return view
    }
}