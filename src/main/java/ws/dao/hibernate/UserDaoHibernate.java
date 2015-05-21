package ws.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ws.dao.LotDao;
import ws.dao.UserDao;
import ws.model.Lot;
import ws.model.User;

import java.util.List;

@SuppressWarnings("unchecked")
public class UserDaoHibernate implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoHibernate.class);


    private static LotDao lotDao;

    public UserDaoHibernate() {
    }

    public static void main(String[] args) {
        System.out.println("begin test");
        UserDao userDao = new UserDaoHibernate();

        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println("user " + user.getId() + ": " + user.getLogin());
            System.out.println();
        }

        System.out.println("end test");
    }

    private LotDao getLotDao() {
        if (lotDao == null) {
            lotDao = new LotDaoHibernate();
        }
        return lotDao;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) getSession()
                .createQuery("from User")
                .list();
    }

    @Override
    public User findById(int id) {
        User user = (User) getSession().load(
                User.class, id);

        return user;
    }

    private Session getSession() {
        return HibernateUtils.getCurrentSession();
    }

    @Override
    public User findByIdWithLots(int id) {
        User user = findById(id);
        List<Lot> lots = getLotDao().findAllByUser(user);
        user.setLots(lots);
        return user;
    }

    @Override
    public boolean save(User user) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;
    }

    @Override
    public User findByLogin(String login) {
        Query query = getSession()
                .createQuery("from User where login=?")
                .setParameter(0, login);
        List<User> users = query.list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.delete(findById(id));
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;

    }
}
