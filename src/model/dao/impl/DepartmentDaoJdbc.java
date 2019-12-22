package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
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

  }

  @Override
  public void deleteById(Integer id) {

  }

  @Override
  public Department findById(Integer id) {
    return null;
  }

  @Override
  public List<Department> findAll() {
    return null;
  }
}
