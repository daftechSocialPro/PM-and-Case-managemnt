package com.example.casemanagment.Model

import java.io.File
import java.io.Serializable

class TransferAffair:Serializable {

    var empId :String ?= null
    var affairHisId : String?= null
    var toEmployeeId : String ? = null
    var toStructureId : String ? = null
    var remark : String ? = null
    var caseTypeId : String ? = null;


    constructor(empId : String ? ,affairHisId : String ? , toEmployeeId : String ? ,toStructureId : String?, remark : String ? , caseTypeId : String ?){

        this.empId= empId;
        this.affairHisId = affairHisId;
        this.toEmployeeId = toEmployeeId;
        this.toStructureId=toStructureId;
        this.remark  = remark;
        this.caseTypeId = caseTypeId

    }

}

