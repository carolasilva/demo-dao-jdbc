package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJdbc implements DepartmentDao {

  private Connection connection;

  public DepartmentDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Department department) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(
          "INSERT INTO department "
              + "(Name) "
              + "VALUES "
              + "(?)",
          Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, department.getName());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          int id = resultSet.getInt(1);
          department.setId(id);
          DB.closeResultSet(resultSet);
        } else {
          throw new DbException("Unexpected error! No rows affected!");
        }
      }

    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void update(Department department) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(
          "UPDATE department "
              + "SET Name = ? "
              + "WHERE Id = ?"
      );

      preparedStatement.setString(1, department.getName());
      preparedStatement.setInt(2, department.getId());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected < 0)
        throw new DbException("Unexpected error! No rows affected");
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void deleteById(Integer id) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement("DELETE FROM department where Id = ?");
      preparedStatement.setInt(1, id);
      int rows = preparedStatement.executeUpdate();
      if (rows == 0)
        throw new DbException("Nonexistent id!");
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public Department findById(Integer id) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement(
          "SELECT department.* " +
              "FROM department " +
              "WHERE Id = ?"
      );

      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        return instantiateDepartment(resultSet);
      }

      return null;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }

  @Override
  public List<Department> findAll() {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement("SELECT * FROM department ORDER BY Name");
      resultSet = preparedStatement.executeQuery();
      List<Department> departments = new ArrayList<>();

      while (resultSet.next())
        departments.add(instantiateDepartment(resultSet));

      return departments;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();
    department.setId(resultSet.getInt("Id"));
    department.setName(resultSet.getString("Name"));
    return department;
  }
}
