import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProgramService } from '../programs.services';
import { PlanService } from '../../plans/plan.service';
import { TaskService } from '../../tasks/task.service';
import { Program } from '../Program';
import { PlanSingleview, PlanView } from '../../plans/plans';
import { ActivityView } from '../../view-activties/activityview';
import { PMService } from '../../pm.services';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { UserView } from 'src/app/pages/pages-login/user';
import { TaskView } from '../../tasks/task';

@Component({
  selector: 'app-program-detail',
  templateUrl: './program-detail.component.html',
  styleUrls: ['./program-detail.component.css']
})
export class ProgramDetailComponent implements OnInit {

  @Input() programId!: string;
  Program!: Program
  Plans: PlanView[] = []
  user!: UserView
  plan!: PlanSingleview
  planTasks: Map<string, any[]> = new Map<string, any[]>();
  taskActivities: Map<String, any[]> = new Map<String, any[]>();

  constructor(
    private activeModal: NgbActiveModal,
    private programService: ProgramService,
    private planService: PlanService,
    private taskService: TaskService,
    private userService: UserService,
    

  ){}

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser()
    this.getProgram()
    this.getPlans()
  }
  



  getProgram() {

    this.programService.getProgramById(this.programId).subscribe({
      next: (res) => {
        this.Program = res

        console.log('programs',res)
      },
      error: (err) => {
        console.error(err)
      }
    })
  }

  getPlans(){
    this.planService.getPlans(this.programId).subscribe({
      next: (res) => {
        console.log("projects",res)
        this.Plans = res
        this.Plans.forEach((plan) => {
          this.ListTask(plan.Id);
        });
        console.log('this.planTasks: ', this.planTasks);
        
      },
      error: (err) => {
        console.error(err)
      }
    })
  }
  
  getSingleTaskActivities (taskId: String) {
    this.taskService.getSingleTask(taskId).subscribe({
      next: (res) => {
        if (res.ActivityViewDtos !== undefined) {
          const result = res.ActivityViewDtos;
          this.taskActivities.set(taskId, result);
        }
        
        
      }, error: (err) => {
        console.error(err)
      }
    })

  }

  ListTask(planId: string) {

    this.planService.getSinglePlans(planId).subscribe({
      next: (res) => {
        this.plan = res
        const result = res.Tasks
        result.forEach((task) => {
          if (task.Id !== undefined) {
            this.getSingleTaskActivities(task.Id)
          }
          
        });
        
        this.planTasks.set(planId, result);
        console.log('this.taskActivities: ', this.taskActivities);
     
      }
    })
  }
  closeModal() {
    this.activeModal.close();
  }
}
