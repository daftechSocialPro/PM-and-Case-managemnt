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
import com.example.casemanagment.Model.TblUser
import com.example.casemanagment.R
import com.example.casemanagment.Remote.CustomAdapter
import com.example.casemanagment.Remote.IMyAPI
import com.example.casemanagment.Remote.RetrofitClient
import com.example.casemanagment.Remote.WaitingListAdapter
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaitingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.requireActivity().setTitle("Waitng List")
        return inflater.inflate(R.layout.fragment_waiting_list, container, false)
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

        val user = TblUser(empId)
        compositeDisposable.addAll(
                iMyAPI.GetWaitingList(user!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { s -> if (s.any()){
                                    val recyclerView = view?. findViewById<RecyclerView>(R.id.recycler_view_Waiting)
                                    val adapter = WaitingListAdapter()

                                    recyclerView.layoutManager= LinearLayoutManager(this.requireContext())
                                    recyclerView.adapter = adapter
                                    adapter.affairs= s


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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WaitingListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WaitingListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}