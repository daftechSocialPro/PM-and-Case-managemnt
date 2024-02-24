import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CaseService } from '../case.service';
import { ICaseView } from '../encode-case/Icase';
import { DetailReportComponent } from '../case-report/case-detail-report/detail-report/detail-report.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-search-cases',
  templateUrl: './search-cases.component.html',
  styleUrls: ['./search-cases.component.css']
})
export class SearchCasesComponent implements OnInit {

  searchForm !: FormGroup
  myacaselist!: ICaseView[]
  constructor(
    private modalService : NgbModal,
    private caseService : CaseService,
    private formBuilder: FormBuilder){}
  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({

      key : ['']

    })
    
  }

  getSearchCases(){

    this.caseService.getSearchCases(this.searchForm.value.key).subscribe({
      next:(res)=>{

      this.myacaselist = res 

      },error:(err)=>{

      }
    })

  }

  Search( ){

    this.getSearchCases()
  }

  detail(caseId : string) {
    let modalRef = this.modalService.open(DetailReportComponent, { size: "xl", backdrop: "static" })
    modalRef.componentInstance.CaseId = caseId
  }
}
