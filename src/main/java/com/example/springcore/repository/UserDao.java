package com.example.springcore.repository;

import com.example.springcore.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class UserDao<T> {
    protected SessionFactory sessionFactory;

    protected UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> getUsernamesByFirstNameAndLastName(String firstName, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        String baseUsername = firstName + "." + lastName;
        return session.createQuery("select u.userName from User u where u.userName like :baseUsername", String.class)
                .setParameter("baseUsername", baseUsername + "%")
                .getResultList();
    }

    public T save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return entity;
    }

    public T update(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
        return entity;
    }

    public User changePassword(String username, String oldPassword, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("from User u where u.userName = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Objects.equals(user.getPassword(), oldPassword)) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(newPassword);

        return session.merge(user);
    }

    public Optional<T> getUserByUsername(String username, Function<User, T> converter) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User u where u.userName = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional()
                .map(converter);
    }

    public void changeUserStatus(String username, boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("from User u where u.userName = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional()
                .ifPresentOrElse(
                        user -> {
                            user.setIsActive(isActive);
                            session.flush();
//                            session.merge(user);
                        },
                        () -> { throw new RuntimeException("User not found"); }
                );
    }
}
