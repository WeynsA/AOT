using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace AppOTYearASP.Models
{
    public class Location
    {
        [Key]
        public int LocId { get; set; }
        public string LocName { get; set; }
        public double LocLat { get; set; }
        public double LocLong { get; set; }
        public int RaceIdFK { get; set; }
        public Race Race { get; set; }
        
    }
}
