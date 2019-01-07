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
        public DbSet<Race> Races { get; set; }
        public DbSet<Location> Locations { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().ToTable("Users");
            modelBuilder.Entity<Score>().ToTable("Scores");        
            modelBuilder.Entity<Race>().ToTable("Races");            
            modelBuilder.Entity<Location>().ToTable("Locations");

            modelBuilder.Entity<Location>()
                .HasOne(c => c.Race)
                .WithMany(e => e.Locations)
                .HasForeignKey(y => y.RaceIdFK)
                .IsRequired();
            
        }
    }
}
