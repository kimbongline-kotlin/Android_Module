package com.kbline.kotlin_module.NavigationFolder

import android.content.Context
import android.location.Address
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DistanceMatrixApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import com.kbline.kotlin_module.KbViewModel
import com.kbline.kotlin_module.R
import io.nlopez.smartlocation.OnReverseGeocodingListener
import io.nlopez.smartlocation.SmartLocation


class NavigationViewModel : KbViewModel() {

    private val _addressData = MutableLiveData<Address>()
    val addressData : LiveData<Address> get() = _addressData

    private val _latlngData = MutableLiveData<LatLng>()
    val latlngData : LiveData<LatLng> get() = _latlngData

    private val _naviData = MutableLiveData<ArrayList<String>>()
    val naviData : LiveData<ArrayList<String>> get() = _naviData

    fun getMyLocation(location : Location, con : Context) {
        SmartLocation.with(con).geocoding().reverse(location, object :
            OnReverseGeocodingListener {
            override fun onAddressResolved(p0: Location, p1: MutableList<Address>) {
                if(p1.size > 0) {
                    var result = p1.get(0)
                    _addressData.postValue(result)
                }
            }
        })
    }

    fun distanceToAddress(start_lat : LatLng, end_lat : LatLng, con : Context) {

        var geoContext = GeoApiContext().setApiKey(con.resources.getString(R.string.geo_api))

        val work_trix = DistanceMatrixApi.newRequest(geoContext).origins(com.google.maps.model.LatLng(start_lat.latitude,start_lat.longitude))
            .destinations(com.google.maps.model.LatLng(end_lat.latitude,end_lat.longitude))
            .mode(TravelMode.WALKING)
            .language("ko")
            .await()

        val bycycle_trix = DistanceMatrixApi.newRequest(geoContext).origins(com.google.maps.model.LatLng(start_lat.latitude,start_lat.longitude))
            .destinations(com.google.maps.model.LatLng(end_lat.latitude,end_lat.longitude))
            .mode(TravelMode.BICYCLING)
            .language("ko")
            .await()

        val transit_trix = DistanceMatrixApi.newRequest(geoContext).origins(com.google.maps.model.LatLng(start_lat.latitude,start_lat.longitude))
            .destinations(com.google.maps.model.LatLng(end_lat.latitude,end_lat.longitude))
            .mode(TravelMode.TRANSIT)
            .language("ko")
            .await()

        val drive_trix = DistanceMatrixApi.newRequest(geoContext).origins(com.google.maps.model.LatLng(start_lat.latitude,start_lat.longitude))
            .destinations(com.google.maps.model.LatLng(end_lat.latitude,end_lat.longitude))
            .mode(TravelMode.DRIVING)
            .language("ko")
            .await()
        var data = ArrayList<String>()

        if(work_trix.rows.size > 0) {
            if(work_trix.rows.get(0).elements[0].duration != null) {
                data.add("도보 : ${work_trix.rows.get(0).elements[0].duration}\n")
            }

        }

        if(bycycle_trix.rows.size > 0) {
            if(bycycle_trix.rows.get(0).elements[0].duration != null) {
                data.add("자전거 : ${bycycle_trix.rows.get(0).elements[0].duration}\n")
            }

        }

        if(transit_trix.rows.size > 0) {
            if(transit_trix.rows.get(0).elements[0].duration != null) {
                data.add("대중교통 : ${transit_trix.rows.get(0).elements[0].duration}")
            }

        }

        if(drive_trix.rows.size > 0) {
            if(drive_trix.rows.get(0).elements[0].duration != null) {
                data.add("자동차 : ${drive_trix.rows.get(0).elements[0].duration}\n")
            }

        }
        _naviData.postValue(data)

    }
}