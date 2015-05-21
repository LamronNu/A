package ws.dao;

import org.apache.log4j.Logger;
import ws.dao.hibernate.BidDaoHibernate;
import ws.dao.hibernate.LotDaoHibernate;
import ws.dao.hibernate.UserDaoHibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DaoFactory {
    public static final String DATA_DELIMITER = " = ";
    public static final String PROPERTIES_FILE = "src\\main\\resources\\dao.properties";
    private static final Logger log = Logger.getLogger(DaoFactory.class);
    static Map<String, String> configMap = new HashMap<String, String>();

    public static void main(String[] args) {
        UserDao userDao = getUserDao();
        System.out.println(userDao.getClass().getSimpleName());
    }

    private static void getConfig() {
        Scanner in = null;
        try {
            in = new Scanner(new File(PROPERTIES_FILE));
        } catch (FileNotFoundException e) {
            log.error(e);
            return;
        }
        String currentRow = "";
        String currentValue;
        String currentKey;
        while (in.hasNext()) {
            currentRow = in.nextLine();
            currentKey = currentRow.split(DATA_DELIMITER)[0];
            currentValue = currentRow.split(DATA_DELIMITER)[1];
            configMap.put(currentKey, currentValue);
        }
    }

    private static Object createClass(String interfaceName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        getConfig();
        String className = configMap.get(interfaceName);
        if (className != null) {
            Class classObj = Class.forName(className);
            Object obj = classObj.newInstance();
            return obj;
        } else {
            throw new ClassNotFoundException();
        }
    }


    public static UserDao getUserDao() {
        try {
            return (UserDao) createClass("ws.dao.UserDao");
        } catch (ClassNotFoundException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InstantiationException e) {
            log.error(e);
        }
        return new UserDaoHibernate();//by default
    }

    public static LotDao getLotDao() {
        try {
            return (LotDao) createClass("ws.dao.LotDao");
        } catch (ClassNotFoundException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InstantiationException e) {
            log.error(e);
        }
        return new LotDaoHibernate();//by default
    }

    public static BidDao getBidDao() {
        try {
            return (BidDao) createClass("ws.dao.BidDao");
        } catch (ClassNotFoundException e) {
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InstantiationException e) {
            log.error(e);
        }
        return new BidDaoHibernate();//by default
    }
}
