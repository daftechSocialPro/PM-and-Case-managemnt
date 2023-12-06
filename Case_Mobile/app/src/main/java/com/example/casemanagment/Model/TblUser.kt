package com.example.casemanagment.Model

import java.io.Serializable

class TblUser :Serializable {

    var userName : String ?= null;
    var password: String ? = null;
    var employeeID :String ? = null;
    var structureName : String ?= null;
    var fullName : String ?= null;
    var imagePath: String ?= null;
    var userRole : String ?= null;

    constructor()
    constructor(employeeId: String?){
        this.employeeID= employeeId
    }
    constructor(UserName:String ?, Password:String?){

        this.userName= UserName;
        this.password= Password;

    }
    constructor(UserName: String?,Password: String?,StructureName:String?,fullName:String?,imagePath:String?,userRole:String?,employeeId:String?){

        this.userName=UserName;
        this.password=Password;
        this.structureName= StructureName;
        this.fullName= fullName;
        this.imagePath= imagePath;
        this.userRole= userRole;
        this.employeeID = employeeId;
    }
}