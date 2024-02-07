package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    SessionFactory sessionFactory = Util.getSessionFactory();
    Transaction transaction = null;

    //    User user;

    @Override
    public void createUsersTable() {


        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String sql = "CREATE TABLE IF NOT EXISTS users " +
                    " (" +

                    "  id INT NOT NULL AUTO_INCREMENT," +

                    "  name VARCHAR(50) NOT NULL, " +

                    "  lastName VARCHAR(50) NOT NULL," +

                    "  age INT NOT NULL," +

                    "  PRIMARY KEY (id));";
            session.createNativeQuery

                    (sql, User.class).executeUpdate();

            //   session.persist(user);
            transaction.commit();


        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }


        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery("DROP TABLE IF EXISTS users", User.class).executeUpdate();


            session.getTransaction().commit();


        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(new User(name, lastName, age));
            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.remove(session.get(User.class, id));

            session.getTransaction().commit();


        }
    }
        @Override
        public List<User> getAllUsers () {

            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();

                List<User> list = session.createNativeQuery("FROM users ", User.class).getResultList();

                transaction.commit();
                // session.getTransaction().commit();

                return list;
            }
        }


        @Override
        public void cleanUsersTable () {
            try (Session session = sessionFactory.openSession()) {

                transaction = session.beginTransaction();

                User user = new User();
                session.remove(user);
                session.persist(user);

                session.getTransaction().commit();


            }
        }
}
