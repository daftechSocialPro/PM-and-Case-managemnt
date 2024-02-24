using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class casetypetoactivity : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<Guid>(
                name: "CaseTypeId",
                table: "Activities",
                type: "uniqueidentifier",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Activities_CaseTypeId",
                table: "Activities",
                column: "CaseTypeId");

            migrationBuilder.AddForeignKey(
                name: "FK_Activities_CaseTypes_CaseTypeId",
                table: "Activities",
                column: "CaseTypeId",
                principalTable: "CaseTypes",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Activities_CaseTypes_CaseTypeId",
                table: "Activities");

            migrationBuilder.DropIndex(
                name: "IX_Activities_CaseTypeId",
                table: "Activities");

            migrationBuilder.DropColumn(
                name: "CaseTypeId",
                table: "Activities");
        }
    }
}
