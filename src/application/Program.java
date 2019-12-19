package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
  public static void main(String[] args) {
    SellerDao sellerDao = DaoFactory.createSellerDao();
    System.out.println();
    System.out.println("=== TEST 1: Seller findById ===");
    System.out.println(sellerDao.findById(3));
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 2: Seller findByDepartment ===");
    Department department = new Department(2, null);
    List<Seller> sellers = sellerDao.findByDepartment(department);
    for (Seller seller : sellers)
      System.out.println(seller);
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 3: Seller findAll ===");
    sellers = sellerDao.findAll();
    for (Seller seller : sellers)
      System.out.println(seller);
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 4: Seller insert ===");
    Seller seller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
    sellerDao.insert(seller);
    System.out.println("Inserted! New id = " + seller.getId());
    System.out.println("===============================");

    System.out.println();
    System.out.println("=== TEST 4: Seller update ===");
    seller = sellerDao.findById(1);
    seller.setName("Carol");
    sellerDao.update(seller);
    System.out.println("Update completed");
    System.out.println("===============================");

  }
}
