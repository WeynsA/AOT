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

                var race1 = new Race()
                {
                    RaceName = "ArnoLoop",                    
                };
                context.Races.Add(race1);
                context.SaveChanges();

                var location1 = new Location()
                {
                    LocName = "Brabo",
                    LocLat = 51.221228,
                    LocLong = 4.399698,
                    RaceIdFK = 1
                };
                var location2 = new Location()
                {
                    LocName = "Standbeeld Stadhuis",
                    LocLat = 51.220884,
                    LocLong = 4.398995,
                    RaceIdFK = 1

                };
                var location3 = new Location()
                {
                    LocName = "Het Steen",
                    LocLat = 51.222773,
                    LocLong = 4.397367,
                    RaceIdFK = 1

                };
                var location4 = new Location()
                {
                    LocName = "Pieter Paul Rubens",
                    LocLat = 51.219326,
                    LocLong = 4.401576,
                    RaceIdFK = 1


                };
                var location5 = new Location()
                {
                    LocName = "MAS",
                    LocLat = 51.228989,
                    LocLong = 4.40816,
                    RaceIdFK = 1


                };
                var location6 = new Location()
                {
                    LocName = "Stadswaag",
                    LocLat = 51.223877,
                    LocLong = 4.407136,
                    RaceIdFK = 1

                };

                context.Locations.Add(location1);
                context.Locations.Add(location2);
                context.Locations.Add(location3);
                context.Locations.Add(location4);
                context.Locations.Add(location5);
                context.Locations.Add(location6);
                context.SaveChanges();
            }
        }
    }
}
