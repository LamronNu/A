package ws.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ws.dao.BidDao;
import ws.model.Bid;
import ws.model.Lot;

import java.util.List;

public class BidDaoHibernate implements BidDao {
    private static final Logger log = Logger.getLogger(BidDaoHibernate.class);

    public BidDaoHibernate() {
    }

    private Session getSession() {
        return HibernateUtils.getCurrentSession();
    }

    @Override
    public List<Bid> findAllByLot(Lot lot) {
        Query q = getSession()
                .createQuery("from Bid l where l.lot = :lot");
        q.setParameter("lot", lot);
        List<Bid> bids = q.list();
        return bids;
    }


    @Override
    public boolean save(Bid bid) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.save(bid);
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;

    }
}
