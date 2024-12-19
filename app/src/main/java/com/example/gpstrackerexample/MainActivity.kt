package com.example.gpstrackerexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gpstrackerexample.adapter.TrackingItemAdapter
import com.example.gpstrackerexample.data.TrackingItem
import com.example.gpstrackerexample.data.TrackingItemDatabase
import com.example.gpstrackerexample.databinding.ActivityMainBinding
import com.example.gpstrackerexample.service.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivityMainBinding

    private var adapter = TrackingItemAdapter()

    private lateinit var mMap: GoogleMap

    private var currentMarker : Marker? = null

    private var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }else{
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ActivityCompat.requestPermissions(this@MainActivity,permissions, PERMISSION_REQUEST)
        setView()

        TrackingItemDatabase.getDatabase(this).trackingItemDao().getAll().observe(this){
            adapter.trackingList.clear()
            if (it.isNotEmpty()){
                adapter.trackingList.addAll(it.reversed())
                refreshMapMarker(it)
            }
            adapter.notifyDataSetChanged()

        }
    }

    private fun setView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.rvTracking.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter.apply {
                onClickItem = object : TrackingItemAdapter.OnItemClickCallback{
                    override fun onClickItem(item: TrackingItem) {
                        Toast.makeText(this@MainActivity, "latitude : ${item.latitude}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@MainActivity, "longitude : ${item.longitude}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnStartLiveTracking.setOnClickListener {
            if (checkPermission(permissions)){
                mMap.clear()
                val locationServiceIntent = Intent(this@MainActivity, LocationServices::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(locationServiceIntent)
                }else{
                    startService(locationServiceIntent)
                }
            }else{
                ActivityCompat.requestPermissions(this@MainActivity,permissions, PERMISSION_REQUEST)
            }
        }
        binding.btnEndLiveTracking.setOnClickListener {
            val locationServiceIntent = Intent(this@MainActivity, LocationServices::class.java)
            stopService(locationServiceIntent)
            CoroutineScope(Dispatchers.IO).launch{
                val trackingItemDatabase = TrackingItemDatabase.getDatabase(this@MainActivity)
                trackingItemDatabase.trackingItemDao().delete()
            }
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, permissionArray[i]) != PackageManager.PERMISSION_GRANTED){
                allSuccess = false
            }
        }
        return allSuccess
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun refreshMapMarker(data : List<TrackingItem>){
        val lastData = data.last()
        val lastDataLat = lastData.latitude?.toDoubleOrNull()
        val lastDataLng = lastData.longitude?.toDoubleOrNull()
        val newLatLng = if (lastDataLat != null && lastDataLng != null)LatLng(lastDataLat, lastDataLng) else LatLng(0.0, 0.0)

        currentMarker?.remove()
        currentMarker = mMap.addMarker(MarkerOptions().position(newLatLng).title(lastData.startTime))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))

        val latLngList: List<LatLng> = data.mapNotNull { trackingItem ->
            val latitude = trackingItem.latitude?.toDoubleOrNull()
            val longitude = trackingItem.longitude?.toDoubleOrNull()
            if (latitude != null && longitude != null) {
                LatLng(latitude, longitude)
            } else {
                null
            }
        }

        mMap.addPolyline(
            PolylineOptions()
                .color(Color.CYAN)
                .width(10f)
                .addAll(latLngList)
        )
    }
}