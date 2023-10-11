package com.example.shoptest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.shoptest.MainActivity
import com.example.shoptest.MainViewModel
import com.example.shoptest.R
import com.example.shoptest.adapter.CartAdapter
import com.example.shoptest.data.datamodels.models.Clothes
import com.example.shoptest.data.datamodels.models.Profile
import com.example.shoptest.databinding.FragmentCashoutBinding

class CashoutFragment : Fragment() {

    private lateinit var binding: FragmentCashoutBinding
    val viewmodel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCashoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.setToolbarTitle("Warenkorb")

        val cartAdapter = CartAdapter(emptyList(), viewmodel)
        binding.cartRV.adapter = cartAdapter


        // Im Fragment oder ViewModel
        viewmodel.allClothes.observe(viewLifecycleOwner) { clothesList ->
            // Dieser Codeblock wird aufgerufen, wenn sich die Daten in allClothes ändern
            if (viewmodel.firebaseAuth.currentUser != null) {

                val userId = viewmodel.firebaseAuth.currentUser!!.uid
                val profileRef = viewmodel.firestore.collection("Profile").document(userId)

                profileRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val profile = documentSnapshot.toObject(Profile::class.java)
                        val cartList = profile?.cartList // Warenkorbliste

                        // Jetzt kannst du auf clothesList zugreifen, da du im Observer-Block bist
                        val productsInCart: List<Clothes> = clothesList.filter { clothes ->
                            // clothes Objekte in Warenkorbliste
                            cartList!!.any { it.productId == clothes.id }
                        }
                        cartAdapter.updateData(productsInCart)
                        Log.e("List2", "$clothesList")
                        Log.e("List", "$productsInCart")
                    } else {
                        // Das Profil des Benutzers existiert nicht.
                    }
                }.addOnFailureListener { exception ->
                    // Fehler bei der Firestore-Abfrage.
                }
            } else {
                val toast = Toast.makeText(
                    requireContext(),
                    "Warenkorb ist leer",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }
        }

    }

}