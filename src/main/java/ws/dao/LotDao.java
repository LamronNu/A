package ws.dao;

import ws.model.Lot;
import ws.model.User;

import java.util.List;

public interface LotDao {

    public List<Lot> findAll();

    public List<Lot> findAllByUser(User user);

    public Lot findById(int id);

    public Lot findByIdWithBids(int id);

    public boolean save(Lot lot);

    public boolean update(Lot lot);

    public boolean updateState(Lot lot);

    public boolean delete(Lot lot);

    public List<Lot> findAllOverdueLots();
}
