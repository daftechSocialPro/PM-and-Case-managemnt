export interface SmsTemplatePostDto{

    Title:string
    Description:string
    CreatedBy:string
    Remark:string
}

export interface SmsTemplateGetDto{
    Id:string
    Title:string
    Description:string
    CreatedBy:string
    Remark:string
    CreatedAt:string
}