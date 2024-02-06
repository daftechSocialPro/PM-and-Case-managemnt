namespace PM_Case_Managemnt_API.DTOS
{
    public class AuthenticationDtos
    {
        public class UserRoleDto
        {
            public string UserId { get; set; } = null!;
            public string RoleName { get; set; } = null!;
        }
    }
}
