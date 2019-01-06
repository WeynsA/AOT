using AppOTYearASP.Models;
using AppOTYearASP.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AppOTYearASP.Data
{
    public class DbInitializer
    {
        public static void Initialize(WorldContext context)
        {
            context.Database.EnsureCreated();


            if (!context.Users.Any())
            {
                var user0 = new User()
                {
                    UserName = "Arno",
                    Password = "123456",
                    StartLat = 51.201066,
                    StartLong = 4.409614,
                    EndLat = 51.229800,
                    EndLong = 4.416152,
                    EndStreetname = "Ellermansstraat",
                    StartStreetname = "Schulstraat"
                };

                var user1 = new User()
                {
                    UserName = "ArnoBarbier",
                    Password = "123456",
                    StartLat = 51.201066,
                    StartLong = 4.409614,
                    EndLat = 51.223734,
                    EndLong = 4.407162,
                    StartStreetname = "Schulstraat",
                    EndStreetname = "Stadswaag",

                };
                context.Users.Add(user0);
                context.Users.Add(user1);
                context.SaveChanges();
            }
        }
    }
}
