using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WorldApi.Model
{
    public class DbInitializer
    {
        public static void Initialize(WorldContext context)
        {
            context.Database.EnsureCreated();
            
            if (!context.Appliances.Any())
            {
                var item0 = new Appliance()
                {
                    name = "Wasmachine",
                    price = 10,
                    description = "Ik leen mijn wasmachine uit aan idereen die wil tegen kleine vergoeding.",
                    imgUrl = "data:image/png;base64,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"
                };
                var item1 = new Appliance()
                {
                    name = "Boor",
                    price = 6,
                    description = "Ik leen mijn boormachine uit om in elk gaatje te boren.",
                    imgUrl = "https://st3.depositphotos.com/4177785/18034/v/1600/depositphotos_180341802-stockillustratie-boormachine-glyph-pictogram.jpg"
                };
                var item2 = new Appliance()
                {
                    name = "Grasmaaier",
                    price = 29,
                    description = "Ik leen mijn wasmachine uit aan idereen die wil tegen kleine vergoeding.",
                    imgUrl = "https://image.freepik.com/iconen-gratis/maaier-machine_318-63408.jpg"
                };
                var item3 = new Appliance()
                {
                    name = "Stofzuiger",
                    price = 4,
                    description = "Ik leen mijn stofzuiger uit om stof te zuigen.",
                    imgUrl = "https://img.freepik.com/iconen-gratis/stofzuiger-facing-right_318-99207.jpg?size=338&ext=jpg"
                };
                context.Appliances.Add(item0);
                context.Appliances.Add(item1);
                context.Appliances.Add(item2);
                context.Appliances.Add(item3);

                context.SaveChanges();
            }

            if (!context.Users.Any())
            {
                var user0 = new User()
                {
                    UserName = "Arno",
                    Password = "123456",
                    StartLat = 51.201066,
                    StartLong = 4.409614,
                    EndLat = 51.229800,
                    EndLong = 4.416152
                };

                var user1 = new User()
                {
                    UserName = "ArnoBarbier",
                    Password = "123456",
                    StartLat = 51.201066,
                    StartLong = 4.409614,
                    EndLat = 51.229800,
                    EndLong = 4.416152
                };
                context.Users.Add(user0);
                context.Users.Add(user1);
                context.SaveChanges();
            }
        }
    }
}
