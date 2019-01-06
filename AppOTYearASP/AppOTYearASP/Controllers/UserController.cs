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
    public class UserController : Controller
    {
        private readonly WorldContext db;

        public UserController(WorldContext db)
        {
            this.db = db;
        }

        // GET api/values
        [HttpGet]
        public IEnumerable<User> GetAll()
        {
            return db.Users;
                
        }

        [HttpGet("{userName}")]
        public ActionResult<User> GetUser(string username)
        {
            var user = db.Users.SingleOrDefault(x => x.UserName == username);
            if (user == null)
                return NotFound();
            return user;

        }

        [HttpPost]
        public IActionResult CreateUser([FromBody] User newUser)
        {
            db.Users.Add(newUser);
            db.SaveChanges();

            return Created("", newUser);
        }
    }
}