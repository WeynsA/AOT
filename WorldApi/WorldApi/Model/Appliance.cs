using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WorldApi.Model
{
    public class Appliance
    {
        public int id { get; set; }
        public string name { get; set; }
        public int price { get; set; }
        public string description { get; set; }
        public string imgUrl { get; set; }
    }
}
