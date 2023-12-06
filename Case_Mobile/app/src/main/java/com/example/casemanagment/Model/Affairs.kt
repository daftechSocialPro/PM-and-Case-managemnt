package com.example.casemanagment.Model

import java.io.Serializable

class Affairs :Serializable {

    var historyId:String?=null;
    var applicant:String?= null;
    var affairId :String?=null;
    var affairNumber :String ?= null;
    var affairType :String ? = null ;
    var subject :String ? =null
    var fromEmplyee: String ? = null
    var fromStructure: String ? = null
    var remark :String? =null
    var reciverType:String ? = null
    var affairHistoryStatus:String ? = null
    var createdAt:String ? = null
    var document:Array<String>?= null

    var confirmedSecratary : String ? = null


    constructor()






}