package com.example.casemanagment.Remote

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.Appointments
import com.example.casemanagment.R
import java.text.SimpleDateFormat



class AppointmentAdapter  : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>(   ){

    lateinit var  Appointments : List<Appointments>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.cardviewappointment,
            viewGroup,
            false
        )
        return  ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.itemDescription.text= Appointments[i].description
       // holder.itemName.text=Appointments[i].name


        val parser = SimpleDateFormat("dd/mm/yyyy")
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val output: String = formatter.format(parser.parse(Appointments[i].appointmentDate))

        holder.itemAppointmentDate.text = output

    }

    override fun getItemCount(): Int {
        return Appointments.size
    }




    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        var itemDescription : TextView
        var itemAppointmentDate : TextView
      //  var itemName :TextView

        init {

           // itemName = itemView.findViewById(R.id.item_name)
            itemAppointmentDate =itemView.findViewById(R.id.item_appointmentDate)
            itemDescription = itemView.findViewById(R.id.item_description)

        }



    }



}