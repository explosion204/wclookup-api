package com.example.wclookup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wclookup.R
import com.example.wclookup.core.exception.AccessTokenException
import com.example.wclookup.core.model.Review
import com.example.wclookup.ui.viewmodel.ReviewViewModel
import com.example.wclookup.ui.viewmodel.ToiletViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ReviewFormFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val reviewViewModel: ReviewViewModel by activityViewModels {
        viewModelFactory
    }

    private val toiletViewModel: ToiletViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewViewModel.getCurrentUserId()
        val view = inflater.inflate(R.layout.fragment_review_form, container, false)
        val ratingEdit = view.findViewById(R.id.rating_edit) as EditText
        val textEdit = view.findViewById(R.id.review_text_edit) as TextInputEditText

        view.findViewById<Button>(R.id.review_form_button).setOnClickListener {
            reviewViewModel.review = Review(
                0,
                reviewViewModel.userId,
                toiletViewModel.toilet.id,
                ratingEdit.text.toString().toInt(),
                textEdit.text.toString()
            )
            var update = false
            toiletViewModel.reviews.value!!.forEach {
                if (it.userId == reviewViewModel.userId) {
                    reviewViewModel.review.id = it.id
                    update = true
                }
            }
            if (update) {
                reviewViewModel.updateReview()
                Toast.makeText(requireContext(), "Your review updated!", Toast.LENGTH_SHORT).show()
            } else {
                reviewViewModel.createReview()
                Toast.makeText(requireContext(), "Thanks for creating a review!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}