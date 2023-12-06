package com.example.casemanagment.Model

import java.io.Serializable

class results:Serializable{

    var historyDetailId : String ? = null;
    var affairType : String ? = null
    var nextState: String ? = null
    var currentState : String ? = null
    var neededDocuments : String ? = null
    var employees : List<employee>? = arrayListOf()
    var structures : List<Structure> ? = arrayListOf()

constructor(historyDetailId : String? ){
    this.historyDetailId = historyDetailId
}
}