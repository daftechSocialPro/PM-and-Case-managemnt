import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddProgramsComponent } from './add-programs/add-programs.component';
import { Program } from './Program';
import { ProgramService } from './programs.services';
import { ProgramDetailComponent } from './program-detail/program-detail.component';

@Component({
  selector: 'app-programs',
  templateUrl: './programs.component.html',
  styleUrls: ['./programs.component.css']
})
export class ProgramsComponent implements OnInit {


  Programs: Program[] = []
  constructor(
    private router : Router ,
    private modalService: NgbModal, 
    private programService: ProgramService) { }
  ngOnInit(): void {

    this.listPrograms()
  }

  listPrograms() {

    this.programService.getPrograms().subscribe({
      next: (res) => {
        this.Programs = res

        console.log('programs',res)
      },
      error: (err) => {
        console.error(err)
      }
    })
  }


  getProjects(programId: string ){

    this.router.navigate(['plan',{programId:programId}])

  }


  addProgram() {
    let modalRef = this.modalService.open(AddProgramsComponent, { size: 'xl', backdrop: 'static' })

    modalRef.result.then((res) => {
      this.listPrograms()
    })

  }
  programDetail(programId: string) {
    let modalRef = this.modalService.open(ProgramDetailComponent, { size: 'xl', backdrop: 'static' })
    modalRef.componentInstance.programId = programId
    modalRef.result.then((res) => {
      this.listPrograms()
    })

  }


}
