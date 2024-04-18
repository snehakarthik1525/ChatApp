function validateForm() {

        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        if(password!="password"){
            alert("Wrong password");
            return false;
        }

        if(username=="sneha"){
            localStorage.setItem("userId", 1);
        }else if(username=="karthik"){
            localStorage.setItem("userId", 2);
        }else if(username=="sushma"){
            localStorage.setItem("userId", 3);
        }else if(username=="vinod"){
            localStorage.setItem("userId", 4);
        }else{
            alert("Wrong username");
            return false;
        }


        window.location.href = "chat-app.html";

        return false;

}
