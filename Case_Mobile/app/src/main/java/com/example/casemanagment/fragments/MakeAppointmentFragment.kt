package com.example.casemanagment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.casemanagment.Model.TblUser
import com.example.casemanagment.Model.makeAppointment
import com.example.casemanagment.Remote.IMyAPI
import com.example.casemanagment.Remote.RetrofitClient
import com.example.casemanagment.fragments.AppointmentFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MakeAppointmentFragment(val affairId: String?) : BottomSheetDialogFragment(){

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_make_appointment, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

      //  Toast.makeText(context,"http://$ipAdd:$port/$pub/MobileUser/",Toast.LENGTH_SHORT).show()
        val btnMakeAppointment : TextView = view?.findViewById(R.id.btn_make_Appointment)

//
        val compositeDisposable = CompositeDisposable()
        var iMyAPI: IMyAPI = RetrofitClient.getInstance(ipAdd, pub, port).create(IMyAPI::class.java)
val Date  = view?.findViewById<DatePicker>(R.id.Edit_DatePicker)

        var editdate : String ? =""
        val today = Calendar.getInstance()
        Date.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            val msg = "$month/$day/$year"
            editdate=msg;
           // Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
        }



        var editTime:String=""
        val Time = view?.findViewById<TimePicker>(R.id.Edit_TimePicker)
     Time.setOnTimeChangedListener { _, hour, minute -> var hour = hour
                var am_pm = ""
                // AM_PM decider logic
                when {hour == 0 -> { hour += 12
                    am_pm = "AM"
                }
                    hour == 12 -> am_pm = "PM"
                    hour > 12 -> { hour -= 12
                        am_pm = "PM"
                    }
                    else -> am_pm = "AM"
                }

                val hours = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "$hours : $min $am_pm"
                editTime= msg

       //  Toast.makeText(this.context,msg,Toast.LENGTH_SHORT).show()

            }

        btnMakeAppointment.setOnClickListener {



            //  Toast.makeText(context,"message:"+ messages.message + "affair "+messages.affairId +"empid "+messages.employeeId,Toast.LENGTH_LONG).show()


            if (editdate ==""){
                val date = Date()
                val format = SimpleDateFormat("dd/MM/yyyy")
                val dateString = format.format(date)

                editdate = dateString
            }
            if (editTime==""){

                val date = Date()
                val format = SimpleDateFormat("HH:mm")
                val dateString = format.format(date)

                editTime = dateString


            }



            val appointment = makeAppointment(editdate, editTime, empId, affairId);


           // Toast.makeText(this.requireActivity()," date : "+appointment.executionDate+ " time:"+appointment.executionTime+" empid: "+appointment.employeeId+ " affairid :"+appointment.affairId,Toast.LENGTH_SHORT).show()


            val dialog = SpotsDialog.Builder().setContext(this.requireContext()).build()
            dialog.show()
            compositeDisposable.addAll(
                    iMyAPI.MakeAPpointment(appointment)
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
                                        Toast.makeText(this.requireContext(), t!!.message, Toast.LENGTH_SHORT)
                                                .show()
                                        dialog.dismiss()

                                    }


                            ))



        }



    }


}