package com.example.workslateapp.DataClasses

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Shift(
    var timeStamp: Pair<Timestamp?, Timestamp?>,
    var location: Pair<GeoPoint?, GeoPoint?>,
    var shiftType: ShiftType,

    )
