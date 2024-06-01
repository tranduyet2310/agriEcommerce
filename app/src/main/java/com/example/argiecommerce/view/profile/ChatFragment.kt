package com.example.argiecommerce.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.argiecommerce.R
import com.example.argiecommerce.adapter.ChatAdapter
import com.example.argiecommerce.databinding.FragmentChatBinding
import com.example.argiecommerce.model.ChatList
import com.example.argiecommerce.model.UserFirebase
import com.example.argiecommerce.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private var firebaseUser: FirebaseUser? = null

    private lateinit var chatAdapter: ChatAdapter
    private var supplierList: List<UserFirebase> = arrayListOf()
    private var supplierChatList: List<ChatList> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)

        setupRecyclerView()
        firebaseUser = auth.currentUser
        getCurrentChatList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter.onClick = {
            val b = Bundle().apply {
                putString(Constants.USER_UID_KEY, it.uid)
            }
            this@ChatFragment.findNavController().navigate(R.id.action_messageFragment_to_chatDetailFragment, b)
        }
    }
    private fun getCurrentChatList(){
        val ref = firebaseDatabase.reference.child(Constants.CHAT_LIST).child(firebaseUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (supplierChatList as ArrayList).clear()
                for (s in snapshot.children){
                    val chatList = s.getValue(ChatList::class.java)
                    (supplierChatList as ArrayList).add(chatList!!)
                }
                retrieveChatList()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun setupRecyclerView(){
        binding.rcvChatList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            chatAdapter = ChatAdapter(requireContext(), supplierList, true)
            adapter = chatAdapter
        }
    }
    private fun retrieveChatList(){
        val ref = firebaseDatabase.reference.child(Constants.SUPPLIER)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (supplierList as ArrayList).clear()
                for (s in snapshot.children){
                    val user = s.getValue(UserFirebase::class.java)
                    for (eachChatList in supplierChatList){
                        if (user!!.uid.equals(eachChatList.id)){
                            (supplierList as ArrayList).add(user)
                        }
                    }
                }
//                chatAdapter = ChatAdapter(requireContext(), supplierList, true)
                chatAdapter.notifyDataSetChanged()
                if (supplierList.isEmpty()) binding.imageEmptyBox.visibility = View.VISIBLE
                else binding.imageEmptyBox.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}