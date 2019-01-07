using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AppOTYearASP.Models
{
    public class Race
    {
        public int RaceId { get; set; }
        public string RaceName { get; set; }
        public ICollection<Location> Locations { get; set; }
    }
}
