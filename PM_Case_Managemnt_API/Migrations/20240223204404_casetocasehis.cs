using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class casetocasehis : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ActivityProgresses_Cases_CaseId",
                table: "ActivityProgresses");

            migrationBuilder.AddForeignKey(
                name: "FK_ActivityProgresses_CaseHistories_CaseId",
                table: "ActivityProgresses",
                column: "CaseId",
                principalTable: "CaseHistories",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ActivityProgresses_CaseHistories_CaseId",
                table: "ActivityProgresses");

            migrationBuilder.AddForeignKey(
                name: "FK_ActivityProgresses_Cases_CaseId",
                table: "ActivityProgresses",
                column: "CaseId",
                principalTable: "Cases",
                principalColumn: "Id");
        }
    }
}
