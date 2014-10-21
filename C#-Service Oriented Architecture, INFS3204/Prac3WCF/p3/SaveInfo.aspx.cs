using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text.RegularExpressions;

namespace p3
{
    public partial class SaveInfo : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        public void save(object sender, EventArgs e)
        {
            Service1 Code = new Service1();
            Service2 Campany = new Service2();
            DateTime date;
            int postcode;
            Regex res = new Regex(@"\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*");
            Regex num = new Regex(@"^[0-9]*$");
            
            Job job = new Job();
            Person person = new Person();
            
            if((!int.TryParse(TextBox8.Text, out postcode)) || (!Code.PostcodeValidation(postcode, TextBox7.Text)))
            {
                Label1.Text = "Invalid postcode";
            }
            else if(!res.IsMatch(TextBox4.Text))
            {
                Label1.Text = "Invalid email";
            }
            else if(!num.IsMatch(TextBox9.Text))
            {
                Label1.Text = "Invalid Position number";
            }
            else
            {
                if(DateTime.TryParse(TextBox3.Text, out date)){
                    person.firstName = TextBox1.Text;
                    person.lastName = TextBox2.Text;
                    person.dateOfBirth = date;
                    person.email = TextBox4.Text;
                    person.streetAddress = TextBox5.Text;
                    person.suburb = TextBox6.Text;
                    person.state = TextBox7.Text;
                    person.postcode = postcode;
                    job.positionNumber = Convert.ToInt32(TextBox9.Text);
                    job.positionTitle = TextBox10.Text;
                    job.positionDescription = TextBox11.Text;
                    job.companyName = TextBox12.Text;
                    if (Campany.SaveInfo(person, job))
                    {
                        Label1.Text = "Successfully saved";
                    }
                    else
                    {
                        Label1.Text = "Unable to save";
                    }
                }
                else
                {
                    Label1.Text = "Invalid date(yyyy-MM-dd)";
                }
            }
        }
    }
}