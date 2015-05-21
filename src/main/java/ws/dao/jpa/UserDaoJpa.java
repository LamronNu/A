package ws.dao.jpa;


import org.apache.log4j.Logger;
import ws.dao.LotDao;
import ws.dao.UserDao;
import ws.model.Lot;
import ws.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class UserDaoJpa implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoJpa.class);
    private static final String PERSISTENCE_UNIT_NAME = "auction";
    private EntityManagerFactory emf;
    private EntityManager em;
    private LotDao lotDao;
    private boolean isInit = false;

    public UserDaoJpa() {
        //
        init();
    }

    public static void main(String[] args) {
        System.out.println(Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).isOpen());
    }

    private void init() {
        if (!isInit) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = emf.createEntityManager();
            lotDao = new LotDaoJpa();
            isInit = true;
        }
    }

    @Override
    public List<User> findAll() {
        init();
        Query q = em.createQuery("select c from User c", User.class);
        List<User> users = q.getResultList();
        return users;
    }

    @Override
    public User findById(int id) {
//        Query q = em.createQuery("select c from User c where c.id = :id");
//        q.setParameter("id", id);
//        User user = (User)q.getSingleResult();
        init();
        User user = em.find(User.class, id);
        return user;
    }

    @Override
    public User findByIdWithLots(int id) {
//        Query q = em.createQuery("select c from User c where c.id = :id");
//        q.setParameter("id", id);
//        User user = (User)q.getSingleResult();
        User user = findById(id);
        List<Lot> lots = lotDao.findAllByUser(user);
        user.setLots(lots);
        return user;
    }

    @Override
    public boolean save(User user) {
        init();
        em.getTransaction().begin();
        try {
            em.persist(user);
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public User findByLogin(String login) {
        init();
        Query q = em.createQuery("select c from User c where c.login = :login", User.class);
        q.setParameter("login", login);
        User user = (User) q.getSingleResult();
        return user;
    }

    @Override
    public boolean delete(int id) {
//        Query q = em.createQuery("select c from User c where c.id = :id", User.class);
//        q.setParameter("id", id);
//        User user = (User)q.getSingleResult();
        User user = findById(id);
        em.getTransaction().begin();
        try {
            em.remove(user);
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

}
