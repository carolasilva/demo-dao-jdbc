package model.dao.impl;

import db.DB;
import db.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


public class SellerDaoJdbc implements SellerDao {

  private Connection connection;

  public SellerDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(
          "INSERT INTO seller "
              + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
              + " VALUES "
              + "(?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          int id = resultSet.getInt(1);
          seller.setId(id);
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
  public void update(Seller seller) {

  }

  @Override
  public void deleteById(Integer id) {

  }

  @Override
  public Seller findById(Integer id) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*, department.Name AS DepName\n"
              + "FROM seller INNER JOIN department\n"
              + "ON seller.DepartmentId = department.Id\n"
              + "WHERE seller.Id = ?");
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
        return instantiateSeller(resultSet, department);
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
  public List<Seller> findByDepartment(Department department) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*, department.Name AS DepName\n"
              + "FROM seller INNER JOIN department\n"
              + "ON seller.DepartmentId = department.Id\n"
              + "WHERE departmentId = ?\n"
              + "ORDER BY Name");

      preparedStatement.setInt(1, department.getId());
      resultSet = preparedStatement.executeQuery();
      List<Seller> sellers = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while (resultSet.next()) {
        Department dep = map.get(resultSet.getInt("DepartmentId"));

        if (dep == null) {
          dep = instantiateDepartment(resultSet);
          map.put(dep.getId(), dep);
        }

        Seller seller = instantiateSeller(resultSet, dep);
        sellers.add(seller);
      }

      return sellers;

    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }

  private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    Seller seller = new Seller();
    seller.setId(resultSet.getInt("Id"));
    seller.setName(resultSet.getString("Name"));
    seller.setEmail(resultSet.getString("Email"));
    seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
    seller.setBirthDate(resultSet.getDate("BirthDate"));
    seller.setDepartment(department);

    return seller;
  }

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();
    department.setId(resultSet.getInt("DepartmentId"));
    department.setName(resultSet.getString("DepName"));
    return department;
  }

  @Override
  public List<Seller> findAll() {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      preparedStatement = connection.prepareStatement(
          "SELECT seller.*, department.Name AS DepName\n"
              + "FROM seller INNER JOIN department\n"
              + "ON seller.DepartmentId = department.Id\n"
              + "ORDER BY Name");

      resultSet = preparedStatement.executeQuery();
      List<Seller> sellers = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while (resultSet.next()) {
        Department dep = map.get(resultSet.getInt("DepartmentId"));

        if (dep == null) {
          dep = instantiateDepartment(resultSet);
          map.put(dep.getId(), dep);
        }

        Seller seller = instantiateSeller(resultSet, dep);
        sellers.add(seller);
      }

      return sellers;

    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }
}
