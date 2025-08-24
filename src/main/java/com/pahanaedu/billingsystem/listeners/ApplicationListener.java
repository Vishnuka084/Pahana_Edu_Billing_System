package com.pahanaedu.billingsystem.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:59
 */

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Context Initialized");
        //This method invoked suddenly just after the creation of servlet context

        //How to create connection pool
        BasicDataSource bts=new BasicDataSource();
        bts.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bts.setUrl("jdbc:mysql://localhost/pahana_edu");
        bts.setUsername("root");
        bts.setPassword("1234");

        bts.setMaxTotal(5);//add how many connection we want
        bts.setInitialSize(5);//set how many connection we should initialize

        ServletContext servletContext = servletContextEvent.getServletContext();//get servlet context
        servletContext.setAttribute("bds",bts);//add bds to servlet context
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroyed");
        //This method invoked suddenly before the servlet context destroyed
        try {
            BasicDataSource bds = (BasicDataSource) servletContextEvent.getServletContext().getAttribute("bds");
            bds.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
