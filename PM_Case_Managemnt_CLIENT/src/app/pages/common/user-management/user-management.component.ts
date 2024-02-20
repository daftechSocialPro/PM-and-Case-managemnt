import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonService } from 'src/app/common/common.service';
import { UserService } from '../../pages-login/user.service';
import { Employee } from '../organization/employee/employee';
import { AddUsersComponent } from './add-users/add-users.component';
import { ManageRolesComponent } from './manage-roles/manage-roles.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  employees: Employee[] = []
  constructor(private modalService: NgbModal, private userService: UserService, private commonService : CommonService) { }

  ngOnInit(): void {
 this.getUsers()
  }

getUsers(){
  this.userService.getSystemUsers().subscribe({
    next: (res) => {
      this.employees = res
    }, error: (err) => {
      console.error(err)
    }
  })
}
  addModal() {
    let modalRef = this.modalService.open(AddUsersComponent, { size: 'lg', backdrop: 'static' })
    modalRef.result.then((res) => {
      this.getUsers()
    })

  }

  manageRoles(userId: string){
    let modalRef= this.modalService.open(ManageRolesComponent,{size:'lg',backdrop:'static'})
    modalRef.componentInstance.userId = userId
    modalRef.result.then(()=>{this.getUsers()})
  }

  changePassword(userId: string){
    let modalRef= this.modalService.open(ChangePasswordComponent,{size:'lg',backdrop:'static'})
    modalRef.componentInstance.userId = userId
    modalRef.result.then(()=>{this.getUsers()})
  }

  getPath(value:string){
    return this.commonService.createImgPath(value)
  }

}
