using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WorldApi.Model;
using Microsoft.EntityFrameworkCore;

namespace WorldApi.Controllers
{
        [Route("api/homeracer/user/")]
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
                return db.Users.ToList();
            }

        [HttpGet("{userName}")]
        public IActionResult GetUser(string username)
        {
            var user = db.Users.SingleOrDefault(x => x.UserName == username);
            if (user == null)
                return NotFound();
            return Ok(user);
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