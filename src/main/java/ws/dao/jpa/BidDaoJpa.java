package ws.dao.jpa;

import org.apache.log4j.Logger;
import ws.dao.BidDao;
import ws.model.Bid;
import ws.model.Lot;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class BidDaoJpa implements BidDao {
    private static final Logger log = Logger.getLogger(BidDaoJpa.class);

    private static final String PERSISTENCE_UNIT_NAME = "auction";
    private EntityManagerFactory emf;
    private EntityManager em;
    private boolean isInit = false;

    public BidDaoJpa() {
        // init();
    }

    private void init() {
        if (!isInit) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = emf.createEntityManager();
            isInit = true;
        }
    }

    @Override
    public boolean save(Bid bid) {
        init();
        em.getTransaction().begin();
        try {
            em.persist(bid);
        } catch (Exception e) {
            log.error("catch exception:", e);
            return false;
        }
        em.getTransaction().commit();
        return true;
    }

    @Override
    public List<Bid> findAllByLot(Lot lot) {
        init();
        Query q = em.createQuery("select c from Bid c where c.lot = :lot", Bid.class);
        q.setParameter("lot", lot);
        List<Bid> bids = q.getResultList();
        return bids;
    }
}
