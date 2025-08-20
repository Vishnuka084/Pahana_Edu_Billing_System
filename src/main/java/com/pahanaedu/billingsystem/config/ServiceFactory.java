package com.pahanaedu.billingsystem.config;

import com.pahanaedu.billingsystem.service.SuperService;
import com.pahanaedu.billingsystem.service.impl.CustomerServiceImpl;
import com.pahanaedu.billingsystem.service.impl.ItemServiceImpl;
import com.pahanaedu.billingsystem.service.impl.OrderServiceImpl;
import com.pahanaedu.billingsystem.service.impl.UserServiceImpl;
import com.pahanaedu.billingsystem.type.ServiceTypes;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:14:56
 */
public class ServiceFactory {

    private static ServiceFactory serviceFactory;

    private ServiceFactory(){}

    public static ServiceFactory getServiceFactory(){
        return serviceFactory==null ?
                serviceFactory=new ServiceFactory() : serviceFactory;
    }

    public SuperService getService(ServiceTypes serviceTypes, BasicDataSource bds){
        switch (serviceTypes){
            case USER:
                return new UserServiceImpl(bds);
            case CUSTOMER:
                return new CustomerServiceImpl(bds);
            case ITEM:
                return new ItemServiceImpl(bds);
            case ORDER:
                return new OrderServiceImpl(bds);
//            case ORDER_DETAIL:
//                return new OrderDetailsServiceImpl();
            default:
                return null;
        }
    }

}