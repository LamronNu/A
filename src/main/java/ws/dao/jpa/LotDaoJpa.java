package ws.dao.jpa;

import org.apache.log4j.Logger;
import ws.dao.LotDao;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;


public class LotDaoJpa implements LotDao {
    private static final Logger log = Logger.getLogger(LotDaoJpa.class);

    private static final String PERSISTENCE_UNIT_NAME = "auction";
    private EntityManagerFactory emf;
    private EntityManager em;
    private BidDaoJpa bidDao;
    private boolean isInit = false;

    public LotDaoJpa() {
        //connection = DaoUtils.getConnection();
//        init();
    }

    private void init() {
        if (!isInit) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = emf.createEntityManager();
            bidDao = new BidDaoJpa();
            isInit = true;
        }
    }

    @Override
    public List<Lot> findAllByUser(User user) {
        init();
        Query q = em.createQuery("select c from Lot c where c.owner = :owner", Lot.class);
        q.setParameter("owner", user);
        List<Lot> lots = q.getResultList();
        return lots;
    }

    @Override
    public boolean save(Lot lot) {
        init();
        em.getTransaction().begin();
        try {
            em.persist(lot);
        } catch (Exception e) {
            log.error("catch exception:", e);
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public Lot findById(int id) {
//        Query q = //em.createQuery("select c from Lot c where c.id = :id");
//        q.setParameter("id", id);
        init();
        Lot lot = em.find(Lot.class, id);//(Lot)q.getSingleResult();
        return lot;
    }

    @Override
    public Lot findByIdWithBids(int id) {
        Lot lot = findById(id);
        List<Bid> bids = bidDao.findAllByLot(lot);
        lot.setBids(bids);
        double maxValue = bids == null ? null : bids.get(bids.size() - 1).getValue();
        lot.setMaxBidValue(maxValue);//set max bid
        return lot;
    }

    @Override
    public boolean update(Lot lot) {
//        Query q = em.createQuery("select c from Lot c where c.id = :id");
//        q.setParameter("id", lot.getId());
//        Lot lot = (Lot)q.getSingleResult();
        init();
        em.getTransaction().begin();
        try {//todo check
            em.refresh(lot);//??
        } catch (Exception e) {
            log.error("catch exception:", e);
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean updateState(Lot lot) {
//        Lot dbLot = em.find(Lot.class, lot.getId());
        Lot dbLot = findById(lot.getId());
        em.getTransaction().begin();
        try {
            dbLot.setState(lot.getState());
        } catch (Exception e) {
            log.error("catch exception:", e);
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Lot lot) {
//        Query q = em.createQuery("select c from Lot c where c.id = :id");
//        q.setParameter("id", lot.getId());
//        Lot lot = (Lot)q.getSingleResult();
        init();
        em.getTransaction().begin();
        try {
            em.remove(lot);
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public List<Lot> findAllOverdueLots() {
        init();
        Query q = em.createQuery("select c from Lot c where c.state = :state", Lot.class);
        q.setParameter("state", Lot.ACTIVE);
        List<Lot> lots = q.getResultList();
        return lots;
    }

    @Override
    public List<Lot> findAll() {
        init();
        Query q = em.createQuery("select c from Lot c", Lot.class);
        List<Lot> lots = q.getResultList();
        return lots;
    }

}