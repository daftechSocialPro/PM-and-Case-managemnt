import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService, toastPayload } from 'src/app/common/common.service';
import { SelectList } from 'src/app/pages/common/common';
import { CaseService } from '../../../case.service';
import { IndividualConfig } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';
import * as signalR from '@microsoft/signalr';
import { UserService } from 'src/app/pages/pages-login/user.service';
import { UserView } from 'src/app/pages/pages-login/user';

@Component({
  selector: 'app-case-files',
  templateUrl: './case-files.component.html',
  styleUrls: ['./case-files.component.css']
})
export class CaseFilesComponent implements OnInit {

  qrData!:string
  fileSettings!: SelectList[];
  Documents: any;
  settingsFile: fileSettingSender[] = [];
  toast!: toastPayload;
  case!:any
  user!: UserView
  mobileUplodedFiles:any[] = []
  public connection!: signalR.HubConnection;
  urlHub : string = environment.assetUrl+"/ws/Encoder"


  constructor(

    private activeModal: NgbActiveModal,
    private router: Router,
    private caseService: CaseService,
    private commonService: CommonService,
    private userService: UserService
  ){
    this.case = this.router.getCurrentNavigation()?.extras.state?.['response'];
  }

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser()
    console.log('this.caseId: ', this.case);
    this.connection = new signalR.HubConnectionBuilder()
    .withUrl(this.urlHub, {
      skipNegotiation: true,
      transport: signalR.HttpTransportType.WebSockets
    })
    .configureLogging(signalR.LogLevel.Debug)
    .build();


  this.connection.start()
    .then((res) => {
      console.log("employeeId",this.user.EmployeeId)
     this.connection.invoke('addDirectorToGroup', this.user.EmployeeId);
      console.log('Connection started.......!');
    })
    .catch((err) => console.log('Error while connecting to the server', err));
    if(this.connection){
      this.connection.on('getUplodedFiles', (result) => {
        this.mobileUplodedFiles = result
        console.log("UPLODED FILES",this.mobileUplodedFiles)
       });
    }
    
    this.qrData = `${this.case.CaseId}_${this.user.EmployeeId}_CASE`
  }

  getFileSettings(casetTypeId: string) {
    this.caseService.getFileSettignsByCaseTypeId(casetTypeId).subscribe({
      next: (res) => {
        this.fileSettings = res;
        console.log(res)
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  submit(){

    const formData = new FormData();
    if (this.Documents) {
      for (let file of this.Documents) {
        formData.append('attachments', file);
      }
    }

    for (let file of this.settingsFile) {
      formData.append(
        'fileSettings',
        file.File,
        `${file.FileSettingId}.${file.File.name.split('.').reverse()[0]}`
      );
    }
    // formData.set('ApplicantId', this.case.CaseData.ApplicantId);
    formData.set('CaseId', this.case.CaseId);
    // formData.set('CreatedBy', this.case.CaseData.CreatedBy);

    this.caseService.addCaseFiles(formData).subscribe({
      next: (res) => {
        this.toast = {
          message: ' Case Files Successfully Uploded',
          title: 'Successfully Uploded.',
          type: 'success',
          ic: {
            timeOut: 2500,
            closeButton: true,
          } as IndividualConfig,
        };
        this.commonService.showToast(this.toast);
        
        this.activeModal.close();
      },
      error: (err) => {
        this.toast = {
          message: err.message,
          title: 'Something went wrong.',
          type: 'error',
          ic: {
            timeOut: 2500,
            closeButton: true,
          } as IndividualConfig,
        };
        this.commonService.showToast(this.toast);
        console.log(err);
      },
    });




  }

  onImagesScannedUpdate(images: any) {
    
    
    const fileArray = [];
    for (let i = 0; i < images.length; i++) {

      let Filee = this.getFile(images[i])
      fileArray.push(Filee);

    }
 
    this.Documents = this.createFileList(fileArray);
  }

  onFileSelected(event: any) {
    this.Documents = event.target.files;
    console.log(this.Documents)

  }
  createFileList(files: File[]): FileList {
    const dataTransfer = new DataTransfer();
    for (let i = 0; i < files.length; i++) {
      dataTransfer.items.add(files[i]);
    }
    return dataTransfer.files;
  }
  onFileSettongSelected(filesettingId: string, event: any) {
    var settingFile: fileSettingSender = {
      FileSettingId: filesettingId,
      File: event.target.files[0],
    };

    if (
      this.settingsFile.filter((x) => x.FileSettingId === filesettingId)
        .length > 0
    ) {
      const indexfile = this.settingsFile.findIndex(
        (f) => f.FileSettingId === filesettingId
      );

      this.settingsFile.splice(indexfile, 1);
      this.settingsFile.push(settingFile);
    } else {
      this.settingsFile.push(settingFile);
    }
  }

  getFile(imageData: any) {

    const byteString = atob(imageData.src.split(',')[1]);
    const mimeString = imageData.mimeType;
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const uint8Array = new Uint8Array(arrayBuffer);

    for (let i = 0; i < byteString.length; i++) {
      uint8Array[i] = byteString.charCodeAt(i);
    }

    const blob = new Blob([uint8Array], { type: mimeString });
    const fileName = this.getFileName() + ".jpg"
    const file = new File([blob], fileName, { type: mimeString });
    return file
  }

  getFileName() {
    const length: number = 10;
    let result: string = '';
    const characters: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

    for (let i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * characters.length));
    }

    return result;
  }
  closeModal() {
    this.activeModal.close();
  }
  viewFile(file: string) {
    return this.commonService.createImgPath(file)
  }
}

export interface fileSettingSender {
  FileSettingId: string;
  File: File;
}
