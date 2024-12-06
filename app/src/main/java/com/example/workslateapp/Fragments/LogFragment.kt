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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Locale

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
        DatabaseManeger.getLastShifts(1) { lastLog ->
        lastLog.let {
            if (it.isNotEmpty()) {
                val timestampSecond = it[0].timeStamp.second
                val timestampFirst = it[0].timeStamp.first

                if (timestampSecond != null) {
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n${timestampSecond.toDate()}"
                } else {
                    binding.FragmentLogLastLogTxt.text = "Last Log:\n${timestampFirst!!.toDate()}\n"
                }
            } else {
                binding.FragmentLogLastLogTxt.text = "Last Log:\nNo Logs Yet"
            }
        }
    }

        binding.FragmentLogLogInBtn.setOnClickListener { logIn()}
        binding.FragmentLogLogOutBtn.setOnClickListener { logOut()}
    }

    private fun logIn() {
        getCurrentLocation { currentLocation ->
            DatabaseManeger.addLogInShift(LocalDate.now(), currentLocation) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Log In successful", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun logOut() {
        getCurrentLocation { currentLocation ->
            DatabaseManeger.addLogOutShift(LocalDate.now(), currentLocation) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Log Out successful", Toast.LENGTH_LONG).show()
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

    private fun refreshFragment() {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.detach(this).attach(this).commit()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}