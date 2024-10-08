package com.example.e_store.utils.shared_models

import com.google.android.libraries.places.api.model.Place

object SelectedPlaceSingleton {
    var place: Place? = null

    fun savePlace(place: Place) {
        this.place = place
    }

    fun getPlaceDetails(): String {
        return "Place: ${place?.name}, Address: ${place?.address}"
    }
}
