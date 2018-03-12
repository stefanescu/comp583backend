package com.comp680backend.controllers;

import com.comp680backend.models.User;
import com.comp680backend.util.CloudSqlManager;
import com.google.gson.Gson;
import com.comp680backend.util.GoogleAuthUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




@WebServlet(name = "AuthenticationServlet", value = "/auth")
public class AuthenticationServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GOT POST");
        GoogleAuthUtil authUtil = new GoogleAuthUtil();
        String idTokenField = req.getHeader("google_id_token");
        System.out.println(idTokenField);
        User user = authUtil.authenticate(idTokenField);
    }
}
