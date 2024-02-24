﻿namespace PM_Case_Managemnt_API.DTOS.Common
{
    public record SmsTemplatePostDto
    {
        public string Title { get; set; }
        public string Description { get; set; }
        public Guid CreatedBy { get; set; }
        public string? Remark { get; set; }
    }

    public record SmsTemplateGetDto
    {
        public Guid Id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public Guid CreatedBy { get; set; }
        public DateTime CreatedAt { get; set; }
        public string? Remark { get; set; }
    }

    
}
