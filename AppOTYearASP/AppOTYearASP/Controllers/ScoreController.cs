using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AppOTYearASP.Data;
using AppOTYearASP.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppOTYearASP.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ScoreController : ControllerBase
    {
        private readonly WorldContext db;

        public ScoreController(WorldContext db)
        {
            this.db = db;
        }

        // GET: api/Score
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

        // GET: api/Score/5
        [HttpGet("{raceName}")]
        public ActionResult<List<int>> GetScoresFromRace(string raceName)
        {
            //List<Score> highscores = new List<Score>();
            List<int> highscores = new List<int>();

            foreach (var item in db.Scores.ToList())
            {
                if (item.RaceName == raceName)
                {
                    highscores.Add(item.TimeScore);
                }
            }

            highscores.Sort();

            return highscores;
        }

        // POST: api/Score
        [HttpPost]
        public string Post([FromBody] Score value)
        {
            db.Scores.Add(value);
            db.SaveChanges();

            if (checkIfHighScored(value.TimeScore, value.RaceName))
                return "You have set a new highscore!";
            else
                return "Score added to highscore.";
        }

        // PUT: api/Score/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE: api/ApiWithActions/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }

        public bool checkIfHighScored(long value, string raceName)
        {
            List<int> highscores = new List<int>();

            foreach (var item in db.Scores.ToList())
            {
                if (item.RaceName == raceName)
                {
                    highscores.Add(item.TimeScore);
                }
            }
            highscores.Sort();

            if (highscores.First() == value)
                return true;
            else
                return false;
        }
    }
}
