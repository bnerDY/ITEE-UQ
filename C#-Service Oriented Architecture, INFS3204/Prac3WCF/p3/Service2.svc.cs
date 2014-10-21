using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.IO;

namespace p3
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service2" in code, svc and config file together.
    public class Service2 : IService2
    {
        string path = System.AppDomain.CurrentDomain.BaseDirectory.ToString();
        public Boolean SaveInfo(Person person, Job job)
        {
            string DOB = person.dateOfBirth.ToString("yyyy-MM-dd");
            try
            {
                System.IO.StreamWriter w1 = new System.IO.StreamWriter(path + "Person.txt", true);
                System.IO.StreamWriter w2 = new System.IO.StreamWriter(path + "Job.txt", true);
                w1.WriteLine(person.firstName + "," + person.lastName + "," + DOB + "," + person.email + "," + person.streetAddress + "," + person.state + "," + person.suburb + "," + job.positionNumber);
                w2.WriteLine(job.positionNumber + "," + job.positionTitle + "," + job.positionDescription + "," + job.companyName);
                w1.Close();
                w2.Close();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public Job GetJobInfo(string firstName, string lastName)
        {
            Job res = new Job();
            try
            {
                FileStream person = new FileStream(path + "Person.txt", FileMode.Open, FileAccess.Read);
                FileStream jobinfo = new FileStream(path + "Job.txt", FileMode.Open, FileAccess.Read);
                System.IO.StreamReader sr1 = new System.IO.StreamReader(person, System.Text.Encoding.Default);
                System.IO.StreamReader sr2 = new System.IO.StreamReader(jobinfo, System.Text.Encoding.Default);
                string temp1;
                string temp2;
                while(true){
                    temp1 = sr1.ReadLine();;
                    sr1.Close();
                    string[] p = temp1.Split(',');
                    if(p[0] == firstName && p[1] == lastName){
                        temp2 = sr2.ReadLine();
                        sr2.Close();
                        string[] j = temp2.Split(',');
                        if (p[p.Length-1] == j[0])
                        {
                            res.positionNumber = Convert.ToInt32(j[0]);
                            res.positionTitle = j[j.Length-3];
                            res.positionDescription = j[j.Length-2];
                            res.companyName = j[j.Length-1];
                            return res;
                        }
                    }
                }
            }
            catch 
            {
                return null;
            }
        }

        public List<Person> GetColleagues(string firstName, string lastName)
        {
            List<Person> res = new List<Person>();
            Job job = new Job();   
            try
            {
                string cname = GetJobInfo(firstName, lastName).companyName;
                FileStream person = new FileStream(path + "Person.txt", FileMode.Open, FileAccess.Read);
                FileStream jobinfo = new FileStream(path + "Job.txt", FileMode.Open, FileAccess.Read);
                System.IO.StreamReader sr1 = new System.IO.StreamReader(person, System.Text.Encoding.Default);
                System.IO.StreamReader sr2 = new System.IO.StreamReader(jobinfo, System.Text.Encoding.Default);
                string temp1 ;
                string temp2 ;
                while ((temp2 = sr2.ReadLine()) != null)
                {
                    string[] j = temp2.Split(',');
                    if (cname == j[j.Length - 1])
                    {
                        Person pers = new Person();
                        temp1 = sr1.ReadLine();
                        string[] p = temp1.Split(',');
                        if (j[0] == p[p.Length - 1])
                        {
                            if( p[0]!=firstName && p[1]!=lastName){
                                pers.firstName = p[0];
                                pers.lastName = p[1];
                                pers.dateOfBirth = Convert.ToDateTime(p[2]);
                                pers.email = p[3];
                                pers.streetAddress = p[4];
                                pers.suburb = p[5];
                                pers.state = p[6];
                                pers.postcode = Convert.ToInt32(p[7]);
                                job.positionNumber = Convert.ToInt32(j[0]);
                                job.positionTitle = j[1];
                                job.positionDescription = j[2];
                                job.companyName = j[j.Length - 1];
                                pers.job = job;
                                res.Add(pers);
                            }
                        }
                    }
                }
                sr1.Close();
                sr2.Close();
                return res;
            }
            catch
            {
                return null;
            }
        }
    }
}
