using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WorldApi.Model
{
    public class City
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Residents { get; set; }
        public int Area { get; set; }
        public Country Country { get; set; }
    }
}
