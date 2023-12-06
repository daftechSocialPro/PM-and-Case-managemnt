package com.example.casemanagment.Remote

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.Affairs
import com.example.casemanagment.R
import com.example.casemanagment.caseScreen

class WaitingListAdapter: RecyclerView.Adapter<WaitingListAdapter.ViewHolder>(   ){

    lateinit var  affairs : List<Affairs>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_view,
            viewGroup,
            false
        )
        return  ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.itemAffairType.text= affairs[i].affairType
        holder.itemFromEmployee.text=affairs[i].fromEmplyee
        holder.itemFromStructure.text = affairs[i].fromStructure
        holder.itemSubject.text = affairs[i].subject
        holder.itemReciverType.text = affairs[i].reciverType
        holder.itemAffaiirNo.text = affairs[i].affairNumber
        holder.itemStatus.text =affairs[i].affairHistoryStatus

        if (affairs[i].affairHistoryStatus.toString().toLowerCase()=="seen"){
            holder.affairCard.setCardBackgroundColor(Color.parseColor("#1ABB9B"))

        }
        else{
            holder.affairCard.setCardBackgroundColor(Color.parseColor("#F1556C"))
        }


        holder.goToDetails.setOnClickListener { v ->
            val intent = Intent(v.context, caseScreen::class.java)
            intent.putExtra("title",affairs[i].affairType)
            intent.putExtra("affairId",affairs[i].affairId)
            intent.putExtra("documents",affairs[i].document)

            v.context.startActivity(intent)
        }

        holder.confirmedBySec.setText(affairs[i].confirmedSecratary)

        if (affairs[i].confirmedSecratary!!.split(' ')[0]=="Not"){

            holder.LinearItem2.setBackgroundColor(Color.parseColor("#F1556C"))
        }
        else {
            holder.LinearItem2.setBackgroundColor(Color.parseColor("#709D99"))
        }

    }

    override fun getItemCount(): Int {
        return affairs.size
    }




    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        var itemAffairType : TextView
        var itemFromEmployee : TextView
        var itemFromStructure : TextView
        var itemSubject : TextView
        var itemReciverType : TextView
        var itemAffaiirNo : TextView
        var itemStatus: TextView
        var affairCard : CardView
        var goToDetails: CardView
        var confirmedBySec: TextView
        var LinearItem2 : LinearLayout

        init {

            //  itemImage = itemView.findViewById(R.id.item_image)
            itemAffairType =itemView.findViewById(R.id.item_affirType)
            itemFromEmployee = itemView.findViewById(R.id.item_fromEmployee)
            itemFromStructure = itemView.findViewById(R.id.item_fromStructure)
            itemSubject = itemView.findViewById(R.id.item_subject)
            itemReciverType = itemView.findViewById(R.id.item_ReciverType)
            itemAffaiirNo = itemView.findViewById(R.id.item_AffairNO)
            itemStatus = itemView.findViewById(R.id.item_status)
            affairCard =itemView.findViewById(R.id.affircard2)
            goToDetails  = itemView.findViewById(R.id.card_view)

            confirmedBySec = itemView.findViewById(R.id.item_confirmedBysect)
            LinearItem2 = itemView.findViewById(R.id.linerItem23)




        }



    }



}