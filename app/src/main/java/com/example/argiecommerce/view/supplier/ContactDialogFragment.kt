package com.example.argiecommerce.view.supplier

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentContactDialogBinding


class ContactDialogFragment : AppCompatDialogFragment() {
    private lateinit var binding: FragmentContactDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDialogBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnSave.setOnClickListener{
            dialog?.dismiss()
        }
    }


}