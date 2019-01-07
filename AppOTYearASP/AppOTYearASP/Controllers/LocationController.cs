using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AppOTYearASP.Data;
using AppOTYearASP.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace AppOTYearASP.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LocationController : ControllerBase
    {
        private readonly WorldContext db;

        public LocationController(WorldContext db)
        {
            this.db = db;
        }
        // GET: api/Location
        [HttpGet]
        public ActionResult<List<Location>> GetAllLocations()
        {
            var races = db.Locations.ToList();

            return Ok(races);
        }

        // GET: api/Location/5
        [HttpGet("{id}")]
        public ActionResult<Location> Get(int id)
        {
            var result = db.Locations.Find(id);

            return result;
        }

        // POST: api/Location
        [HttpPost]
        public ActionResult Post([FromBody] Location value)
        {
            db.Locations.Add(value);
            db.SaveChanges();

            return Created("", value);
        }

        // PUT: api/Location/5
        [HttpPut("{id}/{value}")]
        public void Put(int id, string value)
        {
            var result = db.Locations.Find(id);
            result.RaceIdFK = db.Races.SingleOrDefault(x => x.RaceName == value).RaceId;
            db.SaveChanges();
        }

        // DELETE: api/ApiWithActions/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
