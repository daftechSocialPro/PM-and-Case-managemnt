package com.example.casemanagment.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.AffairHistory
import com.example.casemanagment.R
import com.example.casemanagment.Remote.AffairHistoryAdapter
import com.example.casemanagment.Remote.IMyAPI
import com.example.casemanagment.Remote.RetrofitClient
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CaseHistoriesFragment( val affairId: String?) : Fragment() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_case_histories, container, false)
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
        val empId = sharedPref.getString("empId",null)
        val compositeDisposable = CompositeDisposable()


        var iMyAPI: IMyAPI = RetrofitClient.getInstance(ipAdd, pub,port).create(IMyAPI::class.java)
        val dialog = SpotsDialog.Builder().setContext(this.requireActivity()).build()
        dialog.show()

        val affair = AffairHistory(affairId,empId)
        compositeDisposable.addAll(
            iMyAPI.GetAffairHistories(affair!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { s -> if (s.any()){
                        val recyclerView = view?. findViewById<RecyclerView>(R.id.recycler_view_affairHIs)
                        val adapter = AffairHistoryAdapter()

                        recyclerView.layoutManager= LinearLayoutManager(this.requireContext())
                        recyclerView.adapter = adapter
                        adapter.affairHistory= s

                        val adapterItems: ArrayAdapter<String>

                        val items = arrayListOf<String>("Add to Waiting","Transfer","Complete","Make Appointment","Revert","Send SMS")
                        adapterItems= ArrayAdapter<String>(requireActivity(), R.layout.list_action,items)
                        adapter.adapterItems= adapterItems





                        dialog.dismiss()


                    }
                    else {
                        Toast.makeText(this.requireContext(),"No Affairs ", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }},
                    { t: Throwable? ->
                        Toast.makeText(this.requireContext(), t!!.message, Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()

                    }


                ))


    }


}