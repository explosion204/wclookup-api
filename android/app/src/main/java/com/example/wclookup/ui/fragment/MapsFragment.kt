package com.example.wclookup.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.wclookup.R
import com.example.wclookup.core.model.Toilet
import com.example.wclookup.ui.viewmodel.MapsViewModel
import com.example.wclookup.ui.viewmodel.factory.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MapsFragment : DaggerFragment(), GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mapsViewModel: MapsViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var currentPosMarker: Marker
    private var isAdding: Boolean = false

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { map ->
        checkPermissions()
        googleMap = map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            mapsViewModel.currentLatitude = it.latitude
            mapsViewModel.currentLongitude = it.longitude
            val coordinates = LatLng(it.latitude, it.longitude)
            currentPosMarker = googleMap.addMarker(
                MarkerOptions().position(coordinates).title("My last position"))!!
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14F))
        }
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        mapsViewModel.toilets.observe(viewLifecycleOwner, { toilets ->
            toilets.forEach {
                val coordinates = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)

        view.findViewById<FloatingActionButton>(R.id.fab_location).setOnClickListener {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                currentPosMarker.remove()
                mapsViewModel.currentLatitude = it.latitude
                mapsViewModel.currentLongitude = it.longitude
                val coordinates = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions().position(coordinates).title("My last position"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates))
            }
        }

        view.findViewById<FloatingActionButton>(R.id.fab_radius_search).setOnClickListener {
            showRadiusSearchDialog()
        }

        view.findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            isAdding = true
            Toast.makeText(requireContext(), "Tap any point on map to add toilet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onMapClick(coordinates: LatLng) {
        if (isAdding) {
            showAddToiletDialog(coordinates.latitude, coordinates.longitude)
        }
    }

    private fun showAddToiletDialog(latitude: Double, longitude: Double) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Adding toilet")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL

        val addressInput = EditText(requireContext())
        addressInput.setHint("Address")
        addressInput.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(addressInput)

        val scheduleInput = EditText(requireContext())
        scheduleInput.setHint("Schedule")
        scheduleInput.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(scheduleInput)

        layout.addView(addressInput)
        layout.addView(scheduleInput)
        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val toilet = Toilet(
                0, addressInput.text.toString(), scheduleInput.text.toString(),
                latitude, longitude, 0.0
            )
            mapsViewModel.addToilet(toilet)
            mapsViewModel.searchInRadius()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showRadiusSearchDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Set search radius in kilometers")

        val input = EditText(requireContext())
        input.setHint("")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            googleMap.clear()
            val coordinates = LatLng(mapsViewModel.currentLatitude, mapsViewModel.currentLongitude)
            googleMap.addMarker(MarkerOptions().position(coordinates).title("My last position"))

            val radiusString = input.text.toString()

            try {
                mapsViewModel.radius = radiusString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    requireContext(),
                    "Invalid value, radius was set to default (100m)",
                    Toast.LENGTH_SHORT
                ).show()
                mapsViewModel.radius = 0.1
            }

            mapsViewModel.searchInRadius()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                2
            )
        }
    }
}