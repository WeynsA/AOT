using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WorldApi.Model;
using Microsoft.EntityFrameworkCore;



namespace WorldApi.Controllers
{
    [Route("api/[controller]")]
    public class ApplianceController : Controller
    {
        private readonly WorldContext db;

        public ApplianceController(WorldContext db)
        {
            this.db = db;
        }

        // GET api/values
        [HttpGet]
        public IEnumerable<Appliance> GetAll()
        {
            return db.Appliances.ToList();
        }
    }
}