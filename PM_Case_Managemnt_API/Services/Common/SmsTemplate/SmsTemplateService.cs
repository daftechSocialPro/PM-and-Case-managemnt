using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.EntityFrameworkCore;
using PM_Case_Managemnt_API.Data;
using PM_Case_Managemnt_API.DTOS.Common;
using PM_Case_Managemnt_API.Helpers;
using PM_Case_Managemnt_API.Models.Common;

namespace PM_Case_Managemnt_API.Services.Common.SmsTemplate
{
    public class SmsTemplateService : ISmsTemplateService
    {
        private readonly DBContext dBContext;

        public SmsTemplateService(DBContext dBContext)
        {
            this.dBContext = dBContext;
        }

        public async Task<List<SmsTemplateGetDto>> GetSmsTemplates()
        {
            var templates = await dBContext.SmsTemplates.Select(x => new SmsTemplateGetDto
            {
                Id = x.Id,
                Title = x.Title,
                Description= x.Description,
                CreatedAt= x.CreatedAt,
                CreatedBy= x.CreatedBy,
                Remark= x.Remark,

            }).ToListAsync();

            return templates;
        }

        public async Task<SmsTemplateGetDto> GetSmsTemplatebyId(Guid id)
        {
            var template = await dBContext.SmsTemplates.Where(x => x.Id == id).Select(x => new SmsTemplateGetDto
            {
                Id = x.Id,
                Title = x.Title,
                Description = x.Description,
                CreatedAt = x.CreatedAt,
                CreatedBy = x.CreatedBy,
                Remark = x.Remark,

            }).FirstOrDefaultAsync();

            return template;
        }

        public async Task<List<SelectListDto>> GetSmsTemplateSelectList()
        {
            var templates = await dBContext.SmsTemplates.Where(x => x.RowStatus == RowStatus.Active).Select(x => new SelectListDto
            {
                Id = x.Id,
                Name = x.Title,
                Photo = x.Description

        
            }).ToListAsync();

            return templates;
        }

        public async Task<ResponseMessage> CreateSmsTemplate(SmsTemplatePostDto smsTemplate)
        {
            try
            {
                Models.Common.SmsTemplate template = new Models.Common.SmsTemplate()
                {
                    Id = Guid.NewGuid(),
                    Title = smsTemplate.Title,
                    Description = smsTemplate.Description,
                    CreatedBy = smsTemplate.CreatedBy,
                    Remark = smsTemplate.Remark,
                    CreatedAt = DateTime.Now,
                };

                await dBContext.SmsTemplates.AddAsync(template);
                await dBContext.SaveChangesAsync();

                return new ResponseMessage
                {
                    Success = true,
                    Message = "SMS Template Created Successfully"
                };

            }
            catch (Exception ex)
            {
                return new ResponseMessage
                {
                    Success = false,
                    Message = ex.Message
                };
            }
            
        }

        public async Task<ResponseMessage> UpdateSmsTemplate(SmsTemplateGetDto smsTemplate)
        {
            try
            {
                var template = await dBContext.SmsTemplates.FindAsync(smsTemplate.Id);

                if(template is null)
                {
                    return new ResponseMessage
                    {
                        Success = false,
                        Message = "SMS Template Not Found"
                    };
                }
                else
                {
                    template.Title= smsTemplate.Title;
                    template.Description= smsTemplate.Description;
                    template.Remark= smsTemplate.Remark;

                    await dBContext.SaveChangesAsync();

                    return new ResponseMessage
                    {
                        Success = true,
                        Message = "SMS Template Updated Successfully"
                    };
                }
            }
            catch(Exception ex)
            {
                return new ResponseMessage
                {
                    Success = false,
                    Message = ex.Message
                };
            }
        }

        public async Task<ResponseMessage> DeleteSmsTemplate(Guid id)
        {
            var template = await dBContext.SmsTemplates.FindAsync(id);
            if (template is null)
            {
                return new ResponseMessage
                {
                    Success = false,
                    Message = "SMS Template Not Found"
                };
            }
            else
            {
                dBContext.SmsTemplates.Remove(template);
                await dBContext.SaveChangesAsync();
                return new ResponseMessage
                {
                    Success = true,
                    Message = "SMS Template Deleted Successfully"
                };
            }
        }



    }
}
