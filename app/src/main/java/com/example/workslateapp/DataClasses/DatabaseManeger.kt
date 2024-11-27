import android.text.Editable
import android.util.Log
import com.example.workslateapp.DataClasses.Shift
import com.example.workslateapp.DataClasses.ShiftType
import com.example.workslateapp.DataClasses.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Date

object DatabaseManeger {

    private val auth = FirebaseAuth.getInstance()


    fun initUser() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userDoc = db.collection("users").document(user.uid)
            userDoc.get().addOnSuccessListener { result ->
                if (result.data?.isEmpty() == true)
                    return@addOnSuccessListener
                userDoc.update(
                    mapOf(
                        "firstName" to "",
                        "LastName" to "",
                        "phoneNumber" to "",
                        "dateOfBirth" to ""
                    )
                )
            }
        }
    }

    // Add a shift for a user
    fun addLogInShift(date: Timestamp, location: GeoPoint, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            // Shift data with combined date, time, and location
            val shiftData = hashMapOf(
                "shiftType" to shiftTypeIndicator(),
                "dateTime" to date,
                "location" to location
            )
            // Add log in shift to the 'logInShifts' subcollection
            db.collection("users").document(userId).collection("logInShifts").add(shiftData)
                .addOnSuccessListener {
                    onComplete(true, null)
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not authenticated.")
        }
    }

    // Add a log out shift
    fun addLogOutShift(
        date: Timestamp,
        location: GeoPoint,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            // Shift data with combined date, time, and location
            val shiftData = hashMapOf(
                "shiftType" to shiftTypeIndicator(),
                "dateTime" to date,
                "location" to location
            )

            // Add log out shift to the 'logOutShifts' subcollection
            db.collection("users").document(userId).collection("logOutShifts").add(shiftData)
                .addOnSuccessListener {
                    onComplete(true, null)
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not authenticated.")
        }
    }

//    fun getCurrentWeekShifts(onComplete: (List<Shift>) -> Unit) {
//        val db = FirebaseFirestore.getInstance()
//        val currentUser = auth.currentUser
//
//        currentUser?.let { user ->
//            val userId = user.uid
//
//            // Get current date and calculate start and end of the current week
//            val currentDate = LocalDate.now()
//            val startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
//            val endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
//
//            // Convert LocalDate to Date (which Firestore Timestamp requires)
//            val startOfWeekDate =
//                Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant())
//            val endOfWeekDate =
//                Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant())
//
//            // Query Firestore for shifts within this date range based on logInTimestamp
//            db.collection("Users")
//                .document(userId)
//                .collection("Shifts")
//                .whereGreaterThanOrEqualTo("LogInDate", Timestamp(startOfWeekDate))
//                .whereLessThanOrEqualTo("LogOutDate", Timestamp(endOfWeekDate))
//                .get()
//                .addOnSuccessListener { result ->
//                    // Convert documents to a list of Shift objects
//                    val shifts = result.documents.mapNotNull { document ->
//                        document.toObject(Shift::class.java)
//                    }
//                    onComplete(shifts)
//                }
//                .addOnFailureListener { exception ->
//                    // Handle failure and return an empty list
//                    onComplete(emptyList())
//                    println("Error getting shifts: ${exception.message}")
//                }
//        }
//    }

    private fun shiftTypeIndicator(): Int {
        when (LocalTime.now()) {
            in LocalTime.of(6, 0)..LocalTime.of(14, 0) -> return 1
            in LocalTime.of(14, 0)..LocalTime.of(22, 0) -> return 2
            in LocalTime.of(22, 0)..LocalTime.of(6, 0) -> return 3
            else -> return 0
        }
    }

    //write me a function that will get all the users personal information
    fun getUser(onComplete: (User?) -> Unit) {
        val db = FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    val userInfo = User(
                        email = user.email.toString(),
                        name = document.getString("name").orEmpty(),
                        phoneNumber = document.getString("phoneNumber").orEmpty(),
                        CompanyCode = document.getString("companyCode").orEmpty()
                    )
                    onComplete(userInfo)
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting document: ", exception)
                    onComplete(null)
                }
        } ?: run {
            onComplete(null)
        }
    }

    fun getUserShifts(dates: List<LocalDate>, onComplete: (List<LocalDate>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val listOfShifts = mutableListOf<LocalDate>()


        for (date in dates) {
            val year = date.year.toString()
            val month = date.monthValue.toString()
            val day = date.dayOfMonth.toString()

            val path = db.collection("Arrangements").document("2024")
                .collection("11").document("24")

//            path.update("Names", listOf("Bob", "Ari", "Dod")) // Using listOf() for Firestore
//                .addOnSuccessListener {
//                    Log.d("Firestore", "Names updated successfully")
//                }
//                .addOnFailureListener { e ->
//                    Log.w("Firestore", "Error updating names: ", e)
//                }

            path.get()
                .addOnSuccessListener { document ->
                        val names = document.get("Names") as? List<String>
                        if (names != null) {
                            for (name in names) {
                                if (name == currentUser?.displayName) {
                                    listOfShifts.add(date)
                                }
                            }
                        } else {
                            Log.d("Firestore", "Names field is not a list or is null")
                        }

                        // If we are done iterating over the dates, invoke the callback
                        if (date == dates.last()) {
                            onComplete(listOfShifts)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Firestore", "Error getting document: ", exception)

                        // If there's an error and we're on the last date, invoke the callback
                        if (date == dates.last()) {
                            onComplete(listOfShifts)
                        }
                    }
        }
    }



    fun getArrangementByDate(date: LocalDate, onComplete: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("Arrangements").document(date.year.toString())
        .collection(date.monthValue.toString()).document(date.dayOfMonth.toString()).get()
        .addOnSuccessListener { day ->
            if (day.exists()) {
                val names = day.get("Names") as? List<String> ?: emptyList()
                onComplete(names)
            } else {
                onComplete(emptyList())
            }
        }
        .addOnFailureListener {
            onComplete(emptyList())
        }
}

    fun getLastShifts(numberOfLogs: Long): List<Shift> {
        var lastLog = mutableListOf<Shift>()
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let {
            db.collection("Users").document(currentUser.uid)
                .collection("Shifts")
                .orderBy("Date.logIn", Query.Direction.DESCENDING)
                .limit(numberOfLogs).get()
                .addOnSuccessListener { shifts ->
                    if (!shifts.isEmpty) {
                        for (shift in shifts) {
                            lastLog.add(
                                Shift(
                                    timeStamp = Pair(
                                        (shift.get("Date.LogIn") as Timestamp),
                                        shift.get("Date.LogOut") as Timestamp
                                    ),
                                    location = Pair(
                                        shift.get("Location.LogIn") as GeoPoint,
                                        shift.get("Location.LogOut") as GeoPoint
                                    ),
                                    shiftType = shift.get("ShiftType") as ShiftType
                                )
                            )
                        }
                    }
                }
        }
        return lastLog
    }


        fun createUser(
            firstName: String,
            lastName: String,
            phoneNumber: String,
            email: String,
            password: String,
            companyCode: String,
            onSuccess: () -> Unit
        ) {
            val db = FirebaseFirestore.getInstance()

            // Step 1: Create a new user in Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User created successfully in Firebase Authentication
                        val firebaseUser = task.result.user
                        val uid = firebaseUser?.uid

                        if (firebaseUser != null) {
                            // Step 2: Update the user's displayName in Firebase Authentication
                            val profileUpdates = userProfileChangeRequest {
                                displayName = firstName
                            }

                            firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener { profileTask ->
                                    if (profileTask.isSuccessful) {
                                        Log.d(
                                            "CreateUser",
                                            "User displayName updated to: $firstName"
                                        )
                                    } else {
                                        Log.e(
                                            "CreateUser",
                                            "Failed to update displayName: ${profileTask.exception?.message}"
                                        )
                                    }
                                }

                            // Step 3: Add user details to Firestore
                            val userMap = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "phoneNumber" to phoneNumber,
                                "companyCode" to companyCode
                            )

                            if (uid != null) {
                                db.collection("Users").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        // Successfully added to Firestore
                                        Log.d("CreateUser", "User added to Firestore successfully")
                                        onSuccess() // Invoke the success callback
                                    }
                                    .addOnFailureListener { e ->
                                        // Firestore operation failed
                                        Log.e(
                                            "CreateUser",
                                            "Failed to add user to Firestore: ${e.message}"
                                        )
                                    }
                            }
                        } else {
                            Log.e(
                                "CreateUser",
                                "Failed to retrieve UID from Firebase Authentication"
                            )
                        }
                    } else {
                        // Firebase Authentication user creation failed
                        val exception = task.exception
                        Log.e("CreateUser", "User creation failed: ${exception?.message}")
                    }
                }
        }

        fun updateConstraints() {
            TODO("Not yet implemented")
        }

}
