package com.comp680backend.controllers;

import com.comp680backend.models.User;
import com.comp680backend.util.GoogleAuthUtil;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




@WebServlet(name = "AuthenticationServlet", value = "/auth")
public class AuthenticationServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GOT POST");
        GoogleAuthUtil authUtil = new GoogleAuthUtil();
        String idTokenField = req.getHeader("google_id_token");
        System.out.println(idTokenField);
        User user = authUtil.authenticate(idTokenField);
        if (user != null) {
            resp.setStatus(HttpStatus.SC_OK);
            new Gson().toJson(user, resp.getWriter());
        } else {
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
            resp.getWriter().write("Unable to process user with DB");
        }
    }
}
