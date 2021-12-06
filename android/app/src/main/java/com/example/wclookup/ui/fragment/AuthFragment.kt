package com.example.wclookup.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.wclookup.R
import com.example.wclookup.ui.viewmodel.AuthViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AuthFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val authViewModel: AuthViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<SignInButton>(R.id.btn_sign_in).setOnClickListener {
            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1)
        }

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            authViewModel.authenticate(account.idToken!!)
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        authViewModel.authenticate(account.idToken!!)
                        authViewModel.authenticate(account.idToken!!)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .remove(this)
                            .commit()
                    }
                } catch (e: ApiException) {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.sign_in_failed),
                        Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }
}