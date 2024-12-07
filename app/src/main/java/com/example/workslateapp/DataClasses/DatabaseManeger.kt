
import android.util.Log
import com.example.workslateapp.DataClasses.Message
import com.example.workslateapp.DataClasses.Shift
import com.example.workslateapp.DataClasses.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date


object DatabaseManeger {

    private val auth = FirebaseAuth.getInstance()

    // Add a shift for a user
    fun addLogInShift(date: LocalDate, location: GeoPoint, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val data = mapOf(
                "Date" to mapOf(
                    "LogIn" to Timestamp.now()
                ),
                "Location" to mapOf(
                        "LogIn" to location
                )
            )
            db.collection("Users").document(userId).collection("Shifts")
                .document(date.year.toString()).collection(date.monthValue.toString())
                .document(date.dayOfMonth.toString()).set(data, SetOptions.merge())
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
    fun addLogOutShift(date: LocalDate, location: GeoPoint, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val data = mapOf(
                "Date" to mapOf(
                    "LogOut" to Timestamp.now()
                ),
                "Location" to mapOf(
                    "LogOut" to location
                )
            )
            db.collection("Users").document(userId).collection("Shifts")
                .document(date.year.toString()).collection(date.monthValue.toString())
                .document(date.dayOfMonth.toString()).set(data,SetOptions.merge())
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

    //Get users information
    fun getUser(onComplete: (User?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    val userInfo = User(
                        email = user.email.toString(),
                        firstName = document.getString("firstName").orEmpty(),
                        lastName = document.getString("lastName").orEmpty(),
                        phoneNumber = document.getString("phoneNumber").orEmpty(),
                        companyCode = document.getString("companyCode").orEmpty()
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

    fun getMonthlyArrangementsOfUser(currentDate: LocalDate,onComplete: (List<LocalDate>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val listOfDates = mutableListOf<LocalDate>()

        getUser { user ->
        db.collection("Arrangements").document(user!!.companyCode)
            .collection(currentDate.year.toString()).document(currentDate.monthValue.toString()).get()
            .addOnSuccessListener { monthDates ->
                if (monthDates != null) {
                    monthDates.data?.forEach { (dayKey, dayValue) ->
                        val dayArray = dayValue as? List<String>
                        dayArray?.forEach { name ->
                            if (name == user.firstName) {
                                listOfDates.add(LocalDate.of(currentDate.year, currentDate.monthValue, dayKey.toInt()))
                            }
                        }
                    }
                    onComplete(listOfDates)
                } else {
                    onComplete(emptyList())
                }
            }
            .addOnFailureListener {
                onComplete(emptyList())
            }
        }
    }

    fun getArrangementByDate(date: LocalDate, onComplete: (List<String>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        getUser{user->
            db.collection("Arrangements").document(user!!.companyCode)
                .collection(date.year.toString()).document(date.monthValue.toString()).get()
                .addOnSuccessListener { day ->
                    if (day != null) {
                        val names = day.get(date.dayOfMonth.toString()) as List<String>
                        onComplete(names)
                    } else {
                        onComplete(emptyList())
                    }
                }
                .addOnFailureListener {
                    onComplete(emptyList())
                }
        }


    }

    fun getLastShifts(numberOfLogs: Long, onComplete: (List<Shift>) -> Unit) {
        val lastLog = mutableListOf<Shift>()
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val currentDate = LocalDate.now()

        currentUser?.let {
            db.collection("Users").document(currentUser.uid).collection("Shifts")
                .document(currentDate.year.toString()).collection(currentDate.monthValue.toString())
                .orderBy("Date.LogIn", Query.Direction.DESCENDING)
                .limit(numberOfLogs).get()
                .addOnSuccessListener { shifts ->
                    if (!shifts.isEmpty) {
                        for (shift in shifts) {
                            lastLog.add(
                                Shift(
                                    timeStamp = Pair((shift.get("Date.LogIn") as Timestamp), shift.get("Date.LogOut") as? Timestamp),
                                    location = Pair(shift.get("Location.LogIn") as GeoPoint, shift.get("Location.LogOut") as? GeoPoint)
                                )
                            )
                        }
                    }
                    onComplete(lastLog)
                }
                .addOnFailureListener {
                    onComplete(emptyList())
                }
        } ?: run {
            onComplete(emptyList())
        }
    }


    fun createUser(firstName: String, lastName: String, phoneNumber: String, email: String, password: String, companyCode: String, onSuccess: () -> Unit) {
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
                                    Log.d("CreateUser", "User displayName updated to: $firstName")
                                } else {
                                    Log.e("CreateUser", "Failed to update displayName: ${profileTask.exception?.message}")
                                }
                            }

                        // Step 3: Add user details to Firestore
                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "phoneNumber" to phoneNumber,
                            "companyCode" to companyCode,
                            "WeeklyConstraints" to mapOf(
                                "1" to listOf(false, false, false),  // Use listOf() for lists
                                "2" to listOf(false, false, false),
                                "3" to listOf(false, false, false),
                                "4" to listOf(false, false, false),
                                "5" to listOf(false, false, false),
                                "6" to listOf(false, false, false),
                                "7" to listOf(false, false, false)
                            )
                        )

                        if (uid != null) {
                            db.collection("Users").document(uid)
                                .set(userMap)
                                .addOnSuccessListener {
                                    // Successfully added to Firestore
                                    Log.d("CreateUser", "User added to Firestore successfully")
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    // Firestore operation failed
                                    Log.e("CreateUser", "Failed to add user to Firestore: ${e.message}"
                                    )
                                }
                        }
                    } else {
                        Log.e("CreateUser", "Failed to retrieve UID from Firebase Authentication")
                    }
                } else {
                    // Firebase Authentication user creation failed
                    val exception = task.exception
                    Log.e("CreateUser", "User creation failed: ${exception?.message}")
                }
            }
    }

    fun updateConstraints(checkBoxesStatus: MutableMap<String, List<Boolean>>, onComplete: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    currentUser?.let {
        db.collection("Users").document(currentUser.uid).update(mapOf("WeeklyConstraints" to checkBoxesStatus))
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    } ?: run {
        onComplete(false)
    }
}

    fun sendChatMessage(currentMessage: String) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val db = FirebaseDatabase.getInstance("https://workslate-1e401-default-rtdb.europe-west1.firebasedatabase.app")
        getUser{ user->
            if (user != null) {
                val path = "${user.companyCode}/messages"
                db.getReference(path).push()
                    .setValue(mapOf(
                        "sender" to "${user.firstName} ${user.lastName}",
                        "message" to currentMessage,
                        "timestamp" to dateFormat.format(Date())
                    ))
                    .addOnCompleteListener{
                        Log.w("Messages","${currentMessage} : was sent")
                    }
            }
        }
    }
    fun fetchMessages(onMessagesFetched: (List<Message>) -> Unit) {
        val db = FirebaseDatabase.getInstance("https://workslate-1e401-default-rtdb.europe-west1.firebasedatabase.app")
        getUser{user->
            if (user!=null){
                db.getReference("${user.companyCode}/messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages = mutableListOf<Message>()
                        for (messageSnapshot in snapshot.children) {
                            val sender = messageSnapshot.child("sender").value as? String ?: continue
                            val message = messageSnapshot.child("message").value as? String ?: continue
                            val timestamp = messageSnapshot.child("timestamp").value as? String ?: continue
                            messages.add(Message(sender, message, timestamp))
                        }
                        onMessagesFetched(messages)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Chat", "Failed to fetch messages: ${error.message}")
                    }
                })
            }
            }
    }
    fun listenForNewMessages(callback: (Message) -> Unit) {
        val db = FirebaseDatabase.getInstance("https://workslate-1e401-default-rtdb.europe-west1.firebasedatabase.app")
        getUser{user->
            db.getReference("${user?.companyCode}/messages")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val newMessage = snapshot.getValue(Message::class.java)
                        if (newMessage != null) {
                            callback(newMessage)
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("DatabaseManeger", "Failed to listen for new messages: ${error.message}")
                    }
                })
        }
    }
}
