import { Component, Input, OnInit } from '@angular/core';
import { ActivityEmployees, ActivityView } from '../../../view-activties/activityview';
import { SelectList } from 'src/app/pages/common/common';
import { PMService } from '../../../pm.services';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/api';
import { TimeScale } from 'chart.js';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { UserView } from 'src/app/pages/pages-login/user';

@Component({
  selector: 'app-assign-employees-activity',
  templateUrl: './assign-employees-activity.component.html',
  styleUrls: ['./assign-employees-activity.component.css']
})
export class AssignEmployeesActivityComponent implements OnInit {
@Input() activity!: ActivityView 

employees : SelectList[]=[]
selectedEmployee:string[]=[]
userView !: UserView

constructor(
  
  
  private activeModal:NgbActiveModal,
  private userService : UserService,
  private messageService : MessageService,
  private pmService : PMService){}
  ngOnInit(): void {
    this.userView = this.userService.getCurrentUser()
    this.getEmployees()
    console.log('activity',this.activity)

    if (this.activity.Members){
      this.selectedEmployee = this.activity.Members.map((item)=>item.EmployeeId?.toLowerCase()!)
    }
  }

  getEmployees (){
    this.pmService.getEmployeesFromBranch (this.activity.BranchId!).subscribe({
      next:(res)=>{
        this.employees = res 

        console.log(this.employees,"employees")
      }
    })
    
  }

  submit (){

    var assignedEmployee : ActivityEmployees={

      ActivityId :this.activity.Id,
      CreatedBy:this.userView.UserID,
      Employees : this.selectedEmployee
    }


    this.pmService.AssignEmployee(assignedEmployee).subscribe({
      next:(res)=>{
        if (res.Success){

          this.messageService.add({severity:'success',summary:'Successfully Added!',detail:res.Message})

        }else {
          this.messageService.add({severity:'error',summary:'Something went wrong!',detail: res.Message})
        }
      }
      
    })


  }


  closeModal(){
    this.activeModal.close()

  }

}
