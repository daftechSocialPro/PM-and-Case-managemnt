package com.example.casemanagment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SettingBottomSheetFragment : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.setting_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  edtIpAddress = view?.findViewById<EditText>(R.id.edt_ipAdress);
        val edtPubName = view?.findViewById<EditText>(R.id.edt_pubName);
        val edtPort = view?.findViewById<EditText>(R.id.edt_Port)

        view?.findViewById<View>(R.id.btn_save)?.setOnClickListener {
            val ipAddress = edtIpAddress.text.toString();
            val pubName  = edtPubName.text.toString();
            val port =  edtPort.text.toString();

            val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences(
                "Setting",
                Context.MODE_PRIVATE
            );
            val editor = sharedPref.edit();

            editor.apply{

                putString("IpAddress", ipAddress);
                putString("PubName", pubName);
                putString("Port",port)

            }.apply();

            this.dismiss();
            Toast.makeText(this.requireContext(), "Data Saved", Toast.LENGTH_SHORT).show();
        }

        val sharedPreferences= this.requireActivity().getSharedPreferences(
            "Setting",
            Context.MODE_PRIVATE
        );
        val ipadd = sharedPreferences.getString("IpAddress", null);
        val Pub = sharedPreferences.getString("PubName", null);
        val po = sharedPreferences.getString("Port",null)


        edtIpAddress.setText(ipadd);
        edtPubName.setText(Pub);
        edtPort.setText(po)



    }


}