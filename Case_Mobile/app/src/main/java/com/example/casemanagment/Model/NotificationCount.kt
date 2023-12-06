package com.example.casemanagment.Model

import java.io.Serializable

class NotificationCount:Serializable {

    var affairCount : Int ? = 0;
    var appointmentCount : Int ? = 0 ;
    var waitingListCount : Int ? = 0 ;
    var employeeId : String ? = null

    constructor(employeeId : String ?){

        this.employeeId= employeeId
    }


}