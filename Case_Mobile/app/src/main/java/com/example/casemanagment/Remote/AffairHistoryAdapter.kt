package com.example.casemanagment.Remote

import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.*
import com.example.casemanagment.Model.AffairHistory
import com.example.casemanagment.Model.CompletedAffair
import com.example.casemanagment.Model.TblUser
import com.google.android.material.textfield.TextInputLayout
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AffairHistoryAdapter: RecyclerView.Adapter<AffairHistoryAdapter.ViewHolder>(   ){

    lateinit var  affairHistory : List<AffairHistory>
    lateinit var items : Array<String>
  lateinit var  adapterItems: ArrayAdapter<String>





    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.affair_history_card,
                viewGroup,
                false
        )
        return  ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.itemTitle.text = affairHistory[i].title
        holder.itemDate.text = affairHistory[i].datetime
        holder.itemFromStructure.text ="From : "+ affairHistory[i].fromEmployee +" ("+affairHistory[i].fromStructure+")"
        holder.itemToStructure.text ="To : " + affairHistory[i].toEmployee +" ("+affairHistory[i].toStructure+")"

       holder.itemMessageStatus.text = affairHistory[i].messageStatus
        holder.itemAffairStatus.text=affairHistory[i].status

           holder.itemAutoComplteTxt.visibility= View.INVISIBLE
        holder.itemActions.visibility =View.INVISIBLE

        if (affairHistory[i].employeeId ==affairHistory[i].toEmployeeId){

           if (affairHistory[i].status=="Pend"|| affairHistory[i].status=="Seen"|| affairHistory[i].status=="Waiting" || affairHistory[i].status=="Not Seen" || affairHistory[i].status==null) {
               if (affairHistory[i].affairType!="Cc") {
                   holder.itemAutoComplteTxt.visibility = View.VISIBLE
                   holder.itemActions.visibility = View.VISIBLE
               }
            }
        }


        if (affairHistory[i].status=="Seen"){

            holder.itemLinearLayout.setBackgroundColor(Color.parseColor("#709D99"))
        }
        else if (affairHistory[i].status=="Transfered"){

            holder.itemLinearLayout.setBackgroundColor(Color.parseColor("#4FC6E1"))
        }
        else if (affairHistory[i].status=="Reverted"){


            holder.itemLinearLayout.setBackgroundColor(Color.parseColor("#D56576"))
        }
        else {

            holder.itemLinearLayout.setBackgroundColor(Color.parseColor("#86C9D4"))

        }







        holder.itemAutoComplteTxt.setAdapter(adapterItems)
        holder.itemAutoComplteTxt.setOnItemClickListener{ parent, view, position, id ->



            val item:String = parent.getItemAtPosition(position).toString();
            val id = parent.getItemIdAtPosition(position).toString();
           // Toast.makeText(view.context, id, Toast.LENGTH_SHORT).show()
            val manager: FragmentManager = (view.context as AppCompatActivity).supportFragmentManager

            val sharedPref: SharedPreferences = view.context.getSharedPreferences("Setting", MODE_PRIVATE)

            val ipAdd= sharedPref.getString("IpAddress", null)
            val pub = sharedPref.getString("PubName", null)
            val port = sharedPref.getString("Port", null)
            val empId = sharedPref.getString("empId",null)

            val compositeDisposable = CompositeDisposable()
            var iMyAPI: IMyAPI = RetrofitClient.getInstance(ipAdd, pub,port).create(IMyAPI::class.java)

            when {
                id=="0"->{

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle("Are You sure!")
                    builder.setMessage("Do you want to Add to Waiting List?")
                    builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, g: Int ->


                            val affairHis = AffairHistory(
                                affairHistory[i].affairId,
                                empId,
                                affairHistory[i].affairHisId
                            )

                            val dialog = SpotsDialog.Builder().setContext(view.context).build()
                        dialog.show()
                            compositeDisposable.addAll(
                                iMyAPI.AddToWaiting(affairHis)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        { s ->
                                            Toast.makeText(view.context, s, Toast.LENGTH_SHORT)
                                                .show()
                                            dialog.dismiss()

                                            val intent =
                                                Intent(view.context, HomeScreen::class.java)

                                            val user = TblUser(empId)
                                            intent.putExtra("userViewModel", user);
                                            view.context.startActivity(intent)


                                        },
                                        { t: Throwable? ->
                                            Toast.makeText(
                                                view.context,
                                                t!!.message,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            dialog.dismiss()

                                        }


                                    ))

                    }
                    builder.setNegativeButton("No",{dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                         }
                id=="1"->{

                    val transferFragment = TransferFragment(affairHistory[i].affairHisId)
                    transferFragment.show(manager,"BottomSheetDialog")

                   // Toast.makeText(view.context,"trasfer",Toast.LENGTH_SHORT).show()
                    }
                id == "2" ->{
                    val completeFragment= CompleteFragment(affairHistory[i].affairHisId)
                    completeFragment.show(manager,"BottomSheetDialog")
                }
                id=="3"->{

                        val makeAppointment= MakeAppointmentFragment(affairHistory[i].affairId)
                    makeAppointment.show(manager,"BottomSheetDialog")
                }
                id=="4"->{

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle("Are You sure!")
                    builder.setMessage("Do you want to Revert?")
                    builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, k: Int ->

                        val dialog = SpotsDialog.Builder().setContext(view.context).build()
                        dialog.show()
                        val completeaffair = CompletedAffair(empId,affairHistory[i].affairHisId,"")
                        compositeDisposable.addAll(
                            iMyAPI.revertAffair(completeaffair)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    { s ->
                                        Toast.makeText(view.context, s, Toast.LENGTH_SHORT)
                                            .show()
                                        dialog.dismiss()

                                        val intent =
                                            Intent(view.context, HomeScreen::class.java)

                                        val user = TblUser(empId)
                                        intent.putExtra("userViewModel", user);
                                        view.context.startActivity(intent)


                                    },
                                    { t: Throwable? ->
                                        Toast.makeText(
                                            view.context,
                                            t!!.message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        dialog.dismiss()

                                    }


                                ))



                    }
                    builder.setNegativeButton("No",{dialogInterface: DialogInterface, i: Int -> })
                    builder.show()


                }
                id=="5"-> {

                    var sendSMSFragment = SendSMSFragment(affairHistory[i].affairId);
                    sendSMSFragment.show(manager, "BottomSheetDialog")

                }

            }






        }
    }

    override fun getItemCount(): Int {
        return affairHistory.size
    }




    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        var itemTitle : TextView
        var itemDate : TextView
        var itemFromStructure :TextView
        var itemToStructure : TextView
        var itemMessageStatus :TextView
        var itemAffairStatus : TextView
       // var itemAffairhisDetailsBtn : Button
        var itemAutoComplteTxt :AutoCompleteTextView
        var itemActions : TextInputLayout
        var itemLinearLayout : LinearLayout


        init {
            itemTitle = itemView.findViewById(R.id.item_affirHisTitle)
            itemDate = itemView.findViewById(R.id.item_affairHisDate)
            itemFromStructure = itemView.findViewById(R.id.item_affairFromStructure)
            itemToStructure = itemView.findViewById(R.id.item_affairToStructure)
            itemMessageStatus = itemView.findViewById(R.id.item_affairHisMessageStatus)
            itemAffairStatus = itemView.findViewById(R.id.item_affairHisStatus)
           // itemAffairhisDetailsBtn = itemView.findViewById(R.id.affariHis_detail_btn)
            itemAutoComplteTxt = itemView.findViewById(R.id.auto_complete_txt)
            itemActions = itemView.findViewById(R.id.item_actions)
            itemLinearLayout = itemView.findViewById(R.id.lin_st)

        }



    }



}