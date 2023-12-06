package com.example.casemanagment.Remote

import com.example.casemanagment.Model.*
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableAll
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface IMyAPI {


  @POST("login")
  fun  Loginuser (@Body user: TblUser): Observable<TblUser>


  @POST("get-affairs")
  fun GetAffairs (@Body user:TblUser):Observable< List<Affairs>>

  @POST("get-affairWaiting")
  fun GetWaitingList (@Body user:TblUser):Observable< List<Affairs>>

  @POST("get-appointments")
  fun GetAppointments (@Body user:TblUser):Observable<List<Appointments>>

  @POST("get-affairHis")
  fun GetAffairHistories (@Body affairHistory: AffairHistory):Observable <List<AffairHistory>>

  @POST ("send-message")
  fun sendMessage (@Body message: Messagess):Observable<String>

  @POST("Add-to-WaitingList")
  fun AddToWaiting (@Body affairHistory: AffairHistory):Observable<String>

  @POST ("make-appointment")
  fun MakeAPpointment (@Body makeAppointment: makeAppointment):Observable<String>

 @POST ("complete-affair")
 fun completeAffair (@Body completedAffair: CompletedAffair): Observable<String>

 @POST("Revert-affair")
 fun revertAffair  (@Body completedAffair: CompletedAffair): Observable<String>


 @POST("get-results")
 fun getResults (@Body results: results ):Observable <results>

 @Multipart
 @POST("Transfer-affair")
 fun transferAffair(@Part photo: MultipartBody.Part?,
                    @Part("empIdd")  empIdd: RequestBody?,
                    @Part("affairHisIdd") affairHisIdd: RequestBody?,
                    @Part("toEmployeeId") toEmployeeId: RequestBody?,
                    @Part("toStructureId") toStructureId: RequestBody?,
                    @Part("remark") remark: RequestBody?,
                    @Part("caseTypeId") caseTypeId: RequestBody?):Observable<String>



 @POST("get-notification")

 fun getNotification (@Body notificationCount: TblUser):Observable<NotificationCount>

}


