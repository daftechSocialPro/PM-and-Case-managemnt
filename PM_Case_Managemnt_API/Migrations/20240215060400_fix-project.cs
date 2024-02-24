using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class fixproject : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            //migrationBuilder.DropForeignKey(
            //    name: "FK_Activities_Employees_FinanceId",
            //    table: "Activities");

            migrationBuilder.DropForeignKey(
                name: "FK_Plans_Employees_FinanceId",
                table: "Plans");

            migrationBuilder.DropIndex(
                name: "IX_Activities_FinanceId",
                table: "Activities");

            migrationBuilder.DropColumn(
                name: "BudgetType",
                table: "Activities");

            migrationBuilder.DropColumn(
                name: "FinanceId",
                table: "Activities");

            migrationBuilder.DropColumn(
                name: "ProjectFunder",
                table: "Activities");

            migrationBuilder.AlterColumn<Guid>(
                name: "FinanceId",
                table: "Plans",
                type: "uniqueidentifier",
                nullable: true,
                oldClrType: typeof(Guid),
                oldType: "uniqueidentifier");

            migrationBuilder.AddColumn<string>(
                name: "ProjectFunder",
                table: "Plans",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddForeignKey(
                name: "FK_Plans_Employees_FinanceId",
                table: "Plans",
                column: "FinanceId",
                principalTable: "Employees",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Plans_Employees_FinanceId",
                table: "Plans");

            migrationBuilder.DropColumn(
                name: "ProjectFunder",
                table: "Plans");

            migrationBuilder.AlterColumn<Guid>(
                name: "FinanceId",
                table: "Plans",
                type: "uniqueidentifier",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"),
                oldClrType: typeof(Guid),
                oldType: "uniqueidentifier",
                oldNullable: true);

            migrationBuilder.AddColumn<int>(
                name: "BudgetType",
                table: "Activities",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddColumn<Guid>(
                name: "FinanceId",
                table: "Activities",
                type: "uniqueidentifier",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"));

            migrationBuilder.AddColumn<string>(
                name: "ProjectFunder",
                table: "Activities",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Activities_FinanceId",
                table: "Activities",
                column: "FinanceId");

            migrationBuilder.AddForeignKey(
                name: "FK_Activities_Employees_FinanceId",
                table: "Activities",
                column: "FinanceId",
                principalTable: "Employees",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_Plans_Employees_FinanceId",
                table: "Plans",
                column: "FinanceId",
                principalTable: "Employees",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
