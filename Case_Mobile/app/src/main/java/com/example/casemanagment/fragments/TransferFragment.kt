@file:Suppress("DEPRECATION")

package com.example.casemanagment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.BadTokenException
import android.widget.*
import androidx.loader.content.CursorLoader
import com.example.casemanagment.Model.TblUser
import com.example.casemanagment.Model.TransferAffair
import com.example.casemanagment.Model.results
import com.example.casemanagment.Remote.IMyAPI
import com.example.casemanagment.Remote.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.CertificatePinner.Companion.DEFAULT
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http2.Huffman.decode
import java.io.File
import android.util.Base64
import okhttp3.RequestBody.Companion.asRequestBody

import java.lang.Byte.decode
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.toRequestBody


class TransferFragment(val affairHisId: String?) : BottomSheetDialogFragment(){

    var imagePath:String?=null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_transfer, container, false)
        return view
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
        val empId = sharedPref.getString("empId", null)

        //Toast.makeText(context,"http://$ipAdd:$port/$pub/MobileUser/",Toast.LENGTH_SHORT).show()
        val btnTransferCase : TextView = view?.findViewById(R.id.btn_transfer_case)
        val autoCompleteTextView: AutoCompleteTextView = view?.findViewById(R.id.auto_complete_txt_transfer);
        val auto_complete_txt_transfer_emp = view?.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt_transfer_emp)
        val auto_complete_txt_transfer_str = view?.findViewById<AutoCompleteTextView>(R.id.auto_complete_txt_transfer_str)
        val empRelative:TextInputLayout = view?.findViewById(R.id.item_employee_transfer)
        val structureRelative : TextInputLayout = view?.findViewById(R.id.item_structure_transfer)

        val transferTitle :TextView = view?.findViewById(R.id.transferTitle)
        val transferCurrentState :TextView =view?.findViewById(R.id.transferCurrentState)
        val transferNextState :TextView =view?.findViewById(R.id.transferNextState)
        val transferNeededDocuments :TextView =view?.findViewById(R.id.transferNeededDocuments)

        var selectedEmpID :String ?= null
        var selectedStrId : String ? = null

        structureRelative.visibility=View.INVISIBLE
        empRelative.visibility=View.INVISIBLE
        val items = arrayListOf<String>("Department", "Employee")

        val adapterItems :ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.list_action, items)
        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->



            val id = parent.getItemIdAtPosition(position).toString()
            when {
                id=="0"->{
                    structureRelative.visibility=View.VISIBLE
                    empRelative.visibility=View.INVISIBLE
                    selectedEmpID=null

                }
                id=="1"->{
                    empRelative.visibility=View.VISIBLE
                    structureRelative.visibility=View.INVISIBLE
                    selectedStrId=null
                }
            }

        }
        val compositeDisposable = CompositeDisposable()
        val compositeDisposable2 = CompositeDisposable()
        var iMyAPI: IMyAPI = RetrofitClient.getInstance(ipAdd, pub, port).create(IMyAPI::class.java)

            val chooseFile = view?.findViewById<Button>(R.id.chooseFile)

        chooseFile.setOnClickListener {
            val intent=Intent()
            intent.setType("*/*")
            intent.setAction(Intent.ACTION_PICK)
            val result = Intent.createChooser(intent, "Choose File")
            startActivityForResult(result, 10)

        }



        val result= results(affairHisId)
        val dialog = SpotsDialog.Builder().setContext(this.requireContext()).build()
        compositeDisposable.addAll(
                iMyAPI.getResults(result)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { s ->
                                    //Toast.makeText(this.requireContext(),s, Toast.LENGTH_SHORT).show()

                                    transferTitle.setText(s.affairType)
                                    transferCurrentState.setText("Current State: ${s.currentState}")
                                    transferNextState.setText("Next State: ${s.nextState}")
                                    transferNeededDocuments.setText("Needed Documents: ${
                                        if (s.neededDocuments != null) {
                                            s.neededDocuments
                                        } else {
                                            ""
                                        }
                                    }")


                                    val items = arrayListOf<String>()
                                    for (item in s.employees!!) {
                                        items.add(item.empName.toString())
                                    }
                                    val items2 = arrayListOf<String>()
                                    for (item in s.structures!!) {
                                        items2.add(item.structureName.toString())
                                    }

                                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.list_action, items)
                                    val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), R.layout.list_action, items2)

                                    auto_complete_txt_transfer_emp.setAdapter(adapter)
                                    auto_complete_txt_transfer_str.setAdapter(adapter2)

                                    auto_complete_txt_transfer_emp.setOnItemClickListener { parent, view, position, id ->

                                        val empName = parent.getItemAtPosition(position).toString()
                                        for (item in s.employees!!) {

                                            if (item.empName == empName) {
                                                selectedEmpID = item.empId!!;
                                                // Toast.makeText(view.context, selectedEmpID, Toast.LENGTH_SHORT).show()

                                            }
                                        }

                                    }
                                    auto_complete_txt_transfer_str.setOnItemClickListener { parent, view, position, id ->

                                        var strName = parent.getItemAtPosition(position).toString()


                                        for (sstr in s.structures!!) {
                                            if (sstr.structureName == strName) {
                                                selectedStrId = sstr.strucutreId!!
                                                // Toast.makeText(view.context, selectedStrId, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }

                                    dialog.dismiss()


                                },
                                { t: Throwable? ->
                                    Toast.makeText(this.requireContext(), t!!.message, Toast.LENGTH_SHORT)
                                            .show()
                                    dialog.dismiss()

                                }


                        ))

        var mSelected_files: List<String>? = null
        val btn_transfer_case = view?.findViewById<TextView>(R.id.btn_transfer_case)

        btn_transfer_case.setOnClickListener {
            val edit_remark_transfer  = view?.findViewById<EditText>(R.id.edit_remark_transfer)
            //  Toast.makeText(context,"message:"+ messages.message + "affair "+messages.affairId +"empid "+messages.employeeId,Toast.LENGTH_LONG).show()
            val editTransfer = edit_remark_transfer.text.toString();
           // val transferAffair = TransferAffair(empId, affairHisId, selectedEmpID, selectedStrId, editTransfer, null);
            val dialog = SpotsDialog.Builder().setContext(this.requireContext()).build()
            dialog.show()

            try {
                if (empId!=null&& affairHisId!=null && ((selectedEmpID!=null && selectedStrId== null)||(selectedEmpID==null && selectedStrId!=null)) && imagePath!=null ) {
                    // Toast.makeText(this.requireContext(), "hello", Toast.LENGTH_SHORT).show()
               val file : File = File(imagePath)
//               val  photoContent : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//               val photo : MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.getName(), photoContent)

//                    val photoPart: MultipartBody.Part? = if (file != null) {
//                        val bytes = Base64.decode(imagePath, Base64.DEFAULT)
//                        val photoRequestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
//                        MultipartBody.Part.createFormData("photo", file.getName(), photoRequestBody)
//                    } else {
//                        null
//                    }


                    val photoPart: MultipartBody.Part? = if (file != null) {
                        val photoRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("photo", file.getName(), photoRequestBody)
                    } else {
                        null
                    }

                    val empIdd = empId?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val affairHisIdd = affairHisId?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val toEmployeeId = selectedEmpID?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val toStructureId =  selectedStrId?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val remark = editTransfer?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val caseTypeId = "null"?.toRequestBody("multipart/form-data".toMediaTypeOrNull())




 compositeDisposable2.addAll(
            iMyAPI.transferAffair(photoPart, empIdd, affairHisIdd, toEmployeeId, toStructureId, remark, caseTypeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { s ->
                                Toast.makeText(this.requireContext(), s, Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                                val intent =
                                        Intent(view.context, HomeScreen::class.java)

                                val user = TblUser(empId)
                                intent.putExtra("userViewModel", user);
                                view.context.startActivity(intent)


                            },
                            { t: Throwable? ->
                                Toast.makeText(this.requireContext(), t!!.message, Toast.LENGTH_LONG)
                                        .show()
                                dialog.dismiss()

                            }


                    ))
}
                else {
    Toast.makeText(this.requireContext(), "Input must not be empty", Toast.LENGTH_SHORT).show()

             dialog.dismiss()
                }


            }catch (e: Exception) {
                //dialog.dismiss()
                Toast.makeText(this.requireContext(), e.message, Toast.LENGTH_SHORT)

            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==10){

            if(resultCode==RESULT_OK){
                val uri: Uri? = data!!.getData()
                imagePath =  getRealPathFromURI(uri!!)
                Toast.makeText(requireContext(), imagePath, Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun  getRealPathFromURI(contentUri: Uri):String {



        val proj :Array<String> = Array<String>(0, { MediaStore.Images.Media.DATA })
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index:Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result= cursor.getString(column_index)
        cursor.close()
        return result

    }

}