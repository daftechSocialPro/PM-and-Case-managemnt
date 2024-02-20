import { Component, OnInit } from '@angular/core';
import { OrganizationService } from '../organization/organization.service';
import { SmsTemplateGetDto } from './sms-template';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddSmsTemplateComponent } from './add-sms-template/add-sms-template.component';

@Component({
  selector: 'app-sms-template',
  templateUrl: './sms-template.component.html',
  styleUrls: ['./sms-template.component.css']
})
export class SmsTemplateComponent implements OnInit {

  smsTemplates!:SmsTemplateGetDto[]

  constructor(

    private orgService:OrganizationService,
    private modalService: NgbModal
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


}
