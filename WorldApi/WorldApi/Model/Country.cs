using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WorldApi.Model
{
    public class Country
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Continent { get; set; }
        public string Code { get; set; }
        public ICollection<City> Cities { get; set; }
    }
}
