package com.example.casemanagment.Model

import java.io.Serializable

class CompletedAffair:Serializable {


    var employeeId : String ? = null ;
    var affairHisId : String ? =  null ;
    var Remark : String ? = null ;



    constructor(employeeId: String ? , affairHisId: String ? , Remark : String ? ){

        this.employeeId = employeeId ;
        this. affairHisId = affairHisId ;
        this.Remark = Remark;
    }
}