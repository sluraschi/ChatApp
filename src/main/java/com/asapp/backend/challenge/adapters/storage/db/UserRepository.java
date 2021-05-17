package com.asapp.backend.challenge.adapters.storage.db;

import com.asapp.backend.challenge.adapters.storage.IUserRepository;
import com.asapp.backend.challenge.adapters.storage.entity.DbUser;
import com.asapp.backend.challenge.domain.entities.User;
import com.asapp.backend.challenge.domain.useCases.IUserManager;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.net.ConnectException;

public class UserRepository implements IUserRepository {

    private Sql2o sql2o;

    public UserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public User addUser(IUserManager.CreateUserRequest userRequest) throws ConnectException {

        DbUser toInsert = new DbUser(userRequest.getUsername(), userRequest.getPassword());

        int insertedId;
        try (Connection conn = sql2o.open()) {
            insertedId = (int) conn.createQuery("insert into Users(username, password) " +
                    "VALUES (:username, :password)", true)
                    .bind(toInsert)
                    .executeUpdate()
                    .getKey();
        } catch (Exception e) {
            throw new ConnectException();
        }

        return new User(toInsert.getUsername(), toInsert.getPassword(), insertedId);
    }

    @Override
    public User findUserByUsername(String username) throws ConnectException {

        DbUser userFound;
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery("SELECT * FROM main.Users WHERE username == :username")
                    .addParameter("username", username);

            userFound = query.executeAndFetchFirst(DbUser.class);
        } catch (Exception e){
            throw new ConnectException();
        }

        if (userFound == null)
            return null;
        return new User(userFound.getUsername(), userFound.getPassword(), userFound.getId());
    }

    @Override
    public User findUserById(int user) throws ConnectException {
        DbUser userFound;
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery("SELECT * FROM Users WHERE id == :userId")
                    .addParameter("userId", user);

            userFound = query.executeAndFetchFirst(DbUser.class);
        }catch (Exception e){
            throw new ConnectException();
        }

        if (userFound == null)
            return null;
        return new User(userFound.getUsername(), userFound.getPassword(), userFound.getId());
    }
}
