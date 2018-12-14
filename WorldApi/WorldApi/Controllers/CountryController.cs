using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WorldApi.Model;

namespace WorldApi.Controllers
{
    [Route("api/[controller]")]
    public class CountryController : Controller
    {
        private readonly WorldContext db;

        public CountryController(WorldContext db)
        {
            this.db = db;
        }

        // GET api/values
        [HttpGet]
        public IEnumerable<Country> Get()
        {
            return db.Countries.ToList();
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public IActionResult GetCountryId(int id)
        {
            var country = db.Countries.Find(id);
            if (country == null)
                return NotFound();
            return Ok(country);
        }
        //Get met naam ipv ID (werkt)
        //[HttpGet("{name}")]
        //public IActionResult GetCountryName (string name)
        //{
        //    var country = db.Countries.Where(c => c.Name == name).FirstOrDefault();
        //    if (country == null)
        //        return NotFound();

        //    return Ok(country);
        //}

        // POST api/values
        [HttpPost]
        public IActionResult AddCountry([FromBody]Country newCountry)
        {
            db.Countries.Add(newCountry);
            db.SaveChanges();
            return Ok(new Country());
        }

        // PUT api/values/5
        [HttpPut]
        public IActionResult UpdateCountry([FromBody]Country updateCountry)
        {
            var upCountry = db.Countries.Find(updateCountry.Id);
            if (upCountry == null)
                return NotFound();

            upCountry.Name = updateCountry.Name;
            upCountry.Code = updateCountry.Code;
            upCountry.Cities = updateCountry.Cities;
            upCountry.Continent = updateCountry.Continent;

            db.SaveChanges();
            return Ok(upCountry);
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public IActionResult DeleteCountry(int id)
        {
            var country = db.Countries.Find(id);
            if (country == null)
                return NotFound();

            db.Countries.Remove(country);
            db.SaveChanges();

            return NoContent();
        }
    }
}
