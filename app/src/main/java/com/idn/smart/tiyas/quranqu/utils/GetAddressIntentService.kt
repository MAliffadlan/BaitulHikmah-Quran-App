package com.idn.smart.tiyas.quranqu.utils

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import java.util.*

class GetAddressIntentService : IntentService(IDENTIFIER) {
    private var addressResultReceiver: ResultReceiver? = null

    //handle the address request
    override fun onHandleIntent(intent: Intent?) {
        var msg = ""

        //get result receiver from intent
        addressResultReceiver = intent!!.getParcelableExtra("add_receiver")
        if (addressResultReceiver == null) {
            return
        }
        val location = intent.getParcelableExtra<Location>("add_location")

        //send no location error to results receiver
        if (location == null) {
            msg = "No location, can't go further without location"
            sendResultsToReceiver(0, msg)
            return
        }
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1)
        } catch (ignored: Exception) {
        }
        if (addresses == null || addresses.size == 0) {
            msg = "No address found for the location"
            sendResultsToReceiver(1, msg)
        } else {
            val address = addresses[0]
            val addressDetails = StringBuffer()


            addressDetails.append(address.adminArea)
            addressDetails.append("\n")
            sendResultsToReceiver(2, addressDetails.toString())
        }
    }

    //to send results to receiver in the source activity
    private fun sendResultsToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle()
        bundle.putString("address_result", message)
        addressResultReceiver!!.send(resultCode, bundle)
    }

    companion object {
        private const val IDENTIFIER = "GetAddressIntentService"
    }
}