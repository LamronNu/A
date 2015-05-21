package ws.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtils {
    private static final Logger log = Logger.getLogger(HibernateUtils.class);

    private static final SessionFactory sessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        //try {
        Configuration configuration = new Configuration();
        configuration.configure();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            throw new ExceptionInInitializerError(ex);
//        }
    }

    private static Session session;

    public static Session getNewSession() {
        return sessionFactory.openSession();
    }

    public static Session getCurrentSession() throws HibernateException {
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;//sessionFactory.getCurrentSession();
    }


}
