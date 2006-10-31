package com.artivisi.billing.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String nextUrl = req.getParameter("nextUrl");
        
        if (nextUrl == null) {
            nextUrl = req.getContextPath()+"/index.html";
        }
        
        req.getSession().invalidate();
        res.sendRedirect(nextUrl);
    }
}