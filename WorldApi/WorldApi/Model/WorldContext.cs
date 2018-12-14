using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace WorldApi.Model
{
    public class WorldContext : DbContext
    {
        public WorldContext (DbContextOptions<WorldContext> options): base(options)
        {
        }

        public DbSet<City> Cities { get; set; }
        public DbSet<Country> Countries { get; set; }
        public DbSet<Appliance> Appliances { get; set; }
        public DbSet<User> Users { get; set; }
    }
}
