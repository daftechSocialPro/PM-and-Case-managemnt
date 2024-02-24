import { Component, OnInit } from '@angular/core';
import { OrganizationService } from '../organization/organization.service';
import { SmsTemplateGetDto } from './sms-template';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddSmsTemplateComponent } from './add-sms-template/add-sms-template.component';
import { ConfirmationDialogService } from 'src/app/components/confirmation-dialog/confirmation-dialog.service';
import { Router } from '@angular/router';
import { IndividualConfig } from 'ngx-toastr';
import { CommonService, toastPayload } from 'src/app/common/common.service';

@Component({
  selector: 'app-sms-template',
  templateUrl: './sms-template.component.html',
  styleUrls: ['./sms-template.component.css']
})
export class SmsTemplateComponent implements OnInit {

  smsTemplates!:SmsTemplateGetDto[]
  toast !: toastPayload;

  constructor(

    private orgService:OrganizationService,
    private modalService: NgbModal,
    private confirmationDialogService: ConfirmationDialogService,
    private route: Router,
    private commonService: CommonService,
  ){}

  ngOnInit(): void {
      this.getSmsTemplates()

  }

  getSmsTemplates(){
    this.orgService.getSmsTemplate().subscribe({
      next : (res) => {
        this.smsTemplates = res
      }
    })
  }

  addSmsTemplate() {
    let modalRef =  this.modalService.open(AddSmsTemplateComponent, { size: 'lg', backdrop: 'static' })
     modalRef.result.then((res)=>{
       this.getSmsTemplates()
     })
  }

  updateSmsTemplate(template:SmsTemplateGetDto){
    let modalref = this.modalService.open(AddSmsTemplateComponent, { size: 'lg', backdrop: 'static' })
    modalref.componentInstance.template = template
    
    modalref.result.then((res)=>{
      this.getSmsTemplates();
    })
  }

  deleteSmsTemplate(Id: string) {
    this.confirmationDialogService
      .confirm('Please confirm..', 'Do you want to Delete this SMS Template?')
      .then((confirmed) => {
        if (confirmed) {
          this.orgService
            .deleteSmsTemplate(Id)
            .subscribe({
              next: (res) => {
                if (res.Success) {
                  this.toast = {
                    message: res.Message,
                    title: 'Successfull.',
                    type: 'success',
                    ic: {
                      timeOut: 2500,
                      closeButton: true,
                    } as IndividualConfig,
                  };

                  this.commonService.showToast(this.toast);
                  this.getSmsTemplates();
                }
                else{
                  this.toast = {
                    message: res.Message,
                    title: 'Something Went Wrong',
                    type: 'error',
                    ic: {
                      timeOut: 2500,
                      closeButton: true,
                    } as IndividualConfig,
                  };
                  this.commonService.showToast(this.toast);
                }

                
              },
              error: (err) => {
                this.toast = {
                  message: 'Something went wrong!!',
                  title: 'Network error.',
                  type: 'error',
                  ic: {
                    timeOut: 2500,
                    closeButton: true,
                  } as IndividualConfig,
                };
                this.commonService.showToast(this.toast);
              },
            });
        }
      })
      .catch(() =>
        console.log(
          'User dismissed the dialog (e.g., by using ESC, clicking the cross icon, or clicking outside the dialog)'
        )
      );
  }

}
