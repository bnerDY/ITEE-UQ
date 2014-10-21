<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="SaveInfo.aspx.cs" Inherits="p3.SaveInfo" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <h3>Person:</h3>
        FirstName: <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" 
            ControlToValidate="TextBox1" ErrorMessage="RequiredFieldValidator"></asp:RequiredFieldValidator>
        LastName: <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" 
            ControlToValidate="TextBox2" ErrorMessage="RequiredFieldValidator"></asp:RequiredFieldValidator>
        <br />
        DateOfBirth: <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
        <p>Email:<asp:TextBox ID="TextBox4" runat="server"></asp:TextBox>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; StreetAddress: <asp:TextBox ID="TextBox5" runat="server"></asp:TextBox>
        </p>
        <p> Suburb: <asp:TextBox ID="TextBox6" runat="server"></asp:TextBox>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; State: <asp:TextBox ID="TextBox7" runat="server"></asp:TextBox>
        </p>
        <p> PostCode: <asp:TextBox ID="TextBox8" runat="server"></asp:TextBox>
        </p>
        <h3>Job:</h3>
        <p>PositionNumber: <asp:TextBox ID="TextBox9" runat="server"></asp:TextBox>
            <asp:RequiredFieldValidator ID="RequiredFieldValidator9" runat="server" 
                ControlToValidate="TextBox9" ErrorMessage="RequiredFieldValidator"></asp:RequiredFieldValidator>
        </p>
        <p>PositionTitle: <asp:TextBox ID="TextBox10" runat="server"></asp:TextBox>
        </p>
        <p>PositionDescription: <asp:TextBox ID="TextBox11" runat="server"></asp:TextBox>
        </p>
        <p>CampanyName: <asp:TextBox ID="TextBox12" runat="server"></asp:TextBox>
            <asp:RequiredFieldValidator ID="RequiredFieldValidator12" runat="server" 
                ControlToValidate="TextBox12" ErrorMessage="RequiredFieldValidator"></asp:RequiredFieldValidator>
        </p>
        <asp:Button ID="Button1" runat="server" Text="Save" onclick = "save"/>
        <p><asp:Label ID="Label1" runat="server" ></asp:Label></p>
    </div>
    </form>
</body>
</html>
