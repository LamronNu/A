package ws.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import ws.dao.BidDao;
import ws.dao.LotDao;
import ws.dao.UserDao;
import ws.model.Bid;
import ws.model.Lot;
import ws.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LotDaoHibernate implements LotDao {
    private static final Logger log = Logger.getLogger(LotDaoHibernate.class);
    private static BidDao bidDao;
    private UserDao userDao;

    public LotDaoHibernate() {
    }

    public static void main(String[] args) {
        System.out.println("begin test");
        LotDao lotDao = new LotDaoHibernate();

        List<Lot> lots = lotDao.findAll();
        for (Lot lot : lots) {
            System.out.println("lot " + lot.getId() + ": " + lot.getName());
            System.out.println();
        }

        System.out.println("end test");
    }

    private Session getSession() {
        return HibernateUtils.getCurrentSession();
    }

    private Session getNewSession() {
        return HibernateUtils.getNewSession();
    }

    private BidDao getBidDao() {
        if (bidDao == null) {
            bidDao = new BidDaoHibernate();
        }
        return bidDao;
    }
//    private UserDao getUserDao() {
//        if (userDao == null){
//            userDao = new UserDaoHibernate();
//        }
//        return userDao;
//    }

    @Override
    public List<Lot> findAllByUser(User user) {
        Query q = getSession()
                .createQuery("from Lot l where l.owner = :owner");
        q.setParameter("owner", user);
        List<Lot> lots = q.list();
        System.out.println("count of lots: " + lots.size());
        return lots;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lot> findAll() {
        List<Lot> results = new ArrayList<Lot>();
        List<Lot> lots = getSession()
                .createQuery("from Lot").list();
//        for (Lot lot: lots){
//            lot.setOwner(getUserDao().findById(lot.));
//        }
        return lots;
    }

    @Override
    public Lot findById(int id) {
        Lot lot = (Lot) getSession().load(
                Lot.class, id);

        return lot;
    }

    @Override
    public Lot findByIdWithBids(int id) {
        Lot lot = findById(id);
        List<Bid> bids = getBidDao().findAllByLot(lot);
        lot.setBids(bids);
        return lot;
    }

    @Override
    public boolean save(Lot lot) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.save(lot);
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;
    }


    @Override
    public boolean update(Lot lot) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.update(lot);
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;
    }

    @Override
    public boolean updateState(Lot lot) {
        return update(lot);
    }


    @Override
    public boolean delete(Lot lot) {
        Session session = getSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            session.delete(lot);
            tx.commit();
            result = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error(e);
        }
        return result;
    }

    @Override
    public List<Lot> findAllOverdueLots() {
        Criteria cr = getSession().createCriteria(Lot.class);
        cr.add(Restrictions.eq("state", Lot.ACTIVE));//active


        cr.add(Restrictions.lt("finishDate", new Date()));//in past

        List<Lot> lots = cr.list();
        return lots;
    }


}
