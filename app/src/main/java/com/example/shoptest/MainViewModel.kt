package com.example.shoptest

import ClothesApi
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoptest.data.datamodels.AppRepository
import com.example.shoptest.data.datamodels.Datasource
import com.example.shoptest.data.datamodels.local.getDatabase
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.data.datamodels.models.Kategorie
import com.example.shoptest.data.datamodels.models.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user

    lateinit var profileRef: DocumentReference


    private val database = getDatabase(application)

    private val repository = AppRepository(ClothesApi, database)

    private val _datasource = MutableLiveData<List<Kategorie>>()
    val datasource: LiveData<List<Kategorie>>
        get() = _datasource

    init {
        _datasource.postValue(Datasource(application).loadCategories())
        loadData()
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
        }
    }

    fun signUp(email: String, password: String, name: String, telefonnummer: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {
                val profile = Profile(name, telefonnummer)
                profileRef.set(profile)
                setupUserEnv()
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _user.value = firebaseAuth.currentUser
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {
                setupUserEnv()

            }
        }
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

    fun updateLike(liked: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLike(liked, id)
        }
    }

    fun getDetail(id: Int): LiveData<Clothes> {
        return repository.getDetail(id)
    }
}