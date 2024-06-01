package com.example.argiecommerce.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.ChatAdapter
import com.example.argiecommerce.databinding.FragmentSearchChatBinding
import com.example.argiecommerce.model.UserFirebase
import com.example.argiecommerce.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchChatFragment : Fragment() {
    private lateinit var binding: FragmentSearchChatBinding

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private lateinit var chatAdapter: ChatAdapter
    private var supplierList: List<UserFirebase> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchChatBinding.inflate(inflater)

        setupRecyclerView()
        getAllSuppliers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForSupplier(s.toString().lowercase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        chatAdapter.onClick = {
            val b = Bundle().apply {
                putString(Constants.USER_UID_KEY, it.uid)
            }
            this@SearchChatFragment.findNavController().navigate(R.id.action_messageFragment_to_chatDetailFragment, b)
        }
    }

    private fun getAllSuppliers() {
        val userUid = auth.currentUser!!.uid
        val reference = firebaseDatabase.reference.child(Constants.SUPPLIER)
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (supplierList as ArrayList<UserFirebase>).clear()
                if (binding.edtSearch.text.toString() == ""){
                    for (s in snapshot.children){
                        val user: UserFirebase? = s.getValue(UserFirebase::class.java)
                        if (!(user!!.uid).equals(userUid)){
                            (supplierList as ArrayList<UserFirebase>).add(user)
                        }
                    }
//                    chatAdapter = ChatAdapter(requireContext(), supplierList, false)
//                    binding.rcvSearchList.adapter = chatAdapter
                    chatAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun searchForSupplier(supplierName: String){
        val userUid = auth.currentUser!!.uid
        val reference = firebaseDatabase.reference.child(Constants.SUPPLIER).orderByChild("search")
            .startAt(supplierName)
            .endAt(supplierName + "\uf8ff") // \uf8ff = %

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (supplierList as ArrayList<UserFirebase>).clear()
                for (s in snapshot.children){
                    val user: UserFirebase? = s.getValue(UserFirebase::class.java)
                    if (!(user!!.uid).equals(userUid)){
                        (supplierList as ArrayList<UserFirebase>).add(user)
                    }
                    chatAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setupRecyclerView() {
        binding.rcvSearchList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            chatAdapter = ChatAdapter(requireContext(), supplierList, false)
            adapter = chatAdapter
        }
    }

}