package com.example.casemanagment.Model

import java.io.Serializable

class employee:Serializable {
    var empName : String? = null
    var empId : String ? = null

    constructor(empName: String? , empId : String?){

        this.empName= empName
        this.empId = empId
    }
}