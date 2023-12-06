package com.example.casemanagment.Remote

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.Appointments
import com.example.casemanagment.PdfViewer
import com.example.casemanagment.R
import com.example.casemanagment.caseScreen
import java.text.SimpleDateFormat

class DocumentAdapter : RecyclerView.Adapter<DocumentAdapter.ViewHolder>(   ){

    lateinit var  documents : Array<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.document_card,
            viewGroup,
            false
        )
        return  ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        val d = i + 1
        holder.itemDocumentName.text = "file $d"
        holder.itembutton_preview.setOnClickListener{ v->

            val intent = Intent(v.context, PdfViewer::class.java)
                    intent.putExtra("document",documents[i])
            v.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return documents.size
    }




    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


       var  itemDocumentName :TextView
        var itembutton_preview:Button

        init {


            itemDocumentName =itemView.findViewById(R.id.document_name)
            itembutton_preview = itemView.findViewById(R.id.button_preview)

        }



    }



}