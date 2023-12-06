package com.example.casemanagment.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.AffairHistory
import com.example.casemanagment.R
import com.example.casemanagment.Remote.*
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CaseAttachmentsFragment(val documents:Array<String>?) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_case_attachments, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences(
            "Setting",
            Context.MODE_PRIVATE
        );

        val ipAdd= sharedPref.getString("IpAddress", null)
        val pub = sharedPref.getString("PubName", null)
        val port = sharedPref.getString("Port", null)

        val dialog = SpotsDialog.Builder().setContext(this.requireActivity()).build()
        dialog.show()

                        val recyclerView = view?. findViewById<RecyclerView>(R.id.recycler_view_Attachments)
                        val adapter = DocumentAdapter()

                        recyclerView.layoutManager= LinearLayoutManager(this.requireContext())
                        recyclerView.adapter = adapter
        if (documents != null) {
            adapter.documents= documents
        }


                        dialog.dismiss()





    }

}