package com.example.casemanagment.Model

import java.io.Serializable

class AffairHistory:Serializable {


    var employeeId :String? = null;
    var affairType : String ? = null ;
    var toEmployeeId : String ? =null;
    var affairId :String? = null ;
    var datetime :String? = null ;
    var title :String? = null ;
    var fromStructure :String? = null ;
    var toStructure:String? = null ;
    var fromEmployee :String? = null ;
    var  toEmployee :String? = null ;
    var status :String? = null ;
    var messageStatus:String? = null ;
    var affairHisId: String ? = null ;

    constructor()
    constructor(affairId:String?,employeeId:String?){
        this.affairId= affairId
        this.employeeId = employeeId
    }

    constructor(affairId: String? , employeeId: String?, affairHisId : String? ){

        this.affairId = affairId;
        this.employeeId = employeeId;
        this.affairHisId  = affairHisId;
    }

}