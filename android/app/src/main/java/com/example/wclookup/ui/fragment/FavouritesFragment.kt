package com.example.wclookup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wclookup.R
import com.example.wclookup.core.db.TinyDB
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.ui.adapter.ToiletsInRadiusAdapter
import com.example.wclookup.ui.viewmodel.FavouritesViewModel
import com.example.wclookup.ui.viewmodel.ToiletViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavouritesFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val favouritesViewModel: FavouritesViewModel by activityViewModels {
        viewModelFactory
    }

    private val toiletViewModel: ToiletViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view is RecyclerView) {
            view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            val db = TinyDB(requireContext())
            val favList: MutableList<Long> = db.getListLong("favs")
            favouritesViewModel.findByIds(favList)
            favouritesViewModel.toilets.observe(viewLifecycleOwner, {
                view.layoutManager = LinearLayoutManager(context)
                view.adapter = ToiletsInRadiusAdapter(it, object: ToiletsInRadiusAdapter.OnItemClickListener {
                    override fun onItemClick(toilet: Toilet) {
                        toiletViewModel.toilet = toilet
                        findNavController().navigate(R.id.nav_toilet)
                    }
                })
            })
        }
    }
}