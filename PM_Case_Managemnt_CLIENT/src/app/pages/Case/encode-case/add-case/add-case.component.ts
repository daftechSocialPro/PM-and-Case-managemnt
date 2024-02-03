import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IndividualConfig } from 'ngx-toastr';
import { CommonService, toastPayload } from 'src/app/common/common.service';
import { SelectList } from 'src/app/pages/common/common';
import { UserView } from 'src/app/pages/pages-login/user';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { CaseService } from '../../case.service';
import { AddApplicantComponent } from '../add-applicant/add-applicant.component';
import { ConfirmationDialogComponent } from 'src/app/components/confirmation-dialog/confirmation-dialog.component';
import { NotificationService } from 'src/app/layouts/header/notification.service';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';




@Component({
  selector: 'app-add-case',
  templateUrl: './add-case.component.html',
  styleUrls: ['./add-case.component.css'],
})
export class AddCaseComponent implements OnInit {

  
  
  routeItems: MenuItem[] = [];
  


  constructor(
    private activeModal: NgbActiveModal,
    private router: Router

  ) {
    
  }
  ngOnInit(): void {

    this.router.navigate(['casedetails'])
    
    this.routeItems = [
      { label: 'Case Details', routerLink: '/casedetails' },
      { label: 'Case Files', routerLink: '/casefiles' },
      // { label: 'Confirmation', routerLink: 'raiseissue' },
      
  ];
  }


  closeModal() {
    this.activeModal.close();
  }
 }
