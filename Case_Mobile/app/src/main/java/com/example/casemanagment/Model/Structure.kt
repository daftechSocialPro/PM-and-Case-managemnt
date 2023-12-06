package com.example.casemanagment.Model

import java.io.Serializable

class Structure:Serializable {

    var structureName : String ? = null
    var strucutreId : String ? = null

    constructor( structureName : String? , structureId :String? ){

        this.structureName=structureName
        this.strucutreId = structureId
    }
}