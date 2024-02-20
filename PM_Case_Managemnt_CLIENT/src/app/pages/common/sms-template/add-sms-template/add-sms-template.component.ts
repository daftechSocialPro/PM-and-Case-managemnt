import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonService, toastPayload } from 'src/app/common/common.service';
import { OrganizationService } from '../../organization/organization.service';
import { IndividualConfig } from 'ngx-toastr';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { UserView } from 'src/app/pages/pages-login/user';
import { SmsTemplateGetDto, SmsTemplatePostDto } from '../sms-template';

@Component({
  selector: 'app-add-sms-template',
  templateUrl: './add-sms-template.component.html',
  styleUrls: ['./add-sms-template.component.css']
})
export class AddSmsTemplateComponent implements OnInit {

  @Input() template!: SmsTemplateGetDto 
  toast !: toastPayload;
  templateForm!: FormGroup
  user!: UserView
constructor(
  private formBuilder: FormBuilder, 
  private orgService: OrganizationService, 
  private commonService: CommonService, 
  private activeModal: NgbActiveModal,
  private userService:UserService
){}

ngOnInit(): void {
  this.user = this.userService.getCurrentUser()
  this.templateForm = this.formBuilder.group({
    Title: ['', Validators.required],
    Description: ['', Validators.required],
    Remark: ['']
  });

  if(this.template){
    this.getSingleTemplate()
  }
}


getSingleTemplate(){
  this.orgService.getSmsTemplateById(this.template.Id).subscribe({
    next : (res) => {
      this.templateForm.controls["Title"]
    }
  })
}

submit() {

  if (this.templateForm.valid) {

    const template : SmsTemplatePostDto ={
      Title : this.templateForm.value.Title,
      Description : this.templateForm.value.Description,
      Remark : this.templateForm.value.Remark,
      CreatedBy : this.user.UserID
    }
    this.orgService.createSmsTemplate(template).subscribe({

      next: (res) => {

        if (res.success) {

          this.toast = {
            message: res.message,
            title: 'Successfully Created.',
            type: 'success',
            ic: {
              timeOut: 2500,
              closeButton: true,
            } as IndividualConfig,
          };
  
          this.commonService.showToast(this.toast);
          this.closeModal();
          this.templateForm.reset()

        }
        else{
          this.toast = {
            message: res.message,
            title: 'Something went Wrong',
            type: 'error',
            ic: {
              timeOut: 2500,
              closeButton: true,
            } as IndividualConfig,
          };
          this.commonService.showToast(this.toast);
        }

      }, error: (err) => {
        this.toast = {
          message: err,
          title: 'Network error.',
          type: 'error',
          ic: {
            timeOut: 2500,
            closeButton: true,
          } as IndividualConfig,
        };
        this.commonService.showToast(this.toast);


      }
    }
    );
  }
  else{
    this.toast = {
      message: "Please fill required inputs!!'",
      title: 'Form Submit failed.',
      type: 'error',
      ic: {
        timeOut: 2500,
        closeButton: true,
      } as IndividualConfig,
    };
    this.commonService.showToast(this.toast);
  }


}


  closeModal() {

    this.activeModal.close()
  }
}
