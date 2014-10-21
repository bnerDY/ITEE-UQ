using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace p3
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    public class Service1 : IService1
    {
        public Boolean PostcodeValidation(int postcode, string state)
        {
            Boolean result = true;
            switch(state){
                case "NSW":
                    if ((postcode>=2000 && postcode<=2599)||(postcode>=2619 && postcode<=2898)||(postcode>=2921 && postcode<=2999))
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "ACT":
                    if ((postcode>=2600 && postcode<=2618)||(postcode>=2900 && postcode<=2920))
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "VIC":
                    if (postcode>=3000 && postcode<=3999)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "QLD":
                    if (postcode>=4000 && postcode<=4999)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "SA":
                    if (postcode>=5000 && postcode<=5799)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "WA":
                    if (postcode>=6000 && postcode<=6797)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "TAS":
                    if (postcode >= 7000 && postcode <= 7799)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                case "NT":
                    if (postcode >= 0800 && postcode <= 0899)
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    break;
                }
            return result;
        }
    }
}
