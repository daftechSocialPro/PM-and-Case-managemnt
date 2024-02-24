using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class planstructureupdate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<double>(
                name: "BaseLine",
                table: "ActivityParents",
                type: "float",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<bool>(
                name: "IsClassfiedToBranch",
                table: "ActivityParents",
                type: "bit",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<double>(
                name: "Target",
                table: "ActivityParents",
                type: "float",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<Guid>(
                name: "OrganizationalStructureId",
                table: "Activities",
                type: "uniqueidentifier",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Activities_OrganizationalStructureId",
                table: "Activities",
                column: "OrganizationalStructureId");

            migrationBuilder.AddForeignKey(
                name: "FK_Activities_OrganizationalStructures_OrganizationalStructureId",
                table: "Activities",
                column: "OrganizationalStructureId",
                principalTable: "OrganizationalStructures",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Activities_OrganizationalStructures_OrganizationalStructureId",
                table: "Activities");

            migrationBuilder.DropIndex(
                name: "IX_Activities_OrganizationalStructureId",
                table: "Activities");

            migrationBuilder.DropColumn(
                name: "BaseLine",
                table: "ActivityParents");

            migrationBuilder.DropColumn(
                name: "IsClassfiedToBranch",
                table: "ActivityParents");

            migrationBuilder.DropColumn(
                name: "Target",
                table: "ActivityParents");

            migrationBuilder.DropColumn(
                name: "OrganizationalStructureId",
                table: "Activities");
        }
    }
}
