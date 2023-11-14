package com.example.shoptest

import ClothesApi
import android.app.Application
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoptest.data.datamodels.AppRepository
import com.example.shoptest.data.datamodels.Datasource
import com.example.shoptest.data.datamodels.local.getDatabase
import com.example.shoptest.data.datamodels.models.CartItem
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.data.datamodels.models.Kategorie
import com.example.shoptest.data.datamodels.models.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Firestore - Firebase.
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var listOfClothes = mutableListOf<Int>()

    private val _liveListOfCartItems = MutableLiveData<List<CartItem>>(emptyList())
    val liveListOfCartItems: LiveData<List<CartItem>>
        get() = _liveListOfCartItems

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user
    lateinit var profileRef: DocumentReference

    // ROOM.
    private val database = getDatabase(application)
    private val repository = AppRepository(ClothesApi, database)
    private val _datasource = MutableLiveData<List<Kategorie>>()
    val datasource: LiveData<List<Kategorie>>
        get() = _datasource

    var allClothes: List<Clothes> = emptyList()

    init {
        _datasource.postValue(Datasource(application).loadCategories())
        setupUserEnv()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getClothes()
        }
    }

    //FIREBASE_
    fun setupUserEnv() {
        // user in die Livedata speichern
        _user.value = firebaseAuth.currentUser

        firebaseAuth.currentUser?.let { user ->
            //profileref holen
            profileRef = firestore.collection("Profile").document(user.uid)

            profileRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val profile = documentSnapshot.toObject(Profile::class.java)
                        //Warenkorbliste holen
                        val cartList = profile?.cartList
                        if (cartList != null) {
                            _liveListOfCartItems.value = cartList.toMutableList()
                        }
                        //Wunschliste holen
                        val likedList = profile?.likedList
                        if (likedList != null) {
                            listOfClothes = likedList.toMutableList()
                        }
                    }
                } else {
                    // Fehler beim Abrufen der Daten aus Firestore
                }
            }
        }
    }

    fun signUp(email: String, password: String, name: String, telefonnummer: String) {

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || telefonnummer.isEmpty()) {

            val toast = Toast.makeText(
                getApplication(),
                "${getApplication<Application>().getString(R.string.felder)}",
                Toast.LENGTH_LONG
            )
            toast.show()
            return

        } else if (password.length < 6) {
            val toast = Toast.makeText(
                getApplication(),
                "${getApplication<Application>().getString(R.string.passwort)}",
                Toast.LENGTH_LONG
            )
            toast.show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {
                setupUserEnv()
                val profile = Profile(name, telefonnummer)
                profileRef.set(profile)

                Toast.makeText(
                    getApplication(),
                    "${getApplication<Application>().getString(R.string.registrierung)}",
                    Toast.LENGTH_LONG
                )
                    .show()

            } else {

                if (it.exception is FirebaseAuthUserCollisionException) {
                    val toast = Toast.makeText(
                        getApplication(),
                        "${getApplication<Application>().getString(R.string.e_mail)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }
        }
    }

    fun signOut() {

        if (listOfClothes != null) {
            for (item in listOfClothes) {
                updateLike(false, item)
            }
            listOfClothes.clear()
        }
        firebaseAuth.signOut()
        _liveListOfCartItems.postValue(emptyList())
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {

            val toast = Toast.makeText(
                getApplication(),
                "${getApplication<Application>().getString(R.string.felder)}",
                Toast.LENGTH_LONG
            )
            toast.show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {
                setupUserEnv()

                firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        var profile = it.toObject(Profile::class.java)
                        if (profile?.likedList != null) {
                            for (item in profile!!.likedList) {
                                updateLike(true, item)
                            }
                        }
                    }
                Toast.makeText(
                    getApplication(),
                    "${getApplication<Application>().getString(R.string.angemeldet)}",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {

                Log.e("Error", "${it.exception}")

                if (it.exception is FirebaseAuthInvalidUserException) {
                    // e-mail existiert nicht
                    val toast = Toast.makeText(
                        getApplication(),
                        "${getApplication<Application>().getString(R.string.falsche_mail)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    // passwort falsch
                    val toast = Toast.makeText(
                        getApplication(),
                        "${getApplication<Application>().getString(R.string.falsches_passwort)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    // alle anderen
                    val toast = Toast.makeText(
                        getApplication(),
                        "${getApplication<Application>().getString(R.string.fehler_anmeldung)}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }
        }
    }


    //FIRESTORE_
    fun addLikedItem(id: Int) {
        listOfClothes.add(id)
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            .update("likedList", listOfClothes)
    }

    fun removeLikedItem(id: Int) {
        listOfClothes.remove(id)
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            .update("likedList", listOfClothes)
    }

    fun addToCartLive(productId: Int, title: String, price: Double, image: String) {
        //aktuelles Item holen
        val existingItem = _liveListOfCartItems.value!!.find { it.productId == productId }

        //ggf. quantity erhöhen
        if (existingItem != null) {
            existingItem.quantity++
            val currentCartItems2 = _liveListOfCartItems.value!!.toMutableList()
            _liveListOfCartItems.value = currentCartItems2
            //neues Item erstellen
        } else {
            val newItem = CartItem(productId, 1, title, price, image)
            val currentCartItems = _liveListOfCartItems.value!!.toMutableList()
            currentCartItems.add(newItem)
            _liveListOfCartItems.value = currentCartItems
        }
        //liste für firestore updaten
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            .update("cartList", _liveListOfCartItems.value)
    }

    fun removeCartLive(productId: Int) {

        val existingItem = _liveListOfCartItems.value!!.find { it.productId == productId }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
                val currentCartItems2 = _liveListOfCartItems.value!!.toMutableList()
                _liveListOfCartItems.value = currentCartItems2
            } else {
                val currentCartItems = _liveListOfCartItems.value!!.toMutableList()
                currentCartItems.remove(existingItem)
                _liveListOfCartItems.value = currentCartItems
            }
            firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
                .update("cartList", _liveListOfCartItems.value)
        }

    }

    //ROOM_
    fun getAllClothes() {
        allClothes = repository.getAllClothes()
    }

    fun getAllHerren(): LiveData<List<Clothes>> {
        return repository.getAllHerren()
    }

    fun getAllDamen(): LiveData<List<Clothes>> {
        return repository.getAllDamen()
    }

    fun getAllElectrics(): LiveData<List<Clothes>> {
        return repository.getAllElectrics()
    }

    fun getAllJewelry(): LiveData<List<Clothes>> {
        return repository.getAllJewelry()
    }

    fun getAllLiked(): LiveData<List<Clothes>> {
        return repository.getAllLiked()
    }

    fun updateLike(liked: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLike(liked, id)
        }
    }

    fun getDetail(id: Int): LiveData<Clothes> {
        return repository.getDetail(id)
    }

    // ANIMATION
    fun startScaleAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(getApplication(), R.anim.scale_up)
        view.startAnimation(animation)
    }

    fun clearList() {

        _liveListOfCartItems.postValue(emptyList())
        val new = listOf<CartItem>()
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            .update("cartList", new)
    }

    fun berechneLieferzeitraum(): String {
        val heute = Calendar.getInstance()

        // Ersten Tag berechnen (3 Tage später)
        val ersterTag = Calendar.getInstance()
        ersterTag.add(Calendar.DAY_OF_MONTH, 3)

        // Zweiten Tag berechnen (4 Tage später)
        val zweiterTag = Calendar.getInstance()
        zweiterTag.add(Calendar.DAY_OF_MONTH, 4)

        // Datumsformat festlegen (zum Beispiel "EEE d MMM")
        val dateFormat = SimpleDateFormat("EEE d MMM", Locale.getDefault())

        // Format für die Rückgabe erstellen
        val lieferzeitraum =
            "${dateFormat.format(ersterTag.time)} - ${dateFormat.format(zweiterTag.time)}"

        return lieferzeitraum
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        adress: String,
        zipCode: String,
        city: String
    ) {
        if (lastName.isNotEmpty() && adress.isNotEmpty() && zipCode.isNotEmpty() && city.isNotEmpty()) {
            firestore.collection("Profile")
                .document(firebaseAuth.currentUser!!.uid)
                .update("vorName", "$firstName")
            firestore.collection("Profile")
                .document(firebaseAuth.currentUser!!.uid)
                .update("nachName", "$lastName")
            firestore.collection("Profile")
                .document(firebaseAuth.currentUser!!.uid)
                .update("adresse", "$adress")
            firestore.collection("Profile")
                .document(firebaseAuth.currentUser!!.uid)
                .update("stadt", "$city")
            firestore.collection("Profile")
                .document(firebaseAuth.currentUser!!.uid)
                .update("plz", "$zipCode")

            showToast(getApplication<Application>().getString(R.string.erfolgreich))
        } else {
            showToast("Error")
        }
    }

    fun showToast(message: String) {
        val toast = Toast.makeText(
            getApplication(),
            message,
            Toast.LENGTH_LONG
        )
        toast.show()
    }

}