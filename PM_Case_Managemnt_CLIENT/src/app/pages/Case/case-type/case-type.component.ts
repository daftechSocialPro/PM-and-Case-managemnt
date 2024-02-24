import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { CaseService } from '../case.service';
import { AddCaseChildComponent } from './add-case-child/add-case-child.component';
import { AddCaseTypeComponent } from './add-case-type/add-case-type.component';
import { CaseTypeView } from './casetype';
import { ConfirmEventType, ConfirmationService, MessageService } from 'primeng/api';
@Component({
  selector: 'app-case-type',
  templateUrl: './case-type.component.html',
  styleUrls: ['./case-type.component.css']
})
export class CaseTypeComponent implements OnInit {

  caseTypes!: CaseTypeView[]

  constructor(private modalService: NgbModal, 
    private caseService: CaseService,
    private messageService:MessageService,
    private confirmationService:ConfirmationService) { }

  ngOnInit(): void {
    this.getCaseTypes()
  }
  getCaseTypes() {
    this.caseService.getCaseType().subscribe({
      next: (res) => {
        this.caseTypes = res
        console.log('res', res)

      }, error: (err) => {

        console.log(err)
      }
    })

  }
  addCaseType() {
    let modalRef = this.modalService.open(AddCaseTypeComponent, { size: 'lg', backdrop: 'static' })

    modalRef.result.then(() => {
      this.getCaseTypes()
    })

  }

  AddChild(CaseType:CaseTypeView){

    let modalRef = this.modalService.open(AddCaseChildComponent,{size:'lg',backdrop:'static'})
    modalRef.componentInstance.CaseType = CaseType

    modalRef.result.then(()=>{
      this.getCaseTypes()
    })
  }

  UpdateCaseType (caseType:CaseTypeView){

    let modalRef = this.modalService.open(AddCaseTypeComponent,{size:'lg',backdrop:'static'})
    modalRef.componentInstance.caseType = caseType

    modalRef.result.then(()=>{
      this.getCaseTypes()
    })
  }






  UpdateCaseChild(child:CaseTypeView,CaseType:CaseTypeView) {

    console.log(child,CaseType)

    let modalRef = this.modalService.open(AddCaseChildComponent,{size:'lg',backdrop:'static'})
    modalRef.componentInstance.caseChild = child
    modalRef.componentInstance.CaseType =CaseType
    

    modalRef.result.then(()=>{
      this.getCaseTypes()
    })

  }




  
  DeleteCaseType(caseTypeId : string) {

    this.confirmationService.confirm({
      message: 'Are You sure you want to delete this CaseType?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.caseService.DeleteCaseType(caseTypeId).subscribe({
          next: (res) => {

              this.messageService.add({ severity: 'success', summary: 'Confirmed', detail: 'Case Type successfully Deleted' });
              this.getCaseTypes()
           
          }, error: (err) => {

            this.messageService.add({ severity: 'error', summary: 'Rejected', detail: err });


          }
        })

      },
      reject: (type: ConfirmEventType) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({ severity: 'warn', summary: 'Cancelled', detail: 'You have cancelled' });
            break;
        }
      },
      key: 'positionDialog'
    });
  } 

}
