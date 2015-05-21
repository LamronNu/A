package ws.dao;

import ws.model.User;

import java.util.List;

public interface UserDao {

    public List<User> findAll();

    public User findById(int id);

    public User findByIdWithLots(int id);

    public boolean save(User user);

    public User findByLogin(String login);

    public boolean delete(int id);
}
