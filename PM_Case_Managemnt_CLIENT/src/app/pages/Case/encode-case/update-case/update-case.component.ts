import { Component, Input, OnInit } from '@angular/core';
import { ICaseView } from '../Icase';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonService, toastPayload } from 'src/app/common/common.service';
import { SelectList } from 'src/app/pages/common/common';
import { UserView } from 'src/app/pages/pages-login/user';

import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { CaseService } from '../../case.service';
import { IndividualConfig } from 'ngx-toastr';
import { AddApplicantComponent } from '../add-applicant/add-applicant.component';
import { fileSettingSender } from '../add-case/case-details/case-details.component';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-update-case',
  templateUrl: './update-case.component.html',
  styleUrls: ['./update-case.component.css']
})
export class UpdateCaseComponent implements OnInit {
  @Input() caseId!: string
  routeItems: MenuItem[] = [];
  


  constructor(
    private activeModal: NgbActiveModal,
    private router: Router

  ) {
    
  }
  ngOnInit(): void {

    this.router.navigate(['updatecasedetails',{caseId : this.caseId}])
    
    this.routeItems = [
      { label: 'Update Case Details', routerLink: '/updatecasedetails' },
      { label: 'Update Case Files', routerLink: '/updatecasefiles' },
      // { label: 'Confirmation', routerLink: 'raiseissue' },
      
  ];
  }


  closeModal() {
    this.activeModal.close();
    this.router.navigateByUrl('/encodecase', { skipLocationChange: true }).then(() => {
      this.router.navigate(['encodecase']);
  });
    //window.location.reload();
    
  }
  

  
  

}
