package com.comp680backend.controllers;

import com.comp680backend.util.CloudStorageUtil;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ImageUploadServlet", value = "/uploadImage")
@MultipartConfig
public class ImageUploadServlet extends HttpServlet {
    private CloudStorageUtil storageUtil = new CloudStorageUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String url = storageUtil.processImageUploadRequest(req);
            System.out.println(url);
            resp.setStatus(HttpStatus.SC_OK);
            resp.getWriter().write(url);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpStatus.SC_OK);
        resp.getWriter().write("IMAGE SERVLET UP!");
    }
}
