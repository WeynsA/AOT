using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AppOTYearASP.Models
{
    public class User
    {
        public User()
        {
            //this.Scores = new HashSet<Score>();
        }
        public int UserId { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public double StartLat { get; set; }
        public double StartLong { get; set; }
        public double EndLat { get; set; }
        public double EndLong { get; set; }
        public string StartStreetname { get; set; }
        public string EndStreetname { get; set; }
        //public ICollection<Score> Scores { get; set; }

    }
}
