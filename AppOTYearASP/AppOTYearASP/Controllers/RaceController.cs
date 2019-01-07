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
    public class RaceController : ControllerBase
    {
        private readonly WorldContext db;

        public RaceController(WorldContext db)
        {
            this.db = db;
        }
        // GET: api/Race
        [HttpGet]
        public ActionResult<List<Race>> GetAllRaces()
        {
            var races = db.Races.ToList();

            return Ok(races);
        }

        // GET: api/Race/5
        [HttpGet("{value}")]
        public List<Race> Get(string value)
        {
            return db.Races.
                Include(c => c.Locations)
                .Where(x => x.RaceName == value)
                .ToList() ;
        }

        // POST: api/Race
        [HttpPost]
        public void Post([FromBody] Race value)
        {
            db.Races.Add(value);
            db.SaveChanges();
        }

        // PUT: api/Race/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE: api/ApiWithActions/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
