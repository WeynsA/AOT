using AppOTYearASP.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AppOTYearASP.Data
{
    public class WorldContext : DbContext
    {
        public WorldContext(DbContextOptions<WorldContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Score> Scores { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().ToTable("Users");
            modelBuilder.Entity<Score>().ToTable("Scores");

        //    modelBuilder.Entity<Score>()
        //        .HasOne(c => c.User)
        //        .WithMany(e => e.Scores)
        //        .HasForeignKey(x => x.UserId);
            
        }
    }
}
