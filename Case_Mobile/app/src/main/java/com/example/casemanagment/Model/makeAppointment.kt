package com.example.casemanagment.Model

import java.io.Serializable

class makeAppointment:Serializable {

    var executionDate : String ? = null ;
    var executionTime: String  ? = null;
    var employeeId : String? = null ;
    var affairId : String ? = null ;


    constructor( executionDate: String ? , executionTime: String? , employeeId : String? ,affairId : String ? ){

        this.executionDate = executionDate;
        this.executionTime = executionTime ;
        this.employeeId = employeeId;
        this.affairId = affairId;
    }


}