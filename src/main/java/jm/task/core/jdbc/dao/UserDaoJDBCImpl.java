package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlCommand = "CREATE TABLE IF NOT EXISTS Users (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20),lastName VARCHAR(30), age INT)";
        try (Connection connection=Util.getConnection();
             Statement statement=connection.createStatement()){
            statement.executeUpdate(sqlCommand);
            try {
                connection.commit();
                System.out.println("База Данных Была Создана");
            }catch (SQLException e){
                e.printStackTrace();
                connection.rollback();
            }finally {
                if(!connection.isClosed()){
                    connection.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sqlCommand = "DROP TABLE IF EXISTS Users";
        try (Connection connection=Util.getConnection();
             Statement statement=connection.createStatement()){
            statement.executeUpdate(sqlCommand);
            try {
                connection.commit();
            }catch (SQLException e){
                e.printStackTrace();
                connection.rollback();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            System.out.println("База данных успешно удалена");
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        String sql="INSERT INTO Users (name, lastNAME, age) VALUES (?, ?, ?)";
        try (Connection connection=Util.getConnection();
             PreparedStatement preStatement=connection.prepareStatement(sql)){
            preStatement.setString(1, name);
            preStatement.setString(2, lastName);
            preStatement.setByte(3, age);
            preStatement.executeUpdate(); {
               try {
                   connection.commit();
               }catch (SQLException e){
                   connection.rollback();
                   if(!connection.isClosed()){
                       connection.close();
                   }
               }
            }

        }catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            System.out.println(" Пользователь успешно добавлен.\n Имя :"+name+"\n" +
                    " Фамилия: "+lastName+"\n Возраст: "+age);
        }

    }

    public void removeUserById(long id) {

        String sql="DELETE FROM users WHERE id=?";
        try(Connection connection=Util.getConnection();
            PreparedStatement pS=connection.prepareStatement(sql)){
            pS.setLong(1,id);
            pS.executeUpdate();
            try {
                connection.commit();
            }catch (SQLException e){
                e.printStackTrace();
                connection.rollback();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            System.out.println("Пользователь с id = "+id+" удалён");
        }

    }

    public List<User> getAllUsers() {
        List<User>userList=new ArrayList<>();
        String sql="SELECT * FROM mydbtest.users";

        try(Connection connection=Util.getConnection();
            PreparedStatement prestatement=connection.prepareStatement(sql);
            ResultSet resultSet= prestatement.executeQuery(sql)){
            while (resultSet.next()){
                User user=new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
                try {
                    connection.commit();
                }catch (SQLException e){
                    e.printStackTrace();
                    connection.rollback();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return userList;

    }

    public void cleanUsersTable() {
        String sql="TRUNCATE users";
        try(Connection connection=Util.getConnection();
            Statement statement=connection.createStatement()){
            statement.executeUpdate(sql);
            try {
                connection.commit();
            }catch (SQLException e){
                e.printStackTrace();
                connection.rollback();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
