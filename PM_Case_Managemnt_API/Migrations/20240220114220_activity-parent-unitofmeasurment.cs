using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PMCaseManagemntAPI.Migrations.DB
{
    /// <inheritdoc />
    public partial class activityparentunitofmeasurment : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<Guid>(
                name: "UnitOfMeasurmentId",
                table: "ActivityParents",
                type: "uniqueidentifier",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_ActivityParents_UnitOfMeasurmentId",
                table: "ActivityParents",
                column: "UnitOfMeasurmentId");

            migrationBuilder.AddForeignKey(
                name: "FK_ActivityParents_UnitOfMeasurment_UnitOfMeasurmentId",
                table: "ActivityParents",
                column: "UnitOfMeasurmentId",
                principalTable: "UnitOfMeasurment",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_ActivityParents_UnitOfMeasurment_UnitOfMeasurmentId",
                table: "ActivityParents");

            migrationBuilder.DropIndex(
                name: "IX_ActivityParents_UnitOfMeasurmentId",
                table: "ActivityParents");

            migrationBuilder.DropColumn(
                name: "UnitOfMeasurmentId",
                table: "ActivityParents");
        }
    }
}
