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
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var radius: Double = 0.0
    private var isAdding: Boolean = false


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { map ->
        checkPermissions()
        googleMap = map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener {
            currentLatitude = it.latitude
            currentLongitude = it.longitude
            val coordinates = LatLng(currentLatitude, currentLongitude)
            googleMap.addMarker(MarkerOptions().position(coordinates).title("My last position"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14F))
        }
        googleMap.setOnMapClickListener(this)
        googleMap.setOnMarkerClickListener(this)
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
                currentLatitude = it.latitude
                currentLongitude = it.longitude
                val coordinates = LatLng(currentLatitude, currentLongitude)
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

        mapsViewModel.toiletsList.observe(viewLifecycleOwner, {
            it.forEach { toilet ->
                val coordinates = LatLng(toilet.latitude, toilet.longitude)
                googleMap.addMarker(MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            }
        })
    }

    fun checkPermissions() {
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

        builder.setPositiveButton("Add") { dialog, which ->
            val toilet = Toilet(0, addressInput.text.toString(), scheduleInput.text.toString(),
            latitude, longitude, 0.0, false)
            mapsViewModel.addToilet(toilet)
            mapsViewModel.searchInRadius(currentLatitude, currentLongitude, radius)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
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

        builder.setPositiveButton("OK") { dialog, which ->
            googleMap.clear()
            val coordinates = LatLng(currentLatitude, currentLongitude)
            googleMap.addMarker(MarkerOptions().position(coordinates).title("My last position"))

            val radiusString = input.text.toString()

            try {
                radius = radiusString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Invalid value, radius was set to default", Toast.LENGTH_SHORT).show()
                radius = 0.1
            }

            mapsViewModel.searchInRadius(currentLatitude, currentLongitude, radius)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }
}