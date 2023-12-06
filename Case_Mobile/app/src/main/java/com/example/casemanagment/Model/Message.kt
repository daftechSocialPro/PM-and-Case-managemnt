package com.example.casemanagment.Model

import java.io.Serializable

class Messagess:Serializable {

var message : String ? = null
var affairId : String ? = null
var employeeId : String ? = null

constructor(message:String?, affairId : String? , employeeId : String? ){
    this.message = message
    this.affairId= affairId
    this.employeeId = employeeId
}


}