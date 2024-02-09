import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
