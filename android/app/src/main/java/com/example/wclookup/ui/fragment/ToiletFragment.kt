package com.example.wclookup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wclookup.R
import com.example.wclookup.core.db.TinyDB
import com.example.wclookup.ui.adapter.ReviewsAdapter
import com.example.wclookup.ui.viewmodel.ReviewViewModel
import com.example.wclookup.ui.viewmodel.ToiletViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerFragment
import java.util.ArrayList
import javax.inject.Inject

class ToiletFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val toiletViewModel: ToiletViewModel by activityViewModels {
        viewModelFactory
    }

    private val reviewViewModel: ReviewViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toilet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var idToRemove: Long = -1
        toiletViewModel.reviews.value = emptyList()
        reviewViewModel.getCurrentUserId()
        val addressTextView = view.findViewById(R.id.addressTextView) as TextView
        val scheduleTextView = view.findViewById(R.id.scheduleTextView) as TextView
        val latitudeTextView = view.findViewById(R.id.latitudeTextView) as TextView
        val longitudeTextView = view.findViewById(R.id.longitudeTextView) as TextView
        val ratingTextView = view.findViewById(R.id.ratingTextView) as TextView
        val recyclerView = view.findViewById(R.id.review_list) as RecyclerView
        val removeButton: FloatingActionButton = view.findViewById(R.id.fab_remove_review)
        val favAddButton: FloatingActionButton = view.findViewById(R.id.fab_add_favourite)
        val favRemoveButton: FloatingActionButton = view.findViewById(R.id.fab_remove_favourite)
        removeButton.visibility = View.GONE

        toiletViewModel.findReviews()
        addressTextView.text = toiletViewModel.toilet.address
        scheduleTextView.text = toiletViewModel.toilet.schedule
        latitudeTextView.text = toiletViewModel.toilet.latitude.toString()
        longitudeTextView.text = toiletViewModel.toilet.longitude.toString()
        ratingTextView.text = toiletViewModel.toilet.rating.toString()
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        toiletViewModel.reviews.observe(viewLifecycleOwner) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = ReviewsAdapter(toiletViewModel.reviews, toiletViewModel.nicknames)
            toiletViewModel.reviews.value!!.forEach {
                if (it.userId == reviewViewModel.userId) {
                    removeButton.visibility = View.VISIBLE
                    idToRemove = it.id
                }
            }
        }

        val addButton: FloatingActionButton = view.findViewById(R.id.fab_add_review)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.nav_review_form)
        }


        val db = TinyDB(requireContext())
        val favList: MutableList<Long> = db.getListLong("favs")
        favList.forEach {
            if (it == toiletViewModel.toilet.id) {
                favRemoveButton.visibility = View.VISIBLE
            }
        }
        if (favRemoveButton.visibility == View.GONE) {
            favAddButton.visibility = View.VISIBLE
        }

        favAddButton.setOnClickListener {
            favList.add(toiletViewModel.toilet.id)
            db.putListLong("favs", favList as ArrayList<Long>?)
            favAddButton.visibility = View.GONE
            favRemoveButton.visibility = View.VISIBLE
        }

        favRemoveButton.setOnClickListener {
            favList.remove(toiletViewModel.toilet.id)
            db.putListLong("favs", favList as ArrayList<Long>?)
            favRemoveButton.visibility = View.GONE
            favAddButton.visibility = View.VISIBLE
        }

        removeButton.setOnClickListener {
            reviewViewModel.deleteReview(idToRemove)
            toiletViewModel.findReviews(true)
            removeButton.visibility = View.GONE
        }
    }
}