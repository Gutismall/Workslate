package com.example.workslateapp.Fragments

import DatabaseManeger
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.workslateapp.Callbacks.LocationCallback
import com.example.workslateapp.R
import com.example.workslateapp.databinding.FragmentConstraintsBinding
import com.example.workslateapp.databinding.FragmentLogBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.time.LocalDate
import java.time.LocalTime

class LogFragment : Fragment(), LocationCallback {

    private var _binding: FragmentLogBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }


    private fun initViews() {
        DatabaseManeger.getLastShifts(1){lastLog->
            if (lastLog.isNotEmpty()){
                if (lastLog[0].timeStamp.second.toString().isNotBlank()){
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n${lastLog[0].timeStamp.second}\n${lastLog[0].location.second}"
                }
                else
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n${lastLog[0].timeStamp.first}\n${lastLog[0].location.first}"
            }
            else
                binding.FragmentLogLastLogTxt.text = "Last Log:\nNo Logs Yet"

        }


        binding.FragmentLogLogInBtn.setOnClickListener { logIn()}
        binding.FragmentLogLogOutBtn.setOnClickListener { logOut()}
    }

    private fun logIn() {
        getCurrentLocation { currentLocation ->
            val currentTime = Timestamp.now()
            DatabaseManeger.addLogInShift(LocalDate.now(), currentLocation) { success, message ->
                if (success) {
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n$currentTime\n$currentLocation"
                } else {
                    Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun logOut() {
        getCurrentLocation { currentLocation ->
            val currentTime = Timestamp.now()
            DatabaseManeger.addLogOutShift(LocalDate.now(), currentLocation) { success, message ->
                if (success) {
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n$currentTime\n$currentLocation"
                } else {
                    Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun getCurrentLocation(onLocationReceived: (GeoPoint) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReceived(GeoPoint(location.latitude, location.longitude))
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
            }
    }

    override fun onLocationResult(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}