package com.example.trendymaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.trendymaps.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var markersOnMap = ArrayList<Marker>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude=30.740001
    private var longitude=76.78268
    private val permissionId = 2
    private var zoomOnMarkerLatLong: LatLng = LatLng(latitude, longitude) //default value for zooming and showing info on marker

    //setting icon as twitter icon by lazy (will come into action only when called)
    private val twitterIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.twitter_blue)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_twitter, color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initiating the location service to get current location
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //    Checks & Returns if the location has been enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //    Checks if the permissions has been given to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    //    Requests for permission from the user
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    //    If the permissions are given, it will call the function getLocation()
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    //    Extracts the location
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        latitude=list[0].latitude
                        longitude=list[0].longitude
                        val currLoc = LatLng(latitude, longitude)
                        removePreviousMarkersAndSetNewMarker(currLoc)
                        zoomOnMarkerLatLong = currLoc
                        showArea(currLoc)
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Chandigarh and move the Camera
        val chandigarh = LatLng(latitude, longitude)
        val defaultMarker = mMap.addMarker(
            MarkerOptions().position(chandigarh).icon(twitterIcon)
                .title("${chandigarh.latitude},${chandigarh.longitude}")
                .draggable(true)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chandigarh))

        //click on marker
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
                showArea(zoomOnMarkerLatLong)
            }
            true
        }
//        operations when clicked on marker title
        mMap.setOnInfoWindowClickListener {
            lifecycleScope.launch{
                val response=RetrofitInstance.api.getWOEID(latitude,longitude,"Bearer AAAAAAAAAAAAAAAAAAAAAA6jiwEAAAAA7dapTtX0wawQpWxL5AcgzLhXtMw%3DjoOhKdWx1gKa7whhJLucKAGadSdPJD5nhneSsPw90VadQnnWN5")[0].woeid
                Log.i("helloabc",response.toString())

                val response2=RetrofitInstance.api.getTopics(response,"Bearer AAAAAAAAAAAAAAAAAAAAAA6jiwEAAAAA7dapTtX0wawQpWxL5AcgzLhXtMw%3DjoOhKdWx1gKa7whhJLucKAGadSdPJD5nhneSsPw90VadQnnWN5")[0].trends
                var response3= arrayListOf<String>()
                for(i in response2)
                    response3.add(i.name)
//                response3 has all the trending topics extracted
                Log.i("helloabc",response3.toString())

            }
            Log.i("helloabc",zoomOnMarkerLatLong.latitude.toString())
            Log.i("helloabc",zoomOnMarkerLatLong.longitude.toString())
        }
        if (defaultMarker != null) {
            markersOnMap.add(defaultMarker)
        }

        //setting new marker with a long press, zooming to the location of the pointer
        mMap.setOnMapLongClickListener { latLng ->
            removePreviousMarkersAndSetNewMarker(latLng)
            zoomOnMarkerLatLong = latLng
            showArea(latLng)
        }
    }



    //remove previous markers and set new marker function
    private fun removePreviousMarkersAndSetNewMarker(latLng: LatLng) {
        if (markersOnMap.isNotEmpty()) { //looping on previous markers and removing them
            for (mrks in markersOnMap)
                mrks.remove()
        }
        //setting new marker
        val marker = mMap.addMarker(
            MarkerOptions().position(latLng).icon(twitterIcon)
                .title(latLng.latitude.toString() + "," + latLng.longitude.toString())
        )
        //adding new marker to array of markers for removal on next long press
        if (marker != null) {
            markersOnMap.add(marker)
        }
    }

    //zooming to the location
    private fun showArea(latLng: LatLng) {
        val cameraPosition =
            CameraPosition.Builder().target(latLng).zoom(12f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}


//drag and drop
//        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
//            override fun onMarkerDragStart(marker: Marker) {}
//            override fun onMarkerDragEnd(marker: Marker) {
//                val latLng = marker.position
//                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
//                try {
//                    val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
//                    Log.i("Hello", address.toString())
//                    marker.title = address.latitude.toString() + "," + address.longitude.toString()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onMarkerDrag(marker: Marker) {}
//        })

