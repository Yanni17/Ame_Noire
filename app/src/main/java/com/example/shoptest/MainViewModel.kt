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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Firestore - Firebase.
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var listOfClothes = mutableListOf<Int>()
    var listOfCartItems = mutableListOf<CartItem>()
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

    val allClothes = repository.allClothes

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

        _user.value = firebaseAuth.currentUser

        firebaseAuth.currentUser?.let {
            profileRef = firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            profileRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val profile = documentSnapshot.toObject(Profile::class.java)
                    val cartList = profile?.cartList // Warenkorbliste
                    listOfCartItems = cartList!!.toMutableList()
                    val likedList = profile?.likedList
                    listOfClothes = likedList!!.toMutableList()
                }
            }
        }
    }

    fun signUp(email: String, password: String, name: String, telefonnummer: String) {

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || telefonnummer.isEmpty()) {

            val toast = Toast.makeText(
                getApplication(),
                "Bitte füllen Sie alle Felder aus!",
                Toast.LENGTH_LONG
            )
            toast.show()
            return
        } else if (password.length < 6) {
            val toast = Toast.makeText(
                getApplication(),
                "Das Passwort muss mindestens 6 Zeichen lang sein.",
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

                Toast.makeText(getApplication(), "Registrierung erfolgreich!", Toast.LENGTH_LONG)
                    .show()

            } else {

                if (it.exception is FirebaseAuthUserCollisionException) {
                    val toast = Toast.makeText(
                        getApplication(),
                        "Die E-Mail-Adresse wird bereits verwendet.",
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
        listOfCartItems.clear()
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {

            val toast = Toast.makeText(
                getApplication(),
                "Bitte füllen Sie die Felder aus!",
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
                                addLikedItem(item)
                                updateLike(true, item)
                            }
                        }
                    }
                Toast.makeText(getApplication(), "Erfolgreich Angemeldet!", Toast.LENGTH_LONG)
                    .show()
            } else {

                Log.e("Error", "${it.exception}")

                if (it.exception is FirebaseAuthInvalidUserException) {
                    // e-mail existiert nicht
                    val toast = Toast.makeText(
                        getApplication(),
                        "Die eingegebene E-Mail ist nicht registriert.",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    // passwort falsch
                    val toast = Toast.makeText(
                        getApplication(),
                        "Das eingegebene Passwort ist falsch.",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    // alle anderen
                    val toast = Toast.makeText(
                        getApplication(),
                        "Fehler bei der Anmeldung. Bitte versuchen Sie es erneut.",
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

    fun removeFromCart(productId: Int) {

        val existingItem = listOfCartItems.find { it.productId == productId }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                listOfCartItems.remove(existingItem)
            }
            firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
                .update("cartList", listOfCartItems)
        }
    }


    fun addToCart(productId: Int) {

        val existingItem = listOfCartItems.find { it.productId == productId }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            val newItem = CartItem(productId, 1)
            listOfCartItems.add(newItem)
        }
        firestore.collection("Profile").document(firebaseAuth.currentUser!!.uid)
            .update("cartList", listOfCartItems)

    }

    //ROOM_

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

    fun startScaleAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(getApplication(), R.anim.scale_up)
        view.startAnimation(animation)
    }


}