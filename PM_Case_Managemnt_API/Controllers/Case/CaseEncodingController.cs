﻿using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using PM_Case_Managemnt_API.DTOS.CaseDto;
using PM_Case_Managemnt_API.Hubs.EncoderHub;
using PM_Case_Managemnt_API.Models.Auth;
using PM_Case_Managemnt_API.Models.CaseModel;
using PM_Case_Managemnt_API.Models.Common;
using PM_Case_Managemnt_API.Services.CaseMGMT.Applicants;
using PM_Case_Managemnt_API.Services.CaseMGMT.CaseAttachments;
using PM_Case_Managemnt_API.Services.CaseMGMT.FileInformationService;
using PM_Case_Managemnt_API.Services.CaseService.Encode;
using System.Net.Http.Headers;

namespace PM_Case_Managemnt_API.Controllers.Case
{
    [Route("api/case")]
    [ApiController]
    public class CaseEncodingController : ControllerBase
    {
        private readonly ICaseEncodeService _caseEncodeService;
        private readonly ICaseAttachementService _caseAttachmentService;
        private readonly IFilesInformationService _filesInformationService;
        private readonly IApplicantService _applicantService;
        private IHubContext<EncoderHub, IEncoderHubInterface> _encoderHub;
        private UserManager<ApplicationUser> _userManager;
        public CaseEncodingController(
            ICaseEncodeService caseEncodeService,
            ICaseAttachementService caseAttachementService, 
            IFilesInformationService filesInformationService,
            IApplicantService applicantService,
            IHubContext<EncoderHub, IEncoderHubInterface> encoderHub,
            UserManager<ApplicationUser> userManager )
        {
            _caseEncodeService = caseEncodeService;
            _caseAttachmentService = caseAttachementService;
            _filesInformationService = filesInformationService;
            _applicantService = applicantService;
            _encoderHub = encoderHub;
            _userManager = userManager;
        }


        [HttpPost("encoding"), DisableRequestSizeLimit]
        public async Task<IActionResult> Create()
        {
            try
            {
                CaseEncodePostDto caseEncodePostDto = new CaseEncodePostDto()
                {
                    CaseNumber = Request.Form["CaseNumber"],
                    LetterNumber = Request.Form["LetterNumber"],
                    LetterSubject = Request.Form["LetterSubject"],
                    CaseTypeId = Guid.Parse(Request.Form["CaseTypeId"]),
                    ApplicantId = Guid.Parse(Request.Form["ApplicantId"]),
                    EmployeeId = Guid.Parse(Request.Form["ApplicantId"]),
                    PhoneNumber2 = Request.Form["PhoneNumber2"],
                    Representative = Request.Form["Representative"],
                    CreatedBy = Guid.Parse(Request.Form["CreatedBy"]),
                };
                string caseId = await _caseEncodeService.Add(caseEncodePostDto);
                var result = new { CaseId = caseId, CaseData = caseEncodePostDto };

                
                return Ok(result);

                
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }


        [HttpPost("fileEncoding"), DisableRequestSizeLimit]
        public async Task<IActionResult> Upload()
        {
            try
            {

                var ids = Request.Form["CaseId"].ToString().Split('_');


                    var caseId = ids[0];
                    var Case = await _caseEncodeService.GetSingleCase(Guid.Parse(caseId));
                    
                    
                    var employeeId = ids[1];
                    

                    if (Request.Form.Files.Any())
                    {
                        List<CaseAttachment> attachments = new List<CaseAttachment>();
                        List<FilesInformation> fileInfos = new List<FilesInformation>();
                        List<CaseFilesGetDto> fileList = new List<CaseFilesGetDto>();
                        foreach (var file in Request.Form.Files)
                        {

                            if (file.Name.ToLower() == "attachments")
                            {
                                string folderName = Path.Combine("Assets", "CaseAttachments");

                                var applicant = _applicantService.GetApplicantById(Guid.Parse(Case.ApplicantId));
                                string applicantName = applicant.Result.ApplicantName;
                                string applicantFolder = Path.Combine(folderName, applicantName);

                                string pathToSave = Path.Combine(Directory.GetCurrentDirectory(), applicantFolder);

                                //Create directory if not exists
                                if (!Directory.Exists(pathToSave))
                                    Directory.CreateDirectory(pathToSave);

                                if (file.Length > 0)
                                {
                                    string fileName = ContentDispositionHeaderValue.Parse(file.ContentDisposition).FileName.Trim('"');
                                    string fullPath = Path.Combine(pathToSave, fileName);
                                    string dbPath = Path.Combine(applicantFolder, fileName);

                                    using (var stream = new FileStream(fullPath, FileMode.Create))
                                    {
                                        file.CopyTo(stream);
                                    }
                                    CaseAttachment attachment = new()
                                    {
                                        Id = Guid.NewGuid(),
                                        CreatedAt = DateTime.Now,
                                        CreatedBy = Guid.Parse(Case.CreatedBy),
                                        RowStatus = RowStatus.Active,
                                        CaseId = Guid.Parse(caseId),
                                        FilePath = dbPath
                                    };
                                    attachments.Add(attachment);
                                    CaseFilesGetDto uplodedFile = new()
                                    {
                                        FileName = fileName,
                                        FilePath = dbPath
                                    };
                                    fileList.Add(uplodedFile);

                                }

                            }
                            //else if (file.Name.ToLower() == "filesettings")
                            //{
                            //    string folderName = Path.Combine("Assets", "FileSettings");
                            //    string pathToSave = Path.Combine(Directory.GetCurrentDirectory(), folderName);

                            //    // Create the directory if not exists
                            //    if (!Directory.Exists(pathToSave))
                            //        Directory.CreateDirectory(pathToSave);

                            //    if (file.Length > 0)
                            //    {
                            //        string fileName = ContentDispositionHeaderValue.Parse(file.ContentDisposition).FileName.Trim('"');
                            //        string fullPath = Path.Combine(pathToSave, fileName);
                            //        string dbPath = Path.Combine(folderName, fileName);


                            //        using (var stream = new FileStream(fullPath, FileMode.Create))
                            //        {
                            //            file.CopyTo(stream);
                            //        }

                            //        FilesInformation filesInformation = new()
                            //        {
                            //            Id = Guid.NewGuid(),
                            //            CreatedAt = DateTime.Now,
                            //            CreatedBy = Guid.Parse(Case.CreatedBy),
                            //            RowStatus = RowStatus.Active,
                            //            FilePath = dbPath,
                            //            FileSettingId = Guid.Parse(fileName.Split(".")[0]),
                            //            CaseId = Guid.Parse(caseId),
                            //            filetype = file.ContentType
                            //        };
                            //        fileInfos.Add(filesInformation);


                            //    }
                            //}



                        }
                        await _caseAttachmentService.AddMany(attachments);
                        //await _filesInformationService.AddMany(fileInfos);
                        await _encoderHub.Clients.Group(employeeId).getUplodedFiles(fileList, employeeId);

                    }
                
                

                return NoContent();
            }
            catch(Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }
        }


        [HttpPut("encoding"), DisableRequestSizeLimit]
        public async Task<IActionResult> Update()
        {
            try
            {
                CaseEncodePostDto caseEncodePostDto = new CaseEncodePostDto()
                {
                    caseID  = Guid.Parse(Request.Form["caseId"]),
                    CaseNumber = Request.Form["CaseNumber"],
                    LetterNumber = Request.Form["LetterNumber"],
                    LetterSubject = Request.Form["LetterSubject"],
                    CaseTypeId = Guid.Parse(Request.Form["CaseTypeId"]),
                    ApplicantId = Guid.Parse(Request.Form["ApplicantId"]),
                    EmployeeId = Guid.Parse(Request.Form["ApplicantId"]),
                    PhoneNumber2 = Request.Form["PhoneNumber2"],
                    Representative = Request.Form["Representative"],
                    CreatedBy = Guid.Parse(Request.Form["CreatedBy"]),
                };
                string caseId = await _caseEncodeService.Update(caseEncodePostDto);

                if (Request.Form.Files.Any())
                {
                    List<CaseAttachment> attachments = new List<CaseAttachment>();
                    List<FilesInformation> fileInfos = new List<FilesInformation>();
                    foreach (var file in Request.Form.Files)
                    {

                        if (file.Name.ToLower() == "attachments")
                        {
                            string folderName = Path.Combine("Assets", "CaseAttachments");

                            var applicant = _applicantService.GetApplicantById(caseEncodePostDto.ApplicantId);
                            string applicantName = applicant.Result.ApplicantName; // replace with actual applicant name
                            string applicantFolder = Path.Combine(folderName, applicantName);



                            string pathToSave = Path.Combine(Directory.GetCurrentDirectory(), applicantFolder);

                            //Create directory if not exists
                            if (!Directory.Exists(pathToSave))
                                Directory.CreateDirectory(pathToSave);

                            if (file.Length > 0)
                            {
                                string fileName = ContentDispositionHeaderValue.Parse(file.ContentDisposition).FileName.Trim('"');
                                string fullPath = Path.Combine(pathToSave, fileName);
                                string dbPath = Path.Combine(applicantFolder, fileName);

                                using (var stream = new FileStream(fullPath, FileMode.Create))
                                {
                                    file.CopyTo(stream);
                                }
                                CaseAttachment attachment = new()
                                {
                                    Id = Guid.NewGuid(),
                                    CreatedAt = DateTime.Now,
                                    CreatedBy = caseEncodePostDto.CreatedBy,
                                    RowStatus = RowStatus.Active,
                                    CaseId = Guid.Parse(caseId),
                                    FilePath = dbPath
                                };
                                attachments.Add(attachment);
                            }

                        }
                        else if (file.Name.ToLower() == "filesettings")
                        {
                            string folderName = Path.Combine("Assets", "FileSettings");
                            string pathToSave = Path.Combine(Directory.GetCurrentDirectory(), folderName);

                            // Create the directory if not exists
                            if (!Directory.Exists(pathToSave))
                                Directory.CreateDirectory(pathToSave);

                            if (file.Length > 0)
                            {
                                string fileName = ContentDispositionHeaderValue.Parse(file.ContentDisposition).FileName.Trim('"');
                                string fullPath = Path.Combine(pathToSave, fileName);
                                string dbPath = Path.Combine(folderName, fileName);


                                using (var stream = new FileStream(fullPath, FileMode.Create))
                                {
                                    file.CopyTo(stream);
                                }

                                FilesInformation filesInformation = new()
                                {
                                    Id = Guid.NewGuid(),
                                    CreatedAt = DateTime.Now,
                                    CreatedBy = caseEncodePostDto.CreatedBy,
                                    RowStatus = RowStatus.Active,
                                    FilePath = dbPath,
                                    FileSettingId = Guid.Parse(fileName.Split(".")[0]),
                                    CaseId = Guid.Parse(caseId),
                                    filetype = file.ContentType
                                };
                                fileInfos.Add(filesInformation);

                            }
                        }
                    }
                    await _caseAttachmentService.AddMany(attachments);
                    await _filesInformationService.AddMany(fileInfos);
                }

                return NoContent();
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }


        [HttpGet("encoding")]
        public async Task<IActionResult> GetAll(Guid userId)
        {
            try
            {
                return Ok(await _caseEncodeService.GetAll(userId));

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }
        [HttpGet("getCaseById")]
        public async Task<IActionResult> GetCaseById(Guid caseId)
        {
            try
            {
                return Ok(await _caseEncodeService.GetSingleCase(caseId));

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }


        [HttpGet("getCaseNumber")]

        public async Task<string> GetCaseNumebr()
        {

            return await _caseEncodeService.GetCaseNumber();




        }
        [HttpGet("getnotification")]
        public async Task<IActionResult> GetNotification(Guid employeeId)
        {
            try
            {
                return Ok(await _caseEncodeService.GetAllTransfred(employeeId));

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }
        [HttpGet("mycaseList")]
        public async Task<IActionResult> MyCaseList(Guid employeeId)
        {
            try
            {
                return Ok(await _caseEncodeService.MyCaseList(employeeId));

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }


        [HttpGet("searchCases")]
        public async Task<IActionResult> searchCases(string  searchBY)
        {
            try
            {
                return Ok(await _caseEncodeService.SearchCases(searchBY));

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }







        [HttpGet("completedList")]
        public async Task<IActionResult> CompletedCases()
        {
            try
            {
                return Ok(await _caseEncodeService.CompletedCases());

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }
        [HttpGet("getArchivedCases")]

        public async Task<IActionResult> GetArchivedCases()
        {
            try
            {
                return Ok(await _caseEncodeService.GetArchivedCases());

            }
            catch (Exception ex)
            {
                return StatusCode(500, "Internal Server Error");
            }

        }






    }
}
