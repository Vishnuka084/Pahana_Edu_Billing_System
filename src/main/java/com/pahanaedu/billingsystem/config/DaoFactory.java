package com.pahanaedu.billingsystem.config;

import com.pahanaedu.billingsystem.dao.DaoTypes;
import com.pahanaedu.billingsystem.dao.SuperDAO;
import com.pahanaedu.billingsystem.dao.impl.CustomerDAOImpl;
import com.pahanaedu.billingsystem.dao.impl.ItemDAOImpl;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:13:34
 */
public class DaoFactory {

    private static DaoFactory daoFactory;

    private DaoFactory(){}

    public static DaoFactory getDaoFactory(){
        return daoFactory==null ? daoFactory=new DaoFactory() : daoFactory;
    }

    public SuperDAO getDAO(DaoTypes daoTypes){
        switch (daoTypes){
//            case USER:
//                return new UserDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
//            case ORDER:
//                return new OrderDAOImpl();
//            case ORDER_DETAIL:
//                return new OrderDetailsDAOImpl();
            default:
                return null;
        }
    }
}
