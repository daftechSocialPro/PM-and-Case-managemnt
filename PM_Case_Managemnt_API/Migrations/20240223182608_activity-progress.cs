using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class activityprogress : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<Guid>(
                name: "CaseId",
                table: "ActivityProgresses",
                type: "uniqueidentifier",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_ActivityProgresses_CaseId",
                table: "ActivityProgresses",
                column: "CaseId");

            migrationBuilder.AddForeignKey(
                name: "FK_ActivityProgresses_Cases_CaseId",
                table: "ActivityProgresses",
                column: "CaseId",
                principalTable: "Cases",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ActivityProgresses_Cases_CaseId",
                table: "ActivityProgresses");

            migrationBuilder.DropIndex(
                name: "IX_ActivityProgresses_CaseId",
                table: "ActivityProgresses");

            migrationBuilder.DropColumn(
                name: "CaseId",
                table: "ActivityProgresses");
        }
    }
}
