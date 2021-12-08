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
import com.example.wclookup.core.model.Ticket
import com.example.wclookup.ui.viewmodel.TicketViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class TicketFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val ticketViewModel: TicketViewModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)
        val subjectEdit = view.findViewById(R.id.subject_edit) as EditText
        val textEdit = view.findViewById(R.id.text_edit) as TextInputEditText

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        val email = account!!.email!!

        view.findViewById<Button>(R.id.ticket_button).setOnClickListener {
            ticketViewModel.ticket = Ticket(
                0,
                subjectEdit.text.toString(),
                textEdit.text.toString(),
                email
            )
            ticketViewModel.createTicket()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            Toast.makeText(requireContext(), "Thanks for sending a ticket!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        return view
    }
}