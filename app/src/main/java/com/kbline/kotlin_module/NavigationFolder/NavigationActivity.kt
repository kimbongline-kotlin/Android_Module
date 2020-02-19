package com.kbline.kotlin_module.NavigationFolder

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.kbline.kotlin_module.KbActivity
import com.kbline.kotlin_module.R
import com.kbline.kotlin_module.databinding.ActivityNavigationBinding
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import kotlinx.android.synthetic.main.activity_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

import java.util.*
import kotlin.collections.ArrayList


class NavigationActivity : KbActivity<ActivityNavigationBinding,NavigationViewModel>(), OnMapReadyCallback{
    override val reId: Int
        get() = R.layout.activity_navigation
    override val viewModel: NavigationViewModel by viewModel()


    lateinit var provider: LocationGooglePlayServicesProvider
    lateinit var myLatLng : Location
    lateinit var targetLatLng : LatLng
    lateinit var gMap : GoogleMap

    private val REQUEST_SELECT_PLACE = 1000
    var markers = ArrayList<Marker>()

    override fun viewStart() {

        getLocation()
        val maps = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        maps!!.getMapAsync(this)

    }

    fun getLocation() {
        provider = LocationGooglePlayServicesProvider()
        provider.setCheckLocationSettings(true)

        var smartLocation = SmartLocation.Builder(applicationContext).logging(true).build()
        smartLocation.location(provider).start {


            myLatLng = it
            showMap()
        }

        smartLocation.activity().start {

        }
    }

    override fun bindStart() {


        Places.initialize(applicationContext, resources.getString(R.string.place_api))

        viewModel.addressData.observe(this, Observer {
            myAddressLabel.setText(it.getAddressLine(0))
        })


        viewModel.naviData.observe(this, Observer {
            if(it.size > 0) {
                resultLayer.visibility = View.VISIBLE

                var result = ""
                it.forEach {
                    result = result + it
                }
                resultLayer.setText(result)

            }else {
                resultLayer.visibility = View.GONE
            }
        })
    }

    override fun bindAfter() {

        searchAddressBtn.setOnClickListener {
            call_place()
        }


    }

    fun call_place() {
        val fields: List<Place.Field> =
            Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, REQUEST_SELECT_PLACE)
    }

    override fun onMapReady(p0: GoogleMap?) {
        gMap = p0!!



    }


    fun showMap() {
        var myLoc = LatLng(myLatLng.latitude,myLatLng.longitude)
        var marker = MarkerOptions()
        marker.position(myLoc)
        marker.title("내 위치")
        markers.add(gMap.addMarker(marker))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc))
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

        viewModel.getMyLocation(myLatLng,applicationContext)
    }


    fun addMarker(latLng : LatLng) {
        if(markers.size > 1) {
            markers.get(1).remove()
        }
        var marker = MarkerOptions()
        marker.position(latLng)
        marker.title("목적지")
        markers.add(gMap.addMarker(marker))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        gMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f))

        viewModel.getMyLocation(myLatLng,applicationContext)




        viewModel.distanceToAddress(LatLng(myLatLng.latitude,myLatLng.longitude),targetLatLng,applicationContext)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_SELECT_PLACE) {
            if(resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                searchAddressBtn.setText(place.address!!.toString())
                targetLatLng = place.latLng!!
                addMarker(targetLatLng)
            }
        }
    }
}
