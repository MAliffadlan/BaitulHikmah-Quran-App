package com.idn.smart.tiyas.quranqu.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class ParserPlace {
    fun parse(jObject: JSONObject): List<HashMap<String, String>> {
        var jPlaces: JSONArray? = null
        try {
            jPlaces = jObject.getJSONArray("results")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return getPlaces(jPlaces)
    }

    private fun getPlaces(jPlaces: JSONArray?): List<HashMap<String, String>> {
        val placesCount = jPlaces!!.length()
        val placesList: MutableList<HashMap<String, String>> = ArrayList()
        var place: HashMap<String, String>
        for (i in 0 until placesCount) {
            try {
                place = getPlace(jPlaces[i] as JSONObject)
                placesList.add(place)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return placesList
    }

    private fun getPlace(jPlace: JSONObject): HashMap<String, String> {
        val place = HashMap<String, String>()
        var placeName = "-NA-"
        var vicinity = "-NA-"
        var Address = "-NA-"
        var latitude = ""
        var longitude = ""
        try {
            if (!jPlace.isNull("name")) {
                placeName = jPlace.getString("name")
            }
            if (!jPlace.isNull("vicinity")) {
                vicinity = jPlace.getString("vicinity")
            }
            if (!jPlace.isNull("formatted_address")) {
                Address = jPlace.getString("formatted_address")
            }
            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat")
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng")
            place["place_name"] = placeName
            place["vicinity"] = vicinity
            place["Address"] = Address
            place["lat"] = latitude
            place["lng"] = longitude
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return place
    }
}