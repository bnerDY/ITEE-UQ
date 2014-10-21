<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="SearchColleagues.aspx.cs" Inherits="p3.SearchColleagues" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        First Name:<asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        Last Name:<asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        <br />
        <asp:Button ID="Button1" runat="server" Text="search" onclick = "search"/>
    </div>
    <asp:TextBox ID="TextBox3" runat="server" TextMode="MultiLine" Width="707px" 
        Height="235px"></asp:TextBox>
    <p>
        <asp:Label ID="Label1" runat="server"></asp:Label>
    </p>


    </form>
</body>
</html>
